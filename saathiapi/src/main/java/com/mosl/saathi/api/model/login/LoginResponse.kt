package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
  @SerializedName("SMTEnabled") @JvmField val smtEnabled: String,
  @SerializedName("IsEKYCRevamp") @JvmField val ekycRevamp: String,
  @SerializedName("User") @JvmField val user: User
) {

  fun isSMTEnabled(): Boolean {
    return smtEnabled.equals("1")
  }

  fun isEkycRevamp(): Boolean {
    return ekycRevamp.equals("1")
  }
}