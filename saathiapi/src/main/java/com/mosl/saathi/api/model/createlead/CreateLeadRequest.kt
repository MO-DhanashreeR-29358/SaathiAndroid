package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class CreateLeadRequest(
    @SerializedName("BusinessUnitID") val businessUnitID: String = "",
    @SerializedName("LeadCategoryID") val leadCategoryID: String = "",
    @SerializedName("Branchcode") val branchCode: String = "",
    @SerializedName("ContactPerson") val contactPerson: String?,
    @SerializedName("SourceID") val sourceID: String = "",
    @SerializedName("LeadSourceDetails") val leadSourceDetails: String = "",
    @SerializedName("LeadSourceID") val leadSourceID: String = "",
    @SerializedName("LeadSourceDetailsID") val leadSourceDetailsID: String = "",
    @SerializedName("ProfessionID") val professionid: String = "",
    @SerializedName("RefereeCode") val refereeCode: String?,
    @SerializedName("RefereeName") val refereeName: String?,
    @SerializedName("RefereeEmail") val refereeEmail: String?,
    @SerializedName("RefereeMobile") val refereeMobile: String?,
    @SerializedName("Comments") val comments: String = "",
    @SerializedName("CreatedBy") val createdBy: String?,
    @SerializedName("CreatedThrough") val createdThrough: String = "",
    @SerializedName("ProspectRefereName") val prospectRefereName: String = "",
    @SerializedName("ProspectRefereMobile") val prospectRefereMobile: String = "",
    @SerializedName("ProspectRefereEmail") val ProspectRefereEmail: String = "",
    @SerializedName("CreationDate") val creationDate: String = "",
    @SerializedName("contact") private val contact: CreateLeadContact,
    @SerializedName("leadProductMapping") private val productMapping: List<CreateLeadProduct>,
    @SerializedName("ReferralEmpCode") val referralEmpCode: String = "",
    @SerializedName("NriRiID") val nriRiId: String = "",
    @SerializedName("ExpectedMargin") val expectedMargin: String = "",
    @SerializedName("AccountTypeID") val leadAccountTypeId: String = "-1",
    @SerializedName("ChipsEventIdName") val chipsEventIdName: String? = ""
)

data class CreateLeadContact(
    @SerializedName("Name") val name: String = "",
    @SerializedName("DOB") val dob: String = "",
    @SerializedName("BANKACCNO") val bankAccountNumber: String = "",
    @SerializedName("EmailId") val eMail: String = "",
    @SerializedName("AlternateEmailId") val alternateEmail: String = "",
    @SerializedName("AlternateMobileNo") val alternateMobileNo: String = "",
    @SerializedName("Mobile") val mobileNo: String = "",
    @SerializedName("MobileRi") val mobileNoRi: String = "",
    @SerializedName("LandlineNo") val landLineNo: String = "",
    @SerializedName("EKYCMobileNo") val ekycMobileNo: String = "",
    @SerializedName("PanNumber") val panNo: String = "",
    @SerializedName("AadharNumber") val aadharNo: String = "",
    @SerializedName("IsSameAsAbove") val sameAsAbove: Boolean = false,
    @SerializedName("WM_RMCheckFlag") val WMCodeCheck: Boolean = false,
    @SerializedName("contactAddress") val addresses: List<CreateLeadAddressDetails>
)

data class CreateLeadAddressDetails(
    @SerializedName("AddressTypeID") val addressTypeID: Int = 0,
    @SerializedName("Address1") val address1: String = "",
    @SerializedName("Address2") val address2: String = "",
    @SerializedName("Address3") val address3: String? = "",
    @SerializedName("StateID") val stateID: String? = "",
    @SerializedName("CityID") val cityID: String? = "",
    @SerializedName("CountryID") val countryID: String? = "",
    @SerializedName("Pincode") val pincode: String = "",
    @SerializedName("isPrimary") val isPrimary: Boolean = false,
    @SerializedName("BusinessUnitID") val businessUnitID: String = ""
)

data class CreateLeadProduct(
    @SerializedName("InterestedIn") val interestedIn: String = "",
    @SerializedName("ProductID") val productID: String?,
    @SerializedName("PrimaryRM") val primaryRM: String?,
    @SerializedName("ClientCode") val clientCode: String?,
    @SerializedName("AssignedBy") val assignedBy: String?,
    @SerializedName("AssignedByDate") val assignedByDate: String?,
    @SerializedName("StageID") val stageID: String,
    @SerializedName("StatusID") val statusID: String,
    @SerializedName("SecondaryRM") val secondaryRM: String?
)
    
    
    