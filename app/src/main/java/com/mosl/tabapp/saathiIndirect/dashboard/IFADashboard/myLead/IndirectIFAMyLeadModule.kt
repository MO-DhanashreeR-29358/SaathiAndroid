package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead


import dagger.Module
import dagger.Provides

@Module
class IndirectIFAMyLeadModule {
    @Provides
    internal  fun provoideIndirectIFAMyLeadModule(fragment: IndirectIFAMyLeadFragment): IndirectIFAMyLeadView {
        return fragment
    }
}