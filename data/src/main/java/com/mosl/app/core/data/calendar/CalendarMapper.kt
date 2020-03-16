package com.mosl.app.core.data.calendar

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.mobile.domain.calendar.CalenderRequestInput
import com.mosl.saathi.api.model.calender.CalenderItem
import com.mosl.saathi.api.model.calender.CalenderRequest

fun CalenderRequestInput.mapCalendarRequest(): CalenderRequest {
  return CalenderRequest(
    meetingDate = meetingDate,
    attendeeCode = attendeeCode,
    meetingType = meetingType
  )
}

fun List<CalenderItem>.calendarItemResponseTransform(): List<CalenderItemResponse> {
  return safeMap {
    CalenderItemResponse(
      parentID = it.parentID,
      products = it.products,
      product = it.product ?: "",
      meetingReason = it.meetingReason ?: "",
      meetingReasonID = it.meetingReasonID,
      geoLongitude = it.geoLongitude,
      subCategoryID = it.subCategoryID,
      geoTagStatus = it.geoTagStatus,
      meetingObjectiveID = it.meetingObjectiveID,
      uCID = it.uCID,
      name = it.attendeeName,
      email = it.email ?: "",
      mobNo = it.mobNo ?: "",
      isAdminMode = it.isAdminMode,
      categoryID = it.subCategoryID,
      repeatEveryDayCode = it.repeatEveryDayCode,
      status = it.status ?: "",
      statusName = it.statusName ?: "",
      repetitionStartOn = it.repeatationStartOn,
      subCategoryDescription = it.subCategoryDescription,
      createdBy = it.createdBy,
      meetingAttendee = it.meetingAttendee,
      geoLatitude = it.geoLatitude,
      activityTypeID = it.activityTypeID,
      businessUnitId = it.businessUnitId,
      outstationMeetingID = it.outstationMeetingID,
      bAClientMeetingID = it.bAClientMeetingID,
      meetingDurationID = it.meetingDurationID,
      repetitionEndsOn = it.repeatationEndsOn,
      geoArea = it.geoArea,
      deviceType = it.deviceType,
      meetingType = it.meetingType,
      categoryDescription = it.subCategoryDescription,
      finalAction = it.finalAction,
      pincode = it.pincode,
      repeatEveryWeekCode = it.repeatEveryWeekCode,
      meetingWith = it.meetingWith,
      attendeeEmp = it.attendeeEmp ?: "",
      attendeeName = it.attendeeName,
      productTypeID = it.productTypeID,
      pIDTxnMeetingProduct = it.pIDTxnMeetingProduct,
      meetingDate = it.meetingDate ?: "",
      clientcode = it.clientcode,
      productCategoryID = it.productCategoryID,
      meetingAddress = it.meetingAddress,
      meetingID = it.meetingID ?: "",
      meetingDuration = it.actionable ?: "",
      actionable = it.actionable ?: "",
      leadId = it.leadId ?: "",
      comments = it.comments ?: "",
      stage = it.stage ?: "",
      queryDesc = it.queryDesc ?: "",
      dateDisplay = it.dateDisplay ?: "",
      customerServiceRemark = it.customerServiceRemark ?: "",
      category = it.category ?: "",
      meetingDescription = it.meetingDescription,
      attendeeCode = it.attendeeCode,
      isSubcategoryData = it.isSubcategoryData,
      clientname = it.clientname,
      amount = it.amount,
      travellingModeIds = it.travellingModeIds,
      meetingAddressTypeId = it.meetingAddressTypeId,
      inApproval = it.inApproval,
      approvalFieldList = it.approvalFieldList,
      activityType = it.activityType ?: "",
      customerRemark = it.customerRemark,
      isPotentialPMSProduct = it.isPotentialPMSProduct,
      repeatOnCode = it.repeatOnCode,
      actionableID = it.actionableID
    )
  }
}

