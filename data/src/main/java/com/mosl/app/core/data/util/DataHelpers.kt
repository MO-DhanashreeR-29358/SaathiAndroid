package com.mosl.app.core.data.util

import android.content.Context
import android.util.Xml.Encoding.UTF_8
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.*

fun <E> List<E>.safeSubList(
    offSet: Int,
    count: Int
): List<E> {
    if (offSet >= size) return emptyList()
    val toIndex: Int = if ((offSet + count) > size) size else (offSet + count)
    return subList(offSet, toIndex)
}

fun <T> List<T>.addDefaultSelection(selector: T): List<T> {
    return toMutableList().apply {
        if (size > 1) {
            add(0, selector)
        }
    }.toList()
}

fun <T> Sequence<T>.addDefaultSelection(selector: T): List<T> {
    return toMutableList().apply {
        if (size > 1) {
            add(0, selector)
        }
    }.toList()
}

fun <T> List<T>.defaultSelection(selector: T): List<T> {
    return toMutableList().apply {
        add(0, selector)
    }.toList()
}

fun <T> Sequence<T>.defaultSelection(selector: T): List<T> {
    return toMutableList().apply {
        add(0, selector)
    }.toList()
}

inline fun <T> Iterable<T>.findOrFirst(
    predicate: (T) -> Boolean
): T {
    for (element in this) if (predicate(element)) return element
    return first()
}

fun readFile(
    context: Context,
    fileName: String
): String {
    return try {
        val `in` = context.assets.open(fileName)
        val s = Scanner(`in`, UTF_8.name).useDelimiter("\\A")
        if (s.hasNext()) s.next() else ""
    } catch (e: IOException) {
        ""
    }
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(
    json,
    object : TypeToken<T>() {}.type
)!!