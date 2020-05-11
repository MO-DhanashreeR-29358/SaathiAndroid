package com.mosl.tabapp.leadcreation.data

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.createlead.CreateLeadAddressDetails
import com.mosl.saathi.api.model.createlead.CreateLeadContact
import com.mosl.saathi.api.model.createlead.CreateLeadProduct
import com.mosl.saathi.api.model.createlead.CreateLeadRequest
import com.mosl.saathi.api.model.createlead.SourceListItem
import com.mosl.saathi.api.model.lead.CityItem
import com.mosl.saathi.api.model.lead.CountryItem
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.StageItem
import com.mosl.saathi.api.model.lead.StateProvinceItem
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl.Companion.SELECT_ROW_DATA
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import com.mosl.tabapp.model.AddressDetailModel
import com.mosl.tabapp.model.ContactDetailModel
import com.mosl.tabapp.model.LeadCreationContact
import com.mosl.tabapp.model.LeadCreationModel
import com.mosl.tabapp.model.LeadCreationProductMapping

internal infix fun LeadCreationUserInput.mapToCreateLeadRequest(user: User?): CreateLeadRequest {

  val model = mapToLeadCreationModel(user)
  val listOfCreateLeadProduct = model.productMapping.mapToProductList()
  val leadContact = model.contact.mapToCreateLeadContact()

  return CreateLeadRequest(
      businessUnitID = model.businessUnitID,
      leadCategoryID = model.leadCategoryID,
      contactPerson = model.contactPerson,
      sourceID = model.sourceID,
      leadSourceID = model.leadSourceID,
      leadSourceDetailsID = model.leadSourceDetailsID,
      professionid = model.professionid,
      refereeCode = model.refereeCode,
      refereeName = model.refereeName,
      refereeEmail = model.refereeEmail,
      refereeMobile = model.refereeMobile,
      comments = model.comments,
      createdBy = model.createdBy,
      createdThrough = model.createdThrough,
      creationDate = model.creationDate,
      prospectRefereName = model.prospectRefereName,
      prospectRefereMobile = model.prospectRefereMobile,
      productMapping = listOfCreateLeadProduct,
      contact = leadContact
  )
}

private fun LeadCreationContact.mapToCreateLeadContact(): CreateLeadContact {
  val leadAddresses = address.mapToCreateLeadAddressDetailList()

  return CreateLeadContact(
      name,
      geteMail(),
      alternateEmail,
      alternateMobileNo,
      mobileNo,
      landLineNo,
      ekycMobileNo,
      panNo,
      aadharNo,
      isSameAsAbove,
      leadAddresses
  )
}

private fun List<AddressDetailModel>.mapToCreateLeadAddressDetailList(): List<CreateLeadAddressDetails> {
  return safeMap { detailModel ->
    CreateLeadAddressDetails(
        detailModel.addressTypeID,
        detailModel.address1,
        detailModel.address2,
        detailModel.address3,
        detailModel.stateID,
        detailModel.cityID,
        detailModel.countryID,
        detailModel.pincode,
        detailModel.isPrimary,
        detailModel.businessUnitID
    )
  }
}

private fun LeadCreationUserInput.mapToLeadCreationModel(user: User?): LeadCreationModel {
  return LeadCreationModel().apply {
    seteMail(emailId)
    setSameAsAbove(isSameAsChecked)
    setAlternateEmail(alternateEmailId)
    setMobileNo(mobileNo)
    setLandLineNo(landlineNo)
    setEkycMobileNo(ekycMobileNo)
    setPanNo(panNo)
    setAadharNo(aadharNo)
    setBusinessUnitID(user?.currentBusinessProfile?.businessUnitId)
    setLeadCategoryID(etCategoryName)
    setContactPerson(contactPerson)
    setSourceID(selectedSource.key)
    setLeadSourceID(selectedProspectSource.key)
    setLeadSourceDetailsID("0")
    setName(name)
    setLeadSourceDetails(prospectSourceDetail)
    if (referCode.length >= 4 && isRefereeCode)
      refereeCode = referCode
    if (referName.length >= 3 && isRefereeCode)
      refereeName = referName
    setAlternateMobileNo(alternateMobileNo)
    refereeEmail = referEmail
    refereeMobile = referMobile
    prospectRefereName = proReferName
    prospectRefereEmail = proReferEmail
    prospectRefereMobile = proReferMob
    comments = comment
    profileID = user?.currentBusinessProfile?.businessProfileId
    createdBy = user?.employeeCode
    createdThrough = "SelfCreated"
    creationDate = CRMAppUtil.getDBDate()
    setInterestedIn(selectedInterestIn.key)
    professionid = professionalId.key
    setProductID(selectedProductType.key)
    setPrimaryRM(primRM ?: user?.employeeCode)
    setClientCode(null)
    setAssignedBy(user?.employeeCode)
    setAssignedByDate(CRMAppUtil.getDBDate())
    setStageID(stageId.key)
    setStatusID(statusId.key)
    setAddresses(officeAdd, residenceAdd)
    contactDetailModels = arrayListOf<ContactDetailModel>()
    addressDetailModels = arrayListOf<AddressDetailModel>()
    productDetailModels = arrayListOf<LeadCreationProductMapping>()
  }
}

private fun LeadCreationProductMapping.mapToProductList(): List<CreateLeadProduct> {
  return listOf(
      CreateLeadProduct(
          interestedIn,
          productID,
          primaryRM,
          clientCode,
          assignedBy,
          assignedByDate,
          stageID,
          statusID
      )
  )
}

internal fun List<LookupItem>.mapToRowDatas(): List<RowData> {
  return mapIndexedNotNull { index: Int, lookupItem: LookupItem ->
    try {
      RowData(
          index + 1,
          "${lookupItem.lookUpid}",
          lookupItem.lookUpName ?: "",
          lookupItem.sysName ?: ""
      )
    } catch (_: Exception) {
      null
    }
  }.addSelectRowData()
}

internal fun List<SourceListItem>.mapToRowDataList(): List<RowData> {
  return mapIndexedNotNull { index: Int, sourceItem: SourceListItem ->
    try {
      RowData(
          index + 1,
          "${sourceItem.sourceID}",
          sourceItem.sourceName ?: ""
      )
    } catch (_: Exception) {
      null
    }
  }
}

internal fun List<StageItem>.stageItemsToRowDatas(): List<RowData> {
  return mapIndexedNotNull { index: Int, stageItem: StageItem ->
    try {
      RowData(
          index + 1,
          "${stageItem.stageID}",
          stageItem.stageName
      )
    } catch (_: Exception) {
      null
    }
  }
}

internal fun List<CountryItem>.countryItemsToRowDatas(): List<RowData> {
  return mapIndexedNotNull { index: Int, countryItem: CountryItem ->
    try {
      RowData(
          index,
          countryItem.countryId ?: "",
          countryItem.countryName
      )
    } catch (_: Exception) {
      null
    }
  }.addSelectRowData()
}

internal fun List<StateProvinceItem>.stateProvinceItemsToRowDatas(): List<RowData> {
  return mapIndexedNotNull { index: Int, stateProvinceItem: StateProvinceItem ->
    try {
      RowData(
          index + 1,
          "${stateProvinceItem.stateId}",
          stateProvinceItem.stateName
      )
    } catch (_: Exception) {
      null
    }
  }.addSelectRowData()
}

internal fun List<CityItem>.cityItemsToRowDatas(): List<RowData> {
  return mapIndexedNotNull { index: Int, cityItem: CityItem ->
    try {
      RowData(
          index + 1,
          "${cityItem.cityId}",
          cityItem.cityName
      )
    } catch (_: Exception) {
      null
    }
  }.addSelectRowData()
}

internal fun List<RowData>.addSelectRowData(): List<RowData> {
  return toMutableList().apply {
    add(0, SELECT_ROW_DATA)
  }
      .toList()
}


