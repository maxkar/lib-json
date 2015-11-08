package ru.maxkar.json

import implicits._

import org.scalatest.FunSuite



/** Tests for DOM json output API. */
final class ObjectOutputTest extends FunSuite {
  /** Converts input value into an output value. */
  private def roll(x : JsonPhantomValue) : JsonValue =
    Json.parse(Json.toString(x))


  test("Basic json serialization") {
    var x = Json.make(
      "a" → 3,
      "b" → "test"
    )

    val xx = roll(x)
    assert(3 === xx.a.as[Int])
    assert("test" === xx.b.as[String])
  }



  test("Conversion of optional values and nulls") {
    val notSet : Option[Int] = None
    val someSet : Option[Int] = Some(443)
    val x = Json.make(
      "b" → null,
      "c" → someSet,
      "d" → notSet)

    val xx = roll(x)
    assert(443 === xx.c.as[Int])
    assert(JsonNull === xx.b)
    assert(JsonUndefined === xx.d)
  }



  test("Testing json arrays") {
    val a = Json.make("a" → 5)
    val b : JsonPhantomValue = 3
    val x = Json.make(
      "arr" → Json.array(a, b))

    val xx = roll(x)
    val arr = xx.arr.as[Seq[JsonValue]]

    assert(5 === arr(0).a.as[Int])
    assert(3 === arr(1).as[Int])
  }
}
