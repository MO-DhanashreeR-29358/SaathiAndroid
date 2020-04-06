package com.mosl.tabapp.dashboard.actionableinsight

import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemActionableInsightBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem

class ActionableInsightItem(val data: WidgetModel?) : BindableItem<ItemActionableInsightBinding>() {
  override fun getLayout(): Int {
    return R.layout.item_actionable_insight
  }

  override fun bind(
    viewBinding: ItemActionableInsightBinding,
    position: Int
  ) {
    viewBinding.txtActionableInsight.setText(data!!.name)
    viewBinding.txtCount.setText(data.actionableCount)
    Picasso.with(viewBinding.root.context)
        .load(data.iconUrl2)
        .placeholder(R.drawable.ic_create_meeting)
        .into(viewBinding.imgActionableInsights)
  }
}