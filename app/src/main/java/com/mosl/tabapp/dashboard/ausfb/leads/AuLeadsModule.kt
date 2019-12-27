package com.mosl.tabapp.dashboard.ausfb.leads

import com.mosl.app.core.data.ausfbLead.AusfbLeadRepositoryImpl
import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import dagger.Module
import dagger.Provides

@Module
class AuLeadsModule {

    @Provides
    internal fun provideAuLeadView(fragment: AuLeadsFragment): AuLeadsView {
        return fragment
    }

    @Provides
    internal fun provideQueryRepository(repository: AusfbLeadRepositoryImpl): AusfbLeadRepository {
        return repository
    }
}