package com.mosl.mobile.user.helper

import android.content.SharedPreferences
import com.mosl.app.core.prefs.PrefStore
import com.mosl.app.core.prefs.StringPreference

data class UserPrefStore(
  @JvmField val userInfo: StringPreference
) : PrefStore {
  override fun clear() {
    userInfo.delete()
  }
}

object PrefStoreProvider {
  const val PREF_USER_INFO = "user_info"

  fun createUserPrefStore(
    preference: SharedPreferences
  ): UserPrefStore {
    return UserPrefStore(
        userInfo = StringPreference(preference, PREF_USER_INFO)
    )
  }

}