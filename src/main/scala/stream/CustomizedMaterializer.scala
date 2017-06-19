package stream

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}

/**
  * Created by shaojie.xu on 16/06/2017.
  */
object CustomizedMaterializer {

  def main(args: Array[String]): Unit = {

    // implicit actor system
    implicit val system = ActorSystem("Sys")
    import system.dispatcher
    val decider: Supervision.Decider = exc => exc match {
      case _: ArithmeticException => Supervision.Resume
      case _ => Supervision.Stop
    }
    // ActorFlowMaterializer takes the list of transformations comprising a akka.stream.scaladsl.Flow
    // and materializes them in the form of org.reactivestreams.Processor
    implicit val mat = ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy(decider))
    val source = Source(0 to 5).map(100 / _)
    source.runWith(Sink.fold(0)(_ + _)).onComplete(t => {
      println(t.get)
      system.terminate()
    })
  }

}
