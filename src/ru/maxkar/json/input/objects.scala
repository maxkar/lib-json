package ru.maxkar.json.input

/**
 * <p>Object-like Json input API. This api resembles javascript in Json
 *   handling. It supports primivite values, arrays and objects.</p>
 * <p>This API does not throw an exception on access to "undefiend" properties
 *   in arrays and objects. It returns an <code>JsonUndefied</code> instead.
 *   This value is similar to <code>JsonNull</code>(denoting null) in all
 *   aspects. It treated as <code>None</code> on optional access and throws
 *   an exception when converted into an object.</p>
 */
package object objects {
}
