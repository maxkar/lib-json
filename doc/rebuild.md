# Changing Defaults

The library was designed to provide good-enough conversions between
Scala and Json values. However, it is possible to define your own ways
of converting things.

This task is pretty simple. You define your own set of conversions for
all the types you need and do not import library's implicits.

You may extend Base traits (BaseImplicits, BaseParsers, BaseGenerators) if
you need to change only a few default conversions.
You may also want to mix-in Opt traits (OptImplicits, OptParsers, OptGenerators)
to automatically derive support for Option[T] types based on your
code for T types (applies to default types only).
