package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import main.CreditCard;
import utils.BogoSort;

import static main.ExamTaskApplication.USELESS_NUMBER_LENGTH;

public class Worker extends AbstractLoggingActor {
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

    // does heavy computations
    private void onMessage(Master.Work work) {
        System.out.println("Received " + getSelf().path().name());
        int[] array = work
                .creditCard
                .getArrayOf(USELESS_NUMBER_LENGTH);
        int[] sortedArray = BogoSort.sort(array);
        StringBuilder str = new StringBuilder();
        for (int digit : sortedArray)
            str.append(digit);
        String uselessNumber = str.toString();
        Collector.Result result = new Collector.Result(work.creditCard, uselessNumber);
        collector.tell(result, self());
        System.out.println("Done " + getSelf().path().name());
    }

    // actor factory with an argument
    public static Props props(ActorRef collector) {
        return Props.create(Worker.class, collector);
    }
}
