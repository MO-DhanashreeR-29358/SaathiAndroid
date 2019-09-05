package com.mosl.mobile.user.model;

import android.content.Context;
import android.content.SharedPreferences;
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

    @Binds abstract LoginRepository provideLoginRepository(LoginRepositoryImpl userManager);

    @Binds abstract AppUserRepository provideAppUserRepository(AppUserRepositoryImpl userManager);

    @Binds abstract UserCache provideUserCache(UserCacheImpl userCache);

	@Binds abstract UserManager provideUserManager(UserManagerImpl userManager);

    @Provides @UserPreference @Singleton
    public static SharedPreferences provideUserCredPreference(Context context) {
        return AppUserHelperKt.getPref(context, context.getPackageName() + USER_MANAGER_PREF);
    }

    @Provides @Singleton
    public static UserPrefStore provideUserPrefStore(
        @UserPreference SharedPreferences preferences) {
        return PrefStoreProvider.INSTANCE.createUserPrefStore(preferences);
    }

}
