package com.julianpeeters.avro2java
package models

import io.circe.syntax._
import io.circe.{Decoder, Encoder, _}
import io.circe.generic.semiauto._

case class Avro2JavaResult(results: List[String])
//  object Avro2JavaResult {
//   implicit val encodeList: Encoder[List[String]] = Encoder.encodeList[String]
//   implicit val resultDecoder: Decoder[Avro2JavaResult] = deriveDecoder[Avro2JavaResult]
//   implicit val resultEncoder: Encoder[Avro2JavaResult] = Encoder.instance {
//       case Avro2JavaResult(rs) => Json.obj("results" -> Json.arr(rs.map(_.asJson):_*))
//     }
//     //deriveEncoder[Avro2JavaResult]
//     //  Encoder.forProduct2("input", "results")(u =>
//     //    (u.input, u.results))
// }