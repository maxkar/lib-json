package ru.maxkar.json.input.objects

import java.io.Reader

import ru.maxkar.json.JsonException

/** Input stream used by the JSON reader. */
private[objects] final class JsonStream(base : Reader) {
  /** Location in the input stream. */
  private val tracker = new LocationTracker()

  /** Next character to return. */
  private var next = base.read()


  /** Checks if this stream is at the EOF. */
  def eof() : Boolean = next < 0


  /** Peeks a next character. Returns a negative value if stream is at EOF. */
  def peek() : Int = next


  /** Reads a next character. Throws an exception if a character could not be read. */
  def read() : Char = {
    if (eof()) throw new JsonException(
      "Unexpected end of file at " + tracker.getLocation())
    val res = next.asInstanceOf[Char]
    tracker.advance(res)
    next = base.read()
    res
  }


  /** Returns a stream location so far. */
  def location() : String = tracker.getLocation()
}
