package com.mosl.mobile.domain.dashboardreport

import io.reactivex.rxjava3.core.Observable

interface DashboardReportRepository {
  fun getDashboardData(dashboardReportRequest: DashboardReportRequest): Observable<DashboardReportCombo>
}