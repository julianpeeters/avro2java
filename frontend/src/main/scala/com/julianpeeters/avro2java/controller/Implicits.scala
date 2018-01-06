package com.julianpeeters.avro2java.controller

import cats.effect.IO
import fs2._
import fs2.async.mutable.Queue
import org.scalajs.dom
import scala.concurrent.ExecutionContext
//import scala.concurrent.ExecutionContext.Implicits.global._
import scala.scalajs.js

object Implicits {
  implicit class ElementSyntax(val element: dom.raw.Element) extends AnyVal {
    private def makeQueue(eventType: String)(implicit EC: ExecutionContext) = {
      def clickEventListener(queue: Queue[IO, dom.Event]): js.Function1[dom.Event, Unit] = { evt: dom.Event =>
        queue.enqueue1(evt).unsafeRunAsync(_ => ())
      }

      def createQueue = {
        for {
          queue <- async.unboundedQueue[IO, dom.Event]
          listenerFn = clickEventListener(queue)
          _ <- IO(element.addEventListener(eventType, listenerFn))
        } yield (queue, listenerFn)
      }

      def emitQueue(queue: Queue[IO, dom.Event], listenerFn: js.Function1[dom.Event, Unit]) =
        Stream.emit(queue).covary[IO]

      def cleanupQueue(queue: Queue[IO, dom.Event], listenerFn: js.Function1[dom.Event, Unit]) =
        IO(element.removeEventListener(eventType, listenerFn))

      Stream.bracket(createQueue)((emitQueue _).tupled, (cleanupQueue _).tupled).covary[IO]
    }

    def stream(eventType: String)(implicit S: ExecutionContext): Stream[IO, dom.Event] =
      makeQueue(eventType).flatMap(_.dequeueAvailable)
    
  }  
    
  def streamEvents(
    element: dom.raw.Element,
    eventType: String = "click", // default event is a single click
    n: Int = 1): Stream[IO, dom.Event] = {
    implicit val strategy = ExecutionContext.Implicits.global
    val eventStream: Stream[IO, dom.Event] = element.stream(eventType)
    eventStream.take(n)
  }
  
}