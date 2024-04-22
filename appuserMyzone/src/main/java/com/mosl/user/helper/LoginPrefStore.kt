package com.mosl.user.helper

import android.content.SharedPreferences
import com.mosl.user.prefs.LongPreference
import com.mosl.user.prefs.SafeStringPreference
import com.mosl.user.prefs.StringPreference

interface PrefStore {
  fun clear()
}

data class LoginPrefStore(
  @JvmField val loginTime: LongPreference,
  @JvmField val emailId: StringPreference
) : PrefStore {
  override fun clear() {
    loginTime.delete()
    emailId.delete()
  }
}

data class TokenPrefStore(
  @JvmField val tokenCreationTime: LongPreference,
  @JvmField val accessToken: SafeStringPreference,
  @JvmField val tokenValidity: LongPreference,
  @JvmField val refreshToken: SafeStringPreference,
  @JvmField val reLoginDuration: LongPreference
) : PrefStore {
  override fun clear() {
    tokenCreationTime.delete()
    accessToken.delete()
    tokenValidity.delete()
    refreshToken.delete()
    reLoginDuration.delete()
  }

  companion object {
    const val PREF_LOGIN_DEVICE_TIME = "last_login_device_time"
    const val PREF_EMAIL_ID = "emp_email_id"
    const val PREF_RE_LOGIN_DURATION = "re_login_duration"

    const val PREF_TOKEN_VALIDITY = "token_validity_time"
    const val PREF_TOKEN_CREATED_TIME = "token_creation_time"
    const val PREF_ACCESS_TOKEN = "access_token"
    const val PREF_REFRESH_TOKEN = "refresh_token"

    fun createLoginPrefStore(preference: SharedPreferences): LoginPrefStore {
      return LoginPrefStore(
        loginTime = LongPreference(preference, PREF_LOGIN_DEVICE_TIME, 0),
        emailId = StringPreference(preference, PREF_EMAIL_ID, null)
      )
    }

    fun createTokenPrefStore(
      preference: SharedPreferences
    ): TokenPrefStore {
      return TokenPrefStore(
        tokenCreationTime = LongPreference(preference, PREF_TOKEN_CREATED_TIME, 0),
        accessToken = SafeStringPreference(preference, PREF_ACCESS_TOKEN),
        tokenValidity = LongPreference(preference, PREF_TOKEN_VALIDITY, 0),
        refreshToken = SafeStringPreference(preference, PREF_REFRESH_TOKEN),
        reLoginDuration = LongPreference(preference, PREF_RE_LOGIN_DURATION, 0)
      )
    }
  }
}