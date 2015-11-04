package ru.maxkar.json.output.objects

import java.io.Writer

/**
 * Representation of the JSON array. Provides an API to "extend" arrays with
 * other arrays or values.
 */
final class JsonArray private[objects](
      private val items : Iterable[JsonValue])
    extends JsonValue {

  /** Creates an array as a concatenation of both json arrays. */
  def ++(otherArray : JsonArray) : JsonArray =
    new JsonArray(items ++ otherArray.items)


  /** Concatenates this array with additional items and returns a new array. */
  def ++(otherItems : Iterable[JsonValue]) : JsonArray =
    new JsonArray(items ++ otherItems)


  override private[objects] def writeTo(writer : Writer) : Unit = {
    writer write '['

    val itr = items.iterator
    if (itr.hasNext)
      Json.writeTo(itr.next, writer)

    while (itr.hasNext) {
      writer write ','
      Json.writeTo(itr.next, writer)
    }

    writer write ']'
  }
}
