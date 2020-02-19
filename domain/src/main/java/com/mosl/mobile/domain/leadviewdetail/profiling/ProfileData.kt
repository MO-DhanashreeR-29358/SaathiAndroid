package com.mosl.mobile.domain.leadviewdetail.profiling

data class ProfileRequestData(
  @JvmField val profileParameterID: String,
  @JvmField val profileSubParameterID: String,
  @JvmField val leadID: String,
  @JvmField val value: String
)

data class LeadProfile(
  @JvmField val groupName: String?,
  @JvmField val status: Int?,
  @JvmField val inApproval: Boolean,
  @JvmField val active: Boolean?,
  @JvmField val profileParameterGroupID: Int,
  @JvmField val businessUnitId: Int,
  @JvmField val approvalFieldList: Any?,
  @JvmField val isAdminMode: Boolean,
  @JvmField val profileParameterList: List<ProfileParameter>?,
  @JvmField val sysname: String?,
  @JvmField val profileParameterId: String?
)

data class ProfileParameter(
  @JvmField val status: Int?,
  @JvmField val questionType: Int?,
  @JvmField val selectedProfileSubParameterID: Int?,
  @JvmField val sequence: Int?,
  @JvmField val inApproval: Boolean?,
  @JvmField val profileSubParameterList: List<ProfileSubParameter>?,
  @JvmField val isDefaultHidden: String?,
  @JvmField val profileParameterGroupID: Int,
  @JvmField val approvalFieldList: Any?,
  @JvmField val value: Any?,
  @JvmField val permissions: Any?,
  @JvmField val isAdminMode: Boolean?,
  @JvmField val controlType: Int,
  @JvmField val profileSubParameterValueModel: List<Any>?,
  @JvmField val questionText: String?,
  @JvmField val profileParameterID: Int?,
  @JvmField val sysname: String?
)

data class ProfileSubParameter(
  @JvmField val status: Int?,
  @JvmField val isHide: String?,
  @JvmField val leadProfileParameterID: Int?,
  @JvmField val questionSubText: String?,
  @JvmField val isSelected: Boolean?,
  @JvmField val extraInfo: String?,
  @JvmField val sequence: Int?,
  @JvmField val extraInfoFieldName: String?,
  @JvmField val extraInfoRowCount: Int?,
  @JvmField val inApproval: Boolean?,
  @JvmField val profileSubParameterID: Int?,
  @JvmField val approvalFieldList: Any?,
  @JvmField val value: String?,
  @JvmField val hideDepedentQuestion: String?,
  @JvmField val permissions: Any?,
  @JvmField val isAdminMode: Boolean?,
  @JvmField val profileParameterID: Int?
)