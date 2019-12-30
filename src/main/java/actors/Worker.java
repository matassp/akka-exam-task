package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import utils.BogoSort;

public class Worker extends AbstractLoggingActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Master.Work.class, this::onWork)
                .build();
    }

    private void onWork(Master.Work work) throws InterruptedException {
        log().info("Working on " + work.creditCard.getName());
        int[] array = work.creditCard.getArrayOf(4);
        int[] sortedArray = BogoSort.sort(array);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < sortedArray.length; i++) {
            str.append(sortedArray[i]);
        }
        log().info("Done " + str);
    }

    public static Props props() {
        return Props.create(Worker.class);
    }
}
