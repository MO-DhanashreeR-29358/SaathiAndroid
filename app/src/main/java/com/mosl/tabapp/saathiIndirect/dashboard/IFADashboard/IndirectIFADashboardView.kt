package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import io.reactivex.rxjava3.core.Observable

interface IndirectIFADashboardView :BaseView{
    fun onLeadCreationClicked(): Observable<Unit>
    fun onActionableInsightsClicked(): Observable<Unit>
    fun showInsightsDialog()
    fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>)



}