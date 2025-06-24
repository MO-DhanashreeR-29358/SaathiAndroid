package com.mosl.tabapp.leadViewDetail.createmeeting.zykrr

import com.google.gson.annotations.SerializedName

data class registerResponse(

@SerializedName("shortSMSLinks"       ) var surveyLinks       : ArrayList<String> = arrayListOf(),
@SerializedName("participantListId" ) var participantListId : String?           = null,
@SerializedName("scheduleId"        ) var scheduleId        : String?           = null

//{"participantListId":"6fcc8322-aa44-4bba-8a37-df81f9923356","surveyLinks":["https://survey.zykrr.com/MOSLIN/t/69opp1PP"],"shortLinks":["https://zykrr.io/69opp1PP"],"shortSMSLinks":["https://zykrr.io/MOSLIN/69opp1PP"],"failedRecords":[]}
)

