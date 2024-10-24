package com.mosl.saathi.api.model.leadprofile

import com.google.gson.annotations.SerializedName

data class ProfileParameterListItem(

  @JvmField @SerializedName("Status")
  val status: Int?,

  @JvmField @SerializedName("QuestionType")
  val questionType: Int?,

  @JvmField @SerializedName("SelectedProfileSubParameterID")
  val selectedProfileSubParameterID: Int?,

  @JvmField @SerializedName("Sequence")
  val sequence: Int?,

  @JvmField @SerializedName("InApproval")
  val inApproval: Boolean?,

  @JvmField @SerializedName("ProfileSubParameterList")
  val profileSubParameterList: List<ProfileSubParameterListItem>?,

  @JvmField @SerializedName("IsDefaultHidden")
  val isDefaultHidden: String?,

  @JvmField @SerializedName("ProfileParameterGroupID")
  val profileParameterGroupID: Int,

  @JvmField @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any?,

  @JvmField @SerializedName("Value")
  val value: Any?,

  @JvmField @SerializedName("Permissions")
  val permissions: Any?,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean?,

  @JvmField @SerializedName("ControlType")
  val controlType: Int,

  @JvmField @SerializedName("ProfileSubParameterValueModel")
  val profileSubParameterValueModel: List<Any>?,

  @JvmField @SerializedName("QuestionText")
  val questionText: String?,

  @JvmField @SerializedName("ProfileParameterID")
  val profileParameterID: Int?,

  @JvmField @SerializedName("Sysname")
  val sysname: String?,

  @JvmField @SerializedName("IsRequired")
  val isRequired: Int?
)