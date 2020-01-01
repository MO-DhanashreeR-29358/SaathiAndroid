package com.mosl.tabapp.dashboard.ausfb.targetachievement

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides


@Module
class AuTargetVsAchievementModule {

    @Provides
    internal fun provideAuLeadView(fragment: AuTargetVsAchievementFragment): AuTargetVsAchievementView {
        return fragment
    }


    @Provides
    internal fun provideDashBoardModel(fragment: AuTargetVsAchievementFragment): DashboardModel {
        val args: Bundle? = fragment.getArguments()
        val dashboardModel: DashboardModel =
            args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        return dashboardModel
    }

}