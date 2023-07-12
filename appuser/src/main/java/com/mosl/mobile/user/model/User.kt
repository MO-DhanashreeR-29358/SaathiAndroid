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
    @JvmField val isPrimeUser: Boolean,
    @JvmField val userTypeSequence: String,
    @JvmField val defaultBusinessProfile: BusinessProfileRequest,
    @JvmField val currentBusinessProfile: CurrentBusinessProfileRequest,
    @JvmField var password: String,
    @JvmField var isBank: Boolean

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
                    || this == "1158"
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

    fun isPCG(): Boolean {
        return currentBusinessProfile.businessUnitId?.equals("5") ?: false
    }

    fun isRM(): Boolean {
        var flag: Boolean = false
        if(!currentBusinessProfile.businessUnitId.equals("14")) {
            currentBusinessProfile.businessProfileName.apply {
                //return this.equals("Sales") || this.equals("Retail Advisor") || this.equals("Prime Sales")
                if(this.equals("Sales") ||
                    this.equals("Retail Advisor") ||
                    this.equals("Prime Sales")||
                    this.equals("Key Account Manager")) {
                    flag = true
                }
            }
        } else {
            flag = true
        }
        return flag
    }
    fun isShowORCode(): Boolean {
        var flag: Boolean = false
        if(currentBusinessProfile.businessUnitId.equals("1")) {
            currentBusinessProfile.businessProfileName.apply {
                if(this.equals("Sales") ||
                    this.equals("Sales Manager") ||
                    this.equals("Sales Team Leader")||
                    this.equals("Team Leader Sales")||
                    this.equals("Area Sales Manager") ||
                    this.equals("Regional Sales Manager")||
                    this.equals("Sales Regional Manager")||
                    this.equals("Cluster Head")||
                    this.equals("Regional Head")||
                    this.equals("National Sales Head")||
                    this.equals("Key Account Manager")||
                    this.equals("National Head")) {
                    flag = true
                }
            }
        }
        return flag
    }

    fun isAllianceBusiness(): Boolean {
        return currentBusinessProfile.businessUnitId?.equals("11") ?: false
    }

    fun isPCGBusiness(): Boolean {
        return currentBusinessProfile.businessUnitId?.equals("5") ?: false
    }

    fun isValidateEmailIdFromLead(): Boolean {
        var flag = false
        if (currentBusinessProfile.businessUnitId.equals("14") ||
            currentBusinessProfile.businessUnitId.equals("1")) {
            flag = true
        }
        return flag
    }

    fun isShowRINRI(): Boolean {
        var flag = false
        //UAT - 17, Live -18
        if (currentBusinessProfile.businessUnitId.equals("17")  ||
            currentBusinessProfile.businessUnitId.equals("18")) {
            flag = true
        }
        return flag
    }
}
