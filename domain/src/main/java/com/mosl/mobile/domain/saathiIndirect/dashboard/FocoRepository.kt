package com.mosl.mobile.domain.saathiIndirect.dashboard

import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import io.reactivex.rxjava3.core.Observable

interface FocoRepository {

    fun getPeriodFilter(request: FocoDashboardInput): Observable<List<FocoPeriodModel>>

    fun getActionableTalkTime(request : FocoDashboardInput): Observable<DashboardReportCombo>
    fun getActionableTopIncentive(request : FocoDashboardInput): Observable<DashboardReportCombo>

    fun getActionableClientReactivation(request : FocoDashboardInput): Observable<DashboardReportCombo>

    fun getActionableTopClientList(request : FocoDashboardInput): Observable<DashboardReportCombo>

    fun getFocoDashRevenueWidgetData(request : FocoDashboardInput): Observable<FocoDashboardModel>
    fun getFocoDashRevenueGrowthWidgetData(request : FocoDashboardInput): Observable<FocoDashboardModel>
    fun getFocoDashNoOfClientsWidgetData(request : FocoDashboardInput): Observable<FocoDashboardModel>

    fun getFocoDashOnlineRevenueWidgetData(request : FocoDashboardInput): Observable<FocoDashboardModel>
    fun getFocoDashClientReachWidgetData(request : FocoDashboardInput): Observable<FocoDashboardModel>

    fun getFocoDashIPSalesWidgetData(request : FocoDashboardInput): Observable<FocoDashboardModel>

    fun getFocoDashTotalMobAppWidgetData(request : FocoDashboardInput): Observable<FocoDashboardModel>
    fun getFocoDashRunrateForAdvisorWidgetData(request : FocoDashboardInput): Observable<DashboardReportCombo>

}