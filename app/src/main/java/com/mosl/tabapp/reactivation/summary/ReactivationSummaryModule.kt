package com.mosl.tabapp.reactivation.summary

import dagger.Binds
import dagger.Module

@Module
abstract class ReactivationSummaryModule {
    @Binds
    abstract fun provideRCTSummaryContractView(fragment: ReactivationSummaryFragment?): ReactivationSummaryView?

}