package com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard

import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import io.reactivex.rxjava3.core.Observable

interface IndirectIFADashboardRepository {

    fun fnGetIFADashboardFilter():Observable<List<LookUp>>

    fun fnGetTodaysBirthdayAnniversary(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetFirstMeetingNotDone(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetLeadsNotUpdated(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetSalesDashInactive(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetIFASalesDashTeamAppointment(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetIFASalesDashNewLead(objIndirectIFADashboardInput:IndirectIFADashboardInput):Observable<DashboardReportCombo>

    fun fnGetIFASalesDashboBuisnessSnapshot(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun  fnGetIFaSalesDashMyLead(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun fnGetIFASalesDashMyBuisnessSnapIP(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun fnGetIFADashREvenueIP(request: FocoDashboardInput): Observable<FocoDashboardModel>

    fun fnGetIFADashRevenueBroking(request: FocoDashboardInput):Observable<FocoDashboardModel>
    fun fnGetIFADashMeeting(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFADashIFAACtivation(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashMeetingTeamUniqueGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashMeetingTargetMetNotMetGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>
    fun fnGetIFASalesDashMeetingMyUniqueGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashRevenueIPProductWiseSalesGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashRevenueIPBranchWiseGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashRevenueIPTargetMetNotMetGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>


    fun fnGetIFASalesDashIFAActivationProductWiseGraph (request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashIFAActivationBranchWiseGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashRevenueBrokingProductWiseSalesGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetIFASalesDashRevenueBrokingBranchWiseGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>
    fun fnGetIFASalesDashRevenueBrokingTargetMetNotMetGraph(request: FocoDashboardInput):Observable<FocoDashboardModel>
    fun fnGetIFASalesDashRevenueBrokingHierarchy(strFilterValue: String):Observable<BusinessSnapshotCombo>
    fun fnGetIFASalesDashRevenueBrokingDetail(strFilterValue: String):Observable<BusinessSnapshotCombo>

    fun fnGetIFASalesDashMeetingHierarchy(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun fnGetIFASalesDashIFAActivationHierarchy(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun fnGetIFASalesDashMeetingDetail(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun fnGetIFASalesDashIFAActivationDetail(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun fnGetIFASalesDashRevenueIPDetail(strFilterValue:String): Observable<BusinessSnapshotCombo>

    fun fnGetIFASalesDashRevenueIPHierarchy(strFilterValue:String): Observable<BusinessSnapshotCombo>


}