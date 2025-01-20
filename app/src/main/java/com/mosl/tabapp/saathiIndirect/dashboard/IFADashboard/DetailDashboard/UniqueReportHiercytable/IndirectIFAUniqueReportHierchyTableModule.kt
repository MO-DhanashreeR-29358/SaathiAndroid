package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable

import dagger.Module
import dagger.Provides

@Module
class IndirectIFAUniqueReportHierchyTableModule
{
    @Provides
    internal  fun provoideIndirectIFAUniqueReportHierchyTableModule(fragment: IndirectIFAUniqueReportHierchyTableFragment): IndirectIFAUniqueReportHierchyTableView {
        return fragment
    }
}