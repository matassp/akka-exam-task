package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import main.CreditCard;

public class Collector extends AbstractLoggingActor {
    // protocol
    public static final class Result {
        public final CreditCard creditCard;
        public final String uselessNumber;
        public Result(CreditCard creditCard, String uselessNumber) {
            this.creditCard = creditCard;
            this.uselessNumber = uselessNumber;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Result.class, this::onMessage)
                .build();
    }

    private void onMessage(Result result) {
        System.out.println(result.uselessNumber);
    }

    // actor factory
    public static Props props() {
        return Props.create(Collector.class);
    }
}
