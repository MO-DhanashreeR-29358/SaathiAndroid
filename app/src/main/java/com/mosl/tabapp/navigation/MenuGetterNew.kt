package com.mosl.tabapp.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.constants.MOSLConstants.URL_PATH
import com.mosl.tabapp.dashboard.DashboardFragment
import com.mosl.tabapp.dashboard.DashboardHierarchyFragment
import com.mosl.tabapp.dashboard.DashboardSecondLevelFragment
import com.mosl.tabapp.leadViewDetail.DocumentFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.query.QueryFragmentNew
import com.mosl.tabapp.smt.SMTFragment
import com.mosl.tabapp.ui.WebViewFragment
import com.mosl.tabapp.ws.CRMWebServiceHandler

object MenuGetterNew {
  @JvmStatic
  fun getFragment(@NavigationConstants pageId: Int): Fragment? {
    val args = Bundle()
    return when (pageId) {
      NavigationConstants.NAVIGATE_TO_DASHBOARD -> DashboardFragment()
      NavigationConstants.NAVIGATE_TO_FRESH_REG -> null
      NavigationConstants.NAVIGATE_TO_EXISTING_APP -> null
      NavigationConstants.NAVIGATE_TO_CALENDAR -> CalendarFragment()
      NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL -> PendingApprovalFragment()
      NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY -> BirthdayAnniversaryFragment()
      NavigationConstants.NAVIGATE_TO_QUERY -> QueryFragmentNew()
      NavigationConstants.NAVIGATE_TO_SALES_BROCHURE -> WebViewFragment().apply {
        args.putString(URL_PATH, CRMWebServiceHandler.COLLATERAL_URL)
        arguments = args
      }
      NavigationConstants.NAVIGATE_TO_SALES_STADIUM -> WebViewFragment().apply {
        args.putString(URL_PATH, CRMWebServiceHandler.SALES_STADIUM_PITCH_URL)
        arguments = args
      }
      NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD -> DashboardSecondLevelFragment()
      NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT -> DashboardHierarchyFragment()
      NavigationConstants.NAVIGATE_TO_LEAD_CREATION -> LeadCreationFragment()
      NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER -> LeadViewMainFragment()
      NavigationConstants.NAVIGATE_TO_LEAD_VIEW_DETAIL -> LeadViewDetailFragment()
      NavigationConstants.NAVIGATE_TO_DOCUMENT -> DocumentFragment()
      NavigationConstants.NAVIGATE_TO_SMT -> SMTFragment()
      else -> null
    }
  }

  @JvmStatic
  fun isHandled(@NavigationConstants pageId: Int): Boolean {
    return when (pageId) {
      NavigationConstants.NAVIGATE_TO_DASHBOARD -> true
      NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING -> true
      NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING_REPORT -> true
      NavigationConstants.NAVIGATE_TO_SALES_STADIUM -> true
      NavigationConstants.NAVIGATE_TO_SALES_BROCHURE -> true
      NavigationConstants.NAVIGATE_TO_CALENDAR -> true
      NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL -> true
      NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY -> true
      NavigationConstants.NAVIGATE_TO_QUERY -> true
      NavigationConstants.NAVIGATE_TO_SUGGEST_ME -> true
      NavigationConstants.NAVIGATE_TO_LOGOUT -> true
      NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD -> true
      NavigationConstants.NAVIGATE_TO_LEAD_CREATION -> true
      NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER -> true
      NavigationConstants.NAVIGATE_TO_LEAD_VIEW_DETAIL -> true
      NavigationConstants.NAVIGATE_TO_DOCUMENT -> true
      NavigationConstants.NAVIGATE_TO_SMT -> true
      else -> return false
    }
  }
}