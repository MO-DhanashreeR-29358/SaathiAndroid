package com.mosl.saathi.api

import com.mosl.app.core.annotation.ExcludeWrapper
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest
import com.mosl.saathi.api.model.clientprofile.ClientProfileResponse
import com.mosl.saathi.api.model.common.ExtraDataWrapper
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createlead.CreateLeadResponse
import com.mosl.saathi.api.model.createlead.LeadSourceItem
import com.mosl.saathi.api.model.createlead.LeadSourceListRequest
import com.mosl.saathi.api.model.createlead.SourceListItem
import com.mosl.saathi.api.model.createlead.SourceListRequest
import com.mosl.saathi.api.model.createlead.TransferLeadRequest
import com.mosl.saathi.api.model.createlead.TransferLeadResponse
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductRequest
import com.mosl.saathi.api.model.lead.BusinessProfile
import com.mosl.saathi.api.model.lead.CityItem
import com.mosl.saathi.api.model.lead.CityListRequest
import com.mosl.saathi.api.model.lead.ClientSummaryResponse
import com.mosl.saathi.api.model.lead.ConfigurationByStatusRequest
import com.mosl.saathi.api.model.lead.CountryItem
import com.mosl.saathi.api.model.lead.CrossSellingProduct
import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
import com.mosl.saathi.api.model.lead.LeadProductMappingItem
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.StageItem
import com.mosl.saathi.api.model.lead.StageMobileItem
import com.mosl.saathi.api.model.lead.StageMobileRequest
import com.mosl.saathi.api.model.lead.StageRequest
import com.mosl.saathi.api.model.lead.StageWiseConfiguration
import com.mosl.saathi.api.model.lead.StateProvinceItem
import com.mosl.saathi.api.model.lead.StateProvinceRequest
import com.mosl.saathi.api.model.lead.StatusItem
import com.mosl.saathi.api.model.lead.StatusRequest
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.saathi.api.model.lead.UpdateLeadResponse
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistItem
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistRequest
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportItem
import com.mosl.saathi.api.model.reports.report.AssignedLeadReportRequest
import com.mosl.saathi.api.model.reports.report.LeadReportItem
import com.mosl.saathi.api.model.reports.report.LeadReportRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

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
    @Body request: Map<String, Any>
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
  ): Observable<String>

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
    @Body request: Any
  ): Observable<SaathiCommonResponse<CreateLeadResponse>>

  //===================== Notification=========================

  @POST("NotificationMessage/GetNotificationMessageMobile")
  @ExtraDataWrapper
  fun getNotifications(@Body request: NotificationRequest): Observable<List<NotificationResponse>>

}