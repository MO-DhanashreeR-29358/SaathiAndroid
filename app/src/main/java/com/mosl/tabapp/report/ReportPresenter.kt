package com.mosl.tabapp.report

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.report.AssignedLeadReportModel
import com.mosl.mobile.domain.report.HierarchyReportInput
import com.mosl.mobile.domain.report.HierarchyReportRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ReportPresenter @Inject constructor(
  view: ReportView,
  private val repository: HierarchyReportRepository,
  val navigator: ScreenNavigatorImpl,
  val userManager: UserManager
) : BasePresenter<ReportView>(view) {
  override fun start() {
    onFetchCalled().switchMap {
        repository.getBranch()
          .compose(applyUiModel())
      }
      .subscribe(view::renderBranchFilter)
      .addTo(disposable)

    view.onBranchChange()
      .filter(InvalidSelectionPredicate)
      .switchMap {
        repository.getDashboardData(getWidgetRequest())
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    Observable.merge(view.onDatePickerEvent(), onRetryCalled())
      .switchMap {
        repository.getDashboardData(getWidgetRequest())
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    view.onNavigateEvent().subscribe {
      navigationFlow(it.leadId, it.UCID)
    }.addTo(disposable)
  }

  private fun getWidgetRequest(): HierarchyReportInput {
    return HierarchyReportInput(
      totalCount = 0,
      periodFrom = view.fromDate(),
      isExportToExcel = false,
      employeeCode = userManager.getUser()?.employeeCode,
      calculatedPage = 0,
      inputTotalCount = 0,
      page = 0,
      reportTypeSystemName = view.widgetSystemsName(),
      businessChannel = "-1",
      pageSize = MOSLConstants.REPORT_PAGE_SIZE,
      periodTo =
      view.toDate(),
      branchCode = getBranch(view.getBranch())
    )
  }

  private fun getBranch(assignedLeadReportModel: AssignedLeadReportModel?): String {
    assignedLeadReportModel?.apply {
      if (this.businessUnitId != -1)
        return this.branchCode
    }
    return ""
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
    const val Period_From = "Period_From"
    const val EmployeeCode = "EmployeeCode"
    const val ReportTypeSystemName = "ReportTypeSystemName"
    const val PageSize = "PageSize"
    const val Period_To = "Period_To"
    const val BranchCode = "BranchCode"
  }
}