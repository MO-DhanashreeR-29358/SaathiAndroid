package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

sealed class IndirectIFADashboardReportUIEvent{

    data class OnReportClick(
        @JvmField val widgetName: String
    ) :IndirectIFADashboardReportUIEvent()
}
