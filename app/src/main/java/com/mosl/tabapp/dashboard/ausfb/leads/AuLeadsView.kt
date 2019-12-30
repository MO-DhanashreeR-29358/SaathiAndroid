package com.mosl.tabapp.dashboard.ausfb.leads

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.ausfbLead.BankCodeModel
import com.mosl.saathi.api.model.ausfbLead.BranchCodeModel
import com.mosl.saathi.api.model.ausfbLead.SubBranchCodeModel
import com.mosl.tabapp.common.FilterRowData
import io.reactivex.Observable

interface AuLeadsView : BaseView {

    fun onPeriodChange(): Observable<Int>
    fun onProductChange(): Observable<Int>
    fun onBankChange(): Observable<Int>
    fun onBranchChange(): Observable<Int>
    fun onSubBranchChange(): Observable<Int>
    fun renderBankFilter(uiModel: UiModel<List<BankCodeModel>>)
    fun renderBranchFilter(uiModel: UiModel<List<BranchCodeModel>>)
    fun renderSubBranchFilter(uiModel: UiModel<List<SubBranchCodeModel>>)
    fun getBank(): BankCodeModel?
    fun getBranch(): BranchCodeModel?
    fun getSubBranch(): SubBranchCodeModel?
    fun getPeriod(): FilterRowData?
    fun getProduct(): FilterRowData?
    fun getWidgetSystemsName(): String?

}