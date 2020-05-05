package com.mosl.mobile.domain.dashboardreport

import io.reactivex.Observable

interface DashboardReportRepository {
  fun getDashboardData(dashboardReportRequest: DashboardReportRequest): Observable<DashboardReportCombo>
}