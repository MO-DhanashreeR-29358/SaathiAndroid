package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName
import com.mosl.saathi.api.model.common.ApiError
import com.mosl.saathi.api.model.common.PermissionModel

data class IndirectExtraDataCommonResponse(
  @SerializedName("Response" ) var Response : ExtraDataInner?         ,
  @SerializedName("Type"     ) var Type     : String?           = null,
  @SerializedName("Message"  ) var Message  : String?           = null,
  @SerializedName("Status"   ) var Status   : Boolean?          = null,
  @SerializedName("Erros"    ) var Erros    : List<ApiError>
)

data class ExtraDataInner(
  @SerializedName("ExtraData"   ) var ExtraData   : Any?          = null,
  @SerializedName("Data"        ) var Data        : ArrayList<Any?> = arrayListOf(),
  @SerializedName("ObjectData"  ) var ObjectData  : String?           = null,
  @SerializedName("Errors"      ) var Errors      : String?           = null,
  @SerializedName("Total"       ) var Total       : String?              = null,
  @SerializedName("Permissions" ) var Permissions : PermissionModel?

)
