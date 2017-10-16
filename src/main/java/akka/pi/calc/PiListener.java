package akka.pi.calc;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

class PiListener extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

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
                .match(Pi.PiApproximation.class, approximation -> {
                    log.info("Pi approximation: {}", approximation.getPi());
                    log.info("Calculation Time: {}", approximation.getDuration());
                    getSender().tell("stop", getSelf());
                    getContext().stop(getSelf());
                })
                .matchAny(o -> log.warning("received unknown message"))
                .build();
    }
}
