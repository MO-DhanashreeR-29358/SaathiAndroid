package com.mosl.tabapp.saathiIndirect.LeadCreation

import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.model.AddressDetailModel

data class IndirectLeadCreationUserInput(
    @JvmField val selectedCategory: RowData,
    @JvmField val etCategoryName: String,
    @JvmField val selectedInterestIn: RowData,
    @JvmField val emailId: String,
    @JvmField val alternateEmailId: String,
    @JvmField val mobileNo: String,
    @JvmField val landlineNo: String,
    @JvmField val panNo: String,
    @JvmField val sourceName: String,
    @JvmField val referCode: String,
    @JvmField val referEmail: String,
    @JvmField val referName: String,
    @JvmField val referMobile: String,
    @JvmField val isRefereeCode: Boolean,
    @JvmField val pinCode: String,
    @JvmField val officePinCode: String,
    @JvmField val isSameAsChecked: Boolean,
    @JvmField val selectedSource: RowData,
    @JvmField val selectedProspectSource: RowData,
    @JvmField val name: String,
    @JvmField val proReferName: String,
    @JvmField val proReferMob: String,
    @JvmField val proReferEmail: String,
    @JvmField val comment: String,
    @JvmField val primRM: String?,
    @JvmField val stageId: RowData,
    @JvmField val statusId: RowData,
    @JvmField val clientCode: String? = null,
    @JvmField val residenceAdd: AddressDetailModel,
    @JvmField val officeAdd: AddressDetailModel,
    @JvmField val isBank: Boolean,
    @JvmField val branchCode : String,
    @JvmField val expectedMargin : String
)