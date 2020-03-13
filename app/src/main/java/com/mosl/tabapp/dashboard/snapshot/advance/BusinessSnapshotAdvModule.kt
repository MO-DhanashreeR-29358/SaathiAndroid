package com.mosl.tabapp.dashboard.snapshot.advance

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides

@Module
class BusinessSnapshotAdvModule {
  @Provides
  internal fun provideBusinessSnapshotAdvView(fragment: BusinessSnapshotAdvFragment): BusinessSnapshotAdvView {
    return fragment
  }

  @Provides
  internal fun provideDashBoardModel(fragment: BusinessSnapshotAdvFragment): DashboardModel {
    val args: Bundle? = fragment.arguments
    return args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel
  }
}