package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("SMTEnabled") @JvmField val smtEnabled: String,
    @SerializedName("IsUseUrl") @JvmField val isUseUrl: String,
    @SerializedName("IsEkycEnable") @JvmField val isEkycEnable: String?,
    @SerializedName("IsRetriveOTPEnable") @JvmField val isRetrieveOTPEnable: String?,
    @SerializedName("IsChequeApprovalEnable") @JvmField val isChequeApprovalEnable: String?,
    @SerializedName("IsPrimeUser") @JvmField val isPrimeUser: String,
    @SerializedName("IsBank") @JvmField val isBank: String,
    @SerializedName("IsQRDownload") @JvmField val isQRDownload: String,
    @SerializedName("EventListRequired") @JvmField val EventListRequired: String,
    @SerializedName("User") @JvmField val user: User
) {

  fun isSMTEnabled(): Boolean {
    return smtEnabled.equals("1")
  }

}