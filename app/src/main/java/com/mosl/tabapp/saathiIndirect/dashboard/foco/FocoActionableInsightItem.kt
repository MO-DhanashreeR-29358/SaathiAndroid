package com.mosl.tabapp.saathiIndirect.dashboard.foco

import android.view.View
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemActionableInsightBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem

class FocoActionableInsightItem(val data: String) : BindableItem<ItemActionableInsightBinding>() {
  override fun getLayout(): Int {
    return R.layout.item_actionable_insight
  }

  override fun bind(
    viewBinding: ItemActionableInsightBinding,
    position: Int
  ) {
    viewBinding.txtActionableInsight.setText(data)
    viewBinding.txtCount.visibility = View.GONE
    viewBinding.imgActionableInsights.visibility = View.GONE
    viewBinding.txtActionableInsight.textSize = 20F
  }
}