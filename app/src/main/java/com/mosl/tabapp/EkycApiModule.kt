//package com.mosl.tabapp
//
//import com.mosl.ekycexpress.ActivityBuilder
//import com.mosl.ekycexpress.api.EkycApi
//import com.mosl.ekycexpress.core.meta.EKycClient
//import com.mosl.ekycexpress.core.meta.InternalAPIRetrofit
//import com.mosl.ekycexpress.login.api.LoginApi
//import com.mosl.mobile.data.util.CommonBaseUrls
//import dagger.Module
//import dagger.Provides
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import javax.inject.Singleton
//
//@Module(
//    includes = [
//      com.mosl.ekycexpress.DataModule::class,
//      com.mosl.ekycexpress.data.CommonDataModule::class,
//      ActivityBuilder::class
//    ]
//)
//class EkycAPIModule {
//  @Provides
//  @Singleton
//  fun provideLoginApi(
//    @EKycClient internalOkClient: OkHttpClient,
//    @InternalAPIRetrofit builder: Retrofit.Builder,
//    apiBaseUrls: CommonBaseUrls
//  ): LoginApi {
//    return builder.client(internalOkClient)
//        .baseUrl(apiBaseUrls.getEkycExpressApiUrl())
//        .build()
//        .create(LoginApi::class.java)
//  }
//
//  @Provides
//  @Singleton
//  fun provideEkycApi(
//    @EKycClient internalOkClient: OkHttpClient,
//    @InternalAPIRetrofit builder: Retrofit.Builder,
//    commonBaseUrls: CommonBaseUrls
//  ): EkycApi {
//    return builder.client(internalOkClient)
//        .baseUrl(commonBaseUrls.getEkycExpressApiUrl())
//        .build()
//        .create(EkycApi::class.java)
//  }
//}