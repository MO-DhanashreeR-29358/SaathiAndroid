package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard

import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectIFADashboardDetailsModule {

    @Binds
    abstract fun provideIndirectIFADashboardDetailsModule(fragment: IndirectIFADashboardDetailsFragment):IndirectIFADashboardDetailsView
}