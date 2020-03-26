package com.mosl.tabapp.helper

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseHelper @Inject constructor(val context: Context) {
  private lateinit var mFirebaseAnalytics: FirebaseAnalytics

  fun setUserProperty(employeeCode: String) {
    mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    mFirebaseAnalytics.setUserId(employeeCode)
  }
}
