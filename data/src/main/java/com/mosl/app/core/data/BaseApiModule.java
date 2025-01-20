package com.mosl.app.core.data;

import com.mosl.app.core.data.meta.InternalAPIClient;
import com.mosl.app.core.data.meta.InternalAPIRetrofit;
import com.mosl.app.core.data.meta.MyZoneAPIClient;
import com.mosl.app.core.data.meta.MyZoneAPIRetrofit;
import com.mosl.app.core.data.meta.SaathiIndirectAPIRetrofit;
import com.mosl.app.core.data.meta.SaathiIndrectAPIClient;
import com.mosl.mobile.data.util.CommonBaseUrls;
import com.mosl.saathi.api.MyZoneApi;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.saathi.api.SaathiIndirectApi;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module public abstract class BaseApiModule {

    @Provides @Singleton
    static SaathiApi provideSaathiApi(@InternalAPIClient OkHttpClient internalOkClient,
        @InternalAPIRetrofit Retrofit.Builder builder, CommonBaseUrls apiBaseUrls) {
        return builder.client(internalOkClient).baseUrl(apiBaseUrls.getSaathiApiUrl())
            .build()
            .create(SaathiApi.class);
    }

    @Provides @Singleton
    static MyZoneApi provideMyZoneApi(@MyZoneAPIClient OkHttpClient internalOkClient,
                                      @MyZoneAPIRetrofit Retrofit.Builder builder, CommonBaseUrls apiBaseUrls) {
        return builder.client(internalOkClient).baseUrl(apiBaseUrls.getMyZoneApiUrl())
                .build()
                .create(MyZoneApi.class);
    }

    @Provides @Singleton
    static SaathiIndirectApi provideSaathiIndirectApi(@SaathiIndrectAPIClient OkHttpClient internalOkClient,
                                                      @SaathiIndirectAPIRetrofit Retrofit.Builder builder, CommonBaseUrls apiBaseUrls) {
        return builder.client(internalOkClient).baseUrl(apiBaseUrls.getSaathiIndirectUrl())
                .build()
                .create(SaathiIndirectApi.class);
    }
}
