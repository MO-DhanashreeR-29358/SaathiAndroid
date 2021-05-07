package com.mosl.mobile.coredata

import android.content.SharedPreferences
import com.mosl.app.core.prefs.BooleanPreference
import com.mosl.app.core.prefs.IntegerPreference
import com.mosl.app.core.prefs.StringPreference
import com.mosl.mobile.app_user.BRANCH_CODE
import com.mosl.mobile.app_user.PREFERENCE_CLEAR
import com.mosl.mobile.app_user.TWENTY_FOUR_SEVEN_CHECK
import com.mosl.mobile.user.annotation.BranchCode
import com.mosl.mobile.user.annotation.PreferenceClear
import com.mosl.mobile.user.annotation.TwentyFourSevenCheck
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

    @BranchCode
    @Singleton
    @Provides
    fun provideBranchCode(prefs: SharedPreferences): StringPreference {
        return StringPreference(prefs, BRANCH_CODE)
    }

    @TwentyFourSevenCheck
    @Singleton
    @Provides
    fun provideTwentyFourSevenPref(prefs: SharedPreferences): BooleanPreference {
        return BooleanPreference(prefs, TWENTY_FOUR_SEVEN_CHECK, false)
    }

}