package actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import main.CreditCard;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Collector extends AbstractActor {
    // protocol
    public static final class Result {
        public final CreditCard creditCard;
        public final String uselessNumber;
        public Result(CreditCard creditCard, String uselessNumber) {
            this.creditCard = creditCard;
            this.uselessNumber = uselessNumber;
        }
    }

    private PrintWriter writer;

    // open file stream
    @Override
    public void preStart() throws IOException {
        FileWriter fileWriter = new FileWriter("results.txt");
        this.writer = new PrintWriter(fileWriter);
        this.writer.printf(
                "%20s%20s%20s%20s\n\n",
                "Name",
                "CCNumber",
                "Balance",
                "UselessNumber"
        );
    }

    // close file stream
    @Override
    public void postStop() {
        this.writer.close();
        System.out.println("Collector stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Result.class, this::onMessage)
                .build();
    }

    private void onMessage(Result result) {
        Object[] row = new String[] {
                result.creditCard.getName(),
                result.creditCard.getCCnumber(),
                String.valueOf(result.creditCard.getBalance()),
                result.uselessNumber
        };
        this.writer.printf("%20s%20s%20s%20s\n", row);
    }

    // actor factory
    public static Props props() {
        return Props.create(Collector.class);
    }
}
