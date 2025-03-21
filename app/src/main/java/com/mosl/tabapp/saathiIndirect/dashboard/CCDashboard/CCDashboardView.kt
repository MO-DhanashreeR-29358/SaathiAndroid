package com.mosl.tabapp.saathiIndirect.dashboard.CCDashboard

import com.mosl.app.core.base.BaseView
import io.reactivex.rxjava3.core.Observable

interface CCDashboardView : BaseView {
    fun onLeadCreationClicked(): Observable<Unit>
}