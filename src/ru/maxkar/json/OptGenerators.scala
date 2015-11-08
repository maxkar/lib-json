package ru.maxkar.json

/**
 * Optional generators.
 */
trait OptGenerators {
  self : BaseGenerators ⇒

  import Generator._

  implicit val optBooleanGenerator = opt(booleanGenerator)
  implicit val optStringGenerator = opt(stringGenerator)
  implicit val optIntGenerator = opt(intGenerator)
  implicit val optLongGenerator = opt(longGenerator)
  implicit val optDoubleGenerator = opt(doubleGenerator)
  implicit val optSeqGenerator = opt(seqGenerator)
  implicit val optMapGenerator = opt(mapGenerator)
}
