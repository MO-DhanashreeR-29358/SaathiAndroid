package com.mosl.mobile.domain.lead.report

import io.reactivex.Observable

interface LeadsReportRepository {
  fun getLeadWidget(request: LeadsReportInput): Observable<Map<String, Any>>

}