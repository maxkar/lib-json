package ru.maxkar.json

/**
 * Base json parsers. These could be inherited by the custom DSLs. Then only
 * those custom conersions could be imported instead of all the base
 * parsers/serializers.
 */
trait BaseParsers {
  implicit val booleanParser = Parser[Boolean](v ⇒ v match {
    case JsonTrue ⇒ true
    case JsonFalse ⇒ false
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to boolean")
  })



  implicit val stringParser = Parser[String](v ⇒ v match {
    case JsonString(x) ⇒ x
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to string")
  })



  implicit val intParser = Parser[Int](v ⇒ v match {
    case JsonNumber(x) ⇒
      try {
        Integer.parseInt(x)
      } catch {
        case e : NumberFormatException ⇒
          throw new JsonException(
            x + " is not valid integer")
      }
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to number")
  })



  implicit val longParser = Parser[Long](v ⇒ v match {
    case JsonNumber(x) ⇒
      try {
        java.lang.Long.parseLong(x)
      } catch {
        case e : NumberFormatException ⇒
          throw new JsonException(
            x + " is not valid long")
      }
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to number")
  })



  implicit val doubleParser = Parser[Double](v ⇒ v match {
    case JsonNumber(x) ⇒
      try {
        java.lang.Double.parseDouble(x)
      } catch {
        case e : NumberFormatException ⇒
          throw new JsonException(
            x + " is not valid double")
      }
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to number")
  })



  implicit val arrayParser = Parser[JsonArray](v ⇒ v match {
    case a@JsonArray(_) ⇒ a
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to array")
  })



  implicit val seqParser = Parser[Seq[JsonValue]](v ⇒ v match {
    case JsonArray(a) ⇒ a
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to array")
  })



  implicit val objectParser = Parser[JsonObject](v ⇒ v match {
    case a@JsonObject(_) ⇒ a
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to object")
  })



  implicit val mapParser = Parser[Map[String, JsonValue]](v ⇒ v match {
    case JsonObject(a) ⇒ a
    case _ ⇒
      throw new JsonException(
        "Could not convert " + Json.typeName(v) + " to object")
  })
}
