package com.mosl.tabapp.ws;

import android.content.Context;

import com.google.gson.Gson;
import com.mosl.app.core.data.MOSLResponseWrapper;
import com.mosl.app.core.rx.DispatchersKt;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.tabapp.EncryptionInterceptor;
import com.mosl.tabapp.urls.ServiceUrlsImpl;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppProvider {
    private static SaathiApi saathiApi;
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static SaathiApi buildSaathiApi(Context context) {
        RxJava2CallAdapterFactory callAdapterFactory =
            RxJava2CallAdapterFactory.createWithScheduler(DispatchersKt.RxDispatchers.io);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client =
            new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(context))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new EncryptionInterceptor())
                .build();

        return new Retrofit.Builder().baseUrl(ServiceUrlsImpl.CRM_BASE_URL())
            .addConverterFactory(new MOSLResponseWrapper())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .addCallAdapterFactory(
                callAdapterFactory)
            .client(client)
            .build()
            .create(SaathiApi.class);
    }

    public static SaathiApi initSaathiApi(Context context) {
        if (saathiApi == null) {
            saathiApi = buildSaathiApi(context);
        }
        return saathiApi;
    }
}
