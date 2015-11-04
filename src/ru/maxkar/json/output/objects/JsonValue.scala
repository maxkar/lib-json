package ru.maxkar.json.output.objects

/**
 * Value in the JSON notation. This value could be written into the
 * stream by using functions on the Json object.
 * @param writeFn object writing function.
 */
class JsonValue private[objects]() extends MaybeJsonValue() {

  override private[objects] val isDefined = true
}
