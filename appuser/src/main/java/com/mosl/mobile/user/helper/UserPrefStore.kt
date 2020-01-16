package com.mosl.mobile.user.helper

import android.content.SharedPreferences
import com.mosl.app.core.prefs.PrefStore
import com.mosl.app.core.prefs.StringPreference

data class UserPrefStore(
  @JvmField val userInfo: StringPreference,
  @JvmField val reportMenuList: StringPreference
) : PrefStore {
  override fun clear() {
    userInfo.delete()
    reportMenuList.delete()
  }
}

object PrefStoreProvider {
  const val PREF_USER_INFO = "user_info"
  const val PREF_REPORT_MENU_LIST = "report_menu_list"

  fun createUserPrefStore(
    preference: SharedPreferences
  ): UserPrefStore {
    return UserPrefStore(
        userInfo = StringPreference(preference, PREF_USER_INFO),
        reportMenuList = StringPreference(preference, PREF_REPORT_MENU_LIST)
    )
  }

}