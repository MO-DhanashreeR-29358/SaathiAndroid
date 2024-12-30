package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import dagger.Binds
import dagger.Module

@Module
public abstract  class IndirectIFADashboardModule {
    @Binds
    abstract fun provideIndirectIFADashboardModule(fragment: IndirectIFADashboardFragment):IndirectIFADashboardView
}