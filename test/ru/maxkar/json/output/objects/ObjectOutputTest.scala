package ru.maxkar.json.output.objects

import org.scalatest.FunSuite

import ru.maxkar.json.input.objects.{Json ⇒ IJson}
import ru.maxkar.json.input.objects.{JsonObject ⇒ IJsonObject}

import ru.maxkar.json.output.objects._
import ru.maxkar.json.output.objects.implicits._


/** Tests for DOM json output API. */
final class ObjectOutputTest extends FunSuite {
  /** Converts input value into an output value. */
  private def roll(x : JsonValue) : IJsonObject =
    IJson.parseObject(Json.asString(x))


  test("Basic json serialization") {
    var x = Json.make(
      "a" → 3,
      "b" → "test"
    )

    val xx = roll(x)
    assert(3 === xx.a.asInt)
    assert("test" === xx.b.asString)
  }



  test("Conversion of optional values and nulls") {
    val notSet : Option[Int] = None
    val someSet : Option[Int] = Some(443)
    val x = Json.make(
      "b" → null,
      "c" → someSet,
      "d" → notSet)

    val xx = roll(x)
    assert(443 === xx.c.asInt)
    assert(IJson.Null === xx.b)
    assert(IJson.Undefined === xx.d)
  }



  test("Testing json arrays") {
    val a = Json.make("a" → 5)
    val b : JsonValue = 3
    val x = Json.make(
      "arr" → Json.fromArray(Seq(a, b)))

    val xx = roll(x)
    val arr = xx.arr.asArray

    assert(5 === arr(0).asObject.a.asInt)
    assert(3 === arr(1).asInt)
  }
}
