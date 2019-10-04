package com.mosl.tabapp.drawermenu.data.source

import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.drawermenu.data.BaseMenu
import com.mosl.tabapp.drawermenu.data.ChildMenu
import com.mosl.tabapp.navigation.NavigationConstants
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MenuDataSourceImpl @Inject constructor(
) : MenuDataSource {
  override fun getMenuItems(
    userManager: UserManager,
    menuItems: List<com.mosl.saathi.api.model.splash.ChildMenu>
  ): Observable<List<BaseMenu>> {
    return Observable.fromCallable {
      val user = userManager.user
      if (user.isAufsb()) {
        getAufsbUserMenu()
      } else if (user.isAuBankSalesSupport()) {
        getAuSalesBank()
      } else if (user.isSMTEnabled()) {
        getSMTUserMenu(menuItems)
      } else {
        getUserMenu(menuItems)
      }
    }
        .subscribeOn(Schedulers.io())
  }

  protected fun getUserMenu(list: List<com.mosl.saathi.api.model.splash.ChildMenu>): List<BaseMenu> {
    return listOf(
        getDashboard(),
        getOpenAccount(),
        getReports(list),
        getCalendar(),
        getPendingApproval(),
        getSalesBrochure(),
        getSalesStadium(),
        getBirthdayAnniversary(),
        getQuery(),
        getLogout()
    )
  }

  protected fun getSMTUserMenu(list: List<com.mosl.saathi.api.model.splash.ChildMenu>): List<BaseMenu> {
    return listOf(
        getDashboard(),
        getOpenAccount(),
        getReports(list),
        getCalendar(),
        getPendingApproval(),
        getSalesBrochure(),
        getSalesStadium(),
        getBirthdayAnniversary(),
        getSuggestMe(),
        getQuery(),
        getLogout()
    )
  }

  protected fun getAufsbUserMenu(): List<BaseMenu> {
    return listOf(
        getDashboard(),
        getOpenAccount(),
        getLogout()
    )
  }

  protected fun getAuSalesBank(): List<BaseMenu> {
    return listOf(
        getAUBankMeeting(),
        getAUBankMeetingReport(),
        getSalesBrochure(),
        getSalesStadium(),
        getLogout()
    )
  }

  internal fun getDashboard(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_menu_dashboard,
        label = R.string.MOSL_MENU_My_DASHBOARD_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_DASHBOARD
    )
  }

  internal fun getReports(list: List<com.mosl.saathi.api.model.splash.ChildMenu>): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_my_reports,
        label = R.string.MOSL_MENU_MY_REPORTS_TXT,
        enabled = true,
        children = list.map {
          ChildMenu(
              label = 0,
              labelStr = it.name,
              enabled = true,
              pageId = NavigationConstants.NAVIGATE_TO_REPORTS,
              navigateKey = it.systemName
          )
        }
    )
  }

  internal fun getAUBankMeeting(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_menu_suggest_me_tool,
        label = R.string.MOSL_MENU_AU_BANK_MEETING_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING
    )
  }

  internal fun getAUBankMeetingReport(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_menu_suggest_me_tool,
        label = R.string.MOSL_MENU_AU_BANK_MEETING_REPORT_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING_REPORT
    )
  }

  internal fun getOpenAccount(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_update_ekyc,
        label = R.string.MOSL_MENU_EKYC_TXT,
        enabled = true,
        children = listOf(
            ChildMenu(
                label = R.string.MOSL_MENU_FRESH_REGISTRATION_TXT,
                enabled = true,
                pageId = NavigationConstants.NAVIGATE_TO_FRESH_REG,
                navigateKey = ""
            ),
            ChildMenu(
                label = R.string.MOSL_MENU_EXISTING_APPLICATIONS_TXT,
                enabled = true,
                pageId = NavigationConstants.NAVIGATE_TO_EXISTING_APP,
                navigateKey = ""
            )
        )
    )
  }

  internal fun getSalesBrochure(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_sales_brochure,
        label = R.string.MOSL_MENU_KNOWLEDGE_CENTRE_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_SALES_BROCHURE
    )
  }

  internal fun getSalesStadium(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_sales_stadium,
        label = R.string.MOSL_MENU_SALES_STADIUM_PITCH_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_SALES_STADIUM
    )
  }

  internal fun getCalendar(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_calendar,
        label = R.string.MOSL_MENU_CALENDAR_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_CALENDAR
    )
  }

  internal fun getPendingApproval(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_my_pending_approvals,
        label = R.string.MOSL_MENU_PENDING_APPROVALS_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL
    )
  }

  internal fun getBirthdayAnniversary(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_birthday_aniversary,
        label = R.string.MOSL_MENU_BIRTHDAY_ANIVERSARY_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY
    )
  }

  internal fun getQuery(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_new_query,
        label = R.string.MOSL_MENU_QUERY_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_QUERY
    )
  }

  internal fun getSuggestMe(enabled: Boolean = true): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_menu_suggest_me_tool,
        label = R.string.MOSL_MENU_SUGGEST_ME_TXT,
        enabled = enabled,
        pageId = NavigationConstants.NAVIGATE_TO_SUGGEST_ME_MENU
    )
  }

  internal fun getLogout(): BaseMenu {
    return BaseMenu(
        icon = R.drawable.ic_new_logout,
        label = R.string.MOSL_MENU_LOGOUT_TXT,
        enabled = true,
        pageId = NavigationConstants.NAVIGATE_TO_LOGOUT
    )
  }
}