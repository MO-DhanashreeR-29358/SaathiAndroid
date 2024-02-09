package com.mosl.user.helper

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock

const val NO_AUTH_HEADER = "No-Auth: true"

fun Context.getPref(name: String): SharedPreferences {
  return getSharedPreferences(name, Context.MODE_PRIVATE)
}

sealed class AccessToken {
  object OnRefresh : AccessToken()
  object OnUnauthorized : AccessToken()
  object OnExpire : AccessToken()
  object OnApiCall : AccessToken()
}

sealed class TokenEvents {
  object Logout : TokenEvents()
  object ReInitiate : TokenEvents()
}

object GetAccessToken : TokenEvents()
object GetRefreshToken : TokenEvents()
object AccessTokenSaved : TokenEvents()

fun wasDeviceRebooted(savedTime: Long): Boolean {
  val realTime = SystemClock.elapsedRealtime()
  return savedTime >= realTime
}