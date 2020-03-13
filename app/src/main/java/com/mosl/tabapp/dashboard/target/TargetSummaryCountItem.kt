package com.mosl.tabapp.dashboard.target

import android.content.Context
import android.view.View
import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.shared.util.Utility
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetSummaryCountItemBinding
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.subjects.PublishSubject

class TargetSummaryCountItem(
    val itemModel: TableItemModel,
    private val publishSubject: PublishSubject<WidgetUiEvent>,
    val context: Context,
    val view: TargetView
) : BindableItem<TargetSummaryCountItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.target_summary_count_item
    }


    override fun bind(viewBinding: TargetSummaryCountItemBinding, position: Int) {
        viewBinding.apply {
            txtValue.setText(itemModel.value)
          txtValue.setTextColor(Utility.VORDIPLOM_COLORS.get(position))
            txtName.setText(itemModel.displayText)
            if (itemModel.ord == 1) {
                verticalView.visibility = View.GONE
            } else {
                verticalView.visibility = View.VISIBLE
            }

            linearLayout.setOnClickListener {
                if (!itemModel.displayText.equals(TARGET) &&
                    itemModel.value!!.toDouble() != 0.0
                ) {
                    publishSubject.onNext(WidgetUiEvent.SummaryCountItemClick(itemModel))
                }

            }
        }
    }

    companion object {
        const val TARGET = "Target"
    }

}