package ru.maxkar.json

import scala.language.implicitConversions

/**
 * Implicits for the optional conversions.
 */
trait OptImplicits {
  self : OptParsers ⇒

  implicit def json2OptBoolean(x : JsonPhantomValue) = x.as[Option[Boolean]]
  implicit def json2OptString(x : JsonPhantomValue) = x.as[Option[String]]
  implicit def json2OptInt(x : JsonPhantomValue) = x.as[Option[Int]]
  implicit def json2OptLong(x : JsonPhantomValue) = x.as[Option[Long]]
  implicit def json2OptDouble(x : JsonPhantomValue) = x.as[Option[Double]]
  implicit def json2OptArray(x : JsonPhantomValue) = x.as[Option[JsonArray]]
  implicit def json2OptSeq(x : JsonPhantomValue) = x.as[Option[Seq[JsonValue]]]
  implicit def json2OptObject(x : JsonPhantomValue) = x.as[Option[JsonObject]]
  implicit def json2OptMap(x : JsonPhantomValue) = x.as[Option[Map[String, JsonValue]]]
}
