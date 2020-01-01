package com.mosl.tabapp.dashboard.ausfb.targetachievement

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.AusfbLeadModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.BankCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.BranchCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.SubBranchCodeModel
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.Observable

interface AuTargetVsAchievementView : BaseView {

    fun onPeriodChange(): Observable<Int>
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
    fun getWidgetSystemsName(): String?
    fun renderData(uiModel: UiModel<AusfbLeadModel>)

    fun onWidgetItemClicked(): Observable<AuWidgetUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
}

