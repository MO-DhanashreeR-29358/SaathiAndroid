package com.mosl.mobile.domain.lead.report

import io.reactivex.rxjava3.core.Observable

interface LeadsReportRepository {
  fun getLeadWidget(request: LeadsReportInput): Observable<LeadsReportModel>

}