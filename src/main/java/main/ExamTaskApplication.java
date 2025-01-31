package main;

import actors.Master;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExamTaskApplication {
    public static final String DATASET_1_PATH = "src/main/resources/dataset-1.json";
    public static final String DATASET_2_PATH = "src/main/resources/dataset-2.json";
    public static final String DATASET_3_PATH = "src/main/resources/dataset-3.json";
    public static final int WORKER_COUNT = 4;
    public static final int USELESS_NUMBER_LENGTH = 8;

    public static void main(String[] args) throws IOException, JSONException {
        // create actor system and master actor
        ActorSystem system = ActorSystem.create("main");
        final ActorRef master = system.actorOf(Master.props(), "master");

        // read credit cards and send them to master actor
        List<CreditCard> creditCards = getCreditCards(DATASET_1_PATH);

        creditCards.forEach(card -> master.tell(new Master.Work(card), ActorRef.noSender()));
        master.tell("kill", ActorRef.noSender());
        system.terminate();
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static List<CreditCard> getCreditCards(String relativePath) throws JSONException, IOException {
        String path = new File(relativePath).getAbsolutePath();
        String jsonString = readFile(path, StandardCharsets.US_ASCII);

        JSONArray array = new JSONArray(jsonString);
        List<CreditCard> cards = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String name = array
                    .getJSONObject(i)
                    .getString("Name");
            String number = array
                    .getJSONObject(i)
                    .getString("CCNumber");
            double balance = array
                    .getJSONObject(i)
                    .getDouble("Balance");
            CreditCard newCard = new CreditCard(name, number, balance);
            cards.add(newCard);
        }

        return cards;
    }
}
