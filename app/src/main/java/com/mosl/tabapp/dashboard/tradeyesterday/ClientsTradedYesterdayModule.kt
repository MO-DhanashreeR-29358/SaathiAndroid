package com.mosl.tabapp.dashboard.tradeyesterday

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides

@Module
class ClientsTradedYesterdayModule {
  @Provides
  internal fun provideClientsTradedYesterdayView(fragment: ClientsTradedYesterdayFragment): ClientsTradedYesterdayView {
    return fragment
  }

  @Provides
  internal fun provideDashBoardModel(fragment: ClientsTradedYesterdayFragment): DashboardModel {
    val args: Bundle? = fragment.arguments
    return args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel
  }
}