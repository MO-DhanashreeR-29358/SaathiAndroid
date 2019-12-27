package com.mosl.saathi.api.model.ausfbLead


data class SubBranchCodeModel(
    val bankBranchId: Int,
    val subBrokerName: String


) {
    override fun toString(): String {
        return subBrokerName
    }
}