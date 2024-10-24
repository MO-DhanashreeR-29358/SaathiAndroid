package com.mosl.mobile.domain.leadviewdetail.profiling

//profileNewRequestList
data class ProfileRequestData(
  @JvmField val profileParameterID: String,
  @JvmField val profileSubParameterID: String,
  @JvmField val leadID: String,
  @JvmField val value: String
)

data class LeadProfile(
  @JvmField val groupName: String?,
  @JvmField val inApproval: Boolean,
  @JvmField val profileParameterGroupID: Int,
  @JvmField val businessUnitId: Int,
  @JvmField val isAdminMode: Boolean,
  @JvmField val profileParameterList: MutableList<ProfileParameter>?,
  @JvmField val profileParameterId: String?,
)

data class ProfileParameter(
  @JvmField val questionType: Int?,
  @JvmField val selectedProfileSubParameterID: Int?,
  @JvmField val inApproval: Boolean?,
  @JvmField val profileSubParameterList: MutableList<ProfileSubParameter>?,
  @JvmField var isDefaultHidden: String?,
  @JvmField val profileParameterGroupID: Int,
  @JvmField val isAdminMode: Boolean?,
  @JvmField val controlType: Int,
  @JvmField val questionText: String?,
  @JvmField val profileParameterID: Int?,
  @JvmField val status: Int?,
  @JvmField var isRequired: Int?

  )

data class ProfileSubParameter(
  @JvmField val isHide: String?,
  @JvmField val questionSubText: String?,
  @JvmField var isSelected: Boolean?,
  @JvmField val extraInfo: String?,
  @JvmField val extraInfoFieldName: String?,
  @JvmField val extraInfoRowCount: Int?,
  @JvmField val inApproval: Boolean?,
  @JvmField val profileSubParameterID: Int?,
  @JvmField val value: String?,
  @JvmField val hideDependentQuestion: String?,
  @JvmField val isAdminMode: Boolean?
)