package com.mosl.tabapp.leadViewDetail.createmeeting.zykrr

import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.lead.createmeeting.SurveyLinkRequest
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    fun getToken(
        @Header("username") username: String,
        @Header("password") password: String
    ): Call<tokenModel>

    @POST("rulesEngine")
    fun registerParticipants(
        @Header("Content-Type") contentType: String,
        @Header("Cookie") token : String,
        @Body request: registerData
    ): Call<registerResponse>

    @POST("Common/TriggerMeetingFeedbackEmailSMS")
    fun sendSurveyLink(
        @Body request: SurveyLinkRequest
    ): Call<String>
}