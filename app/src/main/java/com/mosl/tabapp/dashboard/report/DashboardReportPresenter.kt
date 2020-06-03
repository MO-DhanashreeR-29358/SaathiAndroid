package com.mosl.tabapp.dashboard.report

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
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

    val periodChangedObs = view.onPeriodChange()
      .filter(InvalidSelectionPredicate)
      .share()

    val typeFilterChangedObs = view.onTypeFilterChange()
      .filter(InvalidSelectionPredicate)
      .share()

    val filterChange = Observable.merge(
      view.onDatePickerEvent(),
      periodChangedObs,
      typeFilterChangedObs
    )

    Observable.merge(
      onFetchCalled(),
      onRetryCalled(),
      filterChange
    )
      .map { getWidgetRequest() }
      .switchMap {
        repository.getDashboardData(it)
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

    Observable.just(data.label)
      .subscribeOn(Schedulers.io())
      .subscribe {
        view.renderLabel(it)
      }
      .addTo(disposable)

    Observable.just(data.isDateFilter)
      .subscribeOn(Schedulers.io())
      .subscribe {
        view.renderDatePicker(it)
      }
      .addTo(disposable)

    Observable.just(data.periodFilter)
      .subscribeOn(Schedulers.io())
      .subscribe {
        view.renderPeriodFilter(it)
      }
      .addTo(disposable)

    Observable.just(data.typeFilter)
      .subscribeOn(Schedulers.io())
      .subscribe {
        view.renderTypeFilter(it)
      }
      .addTo(disposable)

    view.onNavigateEvent()
      .subscribe {
        navigationFlow(it.leadId, it.ucid, it.employeeCode, it.level, it.rmName, it.systemsName)
      }
      .addTo(disposable)

    view.onDatePickerChange()
      .subscribe {
        view.getDatePicker()
      }
      .addTo(disposable)
  }

  private fun getWidgetRequest(): DashboardReportRequest {
    val additionalMap = data.additionalMap as HashMap<String, String>
    view.getPeriod()
      ?.apply {
        if (this.value.isNotEmpty()) {
          additionalMap[COL_FILTER] = this.value
        }
      }
    view.getTypeFilter()
      ?.apply {
        if (this.toString().isNotEmpty()) {
          additionalMap[ROW_FILTER] = this.toString()
            .replace("_", " ")
        }
        if (data.label!!.contains(ACTIONABLE)) {
          additionalMap[WIDGETTYPE] = this.toString()
            .replace("_", " ")
        }
      }
    if (data.isDateFilter) {
      additionalMap[FLT_DATE] = view.fromDate()
    }

    return DashboardReportRequest(
      page = PAGE,
      pageSize = DASHBOARD_REPORT_PAGE_SIZE,
      widgetSystemName = data.systemsName,
      addtionalParameters = additionalMap

    )
  }

  private fun navigationFlow(
    leadID: String,
    ucid: String,
    empCode: String,
    level: String,
    rmName: String,
    systemsName: String
  ) {
    if (leadID != "0" || ucid != "0") {
      val args = Bundle()
      val navigationConstants: Int = NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER
      args.putString(MOSLConstants.LEAD_ID, leadID)
      args.putString(MOSLConstants.UCID, ucid)
      navigator.openPage(
        navigationConstants,
        args,
        addToStack = true,
        forceReload = false
      )
    } else if (rmName.isNotEmpty()) {
      val args = Bundle()
      val navigationConstants: Int = NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT
      val additionalMap = data.additionalMap as HashMap<String, String>
      additionalMap[FltLevel] = level
      additionalMap[FltHierarchyEmpCode] = empCode
      args.putSerializable(
        ADDITIONALPARAMETERS,
        additionalMap as Serializable
      )
      args.putString(LABEL, data.label)
      args.putString(WidgetSystemName, systemsName)
      navigator.openPage(
        navigationConstants,
        args,
        addToStack = true,
        forceReload = false
      )
    }

  }

  companion object {
    const val LABEL = "Label"
    const val WidgetSystemName = "WidgetSystemName"
    const val ADDITIONALPARAMETERS = "AdditionalParameters"
    const val PageSize = "PageSize"
    const val Page = "Page"
    const val FltLevel = "flt_level"
    const val Flg_Hierarchy = "flg_Hierarchy"
    const val FlgEmpcode = "flg_empcode"
    const val FltHierarchyEmpCode = "flt_Hierarchy_empCode"
    const val DASHBOARD_REPORT_PAGE_SIZE = "100"
    const val PAGE = "1"
    const val WIDGETNAME = "Widget name"
    const val SYSTEM_NAME = "system_name"
    const val MAP = "map"
    const val FILTER_LIST = "filter_list"
    const val SPINNER_LIST = "spinner_list"
    const val FLT_DATE = "flt_date"
    const val COL_FILTER = "colfilter"
    const val ROW_FILTER = "rowfilter"
    const val ACTIONABLE = "Actionable"
    const val WIDGETTYPE = "WidgetType"

  }
}