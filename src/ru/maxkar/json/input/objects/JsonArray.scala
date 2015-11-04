package ru.maxkar.json.input.objects

/** Representation of the JSON array. Allows "dynamic* access to the properties.
 * Like JsonObject, this array allows an access to properties outside of it's range.
 * Those values could be treated as "optional" arguments and could be converted into
 * an Option[x] value.
 * @param items items in this array.
 */
final class JsonArray(items : Seq[JsonValue])
    extends Seq[JsonValue]() {

  /** Accesses an element at the specified index. */
  override def apply(idx : Int) : JsonValue =
    if (idx < 0 || items.length <= idx) Json.Undefined
    else items(idx)


  /** Length of this array. */
  override def length() : Int = items.length


  /** Iterator over this array. */
  override def iterator() : Iterator[JsonValue] = items.iterator
}
