package com.mosl.tabapp.dashboard.report

import com.mosl.tabapp.dashboard.report.DashboardReportFragment.Companion.LABEL
import com.mosl.tabapp.dashboard.report.DashboardReportFragment.Companion.MAP
import com.mosl.tabapp.dashboard.report.DashboardReportFragment.Companion.SYSTEM_NAME
import com.mosl.tabapp.dashboard.report.DashboardReportFragment.Companion.WIDGETNAME
import dagger.Module
import dagger.Provides
import java.io.Serializable

@Module
class DashboardReportModule {
  @Provides
  internal fun provideView(fragment: DashboardReportFragment): DashboardReportView {
    return fragment
  }

  @Provides
  internal fun provideDashboardReport(fragment: DashboardReportFragment): DashboardReportData {
    val additionalMap: Serializable?
    fragment.arguments?.apply {
      additionalMap = this.getSerializable(MAP)
      val label: String?
      if (this.containsKey(WIDGETNAME)) {
        label = String.format("%s - %s", this.getString(LABEL), this.getString(WIDGETNAME))
      } else {
        label = this.getString(LABEL)
      }
      return DashboardReportData(
        additionalMap as Map<String, String>,
        label, this.getString(SYSTEM_NAME)
      )
    }
    return DashboardReportData(emptyMap<String, String>(), "", "")
  }

}