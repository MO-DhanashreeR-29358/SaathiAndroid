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
      navigationFlow(it.leadId, it.UCID)
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

  private fun navigationFlow(leadID: String, UCID: String) {
    val args = Bundle()
    args.putString(MOSLConstants.LEAD_ID, leadID)
    args.putString(MOSLConstants.UCID, UCID)
    navigator.openPage(
      NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER,
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

  }
}