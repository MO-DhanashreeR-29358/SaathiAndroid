package com.mosl.tabapp.saathiIndirect.dashboard

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.mobile.domain.dashboard.Incentive
import com.mosl.mobile.domain.dashboard.IncentivePolicy
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.*
import io.reactivex.rxjava3.core.Observable

interface IndirectDashboardView : BaseView {
    fun onLeadCreationClicked(): Observable<Unit>
}