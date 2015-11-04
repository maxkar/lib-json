package ru.maxkar.json.input.objects

/**
 * Representation of the JSON "value". This value is dynamic in nature and
 * it could be converted into multiple different types using json accessor.
 * Access of non-existent element yields <code>None</code> for the optional
 * calls and <code>JsonException</code> for non-optional ones.
 * Any attempt to access a typed value (either optional or non-optional) would
 * raise <code>JsonValue</code>
 */
abstract class JsonValue private[objects]() {
  /* BASE CONVERIONS. */

  /** Returns value of this json object as Int. */
  def asInt() : Int

  /** Returns value of this json object as Long. */
  def asLong() : Long

  /** Returns value of this json object as Double. */
  def asDouble() : Double

  /** Returns value of this json object as String. */
  def asString() : String

  /** Returns value of this json object as Boolean. */
  def asBoolean() : Boolean

  /** Returns value of this json object as json Object. */
  def asObject() : JsonObject

  /** Returns value of this json object as json Array. */
  def asArray() : JsonArray

  /** Returns this values as a list of other values. */
  final def asArrayOf[T](fn : JsonValue ⇒ T) : Seq[T] =
    this.asArray().map(fn)


  /* OPTIONALS */

  /** Returns value of this json object as optional Int. */
  def asOptInt() : Option[Int]

  /** Returns value of this json object as optional Long. */
  def asOptLong() : Option[Long]

  /** Returns value of this json object as optional Double. */
  def asOptDouble() : Option[Double]

  /** Returns value of this json object as optional String. */
  def asOptString() : Option[String]

  /** Returns value of this json object as optional Boolean. */
  def asOptBoolean() : Option[Boolean]

  /** Returns value of this json object as optional json Object. */
  def asOptObject() : Option[JsonObject]

  /** Returns value of this json object as optional json Array. */
  def asOptArray() : Option[JsonArray]

  /** Returns this values as an optional list of other values. */
  final def asOptArrayOf[T](fn : JsonValue ⇒ T) : Option[Seq[T]] =
    this.asOptArray().map(_.map(fn))
}
