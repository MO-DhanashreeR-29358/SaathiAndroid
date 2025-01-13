package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class IndirectIFATargetUiEvent{
    data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
        IndirectIFATargetUiEvent()

    data class SummaryCountItemClick(@JvmField val tabelItemInfo: TableItemModel) :
        IndirectIFATargetUiEvent()

    data class PieChartClick(@JvmField val label: String?) :
        IndirectIFATargetUiEvent()
}
