package com.mosl.mobile.domain.birthdayanniversary

data class BirthdayAnniversaryItemData(
  @JvmField val clientCode: String, //0
  @JvmField val clientType: String,

  @JvmField val ucid: String, //0

  @JvmField val clientName: String,

  @JvmField val dob: String,

  @JvmField val anniversaryDate: String,

  @JvmField val noOfYearBirthday: String,

  @JvmField val backOfficeMobileNumber: String,

  @JvmField val email: String,

  @JvmField val birthdayCount: String,

  @JvmField val anniversaryCount: String,

  @JvmField val clientPotential: String,

  @JvmField val birthdayFlag: Boolean,

  @JvmField val anniversaryFlag: Boolean
)

data class ProfileSpinner(
  @JvmField val id: Int,
  @JvmField val lookUpName: String
) {
  override fun toString(): String {
    return lookUpName
  }
}

