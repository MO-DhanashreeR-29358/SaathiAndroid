package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

sealed class FocoDashboardReportUIEvent {

  data class OnReportClick(
      @JvmField val widgetName: String
  ) : FocoDashboardReportUIEvent()
}