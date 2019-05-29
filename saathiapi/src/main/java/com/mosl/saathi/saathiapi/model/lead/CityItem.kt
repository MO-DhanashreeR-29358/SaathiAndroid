package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName

data class CityItem(

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("Active")
  @JvmField val active: Boolean,

  @SerializedName("StatusStr")
  @JvmField val statusStr: Any,

  @SerializedName("CityId")
  @JvmField val cityId: Int,

  @SerializedName("DbStatus")
  @JvmField val dbStatus: Int,

  @SerializedName("StateName")
  @JvmField val stateName: Any,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("StateId")
  @JvmField val stateId: Int,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean,

  @SerializedName("CityName")
  @JvmField val cityName: String
)