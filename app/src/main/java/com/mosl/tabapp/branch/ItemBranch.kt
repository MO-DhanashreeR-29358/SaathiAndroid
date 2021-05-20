package com.mosl.tabapp.branch

import androidx.core.content.ContextCompat
import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.BranchItemBinding
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.rxjava3.subjects.PublishSubject

class ItemBranch(
    val branchItemResponse: BranchItemResponse,
    private val publishSubject: PublishSubject<String>
) : BindableItem<BranchItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.branch_item
    }


    override fun bind(viewBinding: BranchItemBinding, position: Int) {
        val context = viewBinding.root.context
        with(viewBinding) {
            tvBranch.text = branchItemResponse.branchCode
            cardView.setOnClickListener {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.secondaryBackground)
                )
                publishSubject.onNext(branchItemResponse.branchCode)
            }
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