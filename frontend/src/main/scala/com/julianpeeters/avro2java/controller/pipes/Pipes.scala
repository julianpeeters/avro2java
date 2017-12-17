package com.julianpeeters.avro2java.controller.pipes

import fs2._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scalajs.concurrent.JSExecutionContext.Implicits.queue

import com.julianpeeters.avro2java.views.SubmissionPage

object Pipes {
  
  implicit val strategy = Strategy.default

  def appendResults: Pipe[Task, String, dom.raw.Node] =
    _.evalMap(result => SubmissionPage.updateResults(result))
    
  def handleResponse: Pipe[Task, dom.XMLHttpRequest, String] =
    _.evalMap(response => Task.delay(response.responseText))
    
  def post(textarea: dom.html.TextArea): Pipe[Task, dom.Event, dom.XMLHttpRequest] = {
    _.evalMap {
      case me: dom.MouseEvent =>
        Task.fromFuture(Ajax.post("/", s"""{"x":"${textarea.value}"}"""))
      // TODO: better way to handle missed/incorrect event
      case _ =>
        Task.fromFuture(Ajax.post("/", "no data"))
    }
  }
}