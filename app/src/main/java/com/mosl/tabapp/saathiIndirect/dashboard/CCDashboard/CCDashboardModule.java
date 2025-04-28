package com.mosl.tabapp.saathiIndirect.dashboard.CCDashboard;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CCDashboardModule {


    @Binds abstract CCDashboardView provideCCDashboardView(
        CCDashboardFragment fragment);

}
