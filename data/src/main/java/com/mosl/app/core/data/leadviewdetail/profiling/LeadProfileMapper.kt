package com.mosl.app.core.data.leadviewdetail.profiling

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileParameter
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileRequestData
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileSubParameter
import com.mosl.saathi.api.model.leadprofile.LeadProfileItem
import com.mosl.saathi.api.model.leadprofile.ProfileParameterListItem
import com.mosl.saathi.api.model.leadprofile.ProfileSubParameterListItem
import com.mosl.saathi.api.model.leadprofile.SaveLeadProfileRequest

fun List<LeadProfileItem>.transform(): List<LeadProfile> {
  return safeMap {
    it.mapToLeadProfile()
  }
}

private fun LeadProfileItem.mapToLeadProfile(): LeadProfile {

  val profileParameterList = profileParameterList?.mapToProfileParameterList() ?: emptyList()

  return LeadProfile(
      groupName = groupName,
      status = status,
      inApproval = inApproval,
      active = active,
      profileParameterGroupID = profileParameterGroupID,
      businessUnitId = businessUnitId,
      approvalFieldList = approvalFieldList,
      isAdminMode = isAdminMode,
      profileParameterList = profileParameterList,
      sysname = sysname,
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
    profileSubParameterList?.mapToProfileSubParameterList() ?: emptyList<ProfileSubParameter>()

  return ProfileParameter(
      status = status,
      questionType = questionType,
      selectedProfileSubParameterID = selectedProfileSubParameterID,
      sequence = sequence,
      inApproval = inApproval,
      profileSubParameterList = profileSubParameterList,
      isDefaultHidden = isDefaultHidden,
      profileParameterGroupID = profileParameterGroupID,
      approvalFieldList = approvalFieldList,
      value = value,
      permissions = permissions,
      isAdminMode = isAdminMode,
      controlType = controlType,
      profileSubParameterValueModel = profileSubParameterValueModel,
      questionText = questionText,
      profileParameterID = profileParameterID,
      sysname = sysname
  )
}

private fun List<ProfileSubParameterListItem>.mapToProfileSubParameterList(): List<ProfileSubParameter> {
  return safeMap {
    it.mapToProfileSubParameter()
  }
}

private fun ProfileSubParameterListItem.mapToProfileSubParameter(): ProfileSubParameter {
  return ProfileSubParameter(
      status = status,
      isHide = isHide,
      leadProfileParameterID = leadProfileParameterID,
      questionSubText = questionSubText,
      isSelected = isSelected,
      extraInfo = extraInfo,
      sequence = sequence,
      extraInfoFieldName = extraInfoFieldName,
      extraInfoRowCount = extraInfoRowCount,
      inApproval = inApproval,
      profileSubParameterID = profileSubParameterID,
      approvalFieldList = approvalFieldList,
      value = value,
      hideDepedentQuestion = hideDepedentQuestion,
      permissions = permissions,
      isAdminMode = isAdminMode,
      profileParameterID = profileParameterID
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