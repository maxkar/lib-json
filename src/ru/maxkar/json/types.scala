package ru.maxkar.json

import scala.language.dynamics

/* VALUE TYPE HIERARCHY DEFINITION. */


/**
 * Any real (JSON) value or a special placeholder for
 * an absence of any value (JsonUndefined).
 */
abstract sealed class JsonValue extends Dynamic {
  /**
   * Retreives a child of this value. Returns JsonUndefined if
   * this value is not an object or key is not defined on the object.
   * This behaviour allows null-safe traversals to a nested optional
   * property like <code>a.b.c.d</code>.
   * @param name field name.
   * @return field of the object or JsonUndefined.
   */
  def selectDynamic(name : String) : JsonValue = JsonUndefined


  /**
   * Synonym for the dynamic select method. Could be used when
   * json key is not a valid java identifier. */
  final def apply(name : String) : JsonValue =
    selectDynamic(name)


  /**
   * Selects an indexed value if this value (which should be a JsonArray).
   * Returns a JsonUndefined if this value is not a json array or if index
   * is out of bounds.
   * @param idx index of the value to select.
   * @return value at the given index or JsonUndefined if index is not valid.
   */
  def apply(idx : Int) : JsonValue = JsonUndefined


  /**
   * Converts this value into the target type (if possible).
   * @throws JsonException if value could not be parsed as the target type.
   */
  @inline
  def as[T : Parser] : T = implicitly[Parser[T]].parse(this)
}


/**
 * Undefined value. It is returned from object selectors when
 * field is not defined on the object (null is considered
 * as defined). It could be used in the Json.make factory to
 * generate a no-op value (i.e. nothing would be written for
 * the given key).
 */
final case object JsonUndefined extends JsonValue


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
final case class JsonArray(elements : Seq[JsonValue]) extends JsonValue {
  if (elements.contains(JsonUndefined))
    throw new IllegalArgumentException("No JsonUndefined is allowed in the array")


  override def apply(idx : Int) : JsonValue =
    if (idx < 0 || idx >= elements.length)
      JsonUndefined
    else
      elements(idx)
}


/**
 * JSON object (an unordered collection of key-value pairs).
 * Object constructor does not take "Undefineds" as an
 * input. However, instances of this class are not supposed to be constructed
 * directly. Json.make should be used in most cases. It provides all required
 * filtering of Undefinde values.
 * @param values map from keys to correpsonding values.
 */
final case class JsonObject(values : Map[String, JsonValue]) extends JsonValue {
  if (values.values.exists(_ == JsonUndefined))
    throw new IllegalArgumentException("No JsonUndefined is allowed in the object")

  override def selectDynamic(name : String) : JsonValue =
    values.getOrElse(name, JsonUndefined)

  /** Combines two objects into one. */
  def ++(other : JsonObject) : JsonObject = JsonObject(values ++ other.values)
}



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
