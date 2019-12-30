package main;

import actors.Master;
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

    static final String DATASET_1_PATH = "src/main/resources/dataset-1.json";
    static final String DATASET_2_PATH = "src/main/resources/dataset-2.json";
    static final String DATASET_3_PATH = "src/main/resources/dataset-3.json";

    public static void main(String[] args) throws IOException, JSONException {
        ActorSystem system = ActorSystem.create("main");
        final ActorRef master = system.actorOf(Master.props(), "master");

        String path = new File(DATASET_1_PATH).getAbsolutePath();
        List<CreditCard> cards = getCreditCards(path);

        cards.forEach(card -> master.tell(new Master.Work(card), ActorRef.noSender()));

        System.out.println("ENTER to terminate");
        System.in.read();
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
