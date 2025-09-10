package com.mosl.mobile.domain.saathiIndirect.dashboard


data class FocoPeriodModel(

    @JvmField val ord: Int?,
    @JvmField val displayText: String,
    @JvmField val value: String
){
    override fun toString(): String {
        return displayText
    }
}