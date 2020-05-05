package com.mosl.tabapp.dashboard.report.hierarchy

import com.mosl.tabapp.dashboard.report.DashboardReportPresenter
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.WidgetSystemName
import dagger.Module
import dagger.Provides
import java.io.Serializable

@Module
class DashboardHierarchyReportModule {
  @Provides
  internal fun provideView(fragment: DashboardHierarchyReportFragment): DashboardHierarchyReportView {
    return fragment
  }

  @Provides
  internal fun provideDashboardHierarchyReport(fragment: DashboardHierarchyReportFragment): DashboardHierarchyReportData {
    val additionalParametersMap: Serializable?
    fragment.arguments?.apply {
      additionalParametersMap = this.getSerializable(DashboardReportPresenter.ADDITIONALPARAMETERS)
      return DashboardHierarchyReportData(
          additionalParametersMap as Map<String, String>,
          this.getString(DashboardReportPresenter.LABEL),
          this.getString(WidgetSystemName)
      )
    }
    return DashboardHierarchyReportData(emptyMap<String, String>(), "", "")
  }

}