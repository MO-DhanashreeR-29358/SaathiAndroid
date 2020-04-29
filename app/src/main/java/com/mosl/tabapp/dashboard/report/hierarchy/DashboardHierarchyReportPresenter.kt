package com.mosl.tabapp.dashboard.report.hierarchy

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.dashboardreport.DashboardReportRepository
import com.mosl.mobile.domain.dashboardreport.DashboardReportRequest
import com.mosl.tabapp.constants.MOSLConstants
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
    ).switchMap {
      repository.getDashboardData(getWidgetRequest())
        .compose(applyUiModel())
    }
      .subscribe(view::renderData)
      .addTo(disposable)

    view.onNavigateEvent().subscribe {
      navigationFlow(it.leadId, it.UCID, it.employeeCode, it.level, it.rmName, it.systemsName)
    }.addTo(disposable)
  }

  private fun getWidgetRequest(): DashboardReportRequest {
    return DashboardReportRequest(
      page = data.hierarchyMap.get(Page),
      pageSize = data.hierarchyMap.get(PageSize),
      widgetSystemName = data.hierarchyMap.get(WidgetSystemName),
      addtionalParameters = data.hierarchyMap.get(AddtionalParameters) as Map<String, String>

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
      val hierarchyMap = data.hierarchyMap as HashMap<String, Any>
      val additionalMap = data.hierarchyMap.get(AddtionalParameters) as HashMap<String, String>
      additionalMap[FltLevel] = level
      additionalMap[FltHierarchyEmpCode] = empCode
      hierarchyMap[WidgetSystemName] = systemsName
      hierarchyMap[AddtionalParameters] = additionalMap
      args.putSerializable(
        MOSLConstants.HIERARCHY_MAP,
        hierarchyMap as Serializable
      )
      args.putString(MOSLConstants.LABEL, data.lable)
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
    const val WidgetSystemName = "WidgetSystemName"
    const val AddtionalParameters = "AddtionalParameters"
    const val PageSize = "PageSize"
    const val Page = "Page"
    const val FltLevel = "flt_level"
    const val FltHierarchyEmpCode = "flt_Hierarchy_empCode"

  }
}