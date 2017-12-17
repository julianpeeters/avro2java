package com.julianpeeters.avro2java

import models.Test
import views.{Index, Page}

import cats.effect.IO
import cats._, cats.implicits._
import cats.data._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.CacheDirective._
import org.http4s.dsl._
import org.http4s.MediaType._
import org.http4s.headers._
import fs2._

import org.http4s.circe._

object Service extends Http4sDsl[IO] {

  val supportedStaticExtensions =
    List(".html", ".js", ".map", ".css", ".png", ".ico")

  val endpoints = HttpService[IO] {

    case req @ GET -> Root =>
      Ok(Page.template(Seq(), Index.html, Scripts.jsScripts, Seq()).render)
        .withContentType(`Content-Type`(`text/html`, Charset.`UTF-8`))
        .putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`())))
        
    case req @ POST -> Root =>
      implicit val decoder = jsonOf[IO, Test]
      req.as[Test].flatMap(t => Ok(s"hello, ${t.x}"))

    case req if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
      StaticFile
        .fromResource(req.pathInfo, Some(req))
        .map(_.putHeaders())
        .orElse(StaticFile.fromURL(getClass.getResource(req.pathInfo), Some(req)))
        .map(_.putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`()))))
        .getOrElseF(NotFound())

  }

}
