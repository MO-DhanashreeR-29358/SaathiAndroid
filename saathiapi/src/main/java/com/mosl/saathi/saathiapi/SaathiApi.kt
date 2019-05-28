package com.mosl.saathi.saathiapi

import com.mosl.saathi.saathiapi.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.saathiapi.model.common.SaathiCommonResponse
import com.mosl.saathi.saathiapi.model.lead.CityItem
import com.mosl.saathi.saathiapi.model.lead.CityListRequest
import com.mosl.saathi.saathiapi.model.lead.CountryItem
import com.mosl.saathi.saathiapi.model.lead.LeadViewItem
import com.mosl.saathi.saathiapi.model.lead.LeadViewRequest
import com.mosl.saathi.saathiapi.model.lead.LookupItem
import com.mosl.saathi.saathiapi.model.lead.LookupRequest
import com.mosl.saathi.saathiapi.model.lead.StateProvinceItem
import com.mosl.saathi.saathiapi.model.lead.StateProvinceRequest
import com.mosl.saathi.saathiapi.model.lead.StatusItem
import com.mosl.saathi.saathiapi.model.lead.StatusRequest
import com.mosl.saathi.saathiapi.model.lead.UpdateLeadItem
import com.mosl.saathi.saathiapi.model.lead.UpdateLeadRequest
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
  ): Observable<SaathiCommonResponse<AssignedLeadReportItem>>

  @POST("Report/LoadReportViewDataAsync")
  fun postLeadReport(
    @Body request: LeadReportRequest
  ): Observable<SaathiCommonResponse<LeadReportItem>>

  //========================== Lead View Details ==========================

  @POST("Country/GetCountryList")
  fun postCountryList(): Observable<SaathiCommonResponse<CountryItem>>

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

}