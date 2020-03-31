package com.mosl.mobile.domain.report

import io.reactivex.Observable

interface HierarchyReportRepository {

  fun getDashboardData(request: HierarchyReportInput): Observable<HierarchyReportCombo>
}