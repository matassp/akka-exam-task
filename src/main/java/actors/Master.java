package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import main.CreditCard;

public class Master extends AbstractLoggingActor {
    // protocol
    public static class Work {
        private CreditCard creditCard;
        public Work(CreditCard creditCard) {
            this.creditCard = creditCard;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Work.class, this::onMessage)
                .build();
    }

    private void onMessage(Work work) {
        log().info("Received " + work.creditCard.getName());
    }

    public static Props props() {
        return Props.create(Master.class);
    }
}

