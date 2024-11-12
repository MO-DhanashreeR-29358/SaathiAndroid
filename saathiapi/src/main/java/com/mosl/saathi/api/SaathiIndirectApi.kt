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
import com.mosl.saathi.api.model.lead.BackgroundItem
import com.mosl.saathi.api.model.lead.CityItem
import com.mosl.saathi.api.model.lead.CityListRequest
import com.mosl.saathi.api.model.lead.CountryItem
import com.mosl.saathi.api.model.lead.IndirectLeadViewItem
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.saathi.api.model.lead.LeadViewRequest
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

import com.mosl.saathi.api.model.lead.request.LookupRequestTemp
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.LookUpResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface SaathiIndirectApi {

   /* @POST("Dashboard/GetFocoDashFilter")
    fun fnGetFOCODashFilter(@Body request: FOCODashboardFilterpostReq):Observable<ResponseBody>*/

    @POST("Master/GetAllLookup")
    fun getAllLookup(
        @Body request: LookupRequest
    ): Observable<LookUpResponse>

    @POST("Master/GetAllLookup")
    fun getAllLookupTemp(
        @Body request: LookupRequestTemp
    ): Observable<LookUpResponse>


    @POST("Source/GetSourceList")
    fun getSourceList(
        @Body request: StageRequest
    ): Observable<SaathiCommonResponse<List<SourceListItem>>>

    @POST("Stage/GetStageList")
    fun getStageList(
        @Body request: StageRequest
    ): Observable<SaathiCommonResponse<List<StageItem>>>

    @POST("LeadSource/GetLeadSourceList")
    fun getLeadSourceList(
        @Body request: LeadSourceListRequest
    ): Observable<SaathiCommonResponse<List<LeadSourceItem>>>

    @POST("LeadStatus/GetStatusList")
    fun getStatusList(
        @Body request: StatusRequest
    ): Observable<SaathiCommonResponse<List<StatusItem>>>

    @POST("Country/GetCountryList")
    fun getCountries(): Observable<SaathiCommonResponse<List<CountryItem>>>

    @POST("Lead/GetBackgroundList")
    fun getBackground( @Body request: StageRequest): Observable<SaathiCommonResponse<List<BackgroundItem>>>


    @POST("StateProvince/GetStateProvinceList")
    fun getStateProvinces(
        @Body request: StateProvinceRequest
    ): Observable<SaathiCommonResponse<List<StateProvinceItem>>>

    @POST("City/GetCityList")
    fun getCityList(
        @Body request: CityListRequest
    ): Observable<SaathiCommonResponse<List<CityItem>>>

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

    @POST("Lead/GetLeadView")
    fun getLeadView(
        @Body request: LeadViewRequest
    ): Observable<IndirectLeadViewItem>
}