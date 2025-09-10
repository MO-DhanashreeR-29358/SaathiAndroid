package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLeadDetials

import dagger.Binds
import dagger.Module


@Module
public abstract class IndirectIFAMyLeadDetailsModule {

    @Binds
    abstract fun provideIndirectIFAMyLeadDetailsModule(fragment: IndirectIFAMyLeadDetailsFragment): IndirectIFAMyLeadDetailsView
}