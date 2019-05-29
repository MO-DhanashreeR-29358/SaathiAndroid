package com.mosl.saathi.api.model.clientdetail

import com.google.gson.annotations.SerializedName

data class ClientDetailItem(

  @SerializedName("FamilyUCID")
  @JvmField val familyUCID: Int,

  @SerializedName("BankName")
  @JvmField val bankName: String,

  @SerializedName("CreationDate")
  @JvmField val creationDate: String,

  @SerializedName("IsFamilyHead")
  @JvmField val isFamilyHead: Boolean,

  @SerializedName("Products")
  @JvmField val products: List<ProductsItem?>,

  @SerializedName("AdvisoryType")
  @JvmField val advisoryType: Any,

  @SerializedName("Email")
  @JvmField val email: Any,

  @SerializedName("Address")
  @JvmField val address: String,

  @SerializedName("ProductName")
  @JvmField val productName: Any,

  @SerializedName("ClientProfile")
  @JvmField val clientProfile: Any,

  @SerializedName("Sourcing")
  @JvmField val sourcing: Any,

  @SerializedName("AccountOpeningDate")
  @JvmField val accountOpeningDate: String,

  @SerializedName("UCID")
  @JvmField val uCID: Int,

  @SerializedName("Profile")
  @JvmField val profile: Any,

  @SerializedName("ClientName")
  @JvmField val clientName: String,

  @SerializedName("PendingDays")
  @JvmField val pendingDays: String,

  @SerializedName("PANNumber")
  @JvmField val pANNumber: String,

  @SerializedName("PrimaryAdvisor")
  @JvmField val primaryAdvisor: Any,

  @SerializedName("DOB")
  @JvmField val dOB: String,

  @SerializedName("SubBrokerCode")
  @JvmField val subBrokerCode: Any,

  @SerializedName("CreatedBy")
  @JvmField val createdBy: Any,

  @SerializedName("EmployeeCode")
  @JvmField val employeeCode: Any,

  @SerializedName("OAOUserid")
  @JvmField val oAOUserid: Int,

  @SerializedName("MappedBy")
  @JvmField val mappedBy: Any,

  @SerializedName("AccountNo")
  @JvmField val accountNo: Any,

  @SerializedName("StageName")
  @JvmField val stageName: String,

  @SerializedName("Channel")
  @JvmField val channel: String,

  @SerializedName("DefaultBank")
  @JvmField val defaultBank: Any,

  @SerializedName("SecondaryAdvisor")
  @JvmField val secondaryAdvisor: Any,

  @SerializedName("FamilyHead")
  @JvmField val familyHead: Any,

  @SerializedName("AccountType")
  @JvmField val accountType: Any,

  @SerializedName("Mobile")
  @JvmField val mobile: Any,

  @SerializedName("VintagewithMotilalOswalYears")
  @JvmField val vintagewithMotilalOswalYears: Int,

  @SerializedName("AadhaarNumber")
  @JvmField val aadhaarNumber: Any,

  @SerializedName("ClientProfileComplition")
  @JvmField val clientProfileComplition: String,

  @SerializedName("ClientCode")
  @JvmField val clientCode: Any,

  @SerializedName("AreaCode")
  @JvmField val areaCode: String,

  @SerializedName("BankAccountType")
  @JvmField val bankAccountType: String,

  @SerializedName("LeadID")
  @JvmField val leadID: Int,

  @SerializedName("IsPotentialPMSProduct")
  @JvmField val isPotentialPMSProduct: Any,

  @SerializedName("MappingDate")
  @JvmField val mappingDate: Any,

  @SerializedName("BranchCode")
  @JvmField val branchCode: String,

  @SerializedName("BankAccountNumber")
  @JvmField val bankAccountNumber: String,

  @SerializedName("IsOnlyClient")
  @JvmField val isOnlyClient: Boolean
)