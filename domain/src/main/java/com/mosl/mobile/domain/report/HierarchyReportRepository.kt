package com.mosl.mobile.domain.report

import io.reactivex.rxjava3.core.Observable

interface HierarchyReportRepository {

  fun getBranch(): Observable<List<AssignedLeadReportModel>>
  fun getDashboardData(hierarchyReportInput: HierarchyReportInput): Observable<HierarchyReportCombo>
}