package com.mosl.mobile.saathi

import com.mosl.annotations.ActivityScoped
import com.mosl.tabapp.core.CommonActivity
import com.mosl.tabapp.core.CommonActivityModule
import com.mosl.tabapp.navigation.CommonActivityBuilder
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [CommonActivityBuilder::class])
abstract class DebugActivityBuilder {
  @ActivityScoped
  @ContributesAndroidInjector(modules = [CommonActivityModule::class])
  abstract fun bindCommonActivity(): CommonActivity
}