package com.mosl.tabapp.bankmeeting.model

data class BankMeetingInput(
    @JvmField val meetingDate: String,
    @JvmField val meetingTypeId: String,
    @JvmField val meetingDurationId: String,
    @JvmField val meetingDescription: String,
    @JvmField val bankId: String,
    @JvmField val bankBranchId: String,
    @JvmField val subBrokerId: Int,
    @JvmField val disabledFields: Boolean,
    @JvmField val subject: String,
    @JvmField val isSubjectVisible: Boolean,
    @JvmField val noOfClient: String = "0",
    @JvmField val clientName: String,
    @JvmField val clientMobileNumber: String,
    @JvmField val isClientNameVisible: Boolean,
    @JvmField val bankEmployeeCode: String
)