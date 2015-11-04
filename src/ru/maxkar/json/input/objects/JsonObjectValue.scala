package ru.maxkar.json.input.objects

/** Internal representation of the Json Object. */
private[objects] final class JsonObjectValue(base : JsonObject)
    extends AbstractValue("object") {
  override def asObject() : JsonObject = base
}
