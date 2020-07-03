package com.mosl.tabapp.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeetingreport.BankMeetingReportFragmentNew
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment
import com.mosl.tabapp.calendar.CalendarFragment
import com.mosl.tabapp.calendar.calendarappoinslist.CalendarAppointsListFragment
import com.mosl.tabapp.dashboard.base.DashboardFragment
import com.mosl.tabapp.dashboard.report.DashboardReportFragment
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignFragment
import com.mosl.tabapp.dashboard.report.hierarchy.DashboardHierarchyReportFragment
import com.mosl.tabapp.leadViewDetail.CreateMeetingFragment
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment
import com.mosl.tabapp.leadViewDetail.activitydetail.LeadActivitiesDetailFragment
import com.mosl.tabapp.leadViewDetail.document.DocumentFragment
import com.mosl.tabapp.leadViewDetail.updatemeeting.UpdateMeetingFragment
import com.mosl.tabapp.leadcreation.LeadCreationFragment
import com.mosl.tabapp.notification.NotificationFragment
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment
import com.mosl.tabapp.pendingApproval.report.PendingForApprovalFragment
import com.mosl.tabapp.query.QueryFragment
import com.mosl.tabapp.report.ReportFragment
import com.mosl.tabapp.report.hierarchy.HierarchyReportFragment
import com.mosl.tabapp.setappointment.SetAppointmentFragment
import com.mosl.tabapp.smt.SMTFragment
import com.mosl.tabapp.webview.WebviewFragment
import com.mosl.tabapp.webview.WebviewFragment.Companion.CHECK_APPROVAL_ID
import com.mosl.tabapp.webview.WebviewFragment.Companion.EXISTING_APPLICATIONS_ID
import com.mosl.tabapp.webview.WebviewFragment.Companion.FRESH_REGISTRATION_ID
import com.mosl.tabapp.webview.WebviewFragment.Companion.INCENTIVE_REPORT_ID
import com.mosl.tabapp.ws.CRMWebServiceHandler

object MenuGetterNew {
    @JvmStatic
    fun getFragment(@NavigationConstants pageId: Int): Fragment? {
        val args = Bundle()
        return when (pageId) {
            NavigationConstants.NAVIGATE_TO_DASHBOARD -> DashboardFragment()
            NavigationConstants.NAVIGATE_TO_FRESH_REG -> WebviewFragment().apply {
                args.putString(WebviewFragment.PAGE_ID, FRESH_REGISTRATION_ID)
                arguments = args
            }
            NavigationConstants.NAVIGATE_TO_EXISTING_APP -> WebviewFragment().apply {
                args.putString(WebviewFragment.PAGE_ID, EXISTING_APPLICATIONS_ID)
                arguments = args
            }
            NavigationConstants.NAVIGATE_TO_CALENDAR -> CalendarFragment()
            NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL -> PendingApprovalFragment()
            NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY -> BirthdayAnniversaryFragment()
            NavigationConstants.NAVIGATE_TO_QUERY -> QueryFragment()
            NavigationConstants.NAVIGATE_TO_SALES_BROCHURE -> WebviewFragment().apply {
                args.putString(WebviewFragment.URL, CRMWebServiceHandler.COLLATERAL_URL)
                arguments = args
            }
            NavigationConstants.NAVIGATE_TO_SALES_STADIUM -> WebviewFragment().apply {
                args.putString(WebviewFragment.URL, CRMWebServiceHandler.SALES_STADIUM_PITCH_URL)
                arguments = args
            }
            NavigationConstants.NAVIGATE_TO_INCENTIVE_REPORT -> WebviewFragment().apply {
                args.putString(WebviewFragment.PAGE_ID, INCENTIVE_REPORT_ID)
                arguments = args
            }
            NavigationConstants.NAVIGATE_TO_CHECK_APPROVAL -> WebviewFragment().apply {
                args.putString(WebviewFragment.PAGE_ID, CHECK_APPROVAL_ID)
                arguments = args
            }
            NavigationConstants.NAVIGATE_TO_MOST_GUIDE -> WebviewFragment().apply {
                args.putString(WebviewFragment.PAGE_ID, WebviewFragment.MOST_GUIDE_ID)
                arguments = args
            }
            NavigationConstants.NAVIGATE_TO_RESUME_APPLICATION -> WebviewFragment()
            NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD -> DashboardReportFragment()
            NavigationConstants.NAVIGATE_TO_LEAD_ASSIGN_STP -> DashboardReportAssignFragment()
            NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT -> DashboardHierarchyReportFragment()
            NavigationConstants.NAVIGATE_TO_LEAD_CREATION -> LeadCreationFragment()
            NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_REPORT -> HierarchyReportFragment()
            NavigationConstants.NAVIGATE_TO_PENDING_FOR_APPROVAL -> PendingForApprovalFragment()
            NavigationConstants.NAVIGATE_TO_NOTIFICATION -> NotificationFragment()
            NavigationConstants.NAVIGATE_TO_REPORTS -> ReportFragment()
            NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER -> LeadViewMainFragment()
            NavigationConstants.NAVIGATE_TO_LEAD_VIEW_DETAIL -> LeadViewDetailFragment()
            NavigationConstants.NAVIGATE_TO_DOCUMENT -> DocumentFragment()
            NavigationConstants.NAVIGATE_TO_SUGGEST_ME -> SMTFragment()
            NavigationConstants.NAVIGATE_TO_UPDATE_MEETING -> UpdateMeetingFragment()
            NavigationConstants.NAVIGATE_TO_LEAD_ACTIVITIES_DETAILS -> LeadActivitiesDetailFragment()
            NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT -> SetAppointmentFragment()
            NavigationConstants.NAVIGATE_TO_CREATE_MEETING -> CreateMeetingFragment()
            NavigationConstants.NAVIGATE_TO_CALENDER_LIST -> CalendarAppointsListFragment()
            NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING -> BankMeetingFragment()
            NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING_REPORT -> BankMeetingReportFragmentNew()
            NavigationConstants.NAVIGATE_TO_SUGGEST_ME_MENU -> null
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
            NavigationConstants.NAVIGATE_TO_FRESH_REG -> true
            NavigationConstants.NAVIGATE_TO_EXISTING_APP -> true
            NavigationConstants.NAVIGATE_TO_SALES_BROCHURE -> true
            NavigationConstants.NAVIGATE_TO_CALENDAR -> true
            NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL -> true
            NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY -> true
            NavigationConstants.NAVIGATE_TO_QUERY -> true
            NavigationConstants.NAVIGATE_TO_LOGOUT -> true
            NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD -> true
            NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT -> true
            NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_REPORT -> true
            NavigationConstants.NAVIGATE_TO_PENDING_FOR_APPROVAL -> true
            NavigationConstants.NAVIGATE_TO_NOTIFICATION -> true
            NavigationConstants.NAVIGATE_TO_REPORTS -> true
            NavigationConstants.NAVIGATE_TO_LEAD_CREATION -> true
            NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER -> true
            NavigationConstants.NAVIGATE_TO_LEAD_VIEW_DETAIL -> true
            NavigationConstants.NAVIGATE_TO_DOCUMENT -> true
            NavigationConstants.NAVIGATE_TO_SUGGEST_ME -> true
            NavigationConstants.NAVIGATE_TO_SUGGEST_ME_MENU -> true
            NavigationConstants.NAVIGATE_TO_UPDATE_MEETING -> true
            NavigationConstants.NAVIGATE_TO_LEAD_ACTIVITIES_DETAILS -> true
            NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT -> true
            NavigationConstants.NAVIGATE_TO_CREATE_MEETING -> true
            NavigationConstants.NAVIGATE_TO_CALENDER_LIST -> true
            NavigationConstants.NAVIGATE_TO_INCENTIVE_REPORT -> true
            NavigationConstants.NAVIGATE_TO_RESUME_APPLICATION -> true
            NavigationConstants.NAVIGATE_TO_CHECK_APPROVAL -> true
            NavigationConstants.NAVIGATE_TO_LEAD_ASSIGN_STP -> true
            NavigationConstants.NAVIGATE_TO_MOST_GUIDE -> true
            else -> return false
        }
    }
}