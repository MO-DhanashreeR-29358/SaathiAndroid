package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Contact(

  @field:SerializedName("FDB")
  val fDB: Any? = null,

  @field:SerializedName("EKYCMobileNo")
  val eKYCMobileNo: String? = null,

  @field:SerializedName("AadharNumber")
  val aadharNumber: String? = null,

  @field:SerializedName("IsSameAsAbove")
  val isSameAsAbove: Boolean? = null,

  @field:SerializedName("AlternateMobileNo")
  val alternateMobileNo: String? = null,

  @field:SerializedName("Salutation")
  val salutation: Any? = null,

  @field:SerializedName("AnnualCashFlow")
  val annualCashFlow: Any? = null,

  @field:SerializedName("UCID")
  val uCID: Int? = null,

  @field:SerializedName("Name")
  val name: String? = null,

  @field:SerializedName("CompanyName")
  val companyName: Any? = null,

  @field:SerializedName("LandlineNo")
  val landlineNo: String? = null,

  @field:SerializedName("DOB")
  val dOB: Any? = null,

  @field:SerializedName("DOA")
  val dOA: Any? = null,

  @field:SerializedName("PrefixCode")
  val prefixCode: Any? = null,

  @field:SerializedName("IsAdminMode")
  val isAdminMode: Boolean? = null,

  @field:SerializedName("contactAddress")
  val contactAddress: List<ContactAddressItem?>? = null,

  @field:SerializedName("Status")
  val status: Int? = null,

  @field:SerializedName("Designation")
  val designation: Any? = null,

  @field:SerializedName("EmailId")
  val emailId: String? = null,

  @field:SerializedName("ContactID")
  val contactID: Int? = null,

  @field:SerializedName("Mobile")
  val mobile: String? = null,

  @field:SerializedName("InApproval")
  val inApproval: Boolean? = null,

  @field:SerializedName("AlternateEmailId")
  val alternateEmailId: String? = null,

  @field:SerializedName("ApprovalFieldList")
  val approvalFieldList: Any? = null,

  @field:SerializedName("PanNumber")
  val panNumber: String? = null
)