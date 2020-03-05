package com.mosl.app.core.data.setappointment

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.addupdatemeeting.AppointmentAttendeeItemResponse
import com.mosl.mobile.domain.addupdatemeeting.SetAppointmentResponseData
import com.mosl.mobile.domain.createmeeting.AutoCompleteAttendeeItemResponse
import com.mosl.mobile.domain.createmeeting.MeetingAttendeeItemResponse
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.domain.setappointment.LookupRequestInput
import com.mosl.mobile.domain.setappointment.StageWiseFieldConfigurationItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.AdvisorsItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.AdvisoryMappingTypesItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.clientdetail.ProductResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ProductsItemResponse
import com.mosl.mobile.domain.setappointment.lead.ContactAddressItemResponse
import com.mosl.mobile.domain.setappointment.lead.ContactResponse
import com.mosl.mobile.domain.setappointment.lead.CrossSellingProductResponse
import com.mosl.mobile.domain.setappointment.lead.DedupeModelResponse
import com.mosl.mobile.domain.setappointment.lead.LeadFieldSectionResponse
import com.mosl.mobile.domain.setappointment.lead.LeadFieldValueDataResponse
import com.mosl.mobile.domain.setappointment.lead.LeadProductMappingItemResponse
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.saathi.api.model.addupdatemeeting.AppointmentAttendeeItem
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse
import com.mosl.saathi.api.model.clientdetail.AdvisorsItem
import com.mosl.saathi.api.model.clientdetail.AdvisoryMappingTypesItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.clientdetail.Product
import com.mosl.saathi.api.model.clientdetail.ProductsItem
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem
import com.mosl.saathi.api.model.createmeeting.MeetingAttendeeItem
import com.mosl.saathi.api.model.createmeeting.MeetingByIdItem
import com.mosl.saathi.api.model.lead.Contact
import com.mosl.saathi.api.model.lead.ContactAddressItem
import com.mosl.saathi.api.model.lead.CrossSellingProduct
import com.mosl.saathi.api.model.lead.DedupeModel
import com.mosl.saathi.api.model.lead.LeadFieldSection
import com.mosl.saathi.api.model.lead.LeadFieldValueData
import com.mosl.saathi.api.model.lead.LeadProductMappingItem
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.StageWiseFieldConfigurationItem

fun ClientDetailItem.responseClientDetailItemTransform(): ClientDetailIResponseData {
  return ClientDetailIResponseData(
    familyUCID = familyUCID,
    bankName = bankName,
    creationDate = creationDate,
    isFamilyHead = isFamilyHead,
    products = products?.responseProductItemTransform(),
    advisoryType = advisoryType,
    email = email,
    address = address,
    productName = productName,
    clientProfile = clientProfile,
    sourcing = sourcing,
    accountOpeningDate = accountOpeningDate,
    uCID = uCID,
    profile = profile,
    clientName = clientName,
    pendingDays = pendingDays,
    pANNumber = pANNumber,
    primaryAdvisor = primaryAdvisor,
    dOB = dOB,
    subBrokerCode = subBrokerCode,
    createdBy = createdBy,
    employeeCode = employeeCode,
    oAOUserid = oAOUserid,
    mappedBy = mappedBy,
    accountNo = accountNo,
    stageName = stageName,
    channel = channel,
    defaultBank = defaultBank,
    secondaryAdvisor = secondaryAdvisor,
    familyHead = familyHead,
    accountType = accountType,
    mobile = mobile,
    vintagewithMotilalOswalYears = vintagewithMotilalOswalYears,
    aadhaarNumber = aadhaarNumber,
    clientProfileComplition = clientProfileComplition,
    clientCode = clientCode,
    areaCode = areaCode,
    bankAccountType = bankAccountType,
    leadID = leadID,
    isPotentialPMSProduct = isPotentialPMSProduct,
    mappingDate = mappingDate,
    branchCode = branchCode,
    bankAccountNumber = bankAccountNumber,
    isOnlyClient = isOnlyClient,
    createMeetingId = createMeetingId
  )

}

fun List<ProductsItem>.responseProductItemTransform(): List<ProductsItemResponse> {
  return safeMap {
    ProductsItemResponse(
      creationDate = it.creationDate,
      lastModifiedBy = it.lastModifiedBy,
      processtype = it.processtype,
      advisoryType = it.advisoryType,
      product = it.product?.transformProductResponse()!!,
      productId = it.parentProductID,
      uCID = it.uCID,
      clientProductMappingID = it.clientProductMappingID,
      isAdminMode = it.isAdminMode,
      subBrokerCode = it.subBrokerCode,
      unsuccessfullRecords = it.unsuccessfullRecords,
      status = it.status,
      createdBy = it.createdBy,
      advisoryTypeBranchCode = it.advisoryTypeBranchCode,
      productCode = it.productCode,
      batchNumber = it.batchNumber,
      channel = it.channel,
      advisors = it.advisors?.responseAdvisorsItemTransform(),
      customerId = it.customerId,
      inApproval = it.isAdminMode,
      holdingNumber = it.holdingNumber,
      customerClassification = it.customerClassification,
      advisoryTypeProcessType = it.advisoryTypeProcessType,
      parentProductID = it.parentProductID,
      productcategory = it.productcategory,
      familyCode = it.familyCode,
      approvalFieldList = it.approvalFieldList,
      lastModificationDate = it.lastModificationDate,
      branchCode = it.branchCode
    )
  }

}

fun LeadViewItem.responseLeadViewItemTransform(): LeadViewItemResponse {
  return LeadViewItemResponse(
    createdThrough = createdThrough,
    creationDate = creationDate,
    prospectRefereMobile = prospectRefereEmail,
    businessUnitID = businessUnitID,
    contactPerson = contactPerson,
    leadOwner = leadOwner,
    prospectSource = prospectSource,
    potentialPMSProductList = potentialPMSProductList,
    contact = contact?.responseContactTransform(),
    leadProductMapping = leadProductMapping?.responseLeadProductMappingItemTransform(),
    isAdminMode = isAdminMode,
    isPotentialPMSProductText = isPotentialPMSProductText,
    stageWiseFieldConfiguration = stageWiseFieldConfiguration.responseStageWiseFieldConfigurationItemTransform(),
    lastReCycleTransferDate = lastReCycleTransferDate,
    status = status,
    createdBy = createdBy,
    refereeEmail = refereeEmail,
    oAOUserid = oAOUserid,
    queryId = queryId,
    prospectRefereEmail = prospectRefereEmail,
    channelID = channelID,
    isProductSpecialist = isProductSpecialist,
    isAadhar = isAadhar,
    leadCategoryText = leadCategoryText,
    lastRecycleDate = lastRecycleDate,
    branchcode = branchcode,
    active = active,
    profession = profession,
    prospectRefereName = prospectRefereName,
    lastModificationDate = lastModificationDate,
    profileParamPercentage = profileParamPercentage,
    canEditLead = canEditLead,
    isRecycleLead = isRecycleLead,
    refereeMobile = refereeMobile,
    lastModifiedBy = lastModifiedBy,
    refereeName = refereeName,
    professionID = professionID,
    campaignName = campaignName,
    profile = profile,
    leadSourceDetails = leadSourceDetails,
    moneyControlPortfolioURL = moneyControlPortfolioURL,
    availableAddressTypes = availableAddressTypes,
    leadSourceName = leadSourceName,
    leadFieldSectionModels = leadFieldSectionModels?.responseLeadFieldSectionTransform(),
    profileID = profileID,
    dedupeModel = dedupeModel.responseDedupeModelTransform(),
    loginId = loginId,
    approvalMessage = approvalMessage,
    comments = comments,
    sourceID = sourceID,
    refereeCode = refereeCode,
    sourceName = sourceName,
    leadSourceID = leadSourceID,
    contactID = contactID,
    leadSourceDetailsID = leadSourceDetailsID,
    aMCCampaignID = aMCCampaignID,
    isAllowed = isAllowed,
    isPotentialPMSProductValue = isPotentialPMSProductValue,
    inApproval = inApproval,
    isReCycleTransfer = isReCycleTransfer,
    approvalFieldList = approvalFieldList,
    leadID = leadID,
    createMeetingId = createMeetingId,
    isPotentialPMSProduct = isPotentialPMSProduct,
    leadProfileParameters = leadProfileParameters,
    leadCategoryID = leadCategoryID,
    leadFieldValueModels = leadFieldValueModels?.responseLeadFieldValueDataTransform()
  )
}

fun Product.transformProductResponse(): ProductResponse {
  return ProductResponse(
    groupName = groupName,
    status = status,
    productName = productName,
    includeInGroup = includeInGroup,
    traderCode = traderCode,
    productId = productId,
    backOfficeProductId = backOfficeProductId,
    clientProducts = clientProducts,
    inApproval = inApproval,
    approvalFieldList = approvalFieldList,
    isAdminMode = isAdminMode,
    effectiveFrom = effectiveFrom,
    advisoryMappingTypes = advisoryMappingTypes.responseAdvisoryMappingTypesItemTransform()
  )
}

fun List<AdvisorsItem>.responseAdvisorsItemTransform(): List<AdvisorsItemResponse> {
  return safeMap {
    AdvisorsItemResponse(
      status = it.status,
      creationDate = it.creationDate,
      lastModifiedBy = it.lastModifiedBy,
      advisorName = it.advisorName,
      createdBy = it.createdBy,
      advisorCode = it.advisorCode,
      product = it.product,
      clientAdvisoryMappingId = it.clientAdvisoryMappingId,
      productId = it.productId,
      uCID = it.uCID,
      inApproval = it.inApproval,
      approvalFieldList = it.approvalFieldList,
      lastModificationDate = it.lastModificationDate,
      isAdminMode = it.isAdminMode,
      effectiveFrom = it.effectiveFrom,
      advisoryMappingTypeId = it.advisoryMappingTypeId,
      branchCode = it.branchCode
    )
  }
}

fun List<AdvisoryMappingTypesItem>.responseAdvisoryMappingTypesItemTransform()
    : List<AdvisoryMappingTypesItemResponse> {
  return safeMap {
    AdvisoryMappingTypesItemResponse(
      parentId = it.parentId,
      status = it.status,
      active = it.active,
      lookupType = it.lookupType,
      employeeCode = it.employeeCode,
      lookUpid = it.lookUpid,
      lookUpName = it.lookUpName,
      businessUnitId = it.businessUnitId,
      sequence = it.sequence,
      lookupTypeId = it.lookupTypeId
    )
  }
}

fun Contact.responseContactTransform(): ContactResponse {
  return ContactResponse(
    fDB = fDB,
    eKYCMobileNo = eKYCMobileNo,
    aadharNumber = aadharNumber,
    isSameAsAbove = isSameAsAbove,
    alternateMobileNo = alternateMobileNo,
    salutation = salutation,
    annualCashFlow = annualCashFlow,
    uCID = uCID ?: "0",
    name = name ?: "",
    companyName = companyName,
    landlineNo = landlineNo,
    dOB = dOB,
    dOA = dOA,
    prefixCode = prefixCode,
    isAdminMode = isAdminMode,
    contactAddress = contactAddress.responseContactAddressItemTransform(),
    status = status,
    designation = designation,
    emailId = emailId,
    contactID = contactID,
    mobile = mobile,
    inApproval = inApproval,
    alternateEmailId = alternateEmailId,
    approvalFieldList = approvalFieldList,
    panNumber = panNumber
  )

}

fun List<ContactAddressItem>.responseContactAddressItemTransform(): List<ContactAddressItemResponse> {
  return safeMap {
    ContactAddressItemResponse(
      countryID = it.countryID,
      status = it.status,
      countryName = it.countryName,
      businessUnitID = it.businessUnitID,
      stateName = it.stateName,
      address2 = it.address2,
      stateID = it.stateID,
      address3 = it.address3,
      contactID = it.contactID,
      address1 = it.address1,
      cityName = it.cityName,
      inApproval = it.inApproval,
      addressTypeID = it.addressTypeID,
      cityID = it.cityID,
      isPrimary = it.isPrimary,
      approvalFieldList = it.approvalFieldList,
      contactAddressID = it.contactAddressID,
      addressTypeName = it.addressTypeName,
      isAdminMode = it.isAdminMode,
      pincode = it.pincode
    )
  }
}

fun List<LeadProductMappingItem>.responseLeadProductMappingItemTransform(): List<LeadProductMappingItemResponse> {
  return safeMap {
    LeadProductMappingItemResponse(
      lastModifiedBy = it.lastModifiedBy,
      thirdRM = it.thirdRM,
      productName = it.productName,
      crossSellingProducts = it.crossSellingProducts?.responseListCrossSellingProductTransform(),
      uCID = it.uCID,
      isProductDisabled = it.isProductDisabled,
      followUpDate = it.followUpDate,
      fourthRMAssignDate = it.fourthRMAssignDate,
      pendingDays = it.pendingDays,
      thirdRMAssignDate = it.thirdRMAssignDate,
      fourthRMAssignedBy = it.fourthRMAssignedBy,
      isProductSpecialistEditDisabled = it.isProductSpecialistEditDisabled,
      secondaryRM = it.secondaryRM,
      assignedByDate = it.assignedByDate,
      statusID = it.statusID,
      isAdminMode = it.isAdminMode,
      assignedBy = it.fourthRMAssignedBy,
      isDefault = it.isDefault,
      status = it.status,
      stageName = it.stageName,
      stageID = it.stageID,
      lowMargin = it.lowMargin,
      primaryRMName = it.primaryRMName,
      productID = it.productID,
      statusName = it.statusName,
      productSpecialistName = it.productSpecialistName,
      lead = it.isLowMarginLead,
      interestedIn = it.interestedIn,
      inApproval = it.inApproval,
      active = it.active,
      secondaryRMAssignedDate = it.secondaryRMAssignedDate,
      approvalFieldList = it.approvalFieldList,
      clientCode = it.clientCode,
      lastModificationDate = it.lastModificationDate,
      secondaryRMAssignedBy = it.secondaryRMAssignedBy,
      leadID = it.leadID,
      leadProductMappingID = it.leadProductMappingID,
      isPotentialPMSProduct = it.isPotentialPMSProduct,
      fourthRM = it.fourthRM,
      productSpecialist = it.productSpecialist,
      thirdRMAssignedBy = it.thirdRMAssignedBy,
      primaryRM = it.primaryRM,
      isCrossSellingProduct = it.isCrossSellingProduct,
      isLowMarginLead = it.isLowMarginLead
    )
  }
}

fun List<CrossSellingProduct>.responseListCrossSellingProductTransform(): List<CrossSellingProductResponse> {
  return safeMap {
    CrossSellingProductResponse(
      leadProductId = it.leadProductId,
      productId = it.leadProductId,
      statusId = it.statusId,
      productName = it.productName,
      statusName = it.statusName
    )
  }
}

fun List<StageWiseFieldConfigurationItem>.responseStageWiseFieldConfigurationItemTransform()
    : List<StageWiseFieldConfigurationItemResponse> {
  return safeMap {
    StageWiseFieldConfigurationItemResponse(
      isMandetory = it.isMandetory,
      status = it.status,
      creationDate = it.creationDate,
      lastModifiedBy = it.lastModifiedBy,
      createdBy = it.createdBy,
      fieldSystemName = it.fieldSystemName,
      isHide = it.isHide,
      flagForLeadProductMapping = it.flagForLeadProductMapping,
      forDefaultProductOnly = it.forDefaultProductOnly,
      identifierPropertyName = it.identifierPropertyName,
      businessUnitId = it.businessUnitId,
      stageId = it.stageId,
      flagForLead = it.flagForLead,
      inApproval = it.inApproval,
      fieldDisplayName = it.fieldDisplayName,
      approvalFieldList = it.approvalFieldList,
      lastModificationDate = it.lastModificationDate,
      statusId = it.statusId,
      entityStatus = it.entityStatus,
      iD = it.iD
    )
  }
}

fun List<LeadFieldSection>.responseLeadFieldSectionTransform(): List<LeadFieldSectionResponse> {
  return safeMap {
    LeadFieldSectionResponse(
      fieldSectionId = it.fieldSectionId,
      sectionName = it.sectionName,
      sectionCode = it.sectionCode,
      sectionHeader = it.sectionHeader
    )
  }

}

fun List<LeadFieldValueData>.responseLeadFieldValueDataTransform(): List<LeadFieldValueDataResponse> {
  return safeMap {
    LeadFieldValueDataResponse(
      lFSysName = it.lFSysName,
      leadFieldValue = it.leadFieldValue,
      leadFieldId = it.leadFieldId,
      fieldSectionId = it.fieldSectionId,
      fieldName = it.fieldName,
      controlType = it.controlType,
      dropdownValue = it.dropdownValue,
      mandatory = it.mandatory
    )
  }
}

fun DedupeModel.responseDedupeModelTransform(): DedupeModelResponse {
  return DedupeModelResponse(
    status = status,
    isPaymentDone = isPaymentDone,
    email = email,
    currentUser = currentUser,
    productName = productName,
    businessUnitId = businessUnitId,
    checkRemark = checkRemark,
    productId = productId,
    dedupeWith = dedupeWith,
    mobile = mobile,
    inApproval = inApproval,
    clientName = clientName,
    remarks = remarks,
    isTransfer = isTransfer,
    approvalFieldList = approvalFieldList,
    clientCode = clientCode,
    stage = stage,
    isAdminMode = isAdminMode,
    leadID = leadID,
    leadName = leadName,
    branchCode = branchCode,
    caughtInC2 = caughtInC2,
    prospectId = prospectId
  )
}

fun MeetingByIdItem.responseMeetingByIdItemTransform(): MeetingByIdItemResponse {
  return MeetingByIdItemResponse(
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
    categoryID = subCategoryID,
    repeatEveryDayCode = repeatEveryDayCode,
    status = status,
    repeatationStartOn = repeatationStartOn,
    subCategoryDescription = subCategoryDescription,
    createdBy = createdBy,
    meetingAttendee = meetingAttendee.responseMeetingAttendeeItemTransform(),
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
    categoryDescription = subCategoryDescription,
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

fun List<MeetingAttendeeItem>.responseMeetingAttendeeItemTransform(): List<MeetingAttendeeItemResponse> {
  return safeMap {
    MeetingAttendeeItemResponse(
      attendeeType = it.attendeeType,
      status = it.status,
      inApproval = it.inApproval,
      meetingAttendeeID = it.meetingAttendeeID,
      attendeeEmpcode = it.attendeeEmpcode,
      approvalFieldList = it.approvalFieldList,
      meetingID = it.meetingID,
      isAdminMode = it.isAdminMode,
      attendeeEmpName = it.attendeeEmpName,
      attended = it.attended,
      defaultAttendee = it.defaultAttendee
    )
  }
}

fun LookupRequestInput.mapLookupRequestTransform(): LookupRequest {
  return LookupRequest(
    businessUnitId = businessUnitId,
    lookupTypeId = lookupTypeId,
    parentId = parentId,
    IgnoreBusinessUnit = IgnoreBusinessUnit
  )
}

fun List<LookupItem>.responseLookupItemTransform(): List<LookupItemResponse> {
  return safeMap {
    LookupItemResponse(
      parentId = it.parentId,
      status = it.status,
      sysName = it.sysName,
      active = it.active,
      lookupType = it.lookupType,
      lookUpid = it.lookUpid,
      lookUpName = it.lookUpName,
      businessUnitId = it.businessUnitId,
      sequence = it.sequence,
      ignoreBusinessUnit = it.ignoreBusinessUnit,
      lookupTypeId = it.lookupTypeId
    )
  }
}

fun SetAppointmentResponse.responseSetAppointmentTransform(): SetAppointmentResponseData {
  return SetAppointmentResponseData(
    repeatEveryWeekCode = repeatEveryWeekCode,
    repeatEveryDayCode = repeatEveryDayCode,
    repeatationStartOn = repeatationStartOn,
    meetingAttendee = meetingAttendee.responseAppointmentAttendeeItemTransform(),
    displayType = displayType,
    activityTypeID = activityTypeID,
    productTypeID = productTypeID,
    uCID = uCID,
    repeatationEndsOn = repeatationEndsOn,
    meetingDate = meetingDate,
    meetingType = meetingType,
    leadId = leadId,
    repeatOnCode = repeatOnCode
  )

}

fun List<AppointmentAttendeeItem>.responseAppointmentAttendeeItemTransform(): List<AppointmentAttendeeItemResponse> {
  return safeMap {
    AppointmentAttendeeItemResponse(
      attendeeType = it.attendeeType,
      attendeeEmpcode = it.attendeeEmpcode,
      meetingAttendeeID = it.meetingAttendeeID,
      attended = it.attended,
      defaultAttendee = it.defaultAttendee
    )
  }
}

fun List<AutoCompleteAttendeeItem>.responseAutoCompleteAttendeeItemTransform(): List<AutoCompleteAttendeeItemResponse> {
  return safeMap {
    AutoCompleteAttendeeItemResponse(
      advisoryType = it.advisoryType,
      businessProfileId = it.businessProfileId,
      regionCode = it.regionCode,
      productName = it.productName,
      productId = it.productId,
      equityTraderCode = it.equityTraderCode,
      businessChannelId = it.businessChannelId,
      employeeCodes = it.employeeCodes,
      clientType = it.clientType,
      lookUpName = it.lookUpName,
      subBrokerCode = it.subBrokerCode,
      businessUnitName = it.businessUnitName,
      profileGroup = it.profileGroup,
      status = it.status,
      isDefault = it.isDefault,
      employeeCode = it.employeeCode,
      businessUnitId = it.businessUnitId,
      traderCode = it.equityTraderCode,
      numberofClientMapped = it.numberofClientMapped,
      isPrimaryadvisor = it.isPrimaryadvisor,
      channelName = it.channelName,
      totalClientMapped = it.totalClientMapped,
      userId = it.userId,
      businessUnit = it.businessUnit,
      areaCode = it.areaCode,
      id = it.userId,
      employeeName = it.employeeName,
      userType = it.userType,
      branchCode = it.branchCode,
      commodityTraderCode = it.commodityTraderCode,
      primaryRM = it.primaryRM,
      businessProfileName = it.businessProfileName
    )
  }
}

