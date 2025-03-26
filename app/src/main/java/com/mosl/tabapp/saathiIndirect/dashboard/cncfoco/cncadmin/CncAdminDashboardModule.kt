package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.cncadmin


import dagger.Module
import dagger.Provides

@Module
class CncAdminDashboardModule {

    @Provides
    internal fun provideTargetView(fragment: CncAdminDashboardFragment): CncAdminDashboardView {
        return fragment
    }

}