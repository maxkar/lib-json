#Simple JSON serialization and deserialization library.

##Use cases

Target use-cases for this library:
 * Parse json into some data object or set of primitives (method arguments, etc...)
 * To generate a new json representation from some objects or simple values

This library aimed to be extensible. For example, if you parse dates is some
specific format, you should be able to provide automatic conversions from and
to the serialized string.


## Type system

All the concrete Json values are derived from the base type JsonPhantomValue. It
denotes both JsonValue type hierarchy and Undefined value.

Children of JsonValue represent specific types of JSON Literals (strings,
numbers, null, etc...)

JsonUndefined value is subtype of JsonPhantomValue but not of JsonValue. It denotes
"no json value was specified". Usages are:
  * On the object access: to omit exceptions and allow option/null checking.
    I.e. to enable syntax obj.someField match { ... }
  * On the object construction to provide optional values. I.e.
    Json.make('a' -> myOption) where None maps to "JsonUndefined"


JsonNull is different from JsonUndefined. Null is serialized as a null literal
where Undefined is not serialized at all. During the read access Undefined mean
that field is not present and Null mean that field is present and is set to
null.
