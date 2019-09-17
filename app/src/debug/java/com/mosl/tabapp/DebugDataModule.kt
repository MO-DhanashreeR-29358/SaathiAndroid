package com.mosl.mobile.saathi

import com.mosl.app.core.data.interceptor.EncryptionInterceptor
import com.mosl.app.core.data.interceptor.HeaderInterceptor
import com.mosl.app.core.data.meta.InternalAPIClient
import dagger.Module
import dagger.Provides
import okhttp3.Cache
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
    okHttpCache: Cache
  ): OkHttpClient {
    return createOkHttpClient()
        .cache(okHttpCache)
        .build()
  }

  internal fun createOkHttpClient(): OkHttpClient.Builder {
    return OkHttpClient.Builder()
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
  }

  @Provides
  @Singleton
  @InternalAPIClient
  fun provideInternalAPIOkHttpClient(
    @Named("BaseOkClient") okHttpClient: OkHttpClient,
    loggingInterceptor: HttpLoggingInterceptor,
    headerInterceptor: HeaderInterceptor,
    encryptionInterceptor: EncryptionInterceptor
//    authorizationInterceptor: AuthorizationInterceptor
  ): OkHttpClient {
    return okHttpClient.newBuilder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(encryptionInterceptor)
        .addInterceptor(loggingInterceptor)
//        .addInterceptor(authorizationInterceptor)
        .build()
  }

}