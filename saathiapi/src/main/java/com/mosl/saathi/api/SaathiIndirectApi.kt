package com.mosl.saathi.api


import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingItem
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdRequest
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdResponse
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.lead.LookupRequestNew
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.AttendeeResponse
import com.mosl.saathi.api.model.saathiIndirect.prospestMeeting.ProspestMeetingSearchRequest
import com.mosl.saathi.api.model.saathiIndirect.prospestMeeting.ProspestMeetingSearchResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import com.mosl.app.core.annotation.ExcludeWrapper
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingNewResposne
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
import com.mosl.saathi.api.model.createmeeting.MeetingByIdItem
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest
import com.mosl.saathi.api.model.dashboard.target.FocoPeriodResponse
import com.mosl.saathi.api.model.dashboard.target.FocoPeriodResponseObject
import com.mosl.saathi.api.model.dashboard.target.FocoPeriodResponseObjectMain
import com.mosl.saathi.api.model.dashboard.target.TargetItemResponse
import com.mosl.saathi.api.model.dashboard.targetnew.TargetRequest
import com.mosl.saathi.api.model.documents.DeleteDocumentRequest
import com.mosl.saathi.api.model.documents.DocumentsRequest
import com.mosl.saathi.api.model.documents.LeadDocumentsItem
import com.mosl.saathi.api.model.documents.UpdateDocumentsRequest
import com.mosl.saathi.api.model.documents.UploadDocumentItem
import com.mosl.saathi.api.model.documents.UploadDocumentsRequest
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
import com.mosl.saathi.api.model.lead.request.SearchRequest
import com.mosl.saathi.api.model.leadviewactivities.AppointmentRequest
import com.mosl.saathi.api.model.leadviewactivities.AppointmentResponse
import com.mosl.saathi.api.model.leadviewactivities.DeleteAppointmentRequest
import com.mosl.saathi.api.model.leadviewactivities.MeetingRequest
import com.mosl.saathi.api.model.leadviewactivities.MeetingResponse
import com.mosl.saathi.api.model.leadviewhistory.IndirectLeadHistoryResponse
import com.mosl.saathi.api.model.leadviewhistory.IndirectLeadMappingHistoryResponse
import com.mosl.saathi.api.model.leadviewhistory.LeadHistoryRequest
import com.mosl.saathi.api.model.logout.LogoutRequest
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.LookUpResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardCncAdminFilterPostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardDetailsGraphCommonDataResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardFilterPostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDetailsTablePostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectIFADashboardFilterResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectIFAWidgetCommonResponse
import com.mosl.saathi.api.model.search.SearchResponse
import retrofit2.http.Path
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectExtraDataCommonResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.MyLeadDetailsRequestPost
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectCCDashboardFilterResponse


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
    fun setAppointmentMeeting(@Body request: RequestBody): Observable<SetAppointmentResponse>  //SetAppointmentResponse

    @POST("UserApi/GetClientCodesAuto")
    fun fnSearchProspectMeetingClient(@Body request: ProspestMeetingSearchRequest):Observable<ProspestMeetingSearchResponse>

    @POST("UserApi/GetFranchiseeCodesAuto")
    fun fnSearchProspectMeetingFRCode(@Body request: ProspestMeetingSearchRequest):Observable<ProspestMeetingSearchResponse>


    @POST("Lead/GetAddressByLeadId")
    fun getAddressByLeadId(@Body request: AddressByLeadIdRequest): Observable<AddressByLeadIdResponse>

    @POST("Meeting/AddUpdateMeeting")
    fun postAddUpdateMeeting(@Body request: RequestBody): Observable<AddUpdateMeetingNewResposne>

    @POST("Meeting/AddUpdateMeetingBDM")
    fun postBDMAddUpdateMeeting(@Body request: RequestBody): Observable<AddUpdateMeetingNewResposne>


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
    ): Observable<SaathiCommonResponse<SearchRefereeResponse>>

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

    @POST("Auth/Logout")
    fun logout(@Body logoutRequest: LogoutRequest): Observable<String>

    //===================== Documents=========================

    @POST("Lead/GetLeadDocuments")
    @ExtraDataWrapper
    fun getLeadDocuments(
        @Body request: DocumentsRequest
    ): Observable<ExtraDataCommonResponse<List<LeadDocumentsItem>>>

    @POST("Lead/UploadDocument")
    fun postUploadDocuments(
        @Body request: UploadDocumentsRequest
    ): Observable<SaathiCommonResponse<UploadDocumentItem>>

    @POST("Lead/UpdateLeadDocuments")
    fun postUpdateDocuments(
        @Body request: UpdateDocumentsRequest
    ): Observable<SaathiCommonResponse<String>>

    @POST("Lead/DeleteLeadDocuments")
    fun postDeleteDocuments(
        @Body request: DeleteDocumentRequest
    ): Observable<SaathiCommonResponse<String>>

    @POST("Meeting/{action}")
    fun deleteAppointments(
        @Path("action") action: String,
        @Body request: DeleteAppointmentRequest
    ): Observable<SaathiCommonResponse<String>>

    @POST("Meeting/GetMeetingByID")
    fun getMeetingById(@Body request: MeetingByIdRequest): Observable<SaathiCommonResponse<MeetingByIdItem>>

    //---Dashboard----------------------------------

    @POST("Dashboard/GetFocoDashFilter")
    fun getFocoPeriodFilter(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<FocoPeriodResponseObjectMain>
    @POST("Dashboard/GetFocoDashTalkTimeDetail")
    fun getTalkTimeData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>


    @POST("Dashboard/GetFocoDashTopInactiveClientDetail")
    fun getTopIncentiveData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>


    @POST("Dashboard/GetFocoDashClientReactivationDetail")
    fun getClientReactivationData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>
    @POST("Dashboard/GetFocoDashTopClientListDetail")
    fun getTopClientListData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashRevenueWidgetData")
    fun getFocoDashRevenueWidgetData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashRevenueGrowthAndDegrowthWidgetData")
    fun getFocoDashRevenueGrowthWidgetData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>


    @POST("Dashboard/GetFocoDashNoOfClientMeetingWidgetData")
    fun getFocoDashNoOfClientsWidgetData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashOnlineRevenueWidgetData")
    fun getFocoDashOnlineRevenueWidgetData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashClientReachWidgetData")
    fun getFocoDashClientReachWidgetData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashIPSalesWidgetData")
    fun getFocoDashIPSalesWidgetData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashTotalMobAppLoginWidgetData")
    fun getFocoDashTotalMobListWidgetData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashRunrateForAdvisorWidgetData")
    fun getRunrateForAdvisorData(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashRevenueHierarchy")
    fun getRevenueHierarchy(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashRevenueDetail")
    fun getRevenueDetail(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetFocoDashMeetingHierarchy")
    fun getClientMeetingHierarchy(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>
    @POST("Dashboard/GetFocoDashMeetingDetail")
    fun getClientMeetingDetail(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>
    @POST("Dashboard/GetFocoDashOnlineReenueDetail")
    fun getOnlineRevenueDetail(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>
    @POST("Dashboard/GetFocoDashClientReachDetail")
    fun getClientReachDetail(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>
    @POST("Dashboard/GetFocoIPSalesHierarchy")
    fun getIpSalesHierarchy(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>
    @POST("Dashboard/GetFocoDashIPSalesDetail")
    fun getIpSalesDetail(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>
    @POST("Dashboard/GetFocoDashTotalMobAppDetail")
    fun getTotalMobAppDetail(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>


    /*************IFADashboard Start Here************/

    @POST("Dashboard/GetIFASalesDashFilterWidgetData")
    fun getIFADashboard(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFADashboardFilterResponse>>

    @POST("Dashboard/GetIFASalesDashTodaysBirtdayAndAnnivarsaryWidgetData")
    fun getIFABirthdayAnniversary(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashFirstMeetingNotDoneWidgetData")
    fun getIFASaleshFirstMeetingNotDone(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashLeadsNotUpdatedWidgetData")
    fun getIFALeadNotUpdate(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>


    @POST("Dashboard/GetIFASalesDashInactiveIFAsWidgetData")
    fun getIFASalesDashInactive(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashTeamAppointmentWidgetData")
    fun getIFASalesDashTeamAppointment(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashNewLeadWidgetData")
    fun getIFASalesDashNewLead(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashMyBusinessSnapshotBrokingWidgetData")
    fun getIFASalesDashboBuisnessSnapshot(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashMyLeadWidgetData")
    fun getIFaSalesDashMyLead(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashMyBusinessSnapshotIPWidgetData")
    fun getIFASalesDashMyBuisnessSnapIP(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFADashRevenueIPWidgetData")
    fun getIFADashREvenueIP( @Body request: FOCODashboardFilterpostReq):Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetIFADashRevenueBrokingWidgetData")
    fun getIFADashRevenueBroking( @Body request: FOCODashboardFilterpostReq):Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetIFADashMeetingWidgetData")
    fun getIFADashMeeting( @Body request: FOCODashboardFilterpostReq):Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetIFADashIFAActivationWidgetData")
    fun getIFADashIFAACtivation( @Body request: FOCODashboardFilterpostReq):Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetIFASalesDashMeetingTargetMetNotMetGraph")
    fun getIFASalesDashMeetingTargetMetNotMetGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

    @POST("Dashboard/GetIFASalesDashMeetingTeamUniqueGraph")
    fun getIFASalesDashMeetingTeamUniqueGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   @POST("Dashboard/GetIFASalesDashMeetingMyUniqueGraph")
   fun getIFASalesDashMeetingMyUniqueGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   // Revenu Ip product sise sales
   @POST("Dashboard/GetIFASalesDashRevenueIPProductWiseSalesGraph")
   fun getIFASalesDashRevenueIPProductWiseSalesGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   @POST("Dashboard/GetIFASalesDashRevenueIPBranchWiseGraph")
   fun getIFASalesDashRevenueIPBranchWiseGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   @POST("Dashboard/GetIFASalesDashRevenueIPTargetMetNotMetGraph")
   fun getIFASalesDashRevenueIPTargetMetNotMetGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   // ifa activation product wize
   @POST("Dashboard/GetIFASalesDashIFAActivationProductWiseGraph")
   fun getIFASalesDashIFAActivationProductWiseGraph ( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   // ifa activation branch wise

   @POST("Dashboard/GetIFASalesDashIFAActivationBranchWiseGraph")
   fun getIFASalesDashIFAActivationBranchWiseGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>


    //Revenu Broking product wise sales
   @POST("Dashboard/GetIFASalesDashRevenueBrokingProductWiseSalesGraph")
   fun getIFASalesDashRevenueBrokingProductWiseSalesGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   // Revenu broking branch wise
   @POST("Dashboard/GetIFASalesDashRevenueBrokingBranchWiseGraph")
   fun getIFASalesDashRevenueBrokingBranchWiseGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

   @POST("Dashboard/GetIFASalesDashRevenueBrokingTargetMetNotMetGraph")
   fun getIFASalesDashRevenueBrokingTargetMetNotMetGraph( @Body request: FOCODashboardFilterpostReq):Observable<IndirectDashboardDetailsGraphCommonDataResponse>

    // Revenu Broking hierchy
   @POST("Dashboard/GetIFASalesDashRevenueBrokingHierarchy")
   fun getIFASalesDashRevenueBrokingHierarchy(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    // Revenu Broking details
   @POST("Dashboard/GetIFASalesDashRevenueBrokingDetail")
   fun getIFASalesDashRevenueBrokingDetail(@Body request: IndirectDetailsTablePostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>


   @POST("Dashboard/GetIFASalesDashMeetingDetail")
   fun getIFASalesDashMeetingDetail(@Body request: IndirectDetailsTablePostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>


   // ifa details
   @POST("Dashboard/GetIFASalesDashIFAActivationDetail")
   fun getIFASalesDashIFAActivationDetail(@Body request: IndirectDetailsTablePostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetIFASalesDashRevenueIPDetail")
   fun getIFASalesDashRevenueIPDetail(@Body request: IndirectDetailsTablePostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>


//meeting hierchy
    @POST("Dashboard/GetIFASalesDashMeetingHierarchy")
    fun getIFASalesDashMeetingHierarchy(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    // ifa hierchy
    @POST("Dashboard/GetIFASalesDashIFAActivationHierarchy")
    fun  GetIFASalesDashIFAActivationHierarchy (@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    //Revenu ip hierchy
   @POST("Dashboard/GetIFASalesDashRevenueIPHierarchy")
   fun getIFASalesDashRevenueIPHierarchy(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>


   // my leads details

   @POST("Dashboard/GetIFASalesDashMyLeadsDetail")
   fun getIFASalesDashMyLeadsDetail(@Body request: MyLeadDetailsRequestPost):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>


   @POST("Dashboard/GetIFASalesDashFirstMeetingNotDoneDetail")
   fun getIFASalesDashFirstMeetingNotDoneDetail(@Body request: MyLeadDetailsRequestPost):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

   @POST("Dashboard/GetIFASalesDashLeadsNotUpdatedDetail")
   fun getIFASalesDashLeadsNotUpdatedDetail(@Body request: MyLeadDetailsRequestPost):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    /*************IFADashboard End Here************/

    /********************CNC Dashboard Start here ************/

    @POST("Dashboard/GetCNCFocoDashFilter")
    fun getCNCFocoDashFilter(@Body request: IndirectDashboardCncAdminFilterPostRequest):Observable<SaathiCommonResponse<IndirectIFADashboardFilterResponse>>

    @POST("Dashboard/GetCCFiltersWidgetData")
    fun getCCDashboardFilter(@Body request: IndirectDashboardFilterPostRequest):Observable<SaathiCommonResponse<IndirectCCDashboardFilterResponse>>
    @POST("Dashboard/GetCCDashMeetingSummaryZonalHeadWidgetData")
    fun getCCDashMeetingSummary(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetCCDashMeetingReachZonalHeadSummaryWidgetData")
    fun getCCDashMeetingReachSummary(
        @Body request: FOCODashboardFilterpostReq
    ): Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetCNCFocoDashRevenueWidgetData")
    fun  getCNCFocoDashRevenueWidgetData( @Body request: FOCODashboardFilterpostReq):Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetCNCFocoDashNoOfClientMeetingWidgetData")
    fun getCNCFocoDashNoOfClientMeetingWidgetData( @Body request: FOCODashboardFilterpostReq):Observable<IndirectExtraDataCommonResponse>

    @POST("Dashboard/GetCNCFocoDashMeetingDetail")
    fun getCNCFocoDashMeetingDetail(@Body request: IndirectDetailsTablePostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>

    @POST("Dashboard/GetCNCFocoDashRevenueDetail")
    fun getCNCFocoDashRevenueDetail(@Body request: IndirectDetailsTablePostRequest):Observable<SaathiCommonResponse<IndirectIFAWidgetCommonResponse>>
    /********************CNC Dashboard End here ************/



}