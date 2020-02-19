package com.mosl.tabapp.dashboard.ausfb.target

import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class WidgetUiEvent {
    data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
        WidgetUiEvent()

    data class SummaryCountItemClick(@JvmField val tabelItemInfo: TableItemModel) :
        WidgetUiEvent()

    data class PieChartClick(@JvmField val label: String?) :
        WidgetUiEvent()

}