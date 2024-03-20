package com.mosl.tabapp.login.otpverification

import dagger.Binds
import dagger.Module

@Module
abstract class OtpVerificationModule {
    @Binds
    abstract fun provideOtpVierificationModule(fragment: OtpVerificationFragment): OtpVerificationView
}