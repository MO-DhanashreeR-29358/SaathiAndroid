package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard

import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectCncFocoDashboardDetailsModule {
    @Binds
    abstract fun provideIndirectCncFocoDashboardDetailsModule(fragment: IndirectCncFocoDashboardDetailsFragment): IndirectCncFocoDashboardDetailsView
}