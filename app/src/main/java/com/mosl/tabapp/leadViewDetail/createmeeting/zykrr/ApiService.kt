package com.mosl.tabapp.leadViewDetail.createmeeting.zykrr

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {

    @GET("login")
    fun getToken(
        @Header("username") username: String,
        @Header("password") password: String
    ): Call<tokenModel>

    @GET("rulesEngine")
    fun registerParticipants(
        @Header("Content-Type") contentType: String,
        @Header("Cookie") token : String,
        @Body request: registerRequest
    ): Call<registerResponse>
}