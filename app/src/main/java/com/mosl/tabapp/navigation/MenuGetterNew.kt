package com.mosl.tabapp.navigation

import androidx.fragment.app.Fragment
import com.mosl.tabapp.dashboard.DashboardFragment

object MenuGetterNew {
  @JvmStatic
  fun getFragment(@NavigationConstants pageId: Int): Fragment? {
    return when (pageId) {
      NavigationConstants.NAVIGATE_TO_DASHBOARD -> DashboardFragment()
      else -> null
    }
  }

  @JvmStatic
  fun isHandled(@NavigationConstants pageId: Int): Boolean {
    return when (pageId) {
      NavigationConstants.NAVIGATE_TO_DASHBOARD -> true
      else -> return false
    }
  }
}