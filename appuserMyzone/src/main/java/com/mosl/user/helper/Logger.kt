package com.mosl.user.helper

import android.util.Log


interface Logger {
  fun log(message: String)
}

val DEFAULT: Logger = object : Logger {
  override fun log(message: String) {
    Log.d("TokenManager",message)
  }
}