package com.mosl.shared.util

import java.util.Locale

fun Double.toDecimalsOrDash(decimals: Int = 2): String {
  return if (this == 0.0) "0" else String.format(Locale.ENGLISH, "%.${decimals}f", this)
}

fun String.toConvertDecimals(): String {
  val decimalData: String
  if (indexOf(".") > -1 && !endsWith(".0")
    && !endsWith(".00")
  ) {
    decimalData = String.format(Locale.ENGLISH, "%.2f", toDouble())
  } else {
    decimalData = String.format(Locale.ENGLISH, "%.0f", toDouble())
  }
  return decimalData
}