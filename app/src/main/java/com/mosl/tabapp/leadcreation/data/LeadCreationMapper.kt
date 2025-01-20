package com.mosl.tabapp.leadcreation.data

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.createlead.*
import com.mosl.saathi.api.model.createmeeting.ChipsEventResponse
import com.mosl.saathi.api.model.lead.*
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl.Companion.SELECT_ROW_DATA
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leadcreation.ui.LeadCreationUserInput
import com.mosl.tabapp.model.*

internal infix fun LeadCreationUserInput.mapToCreateLeadRequest(user: User?): CreateLeadRequest {

    val model = mapToLeadCreationModel(user)
    val listOfCreateLeadProduct = model.productMapping.mapToProductList()
    val leadContact = model.contact.mapToCreateLeadContact()

    return CreateLeadRequest(
        businessUnitID = model.businessUnitID,
        leadCategoryID = model.leadCategoryID,
        branchCode = model.branchCode,
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
        contact = leadContact,
        referralEmpCode = model.referralEmpCode,
        nriRiId = model.nriRiID,
        expectedMargin = model.expMargin,
        leadAccountTypeId = model.leadAccountTypeId,
        chipsEventIdName = model.chipsEventIdName,
        IsBandhanBankEmployee = model.isBandhanBankEmployee,
        BandhanBankEmployeeCode = model.bandhanBankEmployeeCode
    )
}

private fun LeadCreationContact.mapToCreateLeadContact(): CreateLeadContact {
    val leadAddresses = address.mapToCreateLeadAddressDetailList()

    return CreateLeadContact(
        name,
        dob,
        bankAccountNumber,
        geteMail(),
        alternateEmail,
        alternateMobileNo,
        mobileNo,
        mobileNoRI,
        landLineNo,
        ekycMobileNo,
        panNo,
        aadharNo,
        isSameAsAbove,
        isWMCodeCheck,
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
        setWMCodeCheck(isWMCodeChecked)
        setAlternateEmail(alternateEmailId)
        setMobileNo(mobileNo)
        setMobileNoRi(mobileNo)
        setLandLineNo(landlineNo)
        setEkycMobileNo(ekycMobileNo)
        setPanNo(panNo)
        setAadharNo(aadharNo)
        setDob(dob)
        setBankAccountNumber(BankAccountNumber)
        referralEmpCode = referaalCode
        setBusinessUnitID(user?.currentBusinessProfile?.businessUnitId)
        setLeadCategoryID(selectedCategory.key)
        setBranchCode(selectedBranch.branchName)
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
        setSecondaryRM(secondaryRM)
        setClientCode(null)
        setAssignedBy(user?.employeeCode)
        setAssignedByDate(CRMAppUtil.getDBDate())
        setStageID(stageId.key)
        setStatusID(statusId.key)
        setAddresses(officeAdd, residenceAdd)
        contactDetailModels = arrayListOf<ContactDetailModel>()
        addressDetailModels = arrayListOf<AddressDetailModel>()
        productDetailModels = arrayListOf<LeadCreationProductMapping>()
        nriRiID = nriRiId
        setExpectedMargin(expectedMargin)
        leadAccountTypeId = if(accountTypeId?.isEmpty() == true) "-1" else accountTypeId
        chipsEventIdName = ChipsEventId
        isBandhanBankEmployee = isBandhanBankEmp
        bandhanBankEmployeeCode = BandhanBankEmpCode

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
            statusID,
            secondaryRM
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

internal fun List<LookupItem>.mapToRowDatasWithNoDefault(): List<RowData> {
    return mapIndexedNotNull { index: Int, lookupItem: LookupItem ->
        try {
            RowData(
                index,
                "${lookupItem.lookUpid}",
                lookupItem.lookUpName ?: "",
                lookupItem.sysName ?: ""
            )
        } catch (_: Exception) {
            null
        }
    }
}
internal fun List<LookupItem>.mapToClientTypeRowDatas(): List<RowData> {
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
    }
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
internal fun List<StageItem>.realEstateStageItemsToRowDatas(): List<RowData> {
    return mapIndexedNotNull { index: Int, stageItem: StageItem ->
        try {
            RowData(
                index + 1,
                "${stageItem.stageID}",
                stageItem.statusName
            )
        } catch (_: Exception) {
            null
        }
    }
}
internal fun List<LeadSourceItem>.leadSourceItemsToRowDatas(): List<RowData> {
    return mapIndexedNotNull { index: Int, leadSourceItem: LeadSourceItem ->
        try {
            RowData(
                index + 1,
                leadSourceItem.leadSourceID ?: "",
                leadSourceItem.leadSourceName ?: ""
            )
        } catch (_: Error) {
            null
        }
    }
}

internal fun List<StatusItem>.statusItemsToRowDatas(): List<RowData> {
    return mapIndexedNotNull { index: Int, statusItem: StatusItem ->
        try {
            RowData(
                index + 1,
                "${statusItem.statusID}",
                statusItem.statusName
            )
        } catch (_: Error) {
            null
        }
    }
}

internal fun List<BusinessProfile>.profilesToEmployeeModels(user: User?): List<EmployeeModel> {
    return filter { it.employeeCode != user?.employeeCode }
        .safeMap {
            EmployeeModel(
                it.employeeName ?: "",
                it.employeeCode ?: "",
                it.branchCode ?: "",
                false
            )
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
    }
}

internal fun List<STDCodeItem>.stdCodeItemsToRowDatas(): List<RowData> {
    return mapIndexedNotNull { index: Int, countryItem: STDCodeItem ->
        try {
            RowData(
                index,
                countryItem.STDCode ?: "",
                countryItem.STDCodeDisplay
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

internal fun List<ChipsEventResponse>.chipsItemsToRowDatas(): List<RowData> {
    return mapIndexedNotNull { index: Int, chipsItem: ChipsEventResponse ->
        try {
            RowData(
                index + 1,
                chipsItem.eventName,
                chipsItem.eventNameText
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

internal infix fun SearchRefereeResponse.refereeResponseToReferee(type: String): SearchReferee {
    return SearchReferee(
        code = code ?: "",
        name = name ?: "",
        email = email ?: "",
        mobile = mobile ?: "",
        type = type
    )
}

internal fun List<RowData>.addSelectRowData(): List<RowData> {
    return toMutableList().apply {
        add(0, SELECT_ROW_DATA)
    }
        .toList()
}




