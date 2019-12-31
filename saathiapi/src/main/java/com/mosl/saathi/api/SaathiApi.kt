package com.mosl.saathi.api

import com.mosl.app.core.annotation.ExcludeWrapper
import com.mosl.saathi.api.ApiEndpoints.LoginEndPoint
import com.mosl.saathi.api.model.SearchResponse
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingItem
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.api.model.aubankmeeting.*
import com.mosl.saathi.api.model.aubankmeetingreport.MeetingReportRequest
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryItem
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryRequest
import com.mosl.saathi.api.model.birthdayanniversary.SendEmailRequest
import com.mosl.saathi.api.model.birthdayanniversary.SendGiftRequest
import com.mosl.saathi.api.model.calender.CalenderItem
import com.mosl.saathi.api.model.calender.CalenderRequest
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileResponse
import com.mosl.saathi.api.model.clientviewhistory.ClientHistoryRequest
import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.*
import com.mosl.saathi.api.model.createmeeting.*
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountItem
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountRequest
import com.mosl.saathi.api.model.dashboard.ausfbLead.*
import com.mosl.saathi.api.model.dashboard.client.ClientItem
import com.mosl.saathi.api.model.dashboard.client.ClientRequest
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.ClientTradedYesterdayItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataRequest
import com.mosl.saathi.api.model.dashboard.dashboardsecondlevel.DashboardSecondLevelRequest
import com.mosl.saathi.api.model.dashboard.incentive.IncentiveItem
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyItem
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyRequest
import com.mosl.saathi.api.model.dashboard.snapshot.SnapshotItem
import com.mosl.saathi.api.model.dashboard.target.TargetAchievementItem
import com.mosl.saathi.api.model.dashboard.targetteam.TargetAchievementTeamItem
import com.mosl.saathi.api.model.documents.*
import com.mosl.saathi.api.model.fcm_token.FcmTokenRequest
import com.mosl.saathi.api.model.fcm_token.FcmTokenResponse
import com.mosl.saathi.api.model.lead.*
import com.mosl.saathi.api.model.lead.LeadProductMappingItem
import com.mosl.saathi.api.model.lead.request.SearchRequest
import com.mosl.saathi.api.model.lead.request.UpdateLeadMappingRequest
import com.mosl.saathi.api.model.leadinteraction.LeadInteractionRequest
import com.mosl.saathi.api.model.leadprofile.LeadProfileItem
import com.mosl.saathi.api.model.leadprofile.LeadProfileRequest
import com.mosl.saathi.api.model.leadprofile.SaveLeadProfileRequest
import com.mosl.saathi.api.model.leadviewactivities.*
import com.mosl.saathi.api.model.leadviewhistory.LeadHistoryRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import com.mosl.saathi.api.model.leadviewmain.RegisterProspectOnlineRequest
import com.mosl.saathi.api.model.leadviewmain.RegisterProspectOnlineResponse
import com.mosl.saathi.api.model.location.LocationDataRequest
import com.mosl.saathi.api.model.location.LocationDataResponse
import com.mosl.saathi.api.model.login.LoginRequest
import com.mosl.saathi.api.model.login.LoginResponse
import com.mosl.saathi.api.model.logout.LogoutRequest
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryByEmployeeItem
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryCountItem
import com.mosl.saathi.api.model.pendingapproval.PendingApprovalRequest
import com.mosl.saathi.api.model.pendingforapproval.*
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistItem
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistRequest
import com.mosl.saathi.api.model.prospect.ProspectRequest
import com.mosl.saathi.api.model.prospect.ProspectResponse
import com.mosl.saathi.api.model.prospect.SuggestMeRequest
import com.mosl.saathi.api.model.query.QueryItem
import com.mosl.saathi.api.model.query.QueryRequest
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportItem
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportRequest
import com.mosl.saathi.api.model.reports.report.LeadReportRequest
import com.mosl.saathi.api.model.splash.ReportTypeResponse
import com.mosl.saathi.api.model.splash.VersionRequest
import com.mosl.saathi.api.model.splash.VersionResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface SaathiApi {

  //========================= My Reports ==============================

  @POST("UserApi/GetUserBusinessProfileBranchByHierarchy")
  fun postAssignedLeadReport(
    @Body request: AssignedLeadReportRequest
  ): Observable<List<AssignedLeadReportItem>>

  @ExcludeWrapper
  @POST("Report/LoadReportViewDataAsync")
  fun postLeadReport(
    @Body request: LeadReportRequest
  ): Observable<String>

  //========================== Lead View Details ==========================

  @POST("Country/GetCountryList")
  fun getCountries(): Observable<List<CountryItem>>

  @POST("StateProvince/GetStateProvinceList")
  fun getStateProvinces(
    @Body request: StateProvinceRequest
  ): Observable<List<StateProvinceItem>>

  @POST("City/GetCityList")
  fun getCityList(
    @Body request: CityListRequest
  ): Observable<List<CityItem>>

  @POST("Master/GetAllLookup")
  fun getAllLookup(
    @Body request: LookupRequest
  ): Observable<List<LookupItem>>

  @POST("LeadStatus/GetStatusList")
  fun getStatusList(
    @Body request: StatusRequest
  ): Observable<List<StatusItem>>

  @POST("Lead/GetLeadView")
  fun getLeadView(
    @Body request: LeadViewRequest
  ): Observable<LeadViewItem>

  @POST("Lead/UpdateLead")
  fun updateLead(
    @Body request: UpdateLeadRequest
  ): Observable<UpdateLeadResponse>

  @POST("Lead/SearchCommonMetaData")
  @ExtraDataWrapper
  fun getSearchData(
    @Body request: SearchRequest
  ): Observable<List<SearchResponse>>

  @POST("UserApi/AutoCompleteProductSpecialist")
  fun getProductSpecialist(
    @Body request: ProductSpecialistRequest
  ): Observable<List<ProductSpecialistItem>>

  @POST("UserApi/GetUserBusinesprofile")
  fun getUserBusinessProfile(
    @Body request: UserBusinessProfileRequest
  ): Observable<List<BusinessProfile>>

  @POST("CrossSellingProductMapping/GetCrossSellingProduct")
  fun getCrossSellingProduct(
    @Body request: CrossSellingProductRequest
  ): Observable<List<CrossSellingProduct>>

  @POST("Lead/InsertLeadProductMapping")
  fun insertLeadProductMapping(
    @Body request: InsertLeadProductRequest
  ): Observable<List<LeadProductMappingItem>>

  @POST("Stage/GetStageList")
  fun getStageList(
    @Body request: StageRequest
  ): Observable<List<StageItem>>

  @POST("Stage/GetStageMobileSelectList")
  fun getStageMobileList(
    @Body request: StageMobileRequest
  ): Observable<List<StageMobileItem>>

  @POST("Client/GetClientViewByCommand")
  fun getClientDetail(
    @Body request: ClientDetailRequest
  ): Observable<ClientDetailItem>

  @POST("Client/SaveClientProfile")
  fun saveClientProfile(
    @Body request: ClientProfileRequest
  ): Observable<ClientProfileResponse>

  @POST("Lead/SaveLeadCrossSellingProduct")
  fun saveLeadCrossSellingProduct(
    @Body request: SaveLeadCrossSellingProductRequest
  ): Observable<Any>

  @POST("Lead/DeleteLeadProductMapping")
  fun deleteLeadCrossSellingProduct(
    @Body request: DeleteLeadCrossSellingProductRequest
  ): Observable<Any>

  @POST("Lead/GetTbl_StageWiseFieldConfigurationByStatus")
  fun getStageWiseConfigurationByStatus(
    @Body request: ConfigurationByStatusRequest
  ): Observable<List<StageWiseConfiguration>>

  @POST("Lead/UpdateLeadProductMapping")
  fun updateLeadProductMapping(
    @Body request: UpdateLeadMappingRequest
  ): Observable<List<LeadProductMappingItem>>

  @GET
  @ExcludeWrapper
  fun getClientSummary(
    @Url url: String,
    @Query("Userid") userId: String,
    @Query("ClientUCID") ucid: String
  ): Observable<ClientSummaryResponse>

  @GET
  @ExcludeWrapper
  fun getClientGainLossSummary(
    @Url url: String,
    @Query("Userid") userId: String,
    @Query("ClientUCID") ucid: String
  ): Observable<ClientGainLossSummaryResponse>

  //===================== Create Lead=========================

  @POST("Source/GetSourceList")
  fun getSourceList(
    @Body request: SourceListRequest
  ): Observable<List<SourceListItem>>

  @POST("LeadSource/GetLeadSourceList")
  fun getLeadSourceList(
    @Body request: LeadSourceListRequest
  ): Observable<List<LeadSourceItem>>

  @POST("Lead/TransferLeadCaughtInC2")
  fun transferLead(
    @Body request: TransferLeadRequest
  ): Observable<SaathiCommonResponse<TransferLeadResponse>>

  @POST("Lead/AddUpdateLead")
  fun addUpdateLead(
    @Body request: CreateLeadRequest
  ): Observable<SaathiCommonResponse<CreateLeadResponse>>

  // ==================== Query ===========================

  @POST("Common/RequetQuery")
  fun postQuery(
    @Body request: QueryRequest
  ): Observable<QueryItem>

  //========================== Pending Approval ====================

  @POST("ApprovalRequest/GetAprovalCoutSummary")
  fun postApprovalSummaryCount(
    @Body request: PendingApprovalRequest
  ): Observable<ApprovalSummaryCountItem>

  @POST("ApprovalRequest/GetApprovalFieldListSummaryByEmployeeCode")
  fun postApprovalSummaryByEmployee(
    @Body request: PendingApprovalRequest
  ): Observable<List<ApprovalSummaryByEmployeeItem>>

  //======================= Birthday Anniversary ====================

  @POST("Client/GetBirthdayAnniversary")
  fun postBirthdayAnniversary(
    @Body request: BirthdayAnniversaryRequest
  ): Observable<BirthdayAnniversaryItem>

  @POST("Client/SendGiftRequest")
  fun postSendGift(
    @Body request: SendGiftRequest
  ): Observable<String>

  @POST("Client/GetMailClientId")
  fun postSendEmail(
    @Body request: SendEmailRequest
  ): Observable<String>

  //===================== Notification=========================

  @POST("NotificationMessage/GetNotificationMessageMobile")
  @ExtraDataWrapper
  fun getNotifications(@Body request: NotificationRequest): Observable<List<NotificationResponse>>

  //===================== Lead View Activities=========================

  @POST("Meeting/GetMeeting")
  @ExtraDataWrapper
  fun getMeetings(@Body request: MeetingRequest): Observable<List<MeetingResponse>>

  @POST("Meeting/GetAppointment")
  @ExtraDataWrapper
  fun getAppointments(@Body request: AppointmentRequest): Observable<List<AppointmentResponse>>

  @POST("Meeting/{action}")
  fun deleteAppointments(
    @Path("action") action: String,
    @Body request: DeleteAppointmentRequest
  ): Observable<String>

  @POST("Lead/GetAddressByLeadId")
  fun getAddressByLeadId(@Body request: AddressByLeadIdRequest): Observable<AddressByLeadIdResponse>

  @POST("UserApi/AutoCompleteAttendee")
  fun getAutoCompleteAttendee(@Body request: AutoCompleteAttendeeRequest): Observable<List<AutoCompleteAttendeeItem>>

  @POST("UserApi/AutoCompleteSplAttendee")
  fun getAutoCompleteSplAttendee(@Body request: AutoCompleteAttendeeRequest): Observable<List<AutoCompleteAttendeeItem>>

  @POST("Meeting/GetMeetingByID")
  fun getMeetingById(@Body request: MeetingByIdRequest): Observable<MeetingByIdItem>

  //================================Add Update Meeting======================================

  @POST("Meeting/AddUpdateMeeting")
  fun postAddUpdateMeeting(@Body request: RequestBody): Observable<AddUpdateMeetingItem>

  @POST("Meeting/AddUpdateMeeting")
  fun setAppointmentMeeting(@Body request: RequestBody): Observable<SetAppointmentResponse>
  //===================== Lead View Main=========================

  @POST("Prospect/SMT_RegisterProspectOnline/")
  fun getRegisterProspectOnline(@Body request: RegisterProspectOnlineRequest): Observable<RegisterProspectOnlineResponse>

  //==================== Lead Profile ===========================
  @POST("Lead/GetLeadProfileParameters")
  fun getLeadProfile(@Body request: LeadProfileRequest): Observable<List<LeadProfileItem>>

  @POST("Lead/SaveLeadProfileParameter")
  fun postLeadProfile(
    @Body request: List<SaveLeadProfileRequest>
  ): Observable<String>

  //================= Pending For Approval ====================

  @POST("ApprovalRequest/GetApprovelReqestFieldList")
  fun getApprovalFieldList(
    @Body request: ApprovelRequest
  ): Observable<List<ApprovalFieldItem>>

  @POST("ApprovalRequest/GetApprovelReqestUserList")
  fun getApprovalUserList(
    @Body request: ApprovelRequest
  ): Observable<List<ApprovalUserItem>>

  @ExcludeWrapper
  @POST("ApprovalRequest/ApprovalRequestList")
  fun getApprovalRequestList(
    @Body request: ApprovalListRequest
  ): Observable<String>

  @POST("ApprovalRequest/MultiUpdateApprovalRequest")
  fun postMultiUpdateApproval(
    @Body request: MultiUpdateApprovalRequest
  ): Observable<String>

  //====================== Dashboard =====================

  @POST("UserApi/GetBankCode")
  fun getBankFilter(): Observable<List<BankCodeResponse>>

  @POST("UserApi/GetBranchCodeByBank")
  fun getBranchFilter(@Body request: BranchCodeRequest): Observable<List<BranchCodeResponse>>

  @POST("UserApi/GetSubBranchCodeByBank")
  fun getSubBranchFilter(@Body request: SubBranchCodeRequest): Observable<List<SubBranchCodeResponse>>

  @POST("Dashboard/LoadWidgetAsync")
  fun getClients(
    @Body request: ClientRequest
  ): Observable<ClientItem>

  @ExcludeWrapper
  @POST("Dashboard/LoadWidgetAsync")
  fun getDashboardData(
    @Body request: ClientRequest
  ): Observable<String>


    @POST("Dashboard/LoadWidgetAsync")
    fun getAusfbLeadWidgetData(
        @Body request: AusfbLeadRequest
    ): Observable<AusfbLeadResponse>

  @POST("Dashboard/LoadWidgetAsync")
  fun getSnapshot(
    @Body request: ClientRequest
  ): Observable<SnapshotItem>

  @POST("Dashboard/LoadWidgetAsync")
  fun getTargetAchievement(
    @Body request: ClientRequest
  ): Observable<TargetAchievementItem>

  @POST("Dashboard/LoadWidgetAsync")
  fun getTargetAchievementTeam(
    @Body request: ClientRequest
  ): Observable<TargetAchievementTeamItem>

  @POST("Dashboard/LoadWidgetAsync")
  fun getClientsTradedYesterday(
    @Body request: ClientRequest
  ): Observable<ClientTradedYesterdayItem>

  @ExcludeWrapper
  @POST("Dashboard/LoaddetailsWidgeAsync")
  fun getDashboardSecondLevel(
    @Body request: DashboardSecondLevelRequest
  ): Observable<String>

  @ExcludeWrapper
  @POST("Dashboard/LoaddetailsWidgeAsync")
  fun getDashboardHierarchy(
    @Body request: DashboardSecondLevelRequest
  ): Observable<String>

  @POST("Dashboard/GetIncentivePolicy")
  fun getIncentivePolicy(
    @Body request: IncentivePolicyRequest
  ): Observable<IncentivePolicyItem>

  @POST("Dashboard/GetDashboardWithWidgetsWithGroup/")
  fun getDashboardMetaData(
    @Body request: DashboardMetaDataRequest
  ): Observable<List<DashboardMetaDataItem>>

  @POST("Dashboard/LoadWidgetAsync")
  fun getActionableCount(
    @Body request: ActionableCountRequest
  ): Observable<ActionableCountItem>

  @POST("Dashboard/LoadWidgetAsync")
  fun getIncentive(
    @Body request: ClientRequest
  ): Observable<IncentiveItem>

  //================== Calender ========================

  @POST("Meeting/GetMeetingByDate")
  fun postCalenderByDate(
    @Body request: CalenderRequest
  ): Observable<List<CalenderItem>?>

  @POST("Auth/Login")
  fun login(@Body loginRequest: LoginRequest): Observable<LoginResponse>

  @POST("Auth/Logout")
  fun logout(@Body logoutRequest: LogoutRequest): Observable<String>

  @POST("Auth/UpdateFCMToken")
  fun updateFCMToken(@Body fcmTokenRequest: FcmTokenRequest): Observable<FcmTokenResponse>

  @POST("Prospect/RegisterProspectOnline")
  fun registerProspectOnline(@Body prospectRequest: ProspectRequest): Observable<SaathiCommonResponse<List<ProspectResponse>>>

  @POST("Prospect/SMT_RegisterProspectOnline")
  fun suggestMe(@Body suggestMeRequest: SuggestMeRequest): Observable<SaathiCommonResponse<ProspectResponse>>

  @POST("Common/GetAppVersion")
  @ExtraDataWrapper
  fun appVersion(@Body versionRequest: VersionRequest): Observable<List<VersionResponse>>

  @POST("Report/FetchReportTypes")
  fun fetchReportTypes(): Observable<SaathiCommonResponse<List<ReportTypeResponse>>>

  //===================== Documents=========================

  @POST("Lead/GetLeadDocuments")
  @ExtraDataWrapper
  fun getLeadDocuments(
    @Body request: DocumentsRequest
  ): Observable<List<LeadDocumentsItem>>

  @POST("Lead/UploadDocument")
  fun postUploadDocuments(
    @Body request: UploadDocumentsRequest
  ): Observable<UploadDocumentItem>

  @POST("Lead/UpdateLeadDocuments")
  fun postUpdateDocuments(
    @Body request: UpdateDocumentsRequest
  ): Observable<String>

  @POST("Lead/DeleteLeadDocuments")
  fun postDeleteDocuments(
    @Body request: DeleteDocumentRequest
  ): Observable<String>

  // ================== Lead View History ========================

  @ExcludeWrapper
  @POST("Lead/GetLeadHistoryByLeadId")
  fun getLeadHistoryByLeadId(
    @Body request: LeadHistoryRequest
  ): Observable<String>

  @ExcludeWrapper
  @POST("Lead/GetLeadProductHistoryByLeadId")
  fun getLeadProductHistoryByLeadId(
    @Body request: LeadHistoryRequest
  ): Observable<String>

  @ExcludeWrapper
  @POST("Client/GetCurrentMapping")
  fun getCurrentMapping(
    @Body request: ClientHistoryRequest
  ): Observable<String>

  @ExcludeWrapper
  @POST("Client/GetMappingHistory")
  fun getMappingHistory(
    @Body request: ClientHistoryRequest
  ): Observable<String>

  // ==========================Lead Interaction===================

  @ExtraDataWrapper
  @POST("TalismaInteractionRequest/TalismaRequestGet")
  fun getInteraction(@Body request: LeadInteractionRequest): Observable<String>

  @ExtraDataWrapper
  @POST("Client/GetClientBirthdayAnniversaryWishLog")
  fun getBirthdayAnniversaryEvent(@Body request: LeadInteractionRequest): Observable<String>

  @ExtraDataWrapper
  @POST("Event/GetEventSeminarAttendedList")
  fun getSeminarEvent(@Body request: LeadInteractionRequest): Observable<String>

  @POST("Client/GetClientImage")
  fun getClientImage(
    @Body request: ClientImageRequest
  ): Observable<SaathiCommonResponse<String>>

  //===================== Update meeting =========================

  @POST("Meeting/GetMeetingByID")
  fun postMeetingById(@Body request: MeetingByIdRequest): Observable<MeetingByIdItem>

  @POST("Meeting/AddUpdateMeeting")
  fun saveUpdateMeeting(@Body request: RequestBody): Observable<Any>

  //=======================AU Bank Meeting==========================
  @POST("BankMeeting/GetBankList")
  fun getBankList(@Body request: BankRequest): Observable<List<BankListResponse>>

  @POST("BankMeeting/GetBankBranchList")
  fun getBankBranchList(@Body request: BankRequest): Observable<List<BranchListResponse>>

  @POST("Master/GetAllLookupMaster")
  fun getAllLookupMaster(
    @Body request: LookupRequest
  ): Observable<List<MeetingTypeResponse>>

  @POST("BankMeeting/AutoCompleteBankBranchSubBroker")
  fun getSubBroker(
    @Body request: SubBrokerRequest
  ): Observable<List<SubBrokerResponse>>

  @POST("BankMeeting/AutoCompleteBankEmployee")
  fun getBankEmployee(
    @Body request: BankEmployeeRequest
  ): Observable<List<BankEmployeeResponse>>

  @POST("BankMeeting/InserBankMeeting")
  fun inserBankMeeting(
    @Body request: MeetingSaveRequest
  ): Observable<String>

  @POST("BankMeeting/GetBankMeetingReportForMobile")
  fun postBankMeetingReport(
    @Body request: MeetingReportRequest
  ): Observable<String>

  @ExcludeWrapper
  @POST("GeoLocation/SaveGeoLocation")
  fun saveGeoLocation(
    @Body request: List<LocationDataRequest>
  ): Observable<LocationDataResponse>

  /*==================================================*/
  @POST("Auth/$LoginEndPoint")
  fun login(
    @Header("EmployeeCode") employeeCode: String, @Body loginRequest: LoginRequest
  ): Observable<LoginResponse>

  @POST("Common/${ApiEndpoints.AppVersionEndPoint}")
  @ExtraDataWrapper
  fun appVersion(
    @Header(
        "macaddress"
    ) macAddress: String, @Body versionRequest: VersionRequest
  ): Observable<List<VersionResponse>>

}