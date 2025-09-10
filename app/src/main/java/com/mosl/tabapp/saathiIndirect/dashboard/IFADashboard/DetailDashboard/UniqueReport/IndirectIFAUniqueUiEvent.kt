package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport

import com.mosl.tabapp.model.dashboard.WidgetModel

sealed class IndirectIFAUniqueUiEvent{
    data class ItemClick(@JvmField val widgetItemInfo: WidgetModel, @JvmField val currentPosition: Int) :
        IndirectIFAUniqueUiEvent()
    object onMeetingEventCalled: IndirectIFAUniqueUiEvent()
    object  onIfaActivationEventCalled:IndirectIFAUniqueUiEvent()
    object onRevenuIpEventCalled: IndirectIFAUniqueUiEvent()

    object onRevenuBroEventCalled:IndirectIFAUniqueUiEvent()
}
