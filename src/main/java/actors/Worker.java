package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import utils.BogoSort;

import static main.ExamTaskApplication.USELESS_NUMBER_LENGTH;

public class Worker extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Master.Work.class, this::onMessage)
                .build();
    }

    public Worker(ActorRef collector) {
        this.collector = collector;
    }

    private final ActorRef collector;

    // sorts first N credit card numbers using bogo sort
    // filters out sorted numbers that start with 0
    private void onMessage(Master.Work work) {
        int[] array = work
                .creditCard
                .getArrayOf(USELESS_NUMBER_LENGTH);
        int[] sortedArray = BogoSort.sort(array);
        StringBuilder str = new StringBuilder();
        for (int digit : sortedArray)
            str.append(digit);
        String uselessNumber = str.toString();

        // filtering out numbers that start with 0
        if (uselessNumber.charAt(0) == '0')
            return;
        Collector.Result result = new Collector.Result(work.creditCard, uselessNumber);
        collector.tell(result, getSelf());
    }

    @Override
    public void postStop() {
        System.out.printf("Worker %s stopped\n", getContext().getSelf().path().name());
    }

    // actor factory with an argument
    public static Props props(ActorRef collector) {
        return Props.create(Worker.class, collector).withDispatcher("my-dispatcher");
    }
}
