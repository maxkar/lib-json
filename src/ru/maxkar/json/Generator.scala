package ru.maxkar.json

/**
 * Generator of Json values. Creates a "possible" json value
 * from the initial one. It returns JsonPhantomValue to support
 * optional value generation (optional keys in the output, etc...).
 * @param create function used to create a json value.
 */
final case class Generator[T](val create : T ⇒ JsonPhantomValue)



/**
 * Utilities used in Json Generation.
 */
object Generator {
  /**
   * Creates a serializer for the optional value. None would be converted
   * into JsonUndefined, otherwize a base parser would be applied.
   */
  def opt[T](base : Generator[T]) : Generator[Option[T]] =
    Generator[Option[T]](v ⇒ v match {
      case None ⇒ JsonUndefined
      case Some(x) ⇒ base.create(x)
    })


  /**
   * Creates a serializer for the json array.
   */
  def array[T](base : Generator[T]) : Generator[Seq[T]] =
    Generator[Seq[T]](v ⇒
      Json.array(v.map(base.create) : _*))
}
