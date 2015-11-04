package ru.maxkar.json.output.objects

import java.io.Writer

/**
 * String to be written into the json stream.
 */
private[objects] final class JsonString(s : String) extends JsonValue() {
  override def writeTo(w : Writer) : Unit =
    Json.writeString(w, s)
}
