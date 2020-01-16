package com.mosl.tabapp;

import com.mosl.app.core.base.ViewContainer;
import dagger.Module;
import dagger.Provides;

@Module public abstract class DebugUiModule {

	@Provides static ViewContainer provideViewContainer() {
		return ViewContainer.DEFAULT;
	}
}
