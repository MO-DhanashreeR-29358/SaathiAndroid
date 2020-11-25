package com.mosl.tabapp.drawermenu.data.source

import com.google.android.gms.common.util.CollectionUtils.listOf
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.drawermenu.data.BaseMenu
import com.mosl.tabapp.drawermenu.data.ChildMenu
import com.mosl.tabapp.navigation.NavigationConstants
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class MenuDataSourceImpl @Inject constructor(
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
                getAuSalesBank(user.isCheckApprovalEnable())
            } else if (user.isSMTEnabled()) {
                getSMTUserMenu(menuItems, user.isCheckApprovalEnable())
            } else if (user.isEkycEnable()) {
                getEkycUserMenu(menuItems, user.isCheckApprovalEnable())
            } else {
                getUserMenu(menuItems, user.isCheckApprovalEnable())
            }
        }
            .subscribeOn(Schedulers.io())
    }

    protected fun getEkycUserMenu(
        list: List<com.mosl.saathi.api.model.splash.ChildMenu>,
        isCheckApproval: Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            listOf(
                getDashboard(),
                getOpenAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getIncentiveReport(),
                getCheckApproval(),
                getCalendar(),
                getPendingApproval(),
                getSalesBrochure(),
                getSalesStadium(),
                getBirthdayAnniversary(),
                getMostGuide(),
                getQuery(),
                getSetting(),
                getLogout()
            )
        } else {
            listOf(
                getDashboard(),
                getOpenAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getIncentiveReport(),
                getCalendar(),
                getPendingApproval(),
                getSalesBrochure(),
                getSalesStadium(),
                getBirthdayAnniversary(),
                getMostGuide(),
                getQuery(),
                getSetting(),
                getLogout()
            )
        }
    }

    protected fun getUserMenu(
        list: List<com.mosl.saathi.api.model.splash.ChildMenu>,
        isCheckApproval: Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            listOf(
                getDashboard(),
                getSip(),
                getEModification(),
                getReports(list),
                getIncentiveReport(),
                getCheckApproval(),
                getCalendar(),
                getPendingApproval(),
                getSalesBrochure(),
                getSalesStadium(),
                getBirthdayAnniversary(),
                getMostGuide(),
                getQuery(),
                getSetting(),
                getLogout()
            )
        } else {
            listOf(
                getDashboard(),
                getSip(),
                getEModification(),
                getReports(list),
                getIncentiveReport(),
                getCalendar(),
                getPendingApproval(),
                getSalesBrochure(),
                getSalesStadium(),
                getBirthdayAnniversary(),
                getMostGuide(),
                getQuery(),
                getSetting(),
                getLogout()
            )
        }
    }

    protected fun getSMTUserMenu(
        list: List<com.mosl.saathi.api.model.splash.ChildMenu>,
        isCheckApproval: Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            listOf(
                getDashboard(),
                getOpenAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getIncentiveReport(),
                getCheckApproval(),
                getCalendar(),
                getPendingApproval(),
                getSalesBrochure(),
                getSalesStadium(),
                getBirthdayAnniversary(),
                getSuggestMe(),
                getMostGuide(),
                getQuery(),
                getSetting(),
                getLogout()
            )
        } else {
            listOf(
                getDashboard(),
                getOpenAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getIncentiveReport(),
                getCalendar(),
                getPendingApproval(),
                getSalesBrochure(),
                getSalesStadium(),
                getBirthdayAnniversary(),
                getSuggestMe(),
                getMostGuide(),
                getQuery(),
                getSetting(),
                getLogout()
            )
        }
    }

    protected fun getAufsbUserMenu(): List<BaseMenu> {
        return listOf(
            getDashboard(),
            getOpenAccount(),
            getSetting(),
            getLogout()
        )
    }

    protected fun getAuSalesBank(
        isCheckApproval: Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            listOf(
                getDashboard(),
                getOpenAccount(),
                getSip(),
                getEModification(),
                getAUBankMeeting(),
                getAUBankMeetingReport(),
                getIncentiveReport(),
                getCheckApproval(),
                getSalesBrochure(),
                getSalesStadium(),
                getMostGuide(),
                getSetting(),
                getLogout()
            )
        } else {
            listOf(
                getDashboard(),
                getOpenAccount(),
                getSip(),
                getEModification(),
                getAUBankMeeting(),
                getAUBankMeetingReport(),
                getIncentiveReport(),
                getSalesBrochure(),
                getSalesStadium(),
                getMostGuide(),
                getSetting(),
                getLogout()
            )
        }
    }

    internal fun getDashboard(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_dashboard,
            label = R.string.MOSL_MENU_My_DASHBOARD_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_DASHBOARD
        )
    }

    internal fun getIncentiveReport(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_incentive_reports,
            label = R.string.MOSL_MENU_INCENTIVE_REPORT_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_INCENTIVE_REPORT
        )
    }

    internal fun getCheckApproval(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_cheque_approval,
            label = R.string.MOSL_MENU_CHECK_APPROVAL_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_CHECK_APPROVAL
        )
    }

    internal fun getMostGuide(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_most_guide,
            label = R.string.MOSL_MENU_MOST_GUIDE_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_MOST_GUIDE
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


    internal fun getEModification(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_emodification,
            label = R.string.MOSL_MENU_E_MODIFICATION_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_E_MODIFICATION
        )
    }

    internal fun getSip(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_sip,
            label = R.string.MOSL_MENU_SIP_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_SIP
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
            icon = R.drawable.ic_menu_birthday_aniversary,
            label = R.string.MOSL_MENU_BIRTHDAY_ANIVERSARY_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY
        )
    }

    internal fun getQuery(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_query,
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

    internal fun getSetting(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_setting,
            label = R.string.MOSL_MENU_SETTING,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_SETTING
        )
    }

    internal fun getLogout(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_logout,
            label = R.string.MOSL_MENU_LOGOUT_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_LOGOUT
        )
    }
}