package com.mosl.saathi.api.model.ausfbLead


data class BranchCodeModel(
    val bankId: Int,
    val branchName: String,
    val branchId: Int


) {
    override fun toString(): String {
        return branchName
    }
}