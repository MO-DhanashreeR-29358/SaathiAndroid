package com.mosl.tabapp.dashboard.ausfb.leads

import android.os.Bundle
import com.mosl.app.core.data.ausfbLead.AusfbLeadRepositoryImpl
import com.mosl.app.core.data.ausfbLead.BankRepositoryImpl
import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import com.mosl.mobile.domain.ausfbLead.BankRepository
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
    internal fun provideBankRepository(repository: BankRepositoryImpl): BankRepository {
        return repository
    }

    @Provides
    internal fun provideAusfbLeadRepository(repository: AusfbLeadRepositoryImpl): AusfbLeadRepository {
        return repository
    }


    @Provides
    internal fun provideDashBoardModel(fragment: AuLeadsFragment): DashboardModel {
        val args: Bundle? = fragment.getArguments()
        val dashboardModel: DashboardModel =
            args?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel

        return dashboardModel
    }

}