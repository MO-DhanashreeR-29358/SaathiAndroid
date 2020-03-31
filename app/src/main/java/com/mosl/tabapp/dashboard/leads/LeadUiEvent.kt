package com.mosl.tabapp.dashboard.leads


sealed class LeadUiEvent {

    data class BarChartClick(@JvmField val lable: String, @JvmField val xAxisValue: String) :
        LeadUiEvent()

    data class CellClick(@JvmField val additionalparametersmap: MutableMap<String, String>) :
        LeadUiEvent()

}