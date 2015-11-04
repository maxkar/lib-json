package ru.maxkar.json.input.objects

import scala.language.dynamics

/**
 * Representation of the Json object input.
 * This object could be "applied" to get a value of it. It also support
 * dynamic access to the properties which acts as an access to the "value".
 * Returned value is "capture" which have no specific type (so it could be
 * treated by client in different ways). However, client is responsible
 * for accessing the object in proper (type-safe) way.
 * @param peer base json object representation.
 */
final class JsonObject private[objects](
      private[objects] val content : Map[String, JsonValue])
    extends Dynamic {

  /**
   * Retrieves a value based on the field name. This method does not check
   * field presence as field could be converted into an "optional" value.
   * @param fieldName name of the field to access.
   */
  def apply(fieldName : String) : JsonValue =
    content.getOrElse(fieldName, Json.Undefined)


  /**
   * Retrieves a value based on the field name. This method does not check
   * field presence as field could be converted into an "optional" value.
   * @param fieldName name of the field to access.
   */
  def selectDynamic(fieldName : String) : JsonValue =
    this(fieldName)
}
