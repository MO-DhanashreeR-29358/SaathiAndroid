package com.example.saathiapi

import com.example.saathiapi.model.reports.report.AssignedLeadReportRequest
import com.example.saathiapi.model.reports.report.AssignedLeadReportResponse
import com.example.saathiapi.model.reports.report.LeadReportRequest
import com.example.saathiapi.model.reports.report.LeadReportResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface SaathiApi {

  @POST("UserApi/GetUserBusinessProfileBranchByHierarchy")
  fun postAssignedLeadReport(
    @Body request: AssignedLeadReportRequest
  ): Observable<AssignedLeadReportResponse>

  @POST("Report/LoadReportViewDataAsync")
  fun postLeadReport(
    @Body request: LeadReportRequest
  ): Observable<LeadReportResponse>
}