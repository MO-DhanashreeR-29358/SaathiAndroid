package com.mosl.saathi.api.model.dashboard.reportassgin

import com.google.gson.annotations.SerializedName

data class GetLeadAssignStpItemResponse(

    @JvmField @SerializedName("Name")
    val name: String,

    @JvmField @SerializedName("ProspectId")
    val prospectId: String,

    @JvmField @SerializedName("RegistraionDate")
    val registrationDate: String,

    @JvmField @SerializedName("LastConnected")
    val lastConnected: String,

    @JvmField @SerializedName("Stage")
    val stage: String,

    @JvmField @SerializedName("Lead_Status")
    val leadStatus: String,

    @JvmField @SerializedName("TL")
    val teamLeader: String


)