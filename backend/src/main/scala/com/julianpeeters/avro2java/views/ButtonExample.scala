package com.julianpeeters.avro2java
package views

import scalatags.Text.TypedTag
import scalatags.Text.all.Modifier

object ButtonExample {
  val html: Seq[Modifier] = {
    import scalatags.Text.all._
    Seq(
      h1("Push The Button"),
      a(href:="/", h4("Home")),
      button(
        id := "click-me-button",
        `type` := "button",
        onclick := "addClickedMessage()",
        style := "background-color: #4CAF50; /* Green */ " +
          "border: none; " +
          "border-radius: 12px; " +
          "color: white; " +
          "padding: 15px 32px; " +
          "text-align: center; " +
          "text-decoration: none; " +
          "display: inline-block; " +
          "font-size: 16px;",
        "Click Me"
      )
    )
  }
}