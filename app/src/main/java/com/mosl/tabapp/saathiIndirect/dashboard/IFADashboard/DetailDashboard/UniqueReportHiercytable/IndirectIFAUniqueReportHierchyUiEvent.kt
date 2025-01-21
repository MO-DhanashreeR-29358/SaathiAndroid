package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable

sealed class IndirectIFAUniqueReportHierchyUiEvent
{
    object onMeetingEventCalled: IndirectIFAUniqueReportHierchyUiEvent()
    object  onIfaActivationEventCalled: IndirectIFAUniqueReportHierchyUiEvent()
    object onRevenuIpEventCalled: IndirectIFAUniqueReportHierchyUiEvent()

    object onRevenuBroEventCalled: IndirectIFAUniqueReportHierchyUiEvent()
}

