package ru.maxkar.json

/** Tests for the json parsing. */
final class ParsingTest extends org.scalatest.FunSuite {
  test("Negative exponent is parsed") {
    Json.parse("{\"a\":13E-5}")
  }

  test("Positive exponent is parsed") {
    Json.parse("{\"a\":13E+4}")
  }

  test("No-sign exponent is parsed") {
    Json.parse("{\"a\":13E4}")
  }
}
