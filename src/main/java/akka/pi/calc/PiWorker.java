package akka.pi.calc;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

class PiWorker extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Pi.Work.class, work -> {
                    double result = calculatePiFor(work.getStart(), work.getNrOfElements());
                    getSender().tell(new Pi.Result(result), getSelf());
                })
                .matchAny(o -> log.warning("received unknown message"))
                .build();
    }

    @Override
    public void preStart() {
        log.info("started");
    }
    @Override
    public void postStop() {
        log.info("stopped");
    }

    private double calculatePiFor(int start, int nrOfElements) {
        double acc = 0.0;
        for (int i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1);
             i++) {
            acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
        }
        return acc;
    }
}
