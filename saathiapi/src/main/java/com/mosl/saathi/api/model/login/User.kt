package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class User(
  @SerializedName("Id") @JvmField val userId: String?,
  @SerializedName("EmployeeCode") @JvmField val employeeCode: String?,
  @SerializedName("FirstName") @JvmField val firstName: String?,
  @SerializedName("LastName") @JvmField val lastName: String?,
  @SerializedName("ImageUrl") @JvmField val imageUrl: String?,
  @SerializedName("Mobile") @JvmField val mobile: String?,
  @SerializedName("Email") @JvmField val email: String?,
  @SerializedName("Token") @JvmField val token: String?,
  @SerializedName("UserTypeSeq") @JvmField val userTypeSequence: String?,
  @SerializedName("DefaultBusinessProfile") @JvmField val defaultBusinessProfile: BusinessProfile,
  @SerializedName(
      "CurrentBusinessProfile"
  ) @JvmField val currentBusinessProfile: CurrentBusinessProfile
)