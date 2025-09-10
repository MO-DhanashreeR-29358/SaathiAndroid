package com.mosl.tabapp.saathiIndirect.dashboard;

import static com.mosl.tabapp.dashboard.base.DashboardFragment.ARG_EMPCODE;
import static com.mosl.tabapp.dashboard.base.DashboardFragment.ARG_PRODUCT_ID;
import static com.mosl.tabapp.dashboard.base.DashboardFragment.ARG_PROFILE_ID;

import android.os.Bundle;

import com.mosl.app.core.data.dashboard.DashboardRepositoryImpl;
import com.mosl.mobile.domain.dashboard.DashboardRepository;
import com.mosl.mobile.domain.dashboard.DashboardRequestData;
import com.mosl.tabapp.dashboard.base.DashboardFragment;
import com.mosl.tabapp.dashboard.base.DashboardView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class IndirectDashboardModule {


    @Binds abstract IndirectDashboardView provideIndirectDashboardView(
        IndirectDashboardFragment fragment);

}
