package com.example.saathiapi.model.reports.report

import com.google.gson.annotations.SerializedName

data class ResponseItem(

  @SerializedName("Status")
  val status: Int,

  @SerializedName("BusinessProfileId")
  val businessProfileId: Int,

  @SerializedName("RegionCode")
  val regionCode: Any,

  @SerializedName("BusinessUnitId")
  val businessUnitId: Int,

  @SerializedName("ChannelIds")
  val channelIds: Any,

  @SerializedName("InApproval")
  val inApproval: Boolean,

  @SerializedName("Active")
  val active: Boolean,

  @SerializedName("StatusStr")
  val statusStr: Any,

  @SerializedName("IsSystemBusinessProfile")
  val isSystemBusinessProfile: Boolean,

  @SerializedName("ApprovalFieldList")
  val approvalFieldList: Any,

  @SerializedName("IsAdminMode")
  val isAdminMode: Boolean,

  @SerializedName("ProspectSourceName")
  val prospectSourceName: Any,

  @SerializedName("BusinessUnitName")
  val businessUnitName: Any,

  @SerializedName("ProspectSourceID")
  val prospectSourceID: Int,

  @SerializedName("BranchCode")
  val branchCode: String,

  @SerializedName("BusinessProfileName")
  val businessProfileName: Any
)