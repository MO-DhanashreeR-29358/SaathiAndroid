package com.mosl.tabapp.dashboard.clients

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides

@Module
class ClientsModule {

    @Provides
    internal fun provideClientsView(fragment: ClientsFragmentNew): ClientsView {
        return fragment
    }

    @Provides
    internal fun provideDashBoardModel(fragment: ClientsFragmentNew): DashboardModel {
        val args: Bundle? = fragment.arguments
        return args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel
    }
}