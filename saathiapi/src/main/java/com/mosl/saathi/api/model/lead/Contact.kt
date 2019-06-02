package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class Contact(
    @JvmField @SerializedName("FDB") val fDB: String?,
    @JvmField @SerializedName("EKYCMobileNo") val eKYCMobileNo: String?,
    @JvmField @SerializedName("AadharNumber") val aadharNumber: String?,
    @JvmField @SerializedName("IsSameAsAbove") val isSameAsAbove: Boolean,
    @JvmField @SerializedName("AlternateMobileNo") val alternateMobileNo: String?,
    @JvmField @SerializedName("Salutation") val salutation: String?,
    @JvmField @SerializedName("AnnualCashFlow") val annualCashFlow: String?,
    @JvmField @SerializedName("UCID") val uCID: Int,
    @JvmField @SerializedName("Name") val name: String?,
    @JvmField @SerializedName("CompanyName") val companyName: String?,
    @JvmField @SerializedName("LandlineNo") val landlineNo: String?,
    @JvmField @SerializedName("DOB") val dOB: String?,
    @JvmField @SerializedName("DOA") val dOA: String?,
    @JvmField @SerializedName("PrefixCode") val prefixCode: String?,
    @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean?,
    @JvmField @SerializedName("contactAddress") val contactAddress: List<ContactAddressItem>?,
    @JvmField @SerializedName("Status") val status: Int?,
    @JvmField @SerializedName("Designation") val designation: String?,
    @JvmField @SerializedName("EmailId") val emailId: String?,
    @JvmField @SerializedName("ContactID") val contactID: Int,
    @JvmField @SerializedName("Mobile") val mobile: String?,
    @JvmField @SerializedName("InApproval") val inApproval: Boolean?,
    @JvmField @SerializedName("AlternateEmailId") val alternateEmailId: String?,
    @JvmField @SerializedName("ApprovalFieldList") val approvalFieldList: String?,
    @JvmField @SerializedName("PanNumber") val panNumber: String?
)