package com.mosl.saathi.api


import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingItem
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse
import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdRequest
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdResponse
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.common.ExtraDataCommonResponse
import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.LookupRequestNew
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.AttendeeResponse
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.LookUpResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import com.mosl.saathi.api.model.search.SearchResponse
import com.mosl.saathi.api.model.saathiIndirect.prospestMeeting.ProspestMeetingSearchRequest
import com.mosl.saathi.api.model.saathiIndirect.prospestMeeting.ProspestMeetingSearchResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import com.mosl.app.core.annotation.ExcludeWrapper
import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileResponse
import com.mosl.saathi.api.model.common.ExtraDataCommonResponse
import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadRequest
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.IndirectCreateLeadRequest
import com.mosl.saathi.api.model.createlead.LeadSourceItem
import com.mosl.saathi.api.model.createlead.LeadSourceListRequest
import com.mosl.saathi.api.model.createlead.SearchRefereeModel
import com.mosl.saathi.api.model.createlead.SearchRefereeResponse
import com.mosl.saathi.api.model.createlead.SourceListItem
import com.mosl.saathi.api.model.createlead.SourceListRequest
import com.mosl.saathi.api.model.createlead.TransferLeadRequest
import com.mosl.saathi.api.model.createlead.TransferLeadResponse
import com.mosl.saathi.api.model.lead.AssociatedBrokerItem
import com.mosl.saathi.api.model.lead.BackgroundItem
import com.mosl.saathi.api.model.lead.CityItem
import com.mosl.saathi.api.model.lead.CityListRequest
import com.mosl.saathi.api.model.lead.CountryItem
import com.mosl.saathi.api.model.lead.IndirectLeadViewItem
import com.mosl.saathi.api.model.lead.IndirectUpdateLeadRequest
import com.mosl.saathi.api.model.lead.IndirectUpdateLeadResponse
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
import com.mosl.saathi.api.model.lead.request.SearchRequest
import com.mosl.saathi.api.model.leadviewactivities.AppointmentRequest
import com.mosl.saathi.api.model.leadviewactivities.AppointmentResponse
import com.mosl.saathi.api.model.leadviewactivities.MeetingRequest
import com.mosl.saathi.api.model.leadviewactivities.MeetingResponse
import com.mosl.saathi.api.model.leadviewhistory.IndirectLeadHistoryResponse
import com.mosl.saathi.api.model.leadviewhistory.IndirectLeadMappingHistoryResponse
import com.mosl.saathi.api.model.leadviewhistory.LeadHistoryRequest
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.LookUpResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import com.mosl.saathi.api.model.search.SearchResponse


interface SaathiIndirectApi {

    @POST("Dashboard/GetFocoDashFilter")
    fun fnGetFOCODashFilter(@Body request: FOCODashboardFilterpostReq):Observable<ResponseBody>

    @POST("Lead/SearchCommonMetaData")
    fun getSearchData(
        @Body request: SearchRequest
    ): Observable<ExtraDataCommonResponse<List<SearchResponse>>>

    @POST("NotificationMessage/GetNotificationMessage")
    @ExtraDataWrapper
    fun getNotifications(@Body request: NotificationRequest): Observable<ExtraDataCommonResponse<List<NotificationResponse>>>


    @POST("Master/GetAllLookup")
    fun getAllLookup(
        @Body request: LookupRequest
    ): Observable<LookUpResponse>

    @POST("UserApi/AutoCompleteAttendee")
    fun getAutoCompleteAttendee(@Body request: AutoCompleteAttendeeRequest): Observable<AttendeeResponse>
    @POST("UserApi/AutoCompleteSplAttendee")
    fun getAutoCompleteSplAttendee(@Body request: AutoCompleteAttendeeRequest): Observable<AttendeeResponse>

    @POST("Meeting/AddUpdateMeeting")
    fun setAppointmentMeeting(@Body request: RequestBody): Observable<ResponseBody>  //SetAppointmentResponse

    @POST("UserApi/GetClientCodesAuto")
    fun fnSearchProspectMeetingClient(@Body request: ProspestMeetingSearchRequest):Observable<ProspestMeetingSearchResponse>

    @POST("Lead/GetAddressByLeadId")
    fun getAddressByLeadId(@Body request: AddressByLeadIdRequest): Observable<AddressByLeadIdResponse>

    @POST("Meeting/AddUpdateMeeting")
    fun postAddUpdateMeeting(@Body request: RequestBody): Observable<AddUpdateMeetingItem>



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

    @POST("Lead/GetExistingBrokerList")
    fun getExistingBrokerList( @Body request: StageRequest): Observable<SaathiCommonResponse<List<AssociatedBrokerItem>>>


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
        @Body request: IndirectCreateLeadRequest
    ): Observable<SaathiCommonResponse<CreateLeadResponse>>


    @POST("Common/GetRefereeCode")
    fun getRefereeCode(
        @Body request: SearchRefereeModel
    ): Observable<SearchRefereeResponse>

    @POST("Lead/UpdateLead")
    fun updateLead(
        @Body request: IndirectUpdateLeadRequest
    ): Observable<IndirectUpdateLeadResponse>

    @POST("Lead/GetLeadView")
    fun getLeadView(
        @Body request: LeadViewRequest
    ): Observable<SaathiCommonResponse<IndirectLeadViewItem>>

    @POST("Meeting/GetAppointment")
    @ExtraDataWrapper
    fun getAppointments(@Body request: AppointmentRequest): Observable<ExtraDataCommonResponse<List<AppointmentResponse>>>

    @POST("Meeting/GetMeeting")
    @ExtraDataWrapper
    fun getMeetings(@Body request: MeetingRequest): Observable<ExtraDataCommonResponse<List<MeetingResponse>>>

    @ExcludeWrapper
    @POST("Lead/GetLeadHistoryByLeadId")
    fun getLeadHistoryByLeadId(
        @Body request: LeadHistoryRequest
    ): Observable<ExtraDataCommonResponse<List<IndirectLeadHistoryResponse>>>

    @ExcludeWrapper
    @POST("Lead/GetLeadProductHistoryByLeadId")
    fun getLeadProductHistoryByLeadId(
        @Body request: LeadHistoryRequest
    ): Observable<ExtraDataCommonResponse<List<IndirectLeadMappingHistoryResponse>>>
}