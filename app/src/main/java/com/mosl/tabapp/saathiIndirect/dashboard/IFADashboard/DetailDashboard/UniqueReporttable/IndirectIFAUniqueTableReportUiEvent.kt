package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReporttable



sealed class IndirectIFAUniqueTableReportUiEvent{
    object onMeetingEventCalled: IndirectIFAUniqueTableReportUiEvent()
    object  onIfaActivationEventCalled: IndirectIFAUniqueTableReportUiEvent()
    object onRevenuIpEventCalled: IndirectIFAUniqueTableReportUiEvent()

    object onRevenuBroEventCalled: IndirectIFAUniqueTableReportUiEvent()
}
