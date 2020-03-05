package com.mosl.app.core.data.util;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mosl.app.core.data.MOSLResponseWrapper;
import com.mosl.app.core.data.meta.InternalAPIRetrofit;
import com.mosl.app.core.rx.RxDispatchers;
import com.mosl.commons.rx.Dispatchers;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

@Module
public abstract class BaseDataModule {
    @Provides
    static Dispatchers provideDispatchers() {
        return RxDispatchers.DefaultDispatchers;
    }

    @Provides
    @Singleton
    static RxJava2CallAdapterFactory getRxJava2CallAdapter() {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    @Singleton
    @InternalAPIRetrofit
    static Retrofit.Builder provideNewRetrofitBuilder(OkHttpClient client,
                                                      MOSLResponseWrapper moslResponseWrapper, GsonConverterFactory gsonConverterFactory,
        RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
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
                                                   RxJava2CallAdapterFactory rxJava2CallAdapterFactory) {
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
}