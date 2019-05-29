package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StateProvinceItem(

  @SerializedName("CountryId")
  @JvmField val countryId: Int,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("Active")
  @JvmField val active: Boolean,

  @SerializedName("StatusStr")
  @JvmField val statusStr: Any,

  @SerializedName("CountryName")
  @JvmField val countryName: Any,

  @SerializedName("DbStatus")
  @JvmField val dbStatus: Int,

  @SerializedName("StateName")
  @JvmField val stateName: String,

  @SerializedName("ApprovalFieldList")
  @JvmField val approvalFieldList: Any,

  @SerializedName("StateId")
  @JvmField val stateId: Int,

  @SerializedName("IsAdminMode")
  @JvmField val isAdminMode: Boolean
)