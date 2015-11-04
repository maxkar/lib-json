package ru.maxkar.json.input.objects

import java.io.IOException
import java.io.Reader

import ru.maxkar.json.JsonException

import scala.language.implicitConversions


/**
 * <p>JSON manipulation utilities. Provides many convinience/utility methods,
 * implicit conversions, etc... </p>
 *
 * <p>All strict conversion methods throws a NoJsonFieldException if value
 * denotes an access to the non-existent property. They throw
 * IncompatibleValueException if field value is not compatible to the required
 * type.
 * </p>
 */
object Json {
  /* MODEL PARTS. */

  /** Truth value. */
  private val True = new AbstractValue("boolean") {
    override def asBoolean() : Boolean = true
  }



  /** False value. */
  private val False = new AbstractValue("boolean") {
    override def asBoolean() : Boolean = false
  }



  /** Representation of the Json <code>null</code> value. This value was
   * explicitly specified as null in the input.
   */
  val Null = new EmptyValue("null")


  /** Representation of the "undefined" value. This value is returned on
   * access to the undefined property (on the object) or index
   * (on json array). This value is automatically converted into
   * <code>None</code> in option accessors. This simplifies code accessing
   * optional arguments:
   * <code> var x : Option[Int] = x.a </code> No explicit checks are
   * required. This value behaves like a <code>JsonNull</code> but have
   * a different identity. So code aware of the difference between null
   * and undefined could distinguish these scerarios.
   */
  val Undefined = new EmptyValue("undefined")
  /* UTILITY SECTION. */


  /**
   * Parses a string as a JSON object.
   */
  def parseObject(str : String) : JsonObject =
    parseValue(str).asObject


  /**
   * Parses a json stream. This method performs an automatic
   * encoding detection according to the RFC-4627.
   * @throws IOException if bytes does not define a valid JSON response.
   */
  def parseObject(bytes : Array[Byte]) : JsonObject =
    parseObject(asJsonText(bytes))



  /**
   * Parses a string as a JSON object.
   */
  def parseArray(str : String) : JsonArray =
    parseValue(str).asArray



  /**
   * Parses a json stream. This method performs an automatic
   * encoding detection according to the RFC-4627.
   * @throws IOException if bytes does not define a valid JSON response.
   */
  def parseArray(bytes : Array[Byte]) : JsonArray =
    parseArray(asJsonText(bytes))



  /** Parses a JSON value from the underlying stream. */
  def parseValue(rdr : Reader) : JsonValue = {
    val stream = new JsonStream(rdr)
    val res = readValue(stream)

    skipWhites(stream)
    if (!stream.eof)
      throw new JsonException("Trailing data after the json at " + stream.location)
    res
  }


  /** Parses a string value. */
  def parseValue(str : String) : JsonValue =
    parseValue(new java.io.StringReader(str))


  /** Parses a json stream from the underlying byte array. */
  def parseValue(bytes : Array[Byte]) : JsonValue =
    parseValue(asJsonReader(bytes))



  /**
   * Checks that content type is a json content type.
   */
  def isJsonContentType(contentType : String) : Boolean = {
    val lowercase = contentType.toLowerCase
    /* According to IANA https://www.iana.org/assignments/media-types/application/json
     * no charset parameter is defined for the JSON schema. However, many servers
     * pass that non-compliant parameter in the response. We allow both
     * canonical and non-compliant representation.
     */
    return lowercase == "application/json" || lowercase.startsWith("application/json;")
  }



  /**
   * Ensures that content type is a valid json content type. Throws an
   * IOException if content type is not valid for the json.
   */
  def ensureJsonContentType(contentType : String) : Unit =
    if (!isJsonContentType(contentType))
      throw new IOException(
        "Content type " + contentType + " is not a valid json content type")



  /** Converts json into the text representation according to the
   * supplied json encoding.
   */
  private def asJsonText(bytes : Array[Byte]) : String = {
    val encoding = sniffEncoding(bytes)
    return new String(bytes, encoding)
  }


  /** Converts json bytes into the reader. */
  private def asJsonReader(bytes : Array[Byte]) : Reader = {
    return new java.io.StringReader(asJsonText(bytes))
  }



  /** Sniffs a JSON encoding. This is performed according to the
   * RFC-4627, chapter 3.
   */
  private def sniffEncoding(bytes : Array[Byte]) : String = {
    if (bytes.length < 4)
      return "UTF-8"
    if (bytes(0) == 0 && bytes(1) == 0 && bytes(2) == 0 && bytes(3) != 0)
      return "UTF-32BE"
    if (bytes(0) == 0 && bytes(1) != 0 && bytes(2) == 0 && bytes(3) != 0)
      return "UTF-16BE"
    if (bytes(0) != 0 && bytes(1) == 0 && bytes(2) == 0 && bytes(3) == 0)
      return "UTF-32LE"
    if (bytes(0) != 0 && bytes(1) == 0 && bytes(2) != 0 && bytes(3) == 0)
      return "UTF-16LE"
    if (bytes(0) != 0 && bytes(1) != 0 && bytes(2) != 0 && bytes(3) != 0)
      return "UTF-8"
    throw new IOException(
      "Unsupported start pattern " +
      bytes(0) + "," + bytes(1) + "," + bytes(2) + "," + bytes(3))
  }



  /** Skips all whitespaces in the input stream. */
  private def skipWhites(stream : JsonStream) : Unit =
    while (isWhitespace(stream.peek))
      stream.read



  /** Checks if a character(code) is JSON whitespace. */
  private def isWhitespace(c : Int) : Boolean =
    c match {
      case ' ' | '\t' | '\r' | '\n' ⇒ true
      case _ ⇒ false
    }


  /** Reads an explicit literal.
   * @param stream stream to read the value from.
   * @param repr string representation of the literal.
   * @param res resulting json value.
   */
  private def readLiteral(
        stream : JsonStream,
        repr : String,
        res : JsonValue)
      : JsonValue = {
    val start = stream.location
    val len = repr.length

    var ptr = 0

    while (ptr < len) {
      if (stream.eof) throw new JsonException(
        "Unexpected EOF at " + stream.location + " inside literal " + repr +
          " started at " + start)

      val nxt = stream.read
      if (repr.charAt(ptr) != nxt) throw new JsonException(
        "Mailformed literal " + repr + " at " + start + ": expected " +
          repr.charAt(ptr) + " at " + stream.location + " but got " + nxt)

      ptr += 1
    }

    res
  }



  /** Reads one value from the stream. */
  private def readValue(stream : JsonStream) : JsonValue = {
    skipWhites(stream)
    stream.peek match {
      case 't' ⇒ readLiteral(stream, "true", True)
      case 'f' ⇒ readLiteral(stream, "false", False)
      case 'n' ⇒ readLiteral(stream, "null", Null)
      case '{' ⇒ readJsonObject(stream)
      case '[' ⇒ readJsonArray(stream)
      case '-' ⇒ readNumber(stream)
      case '"' ⇒ new JsonString(readString(stream))
      case x if '0' <= x && x <= '9' ⇒ readNumber(stream)
      case x if x < 0 ⇒
        throw new JsonException("No json value found till " + stream.location)
      case x ⇒
        throw new JsonException(
          "Illegal start of json value at " + stream.location + " char = " + x)
    }
  }



  /** Reads a JSON array. */
  private def readJsonArray(stream : JsonStream) : JsonValue = {
    val start = stream.location

    stream.read() // '['

    skipWhites(stream)
    if (stream.peek == ']') {
      stream.read()
      return new JsonArrayValue(new JsonArray(Seq.empty))
    }

    val items = new scala.collection.mutable.ArrayBuffer[JsonValue]
    items += readValue(stream)

    skipWhites(stream)
    while (stream.peek != ']') {

      if (stream.peek != ',') throw new JsonException(
        "Mailformed array at " + start + ", expected either , or ] at " +
          stream.location + " but got " + stream.peek)

      stream.read() // ','

      skipWhites(stream)
      items += readValue(stream)
      skipWhites(stream)
    }
    stream.read() // ']'

    new JsonArrayValue(new JsonArray(items))
  }



  /** Reads a JSON object. */
  private def readJsonObject(stream : JsonStream) : JsonValue = {
    val start = stream.location

    stream.read() // '{'
    skipWhites(stream)

    if (stream.peek() == '}') {
      stream.read()
      return new JsonObjectValue(new JsonObject(Map.empty))
    }

    val defMap = new scala.collection.mutable.HashMap[String, (String, JsonValue)]
    defMap += readObjectEntry(stream)

    skipWhites(stream)
    while (stream.peek != '}') {

      if (stream.peek != ',') throw new JsonException(
        "Mailformed object at " + start + ", expected either , or } at " +
          stream.location + " but got " + stream.peek)

      stream.read() // ','

      skipWhites(stream)
      val nextEntry = readObjectEntry(stream)

      if (defMap.contains(nextEntry._1)) throw new JsonException(
        "Redefinition of property " + nextEntry._1 + " at " + nextEntry._2._1 +
          " previously defined at " + defMap(nextEntry._1)._1)

      defMap += nextEntry
      skipWhites(stream)
    }

    stream.read() // '}'

    new JsonObjectValue(new JsonObject(defMap.mapValues(x ⇒ x._2).toMap))
  }



  /** Reads one object entry. */
  private def readObjectEntry(stream : JsonStream) : (String, (String, JsonValue)) = {
    if (stream.peek != '"') throw new JsonException(
      "Bad object entry start at " + stream.location + ", expected \" but got " + stream.peek)

    val defStart = stream.location

    val name = readString(stream)
    skipWhites(stream)

    if (stream.peek != ':') throw new JsonException(
      "Expected ':' in the object body but got " + stream.peek + " at " + stream.location)

    stream.read() // ':'
    (name, (defStart, readValue(stream)))
  }



  /** Reads a tail of the string. */
  private def readString(stream : JsonStream) : String = {
    val start = stream.location
    stream.read() // '"'

    val buf = new StringBuilder()
    while (stream.peek != '"') {
      if (stream.eof) throw new JsonException(
        "Mailforme string at " + start + ": could not find stream end till " + stream.location)
      buf += readStringChar(stream)
    }

    stream.read() // '"'

    buf.toString()
  }



  /** Reads a string character. */
  private def readStringChar(stream : JsonStream) : Char = {
    if (stream.peek != '\\') {
      if (stream.peek < 0x0020) throw new JsonException(
        "Unescaped special char at " + stream.location)
      return stream.read
    }

    stream.read() // '\\'

    val escapedLoc = stream.location

    if (stream.eof) throw new JsonException(
      "EOF detected in string after escape at " + escapedLoc)

    stream.read match {
      case '"' ⇒ '"'
      case '\\' ⇒ '\\'
      case '/' ⇒ '/'
      case 'b' ⇒ '\b'
      case 'f' ⇒ '\f'
      case 'n' ⇒ '\n'
      case 'r' ⇒ '\r'
      case 't' ⇒ '\t'
      case 'u' ⇒ readUnicodeChar(stream)
      case x ⇒ throw new JsonException(
        "Unrecognizeable escaped char " + x + " at " + escapedLoc)
    }
  }


  /** Reads an unicode character. */
  private def readUnicodeChar(stream : JsonStream) : Char =
    ((readUnicodeDigit(stream) << 12) +
     (readUnicodeDigit(stream) << 8) +
     (readUnicodeDigit(stream) << 4) +
      readUnicodeDigit(stream)
    ).asInstanceOf[Char]



  /** Reads one unicode digit. */
  private def readUnicodeDigit(stream : JsonStream) : Int = {
    if (stream.eof) throw new JsonException(
      "Unexpected EOF inside unicode character at " + stream.location)
    val nxt = stream.read
    if ('0' <= nxt && nxt <= '9')
      nxt - '0'
    else if ('A' <= nxt && nxt <= 'F')
      nxt - 'A' + 10
    else if ('a' <= nxt && nxt <= 'f')
      nxt - 'a' + 10
    else throw new JsonException(
      "Invalid unicode digit before " + stream.location)
  }


  /** Reads a number literal. */
  private def readNumber(stream : JsonStream) : JsonValue = {
    val res = new StringBuilder()

    /* sign */
    if (stream.peek == '-')
      res += stream.read

    /* int */
    if (stream.eof)
      throw new JsonException("Unexpected eof inside number at " + stream.location)

    if (stream.peek == '0') {
      res += stream.read
      if (!stream.eof && Character.isDigit(stream.peek))
        throw new JsonException("Could not read numbef with trailing zero at " + stream.location)
    } else if (Character.isDigit(stream.peek)) {
      while (!stream.eof && Character.isDigit(stream.peek))
        res += stream.read
    } else
      throw new JsonException("Illegal number start character " + stream.peek + " at " + stream.location)


    /* frac */
    if (stream.peek == '.') {
      res += stream.read
      if (stream.eof || !Character.isDigit(stream.peek))
        throw new JsonException("Illegal frac part at " + stream.location)

      while (!stream.eof && Character.isDigit(stream.peek))
        res += stream.read
    }


    /* exp */
    if (stream.peek == 'e' || stream.peek == 'E') {
      res += stream.read
      if (stream.eof || !Character.isDigit(stream.peek))
        throw new JsonException("Illegal exp part at " + stream.location)

      while (!stream.eof && Character.isDigit(stream.peek))
        res += stream.read
    }

    new JsonNumber(res.toString)
  }
}

