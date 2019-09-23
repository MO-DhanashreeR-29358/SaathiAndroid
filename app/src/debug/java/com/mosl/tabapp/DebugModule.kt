package com.mosl.mobile.saathi

import android.app.Application
import android.content.Context
import com.mosl.app.core.data.BaseApiModule
import com.mosl.tabapp.CommonUiModel
import com.mosl.tabapp.DebugUiModule
import com.mosl.tabapp.SaathiAppNew
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