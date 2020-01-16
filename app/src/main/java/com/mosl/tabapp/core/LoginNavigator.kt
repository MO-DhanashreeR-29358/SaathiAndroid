package com.mosl.tabapp.core

import com.mosl.navigator.BaseNavigator

interface LoginNavigator : BaseNavigator {
  fun finish()
  fun openDashboard()
}