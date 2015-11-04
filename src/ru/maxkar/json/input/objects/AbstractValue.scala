package ru.maxkar.json.input.objects

import ru.maxkar.json.JsonException

/** Abstract implementation of the JsonValue. */
private[objects] abstract class AbstractValue(kind : String) extends JsonValue {
  override def asArray() : JsonArray =
    throw new JsonException("Could not convert " + kind + " into a json array")

  override def asBoolean() : Boolean =
    throw new JsonException("Could not convert " + kind + " into a boolean")

  override def asDouble() : Double =
    throw new JsonException("Could not convert " + kind + " into a double")

  override def asInt() : Int =
    throw new JsonException("Could not convert " + kind + " into an int")

  override def asLong() : Long =
    throw new JsonException("Could not convert " + kind + " into an long")

  override def asString() : String =
    throw new JsonException("Could not convert " + kind + " into an string")

  override def asObject() : JsonObject =
    throw new JsonException("Could not convert " + kind + " into an object")

  def asOptInt() : Option[Int] = Some(asInt)
  def asOptLong() : Option[Long] = Some(asLong)
  def asOptDouble() : Option[Double] = Some(asDouble)
  def asOptString() : Option[String] = Some(asString)
  def asOptBoolean() : Option[Boolean] = Some(asBoolean)
  def asOptObject() : Option[JsonObject] = Some(asObject)
  def asOptArray() : Option[JsonArray] = Some(asArray)
}
