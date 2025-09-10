package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshotIp

import dagger.Module
import dagger.Provides

@Module
class IndirectIFABuisnessSnapshotIpModule {
    @Provides
    internal  fun provoideIndirectIFABuisnessSnapshotIpModule(fragment: IndirectIFABuisnessSnapshotIpFragment): IndirectIFABuisnessSnapshotIpView {
        return fragment
    }
}