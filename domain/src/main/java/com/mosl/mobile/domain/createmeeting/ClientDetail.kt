package com.mosl.mobile.domain.createmeeting

data class ClientDetail(
  @JvmField val leadId: Int,
  @JvmField val currentStage: String,
  @JvmField val currentStageSince: String,
  @JvmField val name: String,
  @JvmField val branchcode: String,
  @JvmField val clientcode: String
)

data class LookUp(
  @JvmField val lookUpID: Int,
  @JvmField val lookUpName: String
) {
  override fun toString(): String {
    return lookUpName
  }
}

data class ClientData(
  @JvmField val clientDetail: ClientDetail,
  @JvmField val imageUrl: String,
  @JvmField val listActivity: List<LookUp>,
  @JvmField val listMeetingDuration: List<LookUp>,
  @JvmField val listInterestedIn: List<LookUp>,
  @JvmField val listMeetingObjective: List<LookUp>,
  @JvmField val listTravellingMode: List<LookUp>,
  @JvmField val listMeetingAdressID: List<LookUp>
)