package com.mosl.tabapp.leadcreation.ui

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

interface LeadCreationNavigator {
  fun navigateToHome()
  fun navigateToLeadViewPager(leadId: String)
}

class LeadCreationNavigatorImpl @Inject constructor(
  val screenNavigator: ScreenNavigatorImpl
) : LeadCreationNavigator {

  override fun navigateToHome() {
    screenNavigator.openHome()
  }

  override fun navigateToLeadViewPager(leadId: String) {
    val bundle = Bundle().apply {
      putString(MOSLConstants.LEAD_ID, leadId)
      putString(MOSLConstants.UCID, "0")
    }
    screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true)
  }
}