package com.mosl.mobile.domain.leadviewdetail.main

data class LeadDetail(
  @JvmField val id: String,
  @JvmField val branchCode: String,
  @JvmField val personalDetail: PersonalDetail,
  @JvmField val createMeetingId: String,
  @JvmField val leadOwner: String,
  @JvmField val source: String,
  @JvmField val prospectSource: String,
  @JvmField val prospectSourceDetail: String,
  @JvmField val oaoUserId: String,
  @JvmField val diyCode: String,
  @JvmField val diyLink: String,
  @JvmField val leadProducts: List<LeadProduct>
)

data class PersonalDetail(
  @JvmField val name: String,
  @JvmField val ucId: String,
  @JvmField val mobile: String,
  @JvmField val email: String
)

data class LeadProduct(
  @JvmField val clientCode: String,
  @JvmField val stageName: String,
  @JvmField val isDefault: String,
  @JvmField val pendingDays: String,
  @JvmField val interestedIn: String
)