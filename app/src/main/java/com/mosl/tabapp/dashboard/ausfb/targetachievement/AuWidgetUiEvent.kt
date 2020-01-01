package com.mosl.tabapp.dashboard.ausfb.targetachievement

import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class AuWidgetUiEvent {
    data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
        AuWidgetUiEvent()
}