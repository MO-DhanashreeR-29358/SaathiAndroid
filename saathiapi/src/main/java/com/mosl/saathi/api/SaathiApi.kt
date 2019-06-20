package com.mosl.saathi.api

import com.mosl.app.core.annotation.ExcludeWrapper
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryItem
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryRequest
import com.mosl.saathi.api.model.birthdayanniversary.SendEmailRequest
import com.mosl.saathi.api.model.birthdayanniversary.SendGiftRequest
import com.mosl.saathi.api.model.calender.CalenderItem
import com.mosl.saathi.api.model.calender.CalenderRequest
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileItem
import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.SourceListItem
import com.mosl.saathi.api.model.createlead.SourceListRequest
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductItem
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductItem
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductItem
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountItem
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountRequest
import com.mosl.saathi.api.model.dashboard.client.ClientItem
import com.mosl.saathi.api.model.dashboard.client.ClientRequest
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.ClientTradedYesterdayItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataItem
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataRequest
import com.mosl.saathi.api.model.dashboard.dashboardsecondlevel.DashboardSecondLevelRequest
import com.mosl.saathi.api.model.dashboard.hierarchy.DashboardHierarchyRequest
import com.mosl.saathi.api.model.dashboard.incentive.IncentiveItem
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyItem
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyRequest
import com.mosl.saathi.api.model.dashboard.snapshot.SnapshotItem
import com.mosl.saathi.api.model.dashboard.target.TargetAchievementItem
import com.mosl.saathi.api.model.dashboard.targetteam.TargetAchievementTeamItem
import com.mosl.saathi.api.model.fcm_token.FcmTokenRequest
import com.mosl.saathi.api.model.lead.CityItem
import com.mosl.saathi.api.model.lead.CityListRequest
import com.mosl.saathi.api.model.lead.ConfigurationByStatusRequest
import com.mosl.saathi.api.model.lead.CountryItem
import com.mosl.saathi.api.model.lead.InsertLeadProductItem
import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.StageItem
import com.mosl.saathi.api.model.lead.StageMobileItem
import com.mosl.saathi.api.model.lead.StageMobileRequest
import com.mosl.saathi.api.model.lead.StageRequest
import com.mosl.saathi.api.model.lead.StateProvinceItem
import com.mosl.saathi.api.model.lead.StateProvinceRequest
import com.mosl.saathi.api.model.lead.StatusItem
import com.mosl.saathi.api.model.lead.StatusRequest
import com.mosl.saathi.api.model.lead.UpdateLeadItem
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.leadprofile.LeadProfileItem
import com.mosl.saathi.api.model.leadprofile.LeadProfileRequest
import com.mosl.saathi.api.model.leadprofile.SaveLeadProfileRequest
import com.mosl.saathi.api.model.login.LoginRequest
import com.mosl.saathi.api.model.login.LoginResponse
import com.mosl.saathi.api.model.logout.LogoutRequest
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryByEmployeeItem
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryCountItem
import com.mosl.saathi.api.model.pendingapproval.PendingApprovalRequest
import com.mosl.saathi.api.model.pendingforapproval.ApprovalFieldItem
import com.mosl.saathi.api.model.pendingforapproval.ApprovalListRequest
import com.mosl.saathi.api.model.pendingforapproval.ApprovalUserItem
import com.mosl.saathi.api.model.pendingforapproval.ApprovelRequest
import com.mosl.saathi.api.model.pendingforapproval.MultiUpdateApprovalRequest
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
import retrofit2.http.Body
import retrofit2.http.POST

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
  fun postCountryList(): Observable<List<CountryItem>>

  @POST("StateProvince/GetStateProvinceList")
  fun postStateProvinceList(
    @Body request: StateProvinceRequest
  ): Observable<SaathiCommonResponse<StateProvinceItem>>

  @POST("City/GetCityList")
  fun postCityList(
    @Body request: CityListRequest
  ): Observable<SaathiCommonResponse<CityItem>>

  @POST("Master/GetAllLookup")
  fun postAllLookup(
    @Body request: LookupRequest
  ): Observable<List<LookupItem>>

  @POST("LeadStatus/GetStatusList")
  fun postStatusList(
    @Body request: StatusRequest
  ): Observable<SaathiCommonResponse<StatusItem>>

  @POST("Lead/GetLeadView")
  fun postLeadView(
    @Body request: LeadViewRequest
  ): Observable<SaathiCommonResponse<LeadViewItem>>

  @POST("Lead/UpdateLead")
  fun postUpdateLead(
    @Body request: UpdateLeadRequest
  ): Observable<SaathiCommonResponse<UpdateLeadItem>>

  @POST("UserApi/AutoCompleteProductSpecialist")
  fun postProductSpecialist(
    @Body request: ProductSpecialistRequest
  ): Observable<SaathiCommonResponse<ProductSpecialistItem>>

  @POST("UserApi/GetUserBusinesprofile")
  fun postUserBusinessProfile(
    @Body request: UserBusinessProfileRequest
  ): Observable<SaathiCommonResponse<Any>>

  @POST("CrossSellingProductMapping/GetCrossSellingProduct")
  fun postCrossSellingProduct(
    @Body request: CrossSellingProductRequest
  ): Observable<CrossSellingProductItem>

  @POST("Lead/InsertLeadProductMapping")
  fun postInsertLeadProductMapping(
    @Body request: InsertLeadProductRequest
  ): Observable<SaathiCommonResponse<InsertLeadProductItem>>

  @POST("Stage/GetStageList")
  fun postStageList(
    @Body request: StageRequest
  ): Observable<SaathiCommonResponse<StageItem>>

  @POST("Stage/GetStageMobileSelectList")
  fun postStageMobile(
    @Body request: StageMobileRequest
  ): Observable<SaathiCommonResponse<StageMobileItem>>

  @POST("Client/GetClientViewByCommand")
  fun postClientDetail(
    @Body request: ClientDetailRequest
  ): Observable<SaathiCommonResponse<ClientDetailItem>>

  @POST("Client/SaveClientProfile")
  fun postSaveClientProfile(
    @Body request: ClientProfileRequest
  ): Observable<SaathiCommonResponse<ClientProfileItem>>

  @POST("Lead/SaveLeadCrossSellingProduct")
  fun postSaveLeadCrossSellingProduct(
    @Body request: SaveLeadCrossSellingProductRequest
  ): Observable<SaathiCommonResponse<SaveLeadCrossSellingProductItem>>

  @POST("Lead/DeleteLeadProductMapping")
  fun postDeleteLeadCrossSellingProduct(
    @Body request: DeleteLeadCrossSellingProductRequest
  ): Observable<SaathiCommonResponse<DeleteLeadCrossSellingProductItem>>

  @POST("Lead/GetTbl_StageWiseFieldConfigurationByStatus")
  fun postStageWiseConfigurationByStatus(
    @Body request: ConfigurationByStatusRequest
  ): Observable<SaathiCommonResponse<Any>>

  //===================== Create Lead=========================

  @POST("Source/GetSourceList")
  fun postSourceList(
    @Body request: SourceListRequest
  ): Observable<SaathiCommonResponse<SourceListItem>>

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
    @Body request: DashboardHierarchyRequest
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
  fun logout(@Body logoutRequest: LogoutRequest): Observable<Any>

  @POST("Auth/UpdateFCMToken")
  fun updateFCMToken(@Body fcmTokenRequest: FcmTokenRequest): Observable<Any>

  @POST("Prospect/RegisterProspectOnline")
  fun registerProspectOnline(@Body prospectRequest: ProspectRequest): Observable<SaathiCommonResponse<List<ProspectResponse>>>

  @POST("Prospect/SMT_RegisterProspectOnline")
  fun suggestMe(@Body suggestMeRequest: SuggestMeRequest): Observable<SaathiCommonResponse<ProspectResponse>>

  @POST("Common/GetAppVersion")
  @ExtraDataWrapper
  fun appVersion(@Body versionRequest: VersionRequest): Observable<List<VersionResponse>>

  @POST("Report/FetchReportTypes")
  fun fetchReportTypes(): Observable<SaathiCommonResponse<List<ReportTypeResponse>>>


}