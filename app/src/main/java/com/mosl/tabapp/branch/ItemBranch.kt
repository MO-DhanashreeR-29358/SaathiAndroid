package com.mosl.tabapp.branch

import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.BranchItemBinding
import com.xwray.groupie.databinding.BindableItem

class ItemBranch(val branchItemResponse: BranchItemResponse) : BindableItem<BranchItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.branch_item
    }


    override fun bind(viewBinding: BranchItemBinding, position: Int) {
        with(viewBinding) {
            tvBranch.text = branchItemResponse.branchCode
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemBranch

        if (branchItemResponse != other.branchItemResponse) return false

        return true
    }

    override fun hashCode(): Int {
        return branchItemResponse.hashCode()
    }
}