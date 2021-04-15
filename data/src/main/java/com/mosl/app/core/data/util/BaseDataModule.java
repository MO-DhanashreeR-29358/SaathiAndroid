package com.mosl.app.core.data.util;

import android.content.Context;

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.soloader.SoLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mosl.app.core.data.MOSLResponseWrapper;
import com.mosl.app.core.data.meta.InternalAPIRetrofit;
import com.mosl.app.core.rx.RxDispatchers;
import com.mosl.commons.rx.Dispatchers;
import com.mosl.mobile.coredata.PreferenceDataModule;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

@Module(includes = { PreferenceDataModule.class })
public abstract class BaseDataModule {
    @Provides
    static Dispatchers provideDispatchers() {
        return RxDispatchers.DefaultDispatchers;
    }

    @Provides
    @Singleton
    static RxJava3CallAdapterFactory getRxJava2CallAdapter() {
        return RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    @Singleton
    @InternalAPIRetrofit
    static Retrofit.Builder provideNewRetrofitBuilder(OkHttpClient client,
                                                      MOSLResponseWrapper moslResponseWrapper, GsonConverterFactory gsonConverterFactory,
                                                      RxJava3CallAdapterFactory rxJava2CallAdapterFactory,
                                                      ScalarsConverterFactory scalarsConverterFactory) {
        return new Retrofit.Builder().client(client)
            .addConverterFactory(scalarsConverterFactory)
                .addConverterFactory(moslResponseWrapper)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory);
    }

    @Provides
    @Singleton
    static Retrofit.Builder provideRetrofitBuilder(OkHttpClient client,
                                                   MOSLResponseWrapper moslResponseWrapper,
                                                   GsonConverterFactory gsonConverterFactory,
                                                   RxJava3CallAdapterFactory rxJava2CallAdapterFactory) {
        return new Retrofit.Builder().client(client)
                .addConverterFactory(moslResponseWrapper)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory);
    }

    @Provides
    @Singleton
    static Cache provideCache(Context context) {
        Cache cache = null;
        try {
            cache =
                    new Cache(new File(context.getCacheDir(), "http-cache"), 10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Timber.e(e, "Could not create Cache!");
        }
        return cache;
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    static GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

  @Provides
  @Singleton
  static ScalarsConverterFactory provideScalarConvertetFactory() {
      return ScalarsConverterFactory.create();
  }

    @Provides
    @Singleton
    static HttpListenerFactory provideEventListenerFactory() {
        return HttpListenerFactory.INSTANCE;
    }

    @Singleton @Provides static Crypto provideCrypto(Context context) {
        try {
            SoLoader.init(context, false);
        } catch (Exception e) {
            Timber.e(e);
        }
        SharedPrefsBackedKeyChain keyChain =
            new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        Timber.e("Crypto status, enabled : %s", crypto.isAvailable());
        return crypto;
    }
}