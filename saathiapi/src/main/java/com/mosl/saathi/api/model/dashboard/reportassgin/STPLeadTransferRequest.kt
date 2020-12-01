package com.mosl.saathi.api.model.dashboard.reportassgin

import com.google.gson.annotations.SerializedName

data class STPLeadTransferRequest(

    @JvmField @SerializedName("ProspectIDList")
    val prospectIds: String,

    @JvmField @SerializedName("AssignTo_transferLead")
    val assignToTransferLead: String,

    @JvmField @SerializedName("checkAllFlag")
    val checkAllFlag: Int
)