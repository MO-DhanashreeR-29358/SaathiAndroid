package com.mosl.tabapp.dashboard.ausfb.leads


sealed class AuLeadUiEvent {

    data class BarChartClick(@JvmField val lable: String, @JvmField val xAxisValue: String) :
        AuLeadUiEvent()

}