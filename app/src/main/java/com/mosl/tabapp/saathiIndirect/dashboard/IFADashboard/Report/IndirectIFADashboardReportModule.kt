package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import dagger.Module
import dagger.Provides

@Module
public  class IndirectIFADashboardReportModule {
    @Provides
    internal fun provideIndirectIFADashboardReportModule(fragment: IndirectIFADashboardReportFragment):IndirectIFADashboardReportView{
        return fragment
    }

}