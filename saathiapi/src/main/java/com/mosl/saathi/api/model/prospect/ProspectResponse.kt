package com.mosl.saathi.api.model.prospect

import com.google.gson.annotations.SerializedName

data class ProspectResponse(
  @JvmField @SerializedName("RegistrationAllowed") val registrationAllowed: String,
  @JvmField @SerializedName("eKYC_UserID") val ekycUserID: String,
  @JvmField @SerializedName("EncryptedURL") val encryptedUrl: String,
  @SerializedName("IsUseUrl") @JvmField val isUseUrl: String

) {
  fun isUseUrlEnable(): Boolean {
    return isUseUrl == "1"
  }

  fun isRegistrationAllowed(): Boolean {
    return registrationAllowed == "true"
  }
}