package ru.maxkar.json.input.objects

import ru.maxkar.json.JsonException


/** Base class for "empty" values. This class have behaviour common to
 * undefined (value not set) and null as in many scenarios they could
 * be used interchangeably.
 * @param valueName name of the value. It could be used in diagnostical purposes.
 */
private[objects] final class EmptyValue(valueName : String)
    extends JsonValue {

  override def asInt() : Int = raise()
  override def asLong() : Long = raise()
  override def asDouble() : Double = raise()
  override def asString() : String = raise()
  override def asBoolean() : Boolean = raise()
  override def asObject() : JsonObject = raise()
  override def asArray() : JsonArray = raise()

  override def asOptInt() : Option[Int] = None
  override def asOptLong() : Option[Long] = None
  override def asOptDouble() : Option[Double] = None
  override def asOptString() : Option[String] = None
  override def asOptBoolean() : Option[Boolean] = None
  override def asOptObject() : Option[JsonObject] = None
  override def asOptArray() : Option[JsonArray] = None

  /** Raises an exception about missing property. */
  private def raise[T]() : T =
    throw new JsonException(
      "Could not convert " + valueName + " into another type")
}
