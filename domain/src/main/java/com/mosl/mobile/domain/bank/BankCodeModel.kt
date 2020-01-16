package com.mosl.mobile.domain.bank


data class BankCodeModel(
    @JvmField val bankId: Int,
    @JvmField val bankName: String


) {
    override fun toString(): String {
        return bankName
    }
}