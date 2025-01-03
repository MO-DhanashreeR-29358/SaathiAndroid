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
  @SerializedName("ObjectData"  ) var ObjectData  : ObjectData?            = null,
  @SerializedName("Errors"      ) var Errors      : String?           = null,
  @SerializedName("Total"       ) var Total       : String?              = null,
  @SerializedName("Permissions" ) var Permissions : PermissionModel?

)
data class ObjectData (

  @SerializedName("graph"             ) var graph             : ArrayList<Graph> = arrayListOf(),
  @SerializedName("Target"            ) var strTarget            : String?              = null,
  @SerializedName("Achieved"          ) var strAchieved          : String?              = null,
  @SerializedName("target"            ) var target2            : String?              = null,
  @SerializedName("IsAdminMode"       ) var IsAdminMode       : Boolean?          = null,
  @SerializedName("Status"            ) var Status            : Int?              = null,
  @SerializedName("InApproval"        ) var InApproval        : Boolean?          = null,
  @SerializedName("ApprovalFieldList" ) var ApprovalFieldList : String?           = null,
  @SerializedName("CreatedBy"         ) var CreatedBy         : String?           = null,
  @SerializedName("CreationDate"      ) var CreationDate      : String?           = null

)

data class Graph (

  @SerializedName("product"           ) var product           : String?  = null,
  @SerializedName("Value"             ) var Value             : String?  = null,
  @SerializedName("Parameter"         ) var Parameter         : String?  = null,
  @SerializedName("Val"               ) var Val               : Int?     = null,
  @SerializedName("Revenue"           ) var Revenue           : Int?     = null,
  @SerializedName("CurrentMonth"      ) var CurrentMonth      : Int?     = null,
  @SerializedName("LastMonth"         ) var LastMonth         : Int?     = null,
  @SerializedName("Series"            ) var Series            : String?  = null,
  @SerializedName("Category"          ) var Category          : String?  = null,
  @SerializedName("Valuedata"         ) var Valuedata         : Int?     = null,
  @SerializedName("IsAdminMode"       ) var IsAdminMode       : Boolean? = null,
  @SerializedName("Status"            ) var Status            : Int?     = null,
  @SerializedName("InApproval"        ) var InApproval        : Boolean? = null,
  @SerializedName("ApprovalFieldList" ) var ApprovalFieldList : String?  = null,
  @SerializedName("CreatedBy"         ) var CreatedBy         : String?  = null,
  @SerializedName("CreationDate"      ) var CreationDate      : String?  = null

)