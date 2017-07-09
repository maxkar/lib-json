# Extending Conversions

The default set of conversions is provided by the library. However,
clients may want to provide their own conversions. It is usually advised
to explicitly call conversion methods for user types. However,
the library does not enforce that and for some "basic" types
(like dates) an automatic conversion could be desirable.

This type of extension is done by providing implicit values and
implicit conversions.

## Scala to Json

To define a conversion from Scala to Json you have to define an
instance of `Generator` for your type. Usually you also want to 
create a Generator for your optional value. The typical code
will look like:

```
implicit val dateGenerator = Generator[Date](dt ⇒ JsonString(dt.toString))
implicit val optDateGenerator = Generator.opt(dateGenerator)
```


## Json to Scala

To define a conversion from Json to a Scala type you have to define two
things:

 * An instance of Parser for your type (used in .as[T] calls)
 * An implicit conversion form Json to your type (used in implicit conversions)


You may also want to define optional converters for your type.

A typical code will look like:

```
implicit val dateParser = Parser[Date](json ⇒ new Date(json.as[String]))
implicit val optDateParser = Parser.opt(dateParser)

implicit def jsonToDate(x : JsonValue) : Date = x.as[Date]
implicit def jsonToOptDate(x : JsonValue) : Option[Date] = x.as[Option[Date]]
```
