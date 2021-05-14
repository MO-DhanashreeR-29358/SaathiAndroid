package com.mosl.tabapp.dashboard.report

sealed class DashboardReportUIEvent {

  data class CellEvent(
      @JvmField val leadId: String,
      @JvmField val ucid: String,
      @JvmField val employeeCode: String = "",
      @JvmField val level: String = "",
      @JvmField val systemsName: String = "",
      @JvmField val rmName: String = "",
      @JvmField val branchCode: String = ""
  ) :
      DashboardReportUIEvent()
}