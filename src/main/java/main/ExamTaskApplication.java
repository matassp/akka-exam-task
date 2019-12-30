package main;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import scala.io.StdIn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExamTaskApplication {

//    static class Counter extends AbstractLoggingActor {
//        // protocol
//        static class Message {}
//
//        private int counter = 0;
//
//        @Override
//        public Receive createReceive() {
//            return receiveBuilder()
//                    .match(Message.class, this::onMessage)
//                    .build();
//        }
//
//        private void onMessage(Message message) {
//            counter++;
//            log().info("Increase counter " + counter);
//        }
//
//        public static Props props() {
//            return Props.create(Counter.class);
//        }
//    }

    public static void main(String[] args) throws IOException, JSONException {
//        ActorSystem system = ActorSystem.create("sample");
//
//        final ActorRef counter = system.actorOf(Counter.props(), "counter");
//        counter.tell(new Counter.Message(), ActorRef.noSender());
//
//        System.out.println("ENTER to terminate");
//        System.in.read();
        String path = new File("src/main/resources/dataset-1.json").getAbsolutePath();
        List<CreditCard> cards = getCreditCards(path);
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static List<CreditCard> getCreditCards(String path) throws JSONException, IOException {
        String jsonString = readFile(path, StandardCharsets.US_ASCII);
        JSONArray array = new JSONArray(jsonString);
        List<CreditCard> cards = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String name = array.getJSONObject(i).getString("Name");
            String number = array.getJSONObject(i).getString("CCNumber");
            double balance = array.getJSONObject(i).getDouble("Balance");
            cards.add(new CreditCard(name, number, balance));
        }
        return cards;
    }
}
