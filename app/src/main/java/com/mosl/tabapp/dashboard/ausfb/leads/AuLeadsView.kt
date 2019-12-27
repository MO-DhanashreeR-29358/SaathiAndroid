package com.mosl.tabapp.dashboard.ausfb.leads

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.ausfbLead.BankCodeModel
import io.reactivex.Observable

interface AuLeadsView : BaseView {

    fun onMonthChange(): Observable<Int>
    fun onProductChange(): Observable<Int>
    fun onBankChange(): Observable<Int>
    fun onBranchChange(): Observable<Int>
    fun onSubBranchChange(): Observable<Int>
    fun renderBankFilter(uiModel: UiModel<List<BankCodeModel>>)

}