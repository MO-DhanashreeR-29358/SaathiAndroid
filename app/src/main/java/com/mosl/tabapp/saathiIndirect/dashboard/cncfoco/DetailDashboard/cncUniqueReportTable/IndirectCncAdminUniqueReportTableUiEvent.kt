package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.cncUniqueReportTable


sealed class IndirectCncAdminUniqueReportTableUiEvent {
    object onMeetingEventCalled: IndirectCncAdminUniqueReportTableUiEvent()

    object onRevenuBroEventCalled: IndirectCncAdminUniqueReportTableUiEvent()

    data class onReportRowClicked(var strLeadId:String): IndirectCncAdminUniqueReportTableUiEvent()
}