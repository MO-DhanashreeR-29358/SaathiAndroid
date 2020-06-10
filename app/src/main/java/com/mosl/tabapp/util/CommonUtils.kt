package com.mosl.tabapp.util

import android.widget.EditText

fun emptyString() = ""

fun EditText.string(): String {
  return text.toString()
}