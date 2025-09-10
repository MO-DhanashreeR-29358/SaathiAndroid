package com.mosl.saathi.api.model.leadviewhistory

import com.google.gson.annotations.SerializedName

data class IndirectLeadMappingHistoryResponse(
    @SerializedName("Product"          ) var Product          : String?  = null,
    @SerializedName("Stage"                  ) var Stage                  : String?  = null,
    @SerializedName("StatusName"                ) var Status                : String?  = null,
    @SerializedName("PrimaryRM"               ) var PrimaryRM               : String?  = null,
    @SerializedName("PrimaryRMName"               ) var PrimaryRMname              : String?  = null,
    @SerializedName("SecondaryRM"            ) var SecondaryRM            : String?  = null,
    @SerializedName("SecondaryRMName"            ) var SecondaryRMname            : String?  = null,
    @SerializedName("AssignedBy"               ) var AssignedBy               : String?  = null,
    @SerializedName("AssignedByName"               ) var AssignedByName              : String?  = null,
    @SerializedName("AssignedByDate"             ) var AssignedDate             : String?  = null,
    @SerializedName("Action"              ) var Action              : String?  = null,
    @SerializedName("ActionUser"        ) var ActionUser        : String?  = null,

    @SerializedName("ActionUserName"        ) var ActionUserName        : String?  = null,
    @SerializedName("ActionDate"            ) var ActionDate            : String?  = null

)
