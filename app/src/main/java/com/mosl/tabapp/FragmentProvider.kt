package com.mosl.tabapp

import com.mosl.tabapp.dashboard.DashboardFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {
//  @ContributesAndroidInjector(modules = [QueryModule::class])
//  abstract fun provideQueryFragmentFactory(): QueryFragmentNew

  @ContributesAndroidInjector
  abstract fun provideDashboardFragmentFactory(): DashboardFragment
}