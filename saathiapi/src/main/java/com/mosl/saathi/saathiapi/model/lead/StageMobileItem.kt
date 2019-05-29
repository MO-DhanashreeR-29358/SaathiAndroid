package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName

data class StageMobileItem(

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("StageName")
  @JvmField val stageName: String,

  @SerializedName("StageID")
  @JvmField val stageID: Int,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: Int,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("Permissions")
  @JvmField val permissions: Any,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("StageCode")
  @JvmField val stageCode: String,

  @SerializedName("ShowPotentialPopup")
  @JvmField val showPotentialPopup: String
)