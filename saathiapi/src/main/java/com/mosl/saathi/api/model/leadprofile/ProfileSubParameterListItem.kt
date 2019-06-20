package com.mosl.saathi.api.model.leadprofile

import com.google.gson.annotations.SerializedName

data class ProfileSubParameterListItem(

  @JvmField @SerializedName("Status")
  val status: Int?,

  @JvmField @SerializedName("IsHide")
  val isHide: String?,

  @JvmField @SerializedName("LeadProfileParameterID")
  val leadProfileParameterID: Int?,

  @JvmField @SerializedName("QuestionSubText")
  val questionSubText: String?,

  @JvmField @SerializedName("IsSelected")
  val isSelected: Boolean?,

  @JvmField @SerializedName("ExtraInfo")
  val extraInfo: String?,

  @JvmField @SerializedName("Sequence")
  val sequence: Int?,

  @JvmField @SerializedName("ExtraInfoFieldName")
  val extraInfoFieldName: String?,

  @JvmField @SerializedName("ExtraInfoRowCount")
  val extraInfoRowCount: Int?,

  @JvmField @SerializedName("InApproval")
  val inApproval: Boolean?,

  @JvmField @SerializedName("ProfileSubParameterID")
  val profileSubParameterID: Int?,

  @JvmField @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any?,

  @JvmField @SerializedName("Value")
  val value: String?,

  @JvmField @SerializedName("HideDepedentQuestion")
  val hideDepedentQuestion: String?,

  @JvmField @SerializedName("Permissions")
  val permissions: Any?,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean?,

  @JvmField @SerializedName("ProfileParameterID")
  val profileParameterID: Int?
)