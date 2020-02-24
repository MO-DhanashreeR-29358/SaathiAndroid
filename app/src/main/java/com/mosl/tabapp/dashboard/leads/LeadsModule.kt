package com.mosl.tabapp.dashboard.leads

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides


@Module
class LeadsModule {

    @Provides
    internal fun provideAuLeadView(fragment: LeadsFragment): LeadsView {
        return fragment
    }


    @Provides
    internal fun provideDashBoardModel(fragment: LeadsFragment): DashboardModel {
        val args: Bundle? = fragment.getArguments()
        val dashboardModel: DashboardModel =
            args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        return dashboardModel
    }

}