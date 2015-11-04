package ru.maxkar.json.input.objects

import ru.maxkar.json.JsonException


/** Number as a value. */
private[objects] final class JsonNumber(repr : String)
    extends AbstractValue("number") {

  override def asDouble() : Double =
    try {
      java.lang.Double.parseDouble(repr)
    } catch {
      case e : NumberFormatException ⇒
        throw new JsonException("Could not convert " + repr + " into double")
    }


  override def asInt() : Int =
    try {
      Integer.parseInt(repr)
    } catch {
      case e : NumberFormatException ⇒
        throw new JsonException("Could not convert " + repr + " into int")
    }


  override def asLong() : Long =
    try {
      java.lang.Long.parseLong(repr)
    } catch {
      case e : NumberFormatException ⇒
        throw new JsonException("Could not convert " + repr + " into long")
    }
}
