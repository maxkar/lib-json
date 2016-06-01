package ru.maxkar.json

import scala.language.implicitConversions

/**
 * Implicits for the optional conversions.
 */
trait OptImplicits {
  self : OptParsers ⇒

  implicit def json2OptBoolean(x : JsonValue) = x.as[Option[Boolean]]
  implicit def json2OptString(x : JsonValue) = x.as[Option[String]]
  implicit def json2OptInt(x : JsonValue) = x.as[Option[Int]]
  implicit def json2OptLong(x : JsonValue) = x.as[Option[Long]]
  implicit def json2OptDouble(x : JsonValue) = x.as[Option[Double]]
  implicit def json2OptArray(x : JsonValue) = x.as[Option[JsonArray]]
  implicit def json2OptSeq(x : JsonValue) = x.as[Option[Seq[JsonValue]]]
  implicit def json2OptObject(x : JsonValue) = x.as[Option[JsonObject]]
  implicit def json2OptMap(x : JsonValue) = x.as[Option[Map[String, JsonValue]]]
}
