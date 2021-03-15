package com.mosl.mobile.domain.createmeeting

data class MobileListData(
    @JvmField val contactField: String,
    @JvmField val contactNumber: String
) {
    override fun toString(): String {
        return contactField
    }
}