package com.mosl.tabapp.data;

import com.mosl.app.core.data.util.BaseDataModule;
import com.mosl.tabapp.urls.ServiceUrlsImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(includes = { BaseDataModule.class })
public abstract class CommonDataModule {
	@Provides @Singleton static String provideApiBaseUrl() {
		return ServiceUrlsImpl.CRM_BASE_URL();
	}
}