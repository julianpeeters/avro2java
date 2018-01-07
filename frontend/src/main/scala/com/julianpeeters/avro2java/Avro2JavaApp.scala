package com.julianpeeters.avro2java

import com.julianpeeters.avro2java.components.AppComponent

import org.scalajs.dom
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.JSApp

@JSExportTopLevel("Avro2JavaApp")
object Avro2JavaApp extends JSApp {

  def main(): Unit = {
    val page = AppComponent.component()
    page.renderIntoDOM(dom.window.document.getElementById("app-hook"))
  }

}
