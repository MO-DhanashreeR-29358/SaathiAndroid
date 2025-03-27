package com.mosl.tabapp.saathiIndirect.dashboard.CCDashboard

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.IndirectCCFilterDataClass
import io.reactivex.rxjava3.core.Observable

interface CCDashboardView : BaseView {
    fun fnRenderFilterList(uiModel: UiModel<IndirectCCFilterDataClass>)
    fun onPeriodChange(): Observable<Int>
    fun onAdvisorChange(): Observable<Int>

    fun getPeriod(): String?
    fun getAdvisor(): String?

    fun renderMeetingSummaryData(uiModel: UiModel<DashboardReportCombo>)
    fun renderMeetingReachSummaryData(uiModel: UiModel<DashboardReportCombo>)

    fun onMRSLoadEvent() : Observable<CCDashboardUIEvent.OnMeetingReachSummaryLoad>
    fun onLeadCreationClicked(): Observable<Unit>

}