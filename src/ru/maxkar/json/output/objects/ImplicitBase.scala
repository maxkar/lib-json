package ru.maxkar.json.output.objects

import scala.language.implicitConversions

/**
 * Base trait for implicit json conversions. Domain-specific libraries
 * could inherit base conversions from this trait. It that case they
 * would be able to provide all conversions in one import.
 */
trait ImplicitBase {
  /** Allows use of nulls as Json values. */
  @inline
  implicit def fromNull(obj : Null) : JsonValue = Json.Null

  /** Converts int into a Json value. */
  @inline
  implicit def fromInt(v : Int) : JsonValue =
    Json.fromInt(v)

  /** Converts long into a Json value. */
  @inline
  implicit def fromLong(v : Long) : JsonValue =
    Json.fromLong(v)

  /** Converts double into a Json value. */
  @inline
  implicit def fromDouble(v : Double) : JsonValue =
    Json.fromDouble(v)

  /** Converts string into a Json value. */
  @inline
  implicit def fromString(v : String) : JsonValue =
    Json.fromString(v)

  /** Converts boolean into a Json value. */
  @inline
  implicit def fromBoolean(v : Boolean) : JsonValue =
    Json.fromBoolean(v)

  /** Converts sequence of items into Json Array. */
  @inline
  implicit def fromArray(v : Iterable[JsonValue]) : JsonArray =
    Json.fromArray(v)

  /** Converts sequence of key-value pairs into a Json Object. */
  @inline
  implicit def fromKeyValues(keyValues : Seq[(String, MaybeJsonValue)]) : JsonValue =
    Json.fromKeyValues(keyValues)

  /** Converts optional int into a potential Json value. */
  @inline
  implicit def fromOptInt(v : Option[Int]) : MaybeJsonValue =
    Json.fromOptInt(v)

  /** Converts optional long into a potential Json value. */
  @inline
  implicit def fromOptLong(v : Option[Long]) : MaybeJsonValue =
    Json.fromOptLong(v)

  /** Converts optional double into a potential Json value. */
  @inline
  implicit def fromOptDouble(v : Option[Double]) : MaybeJsonValue =
    Json.fromOptDouble(v)

  /** Converts optional string into a potential Json value. */
  @inline
  implicit def fromOptString(v : Option[String]) : MaybeJsonValue =
    Json.fromOptString(v)

  /** Converts optional boolean into a potential Json value. */
  @inline
  implicit def fromOptBoolean(v : Option[Boolean]) : MaybeJsonValue =
    Json.fromOptBoolean(v)

  /** Converts optional sequence of items into potential Json Array. */
  @inline
  implicit def fromOptArray(v : Option[Iterable[JsonValue]]) : MaybeJsonValue =
    Json.fromOptArray(v)

  /** Converts optional sequence of key-value pairs into a potential Json
   * Object.
   */
  @inline
  implicit def fromOptKeyValues(
        keyValues : Option[Seq[(String, JsonValue)]])
      : MaybeJsonValue =
    Json.fromOptKeyValues(keyValues)
}
