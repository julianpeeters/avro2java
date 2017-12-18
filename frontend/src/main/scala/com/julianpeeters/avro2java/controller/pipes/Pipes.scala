package com.julianpeeters.avro2java
package controller
package pipes

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._

import fs2._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scalajs.concurrent.JSExecutionContext.Implicits.queue

import com.julianpeeters.avro2java.views.SubmissionPage
import com.julianpeeters.avro2java.models.Test

object Pipes {
  
  implicit val strategy = Strategy.default

  def appendResult: Pipe[Task, Either[io.circe.Error, List[String]], dom.raw.Node] =
    _.evalMap(resultsOrError => resultsOrError match {
      case Right(results) => SubmissionPage.updateResult(results)
      case Left(error) => SubmissionPage.updateResult(List.empty)
    })
    
  def handleResponse: Pipe[Task, dom.XMLHttpRequest, Either[io.circe.Error,List[String]]] =
      _.evalMap(response => Task.delay(decode[List[String]](response.responseText)))
  
  def post(textarea: dom.html.TextArea): Pipe[Task, dom.Event, dom.XMLHttpRequest] = {
    _.evalMap {
      case me: dom.MouseEvent => Task.fromFuture(Ajax.post("/", textarea.value))
      // TODO: better way to handle missed/incorrect event
      case _ => Task.fromFuture(Ajax.post("/", "no data"))
    }
  }
}