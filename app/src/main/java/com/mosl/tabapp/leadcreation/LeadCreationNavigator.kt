package com.mosl.tabapp.leadcreation

import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

interface LeadCreationNavigator {
  fun navigateToHome()
}

class LeadCreationNavigatorImpl @Inject constructor(
  val screenNavigator: ScreenNavigatorImpl
) : LeadCreationNavigator {

  override fun navigateToHome() {
    screenNavigator.openHome()
  }
}