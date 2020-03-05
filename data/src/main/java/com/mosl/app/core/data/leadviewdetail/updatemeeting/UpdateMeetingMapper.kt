package com.mosl.app.core.data.leadviewdetail.updatemeeting

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Lookup
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.MeetingAttendee
import com.mosl.saathi.api.model.createmeeting.MeetingAttendeeItem
import com.mosl.saathi.api.model.createmeeting.MeetingByIdItem
import com.mosl.saathi.api.model.lead.LookupItem

fun MeetingByIdItem.transform(): Meeting {

  val meetingAttendeeList = meetingAttendee.mapToAttendeeList()

  return Meeting(
      parentID = parentID,
      products = products,
      meetingReasonID = meetingReasonID,
      geoLongitude = geoLongitude,
      subCategoryID = subCategoryID,
      geoTagStatus = geoTagStatus,
      meetingObjectiveID = meetingObjectiveID,
      uCID = uCID,
      name = name,
      isAdminMode = isAdminMode,
      categoryID = categoryID,
      repeatEveryDayCode = repeatEveryDayCode,
      status = status,
      repeatationStartOn = repeatationStartOn,
      subCategoryDescription = subCategoryDescription,
      createdBy = createdBy,
      meetingAttendeeList = meetingAttendeeList,
      geoLatitude = geoLatitude,
      activityTypeID = activityTypeID,
      businessUnitId = businessUnitId,
      outstationMeetingID = outstationMeetingID,
      bAClientMeetingID = bAClientMeetingID,
      meetingDurationID = meetingDurationID,
      repeatationEndsOn = repeatationEndsOn,
      geoArea = geoArea,
      deviceType = deviceType,
      meetingType = meetingType,
      categoryDescription = categoryDescription,
      finalAction = finalAction,
      pincode = pincode,
      repeatEveryWeekCode = repeatEveryWeekCode,
      meetingWith = meetingWith,
      attendeeName = attendeeName,
      productTypeID = productTypeID,
      pIDTxnMeetingProduct = pIDTxnMeetingProduct,
      meetingDate = meetingDate,
      clientcode = clientcode,
      productCategoryID = productCategoryID,
      meetingAddress = meetingAddress,
      meetingID = meetingID,
      actionableID = actionableID,
      leadId = leadId,
      meetingDescription = meetingDescription,
      attendeeCode = attendeeCode,
      isSubcategoryData = isSubcategoryData,
      clientname = clientname,
      amount = amount,
      travellingModeIds = travellingModeIds,
      meetingAddressTypeId = meetingAddressTypeId,
      inApproval = inApproval,
      approvalFieldList = approvalFieldList,
      customerRemark = customerRemark,
      isPotentialPMSProduct = isPotentialPMSProduct,
      repeatOnCode = repeatOnCode
  )

}

private fun List<MeetingAttendeeItem>.mapToAttendeeList(): List<MeetingAttendee> {
  return safeMap {
    it.mapToAttendee()
  }
}

private fun MeetingAttendeeItem.mapToAttendee(): MeetingAttendee {
  return MeetingAttendee(
      attendeeType = attendeeType,
      status = status,
      inApproval = inApproval,
      meetingAttendeeID = meetingAttendeeID,
      attendeeEmpcode = attendeeEmpcode,
      approvalFieldList = approvalFieldList,
      meetingID = meetingID,
      isAdminMode = isAdminMode,
      attendeeEmpName = attendeeEmpName,
      attended = attended,
      defaultAttendee = defaultAttendee
  )
}

fun List<LookupItem>.mapToLookupList(): List<Lookup> {
  return safeMap {
    it.mapToLookup()
  }
}

private fun LookupItem.mapToLookup(): Lookup {
  return Lookup(
      parentId = parentId,
      status = status,
      sysName = sysName,
      active = active,
      lookupType = lookupType,
      lookupId = lookUpid,
      lookUpName = lookUpName,
      businessUnitId = businessUnitId,
      sequence = sequence,
      ignoreBusinessUnit = ignoreBusinessUnit,
      lookupTypeId = lookupTypeId
  )
}