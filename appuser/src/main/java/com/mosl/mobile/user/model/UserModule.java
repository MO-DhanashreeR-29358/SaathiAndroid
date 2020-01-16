package com.mosl.mobile.user.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.mosl.annotations.FcmToken;
import com.mosl.annotations.FcmTokenRequest;
import com.mosl.app.core.prefs.StringPreference;
import com.mosl.mobile.user.UserPreference;
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
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by lalkishore on 14/01/18.
 */

@Module public abstract class UserModule {
	private static final String USER_MANAGER_PREF = ".user_manager";
	private static final String GCM_TOKEN = "gcmToken";
	private static final String SAVED_TOKEN_REQUEST = "saved_token_request";

	@Provides @UserPreference @Singleton
	public static SharedPreferences provideUserCredPreference(Context context) {
		return AppUserHelperKt.getPref(context, context.getPackageName() + USER_MANAGER_PREF);
	}

	@Provides @Singleton
	public static UserPrefStore provideUserPrefStore(
		@UserPreference SharedPreferences preferences) {
		return PrefStoreProvider.INSTANCE.createUserPrefStore(preferences);
	}

	@Provides @Singleton
	static RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
		return RxSharedPreferences.create(prefs);
	}

	@Binds abstract UserManager provideUserManager(UserManagerImpl userManager);

	@Provides @Singleton public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
	}

	@FcmToken @Singleton @Provides
	static Preference<String> provideGcmToken(RxSharedPreferences prefs) {
		return prefs.getString(GCM_TOKEN, "0");
	}

	@FcmTokenRequest @Singleton @Provides
	static StringPreference provideFcmTokenRequest(SharedPreferences prefs) {
		return new StringPreference(prefs, SAVED_TOKEN_REQUEST, "");
	}

	@Binds abstract LoginRepository provideLoginRepository(LoginRepositoryImpl userManager);

	@Binds abstract AppUserRepository provideAppUserRepository(AppUserRepositoryImpl userManager);

	@Binds abstract UserCache provideUserCache(UserCacheImpl userCache);
}
