package ru.maxkar.json

/**
 * Set of optional parsers for the types of defined in BaseParsers.
 */
trait OptParsers {
  self : BaseParsers ⇒

  import Parser.opt


  implicit val optBooleanParser = opt(booleanParser)
  implicit val optStringParser = opt(stringParser)
  implicit val optIntParser = opt(intParser)
  implicit val optLongParser = opt(longParser)
  implicit val optDoubleParser = opt(doubleParser)
  implicit val optArrayParser = opt(arrayParser)
  implicit val optSeqParser = opt(seqParser)
  implicit val optObjectParser = opt(objectParser)
  implicit val optMapParser = opt(mapParser)
}
