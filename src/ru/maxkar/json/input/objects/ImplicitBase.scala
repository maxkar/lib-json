package ru.maxkar.json.input.objects

import scala.language.implicitConversions

/** Injectable default implicit conversions. May be mixed in into
 * application-specific DSLs.
 */
trait ImplicitBase {
  /** Converts dynamic json value into an integer. */
  @inline
  implicit def asInt(value : JsonValue) : Int =
    value.asInt

  /** Converts dynamic json value into a long. */
  @inline
  implicit def asLong(value : JsonValue) : Long =
    value.asLong

  /** Converts dynamic json value into a double. */
  @inline
  implicit def asDouble(value : JsonValue) : Double =
    value.asDouble

  /** Converts dynamic json value into a string. */
  @inline
  implicit def asString(value : JsonValue) : String =
    value.asString

  /** Converts dynamic json value into a boolean. */
  @inline
  implicit def asBoolean(value : JsonValue) : Boolean =
    value.asBoolean

  /** Converts dynamic json value into an object. */
  @inline
  implicit def asObject(value : JsonValue) : JsonObject =
    value.asObject

  /** Converts dynamic json value into an array. */
  @inline
  implicit def asArray(value : JsonValue) : JsonArray =
    value.asArray



  /** Converts dynamic json value into an optional integer. */
  @inline
  implicit def asOptInt(value : JsonValue) : Option[Int] =
    value.asOptInt

  /** Converts dynamic json value into an optional long. */
  @inline
  implicit def asOptLong(value : JsonValue) : Option[Long] =
    value.asOptLong

  /** Converts dynamic json value into an optional double. */
  @inline
  implicit def asOptDouble(value : JsonValue) : Option[Double] =
    value.asOptDouble

  /** Converts dynamic json value into an optional string. */
  @inline
  implicit def asOptString(value : JsonValue) : Option[String] =
    value.asOptString

  /** Converts dynamic json value into an optional boolean. */
  @inline
  implicit def asOptBoolean(value : JsonValue) : Option[Boolean] =
    value.asOptBoolean

  /** Converts dynamic json value into an optional object. */
  @inline
  implicit def asOptObject(value : JsonValue) : Option[JsonObject] =
    value.asOptObject

  /** Converts dynamic json value into an optional array. */
  @inline
  implicit def asOptArray(value : JsonValue) : Option[JsonArray] =
    value.asOptArray
}
