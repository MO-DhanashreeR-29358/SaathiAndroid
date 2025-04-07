package com.mosl.mobile.domain.saathiIndirect.dashboard.cncfoco

import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import io.reactivex.rxjava3.core.Observable

interface CncFocoRepository {

    fun fnGetCncDashboardFilter():Observable<IndirectFilterDataClass>

    fun fngetCNCFocoDashRevenueWidgetData(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fngetCNCFocoDashNoOfClientMeetingWidgetData(request: FocoDashboardInput):Observable<FocoDashboardModel>

    fun fnGetCNCFocoDashMeetingDetail(strFilterValue:String,strFilterProduct:String): Observable<BusinessSnapshotCombo>

    fun fnGetCNCFocoDashRevenueDetail(strFilterValue:String,strFilterProduct:String): Observable<BusinessSnapshotCombo>


}