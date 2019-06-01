package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

class BirthdayAnniversaryData(
  @JvmField @SerializedName("ClientCode")
  val clientCode: String?, //0

  @JvmField @SerializedName("ClientType")
  val clientType: String?,

  @JvmField @SerializedName("UCID")
  val ucid: String?, //0

  @JvmField @SerializedName("ClientName")
  val clientName: String?,

  @JvmField @SerializedName("DOB")
  val dob: String?,

  @JvmField @SerializedName("AniversaryDate")
  val anniversaryDate: String?,

  @JvmField @SerializedName("NoOfYearBirthday")
  val noOfYearBirthday: String?,

  @JvmField @SerializedName("BackOfficeMobileNumber")
  val backOfficeMobileNumber: String?,

  @JvmField @SerializedName("Email")
  val email: String?,

  @JvmField @SerializedName("BirthdayCount")
  val birthdayCount: String?,

  @JvmField @SerializedName("AniversaryCount")
  val anniversaryCount: String?,

  @JvmField @SerializedName("ClientPotential")
  val clientPotential: String?,

  @JvmField @SerializedName("Birthday")
  val birthdayFlag: Boolean,

  @JvmField @SerializedName("Anniversory")
  val anniversaryFlag: Boolean

)