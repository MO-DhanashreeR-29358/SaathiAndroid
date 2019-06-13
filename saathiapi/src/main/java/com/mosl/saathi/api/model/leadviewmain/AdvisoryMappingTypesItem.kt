package com.mosl.saathi.api.model.leadviewmain

import com.google.gson.annotations.SerializedName

data class AdvisoryMappingTypesItem(

  @JvmField @SerializedName("ParentId") val parentId: Int,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("Active") val active: Boolean,

  @JvmField @SerializedName("LookupType") val lookupType: Int,

  @JvmField @SerializedName("EmployeeCode") val employeeCode: Any,

  @JvmField @SerializedName("LookUpid") val lookUpid: Int,

  @JvmField @SerializedName("LookUpName") val lookUpName: String,

  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: Int,

  @JvmField @SerializedName("Sequence") val sequence: Int,

  @JvmField @SerializedName("LookupTypeId") val lookupTypeId: Int
)