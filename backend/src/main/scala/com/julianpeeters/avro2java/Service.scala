package com.julianpeeters.avro2java

import avrohugger.format.abstractions.JavaTreehugger
import avrohugger.input.parsers.StringInputParser
import avrohugger.stores.{ClassStore, SchemaStore}


import views.Page

import cats.effect.IO
import cats.data._
import io.circe.generic.auto._
import io.circe._
import io.circe.syntax._
import org.http4s._
import org.http4s.CacheDirective._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.MediaType._
import org.http4s.headers._
import scala.collection.JavaConverters._

object Service extends Http4sDsl[IO] {

  val supportedStaticExtensions =
    List(".html", ".js", ".map", ".css", ".png", ".ico")

  val endpoints = HttpService[IO] {

    case req @ GET -> Root =>
      Ok(Page.template(Seq(), Seq(), Scripts.jsScripts, Seq()).render)
        .withContentType(`Content-Type`(`text/html`, Charset.`UTF-8`))
        .putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`())))
        
    case req @ POST -> Root =>
      val inputString: IO[String] = req.as[String] 
      inputString.flatMap(input => {
        implicit val encoder = jsonEncoderOf[IO, List[String]]
        val parser = new StringInputParser
        val schemaStore = new SchemaStore
        val schemaOrProtocols = parser.getSchemaOrProtocols(input, schemaStore)
        val codeStrings: List[String] = schemaOrProtocols.flatMap(_ match {
          case Left(schema) => List(JavaTreehugger.asJavaCodeString(
            new ClassStore,
            None,
            schema))
          case Right(protocol) => protocol.getTypes.asScala.map(JavaTreehugger.asJavaCodeString(
            new ClassStore,
            None,
            _))
        })
          
        Ok(codeStrings)
      })

    case req if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
      StaticFile
        .fromResource(req.pathInfo, Some(req))
        .map(_.putHeaders())
        .orElse(StaticFile.fromURL(getClass.getResource(req.pathInfo), Some(req)))
        .map(_.putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`()))))
        .getOrElseF(NotFound())

  }

}
