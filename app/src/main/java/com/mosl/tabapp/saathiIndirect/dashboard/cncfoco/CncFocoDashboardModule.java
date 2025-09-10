package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CncFocoDashboardModule {
    @Binds
    abstract CncFocoDashboardView provideCncFocoDashboardView(
          CncFocoDashboardFragment fragment
    );
}
