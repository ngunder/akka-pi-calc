package akka.pi.calc;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


class PiMaster extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final int nrOfMessages;
    private final int nrOfElements;

    private double pi;
    private int nrOfResults;
    private final long start = System.currentTimeMillis();

    private final ActorRef listener;
    private final ActorRef workerRouter;

    PiMaster(int nrOfWorkers, int nrOfMessages, int nrOfElements,
           ActorRef listener) {

        this.nrOfMessages = nrOfMessages;
        this.nrOfElements = nrOfElements;
        this.listener = listener;

        workerRouter = this.getContext().actorOf(Props.create(PiWorker.class)
                .withRouter(new RoundRobinPool(nrOfWorkers)), "workerRouter");
    }

    @Override
    public void preStart() {
        log.info("started");
    }
    @Override
    public void postStop() {
        log.info("stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("calculate", s -> {
                    for (int start = 0; start < nrOfMessages; start++) {
                        workerRouter.tell(new Pi.Work(start, nrOfElements), getSelf());
                    }
                })
                .match(Pi.Result.class, result -> {
                    pi += result.getValue();
                    nrOfResults += 1;
                    if (nrOfResults == nrOfMessages) {
                        Duration duration = Duration.create(System.currentTimeMillis() -
                                start, TimeUnit.MILLISECONDS);
                        listener.tell(new Pi.PiApproximation(pi, duration), getSelf());
                    }
                })
                .matchEquals("stop", s -> getContext().getSystem().terminate() )
                .matchAny(o -> log.warning("received unknown message"))
                .build();
    }
}
