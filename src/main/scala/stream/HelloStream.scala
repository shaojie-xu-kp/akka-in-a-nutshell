package stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}

/**
  * Created by shaojie.xu on 16/06/2017.
  */
object HelloStream extends App {

  implicit val actorSystem = ActorSystem("akka-streams-example")
  implicit val materializer = ActorMaterializer()


  val helloWorldStream: RunnableGraph[NotUsed] = Source.single("Hello world")
                                                  .via(Flow[String].map(s => s.toUpperCase()))
                                                  .to(Sink.foreach(println))
  helloWorldStream.run()

}
