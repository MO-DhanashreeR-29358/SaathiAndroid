package com.mosl.tabapp

import android.app.Application
import android.content.Context
import com.mosl.app.core.data.BaseApiModule
import com.mosl.app.core.data.DataModule
import com.mosl.tabapp.data.CommonDataModule
import dagger.Binds
import dagger.Module

@Module(
    includes = [UiModule::class, BaseApiModule::class, CommonDataModule::class, DataModule::class, CommonUiModel::class]
)
abstract class ApplicationModule {
  @Binds
  internal abstract fun provideApplication(application: SaathiAppNew): Application

  @Binds
  internal abstract fun provideContext(application: Application): Context
}