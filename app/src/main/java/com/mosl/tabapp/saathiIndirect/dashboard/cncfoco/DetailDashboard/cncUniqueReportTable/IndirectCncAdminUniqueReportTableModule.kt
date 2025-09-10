package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.cncUniqueReportTable

import dagger.Module
import dagger.Provides

@Module
class IndirectCncAdminUniqueReportTableModule {
    @Provides
    internal  fun provoideIndirectCncAdminUniqueReportTableModule(fragment: IndirectCncAdminUniqueReportTableFragment): IndirectCncAdminUniqueReportTableView {
        return fragment
    }
}
