# Quick Start

## Build and Install

The library is not hosted in any library repositories so it should 
be built and installed into the repository you use.

The simplest way is to install the library locally:

 1. Check out the library code.
 2. Checkout the tag (version of the library) you want to build.
 3. Run the `sbt publishLocal` command inside the project directory
   to publish the library into your local repository.

## Add the Library Dependency

Add the library dependency into your build definition:

```
libraryDependencies += "ru.maxkar" %% "lib-json" % "0.0.3-SNAPSHOT"
```

The actual library version may vary.


## Use the Library

First, you have to import the most important definitions:

```
import ru.maxkar.json.Json
import ru.maxkar.json.JsonValue
import ru.maxkar.json.implicits._
```

The `Json` object is an entry point for reading and writing JSON.\
JsonValue is a base type for all the JSON values supported by the
library. `implicits` package has a definition of all the standard
(default) conversions between scala and JSON types.


Now you could parse and build json objects:

```
val myJson = Json.parse("""{"a" : 3, "arr" : ["test"], "nested" : {"obj" : 3}})
val a1 = myJson.a.as[Int] // specify the expected type
val a2 : Int = myJson.a // and there is an implicit conversion
val nested = myJson.nested.obj.as[Int] // traverse an object
val v1 : Option[Int] = myJson.nonExistent // reading optional values
val v2 : Option[Int] = myJson.a // also reading optionals
val items = json.arr.as[Seq[JsonValue]].map(_.as[String]) // handling collections
val jsonMap : Map[String, JsonValue] = myJson.nested // object could be treated as maps

val json1 : JsonValue = Json.make(
  "a" -> a1, // put a value
  "b" -> v1, // optionally put a value
  "c" -> items.map(i => i : JsonValue) // put a collection of values
)
```
