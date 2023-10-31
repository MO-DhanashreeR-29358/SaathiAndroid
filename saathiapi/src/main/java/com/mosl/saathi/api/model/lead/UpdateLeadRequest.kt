package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class UpdateLeadRequest(
    @JvmField @SerializedName("LeadID") val leadID: String,
    @JvmField @SerializedName("BusinessUnitID") val businessUnitID: String,
    @JvmField @SerializedName("IsPotentialPMSProductValue") val isPotentialPMSProductValue: String,
    @JvmField @SerializedName("LeadCategoryID") val leadCategoryID: String?,
    @JvmField @SerializedName("ContactPerson") val contactPerson: String?,
    @JvmField @SerializedName("SourceID") val sourceID: String,
    @JvmField @SerializedName("LeadSourceID") val leadSourceID: String,
    @JvmField @SerializedName("LeadSourceDetailsID") val leadSourceDetailsID: String,
    @JvmField @SerializedName("LeadSourceDetails") val leadSourceDetails: String,
    @JvmField @SerializedName("RefereeCode") val refereeCode: String,
    @JvmField @SerializedName("RefereeName") val refereeName: String,
    @JvmField @SerializedName("RefereeEmail") val refereeEmail: String?,
    @JvmField @SerializedName("RefereeMobile") val refereeMobile: String?,
    @JvmField @SerializedName("Comments") val comments: String?,
    @JvmField @SerializedName("ProfileID") val profileID: String,
    @JvmField @SerializedName("BrokingProfileID") val brokingProfileID: String,
    @JvmField @SerializedName("IPProfileID") val ipProfileID: String,
    @JvmField @SerializedName("FinalProfileID") val finalProfileID: String,
    @JvmField @SerializedName("ProfessionID") val professionID: String,
    @JvmField @SerializedName("CreatedBy") val createdBy: String?,
    @JvmField @SerializedName("ProspectRefereName") val prospectRefereName: String,
    @JvmField @SerializedName("ProspectRefereMobile") val prospectRefereMobile: String,
    @JvmField @SerializedName("ProspectRefereEmail") val prospectRefereEmail: String,
    @JvmField @SerializedName("CreatedThrough") val createdThrough: String,
    @JvmField @SerializedName("CreationDate") val creationDate: String,
    @JvmField @SerializedName("contact") val contact: ContactRequest,
    @JvmField @SerializedName(
        "leadFieldValueModels"
  ) val leadFieldValueModels: List<LeadFieldValueRequest>,
    @JvmField @SerializedName("SectorID") val sectorId: String?,
    @JvmField @SerializedName("Sector") val sector: String?,
    @JvmField @SerializedName("DesigID") val desigId: String?,
    @JvmField @SerializedName("Desig") val desig: String?,
    @JvmField @SerializedName("NetworthID") val networthId: String?,
    @JvmField @SerializedName("Networth") val networth: String?,
    @JvmField @SerializedName("ReferralEmpCodeN") val referralEmpCode: String?,
    @JvmField @SerializedName("ReferralEmpName") val referralEmpName: String?,
    @JvmField @SerializedName("AccountTypeID") val leadAccountTypeId: String
)

data class LeadFieldValueRequest(
  @JvmField @SerializedName("LF_SysName") val lF_SysName: String?,
  @JvmField @SerializedName("LeadField_Value") val leadField_Value: String?,
  @JvmField @SerializedName("LeadFieldId") val leadFieldId: Int,
  @JvmField @SerializedName("LeadID") val leadID: String?
)

data class ContactRequest(
  @JvmField @SerializedName("Name") val name: String,
  @JvmField @SerializedName("EmailId") val emailId: String,
  @JvmField @SerializedName("AlternateEmailId") val alternateEmailId: String,
  @JvmField @SerializedName("Mobile") val mobile: String,
  @JvmField @SerializedName("EKYCMobileNo") val eKYCMobileNo: String,
  @JvmField @SerializedName("LandlineNo") val landlineNo: String,
  @JvmField @SerializedName("AlternateMobileNo") val alternateMobileNo: String,
  @JvmField @SerializedName("AadharNumber") val aadharNumber: String,
  @JvmField @SerializedName("PanNumber") val panNumber: String,
  @JvmField @SerializedName("DOB") val dOB: String,
  @JvmField @SerializedName("DOA") val dOA: String,
  @JvmField @SerializedName("IsSameAsAbove") val isSameAsAbove: Boolean,
  @JvmField @SerializedName("contactAddress") val contactAddress: List<ContactAddressRequest>?
)

data class ContactAddressRequest(
  @JvmField @SerializedName("AddressTypeID") val addressTypeID: String?,
  @JvmField @SerializedName("Address1") val address1: String?,
  @JvmField @SerializedName("Address2") val address2: String?,
  @JvmField @SerializedName("Address3") val address3: String?,
  @JvmField @SerializedName("StateID") val stateID: String?,
  @JvmField @SerializedName("CountryID") val countryID: String?,
  @JvmField @SerializedName("CityID") val cityID: String?,
  @JvmField @SerializedName("Pincode") val pincode: String?,
  @JvmField @SerializedName("isPrimary") val isPrimary: Boolean,
  @JvmField @SerializedName("BusinessUnitID") val businessUnitID: String?
)