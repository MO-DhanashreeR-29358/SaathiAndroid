package com.mosl.tabapp.report

import dagger.Module
import dagger.Provides

@Module
class HierarchyReportModule {
  @Provides
  internal fun provideView(fragment: HierarchyReportFragment): HierarchyReportView {
    return fragment
  }

}