package com.mosl.tabapp.saathiIndirect.dashboard.foco.target

import android.content.Context
import com.mosl.mobile.domain.saathiIndirect.dashboard.Graph
import com.mosl.mobile.domain.target.Table1ItemModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetLableItemBinding
import com.xwray.groupie.databinding.BindableItem

class FocoTargetLableItem(
    val itemModel: Graph,
    val context: Context,
    val auTargetView: FocoTargetView
) : BindableItem<TargetLableItemBinding>() {


    override fun getLayout(): Int {
        return R.layout.target_lable_item
    }


    override fun bind(viewBinding: TargetLableItemBinding, position: Int) {
        viewBinding.apply {
            tvLabel.setText(itemModel.product)
            tvPercentage.setText(itemModel.Value)
            view.setBackgroundColor(auTargetView.getColorsArray()[position])

        }
    }


}