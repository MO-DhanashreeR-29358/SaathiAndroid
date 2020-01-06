package com.mosl.tabapp.dashboard.ausfb.target

import com.mosl.mobile.domain.autarget.TableItemModel
import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class AuWidgetUiEvent {
    data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
        AuWidgetUiEvent()

    data class SummaryCountItemClick(@JvmField val tabelItemInfo: TableItemModel) :
        AuWidgetUiEvent()

    data class PieChartClick(@JvmField val label: String?) :
        AuWidgetUiEvent()


}