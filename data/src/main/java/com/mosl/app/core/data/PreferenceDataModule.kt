package com.mosl.mobile.coredata

import android.content.SharedPreferences
import com.mosl.app.core.prefs.BooleanPreference
import com.mosl.mobile.app_user.FORCE_PREFERENCE_DELETE
import com.mosl.mobile.user.annotation.ForcePreferenceDelete
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceDataModule {

    @ForcePreferenceDelete
    @Singleton
    @Provides
    internal fun provideForcePreferenceDelete(prefs: SharedPreferences): BooleanPreference {
        return BooleanPreference(prefs, FORCE_PREFERENCE_DELETE, true)
    }
}