package com.mosl.saathi.api

import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface SaathiIndirectApi {

    @POST("Dashboard/GetFocoDashFilter")
    fun fnGetFOCODashFilter(@Body request: FOCODashboardFilterpostReq):Observable<ResponseBody>
}