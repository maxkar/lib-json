package ru.maxkar.json

import implicits._

import org.scalatest.FunSuite


/** Tests for dynamic json API. */
final class DynParsingTest extends FunSuite {
  case class T1(x : String, y : Int)


  test("Basic object parsin and type casting") {
    val json = Json.parse("{\"a\" : 3, \"b\" : \"aoeu\"}")
    val a : Int = json.a
    val b = json.b : String

    assert(a === 3)
    assert(b === "aoeu")
  }


  test("Test element usage in the argument positions.") {
    val json = Json.parse("{\"a\" : 5, \"b\": \"text\"}")
    val r = T1(json.b, json.a)

    assert(r.x === "text")
    assert(r.y === 5)
  }



  test("Test that json accessor could be mapped") {
    val json = Json.parse("{\"a\" : [1, 2, 5, 4]}")
    var r = json.a.as[Seq[JsonValue]].map(x ⇒ x : Int)

    assert(r === Seq(1, 2, 5, 4))
  }


  test("Test difference between json null and undefined") {
    val json = Json.parse("{\"a\" : null}")

    assert(JsonUndefined === json.xxx)
    assert(JsonNull === json.a)
  }


  test("Minimal array is parsed") {
    Json.parse("{}".getBytes("UTF-8"))
    Json.parse("{}".getBytes("UTF-16BE"))
    Json.parse("{}".getBytes("UTF-16LE"))
    Json.parse("{}".getBytes("UTF-32BE"))
    Json.parse("{}".getBytes("UTF-32LE"))
  }


  test("Empty arrays are parsed correctly in many encodings") {
    Json.parse("[]".getBytes("UTF-8"))
    Json.parse("[]".getBytes("UTF-16BE"))
    Json.parse("[]".getBytes("UTF-16LE"))
    Json.parse("[]".getBytes("UTF-32BE"))
    Json.parse("[]".getBytes("UTF-32LE"))
  }


  test("Single-char number is parsed in multiple encodings") {
    val s = "3"
    assert(3 === Json.parse(s.getBytes("UTF-8")).as[Int])
    assert(3 === Json.parse(s.getBytes("UTF-16BE")).as[Int])
    assert(3 === Json.parse(s.getBytes("UTF-16LE")).as[Int])
    assert(3 === Json.parse(s.getBytes("UTF-32BE")).as[Int])
    assert(3 === Json.parse(s.getBytes("UTF-32LE")).as[Int])
  }
}

