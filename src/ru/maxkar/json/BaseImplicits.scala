package ru.maxkar.json

import scala.language.implicitConversions

/**
 * Base definition of implicit conversions (for base parsers).
 * This could be inherited by the custom Json DSLs.
 */
trait BaseImplicits {
  self : BaseParsers ⇒

  implicit def json2boolean(x : JsonPhantomValue) = x.as[Boolean]
  implicit def json2string(x : JsonPhantomValue) = x.as[String]
  implicit def json2int(x : JsonPhantomValue) = x.as[Int]
  implicit def json2long(x : JsonPhantomValue) = x.as[Long]
  implicit def json2double(x : JsonPhantomValue) = x.as[Double]
  implicit def json2array(x : JsonPhantomValue) = x.as[JsonArray]
  implicit def json2seq(x : JsonPhantomValue) = x.as[Seq[JsonValue]]
  implicit def json2object(x : JsonPhantomValue) = x.as[JsonObject]
  implicit def json2map(x : JsonPhantomValue) = x.as[Map[String, JsonValue]]
}
