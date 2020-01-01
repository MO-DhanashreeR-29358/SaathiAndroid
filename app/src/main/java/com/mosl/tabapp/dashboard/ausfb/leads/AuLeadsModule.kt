package com.mosl.tabapp.dashboard.ausfb.leads

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides


@Module
class AuLeadsModule {

    @Provides
    internal fun provideAuLeadView(fragment: AuLeadsFragment): AuLeadsView {
        return fragment
    }


    @Provides
    internal fun provideDashBoardModel(fragment: AuLeadsFragment): DashboardModel {
        val args: Bundle? = fragment.getArguments()
        val dashboardModel: DashboardModel =
            args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        return dashboardModel
    }

}