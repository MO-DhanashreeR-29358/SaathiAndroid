package com.mosl.tabapp.saathiIndirect.dashboard.foco;


import dagger.Binds;
import dagger.Module;

@Module
public abstract class FocoDashboardModule {


    @Binds abstract FocoDashboardView provideFocoDashboardView(
        FocoDashboardFragment fragment);

}
