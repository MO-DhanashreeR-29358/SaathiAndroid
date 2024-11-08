package com.mosl.tabapp.data;

import android.content.Context;

import com.mosl.app.core.data.RepositoryAliasProviders;
import com.mosl.app.core.data.attendance.AttendanceRepositoryImpl;
import com.mosl.app.core.data.location.LocationRepositoryImpl;
import com.mosl.app.core.data.util.BaseDataModule;
import com.mosl.app.core.data.util.CertPinProvider;
import com.mosl.mobile.domain.attendance.AppInfo;
import com.mosl.mobile.domain.attendance.AttendanceRepository;
import com.mosl.mobile.data.util.CommonBaseUrls;
import com.mosl.mobile.domain.location.LocationRepository;
import com.mosl.tabapp.BuildConfig;
import com.mosl.tabapp.CertPinProviderImpl;
import com.mosl.tabapp.EkycAPIModule;
import com.mosl.tabapp.R;
import com.mosl.tabapp.drawermenu.data.source.MenuDataSource;
import com.mosl.tabapp.drawermenu.data.source.MenuDataSourceImpl;
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl;
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository;
import com.mosl.tabapp.leaddetail.LeadDetailRepository;
import com.mosl.tabapp.leaddetail.LeadDetailRepositoryImpl;
import com.mosl.tabapp.saathiIndirect.LeadCreation.data.IndirectLeadCreationRepositoryImpl;
import com.mosl.tabapp.saathiIndirect.LeadCreation.domain.IndirectLeadCreationRepository;
import com.mosl.tabapp.urls.ApiBaseUrlImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(includes = { BaseDataModule.class, RepositoryAliasProviders.class, EkycAPIModule.class })
public abstract class CommonDataModule {
    @Provides
    @Singleton
    static CommonBaseUrls provideApiBaseUrl() {
        return new ApiBaseUrlImpl();
    }

    @Provides
    static CertPinProvider provideCertPinnerProvider() {
        return CertPinProviderImpl.INSTANCE;
    }

    @Binds abstract MenuDataSource provideMenuDataSource(MenuDataSourceImpl dataSource);

    @Binds abstract LocationRepository provideLocationRepository(LocationRepositoryImpl dataSource);

    @Binds abstract LeadCreationRepository bindLeadCreationRepository(
        LeadCreationRepositoryImpl repository);

    @Binds abstract IndirectLeadCreationRepository bindIndirectLeadCreationRepository(
            IndirectLeadCreationRepositoryImpl repository);

    @Binds abstract LeadDetailRepository bindLeadDetailRepository(
        LeadDetailRepositoryImpl repository);

    @Binds abstract AttendanceRepository bindAttendanceRepository(
            AttendanceRepositoryImpl repository);

    @Singleton
    @Provides
    static AppInfo provideAppInfo(Context context) {
        return new AppInfo(context.getString(R.string.app_name), "Android",
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }

}