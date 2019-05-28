package com.mosl.saathi.saathiapi

import com.mosl.saathi.saathiapi.model.lead.CountryListResponse
import com.mosl.saathi.saathiapi.model.lead.StateProvinceListResponse
import com.mosl.saathi.saathiapi.model.lead.StateProvinceRequest
import com.mosl.saathi.saathiapi.model.reports.report.AssignedLeadReportRequest
import com.mosl.saathi.saathiapi.model.reports.report.AssignedLeadReportResponse
import com.mosl.saathi.saathiapi.model.reports.report.LeadReportRequest
import com.mosl.saathi.saathiapi.model.reports.report.LeadReportResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface SaathiApi {

  //========================= My Reports ==============================

  @POST("UserApi/GetUserBusinessProfileBranchByHierarchy")
  fun postAssignedLeadReport(
    @Body request: AssignedLeadReportRequest
  ): Observable<AssignedLeadReportResponse>

  @POST("Report/LoadReportViewDataAsync")
  fun postLeadReport(
    @Body request: LeadReportRequest
  ): Observable<LeadReportResponse>

  //========================== Lead View Details ==========================

  @POST("Country/GetCountryList")
  fun postCountryList(): Observable<CountryListResponse>

  @POST("StateProvince/GetStateProvinceList")
  fun postStateProvinceList(
    @Body request: StateProvinceRequest
  ): Observable<StateProvinceListResponse>























}