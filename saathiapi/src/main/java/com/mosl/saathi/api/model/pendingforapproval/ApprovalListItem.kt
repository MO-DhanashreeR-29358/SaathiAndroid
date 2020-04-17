package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class ApprovalListItem(

  @JvmField @SerializedName("Entity")
  val entity: String,

  @JvmField @SerializedName("EmployeeCode")
  val employeeCode: String?,

  @JvmField @SerializedName("Approver2Remarks")
  val approver2Remarks: String?,

  @JvmField @SerializedName("Approver1Remarks")
  val approver1Remarks: String?,

  @JvmField @SerializedName("UpdationDate")
  val updationDate: String,

  @JvmField @SerializedName("UCID")
  val uCID: String,

  @JvmField @SerializedName("Name")
  val name: String?,

  @JvmField @SerializedName("EntityId")
  val entityId: String?,

  @JvmField @SerializedName("UpdatedBy")
  val updatedBy: String,

  @JvmField @SerializedName("ClientName")
  val clientName: String,

  @JvmField @SerializedName("ApprovalFieldValue")
  val approvalFieldValue: String,

  @JvmField @SerializedName("Approver1StatusUpdateDate")
  val approver1StatusUpdateDate: String?,

  @JvmField @SerializedName("ClientCode")
  val clientCode: String,

  @JvmField @SerializedName("LeadID")
  val leadID: String,

  @JvmField @SerializedName("FieldValue")
  val fieldValue: String?,

  @JvmField @SerializedName("Approver2StatusUpdateDate")
  val approver2StatusUpdateDate: String?,

  @JvmField @SerializedName("EmployeeName")
  val employeeName: String,

  @JvmField @SerializedName("FinalStatus")
  val finalStatus: String,

  @JvmField @SerializedName("EntityField")
  val entityField: String?,

  @JvmField @SerializedName("ReferenceName")
  val referenceName: String,

  @JvmField @SerializedName("ApprovalRequestId")
  val approvalRequestId: String
)