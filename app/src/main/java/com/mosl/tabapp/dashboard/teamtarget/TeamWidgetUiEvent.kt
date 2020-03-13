package com.mosl.tabapp.dashboard.teamtarget

import com.mosl.mobile.domain.teamtarget.TeamTableItem3Model
import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class TeamWidgetUiEvent {
  data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
    TeamWidgetUiEvent()

  data class SummaryCountItemClick(@JvmField val tabelItemInfo: TeamTableItem3Model) :
    TeamWidgetUiEvent()

  data class PieChartClick(@JvmField val label: String?, @JvmField val widgetType: String?) :
    TeamWidgetUiEvent()

}