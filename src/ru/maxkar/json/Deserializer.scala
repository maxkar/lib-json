package ru.maxkar.json

import java.io.Reader

import java.io.IOException

/**
 * Json deserializer object.
 */
private[json] object Deserializer {


  /** Parses a byte array as json object. */
  def parse(bytes : Array[Byte]) : JsonValue =
    parse(asJsonReader(bytes))



  /** Parses a JSON value from the underlying stream. */
  def parse(rdr : Reader) : JsonValue = {
    val stream = new JsonStream(rdr)
    val res = readValue(stream)

    skipWhites(stream)
    if (!stream.eof)
      throw new JsonException("Trailing data after the json at " + stream.location)
    res
  }



  /** Reads one value from the stream. */
  private def readValue(stream : JsonStream) : JsonValue = {
    skipWhites(stream)
    stream.peek match {
      case 't' ⇒ readLiteral(stream, "true", JsonTrue)
      case 'f' ⇒ readLiteral(stream, "false", JsonFalse)
      case 'n' ⇒ readLiteral(stream, "null", JsonNull)
      case '{' ⇒ readJsonObject(stream)
      case '[' ⇒ readJsonArray(stream)
      case '-' ⇒ readNumber(stream)
      case '"' ⇒ JsonString(readString(stream))
      case x if '0' <= x && x <= '9' ⇒ readNumber(stream)
      case x if x < 0 ⇒
        throw new JsonException("No json value found till " + stream.location)
      case x ⇒
        throw new JsonException(
          "Illegal start of json value at " + stream.location + " char = " + x)
    }
  }



  /**
   * Reads an explicit literal.
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



  /** Reads a JSON array. */
  private def readJsonArray(stream : JsonStream) : JsonValue = {
    val start = stream.location

    stream.read() // '['

    skipWhites(stream)
    if (stream.peek == ']') {
      stream.read()
      return JsonArray(Seq.empty)
    }

    val items = new scala.collection.mutable.ArrayBuffer[JsonValue]
    items += readValue(stream)

    skipWhites(stream)
    while (stream.peek != ']') {

      if (stream.peek != ',') throw new JsonException(
        "Mailformed array at " + start + ", expected either , or ] at " +
          stream.location + " but got " + stream.peek)

      stream.read() // ','

      items += readValue(stream)
      skipWhites(stream)
    }
    stream.read() // ']'

    JsonArray(items)
  }



  /** Reads a JSON object. */
  private def readJsonObject(stream : JsonStream) : JsonValue = {
    val start = stream.location

    stream.read() // '{'
    skipWhites(stream)

    if (stream.peek() == '}') {
      stream.read()
      return new JsonObject(Map.empty)
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

    JsonObject(defMap.mapValues(x ⇒ x._2).toMap)
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

      if (!stream.eof && (stream.peek == '+' || stream.peek == '-'))
        res += stream.read

      if (stream.eof || !Character.isDigit(stream.peek))
        throw new JsonException("Illegal exp part at " + stream.location)

      while (!stream.eof && Character.isDigit(stream.peek))
        res += stream.read
    }

    JsonNumber(res.toString)
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



  /** Converts json bytes into the reader. */
  private def asJsonReader(bytes : Array[Byte]) : Reader =
    new java.io.InputStreamReader(
      new java.io.ByteArrayInputStream(bytes), sniffEncoding(bytes))



  /** Sniffs a JSON encoding. This is performed according to the
   * RFC-4627, chapter 3.
   */
  private def sniffEncoding(bytes : Array[Byte]) : String = {
    if (bytes.length == 2)
      if (bytes(0) == 0)
        return "UTF-16BE"
      else if (bytes(1) == 0)
        return "UTF-16LE"
      else
        return "UTF-8"
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
}
