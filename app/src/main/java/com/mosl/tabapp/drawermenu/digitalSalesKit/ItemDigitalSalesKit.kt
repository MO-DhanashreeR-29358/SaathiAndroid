package com.mosl.tabapp.drawermenu.digitalSalesKit

import androidx.core.content.ContextCompat
import com.mosl.mobile.domain.branch.BranchItemResponse
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.BranchItemBinding
import com.mosl.tabapp.databinding.DigitalSalesKitItemBinding
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.rxjava3.subjects.PublishSubject

class ItemDigitalSalesKit(
    val childMenu: DigitalSalesKitData,
    private val publishSubject: PublishSubject<String>
) : BindableItem<DigitalSalesKitItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.digital_sales_kit_item
    }


    override fun bind(viewBinding: DigitalSalesKitItemBinding, position: Int) {
        val context = viewBinding.root.context
        with(viewBinding) {
            tvChildMenu.text = childMenu.systemName
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.white))
            cardView.setOnClickListener {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.secondaryBackground)
                )
                publishSubject.onNext(childMenu.pdfPath)
            }
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemDigitalSalesKit

        return true
    }

    override fun hashCode(): Int {
        return childMenu.hashCode()
    }
}