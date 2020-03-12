package com.mosl.mobile.domain.teamtarget

data class TeamTargetInput(
  @JvmField val businessProfileId: String?,
  @JvmField val addtionalParameters: AditionalParametersTeamTarget?,
  @JvmField val widgetSystemName: String?,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)
