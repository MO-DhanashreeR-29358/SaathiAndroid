package com.mosl.tabapp.advancesearch.advanceSearch

import dagger.Binds
import dagger.Module

@Module
abstract class AdvanceSearchTabModule {
    @Binds
    abstract fun provideAdvanceSearchTabModule(
        fragment: AdvanceSearchTabFragment?
    ): AdvanceSearchTabView?

}