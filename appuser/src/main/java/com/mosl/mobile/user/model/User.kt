package com.mosl.mobile.user.model

/**
 * Created by lalkishore on 14/01/18.
 */
data class User(
    @JvmField val smtEnabled: String,
    @JvmField val isEkycEnable: String,
    @JvmField val isUseUrl: String,
    @JvmField val isChequeApprovalEnable: String,
    @JvmField val isRetrieveOTPEnable: String,
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
        return userTypeSequence == "9"
    }

    fun isSMTEnabled(): Boolean {
        return smtEnabled == "1"
    }

    fun isEkycEnable(): Boolean {
        return isEkycEnable == "1"
    }


    fun isCheckApprovalEnable(): Boolean {
        return isChequeApprovalEnable == "1"
    }

    fun isRetrieveOTPEnable(): Boolean {
        return isRetrieveOTPEnable == "1"
    }

    fun isUseUrlEnable(): Boolean {
        return isUseUrl == "1"
    }

    fun isAuBankSalesSupport(): Boolean {
        currentBusinessProfile.businessProfileId?.apply {
            return this == "1142" || this == "1156"
        }
        return false
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

    fun isRM(): Boolean {
        currentBusinessProfile.businessProfileName.apply {
            return this.equals("Sales") || this.equals("Retail Advisor")
        }
    }

}
