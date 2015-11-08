package ru.maxkar.json

/**
 * Json facade.
 */
object Json {
  /**
   * Returns a "human-readable" type of the json value. The primary goal
   * is to provide good error or debugging messages.
   */
  def typeName(obj : JsonPhantomValue) : String =
    obj match {
      case JsonUndefined ⇒ "undefined"
      case JsonNull ⇒ "null"
      case JsonTrue | JsonFalse ⇒ "boolean"
      case JsonString(_) ⇒ "string"
      case JsonNumber(_) ⇒ "number"
      case JsonArray(_) ⇒ "array"
      case JsonObject(_) ⇒ "object"
    }


  /**
   * Creates an array from a list of (possible optional) values.
   * Undefined values are not present in the resulting array, but
   * JsonNull's are.
   */
  def array(items : JsonPhantomValue*) : JsonArray =
    JsonArray(items.view
      .filter(_ != JsonUndefined)
      .map(x ⇒ if (x == null) JsonNull else x.asInstanceOf[JsonValue])
      .toSeq)


  /**
   * Creates a new Json object from the list of key-value pairs.
   * Pairs with the "undefined" value are ignored and not present in
   * the reuslting object. However, JsonNulls are serialized.
   */
  def make(pairs : (String, JsonPhantomValue)*) : JsonObject =
    JsonObject(Map(pairs.view
      .filter(x ⇒ x._2 != JsonUndefined)
      .map(x ⇒
        if (x._2 == null) (x._1, JsonNull)
        else (x._1, x._2.asInstanceOf[JsonValue])) : _*))
}
