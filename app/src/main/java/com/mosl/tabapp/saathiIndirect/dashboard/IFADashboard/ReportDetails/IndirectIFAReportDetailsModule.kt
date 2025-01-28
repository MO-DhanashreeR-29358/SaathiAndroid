package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.ReportDetails

import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectIFAReportDetailsModule {

    @Binds
    abstract fun provideIndirectIFAReportDetailsModule(fragment: IndirectIFAReportDetailsFragment): IndirectIFAReportDetailsView
}