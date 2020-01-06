package com.mosl.tabapp.dashboard.ausfb.target

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides


@Module
class AuTargetModule {

    @Provides
    internal fun provideAuLeadView(fragment: AuTargetFragment): AuTargetView {
        return fragment
    }

    @Provides
    internal fun provideDashBoardModel(fragment: AuTargetFragment): DashboardModel {
        val args: Bundle? = fragment.getArguments()
        val dashboardModel: DashboardModel =
            args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        return dashboardModel
    }

}