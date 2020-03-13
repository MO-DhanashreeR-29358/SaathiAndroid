package com.mosl.tabapp.dashboard.trends

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides


@Module
class MonthOnMonthTrendsModule {

    @Provides
    internal fun provideMonthMonthTrendsView(fragment: MonthOnMonthTrendsFragment): MonthsOnMonthsView {
        return fragment
    }

    @Provides
    internal fun provideDashBoardModel(fragment: MonthOnMonthTrendsFragment): DashboardModel {
        val args: Bundle? = fragment.getArguments()
        val dashboardModel: DashboardModel =
            args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        return dashboardModel
    }

}