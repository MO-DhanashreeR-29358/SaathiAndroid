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
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
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

    val branchChangedObs = view.onBranchChange()
      .filter(InvalidSelectionPredicate)
      .doOnNext { position -> Timber.d("Branch changed %s", position) }
      .share()

    val dateChangedObs: Observable<Int> = view.onDatePickerEvent()
      .doOnNext { position -> Timber.d("Date changed %s", position) }
      .share()

    val combine: Observable<Int> =
      Observable.combineLatest(branchChangedObs, dateChangedObs,
        BiFunction { _, _ -> REPORTNO })
    combine.map { ignored -> getWidgetRequest() }
      .switchMap {
        repository.getDashboardData(it)
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    view.onNavigateEvent().subscribe {
      navigationFlow(it.leadId, it.UCID, it.employeeCode)
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
      periodTo = view.toDate(),
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

  private fun navigationFlow(leadID: String, UCID: String, employeeCode: String) {
    val args = Bundle()
    var navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
    if (employeeCode.equals("0")) {
      navigationConstants = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
      args.putString(MOSLConstants.LEAD_ID, leadID)
      args.putString(MOSLConstants.UCID, UCID)
    } else {
      args.putString(MOSLConstants.STRING_TITLE, view.titleName())
      args.putSerializable(MOSLConstants.STRING_MAP, employeeCode.getMap())
      navigationConstants = NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_REPORT
    }
    navigator.openPage(
      navigationConstants,
      args,
      addToStack = true,
      forceReload = false
    )
  }

  private fun String.getMap(): HashMap<String, String> {
    val dataMap: HashMap<String, String> = HashMap()
    dataMap[EmployeeCode] = this
    dataMap[BranchCode] = getBranch(view.getBranch())
    dataMap[Period_From] = view.fromDate()
    dataMap[Period_To] = view.toDate()
    view.widgetSystemsName()?.let { dataMap[ReportTypeSystemName] = it }
    return dataMap
  }

  companion object {
    const val Period_From = "Period_From"
    const val EmployeeCode = "EmployeeCode"
    const val ReportTypeSystemName = "ReportTypeSystemName"
    const val PageSize = "PageSize"
    const val Period_To = "Period_To"
    const val BranchCode = "BranchCode"
    const val REPORTNO = 1
  }
}