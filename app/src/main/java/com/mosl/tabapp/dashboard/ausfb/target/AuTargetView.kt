package com.mosl.tabapp.dashboard.ausfb.target

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.autarget.TargetItemModel
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.mobile.domain.bank.SubBranchCodeModel
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.Observable

interface AuTargetView : BaseView {

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
    fun renderData(uiModel: UiModel<TargetItemModel>)
    fun onWidgetItemClicked(): Observable<AuWidgetUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
    fun getWidgetModel(): WidgetModel?
    fun getAddtionalParameterMap(): Map<String, String>
    fun onSummaryCountClicked(): Observable<AuWidgetUiEvent.SummaryCountItemClick>
    fun onPieChartClicked(): Observable<AuWidgetUiEvent.PieChartClick>
    fun getColorsArray(): IntArray

}

