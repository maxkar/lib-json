#Simple JSON serialization and deserialization library.

##Use cases

Target use-cases for this library:
 * Parse json into some data object or set of primitives (method arguments, etc...)
 * To generate a new json representation from some objects or simple values

This library aimed to be extensible. For example, if you parse dates is some
specific format, you should be able to provide automatic conversions from and
to the serialized string.


## Type system

Type hierarchy was updated since 0.0.3 zero to provide more convenient API.

The JsonValue is a root of the type hierarchy. It have children for each
concrete JSON type (strintg, number, null, etc...). 

There is also a special JsonUndefined value.  It denotes
"no json value was specified". Usages are:
  * On the object access: to omit exceptions and allow option/null checking.
    I.e. to enable syntax obj.someField match { ... }
  * On the object construction to provide optional values. I.e.
    Json.make('a' -> myOption) where None maps to "JsonUndefined"

JsonNull is different from JsonUndefined. JsonNull is serialized as null value
on the wire. JsonUndefined is not serialized (and not stored). JsonNull
value during the read means an explicit null value where JsonUndefined means
an absence of the value.
