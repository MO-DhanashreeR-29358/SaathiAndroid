package com.mosl.saathi.api.model.leadviewhistory

import com.google.gson.annotations.SerializedName

data class IndirectLeadHistoryResponse(
    @SerializedName("BusinessUnit"          ) var BusinessUnit          : String?  = null,
    @SerializedName("Name"                  ) var Name                  : String?  = null,
    @SerializedName("Mobile"                ) var Mobile                : String?  = null,
    @SerializedName("EmailId"               ) var EmailId               : String?  = null,
    @SerializedName("Branchcode"            ) var Branchcode            : String?  = null,
    @SerializedName("Profile"               ) var Profile               : String?  = null,
    @SerializedName("LeadOwner"             ) var LeadOwner             : String?  = null,
    @SerializedName("Comments"              ) var Comments              : String?  = null,
    @SerializedName("Action"                ) var Action                : String?  = null,
    @SerializedName("ActionDate"            ) var ActionDate            : String?  = null,
    @SerializedName("ActionUserName"        ) var ActionUserName        : String?  = null,

)
