package com.mosl.mobile.domain.reactivation

data class ReactivationActualMarginPostModel(
    @JvmField  val businessUnitId: String,

    @JvmField   val clientCode: String,

    @JvmField  val actualActivationDate: String
)
