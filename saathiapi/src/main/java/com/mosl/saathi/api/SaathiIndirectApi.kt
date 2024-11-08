package com.mosl.saathi.api

import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileResponse
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadRequest
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.LeadSourceItem
import com.mosl.saathi.api.model.createlead.LeadSourceListRequest
import com.mosl.saathi.api.model.createlead.SearchRefereeModel
import com.mosl.saathi.api.model.createlead.SearchRefereeResponse
import com.mosl.saathi.api.model.createlead.SourceListItem
import com.mosl.saathi.api.model.createlead.SourceListRequest
import com.mosl.saathi.api.model.createlead.TransferLeadRequest
import com.mosl.saathi.api.model.createlead.TransferLeadResponse
import com.mosl.saathi.api.model.lead.CityItem
import com.mosl.saathi.api.model.lead.CityListRequest
import com.mosl.saathi.api.model.lead.CountryItem
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.StageItem
import com.mosl.saathi.api.model.lead.StageRequest
import com.mosl.saathi.api.model.lead.StateProvinceItem
import com.mosl.saathi.api.model.lead.StateProvinceRequest
import com.mosl.saathi.api.model.lead.StatusItem
import com.mosl.saathi.api.model.lead.StatusRequest
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

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

    @POST("Master/GetAllLookup")
    fun getAllLookup(
        @Body request: LookupRequest
    ): Observable<List<LookupItem>>

    @POST("Source/GetSourceList")
    fun getSourceList(
        @Body request: SourceListRequest
    ): Observable<List<SourceListItem>>

    @POST("Stage/GetStageList")
    fun getStageList(
        @Body request: StageRequest
    ): Observable<List<StageItem>>

    @POST("LeadSource/GetLeadSourceList")
    fun getLeadSourceList(
        @Body request: LeadSourceListRequest
    ): Observable<List<LeadSourceItem>>

    @POST("LeadStatus/GetStatusList")
    fun getStatusList(
        @Body request: StatusRequest
    ): Observable<List<StatusItem>>

    @POST("Country/GetCountryList")
    fun getCountries(): Observable<List<CountryItem>>

    @POST("Client/SaveClientProfile")
    fun saveClientProfile(
        @Body request: ClientProfileRequest
    ): Observable<ClientProfileResponse>

    @POST("StateProvince/GetStateProvinceList")
    fun getStateProvinces(
        @Body request: StateProvinceRequest
    ): Observable<List<StateProvinceItem>>

    @POST("City/GetCityList")
    fun getCityList(
        @Body request: CityListRequest
    ): Observable<List<CityItem>>

    @POST("Lead/AddUpdateLead")
    fun addUpdateLead(
        @Body request: CreateLeadRequest
    ): Observable<SaathiCommonResponse<CreateLeadResponse>>

    @POST("Lead/TransferLeadCaughtInC2")
    fun transferLead(
        @Body request: TransferLeadRequest
    ): Observable<SaathiCommonResponse<TransferLeadResponse>>

    @POST("Common/GetRefereeCode")
    fun getRefereeCode(
        @Body request: SearchRefereeModel
    ): Observable<SearchRefereeResponse>

    @POST("Lead/UpdateLead")
    fun updateLead(
        @Body request: UpdateLeadRequest
    ): Observable<UpdateLeadResponse>

}