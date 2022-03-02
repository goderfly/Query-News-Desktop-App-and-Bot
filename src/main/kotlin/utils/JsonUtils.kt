package utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

val gSon: Gson by lazy { GsonBuilder().disableHtmlEscaping().create() }

fun Any?.toJson(): String = gSon.toJson(this)

// Convert a String to an Object
inline fun <reified T> String.toObject(): T {
    val type = typeToken<T>()
    return gSon.fromJson(this, type)
}

inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type

// Convert a Map to an Object
inline fun <reified T> Map<String, Any>.toObject(): T = convert()

// Convert an object to a Map
fun <T> T.toMap(): Map<String, Any> {
    return convert()
}

// Convert an object of type T to type R
inline fun <T, reified R> T.convert(): R {
    return gSon.toJson(this).toObject()
}
