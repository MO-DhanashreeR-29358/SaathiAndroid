package com.mosl.mobile.saathi.core

import com.mosl.navigator.BaseNavigator

interface LoginNavigator : BaseNavigator {
  fun finish()
  fun openDashboard()
}