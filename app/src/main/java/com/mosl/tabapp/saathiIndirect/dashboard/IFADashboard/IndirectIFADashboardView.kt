package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import com.mosl.app.core.base.BaseView
import io.reactivex.rxjava3.core.Observable

interface IndirectIFADashboardView :BaseView{
    fun onLeadCreationClicked(): Observable<Unit>
    fun onActionableInsightsClicked(): Observable<Unit>
    fun showInsightsDialog()
}