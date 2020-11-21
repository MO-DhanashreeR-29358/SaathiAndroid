package com.mosl.tabapp.settings

import com.mosl.tabapp.common.FusedLocation
import dagger.Module
import dagger.Provides

@Module
abstract class SettingFragmentModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideSettingsFragment(appCompatActivity: SettingActivity): UserLocationHelper {
            return UserLocationHelper(appCompatActivity)
        }
    }

}
