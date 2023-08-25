package com.mosl.tabapp.reactivation.detail

import dagger.Binds
import dagger.Module

@Module
abstract class ReactivationDetailModule {
    @Binds
    abstract fun provideRCTDetailContractView(fragment: ReactivationDetailFragment?): ReactivationDetailView?

}