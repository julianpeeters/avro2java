package com.julianpeeters.avro2java

import cats.effect.IO

import java.util.concurrent.{ExecutorService, Executors}

import scala.util.Properties.envOrNone

import fs2._

import org.http4s.util.StreamApp
import org.http4s.server.blaze.BlazeBuilder

object Server extends StreamApp[IO] {

  val port: Int = envOrNone("HTTP_PORT") map (_.toInt) getOrElse 8080
  val ip: String = "0.0.0.0"
  val pool: ExecutorService = Executors.newCachedThreadPool()

  def stream(
    args: List[String],
    requestShutdown: IO[Unit]): Stream[IO, StreamApp.ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(port, ip)
      .mountService(JSApplication.service)
      .withServiceExecutor(pool)
      .serve
}
