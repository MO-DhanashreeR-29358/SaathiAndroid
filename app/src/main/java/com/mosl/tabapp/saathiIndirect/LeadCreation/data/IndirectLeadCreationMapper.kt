package com.mosl.tabapp.saathiIndirect.LeadCreation.data

import com.mosl.app.core.data.util.addDefaultSelection
import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.createlead.*
import com.mosl.saathi.api.model.createmeeting.ChipsEventResponse
import com.mosl.saathi.api.model.lead.*
import com.mosl.saathi.api.model.saathiIndirect.SetAppointment.ResponseItem
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl.Companion.SELECT_ROW_DATA
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.model.*
import com.mosl.tabapp.saathiIndirect.LeadCreation.IndirectLeadCreationUserInput

object IndirectLeadCreationMapper {


    //adding LeadCategoryId and LeadCategoryText hardcoded because of Foco lead was not get created without these value
    // set hardcode as per the backend developer suggestion
internal infix fun IndirectLeadCreationUserInput.mapToCreateLeadRequest(user: User?): IndirectCreateLeadRequest {

    val model = mapToLeadCreationModel(user)
    val listOfCreateLeadProduct = model.productMapping.mapToProductList()
    val leadContact = model.contact.mapToCreateLeadContact()

    return IndirectCreateLeadRequest(
        businessUnitID = model.businessUnitID,
        leadCategoryID = model.leadCategoryID,
        leadName = model.leadName,
        bgId = model.bgId,
        workExpId = model.workExpId,
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
        expectedMargin = model.expMargin,
        leadCategoryId = "2815",
        LeadCategoryText = "Individual"
    )
}

private fun IndirectLeadCreationContact.mapToCreateLeadContact(): IndirectCreateLeadContact {
    val leadAddresses = address.mapToCreateLeadAddressDetailList()

    return IndirectCreateLeadContact(
        name,
        dob,
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

private fun IndirectLeadCreationUserInput.mapToLeadCreationModel(user: User?): IndirectLeadCreationModel {
    return IndirectLeadCreationModel().apply {
        seteMail(emailId)
        setSameAsAbove(isSameAsChecked)
        setAlternateEmail(alternateEmailId)
        setMobileNo(mobileNo)
        setMobileNoRi(mobileNo)
        setLandLineNo(landlineNo)
        setEkycMobileNo(ekycMobileNo)
        setPanNo(panNo)
        setAadharNo(aadharNo)
        setDob(dob)
        setAlternateMobileNo(alternateMobNo)
        setBusinessUnitID(user?.currentBusinessProfile?.businessUnitId)
        setLeadCategoryID(selectedCategory.key)
        setBgId(selectedBackground.key)
        setWorkExpId (selectedWorkExp.key )
        setBranchCode(branchCode_)
        setContactPerson(contactPerson)
        setSourceID(selectedSource.key)
        setLeadSourceID(selectedProspectSource.key)
        setLeadSourceDetailsID("0")
        leadName = name
        setName(name)
        setLeadSourceDetails(prospectSourceDetail)
        if (referCode.length >= 4 && isRefereeCode)
            refereeCode = referCode
        if (referName.length >= 3 && isRefereeCode)
            refereeName = referName
        refereeEmail = referEmail
        refereeMobile = referMobile
        prospectRefereName = proReferName
        prospectRefereEmail = proReferEmail
        prospectRefereMobile = proReferMob
        comments = comment
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
        setExpectedMargin(expectedMargin)
    }
}

private fun LeadCreationProductMapping.mapToProductList(): List<IndirectCreateLeadProduct> {
    return listOf(
        IndirectCreateLeadProduct(
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

 internal fun List<BackgroundItem>.backgroundItemsToRowDatas(): List<RowData> {
        return mapIndexedNotNull { index: Int, backgroundItem: BackgroundItem ->
            try {
                RowData(
                    index + 1,
                    backgroundItem.backgroundId ?: "",
                    backgroundItem.backgroundName
                )
            } catch (_: Exception) {
                null
            }
        }.addIndirectSelectRowData()
    }

    internal fun List<AssociatedBrokerItem>.borkerItemsToRowDatas(): List<RowData> {
        return mapIndexedNotNull { index: Int, item: AssociatedBrokerItem ->
            try {
                RowData(
                    index + 1,
                    item.brokerId ?: "",
                    item.brokerName
                )
            } catch (_: Exception) {
                null
            }
        }.addIndirectSelectRowData()
    }

internal fun List<LookupItem>.mapToIndirectRowDatas(): List<RowData> {
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
    }.addIndirectSelectRowData()
}

fun transformLookupData(
    list: List<ResponseItem?>
): List<RowData> {
    return list.mapIndexedNotNull { index: Int, lookupItem: ResponseItem? ->
        try {
            RowData(
                index + 1,
                "${lookupItem?.lookUpid}",
                lookupItem?.lookUpName ?: ""
            )
        } catch (_: Exception) {
            null
        }
    }.addIndirectSelectRowData()

}

internal fun List<SourceListItem>.mapToIndirectRowDataList(): List<RowData> {
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

internal fun List<StageItem>.indirectStageItemsToRowDatas(): List<RowData> {
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

internal fun List<LeadSourceItem>.indirectLeadSourceItemsToRowDatas(): List<RowData> {
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

internal fun List<StatusItem>.indirectStatusItemsToRowDatas(): List<RowData> {
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

internal fun List<BusinessProfile>.indirectProfilesToEmployeeModels(user: User?): List<EmployeeModel> {
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

internal fun List<CountryItem>.indirectCountryItemsToRowDatas(): List<RowData> {
    return mapIndexedNotNull { index: Int, countryItem: CountryItem ->
        try {
            RowData(
                index + 1,
                countryItem.countryId ?: "",
                countryItem.countryName
            )
        } catch (_: Exception) {
            null
        }
    }
}

internal fun List<STDCodeItem>.indirectStdCodeItemsToRowDatas(): List<RowData> {
    return mapIndexedNotNull { index: Int, countryItem: STDCodeItem ->
        try {
            RowData(
                index + 1,
                countryItem.STDCode ?: "",
                countryItem.STDCodeDisplay
            )
        } catch (_: Exception) {
            null
        }
    }.addIndirectSelectRowData()
}

internal fun List<StateProvinceItem>.indirectStateProvinceItemsToRowDatas(): List<RowData> {
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
    }.addIndirectSelectRowData()
}

internal fun List<ChipsEventResponse>.indirectChipsItemsToRowDatas(): List<RowData> {
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
    }.addIndirectSelectRowData()
}

internal fun List<CityItem>.indirectCityItemsToRowDatas(): List<RowData> {
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
    }.addIndirectSelectRowData()
}

internal infix fun SearchRefereeResponse.indirectRefereeResponseToReferee(type: String): SearchReferee {
    return SearchReferee(
        code = code ?: "",
        name = name ?: "",
        email = email ?: "",
        mobile = mobile ?: "",
        type = type
    )
}

internal fun List<RowData>.addIndirectSelectRowData(): List<RowData> {
    return toMutableList().apply {
        add(0, SELECT_ROW_DATA)
    }
        .toList()
}

}


