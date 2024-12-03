package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class Contact(

  @JvmField @SerializedName("FDB") val fDB: Any?,

  @JvmField @SerializedName("EKYCMobileNo") val eKYCMobileNo: String?,

  @JvmField @SerializedName("AadharNumber") val aadharNumber: String,

  @JvmField @SerializedName("IsSameAsAbove") val isSameAsAbove: Boolean,

  @JvmField @SerializedName("AlternateMobileNo") val alternateMobileNo: String,

  @JvmField @SerializedName("Salutation") val salutation: Any?,

  @JvmField @SerializedName("AnnualCashFlow") val annualCashFlow: Any?,

  @JvmField @SerializedName("UCID") val uCID: String? = "0",

  @JvmField @SerializedName("Name") val name: String? = "",

  @JvmField @SerializedName("CompanyName") val companyName: Any?,

  @JvmField @SerializedName("LandlineNo") val landlineNo: String,

  @JvmField @SerializedName("DOB") val dOB: String,

  @JvmField @SerializedName("DOA") val dOA: String,

  @JvmField @SerializedName("PrefixCode") val prefixCode: Any?,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean,

  @JvmField @SerializedName("contactAddress") val contactAddress: List<ContactAddressItem>,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("Designation") val designation: Any?,

  @JvmField @SerializedName("EmailId") val emailId: String?,

  @JvmField @SerializedName("ContactID") val contactID: Int,

  @JvmField @SerializedName("Mobile") val mobile: String?,

  @JvmField @SerializedName("InApproval") val inApproval: Boolean,

  @JvmField @SerializedName("AlternateEmailId") val alternateEmailId: String,

  @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: Any?,

  @JvmField @SerializedName("PanNumber") val panNumber: String,

  @JvmField @SerializedName("MaskedPanNumber") val maskedPanNumber: String,
  @JvmField @SerializedName("MaskedEmailId") val maskedEmailId: String,
  @JvmField @SerializedName("MaskedMobile") val maskedMobile: String,
  @JvmField @SerializedName("MaskedAlternateMobileNo") val maskedAlternateMobileNo: String,
  @JvmField @SerializedName("MaskedLandlineNo") val maskedLandlineNo: String,
  @JvmField @SerializedName("MaskedAlternateEmailId") val maskedAlternateEmailId: String
)