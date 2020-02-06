package com.mosl.mobile.pdf

import com.mosl.tabapp.leadViewDetail.createmeeting.OTPContract
import dagger.Module
import dagger.Provides

@Module
class OTPModule {

    @Provides
    internal fun ProvidePdfFragment(fragment: OTPFragment): OTPContract {
        return fragment
    }
}