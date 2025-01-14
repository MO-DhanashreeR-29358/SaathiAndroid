package com.mosl.tabapp.saathiIndirect.dashboard.foco

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import io.reactivex.rxjava3.core.Observable

interface FocoDashboardView : BaseView {



    fun onActionableInsightsClicked(): Observable<Unit>
    fun showInsightsDialog()
    fun onLeadCreationClicked(): Observable<Unit>
}