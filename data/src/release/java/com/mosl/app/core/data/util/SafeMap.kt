package com.mosl.app.core.data.util

public inline fun <T, R : Any> Iterable<T>.safeMap(transform: (T) -> R?): List<R> {
  return safeMapTo(ArrayList<R>(), transform)
}

public inline fun <T, R : Any, C : MutableCollection<in R>> Iterable<T>.safeMapTo(
  destination: C,
  transform: (T) -> R?
): C {
  forEach { element ->
    try {
      transform(element)
    } catch (e: Exception) {
      null
    }?.let { destination.add(it) }
  }
  return destination
}