package ru.maxkar.json.output.objects

import java.io.Writer

/**
 * Representation of the primitive JSON value.
 * @param repr string representation of the value.
 */
private[objects] class JsonPrimitive (repr : String)
    extends JsonValue() {

  override private[objects] def writeTo(writer : Writer) : Unit =
    writer write repr
}
