package com.mosl.tabapp.advancesearch

import dagger.Binds
import dagger.Module

@Module
abstract class QuickSearchTabModule {
    @Binds
    abstract fun provideQuickSearchTabModule(
        fragment: QuickSearchTabFragment?
    ): QuickSearchTabView?

}