package com.mosl.mobile.domain.setappointment.lead

data class ContactResponse(

  @JvmField val fDB: Any,

  @JvmField val eKYCMobileNo: String,

  @JvmField val aadharNumber: String,

  @JvmField val isSameAsAbove: Boolean,

  @JvmField val alternateMobileNo: String,

  @JvmField val salutation: Any,

  @JvmField val annualCashFlow: Any,

  @JvmField val uCID: String? = "0",

  @JvmField val name: String? = "",

  @JvmField val companyName: Any,

  @JvmField val landlineNo: String,

  @JvmField val dOB: String,

  @JvmField val dOA: String,

  @JvmField val prefixCode: Any,

  @JvmField val isAdminMode: Boolean,

  @JvmField val contactAddress: List<ContactAddressItemResponse>,

  @JvmField val status: Int,

  @JvmField val designation: Any,

  @JvmField val emailId: String?,

  @JvmField val contactID: Int,

  @JvmField val mobile: String?,

  @JvmField val inApproval: Boolean,

  @JvmField val alternateEmailId: String,

  @JvmField val approvalFieldList: Any,

  @JvmField val panNumber: String
)