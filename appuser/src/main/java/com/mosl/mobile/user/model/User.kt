package com.mosl.mobile.user.model

/**
 * Created by lalkishore on 14/01/18.
 */
data class User(
    @JvmField val smtEnabled: String,
    @JvmField val userId: String,
    @JvmField val employeeCode: String,
    @JvmField val firstName: String,
    @JvmField val lastName: String,
    @JvmField val imageUrl: String,
    @JvmField val mobile: String,
    @JvmField val email: String,
    @JvmField val token: String,
    @JvmField val userTypeSequence: String,
    @JvmField val defaultBusinessProfile: BusinessProfileRequest,
    @JvmField val currentBusinessProfile: CurrentBusinessProfileRequest,
    @JvmField var password: String

) {
    fun isAufsb(): Boolean {
        return userTypeSequence.equals("9")
    }

    fun isSMTEnabled(): Boolean {
        return smtEnabled.equals("1")
    }

    fun isAuBankSalesSupport(): Boolean {
        return currentBusinessProfile.businessProfileId?.equals("1142") ?: false
    }

    fun isRetail(): Boolean {
        return currentBusinessProfile.businessUnitId?.equals("1") ?: false
    }

    fun isFranchise(): Boolean {
        return currentBusinessProfile.businessUnitId?.equals("3") ?: false
    }

    fun isIFA(): Boolean {
        return currentBusinessProfile.businessUnitId?.equals("4") ?: false
    }

}
