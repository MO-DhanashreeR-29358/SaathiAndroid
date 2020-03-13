package com.mosl.mobile.pdf

import com.mosl.tabapp.leadViewDetail.createmeeting.OTPContract
import dagger.Binds
import dagger.Module

@Module
abstract class OTPModule {
  @Binds
  internal abstract fun provideOTPContract(fragment: OTPFragment): OTPContract
}