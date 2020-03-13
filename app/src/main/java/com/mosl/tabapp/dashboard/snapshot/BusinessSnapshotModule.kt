package com.mosl.tabapp.dashboard.snapshot

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvFragment
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvView
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides

@Module
class BusinessSnapshotModule {
  @Provides
  internal fun provideBusinessSnapshotView(fragment: BusinessSnapshotFragment): BusinessSnapshotView {
    return fragment
  }

  @Provides
  internal fun provideBusinessSnapshotAdvView(fragment: BusinessSnapshotAdvFragment): BusinessSnapshotAdvView {
    return fragment
  }

  @Provides
  internal fun provideDashBoardModel(fragment: BusinessSnapshotFragment): DashboardModel {
    val args: Bundle? = fragment.arguments
    return args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel
  }
}