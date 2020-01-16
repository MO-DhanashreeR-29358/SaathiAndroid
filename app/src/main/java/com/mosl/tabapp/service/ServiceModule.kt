package com.mosl.tabapp.service

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

  @ContributesAndroidInjector
  abstract fun provideForgroundService(): ForegroundService

  @ContributesAndroidInjector
  abstract fun provideMessageReceiverIntentServiceFactory(): MessageReceiverIntentService

}