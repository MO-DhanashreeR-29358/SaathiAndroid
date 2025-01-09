package com.mosl.tabapp.saathiIndirect.dashboard.foco.target

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.mobile.domain.bank.SubBranchCodeModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.mobile.domain.target.TargetItemModel
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.dashboard.target.WidgetUiEvent
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.rxjava3.core.Observable

interface FocoTargetView : BaseView {

    fun renderPeriodFilter(uiModel: UiModel<List<FocoPeriodModel>>)
    fun onPeriodChange(): Observable<Int>
    fun getPeriod(): FocoPeriodModel?
    fun onWidgetItemClick(): Observable<FocoTargetUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
    fun renderData(uiModel: UiModel<FocoDashboardModel>)

    fun renderBarChartData(uiModel: UiModel<FocoDashboardModel>)
    fun getColorsArray(): IntArray
}

