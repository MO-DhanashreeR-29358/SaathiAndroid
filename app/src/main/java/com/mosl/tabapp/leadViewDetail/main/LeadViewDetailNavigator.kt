package com.mosl.tabapp.leadViewDetail.main

import android.os.Bundle
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

interface LeadViewDetailNavigator {
    fun navigateToSuggestMe(url: String)
    fun openNewEkycApp(id: String)
    fun navigateToCreateMeeting(
        leadId: String,
        ucId: String
    )

    fun navigateToSetAppointment(
        leadId: String,
        ucId: String
    )

    fun openDocument(
        leadId: String,
        ucId: String
    )
}

class LeadViewDetailNavigatorImpl @Inject constructor(
    val screenNavigator: ScreenNavigatorImpl,
    val ekycNavigator: EkycNavigator,
    val userManager: UserManager
) : LeadViewDetailNavigator {

    override fun navigateToSuggestMe(url: String) {
        val bundle = Bundle().apply {
            putString(MOSLConstants.URL_PATH, url)
        }
        screenNavigator.openPage(
            NavigationConstants.NAVIGATE_TO_SUGGEST_ME.toString(),
            bundle,
            true
        )
    }

    override fun openNewEkycApp(id: String) {
        ekycNavigator.openNewEkycApp(true, id)
    }

    override fun navigateToCreateMeeting(
        leadId: String,
        ucId: String
    ) {
        val bundle = Bundle().apply {
            putString(MOSLConstants.LEAD_ID, leadId)
            putString(MOSLConstants.UCID, ucId)
        }
        if(userManager.getUser()?.isIndirectUser() == true) {
            screenNavigator.openPage(
                NavigationConstants.NAVIGATE_TO_INDIRECT_CREATE_MEETING.toString(),
                bundle,
                true
            )
        }else{
            screenNavigator.openPage(
                NavigationConstants.NAVIGATE_TO_CREATE_MEETING.toString(),
                bundle,
                true
            )
        }
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
            NavigationConstants.NAVIGATE_TO_DOCUMENT.toString(),
            bundle,
            true
        )
    }

    override fun navigateToSetAppointment(
        leadId: String,
        ucId: String
    ) {
        val bundle = Bundle().apply {
            putString(MOSLConstants.LEAD_ID, leadId)
            putString(MOSLConstants.UCID, ucId)
        }
        if(userManager.getUser()?.isIndirectUser() == true) {
            screenNavigator.openPage(
                NavigationConstants.NAVIGATE_TO_INDIRECT_APPOINTMENT.toString(),
                bundle,
                true
            )
        }else{
            screenNavigator.openPage(
                NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT.toString(),
                bundle,
                true
            )
        }

    }
}