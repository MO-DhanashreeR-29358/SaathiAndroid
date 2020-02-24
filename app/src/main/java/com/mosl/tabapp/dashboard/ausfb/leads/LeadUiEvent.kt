package com.mosl.tabapp.dashboard.ausfb.leads


sealed class LeadUiEvent {

    data class BarChartClick(@JvmField val lable: String, @JvmField val xAxisValue: String) :
        LeadUiEvent()

}