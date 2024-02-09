package com.mosl.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.mosl.core.annotations.AppUpdateDetails;
import com.mosl.core.annotations.ConveyanceAuthToken;
import com.mosl.core.annotations.DepartmentIds;
import com.mosl.core.annotations.EncryptedTokenForAuthToken;
import com.mosl.core.annotations.LastLocationAPITime;
import com.mosl.core.annotations.LastLocationTime;
import com.mosl.core.annotations.ServerTime;
import com.mosl.core.annotations.TwentyFourSevenCheck;
import com.mosl.user.data.login.MyZoneLoginRepositoryImpl;
import com.mosl.user.domain.LoginRepository;
import com.mosl.user.helper.AppUserHelperKt;
import com.mosl.user.helper.Clock;
import com.mosl.user.helper.LoginPrefStore;
import com.mosl.user.helper.RealClock;
import com.mosl.user.helper.TokenPrefStore;
import com.mosl.user.login.MyZoneUserManagerImpl;
import com.mosl.user.login.UserManager;
import com.mosl.user.login.cache.UserCache;
import com.mosl.user.login.cache.UserCacheImpl;
import com.mosl.user.meta.UserPreference;
import com.mosl.user.prefs.BooleanPreference;
import com.mosl.user.prefs.LongPreference;
import com.mosl.user.prefs.SafeStringPreference;
import com.mosl.user.prefs.StringPreference;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import mosl.rx_preferences.BuildConfig;

@Module
public abstract class AppUserModule {
    private static final String PREF_USER_DATA = "pref_user_data";
    private static final String LAST_LOCATION_TIME_KEY = "LastLocationTime";
    private static final String LAST_LOCATION_API_TIME_KEY = "LastLocationAPITime";
    private static final String TWENTY_FOUR_SEVEN_CHECK = "TwentyFourSevenCheck";
    private static final String APP_UPDATE_DETAILS = "AppUpdateDetails";
    private static final String CONVEYANCE_AUth_TOKEN = "ConveyanceAuthToken";
    private static final String ENCRYPTED_TOKEN_FOT_AUTH_TOKEN = "EncryptedTokenForAuthToken";
    private static final String PREF_SERVER_TIME = "pref_server_time_millis";
    private static final String PREF_DEPARTMENT_ID = "department_ids";
    @Provides
    @Singleton
    static LoginPrefStore provideLoginPrefStore(@UserPreference SharedPreferences preferences) {
        return TokenPrefStore.Companion.createLoginPrefStore(preferences);
    }

    @Provides
    @UserPreference
    @Singleton
    static SharedPreferences provideUserCredPreference(Context context) {
        String USER_CREDENTIAL_PREF = BuildConfig.LIBRARY_PACKAGE_NAME + ".login_manager";
        return AppUserHelperKt.getPref(context, USER_CREDENTIAL_PREF);
    }

    @LastLocationTime
    @Singleton
    @Provides
    static StringPreference provideLastLocationTimePref(SharedPreferences prefs) {
        return new StringPreference(prefs, LAST_LOCATION_TIME_KEY, "");
    }

    @LastLocationAPITime
    @Singleton
    @Provides
    static StringPreference provideLastLocationAPITimePref(SharedPreferences prefs) {
        return new StringPreference(prefs, LAST_LOCATION_API_TIME_KEY, "");
    }

    @TwentyFourSevenCheck
    @Singleton
    @Provides
    static BooleanPreference provideTwentyFourSevenPref(SharedPreferences prefs) {
        return new BooleanPreference(prefs, TWENTY_FOUR_SEVEN_CHECK, false);
    }

    @AppUpdateDetails
    @Singleton
    @Provides
    static BooleanPreference provideAppUpdateDetailsPref(SharedPreferences prefs) {
        return new BooleanPreference(prefs, APP_UPDATE_DETAILS, false);
    }

    @ConveyanceAuthToken
    @Singleton
    @Provides
    static StringPreference provideConveyancePref(SharedPreferences prefs) {
        return new StringPreference(prefs, CONVEYANCE_AUth_TOKEN, "");
    }

    @EncryptedTokenForAuthToken
    @Singleton
    @Provides
    static StringPreference provideAuthToken(SharedPreferences prefs) {
        return new StringPreference(prefs, ENCRYPTED_TOKEN_FOT_AUTH_TOKEN, "");
    }

    @Provides
    static SafeStringPreference provideUserDataPreference(
            @UserPreference SharedPreferences preferences) {
        return new SafeStringPreference(preferences, PREF_USER_DATA);
    }

    @DepartmentIds
    @Singleton
    @Provides
    static StringPreference provideDepartmentIds(SharedPreferences prefs) {
        return new StringPreference(prefs, PREF_DEPARTMENT_ID, "");
    }


    @ServerTime
    @Singleton
    @Provides
    static LongPreference provideServerTimePref(SharedPreferences prefs) {
        return new LongPreference(prefs, PREF_SERVER_TIME, 0);
    }

    @Provides
    @Singleton
    static TokenPrefStore provideTokenPrefStore(@UserPreference SharedPreferences preferences) {
        return TokenPrefStore.Companion.createTokenPrefStore(preferences);
    }

    @Provides
    static Clock provideClock(@ServerTime LongPreference serverTime) {
        return RealClock.INSTANCE;
    }

    @Binds
    public abstract LoginRepository provideLoginRepository(MyZoneLoginRepositoryImpl repositoryImpl);

    @Binds
    public abstract UserCache provideUserCache(UserCacheImpl userCache);

    @Binds
    public abstract UserManager provideUserManager(MyZoneUserManagerImpl userManager);
}
