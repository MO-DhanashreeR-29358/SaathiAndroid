package com.mosl.tabapp.dashboard.report

import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MoslConstant.dd_MMM_yyyy
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FILTER_LIST
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FLT_DATE
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.LABEL
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.MAP
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.SPINNER_LIST
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.SYSTEM_NAME
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.WIDGETNAME
import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.WidgetFilterModel
import dagger.Module
import dagger.Provides
import java.io.Serializable
import java.util.Date

@Module
class DashboardReportModule {
  @Provides
  internal fun provideView(fragment: DashboardReportFragment): DashboardReportView {
    return fragment
  }

  @Provides
  internal fun provideDashboardReport(fragment: DashboardReportFragment): DashboardReportData {
    val additionalMap: Serializable?
    var isDateFilter = false
    var typeFilter: List<TypeActionableFilterModel> = emptyList<TypeActionableFilterModel>()
    var periodFilter: List<FilterRowData> = emptyList<FilterRowData>()
    fragment.arguments?.apply {
      additionalMap = this.getSerializable(MAP) as HashMap<String, String>
      val label: String?
      if (this.containsKey(WIDGETNAME)) {
        label = String.format("%s - %s", this.getString(LABEL), this.getString(WIDGETNAME))
      } else {
        label = this.getString(LABEL)
      }
      if (this.containsKey(FILTER_LIST)) {
        val filterList = this.getParcelableArrayList<WidgetFilterModel>(FILTER_LIST)
          ?: emptyList<WidgetFilterModel>()
        if (filterList.isNotEmpty()) {
          if (filterList[0].keyValue.equals(FLT_DATE, ignoreCase = true)) {
            isDateFilter = true
            additionalMap[FLT_DATE] = CRMAppUtil.getDateStr(Date(), dd_MMM_yyyy)
          }
          periodFilter = filterList[0].filterData
        }
      }

      if (this.containsKey(SPINNER_LIST)) {
        typeFilter =
          this.getParcelableArrayList<TypeActionableFilterModel>(SPINNER_LIST) ?: emptyList()
      }

      return DashboardReportData(
        additionalMap as Map<String, String>,
        label, this.getString(SYSTEM_NAME), periodFilter, typeFilter, isDateFilter
      )
    }
    return DashboardReportData(
      emptyMap<String, String>(),
      "",
      "",
      emptyList<FilterRowData>(),
      emptyList<TypeActionableFilterModel>(),
      false
    )
  }

}