package com.mosl.tabapp.dashboard.leads.report

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides

@Module
class LeadsReportModule {
  @Provides
  internal fun provideAuLeadView(fragment: LeadsReportFragment): LeadsReportView {
    return fragment
  }

  @Provides
  internal fun provideDashBoardModel(fragment: LeadsReportFragment): DashboardModel {
    val args: Bundle? = fragment.getArguments()
    val dashboardModel: DashboardModel =
      args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

    return dashboardModel
  }
}