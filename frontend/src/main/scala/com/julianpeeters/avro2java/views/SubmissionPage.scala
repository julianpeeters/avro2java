package com.julianpeeters.avro2java.views

import cats.effect.IO
import fs2._
import org.scalajs.dom
import scalatags.JsDom.all._
import scalatags.Text.TypedTag

object SubmissionPage {
  
  val submitButton = button("submit").render
  
  val input: dom.html.TextArea = textarea(
    `type`:="text",
    id:="input-textarea",
    placeholder:="paste your schema, idl, protocol, or case class here").render
    
  val result: dom.html.TextArea = textarea(
    `type`:="text",
    id:="result-textarea",
    placeholder:="here is the result").render
    
  val content: dom.html.Div =
    div(id:="submission-page",
      div(id:="submission",
        div(id:="input-text-area", input),
        submitButton),
      div(id:="results",
        div(id:="result-text-area", result))).render
        
  def append: IO[dom.raw.Node] = IO {
    dom.document.body.appendChild(this.content)
  }
  
  def updateResult(resultStrings: List[String]): IO[dom.raw.Node] = IO {
    val submissionPage = dom.document.getElementById("submission-page")
    val oldResults = dom.document.getElementById("results").render
    val newResults = div(id:="results", resultStrings.zipWithIndex.map {
        case (resultElement, i) => {
          div(textarea(
          `type`:="text",
          id:="result-textarea-" + i,
          placeholder:="here is the result",
          resultElement))}}).render
    submissionPage.replaceChild(newResults, oldResults)
  }
}