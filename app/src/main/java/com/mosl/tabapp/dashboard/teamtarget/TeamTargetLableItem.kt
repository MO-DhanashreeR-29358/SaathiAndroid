package com.mosl.tabapp.dashboard.teamtarget

import android.content.Context
import com.mosl.mobile.domain.teamtarget.TeamTableItemModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetLableItemBinding
import com.xwray.groupie.databinding.BindableItem

class TeamTargetLableItem(
  val itemModel: TeamTableItemModel,
  val context: Context,
  val auTargetView: TeamTargetView
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