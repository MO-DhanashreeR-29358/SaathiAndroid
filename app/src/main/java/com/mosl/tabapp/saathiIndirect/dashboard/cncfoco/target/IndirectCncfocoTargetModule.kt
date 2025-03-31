package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target


import dagger.Module
import dagger.Provides

@Module
class IndirectCncfocoTargetModule {
    @Provides
    internal fun provideTargetView(fragment: IndirectCncfocoTargetFragment): IndirectCncTargetView {
        return fragment
    }
}