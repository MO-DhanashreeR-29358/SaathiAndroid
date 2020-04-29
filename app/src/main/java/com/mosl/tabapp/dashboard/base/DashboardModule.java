package com.mosl.tabapp.dashboard.base;

import android.os.Bundle;
import com.mosl.app.core.data.dashboard.DashboardRepositoryImpl;
import com.mosl.mobile.domain.dashboard.DashboardRepository;
import com.mosl.mobile.domain.dashboard.DashboardRequestData;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import static com.mosl.tabapp.dashboard.base.DashboardFragmentNew.ARG_EMPCODE;
import static com.mosl.tabapp.dashboard.base.DashboardFragmentNew.ARG_PRODUCT_ID;
import static com.mosl.tabapp.dashboard.base.DashboardFragmentNew.ARG_PROFILE_ID;

@Module
public abstract class DashboardModule {

    @Provides static DashboardRequestData provideDashboardData(DashboardFragmentNew fragment) {
        Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            String productID = arguments.getString(ARG_PRODUCT_ID);
            String profileID = arguments.getString(ARG_PROFILE_ID);
            String empID = arguments.getString(ARG_EMPCODE);
            return new DashboardRequestData(profileID, productID, empID);
        }
        return new DashboardRequestData("", "", "");
    }

    @Binds abstract DashboardView provideDashboardView(
        DashboardFragmentNew fragment);

    @Binds abstract DashboardRepository provideDashboardRepository(
        DashboardRepositoryImpl repository);
}
