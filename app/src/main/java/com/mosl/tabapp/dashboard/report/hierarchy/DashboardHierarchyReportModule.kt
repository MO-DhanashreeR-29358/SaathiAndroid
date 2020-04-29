package com.mosl.tabapp.dashboard.report.hierarchy

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
    val hierarchyMap: Serializable?
    fragment.arguments?.apply {
      hierarchyMap = this.getSerializable(DashboardHierarchyReportFragment.HIERARCHY_MAP)
      return DashboardHierarchyReportData(
        hierarchyMap as Map<String, String>,
        this.getString(DashboardHierarchyReportFragment.LABEL)
      )
    }
    return DashboardHierarchyReportData(emptyMap<String, String>(), "")
  }

}