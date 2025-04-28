package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import io.reactivex.rxjava3.core.Observable

interface IndirectCncFocoDashboardDetailsView :BaseView {
    fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>)

    fun onFilterEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.onFilterChanged>

    fun fnRenderFilterEventCalled(strFilterVal:String)

    fun onDashboardDetailsEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.OnDetailsDataCall>
}