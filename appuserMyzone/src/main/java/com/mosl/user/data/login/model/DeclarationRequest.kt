package com.mosl.user.data.login.model

import com.google.gson.annotations.SerializedName

data class DeclarationRequest(
  @JvmField @SerializedName("EmpCode") val empCode: String
)