package com.mosl.tabapp.actionableinsight

import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class ActionableInsightEvent {
  data class OnItemClicked(@JvmField val widgetModel: WidgetModel) : ActionableInsightEvent()
}