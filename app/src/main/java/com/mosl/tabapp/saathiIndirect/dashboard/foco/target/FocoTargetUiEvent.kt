package com.mosl.tabapp.saathiIndirect.dashboard.foco.target

import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class FocoTargetUiEvent {
    data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
        FocoTargetUiEvent()

    data class SummaryCountItemClick(@JvmField val tabelItemInfo: TableItemModel) :
        FocoTargetUiEvent()

    data class PieChartClick(@JvmField val label: String?) :
        FocoTargetUiEvent()

}