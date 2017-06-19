package stream;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;


/**
 * Created by shaojie.xu on 14/06/2017.
 */
public class Stream1 {

    static final ActorSystem system = ActorSystem.create("sys");
    static final Materializer materializer = ActorMaterializer.create(system);

    public static void main(String[] argv) {

        final Source<Integer, NotUsed> source =  Source.range(1, 100);
        final CompletionStage<Done> done = source.runForeach(i -> System.out.println(i), materializer);

        final Source<BigInteger, NotUsed> factorials =
                                                    source.scan(BigInteger.ONE, (acc, next)
                                                            -> acc.add(BigInteger.valueOf(next)));

        final CompletionStage<IOResult> result =
                factorials
                        .map(num -> ByteString.fromString(num.toString() + "\n"))
                        .runWith(FileIO.toPath(Paths.get("factorials.txt")), materializer);

        factorials.map(BigInteger::toString).runWith(lineSink("factorial2.txt"), materializer);


        final Sink<Integer, CompletionStage<Integer>> sumSink = Sink.<Integer, Integer>fold(0, (acc, elem) -> acc + elem);
        final RunnableGraph<CompletionStage<Integer>> counter = source.map(t -> 1).toMat(sumSink, Keep.right());
        CompletionStage<Integer> sum1 = source.map(t -> 1).runWith(sumSink, materializer);
        final CompletionStage<Integer> sum = counter.run(materializer);
        sum.thenAcceptAsync(c -> System.out.println("Total tweets processed: " + c), system.dispatcher());
        System.out.println("finished");


    }


    public static Sink<String, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(String.class)
                    .map(s -> ByteString.fromString(s.toString() + "\n"))
                    .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }
}
