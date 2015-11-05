package ru.maxkar.json

import java.io.IOException

/**
 * Exception in JSON handling or processing. Json parsing or interpretation
 * is the main source of these exceptions. A root cause is the incorrect data
 * format of the incoming request hence the inheritance from the IO exception.
 * Inability to access/generated json is a common kind of "input" error.
 */
class JsonException(msg : String) extends IOException(msg)
