package com.mosl.tabapp.dashboard.teamtarget

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.mobile.domain.bank.SubBranchCodeModel
import com.mosl.mobile.domain.teamtarget.TeamTargetItemModel
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.Observable

interface TeamTargetView : BaseView {

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
  fun renderData(uiModel: UiModel<TeamTargetItemModel>)
  fun onWidgetItemClick(): Observable<TeamWidgetUiEvent.ItemClick>
  fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
  fun getWidgetModel(): WidgetModel?
  fun getAddtionalParameterMap(): Map<String, String>
  fun onSummaryCountClick(): Observable<TeamWidgetUiEvent.SummaryCountItemClick>
  fun onPieChartClick(): Observable<TeamWidgetUiEvent.PieChartClick>
  fun getColorsArray(): IntArray

}

