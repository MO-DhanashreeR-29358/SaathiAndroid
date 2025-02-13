package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.ReportDetails

sealed class IndirectIFAReportDetailsUiEvent {

    object onDataRenderCall:IndirectIFAReportDetailsUiEvent()

    object  onLeadNotUpdateDataRenderCall:IndirectIFAReportDetailsUiEvent()
}