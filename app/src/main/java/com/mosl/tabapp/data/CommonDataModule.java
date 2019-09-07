package com.mosl.tabapp.data;

import com.mosl.app.core.data.util.BaseDataModule;
import dagger.Module;

@Module(includes = { BaseDataModule.class })
public abstract class CommonDataModule {
	//@Provides @Singleton static CommonBaseUrls provideApiBaseUrl() {
	//	return new ApiBaseUrlImpl();
	//}
}