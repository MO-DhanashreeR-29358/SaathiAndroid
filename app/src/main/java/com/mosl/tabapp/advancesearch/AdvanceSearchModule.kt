package com.mosl.tabapp.advancesearch


import dagger.Binds
import dagger.Module

@Module
abstract class AdvanceSearchModule {
    @Binds
    abstract fun provideAdvanceSearchModule(
        fragment: AdvanceSearchFragment?
    ): AdvanceSearchView?

}