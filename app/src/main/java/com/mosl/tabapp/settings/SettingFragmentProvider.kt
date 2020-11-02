package com.mosl.tabapp.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingFragmentProvider {

    @ContributesAndroidInjector
    internal abstract fun provideSettingFragment(): SettingFragment

}