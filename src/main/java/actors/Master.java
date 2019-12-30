package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.routing.*;
import main.CreditCard;

import static main.ExamTaskApplication.WORKER_COUNT;

public class Master extends AbstractLoggingActor {

    // protocol
    public static final class Work {
        public final CreditCard creditCard;
        public Work(CreditCard creditCard) {
            this.creditCard = creditCard;
        }
    }

    ActorRef collector;
    ActorRef router;
    {
        // create result collector
        collector = getContext().actorOf(Collector.props(), "collector");
        // create router as a pool
        router = getContext().actorOf(new RoundRobinPool(WORKER_COUNT).props(Worker.props(collector)), "router");
        //router = getContext().actorOf(new SmallestMailboxPool(WORKER_COUNT).props(Worker.props(collector)), "router");
        //router = getContext().actorOf(new BalancingPool(WORKER_COUNT).props(Worker.props(collector)), "router");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Work.class, message -> {
                    router.tell(message, getSelf());
                })
                .match(String.class, m -> {
                    router.tell(new Broadcast(PoisonPill.getInstance()), getSelf());
                })
                .build();
    }

    // actor factory
    public static Props props() {
        return Props.create(Master.class);
    }
}

