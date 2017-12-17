package com.julianpeeters.avro2java
package views

import scalatags.Text.TypedTag
import scalatags.Text.all.Modifier

object Index {
  val html: Seq[Modifier] = {
    import scalatags.Text.all._
    Seq(
      h1(
        style:= "align: center;",
        "Http4s Scala-js Example App"
      ),
      a(href:="/button", h4("Button Example"))
    )
  }
}