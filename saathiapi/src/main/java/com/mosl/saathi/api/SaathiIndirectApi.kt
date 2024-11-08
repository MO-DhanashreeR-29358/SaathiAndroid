package com.mosl.saathi.api

import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.request.LookupRequestTemp
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.LookUpResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface SaathiIndirectApi {

    @POST("Dashboard/GetFocoDashFilter")
    fun fnGetFOCODashFilter(@Body request: FOCODashboardFilterpostReq):Observable<ResponseBody>

    @POST("Master/GetAllLookup")
    fun getAllLookup(
        @Body request: LookupRequest
    ): Observable<LookUpResponse>

    @POST("Master/GetAllLookup")
    fun getAllLookupTemp(
        @Body request: LookupRequestTemp
    ): Observable<LookUpResponse>

}