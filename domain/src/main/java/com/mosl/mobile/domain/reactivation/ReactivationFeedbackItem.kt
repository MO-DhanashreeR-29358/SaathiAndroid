package com.mosl.mobile.domain.reactivation

data class ReactivationFeedbackItem(
    @JvmField val employeeCode: String,
    @JvmField val period: String,
    @JvmField val currentStage: String,
    @JvmField val refNo: String,
    @JvmField val remarks: String,
)
