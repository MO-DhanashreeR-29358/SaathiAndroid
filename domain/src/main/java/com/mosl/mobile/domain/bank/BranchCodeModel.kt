package com.mosl.mobile.domain.bank


data class BranchCodeModel(
    @JvmField val bankId: Int,
    @JvmField val branchName: String,
    @JvmField val branchId: Int


) {
    override fun toString(): String {
        return branchName
    }
}