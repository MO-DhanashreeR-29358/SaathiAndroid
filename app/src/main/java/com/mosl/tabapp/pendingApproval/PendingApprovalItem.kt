package com.mosl.tabapp.pendingApproval

import com.mosl.mobile.domain.pendingapproval.ApprovalSummaryByEmployeeItemModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.PendingApprovalsListBinding
import com.xwray.groupie.databinding.BindableItem

class PendingApprovalItem(
  internal val model: ApprovalSummaryByEmployeeItemModel
) : BindableItem<PendingApprovalsListBinding>() {

  override fun getLayout(): Int {
    return R.layout.pending_approvals_list
  }

  override fun bind(
    viewBinding: PendingApprovalsListBinding,
    position: Int
  ) {
    viewBinding.apply {
      tvpendingcount.text = model.pendingCount
      tvpendinglead.text = model.approvedCount

    }
  }

  fun getEntityField(): String {
    return model.entityField
  }

  override fun hashCode(): Int {
    return model.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PendingApprovalItem

    if (model != other.model) return false

    return true
  }
}