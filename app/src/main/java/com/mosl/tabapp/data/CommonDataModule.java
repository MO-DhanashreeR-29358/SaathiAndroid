package com.mosl.tabapp.data;

import com.mosl.app.core.data.RepositoryAliasProviders;
import com.mosl.app.core.data.location.LocationRepositoryImpl;
import com.mosl.app.core.data.util.BaseDataModule;
import com.mosl.app.core.data.util.CertPinProvider;
import com.mosl.mobile.domain.location.LocationRepository;
import com.mosl.tabapp.CertPinProviderImpl;
import com.mosl.tabapp.drawermenu.data.source.MenuDataSource;
import com.mosl.tabapp.drawermenu.data.source.MenuDataSourceImpl;
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl;
import com.mosl.tabapp.leadcreation.domain.LeadCreationRepository;
import com.mosl.tabapp.leaddetail.LeadDetailRepository;
import com.mosl.tabapp.leaddetail.LeadDetailRepositoryImpl;
import com.mosl.tabapp.urls.ServiceUrlsImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(includes = {BaseDataModule.class, RepositoryAliasProviders.class})
public abstract class CommonDataModule {
	@Provides @Singleton static String provideApiBaseUrl() {
		return ServiceUrlsImpl.CRM_BASE_URL();
	}

	@Binds abstract MenuDataSource provideMenuDataSource(MenuDataSourceImpl dataSource);

	@Binds abstract LocationRepository provideLocationRepository(LocationRepositoryImpl dataSource);

    @Binds abstract LeadCreationRepository bindLeadCreationRepository(
        LeadCreationRepositoryImpl repository);

	@Binds abstract LeadDetailRepository bindLeadDetailRepository(
		LeadDetailRepositoryImpl repository);


	@Provides
	static CertPinProvider provideCertPinnerProvider() {
		return CertPinProviderImpl.INSTANCE;
	}
}