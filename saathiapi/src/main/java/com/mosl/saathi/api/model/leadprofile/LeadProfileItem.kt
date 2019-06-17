package com.mosl.saathi.api.model.leadprofile

import com.google.gson.annotations.SerializedName

data class LeadProfileItem(

  @JvmField @SerializedName("GroupName")
  val groupName: String?,

  @JvmField @SerializedName("Status")
  val status: Int?,

  @JvmField @SerializedName("InApproval")
  val inApproval: Boolean?,

  @JvmField @SerializedName("Active")
  val active: Boolean?,

  @JvmField @SerializedName("ProfileParameterGroupID")
  val profileParameterGroupID: Int?,

  @JvmField @SerializedName("BusinessUnitId")
  val businessUnitId: Int?,

  @JvmField @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any?,

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean?,

  @JvmField @SerializedName("ProfileParameterList")
  val profileParameterList: List<ProfileParameterListItem>?,

  @JvmField @SerializedName("Sysname")
  val sysname: String?
)