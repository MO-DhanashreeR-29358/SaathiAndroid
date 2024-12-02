package com.mosl.tabapp.leadViewDetail.createmeeting.zykrr

import com.google.gson.annotations.SerializedName

data class registerRequest(
    @SerializedName("data"                    ) var data                    : ArrayList<MeetingFeedbackData> = arrayListOf(),
    @SerializedName("templateId"              ) var templateId              : String?         = null,
    @SerializedName("scheduleDateTime"        ) var scheduleDateTime        : String?         = null,
    @SerializedName("scheduleRuleSetId"       ) var scheduleRuleSetId       : String?         = null,
    @SerializedName("autoCreateNewMCQOptions" ) var autoCreateNewMCQOptions : Boolean?        = null,
    @SerializedName("participantListName"     ) var participantListName     : String?         = null
)
data class MeetingFeedbackData (

    @SerializedName("participantInfo" ) var participantInfo : ParticipantInfo? = ParticipantInfo(),
    @SerializedName("transactionInfo" ) var transactionInfo : TransactionInfo? = TransactionInfo()

)
data class ParticipantInfo (

    @SerializedName("Name"       ) var Name       : String? = null,
    @SerializedName("Email"      ) var Email      : String? = null,
    @SerializedName("Phone"      ) var Phone      : String? = null,
    @SerializedName("CustomerId" ) var CustomerId : String? = null

)


data class TransactionInfo (

@SerializedName("d8827b50-86b9-4914-8262-8ba7345e77ef" ) var d8827b50_86b9_4914_8262_8ba7345e77ef : ArrayList<String> = arrayListOf(),
@SerializedName("1c57c42e-8546-479f-84ba-95c7dd62947e" ) var c57c42e_8546_479f_84ba_95c7dd62947e : String?           = null,
@SerializedName("85d93fd9-7966-42cf-be01-b824328e66ea" ) var d93fd9_7966_42cf_be01_b824328e66ea : ArrayList<String> = arrayListOf()

)