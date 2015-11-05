package ru.maxkar.json

/* VALUE TYPE HIERARCHY DEFINITION. */

/**
 * Common type for all values and "undefined" (which is not
 * a common value). Object selectors use this type to represent
 * values absent in the object itself. Object factories take
 * this type to allow "no-op" key-value pairs which could be
 * generated by optional values.
 */
abstract sealed class JsonPhantomValue


/**
 * Undefined value. It is returned from object selectors when
 * field is not defined on the object (null is considered
 * as defined). It could be used in the Json.make factory to
 * generate a no-op value (i.e. nothing would be written for
 * the given key).
 */
final case object JsonUndefined extends JsonPhantomValue


/**
 * Real json value like object, array, literal and null.
 */
abstract sealed class JsonValue extends JsonPhantomValue


/**
 * Representation of the JSON null literal. Unlike JsonUndefined,
 * JsonNull <em>is</em> written into the output as a null value.
 */
final case object JsonNull extends JsonValue


/**
 * Json "True" literal. This is a separate literal rather than
 * JsonBoolean(v : Boolean) to save memory when many literals
 * are present in the file.
 */
final case object JsonTrue extends JsonValue


/**
 * Json "False" literal. This is a separate literal rather than
 * JsonBoolean(v : Boolean) to save memory when many literals
 * are present in the file.
 */
final case object JsonFalse extends JsonValue


/**
 * String JSON literal. It contains unquoted (real) value.
 * @param value value of the string literal.
 */
final case class JsonString(value : String) extends JsonValue


/**
 * JSON Number literal. It contains unparsed string representation of
 * the numeric input. This value could be treated in the different ways
 * by the user libraries (parsed as double, integer or BigDecimal). No
 * conversions are made at this point to allow that form of the customization
 * and save all information which could affect the result.
 * @param value string representation of the number literal.
 * @throws IllegalArgumentException if value is not valid JSON Number
 */
final case class JsonNumber(value : String) extends JsonValue {
  JsonNumber.validate(value)
}


/**
 * JSON array (list of JSON values).
 * @param elements elements of the array.
 */
final case class JsonArray(elements : Seq[JsonValue]) extends JsonValue


/**
 * JSON object (an unordered collection of key-value pairs).
 * Object constructor does not take "Undefineds" as an
 * input. However, instances of this class are not supposed to be constructed
 * directly. Json.make should be used in most cases. It provides all required
 * filtering of Undefinde values.
 * @param values map from keys to correpsonding values.
 */
final case class JsonObject(values : Map[String, JsonValue]) extends JsonValue



/* UTILITIES. */

/**
 * Utilities for the json number.
 */
private object JsonNumber {
  /** Minus part of the pattern. */
  private val pat_minus = "-"

  /** Pattetrn for the JSON int part. */
  private val pat_int = "0|([1-9]\\d*)"

  /** Pattern for the fractional part. */
  private val pat_frac = "\\.\\d+"

  /** Pattern for the exponent part. */
  private val pat_exp = "[eE][-+]?\\d+"

  /** Pattern of the JSON Number. */
  private val pat = java.util.regex.Pattern.compile(
      pat_minus + "?" +
      "(" + pat_int + ")" +
      "(" + pat_frac + ")?" +
      "(" + pat_exp + ")?"
    )


  /**
   * Validates that num is a valid JSON number representation.
   * @throws IllegalArgumentException if value is not valid.
   */
  private def validate(num : String) : Unit =
    if (!pat.matcher(num).matches)
      throw new IllegalArgumentException("Invalid json number value " + num)
}
