package com.mosl.tabapp.dashboard.ausfb.target

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides


@Module
class TargetModule {

    @Provides
    internal fun provideAuLeadView(fragment: TargetFragment): TargetView {
        return fragment
    }

    @Provides
    internal fun provideDashBoardModel(fragment: TargetFragment): DashboardModel {
        val args: Bundle? = fragment.getArguments()
        val dashboardModel: DashboardModel =
            args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        return dashboardModel
    }

}