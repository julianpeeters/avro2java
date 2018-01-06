package com.julianpeeters.avro2java.controller

import cats.effect.IO
import fs2._
import org.scalajs.dom
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSApp

import Implicits._
import com.julianpeeters.avro2java.controller.pipes.Pipes
import com.julianpeeters.avro2java.views._


@JSExportTopLevel("Avro2JavaApp")
object Avro2JavaApp extends JSApp {

  def main(): Unit = {
    import Pipes._
    val program: IO[Unit] = for {
      _ <- SubmissionPage.append
      _ <- streamEvents(SubmissionPage.submitButton).through {
             post(SubmissionPage.input) andThen handleResponse andThen appendResult
           }.run
    } yield ()
    
    program.unsafeRunAsync(_ => ())
  }
}
