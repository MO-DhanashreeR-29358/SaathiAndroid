package com.mosl.tabapp.core;

import com.mosl.navigator.FragmentResolver;
import com.mosl.tabapp.navigation.AppFragmentResolver;
import dagger.Module;
import dagger.Provides;

@Module public abstract class MainActivityModule {
    @Provides static FragmentResolver provideFragmentResolver() {
        return AppFragmentResolver.INSTANCE;
    }
}
