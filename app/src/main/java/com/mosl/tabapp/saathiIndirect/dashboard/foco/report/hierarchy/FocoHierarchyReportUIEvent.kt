package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

sealed class FocoHierarchyReportUIEvent {

  data class OnReportClick(
      @JvmField val widgetName: String
  ) : FocoHierarchyReportUIEvent()
}