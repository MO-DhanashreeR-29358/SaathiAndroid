package com.mosl.tabapp.dashboard.report

sealed class DashboardReportUIEvent {

  data class CellEvent(
    @JvmField val leadId: String,
    @JvmField val UCID: String,
    @JvmField val employeeCode: String = ""
  ) :
    DashboardReportUIEvent()
}