package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport


import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectIFAUniqueModule {
    @Binds
    abstract fun provideIndirectIFAUniqueModule(fragment: IndirectIFAUniqueFragment): IndirectIFAUniqueView
}