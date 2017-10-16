## Synopsis
A Port of the Akka 2.0.2 Getting started example to 2.5.6

## Motivation
I liked the example from Akka 2.0.2, so I decided to port that to Akka 2.5.6 with use of AbstractActor and Lambdas.

The main changes are to replace the use of UntypedActor which is now depreciated. I have also updated the use of RoundRobinRouter with RoundRobinPool. I also tried using the akka.event.* logging packages, mainly for learning if they can be used in the way I intended.

To learn a bit more about Akka, which I know nothing about, and also to come back to Java coding after a very long pause. Last time I coded Java was in 98, so this will be an interesting first project.

Maybe this was done already, but I thought it would be fun to learn it this way versus just going though the working code in the new examples. Plus, I did not think the new example of IoTs was interesting enough for me.

## Old Example from 2.0.2
https://doc.akka.io/docs/akka/2.0.2/intro/getting-started-first-java.html

## To Run
```
mvn compile
mvn exec:java -Dexec.mainClass="akka.pi.calc.Pi"
```

