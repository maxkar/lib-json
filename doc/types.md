# JSON Types

The library uses an extension of the standard JSON types. All the JSON
types are children of the base `JsonValue` type. The children (leaf) types
are:

 * `JsonNull` - the null literal
 * `JsonTrue` and `JsonFalse` - boolean constants
 * `JsonString` - string literal
 * `JsonNumber` - numeric literal. The library stores each number as the string
   encoding of the value to prevent any data loss (some users may want to
   know a number of trailing zeros in a floating-point number for example).
 * `JsonArray` - array of json values
 * `JsonObject` - json object, a map from strings to JsonValues
 * `JsonUndefined` - a special "undefined" object to support nice 
   access and serialization APIs. This value is not part of the core
   JSON specification but provides support for "optional" values. 
   It is returned on an access to non-existent property. JsonUndefined
   values are ignored when a new Array or Object is created (however,
   JsonNulls are explicitly serialized as null literals)
