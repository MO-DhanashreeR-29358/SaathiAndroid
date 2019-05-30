package com.mosl.saathi.saathiapi

import com.mosl.saathi.saathiapi.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.saathiapi.model.clientdetail.ClientDetailItem
import com.mosl.saathi.saathiapi.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.saathiapi.model.clientprofile.ClientProfileItem
import com.mosl.saathi.saathiapi.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.saathiapi.model.common.SaathiCommonResponse
import com.mosl.saathi.saathiapi.model.crosssellingproduct.CrossSellingProductItem
import com.mosl.saathi.saathiapi.model.crosssellingproduct.CrossSellingProductRequest
import com.mosl.saathi.saathiapi.model.lead.CityItem
import com.mosl.saathi.saathiapi.model.lead.CityListRequest
import com.mosl.saathi.saathiapi.model.lead.CountryItem
import com.mosl.saathi.saathiapi.model.lead.InsertLeadProductItem
import com.mosl.saathi.saathiapi.model.lead.InsertLeadProductRequest
import com.mosl.saathi.saathiapi.model.lead.LeadViewItem
import com.mosl.saathi.saathiapi.model.lead.LeadViewRequest
import com.mosl.saathi.saathiapi.model.lead.LookupItem
import com.mosl.saathi.saathiapi.model.lead.LookupRequest
import com.mosl.saathi.saathiapi.model.lead.StageItem
import com.mosl.saathi.saathiapi.model.lead.StageMobileItem
import com.mosl.saathi.saathiapi.model.lead.StageMobileRequest
import com.mosl.saathi.saathiapi.model.lead.StageRequest
import com.mosl.saathi.saathiapi.model.lead.StateProvinceItem
import com.mosl.saathi.saathiapi.model.lead.StateProvinceRequest
import com.mosl.saathi.saathiapi.model.lead.StatusItem
import com.mosl.saathi.saathiapi.model.lead.StatusRequest
import com.mosl.saathi.saathiapi.model.lead.UpdateLeadItem
import com.mosl.saathi.saathiapi.model.lead.UpdateLeadRequest
import com.mosl.saathi.saathiapi.model.pendingapproval.ApprovalSummaryCountItem
import com.mosl.saathi.saathiapi.model.pendingapproval.PendingApprovalRequest
import com.mosl.saathi.saathiapi.model.productspecialist.ProductSpecialistItem
import com.mosl.saathi.saathiapi.model.productspecialist.ProductSpecialistRequest
import com.mosl.saathi.saathiapi.model.reports.report.AssignedLeadReportItem
import com.mosl.saathi.saathiapi.model.reports.report.AssignedLeadReportRequest
import com.mosl.saathi.saathiapi.model.reports.report.LeadReportItem
import com.mosl.saathi.saathiapi.model.reports.report.LeadReportRequest
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
  ): Observable<ClientProfileItem>

  //========================== Pending Approval ====================

  @POST("ApprovalRequest/GetAprovalCoutSummary")
  fun postApprovalSummaryCount(
    @Body request: PendingApprovalRequest
  ): Observable<SaathiCommonResponse<ApprovalSummaryCountItem>>

  @POST("ApprovalRequest/GetApprovalFieldListSummaryByEmployeeCode")
  fun postApprovalSummaryByEmployee(
    @Body request: PendingApprovalRequest
  ): Observable<SaathiCommonResponse<List<Any>>>

}