package com.mosl.mobile.domain.setappointment

data class StageWiseFieldConfigurationItemResponse(

  @JvmField val isMandetory: Boolean,

  @JvmField val status: Int,

  @JvmField val creationDate: String,

  @JvmField val lastModifiedBy: Any?,

  @JvmField val createdBy: String,

  @JvmField val fieldSystemName: String,

  @JvmField val isHide: Boolean,

  @JvmField val flagForLeadProductMapping: String,

  @JvmField val forDefaultProductOnly: Boolean,

  @JvmField val identifierPropertyName: String,

  @JvmField val businessUnitId: Int,

  @JvmField val stageId: Int,

  @JvmField val flagForLead: String,

  @JvmField val inApproval: Boolean,

  @JvmField val fieldDisplayName: String,

  @JvmField val approvalFieldList: Any?,

  @JvmField val lastModificationDate: String,

  @JvmField val statusId: Int,

  @JvmField val entityStatus: Int,

  @JvmField val iD: Int
)