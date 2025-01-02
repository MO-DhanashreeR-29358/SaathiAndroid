package com.mosl.tabapp.saathiIndirect.dashboard.foco

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.tabapp.saathiIndirect.dashboard.foco.domain.FocoPeriodModel
import io.reactivex.rxjava3.core.Observable

interface FocoDashboardView : BaseView {

    fun renderPeriodFilter(uiModel: UiModel<List<FocoPeriodModel>>)

    fun onActionableInsightsClicked(): Observable<Unit>
    fun showInsightsDialog()
    fun onLeadCreationClicked(): Observable<Unit>
}