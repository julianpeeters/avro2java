package com.julianpeeters.avro2java
package views

import scalatags.Text.TypedTag
import scalatags.Text.all.Modifier

object Page {

  def template(
      headContent: Seq[Modifier],
      bodyContent: Seq[Modifier],
      scripts: Seq[Modifier],
      css: Seq[Modifier]): TypedTag[String] = {
    import scalatags.Text.all._

    html(
      head(
        headContent
      ),
      body(
        bodyContent,
        scripts
      )
    )

  }

}