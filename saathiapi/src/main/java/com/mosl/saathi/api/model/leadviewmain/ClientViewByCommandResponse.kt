package com.mosl.saathi.api.model.leadviewmain

import com.google.gson.annotations.SerializedName

data class ClientViewByCommandResponse(

  @JvmField @SerializedName("FamilyUCID") val familyUCID: Int,

  @JvmField @SerializedName("BankName") val bankName: String,

  @JvmField @SerializedName("CreationDate") val creationDate: String,

  @JvmField @SerializedName("IsFamilyHead") val isFamilyHead: Boolean,

  @JvmField @SerializedName("Products") val products: List<ProductsItem?>,

  @JvmField @SerializedName("AdvisoryType") val advisoryType: Any,

  @JvmField @SerializedName("Email") val email: Any,

  @JvmField @SerializedName("Address") val address: String,

  @JvmField @SerializedName("ProductName") val productName: Any,

  @JvmField @SerializedName("ClientProfile") val clientProfile: Any,

  @JvmField @SerializedName("Sourcing") val sourcing: Any,

  @JvmField @SerializedName("AccountOpeningDate") val accountOpeningDate: String,

  @JvmField @SerializedName("UCID") val uCID: Int,

  @JvmField @SerializedName("Profile") val profile: Any,

  @JvmField @SerializedName("ClientName") val clientName: String? = "",

  @JvmField @SerializedName("PendingDays") val pendingDays: String? = "",

  @JvmField @SerializedName("PANNumber") val pANNumber: String,

  @JvmField @SerializedName("PrimaryAdvisor") val primaryAdvisor: Any,

  @JvmField @SerializedName("DOB") val dOB: String,

  @JvmField @SerializedName("SubBrokerCode") val subBrokerCode: Any,

  @JvmField @SerializedName("CreatedBy") val createdBy: Any,

  @JvmField @SerializedName("EmployeeCode") val employeeCode: Any,

  @JvmField @SerializedName("OAOUserid") val oAOUserid: Int? = 0,

  @JvmField @SerializedName("MappedBy") val mappedBy: Any,

  @JvmField @SerializedName("AccountNo") val accountNo: Any,

  @JvmField @SerializedName("StageName") val stageName: String? = "",

  @JvmField @SerializedName("Channel") val channel: String,

  @JvmField @SerializedName("DefaultBank") val defaultBank: Any,

  @JvmField @SerializedName("SecondaryAdvisor") val secondaryAdvisor: Any,

  @JvmField @SerializedName("FamilyHead") val familyHead: Any,

  @JvmField @SerializedName("AccountType") val accountType: Any,

  @JvmField @SerializedName("Mobile") val mobile: String? = "",

  @JvmField @SerializedName("VintagewithMotilalOswalYears") val vintagewithMotilalOswalYears: Int,

  @JvmField @SerializedName("AadhaarNumber") val aadhaarNumber: String,

  @JvmField @SerializedName("ClientProfileComplition") val clientProfileComplition: String,

  @JvmField @SerializedName("ClientCode") val clientCode: Any,

  @JvmField @SerializedName("AreaCode") val areaCode: String,

  @JvmField @SerializedName("BankAccountType") val bankAccountType: String,

  @JvmField @SerializedName("LeadID") val leadID: String? = "",

  @JvmField @SerializedName("IsPotentialPMSProduct") val isPotentialPMSProduct: String,

  @JvmField @SerializedName("MappingDate") val mappingDate: Any,

  @JvmField @SerializedName("BranchCode") val branchCode: String,

  @JvmField @SerializedName("BankAccountNumber") val bankAccountNumber: String,

  @JvmField @SerializedName("IsOnlyClient") val isOnlyClient: Boolean
)