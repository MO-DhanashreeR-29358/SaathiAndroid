package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.FilterPeriodModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardUIEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectIFATargetView:BaseView {

    fun onFilterEventCalled():Observable<IndirectIFADashboardUIEvent.onFilterChanged>

    fun fnRenderFilterEventCalled(strFilterVal:String)
}