package com.mosl.app.core.data;

import com.mosl.app.core.data.interceptor.EncryptionInterceptor;
import com.mosl.app.core.data.interceptor.HeaderInterceptor;
import com.mosl.app.core.data.meta.InternalAPIClient;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module public abstract class DataModule {
    /*@Provides @Singleton public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }*/

	static OkHttpClient.Builder createOkHttpClient() {
        return new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS);
    }

    @Provides @Singleton static OkHttpClient provideOkHttpClient(Cache okHttpCache) {
        return createOkHttpClient().cache(okHttpCache).build();
    }

    @Provides @Singleton @InternalAPIClient
    static OkHttpClient provideInternalAPIOkHttpClient(OkHttpClient okHttpClient,
        HttpLoggingInterceptor loggingInterceptor,
        HeaderInterceptor headerInterceptor,
        EncryptionInterceptor encryptionInterceptor) {
        return okHttpClient.newBuilder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(encryptionInterceptor)
            .addInterceptor(loggingInterceptor)
            .build();
    }

    @Provides @Singleton static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message)).setLevel(
            HttpLoggingInterceptor.Level.BODY);
    }
}
