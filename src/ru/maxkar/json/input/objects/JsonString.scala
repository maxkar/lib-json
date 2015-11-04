package ru.maxkar.json.input.objects

/** String-valued data. */
private[objects] final class JsonString(v : String)
    extends AbstractValue("string") {
  override def asString() : String = v
}
