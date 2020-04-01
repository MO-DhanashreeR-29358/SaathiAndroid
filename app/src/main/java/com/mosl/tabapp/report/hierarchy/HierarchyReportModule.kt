package com.mosl.tabapp.report.hierarchy

import dagger.Module
import dagger.Provides

@Module
class HierarchyReportModule {
  @Provides
  internal fun provideView(fragment: HierarchyReportFragment): HierarchyReportView {
    return fragment
  }

}