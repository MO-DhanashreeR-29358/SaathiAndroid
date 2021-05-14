package com.mosl.tabapp.dashboard.report.hierarchy

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.dashboardreport.DashboardReportRepository
import com.mosl.mobile.domain.dashboardreport.DashboardReportRequest
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.ADDITIONALPARAMETERS
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.DASHBOARD_REPORT_PAGE_SIZE
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FLT_HIERARCHY_BRANCH_CODE
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FltHierarchyEmpCode
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FltLevel
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.LABEL
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.PAGE
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.WidgetSystemName
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import java.io.Serializable
import javax.inject.Inject

class DashboardHierarchyReportPresenter @Inject constructor(
    view: DashboardHierarchyReportView,
    private val repository: DashboardReportRepository,
    val data: DashboardHierarchyReportData,
    val navigator: ScreenNavigatorImpl
) : BasePresenter<DashboardHierarchyReportView>(view) {
    override fun start() {
        Observable.merge(
            onFetchCalled(), onRetryCalled(), view.onViewChange()
        )
            .map { getWidgetRequest(view.getWidgetSystemsName()) }
            .switchMap {
                repository.getDashboardData(it)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderData)
            .addTo(disposable)

        view.onNavigateEvent()
            .subscribe {
                navigationFlow(
                    it.leadId,
                    it.ucid,
                    it.employeeCode,
                    it.level,
                    it.rmName,
                    it.systemsName,
                    it.branchCode
                )
            }
            .addTo(disposable)
    }

    private fun getWidgetRequest(systemsName: String): DashboardReportRequest {
        return DashboardReportRequest(
            page = PAGE,
            pageSize = DASHBOARD_REPORT_PAGE_SIZE,
            widgetSystemName = systemsName,
            addtionalParameters = data.additionalParametersMap

        )
    }

    private fun navigationFlow(
        leadID: String,
        ucid: String,
        empCode: String,
        level: String,
        rmName: String,
        systemsName: String,
        branchCode: String
    ) {
        if (leadID != "0" || ucid != "0") {
            val navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
            val args = Bundle()
            args.putString(MOSLConstants.LEAD_ID, leadID)
            args.putString(MOSLConstants.UCID, ucid)
            navigator.openPage(
                navigationConstants.toString(),
                args,
                addToStack = true,
                forceReload = false
            )
        } else if (rmName.isNotEmpty() || branchCode.isNotEmpty()) {
            val navigationConstants = NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT
            val args = Bundle()
            val additionalMap = data.additionalParametersMap as HashMap<String, String>
            additionalMap[FltLevel] = level
            additionalMap[FltHierarchyEmpCode] = empCode
            additionalMap[FLT_HIERARCHY_BRANCH_CODE] = branchCode
            args.putSerializable(
                ADDITIONALPARAMETERS,
                additionalMap as Serializable
            )
            args.putString(LABEL, data.label)
            args.putString(WidgetSystemName, systemsName)
            navigator.openPage(
                navigationConstants.toString(),
                args,
                addToStack = true,
                forceReload = false
            )
        }
    }

}