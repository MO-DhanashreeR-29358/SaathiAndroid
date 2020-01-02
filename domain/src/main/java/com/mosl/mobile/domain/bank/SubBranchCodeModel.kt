package com.mosl.mobile.domain.bank


data class SubBranchCodeModel(
    @JvmField val bankBranchId: Int,
    @JvmField val subBrokerName: String


) {
    override fun toString(): String {
        return subBrokerName
    }
}