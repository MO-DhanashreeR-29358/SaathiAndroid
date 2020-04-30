package com.mosl.tabapp.dashboard.report

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.dashboardreport.DashboardReportRepository
import com.mosl.mobile.domain.dashboardreport.DashboardReportRequest
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject

class DashboardReportPresenter @Inject constructor(
  view: DashboardReportView,
  private val repository: DashboardReportRepository,
  val data: DashboardReportData,
  val navigator: ScreenNavigatorImpl
) : BasePresenter<DashboardReportView>(view) {
  override fun start() {
    Observable.merge(
      onFetchCalled(), onRetryCalled()
    ).map { getWidgetRequest() }
      .switchMap {
        repository.getDashboardData(it)
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    Observable.just(data.label).subscribeOn(Schedulers.io()).subscribe {
      view.renderLabel(it)
    }.addTo(disposable)

    view.onNavigateEvent().subscribe {
      navigationFlow(it.leadId, it.UCID, it.employeeCode, it.level, it.rmName, it.systemsName)
    }.addTo(disposable)
  }

  private fun getWidgetRequest(): DashboardReportRequest {
    return DashboardReportRequest(
      page = PAGE,
      pageSize = DASHBOARD_REPORT_PAGE_SIZE,
      widgetSystemName = data.systemsName,
      addtionalParameters = data.additionalMap

    )
  }

  private fun navigationFlow(
    leadID: String, ucid: String, empCode: String,
    level: String, rmName: String, systemsName: String
  ) {
    val args = Bundle()
    val navigationConstants: Int
    if (rmName.equals("0")) {
      navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
      args.putString(MOSLConstants.LEAD_ID, leadID)
      args.putString(MOSLConstants.UCID, ucid)
    } else {
      val additionalMap = data.additionalMap as HashMap<String, String>
      additionalMap[FltLevel] = level
      additionalMap[FltHierarchyEmpCode] = empCode
      args.putSerializable(
        ADDITIONALPARAMETERS,
        additionalMap as Serializable
      )
      args.putString(LABEL, data.label)
      args.putString(WIDGETSYSTEMNAME, systemsName)
      navigationConstants = NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT
    }
    navigator.openPage(
      navigationConstants,
      args,
      addToStack = true,
      forceReload = false
    )
  }

  companion object {
    const val LABEL = "Label"
    const val WIDGETSYSTEMNAME = "WidgetSystemName"
    const val ADDITIONALPARAMETERS = "AdditionalParameters"
    const val PageSize = "PageSize"
    const val Page = "Page"
    const val FltLevel = "flt_level"
    const val FltHierarchyEmpCode = "flt_Hierarchy_empCode"
    const val DASHBOARD_REPORT_PAGE_SIZE = "100"
    const val PAGE = "1"
    const val WIDGETNAME = "Widget name"
    const val SYSTEM_NAME = "system_name"
    const val MAP = "map"

  }
}