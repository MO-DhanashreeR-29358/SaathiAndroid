package com.mosl.tabapp.dashboard.productivity

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides

@Module
class ProductivityModule {
  @Provides
  internal fun provideBusinessSnapshotView(fragment: ProductivityFragment): productivityView {
    return fragment
  }

  @Provides
  internal fun provideDashBoardModel(fragment: ProductivityFragment): DashboardModel {
    val args: Bundle? = fragment.arguments
    return args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel
  }
}