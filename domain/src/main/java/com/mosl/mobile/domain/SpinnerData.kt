package com.mosl.mobile.domain

interface SpinnerData {
    val labelRes: Int
}

data class SpinnerModel(
    val id: Int,
    val value: String
) {
    override fun toString(): String {
        return value
    }
}