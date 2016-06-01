package ru.maxkar.json

import scala.language.implicitConversions

/**
 * Base definition of implicit conversions (for base parsers).
 * This could be inherited by the custom Json DSLs.
 */
trait BaseImplicits {
  self : BaseParsers ⇒

  implicit def json2boolean(x : JsonValue) = x.as[Boolean]
  implicit def json2string(x : JsonValue) = x.as[String]
  implicit def json2int(x : JsonValue) = x.as[Int]
  implicit def json2long(x : JsonValue) = x.as[Long]
  implicit def json2double(x : JsonValue) = x.as[Double]
  implicit def json2array(x : JsonValue) = x.as[JsonArray]
  implicit def json2seq(x : JsonValue) = x.as[Seq[JsonValue]]
  implicit def json2object(x : JsonValue) = x.as[JsonObject]
  implicit def json2map(x : JsonValue) = x.as[Map[String, JsonValue]]
}
