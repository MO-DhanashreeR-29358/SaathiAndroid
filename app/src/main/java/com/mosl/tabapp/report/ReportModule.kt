package com.mosl.tabapp.report

import dagger.Module
import dagger.Provides

@Module
class ReportModule {
  @Provides
  internal fun provideView(fragment: ReportFragment): ReportView {
    return fragment
  }

}