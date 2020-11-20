package com.mosl.tabapp.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

abstract class SettingFragmentProvider {

    @ContributesAndroidInjector(modules = [SettingFragmentModule::class])
    internal abstract fun provideSettingFragment(): SettingFragment

}