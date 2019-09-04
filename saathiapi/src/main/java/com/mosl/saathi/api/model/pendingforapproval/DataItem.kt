package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class DataItem(

  @JvmField @SerializedName("Entity")
  val entity: String,

  @JvmField @SerializedName("EmployeeCode")
  val employeeCode: Any,

  @JvmField @SerializedName("Approver2Remarks")
  val approver2Remarks: Any,

  @JvmField @SerializedName("Approver1Remarks")
  val approver1Remarks: Any,

  @JvmField @SerializedName("UpdationDate")
  val updationDate: String,

  @JvmField @SerializedName("UCID")
  val uCID: Int,

  @JvmField @SerializedName("Name")
  val name: Any,

  @JvmField @SerializedName("EntityId")
  val entityId: Any,

  @JvmField @SerializedName("UpdatedBy")
  val updatedBy: String,

  @JvmField @SerializedName("ClientName")
  val clientName: String,

  @JvmField @SerializedName("ApprovalFieldValue")
  val approvalFieldValue: String,

  @JvmField @SerializedName("Approver1StatusUpdateDate")
  val approver1StatusUpdateDate: Any,

  @JvmField @SerializedName("ClientCode")
  val clientCode: String,

  @JvmField @SerializedName("LeadID")
  val leadID: Int,

  @JvmField @SerializedName("FieldValue")
  val fieldValue: Any,

  @JvmField @SerializedName("Approver2StatusUpdateDate")
  val approver2StatusUpdateDate: Any,

  @JvmField @SerializedName("EmployeeName")
  val employeeName: String,

  @JvmField @SerializedName("FinalStatus")
  val finalStatus: Int,

  @JvmField @SerializedName("EntityField")
  val entityField: Any,

  @JvmField @SerializedName("ReferenceName")
  val referenceName: String,

  @JvmField @SerializedName("ApprovalRequestId")
  val approvalRequestId: Int
)