package com.mosl.saathi.api.model.clientdetail

import com.google.gson.annotations.SerializedName

data class AdvisoryMappingTypesItem(

  @SerializedName("ParentId")
  @JvmField val parentId: Int,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("Active")
  @JvmField val active: Boolean,

  @SerializedName("LookupType")
  @JvmField val lookupType: Int,

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: Any,

  @SerializedName("LookUpid")
  @JvmField val lookUpid: Int,

  @SerializedName("LookUpName")
  @JvmField val lookUpName: String,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: Int,

  @SerializedName("Sequence")
  @JvmField val sequence: Int,

  @SerializedName("LookupTypeId")
  @JvmField val lookupTypeId: Int
)