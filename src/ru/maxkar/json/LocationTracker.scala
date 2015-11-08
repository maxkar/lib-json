package ru.maxkar.json

/**
 * Tracker for the input stream location. Used by the JSON parser
 * to provide good error messages.
 */
private[json] final class LocationTracker {
  /** Absolute offset of the character in the input stream. */
  private var charOffset = 0l

  /** Column in the input document. */
  private var column = 1

  /** Row in the input document. */
  private var row = 1

  /** True if we are after the CR character (it could be a part of CR+LF combination). */
  private var afterCR = false


  /** Updates the position based on the input character being read. */
  def advance(c : Char) : Unit =  {
    charOffset += 1

    c match {
      case '\n' ⇒
        if (afterCR) afterCR = false
        else toNextLine()
      case '\r' ⇒
        toNextLine()
        afterCR = true
      case _ ⇒
        column += 1
        afterCR = false
    }
  }


  /** Returns a textual representation of the current location. */
  def getLocation() : String =
    "(" + row + ":" + column + ", offest = " + charOffset + ")"


  /** Advanced to a next line. */
  private def toNextLine() : Unit = {
    column = 1
    row += 1
  }
}

