package com.mosl.tabapp

import android.app.Application
import android.content.Context
import com.mosl.app.core.data.BaseApiModule
import com.mosl.mobile.saathi.DebugDataModule
import com.mosl.tabapp.data.CommonDataModule
import dagger.Binds
import dagger.Module

@Module(
    includes = [DebugUiModule::class, BaseApiModule::class, CommonDataModule::class,
      DebugDataModule::class, CommonUiModel::class]
)
abstract class DebugModule {
  @Binds
  internal abstract fun provideApplication(application: SaathiAppNew): Application

  @Binds
  internal abstract fun provideContext(application: Application): Context
}