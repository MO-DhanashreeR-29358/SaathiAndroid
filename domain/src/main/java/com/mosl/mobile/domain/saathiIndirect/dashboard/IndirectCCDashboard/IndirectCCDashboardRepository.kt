package com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard

import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import io.reactivex.rxjava3.core.Observable

interface IndirectCCDashboardRepository {

    fun fnGetCCDashboardFilter():Observable<IndirectCCFilterDataClass>
    fun getMeetingSummary(request : CCDashboardInput): Observable<DashboardReportCombo>

    fun getMeetingReachSummary(request : CCDashboardInput): Observable<DashboardReportCombo>
}