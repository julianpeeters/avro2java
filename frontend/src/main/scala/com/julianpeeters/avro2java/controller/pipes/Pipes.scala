package com.julianpeeters.avro2java
package controller
package pipes

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._

import cats.effect.IO
import cats.Eval
import fs2._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.concurrent.ExecutionContext
import scalajs.concurrent.JSExecutionContext.Implicits.queue

import com.julianpeeters.avro2java.views.SubmissionPage
import com.julianpeeters.avro2java.models.Test

object Pipes {
  
  implicit val strategy = ExecutionContext.Implicits.global

  def appendResult: Pipe[IO, Either[io.circe.Error, List[String]], dom.raw.Node] =
    _.evalMap(resultsOrError => resultsOrError match {
      case Right(results) => SubmissionPage.updateResult(results)
      case Left(error) => SubmissionPage.updateResult(List.empty)
    })
    
  def handleResponse: Pipe[IO, dom.XMLHttpRequest, Either[io.circe.Error,List[String]]] =
      _.evalMap(response => IO(decode[List[String]](response.responseText)))
  
  def post(textarea: dom.html.TextArea): Pipe[IO, dom.Event, dom.XMLHttpRequest] = {
    _.evalMap {
      case me: dom.MouseEvent =>
        IO.fromFuture(IO(Ajax.post("/", textarea.value)))
      // TODO: better way to handle missed/incorrect event
      case _ =>
        IO.fromFuture(IO(Ajax.post("/", "no data")))
    }
  }
}