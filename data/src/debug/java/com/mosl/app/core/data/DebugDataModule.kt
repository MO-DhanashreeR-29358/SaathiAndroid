package com.mosl.app.core.data

import com.mosl.app.core.data.interceptor.EncryptionInterceptor
import com.mosl.app.core.data.interceptor.HeaderInterceptor
import com.mosl.app.core.data.interceptor.MyZoneHeaderInterceptor
import com.mosl.app.core.data.meta.InternalAPIClient
import com.mosl.app.core.data.meta.MyZoneAPIClient
import com.mosl.app.core.data.meta.SaathiIndrectAPIClient
import com.mosl.app.core.data.util.CertPinProvider
import com.mosl.app.core.data.util.HttpListenerFactory
import com.mosl.user.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Logger
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class DebugDataModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(object : Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp")
                    .d(message)
            }
        })
        return httpLoggingInterceptor.apply { httpLoggingInterceptor.level = Level.BODY }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("BaseOkClient") okHttpClient: OkHttpClient
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .build()
    }

    @Provides
    @Singleton
    @Named("BaseOkClient")
    fun provideBaseOkHttpClient(
        okHttpCache: Cache,
        pinProvider: CertPinProvider
    ): OkHttpClient {
        return createOkHttpClient(pinProvider.getPinner())
            .cache(okHttpCache)
            .build()
    }

    internal fun createOkHttpClient(pinner: CertificatePinner?): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        if (pinner != null) {
            builder.certificatePinner(pinner)
        }

        return builder
    }

    @Provides
    @Singleton
    @InternalAPIClient
    fun provideInternalAPIOkHttpClient(
        @Named("BaseOkClient") okHttpClient: OkHttpClient,
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
        encryptionInterceptor: EncryptionInterceptor,
        listenerFactory: HttpListenerFactory
//    authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(encryptionInterceptor)
            .addInterceptor(loggingInterceptor)
            .eventListenerFactory(listenerFactory)
//        .addInterceptor(authorizationInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @MyZoneAPIClient
    fun provideMyZoneAPIOkHttpClient(
        @Named("BaseOkClient") okHttpClient: OkHttpClient,
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: MyZoneHeaderInterceptor,
        listenerFactory: HttpListenerFactory,
       authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(loggingInterceptor)
            .eventListenerFactory(listenerFactory)
            .addInterceptor(authorizationInterceptor)
            .build()
    }


    @Provides
    @Singleton
    @SaathiIndrectAPIClient
    fun provideSaathiIndirectAPIOkHttpClient(
        @Named("BaseOkClient") okHttpClient: OkHttpClient,
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
        encryptionInterceptor: EncryptionInterceptor,
        listenerFactory: HttpListenerFactory
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(encryptionInterceptor)
            .addInterceptor(loggingInterceptor)
            .eventListenerFactory(listenerFactory)
            .build()
    }

}