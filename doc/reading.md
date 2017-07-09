# Reading JSON

## Parsing Input

The input is parsed by using one of the `Json.parse` methods. The library automatically
detects valid JSON encoding (one of UTF-8 variants) for the overload using array of bytes.
The method supports both complex (arrays and objects) and simple (numbers and strings) values.


## Accessing Members

The memeber accessors (field and index) are defined on the `JsonValue` type.
Applying accessor to the `JsonUndefined` value is always valid and will produce
the `JsonUndefined` value.
Attempt to use an accessor on a primitive value or on a value not supporting
given accessor will provide `JsonUndefined`


### Field Accessors

There are two ways to access a field of an object:

```
val x : JsonValue = ???
val value1 = x.member // field-based syntax
val value2 = x("member") // apply-based syntax
```

The field-based syntax should be used when possible. The apply-based
syntax may be used if the field name is generated dynamically
or if the field name is not valid Scala identifier.

The resulting value is as follows:

 * If x is JsonObject and the field `member` is defined - the falue of the field
 * Otherwise a `JsonUndefined` is returned

### Indexed Accessor

Retrieving the value by index is done in the following way:

```
val x : JsonValue = ???
val v = x(5)
```

The resulting value is as follows:

 * If x is JsonArray and index is inside array bounds (0 to length - 1) then the value is returned
 * Otherwise a `JsonUndefined` is returned

Note that negative index has no special meaning. Passing a negative index will always result
in JsonUndefined value.


## Converting Values

The value could be cast to a required type by either exlicitly using "as" method
or by using JsonValue in a context where some other type is expected. The standard
set of conversions is consistent so if there is an implpicit conversion from
JsonValue to T then you could also use `as` conversion and vice versa.

All the following are valid conversions:

```
def test(v : Int) = ???
val x : JsonValue = ???

val a = x.as[Int]
val b : Int = x
test(x)
```

The "Optional" converters works as follows:
 * If the value is `JsonUndefined` or `JsonNull` - None
 * Value is defined and is compatible to the expected type (Number for Ints, Strings for strings) - Some(value)
 * Otherwise a `JsonException` is raised.

In particular, it means that an attempt to convert JsonNumber to Option[String] will cause an exception
(there is no automatic conversion from numbers to string), not the None value.
