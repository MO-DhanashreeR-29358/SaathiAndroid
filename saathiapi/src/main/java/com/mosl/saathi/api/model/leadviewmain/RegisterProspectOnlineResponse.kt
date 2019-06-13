package com.mosl.saathi.api.model.leadviewmain

import com.google.gson.annotations.SerializedName

data class RegisterProspectOnlineResponse(

  @JvmField @SerializedName("IsEkyCReLive") val isEkyCReLive: Boolean,

  @JvmField @SerializedName("Registration_InterestedIn") val registrationInterestedIn: Any,

  @JvmField @SerializedName("DedupeBranch") val dedupeBranch: Any,

  @JvmField @SerializedName("Registration_Tradercode") val registrationTradercode: Any,

  @JvmField @SerializedName("Registration_PSource") val registrationPSource: Any,

  @JvmField @SerializedName("DedupeOn") val dedupeOn: Any,

  @JvmField @SerializedName("Registration_Emailid") val registrationEmailid: Any,

  @JvmField @SerializedName("DedupeWith") val dedupeWith: Any,

  @JvmField @SerializedName("DedupeRemark") val dedupeRemark: Any,

  @JvmField @SerializedName("eKYC_UserID") val eKYCUserID: Any,

  @JvmField @SerializedName("Registration_PSourceDetail") val registrationPSourceDetail: Any,

  @JvmField @SerializedName("URL") val uRL: Any,

  @JvmField @SerializedName("Registration_Branchcode") val registrationBranchcode: Any,

  @JvmField @SerializedName("RegistrationAllowed") val registrationAllowed: Boolean,

  @JvmField @SerializedName("EncryptedURL") val encryptedURL: String,

  @JvmField @SerializedName("Registration_Source") val registrationSource: Any,

  @JvmField @SerializedName("Registration_MobileNumber") val registrationMobileNumber: Any,

  @JvmField @SerializedName("Registration_Subbrokercode") val registrationSubbrokercode: Any,

  @JvmField @SerializedName("IsDedupe") val isDedupe: Boolean,

  @JvmField @SerializedName("Registration_ApplicationFrom") val registrationApplicationFrom: Any,

  @JvmField @SerializedName("Registration_Employeecode") val registrationEmployeecode: Any
)