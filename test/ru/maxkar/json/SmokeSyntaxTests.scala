package ru.maxkar.json

import org.scalatest.FunSuite

import implicits._

class SmokeSyntaxTests extends FunSuite {
  test("Object could be created in the fluent syntax.") {
    val obj = Json.make("a" → 3, "b" → "test")
  }


  test("Object reads are converted into a proper type.") {
    val obj = Json.make("a" → 3)
    val v : Int = obj.a
    assert(3 === v)
  }


  test("Values are implicitly converted on calls. ") {
    def fn(x : Int) = x
    val obj = Json.make("a" → Json.make("b" → 3))

    assert(3 === fn(obj.a.b))
  }


  test("Options are converted on call.") {
    def fn(x : Option[Int]) = x
    val obj = Json.make("a" → 3)

    assert(None === fn(obj.xyz))
    assert(Some(3) === fn(obj.a))
  }


  test("Undefindes are not present in the object.") {
    val obj = Json.make("a" → JsonUndefined)
    val map = obj.as[JsonObject].values
    assert(false === map.contains("a"))
  }
}
