package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectIFATargetModule {
    @Binds
    abstract fun provideIndirectIFATargetModule(fragment: IndirectIFATargetFragment):IndirectIFATargetView
}