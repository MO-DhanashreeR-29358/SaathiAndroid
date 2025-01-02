package com.mosl.tabapp.saathiIndirect.dashboard.foco.domain


data class FocoPeriodModel(

    @JvmField val ord: Int?,
    @JvmField val displayText: String,
    @JvmField val value: String
){
    override fun toString(): String {
        return displayText
    }
}