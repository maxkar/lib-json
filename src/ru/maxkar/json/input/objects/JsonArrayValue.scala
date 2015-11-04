package ru.maxkar.json.input.objects

/** Implementation of the array value. */
private[objects] final class JsonArrayValue(base : JsonArray)
    extends AbstractValue("array") {
  override def asArray() : JsonArray = base
}
