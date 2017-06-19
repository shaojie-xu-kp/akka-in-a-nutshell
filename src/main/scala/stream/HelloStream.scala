package stream

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}

import scala.concurrent.Future

/**
  * Created by shaojie.xu on 16/06/2017.
  */
object HelloStream extends App {

  implicit val actorSystem = ActorSystem("akka-streams-example")
  implicit val materializer = ActorMaterializer()



  val helloSource : Source[String, NotUsed]= Source.single("Hello world")
  val upperCaseFlow : Flow[String, String, NotUsed] = Flow[String].map(s => s.toUpperCase())
  val printSink : Sink[String, Future[Done]]= Sink.foreach(println)
  val helloWorldStream: RunnableGraph[NotUsed] = helloSource via upperCaseFlow to printSink

  helloWorldStream.run()

}
