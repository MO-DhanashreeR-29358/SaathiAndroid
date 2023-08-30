package com.mosl.mobile.domain.reactivation


data class ReactivationDetailData(
    @JvmField val data: List<String>,
    @JvmField val hasMap: Map<String, String>
)

data class ReactivationDetailCombo(
    val headers: List<String>,
    val data: List<ReactivationDetailData>
)

data class ColHeader(
    val label: String,
    val index: Int = 0
)
