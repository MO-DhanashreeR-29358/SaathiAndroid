package com.mosl.mobile.domain.pendingapproval

class PendingForApprovalItem(
  @JvmField val entity: String,

  @JvmField val updationDate: String,

  @JvmField val ucid: String,

  @JvmField val updatedBy: String,

  @JvmField val clientName: String,

  @JvmField val approvalFieldValue: String,

  @JvmField val clientCode: String,

  @JvmField val leadID: String,

  @JvmField
  val approver2StatusUpdateDate: String,

  @JvmField
  val employeeName: String,

  @JvmField
  val finalStatus: String,

  @JvmField
  val referenceName: String,

  @JvmField
  val approvalRequestId: String
) {

  fun getStatus(): String {
    return when (finalStatus) {

      "1" -> "Pending"
      "2" -> "Approve"
      "3" -> "Reject"

      else -> "Pending"

    }
  }

}

