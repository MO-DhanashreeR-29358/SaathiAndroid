package com.mosl.tabapp.dashboard.ausfb.target

import android.content.Context
import com.mosl.mobile.domain.target.Table1ItemModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetLableItemBinding
import com.xwray.groupie.databinding.BindableItem

class TargetLableItem(
    val itemModel: Table1ItemModel,
    val context: Context,
    val auTargetView: TargetView
) : BindableItem<TargetLableItemBinding>() {


    override fun getLayout(): Int {
        return R.layout.target_lable_item
    }


    override fun bind(viewBinding: TargetLableItemBinding, position: Int) {
        viewBinding.apply {
            tvLabel.setText(itemModel.label)
            tvPercentage.setText(itemModel.formatted)
            view.setBackgroundColor(auTargetView.getColorsArray().get(position))

        }
    }


}