package com.mosl.shared.util

import java.util.Locale

fun Double.toDecimalsOrDash(decimals: Int = 2): String {
  return if (this == 0.0) "0" else String.format(Locale.ENGLISH, "%.${decimals}f", this)
}

fun Double.toRemoveDecimals(decimals: Int = 0): String {
  return if (this == 0.0) "0" else String.format(Locale.ENGLISH, "%.${decimals}f", this)
}