package com.mosl.saathi.api.model.dashboard.ausfbLead


data class SubBranchCodeModel(
    @JvmField val bankBranchId: Int,
    @JvmField val subBrokerName: String


) {
    override fun toString(): String {
        return subBrokerName
    }
}