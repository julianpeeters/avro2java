package com.julianpeeters.avro2java.views

import fs2._
import org.scalajs.dom
import scalatags.JsDom.all._

import com.julianpeeters.avro2java.models.InputElements

object SubmissionPage {
  
  val submitButton = button("submit").render
  val textArea = textarea(`type`:="text", id:="input-textarea", "world").render
  
  val content =
    div(id:="submission-page",
      div(id:="submission",
        div(id:="textarea", textArea),
        submitButton),
      div(id:="results")).render
      
  def create: Task[InputElements] = Task.delay {
    dom.document.body.appendChild(content)
    InputElements(textArea, submitButton)
  }
  
  def updateResults(result: String): Task[dom.raw.Node] = Task.delay {
    val submissionPage = dom.document.getElementById("submission-page")
    val oldResults = dom.document.getElementById("results").render
    val newResults = div(id:="results", result).render
    submissionPage.replaceChild(newResults, oldResults)
  }
}