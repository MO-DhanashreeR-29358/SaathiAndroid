package com.mosl.tabapp.leadViewDetail.main

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

interface LeadViewDetailNavigator {
  fun navigateToSuggestMe(url: String)
}

class LeadViewDetailNavigatorImpl @Inject constructor(
  val screenNavigator: ScreenNavigatorImpl
) : LeadViewDetailNavigator {

  override fun navigateToSuggestMe(url: String) {
    val bundle = Bundle().apply {
      putString(MOSLConstants.URL_PATH, url)
    }
    screenNavigator.openPage(
        NavigationConstants.NAVIGATE_TO_SUGGEST_ME,
        bundle,
        true
    )
  }
}