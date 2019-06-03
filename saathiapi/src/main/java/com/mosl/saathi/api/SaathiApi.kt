package com.mosl.saathi.api

import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
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
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse
import com.mosl.saathi.api.model.pendingforapproval.ApprovelFieldItem
import com.mosl.saathi.api.model.pendingforapproval.ApprovelFieldRequest
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistItem
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistRequest
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportItem
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportRequest
import com.mosl.saathi.api.model.reports.report.LeadReportItem
import com.mosl.saathi.api.model.reports.report.LeadReportRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface SaathiApi {

  //========================= My Reports ==============================

  @POST("UserApi/GetUserBusinessProfileBranchByHierarchy")
  fun postAssignedLeadReport(
    @Body request: AssignedLeadReportRequest
  ): Observable<List<AssignedLeadReportItem>>

  @POST("Report/LoadReportViewDataAsync")
  fun postLeadReport(
    @Body request: LeadReportRequest
  ): Observable<SaathiCommonResponse<LeadReportItem>>

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
  ): Observable<SaathiCommonResponse<LookupItem>>

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

  //===================== Notification=========================

  @POST("NotificationMessage/GetNotificationMessageMobile")
  @ExtraDataWrapper
  fun getNotifications(@Body request: NotificationRequest): Observable<List<NotificationResponse>>

  //================= Pending For Approval ====================

  @POST("ApprovalRequest/GetApprovelReqestFieldList")
  fun postApprovalFieldList(
    @Body request: ApprovelFieldRequest
  ): Observable<List<ApprovelFieldItem>>

}