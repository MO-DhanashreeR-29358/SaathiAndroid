package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class MeetingSaveRequest(
//  @JvmField @SerializedName("BankMeetingId") val bankMeetingId: String,
    @JvmField @SerializedName("MeetingTypeId") val meetingTypeId: String,
    @JvmField @SerializedName("MeetingDate") val meetingDate: String,
    @JvmField @SerializedName("MeetingDurationId") val meetingDurationId: Int,
    @JvmField @SerializedName("MeetingDescription") val meetingDescription: String,
    @JvmField @SerializedName("BankId") val bankId: Int,
    @JvmField @SerializedName("BankBranchId") val bankBranchId: Int,
    @JvmField @SerializedName("SubBrokerId") val subBrokerId: Int,
    @JvmField @SerializedName("Subject") val subject: String,
    @JvmField @SerializedName("ClientName") val clientName: String,
    @JvmField @SerializedName("BankEmployeeCode") val bankEmployeeCode: String,
    @JvmField @SerializedName("MobileNumber") val clientMobileNumber: String,
    @JvmField @SerializedName("BankBranchIDwithIFSC") val bankBranchIdWithIFSC: String,
    @JvmField @SerializedName("OtherBranchName") val otherBranchName: String,
    @JvmField @SerializedName("BankLocationName") val bankLocationName: String
    //@JvmField @SerializedName("BankLocationCode") val bankLocationCode: Int
)