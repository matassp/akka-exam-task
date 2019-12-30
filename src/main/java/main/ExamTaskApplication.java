package main;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.io.StdIn;

import java.io.IOException;

public class ExamTaskApplication {

    static class Counter extends AbstractLoggingActor {
        // protocol
        static class Message {}

        private int counter = 0;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(Message.class, this::onMessage)
                    .build();
        }

        private void onMessage(Message message) {
            counter++;
            log().info("Increase counter " + counter);
        }

        public static Props props() {
            return Props.create(Counter.class);
        }
    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("sample");

        final ActorRef counter = system.actorOf(Counter.props(), "counter");
        counter.tell(new Counter.Message(), ActorRef.noSender());

        System.out.println("ENTER to terminate");
        System.in.read();
    }
}
