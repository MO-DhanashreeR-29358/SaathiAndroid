package com.mosl.app.core.data.leadviewdetail.profiling

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileParameter
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileRequestData
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileSubParameter
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfilingResponseModel
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.leadprofile.LeadProfileItem
import com.mosl.saathi.api.model.leadprofile.ProfileParameterListItem
import com.mosl.saathi.api.model.leadprofile.ProfileSubParameterListItem
import com.mosl.saathi.api.model.leadprofile.SaveLeadProfileRequest

fun List<LeadProfileItem>.transform(): List<LeadProfile> {
  return safeMap {
    it.mapToLeadProfile()
  }
}

fun SaathiCommonResponse<String>.responseTransform(): ProfilingResponseModel {
  val profilingResponseModel = ProfilingResponseModel(
    response = response,
    message = message,
    status = status
  )

  return profilingResponseModel
}

private fun LeadProfileItem.mapToLeadProfile(): LeadProfile {

  val profileParameterList = profileParameterList?.mapToProfileParameterList() ?: emptyList()

  return LeadProfile(
    groupName = groupName,
    inApproval = inApproval,
    profileParameterGroupID = profileParameterGroupID,
    businessUnitId = businessUnitId,
    isAdminMode = isAdminMode,
    profileParameterList = profileParameterList as MutableList<ProfileParameter>,
    profileParameterId = profileParameterId
  )
}

private fun List<ProfileParameterListItem>.mapToProfileParameterList(): List<ProfileParameter> {
  return safeMap {
    it.mapToProfileParameter()
  }
}

private fun ProfileParameterListItem.mapToProfileParameter(): ProfileParameter {
  val profileSubParameterList =
    profileSubParameterList?.mapToProfileSubParameterList() ?: emptyList()

  return ProfileParameter(
    questionType = questionType,
    selectedProfileSubParameterID = selectedProfileSubParameterID,
    inApproval = inApproval,
    profileSubParameterList = profileSubParameterList as MutableList<ProfileSubParameter>,
    isDefaultHidden = isDefaultHidden,
    profileParameterGroupID = profileParameterGroupID,
    isAdminMode = isAdminMode,
    controlType = controlType,
    questionText = questionText,
    profileParameterID = profileParameterID,
    status = status,
    isRequired = isRequired
  )
}

private fun List<ProfileSubParameterListItem>.mapToProfileSubParameterList(): List<ProfileSubParameter> {
  return safeMap {
    it.mapToProfileSubParameter()
  }
}

private fun ProfileSubParameterListItem.mapToProfileSubParameter(): ProfileSubParameter {
  return ProfileSubParameter(
    isHide = isHide,
    questionSubText = questionSubText,
    isSelected = isSelected,
    extraInfo = extraInfo,
    extraInfoFieldName = extraInfoFieldName,
    extraInfoRowCount = extraInfoRowCount,
    inApproval = inApproval,
    profileSubParameterID = profileSubParameterID,
    value = value,
    hideDependentQuestion = hideDepedentQuestion,
    isAdminMode = isAdminMode
  )
}

fun List<ProfileRequestData>.mapToSaveLeadProfileRequestList(): List<SaveLeadProfileRequest> {
  return safeMap {
    it.mapToSaveLeadProfileRequest()
  }
}

private fun ProfileRequestData.mapToSaveLeadProfileRequest(): SaveLeadProfileRequest {
  return SaveLeadProfileRequest(
    profileParameterID = profileParameterID,
    profileSubParameterID = profileSubParameterID,
    leadID = leadID,
    value = value
  )
}