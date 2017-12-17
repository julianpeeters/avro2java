package com.julianpeeters.avro2java.controller

import fs2._
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSApp

import Implicits._
import com.julianpeeters.avro2java.controller.pipes.Pipes
import com.julianpeeters.avro2java.views.SubmissionPage

@JSExportTopLevel("Avro2JavaApp")
object Avro2JavaApp extends JSApp {
  
  def main(): Unit = {
    val program: Task[Unit] = for {
      input <- SubmissionPage.create
      _     <- streamEvents(input.button).through {
                 import Pipes._
                 post(input.textarea) andThen handleResponse andThen appendResults
               }.run
    } yield ()
    
    program.unsafeRunAsync(_ => ())
  }
}
