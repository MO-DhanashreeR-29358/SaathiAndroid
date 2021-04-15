package com.mosl.mobile.user.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.mosl.annotations.FcmToken;
import com.mosl.annotations.FcmTokenRequest;
import com.mosl.annotations.ServerTime;
import com.mosl.app.core.prefs.LongPreference;
import com.mosl.app.core.prefs.StringPreference;
import com.mosl.app.core.utils.RealClock;
import com.mosl.commons.utils.Clock;
import com.mosl.mobile.user.UserPreference;
import com.mosl.mobile.user.annotation.LastLocationAPITime;
import com.mosl.mobile.user.annotation.LastLocationTime;
import com.mosl.mobile.user.annotation.RemoteConfigData;
import com.mosl.mobile.user.annotation.SafePreference;
import com.mosl.mobile.user.helper.AppUserHelperKt;
import com.mosl.mobile.user.helper.PrefStoreProvider;
import com.mosl.mobile.user.helper.UserPrefStore;
import com.mosl.mobile.user.login.AppUserRepository;
import com.mosl.mobile.user.login.AppUserRepositoryImpl;
import com.mosl.mobile.user.login.LoginRepository;
import com.mosl.mobile.user.login.LoginRepositoryImpl;
import com.mosl.mobile.user.login.UserCache;
import com.mosl.mobile.user.login.UserCacheImpl;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.mobile.user.login.UserManagerImpl;
import com.mosl.token.manager.prefs.TokenPrefStore;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import mosl.rx_preferences.Preference;
import mosl.rx_preferences.RxSharedPreferences;

/**
 * Created by lalkishore on 14/01/18.
 */

@Module
public abstract class UserModule {
    private static final String USER_MANAGER_PREF = ".user_manager";
    private static final String GCM_TOKEN = "gcmToken";
    private static final String SAVED_TOKEN_REQUEST = "saved_token_request";
    private static final String REMOTE_CONFIG_DATA_KEY = "RemoteConfigData";
    private static final String LAST_LOCATION_TIME_KEY = "LastLocationTime";
    private static final String LAST_LOCATION_API_TIME_KEY = "LastLocationAPITime";
    private static final String TWENTY_FOUR_SEVEN_CHECK = "TwentyFourSevenCheck";
    private static final String SAFE_USER_MANAGER__PREF = ".safe_user_manager";
    @Provides
    @UserPreference
    @Singleton
    public static SharedPreferences provideUserCredPreference(Context context) {
        return AppUserHelperKt.getPref(context, context.getPackageName() + USER_MANAGER_PREF);
    }

    @Provides
    public static Clock provideClock(@ServerTime LongPreference serverTime) {
        return RealClock.INSTANCE;
    }

    @Provides
    @Singleton
    public static UserPrefStore provideUserPrefStore(
        @UserPreference SharedPreferences preferences) {
        return PrefStoreProvider.INSTANCE.createUserPrefStore(preferences);
    }

    @Provides
    @Singleton
    public static TokenPrefStore provideTokenPrefStore(
            @UserPreference SharedPreferences preferences, @SafePreference SharedPreferences safePrefrence) {
        return TokenPrefStore.Companion.createTokenPrefStore(preferences, safePrefrence);
    }

    @Provides
    @Singleton
    static RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
        return RxSharedPreferences.create(prefs);
    }

    @Provides
    @Singleton
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @FcmToken
    @Singleton
    @Provides
    static Preference<String> provideGcmToken(RxSharedPreferences prefs) {
        return prefs.getString(GCM_TOKEN, "0");
    }

    @FcmTokenRequest
    @Singleton
    @Provides
    static StringPreference provideFcmTokenRequest(SharedPreferences prefs) {
        return new StringPreference(prefs, SAVED_TOKEN_REQUEST, "");
    }

    @RemoteConfigData
    @Singleton
    @Provides
    static StringPreference provideRemoteConfigDataPref(SharedPreferences prefs) {
        return new StringPreference(prefs, REMOTE_CONFIG_DATA_KEY, "");
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

    @Binds
    abstract UserManager provideUserManager(UserManagerImpl userManager);

    @Binds
    abstract LoginRepository provideLoginRepository(LoginRepositoryImpl userManager);

    @Binds
    abstract AppUserRepository provideAppUserRepository(AppUserRepositoryImpl userManager);

    @Binds
    abstract UserCache provideUserCache(UserCacheImpl userCache);

    @Provides
    @SafePreference
    @Singleton
    static SharedPreferences provideSafePreference(Context context) {
        MasterKey masterKeyAlias = null;
        try {
            masterKeyAlias = new MasterKey.Builder(
                    context, "MASTER_KEY_ALIAS")
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        try {
            return EncryptedSharedPreferences.create(
                    context,
                    context.getPackageName() + SAFE_USER_MANAGER__PREF,
                    Objects.requireNonNull(masterKeyAlias),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
