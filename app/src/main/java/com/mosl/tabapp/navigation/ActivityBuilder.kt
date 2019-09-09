package com.mosl.tabapp

import com.mosl.annotations.ActivityScoped
import com.mosl.tabapp.core.CommonActivity
import com.mosl.tabapp.core.CommonActivityModule
import com.mosl.tabapp.navigation.CommonActivityBuilder
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [CommonActivityBuilder::class])
abstract class ActivityBuilder {
  @ActivityScoped
  @ContributesAndroidInjector(modules = [CommonActivityModule::class])
  internal abstract fun bindCommonActivity(): CommonActivity
}