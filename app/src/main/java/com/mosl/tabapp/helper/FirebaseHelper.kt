package com.mosl.tabapp.helper

import android.content.Context
import com.crashlytics.android.Crashlytics
import javax.inject.Inject

class FirebaseHelper @Inject constructor(val context: Context) {

  fun setUserProperty(employeeCode: String) {
    Crashlytics.setUserIdentifier(employeeCode)
  }
}
