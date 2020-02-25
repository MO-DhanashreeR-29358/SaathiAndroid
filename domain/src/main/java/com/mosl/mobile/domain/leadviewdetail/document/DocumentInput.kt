package com.mosl.mobile.domain.leadviewdetail.document

data class DocumentInput(

  @JvmField val requestLeadDocuments: LeadDocumentsInput,

  @JvmField val filterRequest: FilterRequestInput,

  @JvmField val adminMode: Boolean
)

data class LeadDocumentsInput(

  @JvmField val leadID: String,

  @JvmField val uCID: String,

  @JvmField val pageSize: String = "10",

  @JvmField val page: String = "1",

  @JvmField val calculatedPage: String = "0",

  @JvmField val totalCount: String = "0"

)

data class FilterRequestInput(

  @JvmField val operator: String? = null,

  @JvmField val field: String? = null,

  @JvmField val filters: String? = null,

  @JvmField val exportFilter: String? = null,

  @JvmField val value: String? = null,

  @JvmField val export: String? = null,

  @JvmField val logic: String? = null
)

data class LeadDocumentsItemData(

  @JvmField val id: String?,

  @JvmField val documentName: String?,

  @JvmField val documentType: String?,

  @JvmField val documentPath: String?,

  @JvmField val creationDate: String?,

  @JvmField val documentFullPath: String?

)

data class UpdateDocumentsInput(
  val id: String,
  val documentName: String
)

