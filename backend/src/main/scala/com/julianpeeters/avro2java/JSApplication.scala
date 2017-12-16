package com.julianpeeters.avro2java

import cats.effect.IO
import cats.data._
import org.http4s._
import org.http4s.CacheDirective._
import org.http4s.dsl._
import org.http4s.MediaType._
import org.http4s.headers._
import fs2._
import scalatags.Text.TypedTag
import scalatags.Text.all.Modifier

object JSApplication extends Http4sDsl[IO] {

  val jsScript = "avro2java-frontend-fastopt.js"
  val jsDeps = "avro2java-frontend-jsdeps.js"
  val jsScripts: Seq[Modifier] = {
    import scalatags.Text.all._
    List(
      script(src := jsScript),
      script(src := jsDeps),
      script("com.julianpeeters.avro2java.TutorialApp().main()")
    )
  }

  val index: Seq[Modifier] = {
    import scalatags.Text.all._
    Seq(
      h1(
        style:= "align: center;",
        "Http4s Scala-js Example App"
      ),
      a(href:="/button", h4("Button Example"))
    )
  }

  val buttonTag: Seq[Modifier] = {
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

  val supportedStaticExtensions =
    List(".html", ".js", ".map", ".css", ".png", ".ico")

  val service = HttpService[IO] {

    case req @ GET -> Root =>
      Ok(template(Seq(), index, jsScripts, Seq()).render)
        .withContentType(`Content-Type`(`text/html`, Charset.`UTF-8`))
        .putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`())))

    case req @ GET -> Root / "button" =>
      Ok(template(Seq(), buttonTag, jsScripts, Seq()).render)
        .withContentType(`Content-Type`(`text/html`, Charset.`UTF-8`))
        .putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`())))

    case req if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
      StaticFile
        .fromResource(req.pathInfo, Some(req))
        .map(_.putHeaders())
        .orElse(StaticFile.fromURL(getClass.getResource(req.pathInfo), Some(req)))
        .map(_.putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`()))))
        .getOrElseF(NotFound())

  }

}
