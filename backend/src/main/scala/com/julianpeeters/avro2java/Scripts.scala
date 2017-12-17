package com.julianpeeters.avro2java

import scalatags.Text.all.Modifier

object Scripts {
  val jsScript = "avro2java-frontend-fastopt.js"
  val jsDeps = "avro2java-frontend-jsdeps.js"
  val jsScripts: Seq[Modifier] = {
    import scalatags.Text.all._
    List(
      script(src := jsScript),
      script(src := jsDeps),
      script("com.julianpeeters.avro2java.controller.Avro2JavaApp().main()")
    )
  }
}