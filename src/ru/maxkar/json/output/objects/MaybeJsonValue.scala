package ru.maxkar.json.output.objects

import java.io.Writer

/**
 * Predecessor of the values which could be used in optional-aware contexts
 * (primarily object construction).
 */
class MaybeJsonValue private[objects]() {
  /** Sets to true in values and to false for "none" values. */
  private[objects] val isDefined : Boolean = false


  /** Writes this value into the stream (if it is defined).
   * This "maybe value" does not write anything so attempt to output
   * it would cause an exception.
   * This method is present here to avoid typecasting in JSON object.
   */
  private[objects] def writeTo(writer : Writer) : Unit =
    throw new AssertionError("Maybe value should never be written")
}
