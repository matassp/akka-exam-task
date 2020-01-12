package actors;

import akka.actor.*;
import akka.pattern.Patterns;
import akka.routing.*;
import main.CreditCard;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static main.ExamTaskApplication.WORKER_COUNT;

public class Master extends AbstractActor {

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
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Work.class, message -> {
                    router.tell(message, getSelf());
                })
                .matchEquals("kill", message -> {
                    terminate();
                })
                .build();
    }

    private void terminate() throws ExecutionException, InterruptedException {
        // stopping all workers using PoisonPill broadcast
        CompletableFuture<Boolean> workersFuture = Patterns.gracefulStop(
                router,
                Duration.ofSeconds(60),
                new Broadcast(PoisonPill.getInstance())
        ).toCompletableFuture();
        // waiting for workers to finish
        workersFuture.get();

        CompletableFuture<Boolean> collectorFuture = Patterns.gracefulStop(
                collector,
                Duration.ofSeconds(60)
        ).toCompletableFuture();
        // waiting for collector to finish
        collectorFuture.get();

        // terminating self
        context().stop(getSelf());
    }

    @Override
    public void postStop() {
        System.out.println("Master stopped");
    }

    // actor factory
    public static Props props() {
        return Props.create(Master.class);
    }
}

