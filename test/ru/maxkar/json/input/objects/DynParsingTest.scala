package ru.maxkar.json.input.objects

import org.scalatest.FunSuite

import ru.maxkar.json.input.objects._
import ru.maxkar.json.input.objects.implicits._


/** Tests for dynamic json API. */
final class DynParsingTest extends FunSuite {
  case class T1(x : String, y : Int)


  test("Basic object parsin and type casting") {
    val json = Json.parseObject("{\"a\" : 3, \"b\" : \"aoeu\"}")
    val a : Int = json.a
    val b = json.b : String

    assert(a === 3)
    assert(b === "aoeu")
  }


  test("Test element usage in the argument positions.") {
    val json = Json.parseObject("{\"a\" : 5, \"b\": \"text\"}")
    val r = T1(json.b, json.a)

    assert(r.x === "text")
    assert(r.y === 5)
  }



  test("Test that json accessor could be mapped") {
    val json = Json.parseObject("{\"a\" : [1, 2, 5, 4]}")
    var r = json.a.asArrayOf(x ⇒ x : Int)

    assert(r === Seq(1, 2, 5, 4))
  }


  test("Test difference between json null and undefined") {
    val json = Json.parseObject("{\"a\" : null}")

    assert(Json.Undefined === json.xxx)
    assert(Json.Null === json.a)
  }


  test("Minimal array is parsed") {
    Json.parseObject("{}".getBytes("UTF-8"))
    Json.parseObject("{}".getBytes("UTF-16BE"))
    Json.parseObject("{}".getBytes("UTF-16LE"))
    Json.parseObject("{}".getBytes("UTF-32BE"))
    Json.parseObject("{}".getBytes("UTF-32LE"))
  }


  test("Empty arrays are parsed correctly in many encodings") {
    Json.parseArray("[]".getBytes("UTF-8"))
    Json.parseArray("[]".getBytes("UTF-16BE"))
    Json.parseArray("[]".getBytes("UTF-16LE"))
    Json.parseArray("[]".getBytes("UTF-32BE"))
    Json.parseArray("[]".getBytes("UTF-32LE"))
  }
}

