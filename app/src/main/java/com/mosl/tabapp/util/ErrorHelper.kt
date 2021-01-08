package com.mosl.tabapp.util

import android.view.View
import com.google.android.material.textfield.TextInputLayout

fun View.toggleView(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
}

fun TextInputLayout.disableErrorMessages() {
  isErrorEnabled = false
}

fun TextInputLayout.setErrorMessages(msg: String) {
  error = msg
  isFocusableInTouchMode = true
  requestFocus()
}

