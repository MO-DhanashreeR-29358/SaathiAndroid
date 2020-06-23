package com.mosl.tabapp.dashboard.report.assign

sealed class DashboardReportAssignUIEvent {

    data class BtnEvent(
        @JvmField val status: String
    ) :
        DashboardReportAssignUIEvent()

    data class CheckBoxSelectedArrayList(
        @JvmField val selectedArrayList: ArrayList<String>
    ) :
        DashboardReportAssignUIEvent()
}