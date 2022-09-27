package com.mosl.mobile.user.helper

import android.content.SharedPreferences
import com.mosl.app.core.prefs.PrefStore
import com.mosl.app.core.prefs.StringPreference

data class UserPrefStore(
  @JvmField val userInfo: StringPreference,
  @JvmField val reportMenuList: StringPreference,
  @JvmField val digitalSalesKitMenuList: StringPreference
) : PrefStore {
  override fun clear() {
    userInfo.delete()
    reportMenuList.delete()
    digitalSalesKitMenuList.delete()
  }
}

object PrefStoreProvider {
  const val PREF_USER_INFO = "user_info"
  const val PREF_REPORT_MENU_LIST = "report_menu_list"
  const val PREF_DIGITAL_SALES_KIT_MENU_LIST = "digital_sales_kit_menu_list"
  fun createUserPrefStore(
    preference: SharedPreferences
  ): UserPrefStore {
    return UserPrefStore(
        userInfo = StringPreference(preference, PREF_USER_INFO),
        reportMenuList = StringPreference(preference, PREF_REPORT_MENU_LIST),
        digitalSalesKitMenuList = StringPreference(preference, PREF_DIGITAL_SALES_KIT_MENU_LIST)
    )
  }

}