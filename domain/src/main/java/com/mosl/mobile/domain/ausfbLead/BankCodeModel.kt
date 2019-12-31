package com.mosl.saathi.api.model.dashboard.ausfbLead


data class BankCodeModel(
    @JvmField val bankId: Int,
    @JvmField val bankName: String


) {
    override fun toString(): String {
        return bankName
    }
}