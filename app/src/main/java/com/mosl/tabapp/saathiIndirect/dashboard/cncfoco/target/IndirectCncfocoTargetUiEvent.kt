package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target

import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class IndirectCncfocoTargetUiEvent {
    data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
        IndirectCncfocoTargetUiEvent()

    data class SummaryCountItemClick(@JvmField val tabelItemInfo: TableItemModel) :
        IndirectCncfocoTargetUiEvent()

    data class PieChartClick(@JvmField val label: String?) :
        IndirectCncfocoTargetUiEvent()
}