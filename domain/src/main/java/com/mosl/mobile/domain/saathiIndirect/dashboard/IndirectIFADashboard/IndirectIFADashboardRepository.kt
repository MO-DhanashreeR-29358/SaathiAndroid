package com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard

import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import io.reactivex.rxjava3.core.Observable

interface IndirectIFADashboardRepository {

    fun fnGetIFADashboardFilter():Observable<List<LookUp>>

    fun fnGetTodaysBirthdayAnniversary(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetFirstMeetingNotDone(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetLeadsNotUpdated(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetSalesDashInactive(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetIFASalesDashTeamAppointment(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetIFASalesDashNewLead(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetIFASalesDashboBuisnessSnapshot():Observable<String>

    fun fnGetIFASalesDashMyBuisnessSnapIP():Observable<String>

    fun fnGetIFADashREvenueIP():Observable<String>

    fun fnGetIFADashRevenueBroking():Observable<String>
    fun fnGetIFADashMeeting() :Observable<String>

    fun fnGetIFADashIFAACtivation() :Observable<String>
}