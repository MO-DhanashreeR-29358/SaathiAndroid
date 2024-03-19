package com.mosl.tabapp

import com.mosl.tabapp.login.otpverification.OtpVerificationFragment
import com.mosl.tabapp.login.otpverification.OtpVerificationModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginFragmentProvider {
    @ContributesAndroidInjector(modules=[OtpVerificationModule::class])
    abstract fun provideOtpVerificationFragmentFactory(): OtpVerificationFragment
}