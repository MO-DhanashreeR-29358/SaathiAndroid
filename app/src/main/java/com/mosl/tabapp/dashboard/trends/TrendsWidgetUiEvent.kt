package com.mosl.tabapp.dashboard.trends

import com.mosl.tabapp.model.dashboard.DashboardGroupModel

sealed class TrendsWidgetUiEvent {
    data class ItemClick(@JvmField val widgetItemInfo: DashboardGroupModel, @JvmField val currentPosition: Int) :
        TrendsWidgetUiEvent()

}