package com.mosl.tabapp.dashboard.teamtarget

import android.content.Context
import android.view.View
import com.mosl.mobile.domain.teamtarget.TeamTableItem3Model
import com.mosl.shared.util.Utility.VORDIPLOM_COLORS
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetSummaryCountItemBinding
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.subjects.PublishSubject

class TeamTargetSummaryCountItem(
  val itemModel: TeamTableItem3Model,
  private val publishSubject: PublishSubject<TeamWidgetUiEvent>,
  val context: Context,
  val view: TeamTargetView
) : BindableItem<TargetSummaryCountItemBinding>() {

  override fun getLayout(): Int {
    return R.layout.target_summary_count_item
  }

  override fun bind(viewBinding: TargetSummaryCountItemBinding, position: Int) {
    viewBinding.apply {
      txtValue.setText(itemModel.value)
      txtValue.setTextColor(VORDIPLOM_COLORS.get(position))
      txtName.setText(itemModel.displayText)

      if (itemModel.ord == 1) {
        verticalView.visibility = View.GONE
      } else {
        verticalView.visibility = View.VISIBLE
      }

      linearLayout.setOnClickListener {
        if (!itemModel.displayText.equals(TARGET) && !itemModel.value.contains("%") &&
          itemModel.value.toDouble() != 0.0
        ) {
          publishSubject.onNext(TeamWidgetUiEvent.SummaryCountItemClick(itemModel))
        }

      }
    }
  }

  companion object {
    const val TARGET = "Target"
  }

}