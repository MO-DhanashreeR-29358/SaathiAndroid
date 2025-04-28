package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target

import android.content.Context
import android.graphics.Color
import com.mosl.mobile.domain.saathiIndirect.dashboard.Graph
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetLableItemBinding
import com.xwray.groupie.databinding.BindableItem

class IndirectCncfocoTargetLableItem(
    val sysName : String?,
    val itemModel: Graph,
    val context: Context,
    val auTargetView: IndirectCncTargetView
): BindableItem<TargetLableItemBinding>() {


    override fun getLayout(): Int {
        return R.layout.target_lable_item
    }


    override fun bind(viewBinding: TargetLableItemBinding, position: Int) {
        viewBinding.apply {
            if(sysName == IndirectCncfocoTragetPresenter.REVENUE_GROWTH_DEGROWTH){
                tvLabel.setText(itemModel.product)
                val txt = "CM : " + itemModel.currentMonth +"\n" + "LM : " + itemModel.lastMonth
                tvPercentage.setText(txt)
                view.setBackgroundColor(Color.rgb(128, 128, 128))
            }else{
                tvLabel.setText(itemModel.product)
                tvPercentage.setText(itemModel.Value)
                view.setBackgroundColor(auTargetView.getColorsArray()[position])
            }

        }
    }

}