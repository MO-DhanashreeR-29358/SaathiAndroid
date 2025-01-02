package com.mosl.mobile.domain.saathiIndirect.dashboard

import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import io.reactivex.rxjava3.core.Observable

interface FocoRepository {

    fun getPeriodFilter(request: FocoPeriodFilterInput): Observable<List<FocoPeriodModel>>

    fun getActionableTalkTime(request : FocoDashboardInput): Observable<DashboardReportCombo>
    fun getActionableTopIncentive(request : FocoDashboardInput): Observable<DashboardReportCombo>

    fun getActionableClientReactivation(request : FocoDashboardInput): Observable<DashboardReportCombo>

    fun getActionableTopClientList(request : FocoDashboardInput): Observable<DashboardReportCombo>

}