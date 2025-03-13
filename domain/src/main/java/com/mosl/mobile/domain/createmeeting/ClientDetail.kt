package com.mosl.mobile.domain.createmeeting

data class ClientDetail(
  @JvmField val leadId: Int,
  @JvmField val currentStage: String,
  @JvmField val currentStageSince: String,
  @JvmField val name: String,
  @JvmField val branchcode: String,
  @JvmField val clientcode: String,
  @JvmField val isOnlyClient: Boolean,
  @JvmField val isPotentialPMSProduct: String,
  @JvmField val sourceName: String,
  @JvmField val prospectSource: String,
  @JvmField val activateOTPInApp: String,
  @JvmField val leadCreationDate: String
)

data class LookUp(
  @JvmField val lookUpID: Int,
  @JvmField val lookUpName: String
) {
  override fun toString(): String {
    return lookUpName
  }

  fun isPersonalMeeting(): Boolean {
    return lookUpName.equals("Personal Meeting")
  }

  fun isLocationProper(): Boolean {
    return !lookUpName.equals("Other", ignoreCase = true)
  }

  fun isCustomerService(): Boolean {
    return lookUpName.equals("Customer Service Related", ignoreCase = true)
  }
}

data class Attendee(
  @JvmField val empName: String,
  @JvmField val empCode: String,
  @JvmField val branchCode: String,
  @JvmField val hasData: Boolean
)

data class AddedAttendee(
  @JvmField val attendeeName: String,
  @JvmField val attendeeCode: String,
  @JvmField val attendeeType: String,
  @JvmField val attended: Boolean = true
) {
  fun isAttendee(): Boolean {
    return attendeeType.equals("R")
  }
}

data class ClientData(
  @JvmField val clientDetail: ClientDetail,
  @JvmField val imageUrl: String,
  @JvmField val listActivity: List<LookUp>,
  @JvmField val listMobileNumber: List<MobileListData>,
  @JvmField val listMeetingDuration: List<LookUp>,
  @JvmField val listInterestedIn: List<LookUp>,
  @JvmField val listMeetingObjective: List<LookUp>,
  @JvmField val listTravellingMode: List<LookUp>,
  @JvmField val listMeetingAdressID: List<LookUp>
)


data class OTPRequestData(
  @JvmField val latitude: String,
  @JvmField val longitude: String,
  @JvmField val geoArea: String,
  @JvmField val ucid: String,
  @JvmField val leadid: String,
  @JvmField val mobileNumber: String

)

data class SurveyLinkData(
  @JvmField val leadId: String,
  @JvmField val clientCode: String,
  @JvmField val meetingId: Int,
  @JvmField val rmCode: String,
  @JvmField val mobileNumber: String,
  @JvmField val surveyLink: String
)

data class AddUpdateMeeting(
  @JvmField val leadId: Int,
  @JvmField val ucidId: Int,
  @JvmField val meetingId: Int
)

data class SendOtpResponse(
  @JvmField val status: Boolean,
  @JvmField val errorMessage: String
)

data class CommanResponse(
  @JvmField val response: String?,
  @JvmField val status: Boolean,
  @JvmField val errorMessage: String
)

data class ChipsEvent(
  @JvmField val eventId: String,
  @JvmField val eventName: String
) {
  override fun toString(): String {
    return eventName
  }

}