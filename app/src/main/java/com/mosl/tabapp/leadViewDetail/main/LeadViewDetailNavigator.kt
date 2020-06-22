package com.mosl.tabapp.leadViewDetail.main

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

interface LeadViewDetailNavigator {
  fun navigateToSuggestMe(url: String)
  fun openNewEkycApp(id: String)
  fun navigateToCreateMeeting(data: LeadViewDetailArgument)
  fun navigateToSetAppointment(data: LeadViewDetailArgument)
  fun openDocument(
    leadId: String,
    ucId: String
  )
}

class LeadViewDetailNavigatorImpl @Inject constructor(
  val screenNavigator: ScreenNavigatorImpl,
  val ekycNavigator: EkycNavigator
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

  override fun openNewEkycApp(id: String) {
    ekycNavigator.openNewEkycApp(true, id)
  }

  override fun navigateToCreateMeeting(data: LeadViewDetailArgument) {
    val bundle = Bundle().apply {
      putString(MOSLConstants.LEAD_ID, data.leadId)
      putString(MOSLConstants.UCID, data.ucId)
    }
    screenNavigator.openPage(
        NavigationConstants.NAVIGATE_TO_CREATE_MEETING,
        bundle,
        true
    )
  }

  override fun openDocument(
    leadId: String,
    ucId: String
  ) {
    val bundle = Bundle().apply {
      putString(MOSLConstants.LEAD_ID, leadId)
      putString(MOSLConstants.UCID, ucId)
    }
    screenNavigator.openPage(
        NavigationConstants.NAVIGATE_TO_DOCUMENT,
        bundle,
        true
    )
  }

  override fun navigateToSetAppointment(data: LeadViewDetailArgument) {
    val bundle = Bundle().apply {
      putString(MOSLConstants.LEAD_ID, data.leadId)
      putString(MOSLConstants.UCID, data.ucId)
    }
    screenNavigator.openPage(
        NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT,
        bundle,
        true
    )
  }
}