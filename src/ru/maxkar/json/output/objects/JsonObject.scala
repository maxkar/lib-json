package ru.maxkar.json.output.objects

import java.io.Writer

/** Representation of the json object.
 * @param items list of <em>value</em> items. This type is maybejson to
 * prevent additional castings, but runtime value must be a json value.
 */
final class JsonObject private[objects](
      private val items : Map[String, MaybeJsonValue])
    extends JsonValue {


  /** Concatenates an object with another object. Values defined in
   * the second object takes precedence other objects in this object.
   */
  def ++(other : JsonObject) : JsonObject =
    new JsonObject(items ++ other.items)


  /** Concatenates this object with other objects. */
  def ++(otherItems : Iterable[(String, JsonObject)]) : JsonObject =
    new JsonObject(items ++ Json.cleanItems(otherItems))


  override private[objects] def writeTo(writer : Writer) : Unit = {
    writer write '{'

    val itr = items.iterator
    if (itr.hasNext)
      writeEntity(writer, itr.next)

    while (itr.hasNext) {
      writer write ','
      writeEntity(writer, itr.next)
    }

    writer write '}'
  }



  /** Writes an entity. */
  private def writeEntity(writer : Writer, entity : (String, MaybeJsonValue)) : Unit = {
    Json.writeString(writer, entity._1)
    writer write ':'

    val v = entity._2
    if (v == null) writer write "null"
    else v writeTo writer
  }
}
