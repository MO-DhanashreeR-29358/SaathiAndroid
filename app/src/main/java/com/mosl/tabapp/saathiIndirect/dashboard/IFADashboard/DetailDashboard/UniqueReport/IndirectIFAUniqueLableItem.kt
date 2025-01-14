package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport

import android.content.Context
import android.graphics.Color
import com.mosl.mobile.domain.saathiIndirect.dashboard.Graph
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetLableItemBinding
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetView
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter
import com.xwray.groupie.databinding.BindableItem

class IndirectIFAUniqueLableItem (
    val sysName : String?,
    val itemModel: Graph,
    val context: Context,
    val auTargetView: IndirectIFAUniqueView
): BindableItem<TargetLableItemBinding>() {


    override fun getLayout(): Int {
        return R.layout.target_lable_item
    }


    override fun bind(viewBinding: TargetLableItemBinding, position: Int) {
        viewBinding.apply {
            if(sysName == FocoTargetPresenter.REVENUE_GROWTH_DEGROWTH){
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