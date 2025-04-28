package com.mosl.tabapp.bankmeeting.otp

import com.mosl.tabapp.leadViewDetail.createmeeting.OTPContract
import com.mosl.tabapp.leadViewDetail.createmeeting.OTPFragment
import dagger.Binds
import dagger.Module

@Module
abstract class OTPModule {
  @Binds
  internal abstract fun provideOTPContract(fragment: OTPFragment): OTPContract
}