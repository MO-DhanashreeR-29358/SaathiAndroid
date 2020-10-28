package com.mosl.mobile.coredata

import android.content.SharedPreferences
import com.mosl.app.core.prefs.IntegerPreference
import com.mosl.mobile.app_user.PREFERENCE_CLEAR
import com.mosl.mobile.user.annotation.PreferenceClear
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceDataModule {

    @PreferenceClear
    @Singleton
    @Provides
    fun providePreferenceClear(prefs: SharedPreferences): IntegerPreference {
        return IntegerPreference(prefs, PREFERENCE_CLEAR, 199)
    }
}