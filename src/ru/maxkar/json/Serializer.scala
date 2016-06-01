package ru.maxkar.json

import java.io.Writer

/** Serialization utilities. */
private[json] object Serializer {
  private val HEX_CHARS = "0123456789ABCDEF"

  /** Writes a json value. */
  def write(value : JsonValue, stream : Writer) : Unit =
    value match {
      case JsonUndefined ⇒ throw new JsonException("Could not write undefined")
      case JsonNull ⇒ stream write "null"
      case JsonTrue ⇒ stream write "true"
      case JsonFalse ⇒ stream write "false"
      case JsonNumber(x) ⇒ stream write x
      case JsonString(x) ⇒ Serializer.writeString(x, stream)
      case JsonArray(items) ⇒
        stream write "["
        val itr = items.iterator
        if (itr.hasNext)
          write(itr.next, stream)
        while (itr.hasNext) {
          stream write ","
          write(itr.next, stream)
        }
        stream write "]"
      case JsonObject(map) ⇒
        stream write "{"
        val itr = map.iterator
        if (itr.hasNext)
          writeEntry(itr.next, stream)
        while (itr.hasNext) {
          stream write ","
          writeEntry(itr.next, stream)
        }
        stream write "}"
    }



  /** Writes a single object entry into the stream. */
  private def writeEntry(entry : (String, JsonValue), stream : Writer) : Unit = {
    writeString(entry._1, stream)
    stream write ":"
    write(entry._2, stream)
  }




  /** Writes a string into the output stream with proper encoding. */
  private def writeString(s : String, w : Writer) : Unit = {
    if (s == null) {
      w write "null"
      return
    }

    w write '"'

    val len = s.length
    var ptr = 0
    while (ptr < s.length) {
      writeChar(w, s charAt ptr)
      ptr += 1
    }

    w write '"'
  }



  /** Outputs one character into the stream escaping it if needed. */
  private def writeChar(w : Writer, c : Char) : Unit =
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
  private def toHexDigit(x : Int) : Char = HEX_CHARS charAt x
}
