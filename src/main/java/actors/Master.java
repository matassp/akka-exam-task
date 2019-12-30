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

public class Master extends AbstractLoggingActor {
    // protocol
    public static final class Work implements Serializable {
        private static final long serialVersionUID = 1L;
        public final CreditCard creditCard;

        public Work(CreditCard creditCard) {
            this.creditCard = creditCard;
        }
    }

    Router router;
    {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < 5; i++) {
            ActorRef r = getContext().actorOf(Props.create(Worker.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Work.class, message -> router.route(message, getSender()))
                .build();
    }

    public static Props props() {
        return Props.create(Master.class);
    }
}

