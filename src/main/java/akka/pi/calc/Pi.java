package akka.pi.calc;

import akka.actor.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;


public class Pi{

    public static void main(String[] args) {
        Pi pi = new Pi();
        pi.calculate(4, 10000, 10000);
    }

    static class Work {
        private final int start;
        private final int nrOfElements;

        Work(int start, int nrOfElements) {
            this.start = start;
            this.nrOfElements = nrOfElements;
        }

        int getStart() {
            return start;
        }

        int getNrOfElements() {
            return nrOfElements;
        }
    }

    static class Result {
        private final double value;

        Result(double value) {
            this.value = value;
        }

        double getValue() {
            return value;
        }
    }

    static class PiApproximation {
        private final double pi;
        private final Duration duration;

        PiApproximation(double pi, Duration duration) {
            this.pi = pi;
            this.duration = duration;
        }

        double getPi() {
            return pi;
        }

        Duration getDuration() {
            return duration;
        }
    }


    private void calculate(final int nrOfWorkers,
                           final int nrOfElements,
                           final int nrOfMessages) {

        Config conf = ConfigFactory.load();
        ActorSystem system = ActorSystem.create("PiSystem", conf);
        Props listener_props = Props.create(PiListener.class);
        final ActorRef listener = system.actorOf(listener_props, "listener_actor");

        Props master_props = Props.create(
                PiMaster.class, nrOfWorkers, nrOfMessages, nrOfElements, listener);
        final ActorRef master = system.actorOf(master_props, "master_actor");

        master.tell("calculate", null);
    }
}        
  
