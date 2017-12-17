package com.julianpeeters.avro2java
package views

import scalatags.Text.TypedTag
import scalatags.Text.all.Modifier

object Index {
  val html: Seq[Modifier] = {
    import scalatags.Text.all._
    val appHook = Seq(div(id:="app"))
    appHook
  }
}