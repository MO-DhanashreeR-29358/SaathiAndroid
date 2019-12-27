package com.mosl.saathi.api.model.ausfbLead


data class BankCodeModel(
    val bankId: Int,
    val bankName: String


) {
    override fun toString(): String {
        return bankName
    }
}