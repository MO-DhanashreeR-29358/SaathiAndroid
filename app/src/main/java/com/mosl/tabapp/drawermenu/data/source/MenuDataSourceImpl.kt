package com.mosl.tabapp.drawermenu.data.source

import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import com.mosl.tabapp.R
import com.mosl.tabapp.drawermenu.data.BaseMenu
import com.mosl.tabapp.drawermenu.data.ChildMenu
import com.mosl.tabapp.navigation.NavigationConstants
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

open class MenuDataSourceImpl @Inject constructor(
) : MenuDataSource {
    override fun getMenuItems(
        userManager: UserManager,
        menuItems: List<com.mosl.saathi.api.model.splash.ChildMenu>,
        digitalSalesKitMenu : List<DigitalSalesKitData>
    ): Observable<List<BaseMenu>> {
        return Observable.fromCallable {
           val user = userManager.user
            when {
                user.isAufsb() -> {
                    getAufsbUserMenu(user.isQRDownload)
                }
                user.isAuBankSalesSupport() -> {
                    getAuSalesBank(user.isCheckApprovalEnable(),digitalSalesKitMenu, user.isPrimeUser, user.isQRDownload)
                }
                user.isSMTEnabled() -> {
                    getSMTUserMenu(menuItems,digitalSalesKitMenu, user.isCheckApprovalEnable(), user.isPrimeUser, user.isQRDownload, user.isRetail())
                }
                user.isEkycEnable() -> {
                    getEkycUserMenu(menuItems,digitalSalesKitMenu, user.isCheckApprovalEnable(), user.isPrimeUser, user.isQRDownload, user.isRetail())
                }
                else -> {
                    getUserMenu(menuItems,digitalSalesKitMenu, user.isCheckApprovalEnable(), user.isPrimeUser, user.isQRDownload, user.isRetail())
                }
            }
        }
            .subscribeOn(Schedulers.io())
    }

    private fun getEkycUserMenu(
        list: List<com.mosl.saathi.api.model.splash.ChildMenu>,
        digitalSalesKitList : List<DigitalSalesKitData>,
        isCheckApproval: Boolean,
        isPrimeUser: Boolean,
        isShowQRCode: Boolean,
        isSHowReactivation : Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            arrayListOf(
                getDashboard(),
                getOpenAccount(),
                getDeleteRequest(),
                getLASAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, isSHowReactivation)
            }
        } else {
            arrayListOf(
                getDashboard(),
                getOpenAccount(),
                getDeleteRequest(),
                getLASAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, isSHowReactivation)
            }
        }
    }

    private fun getUserMenu(
        list: List<com.mosl.saathi.api.model.splash.ChildMenu>,
        digitalSalesKitList : List<DigitalSalesKitData>,
        isCheckApproval: Boolean,
        isPrimeUser: Boolean,
        isShowQRCode: Boolean,
        isSHowReactivation : Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            arrayListOf(
                getDashboard(),
                getSip(),
                getEModification(),
                getReports(list),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, isSHowReactivation)
            }
        } else {
            arrayListOf(
                getDashboard(),
                getSip(),
                getEModification(),
                getReports(list),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, isSHowReactivation)
            }
        }
    }

    private fun getSMTUserMenu(
        list: List<com.mosl.saathi.api.model.splash.ChildMenu>,
        digitalSalesKitList : List<DigitalSalesKitData>,
        isCheckApproval: Boolean,
        isPrimeUser: Boolean,
        isShowQRCode: Boolean,
        isSHowReactivation : Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            arrayListOf(
                getDashboard(),
                getOpenAccount(),
                getDeleteRequest(),
                getLASAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, isSHowReactivation)
            }
        } else {
            arrayListOf(
                getDashboard(),
                getOpenAccount(),
                getDeleteRequest(),
                getLASAccount(),
                getSip(),
                getEModification(),
                getReports(list),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, isSHowReactivation)
            }
        }
    }

    private fun ArrayList<BaseMenu>.checkForPrime(isPrimeUser: Boolean,isShowQRCode: Boolean,
                                                  isSHowReactivation : Boolean): List<BaseMenu> {
        if (isPrimeUser) {
            add(3, getPrimeEntry())
        }
        if (isShowQRCode) {
            add(1, getQRCode())
        }
        if (isSHowReactivation) {
            add(3, getReactivation())
        }

        return this
    }

    private fun getAufsbUserMenu( isShowQRCode : Boolean): List<BaseMenu> {
        return arrayListOf(
            getDashboard(),
            getOpenAccount(),
            getDeleteRequest(),
            getLogout()
        ).apply { checkForPrime(false,isShowQRCode, false) }
    }

    private fun getAuSalesBank(
        isCheckApproval: Boolean,
        digitalSalesKitList : List<DigitalSalesKitData>,
        isPrimeUser: Boolean,
        isShowQRCode: Boolean
    ): List<BaseMenu> {
        return if (isCheckApproval) {
            arrayListOf(
                getDashboard(),
                getOpenAccount(),
                getDeleteRequest(),
                getLASAccount(),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, false)
            }
        } else {
            arrayListOf(
                getDashboard(),
                getOpenAccount(),
                getDeleteRequest(),
                getLASAccount(),
                getDigitalSalesKitMenu(digitalSalesKitList),
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
            ).apply {
                checkForPrime(isPrimeUser,isShowQRCode, false)
            }
        }
    }

    private fun getDashboard(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_dashboard,
            label = R.string.MOSL_MENU_My_DASHBOARD_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_DASHBOARD
        )
    }

    private fun getIncentiveReport(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_incentive_reports,
            label = R.string.MOSL_MENU_INCENTIVE_REPORT_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_INCENTIVE_REPORT
        )
    }

    private fun getCheckApproval(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_cheque_approval,
            label = R.string.MOSL_MENU_CHECK_APPROVAL_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_CHECK_APPROVAL
        )
    }

    private fun getMostGuide(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_most_guide,
            label = R.string.MOSL_MENU_MOST_GUIDE_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_MOST_GUIDE
        )
    }


    private fun getReports(list: List<com.mosl.saathi.api.model.splash.ChildMenu>): BaseMenu {
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

    private fun getDigitalSalesKitMenu(list: List<DigitalSalesKitData>): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_my_reports,
            label = R.string.MOSL_MENU_DIGITAL_SALES_KIT_TXT,
            enabled = true,
            children = list.map {
                ChildMenu(
                    label = 0,
                    labelStr = it.systemName,
                    enabled = true,
                    pageId = NavigationConstants.NAVIGATE_TO_DIGITAL_SALES_KIT,
                    navigateKey = it.id
                )

            }
        )
    }


    private fun getAUBankMeeting(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_suggest_me_tool,
            label = R.string.MOSL_MENU_AU_BANK_MEETING_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING
        )
    }

    private fun getAUBankMeetingReport(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_suggest_me_tool,
            label = R.string.MOSL_MENU_AU_BANK_MEETING_REPORT_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING_REPORT
        )
    }

    private fun getQRCode(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_qr_code,
            label = R.string.MOSL_MENU_QR_CODE,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_QR_CODE
        )
    }

    private fun getOpenAccount(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_update_ekyc,
            label = R.string.MOSL_MENU_EKYC_TXT,
            enabled = true,
            children = arrayListOf(
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
                ),
                ChildMenu(
                    label = R.string.MOSL_MENU_PENDING_ESIGN_TXT,
                    enabled = true,
                    pageId = NavigationConstants.NAVIGATE_TO_PENDING_ESIGN,
                    navigateKey = ""
                )
            )
        )
    }

    private fun getLASAccount(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_las,
            label = R.string.MOSL_MENU_LAS_TXT,
            enabled = true,
            children = arrayListOf(
                ChildMenu(
                    label = R.string.MOSL_MENU_NEW_APPLICATIONS_TXT,
                    enabled = true,
                    pageId = NavigationConstants.NAVIGATE_TO_LAS_NEW_APPLICATION,
                    navigateKey = ""
                ),
                ChildMenu(
                    label = R.string.MOSL_MENU_RESUME_APPLICATIONS_TXT,
                    enabled = true,
                    pageId = NavigationConstants.NAVIGATE_TO_LAS_RESUME_APPLICATION,
                    navigateKey = ""
                )
            )
        )
    }

    private fun getDeleteRequest(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_delete,
            label = R.string.MOSL_MENU_DELETE_REQUEST,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_DELETE_REQUEST
        )
    }
    private fun getEModification(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_emodification,
            label = R.string.MOSL_MENU_E_MODIFICATION_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_E_MODIFICATION
        )
    }

    private fun getSip(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_sip,
            label = R.string.MOSL_MENU_SIP_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_SIP
        )
    }

    private fun getPrimeEntry(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_prime_entry,
            label = R.string.MOSL_MENU_PRIME_ENTRY_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_PRIME_ENTRY
        )
    }

    private fun getSalesBrochure(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_sales_brochure,
            label = R.string.MOSL_MENU_KNOWLEDGE_CENTRE_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_SALES_BROCHURE
        )
    }

    private fun getSalesStadium(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_sales_stadium,
            label = R.string.MOSL_MENU_SALES_STADIUM_PITCH_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_SALES_STADIUM
        )
    }

    private fun getCalendar(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_calendar,
            label = R.string.MOSL_MENU_CALENDAR_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_CALENDAR
        )
    }

    private fun getPendingApproval(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_my_pending_approvals,
            label = R.string.MOSL_MENU_PENDING_APPROVALS_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL
        )
    }

    private fun getBirthdayAnniversary(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_birthday_aniversary,
            label = R.string.MOSL_MENU_BIRTHDAY_ANIVERSARY_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY
        )
    }

    private fun getQuery(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_query,
            label = R.string.MOSL_MENU_QUERY_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_QUERY
        )
    }

    private fun getSuggestMe(enabled: Boolean = true): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_suggest_me_tool,
            label = R.string.MOSL_MENU_SUGGEST_ME_TXT,
            enabled = enabled,
            pageId = NavigationConstants.NAVIGATE_TO_SUGGEST_ME_MENU
        )
    }

    private fun getSetting(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_setting,
            label = R.string.MOSL_MENU_SETTING,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_SETTING
        )
    }

    private fun getLogout(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_logout,
            label = R.string.MOSL_MENU_LOGOUT_TXT,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_LOGOUT
        )
    }

    private fun getReactivation(): BaseMenu {
        return BaseMenu(
            icon = R.drawable.ic_menu_most_guide,
            label = R.string.MOSL_MENU_REACTIVATION,
            enabled = true,
            pageId = NavigationConstants.NAVIGATE_TO_REACTIVATION_SUMMARY
        )
    }


}