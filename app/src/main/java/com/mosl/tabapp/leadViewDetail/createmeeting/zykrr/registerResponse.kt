package com.mosl.tabapp.leadViewDetail.createmeeting.zykrr

import com.google.gson.annotations.SerializedName

data class registerResponse(

@SerializedName("surveyLinks"       ) var surveyLinks       : ArrayList<String> = arrayListOf(),
@SerializedName("participantListId" ) var participantListId : String?           = null,
@SerializedName("scheduleId"        ) var scheduleId        : String?           = null

)

