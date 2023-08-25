package com.mosl.mobile.domain.reactivation

data class ReactivationSummaryItem(
    @JvmField val assigned: String,
    @JvmField val contacted: String,
    @JvmField val nonContacted: String,
    @JvmField val reactivatedClients: String,
    @JvmField val traded: String,
    @JvmField val revenue: String

)