package com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data

data class IndirectLeadDetailMain(
  @JvmField val id: String,
  @JvmField val branchCode: String,
  @JvmField val personalDetail: IndirectPersonalDetail,
  @JvmField val createMeetingId: Int,
  @JvmField val leadOwner: String,
  @JvmField val leadName: String,
  @JvmField val source: String,
  @JvmField val prospectSource: String,
  @JvmField val prospectSourceDetail: String,
  @JvmField val oaoUserId: Int,
  @JvmField val clientType: String,
  @JvmField val leadProducts: List<IndirectLeadProduct>
)

data class IndirectPersonalDetail(
  @JvmField val name: String,
  @JvmField val ucId: String,
  @JvmField val mobile: String,
  @JvmField val email: String
)

data class IndirectLeadProduct(
  @JvmField val clientCode: String,
  @JvmField val stageName: String,
  @JvmField val isDefault: Boolean,
  @JvmField val pendingDays: String,
  @JvmField val interestedIn: String
)

data class ClientDetail(
  @JvmField val name: String,
  @JvmField val id: String,
  @JvmField val currentStage: String,
  @JvmField val currentStageSince: String,
  @JvmField val clientCode: String,
  @JvmField val branchCode: String,
  @JvmField val mobile: String,
  @JvmField val oaoUserId: Int
)

data class ClientDetailAndImage(
  @JvmField val clientDetail: ClientDetail,
  @JvmField val image: String
)