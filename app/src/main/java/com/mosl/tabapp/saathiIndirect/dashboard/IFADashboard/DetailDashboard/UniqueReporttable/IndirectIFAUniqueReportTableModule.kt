package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable

import dagger.Module
import dagger.Provides

@Module
class IndirectIFAUniqueReportTableModule {
    @Provides
    internal  fun provoideIndirectIFAUniqueReportTableModule(fragment: IndirectIFAUniqueReportTableFragment): IndirectIFAUniqueReportTableView {
        return fragment
    }
}