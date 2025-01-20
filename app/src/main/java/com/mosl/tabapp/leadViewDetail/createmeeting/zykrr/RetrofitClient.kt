package com.mosl.tabapp.leadViewDetail.createmeeting.zykrr

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var BASE_URL: String = ""
    private var retrofitInstance: Retrofit? = null

    // Set the base URL and reset Retrofit instance when BASE_URL is changed
    fun setBaseUrl(url: String) {
        this.BASE_URL = url
        retrofitInstance = null  // Clear the cached Retrofit instance
    }

    // Get the Retrofit instance with the updated BASE_URL
    val retrofit: Retrofit
        get() {
            // If the Retrofit instance is null (not created yet), create a new one
            if (retrofitInstance == null) {
                if (BASE_URL.isEmpty()) {
                    throw IllegalStateException("Base URL must be set before calling retrofit.")
                }

                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

                retrofitInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)  // Use the updated BASE_URL
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofitInstance!!
        }
}

class ApiClient(retrofit: Retrofit) {
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
