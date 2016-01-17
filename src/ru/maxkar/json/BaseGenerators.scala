package ru.maxkar.json

/**
 * Base json generators. These could be inherited by custom DSLs. Then users
 * would be able to import custom DSL only without need to export both DSL and
 * the base items.
 */
trait BaseGenerators {

  implicit val booleanGenerator = Generator[Boolean](x ⇒
    if (x) JsonTrue else JsonFalse)

  implicit val stringGenerator = Generator[String](x ⇒
    if (x == null) JsonNull else JsonString(x))

  implicit val intGenerator = Generator[Int](x ⇒ JsonNumber(x.toString))

  implicit val longGenerator = Generator[Long](x ⇒ JsonNumber(x.toString))

  implicit val doubleGenerator = Generator[Double](x ⇒ JsonNumber(x.toString))

  implicit val seqGenerator = Generator[Seq[JsonPhantomValue]](Json.array)

  implicit val mapGenerator =
    Generator[Map[String, JsonPhantomValue]](x ⇒
      Json.make(x.toSeq : _*))
}
