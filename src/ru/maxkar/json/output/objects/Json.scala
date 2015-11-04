package ru.maxkar.json.output.objects

import java.io.Writer
import java.io.IOException


/** Objects providing an API for json generation (output). */
object Json {
  /* INPUT/OUTPUT */

  /** Outputs a json value into the writer.
   * @param value value to output.
   * @param writer output stream to put json into.
   * @throws IOException if IO exception occured during the writing.
   */
  def writeTo(value : JsonValue, writer : Writer) : Unit =
    if (value == null) writer write "null"
    else value writeTo writer


  /** Converts JSON into a string representation. JSON output indentation
   * is not defined and could vary across library versions.
   */
  def asString(value : JsonValue) : String = {
    val w = new java.io.StringWriter()
    writeTo(value, w)
    w.toString
  }


  /* VALUE CREATION AND MANIPULATION */

  /** Representation of the "undefined" value. Could be used by the object
   * generator for optional fields (produced from optional values).
   */
  val Undefined : MaybeJsonValue = new MaybeJsonValue()

  /** Representation of the JSON null value. */
  val Null : JsonValue = new JsonPrimitive("null")

  /** Converts int into a Json value. */
  def fromInt(v : Int) : JsonValue = new JsonPrimitive(v.toString)

  /** Converts long into a Json value. */
  def fromLong(v : Long) : JsonValue = new JsonPrimitive(v.toString)

  /** Converts double into a Json value. */
  def fromDouble(v : Double) : JsonValue = new JsonPrimitive(v.toString)

  /** Converts string into a Json value. */
  def fromString(v : String) : JsonValue = new JsonString(v)

  /** Converts boolean into a Json value. */
  def fromBoolean(v : Boolean) : JsonValue = new JsonPrimitive(v.toString)

  /** Converts sequence of items into Json Array. */
  def fromArray(v : Iterable[JsonValue]) : JsonArray = new JsonArray(v)

  /** Converts sequence of key-value pairs into a Json Object. */
  def fromKeyValues(keyValues : Iterable[(String, MaybeJsonValue)]) : JsonObject =
    new JsonObject(Map(cleanItems(keyValues) : _*))

  /** Vararg version of fromKeyValues. This version have a shorter name
   * with the goal to be used as a common object constructor:
   * <code>val x = Json.make("a" → 3, "b" → 5)</code>
   */
  def make(items : (String, MaybeJsonValue)*) : JsonObject =
    fromKeyValues(items)


  /** Converts an optional value into a potential json value. */
  def opt[T](x : Option[T], converter : T ⇒ JsonValue) : MaybeJsonValue =
    x match {
      case None ⇒ Undefined
      case Some(x) ⇒ converter(x)
    }


  /** Converts optional int into a potential Json value. */
  def fromOptInt(v : Option[Int]) : MaybeJsonValue =
    opt(v, fromInt)

  /** Converts optional long into a potential Json value. */
  def fromOptLong(v : Option[Long]) : MaybeJsonValue =
    opt(v, fromLong)

  /** Converts optional double into a potential Json value. */
  def fromOptDouble(v : Option[Double]) : MaybeJsonValue =
    opt(v, fromDouble)

  /** Converts optional string into a potential Json value. */
  def fromOptString(v : Option[String]) : MaybeJsonValue =
    opt(v, fromString)

  /** Converts optional boolean into a potential Json value. */
  def fromOptBoolean(v : Option[Boolean]) : MaybeJsonValue =
    opt(v, fromBoolean)

  /** Converts optional sequence of items into potential Json Array. */
  def fromOptArray(v : Option[Iterable[JsonValue]]) : MaybeJsonValue =
    opt(v, fromArray)

  /** Converts optional sequence of key-value pairs into a potential Json
   * Object.
   */
  def fromOptKeyValues(
        keyValues : Option[Seq[(String, JsonValue)]])
      : MaybeJsonValue =
    opt(keyValues, fromKeyValues)


  /** Cleans object items by converting java <code>null</code>
   * values into json Null and removing undefined ones.
   */
  private [objects] def cleanItems(
        items : Iterable[(String, MaybeJsonValue)])
      : Seq[(String, MaybeJsonValue)] =
    items.view
      .map(x ⇒ if (x._2 == null) (x._1, Null) else x)
      .filter(x ⇒ x._2.isDefined)
      .toSeq


  /** Writes a string into the output stream with proper encoding. */
  private[objects] def writeString(w : Writer, s : String) : Unit = {
    if (s == null) {
      w write "null"
      return
    }

    w write '"'

    val len = s.length
    var ptr = 0
    while (ptr < s.length) {
      writeChar(w, s.charAt(ptr))
      ptr += 1
    }

    w write '"'
  }



  /** Outputs one character into the stream escaping it if needed. */
  private[objects] def writeChar(w : Writer, c : Char) : Unit =
    c match {
      case '"' ⇒ w write "\\\""
      case '\\' ⇒ w write "\\\\"
      case '\b' ⇒ w write "\\b"
      case '\f' ⇒ w write "\\f"
      case '\n' ⇒ w write "\\n"
      case '\r' ⇒ w write "\\r"
      case '\t' ⇒ w write "\\t"
      case x if x < 0x0010 ⇒
        w write "\\u000"
        w write toHexDigit(x)
      case x if x < 0x0020 ⇒
        w write "\\u001"
        w write toHexDigit(x - 0x0010)
      case general ⇒ w write general
    }


  /** Converts a number into a hex digit. */
  private def toHexDigit(x : Int) : Char =
    "0123456789ABCDEF".charAt(x)
}
