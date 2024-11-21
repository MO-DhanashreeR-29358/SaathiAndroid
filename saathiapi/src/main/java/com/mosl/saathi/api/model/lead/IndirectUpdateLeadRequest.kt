package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class IndirectUpdateLeadRequest(
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
    @JvmField @SerializedName("ProfessionID") val professionID: String,
    @JvmField @SerializedName("CreatedBy") val createdBy: String?,
    @JvmField @SerializedName("ProspectRefereName") val prospectRefereName: String,
    @JvmField @SerializedName("ProspectRefereMobile") val prospectRefereMobile: String,
    @JvmField @SerializedName("ProspectRefereEmail") val prospectRefereEmail: String,
    @JvmField @SerializedName("CreatedThrough") val createdThrough: String,
    @JvmField @SerializedName("CreationDate") val creationDate: String,
    @JvmField @SerializedName("contact") val contact: IndirectContactRequest,
    @JvmField @SerializedName(
        "leadFieldValueModels"
  ) val leadFieldValueModels: List<IndirectLeadFieldValueRequest>,
    @JvmField @SerializedName("ExpectedMargin") val expectedMargin: String?,
    @JvmField @SerializedName("ActivateOTPInApp") val ActivateOTPInApp: String?,
    @JvmField @SerializedName("BG_ID") val bgId: String?,
    @JvmField @SerializedName("WorkExp_ID") val workExpId: String?,
    @JvmField @SerializedName("ExistingBrokerID") val existingBrokerID: String?,
    @JvmField @SerializedName("businessName") val businessName: String?,
    @JvmField @SerializedName("AssociatedBroker") val associatedBroker: String?,
    @JvmField @SerializedName("LastMeetingDate") val lastMeetingDate: String?,
    @JvmField @SerializedName("RFMName") val RFMName: String?,
    @JvmField @SerializedName("TMName") val TMName: String?,
    @JvmField @SerializedName("CurrentBrokeragePerMonth") val currentBrokeragePerMonth: String?,
    @JvmField @SerializedName("BAType") val BAType: String?
)

data class IndirectLeadFieldValueRequest(
  @JvmField @SerializedName("LF_SysName") val lF_SysName: String?,
  @JvmField @SerializedName("LeadField_Value") val leadField_Value: String?,
  @JvmField @SerializedName("LeadFieldId") val leadFieldId: Int,
  @JvmField @SerializedName("LeadID") val leadID: String?
)

data class IndirectContactRequest(
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
  @JvmField @SerializedName("contactAddress") val contactAddress: List<IndirectContactAddressRequest>?
)

data class IndirectContactAddressRequest(
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