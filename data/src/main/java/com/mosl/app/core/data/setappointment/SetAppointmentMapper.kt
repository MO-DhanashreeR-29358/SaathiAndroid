package com.mosl.app.core.data.setappointment

import com.mosl.mobile.domain.setappointment.StageWiseFieldConfigurationItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.AdvisorsItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.AdvisoryMappingTypesItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ProductResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ProductsItemResponse
import com.mosl.mobile.domain.setappointment.lead.ContactAddressItemResponse
import com.mosl.mobile.domain.setappointment.lead.ContactResponse
import com.mosl.mobile.domain.setappointment.lead.CrossSellingProductResponse
import com.mosl.mobile.domain.setappointment.lead.DedupeModelResponse
import com.mosl.mobile.domain.setappointment.lead.LeadFieldSectionResponse
import com.mosl.mobile.domain.setappointment.lead.LeadFieldValueDataResponse
import com.mosl.mobile.domain.setappointment.lead.LeadProductMappingItemResponse
import com.mosl.saathi.api.model.clientdetail.AdvisorsItem
import com.mosl.saathi.api.model.clientdetail.AdvisoryMappingTypesItem
import com.mosl.saathi.api.model.clientdetail.Product
import com.mosl.saathi.api.model.clientdetail.ProductsItem
import com.mosl.saathi.api.model.lead.Contact
import com.mosl.saathi.api.model.lead.ContactAddressItem
import com.mosl.saathi.api.model.lead.CrossSellingProduct
import com.mosl.saathi.api.model.lead.DedupeModel
import com.mosl.saathi.api.model.lead.LeadFieldSection
import com.mosl.saathi.api.model.lead.LeadFieldValueData
import com.mosl.saathi.api.model.lead.LeadProductMappingItem
import com.mosl.saathi.api.model.lead.StageWiseFieldConfigurationItem

fun List<ProductsItem>.responseProductItemTransform(): List<ProductsItemResponse> {
  return map {
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
    advisoryMappingTypes = advisoryMappingTypes?.responseAdvisoryMappingTypesItemTransform()!!
  )
}

fun List<AdvisorsItem>.responseAdvisorsItemTransform(): List<AdvisorsItemResponse> {
  return map {
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
  return map {
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
  return map {

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
  return map {
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
  return map {
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
  return map {
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
  return map {
    LeadFieldSectionResponse(
      fieldSectionId = it.fieldSectionId,
      sectionName = it.sectionName,
      sectionCode = it.sectionCode,
      sectionHeader = it.sectionHeader
    )
  }

}

fun List<LeadFieldValueData>.responseLeadFieldValueDataTransform(): List<LeadFieldValueDataResponse> {
  return map {
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


