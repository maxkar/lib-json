package ru.maxkar.json

/**
 * Parser for the json value. Converts JsonValue to value
 * of some other type.
 * @param T target conversion type.
 * @param parse value parser. Throws JsonException if value could not
 *   be converted into the target.
 */
final case class Parser[T](val parse : JsonValue ⇒ T)


/**
 * Helpers for the Json Parsers.
 */
object Parser {

  /**
   * Creates an "option" parser from the base value parser. The
   * new parser treats Undefined as None and delegates all the other
   * results to the base.
   */
  def opt[T](base : Parser[T]) : Parser[Option[T]] =
    Parser[Option[T]](x ⇒
      if (x == JsonUndefined || x == JsonNull) None
      else Some(base.parse(x)))


  /**
   * Creates a parser for the json array. Resulting parser throws
   * JsonException if input is not a json array.
   */
  def array[T](base : Parser[T]) : Parser[Seq[T]] =
    Parser[Seq[T]](x ⇒
      x match {
        case JsonArray(v) ⇒ v.map(base.parse)
        case e ⇒ throw new JsonException(
          "Could not convert " + Json.typeName(x) + " to array")
      })
}
