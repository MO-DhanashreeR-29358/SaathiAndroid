package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class IndirectIFADashboardReportPresenter @Inject constructor(
    view: IndirectIFADashboardReportView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
) :BasePresenter<IndirectIFADashboardReportView>(view){

    override fun start() {

        onFetchCalled().switchMap {
            indirectRepo.fnGetIFADashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)

        view.onFilterEventCalled()
            .subscribe {
                view.fnRenderFilterEventCalled(it.strFilterText)
            }

         view.onCallDataEvent()
             .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_TODAY_BIRTHDAY }
             .map {
                 view.fngetFilterValue()
             }
             .switchMap {
                 indirectRepo.fnGetTodaysBirthdayAnniversary(getWidgetRequest(it))
                     .compose(applyUiModel())
             }
             .subscribe(view::renderData, Timber::e)
             .addTo(disposable)

         view.onCallDataEvent()
             .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_1ST_MEETING }
            .map {
                view.fngetFilterValue()
            }
             .switchMap {
                 indirectRepo.fnGetFirstMeetingNotDone(getWidgetRequest(it))
                     .compose(applyUiModel())
             }
             .subscribe(view::renderData, Timber::e)
             .addTo(disposable)

        view.onCallDataEvent()
               .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_LEADS_NOT_UPDATED }
            .map {
                view.fngetFilterValue()
            }
               .switchMap {
                   indirectRepo.fnGetLeadsNotUpdated(getWidgetRequest(it))
                       .compose(applyUiModel())
               }
               .subscribe(view::renderData, Timber::e)
               .addTo(disposable)

        view.onCallDataEvent()
               .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_INACTIVE_IFAS }
               .map {
                   view.fngetFilterValue()
               }
               .switchMap {
                   indirectRepo.fnGetSalesDashInactive(getWidgetRequest(it))
                       .compose(applyUiModel())
               }
               .subscribe(view::renderData, Timber::e)
               .addTo(disposable)

        view.onDateFilterEventCalled().subscribe {
            view.fnRenderDateFilterEventCalled(it.strDateFilterText)
        }

           view.onCallDateDataEvent()
               .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_TEAM_APPOINTMENT }
               .map {
                   view.fngetDateFilterValue()
               }
               .switchMap {
                   indirectRepo.fnGetIFASalesDashTeamAppointment(getWidgetRequestWithDate(it))
                       .compose(applyUiModel())
               }
               .subscribe(view::renderData, Timber::e)
               .addTo(disposable)

        view.onCallDateDataEvent()
               .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_NEW_LEAD }
               .map {
                   view.fngetDateFilterValue()
               }
               .switchMap {
                   indirectRepo.fnGetIFASalesDashNewLead(getWidgetRequestWithDate(it))
                       .compose(applyUiModel())
               }
               .subscribe(view::renderData, Timber::e)
               .addTo(disposable)

        view.onReportClickEvent().subscribe {
            val bundle = Bundle()
            bundle.putString(
                MOSLConstants.PAGE_TYPE,
                it.widgetName
            )
            bundle.putString(
                MOSLConstants.STAGE_CODE,
                it.strStageCode
            )
            navigator.openPage(
                NavigationConstants.NAVIGATE_TO_INDIRECT_IFA_REPORT_DETAILS.toString(), bundle,
                addToStack = true,
                forceReload = false
            )
        } .addTo(disposable)


    }

    fun getWidgetRequest(strFilterValue:String) : IndirectIFADashboardInput {
        return  IndirectIFADashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = strFilterValue, category = "", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 , fitHierarchy = "Team")
    }

    fun getWidgetRequestWithDate(strFilterDateValue:String) : IndirectIFADashboardInput {
        return  IndirectIFADashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "", category = "", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0, date = strFilterDateValue, fitHierarchy = "Team" )
    }

}