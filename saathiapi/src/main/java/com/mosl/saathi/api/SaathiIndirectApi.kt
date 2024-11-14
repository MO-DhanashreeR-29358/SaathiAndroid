package com.mosl.saathi.api

import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.LookUpResponse
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.SetAppointmentPostReq
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

    @POST("UserApi/AutoCompleteAttende")
    fun getAttende()


    @POST("Meeting/AddUpdateMeeting")
    fun fnSetAppointment(@Body request: SetAppointmentPostReq) :Observable<ResponseBody>


}