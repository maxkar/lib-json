package ru.maxkar.json

import scala.language.implicitConversions

/** Default implicit conversions. */
object implicits
    extends BaseImplicits
    with OptImplicits
    with BaseParsers
    with OptParsers
    with BaseGenerators
    with OptGenerators {

  /** Conversion from type to JSON. */
  implicit def typeToJson[T : Generator](v : T) : JsonPhantomValue =
    implicitly[Generator[T]].create(v)
}
