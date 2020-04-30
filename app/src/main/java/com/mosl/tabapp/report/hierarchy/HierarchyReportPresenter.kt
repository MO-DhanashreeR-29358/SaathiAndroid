package com.mosl.tabapp.report.hierarchy

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.report.HierarchyReportInput
import com.mosl.mobile.domain.report.HierarchyReportRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class HierarchyReportPresenter @Inject constructor(
  view: HierarchyReportView,
  private val repository: HierarchyReportRepository,
  val navigator: ScreenNavigatorImpl
) : BasePresenter<HierarchyReportView>(view) {
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
      navigationFlow(it.leadId, it.UCID, it.employeeCode)
    }.addTo(disposable)
  }

  private fun getWidgetRequest(): HierarchyReportInput {
    val mMap = view.getAdditionalParameterMap()
    return HierarchyReportInput(
      totalCount = 0,
      periodFrom = mMap.get(Period_From),
      isExportToExcel = false,
      employeeCode = mMap.get(EmployeeCode),
      calculatedPage = 0,
      inputTotalCount = 0,
      page = 0,
      reportTypeSystemName = mMap.get(ReportTypeSystemName),
      businessChannel = "-1",
      pageSize = MOSLConstants.REPORT_PAGE_SIZE,
      periodTo = mMap.get(Period_To),
      branchCode = mMap.get(BranchCode)
    )
  }

  private fun navigationFlow(leadID: String, UCID: String, employeeCode: String) {
    val args = Bundle()
    var navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
    if (employeeCode.equals("0")) {
      navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
      args.putString(MOSLConstants.LEAD_ID, leadID)
      args.putString(MOSLConstants.UCID, UCID)
    } else {
      val additionalMap = view.getAdditionalParameterMap()
      additionalMap[EmployeeCode] = employeeCode
      args.putString(MOSLConstants.STRING_TITLE, view.titleName())
      args.putSerializable(MOSLConstants.STRING_MAP, additionalMap)
      navigationConstants = NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_REPORT
    }
    navigator.openPage(
      navigationConstants,
      args,
      addToStack = true,
      forceReload = false
    )
  }

  companion object {
    const val Period_From = "Period_From"
    const val EmployeeCode = "EmployeeCode"
    const val ReportTypeSystemName = "ReportTypeSystemName"
    const val PageSize = "PageSize"
    const val Period_To = "Period_To"
    const val BranchCode = "BranchCode"
  }
}