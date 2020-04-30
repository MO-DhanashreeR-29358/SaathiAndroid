package com.mosl.tabapp.dashboard.report.hierarchy

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.dashboardreport.DashboardReportRepository
import com.mosl.mobile.domain.dashboardreport.DashboardReportRequest
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.ADDITIONALPARAMETERS
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.DASHBOARD_REPORT_PAGE_SIZE
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FltHierarchyEmpCode
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FltLevel
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.LABEL
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.PAGE
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
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
      onFetchCalled(), onRetryCalled()
    ).map { getWidgetRequest(view.getWidgetSystemsName()) }
      .switchMap {
        repository.getDashboardData(it)
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    view.onNavigateEvent().subscribe {
      navigationFlow(it.leadId, it.UCID, it.employeeCode, it.level, it.rmName, it.systemsName)
    }.addTo(disposable)
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
    leadID: String, UCID: String, empCode: String,
    level: String, rmName: String, systemsName: String
  ) {
    val args = Bundle()
    var navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
    if (rmName.equals("0")) {
      navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
      args.putString(MOSLConstants.LEAD_ID, leadID)
      args.putString(MOSLConstants.UCID, UCID)
    } else {
      val additionalMap = data.additionalParametersMap as HashMap<String, String>
      additionalMap[FltLevel] = level
      additionalMap[FltHierarchyEmpCode] = empCode
      args.putSerializable(
        ADDITIONALPARAMETERS,
        additionalMap as Serializable
      )
      args.putString(LABEL, data.label)
      args.putString("WidgetSystemName", systemsName)
      navigationConstants = NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT
    }
    navigator.openPage(
      navigationConstants,
      args,
      addToStack = true,
      forceReload = false
    )
  }

}