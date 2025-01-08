package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshot

import dagger.Module
import dagger.Provides

@Module
class IndirectIFABuisnessSnapshotModule {
    @Provides
    internal  fun provoideIndirectIFABuisnessSnapshotModule(fragment: IndirectIFABuisnessSnapshotFragment):IndirectIFABuisnessSnapshotView{
        return fragment
    }
}