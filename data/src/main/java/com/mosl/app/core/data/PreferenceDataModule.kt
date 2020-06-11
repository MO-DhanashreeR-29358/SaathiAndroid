package com.mosl.mobile.coredata

import android.content.SharedPreferences
import com.mosl.app.core.data.annotation.ForcePreferenceClear
import com.mosl.app.core.prefs.BooleanPreference
import com.mosl.mobile.app_user.FORCE_PREFERENCE_CLEAR
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceDataModule {

  @ForcePreferenceClear
  @Singleton
  @Provides
  internal fun provideForcePreferenceClear(prefs: SharedPreferences): BooleanPreference {
    return BooleanPreference(prefs, FORCE_PREFERENCE_CLEAR)
  }
}