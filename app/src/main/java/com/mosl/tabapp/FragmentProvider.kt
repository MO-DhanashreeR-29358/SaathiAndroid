package com.mosl.tabapp

import com.mosl.tabapp.query.QueryFragmentNew
import com.mosl.tabapp.query.QueryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {
  @ContributesAndroidInjector(modules = [QueryModule::class])
  abstract fun provideQueryFragmentFactory(): QueryFragmentNew
}