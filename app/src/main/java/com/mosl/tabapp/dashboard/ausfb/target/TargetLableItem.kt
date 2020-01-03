package com.mosl.tabapp.dashboard.ausfb.target

import android.content.Context
import android.graphics.Color
import com.mosl.mobile.domain.autarget.Table1ItemModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetLableItemBinding
import com.xwray.groupie.databinding.BindableItem

class TargetLableItem(
    val itemModel: Table1ItemModel,
    val context: Context
) : BindableItem<TargetLableItemBinding>() {
    private val colors_array = intArrayOf(
        Color.rgb(255, 21, 0),
        Color.rgb(38, 127, 183),
        Color.rgb(0, 177, 207),
        Color.rgb(22, 225, 143),
        Color.rgb(64, 0, 255),
        Color.rgb(255, 255, 0),
        Color.rgb(254, 46, 46),
        Color.rgb(255, 21, 0),
        Color.rgb(38, 127, 183),
        Color.rgb(0, 177, 207),
        Color.rgb(22, 225, 143),
        Color.rgb(64, 0, 255),
        Color.rgb(255, 255, 0),
        Color.rgb(254, 46, 46),
        Color.rgb(64, 0, 255),
        Color.rgb(255, 255, 0),
        Color.rgb(254, 46, 46)
    )

    override fun getLayout(): Int {
        return R.layout.target_lable_item
    }


    override fun bind(viewBinding: TargetLableItemBinding, position: Int) {
        viewBinding.apply {
            tvLabel.setText(itemModel.label)
            tvPercentage.setText(itemModel.formatted)
            view.setBackgroundColor(colors_array.get(position))

        }
    }


}