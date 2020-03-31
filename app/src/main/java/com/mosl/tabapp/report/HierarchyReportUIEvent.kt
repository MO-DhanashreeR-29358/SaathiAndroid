package com.mosl.tabapp.report

sealed class HierarchyReportUIEvent {

  data class CellEvent(@JvmField val leadId: String, @JvmField val UCID: String) :
    HierarchyReportUIEvent()
}