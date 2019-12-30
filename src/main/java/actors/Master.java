package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import main.CreditCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static main.ExamTaskApplication.WORKER_ACTORS_COUNT;

public class Master extends AbstractLoggingActor {

    // protocol
    public static final class Work {
        public final CreditCard creditCard;
        public Work(CreditCard creditCard) {
            this.creditCard = creditCard;
        }
    }

    Router router;
    {
        // create result collector
        final ActorRef collector = getContext().actorOf(Collector.props(), "collector");

        // routing
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < WORKER_ACTORS_COUNT; i++) {
            ActorRef r = getContext().actorOf(Worker.props(collector), "worker-" + i);
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Work.class, message -> {
                    router.route(message, self());})
                .build();
    }

    // actor factory
    public static Props props() {
        return Props.create(Master.class);
    }
}

