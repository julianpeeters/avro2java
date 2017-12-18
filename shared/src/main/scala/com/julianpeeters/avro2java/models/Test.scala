package com.julianpeeters.avro2java
package models

import io.circe.{Decoder, Encoder, _}
import io.circe.generic.semiauto._

case class Test(x: String)
object Test {
  implicit val decoder: Decoder[Test] = deriveDecoder
  implicit val encoder: Encoder[Test] = deriveEncoder
}