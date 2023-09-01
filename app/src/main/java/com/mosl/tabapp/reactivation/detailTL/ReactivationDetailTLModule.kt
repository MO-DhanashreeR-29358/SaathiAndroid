package com.mosl.tabapp.reactivation.detailTL

import dagger.Binds
import dagger.Module

@Module
abstract class ReactivationDetailTLModule {
    @Binds
    abstract fun provideRCTDetailTLContractView(fragment: ReactivationDetailTLFragment?): ReactivationDetailTLView?

}