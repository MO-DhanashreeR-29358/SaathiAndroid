@file:JvmName("DateFormatUtils")
package com.mosl.saathi.domain

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@JvmField
val DATE_TIME_UTC: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
@JvmField
val DF_DAY: DateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
@JvmField
val DF_MONTH: DateFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
@JvmField
val DF_TIME: DateFormat = SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH)


fun DateFormat.formatOrNull(date: Date?): String? {
  if (date == null) return null
  return try {
    format(date)
  } catch (e: Exception) {
    null
  }
}

@JvmOverloads
fun DateFormat.formatOrDefault(date: Date?, default: String = ""): String {
  if (date == null) return default
  return try {
    format(date)
  } catch (e: Exception) {
    default
  }
}

fun DateFormat.parseOrNull(input: String?): Date? {
  if (input.isNullOrBlank()) return null
  return try {
    parse(input)
  } catch (e: Exception) {
    null
  }
}

@JvmOverloads
fun DateFormat.parseOrDefault(input: String?, defaultDate: Date = Date()): Date {
  if (input.isNullOrBlank()) return defaultDate
  return try {
    parse(input)
  } catch (e: Exception) {
    defaultDate
  }
}