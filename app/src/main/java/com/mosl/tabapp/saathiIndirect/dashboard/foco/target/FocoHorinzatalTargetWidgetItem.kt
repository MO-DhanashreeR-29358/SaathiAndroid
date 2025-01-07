package com.mosl.tabapp.saathiIndirect.dashboard.foco.target

import android.view.View
import androidx.core.content.ContextCompat
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil.setImageUrl
import com.mosl.tabapp.databinding.FocoHorinzontalWidgetItemBinding
import com.mosl.tabapp.databinding.HorinzontalWidgetItemBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.rxjava3.subjects.PublishSubject

class FocoHorinzatalTargetWidgetItem(
  val widgetModel: WidgetModel,
  private val publishSubject: PublishSubject<FocoTargetUiEvent>
) : BindableItem<FocoHorinzontalWidgetItemBinding>() {

  override fun getLayout(): Int {
    return R.layout.foco_horinzontal_widget_item
  }

  fun updateItem(status: Boolean) {
    widgetModel.isSelected = status
    notifyChanged()
  }

  override fun bind(viewBinding: FocoHorinzontalWidgetItemBinding, position: Int) {
    val context = viewBinding.root.context
    viewBinding.apply {
      if (widgetModel.isSelected()) {
        tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.white))
        tvWidgetName.setText(widgetModel.getName())
        cardView.setCardBackgroundColor(
          ContextCompat.getColor(context, R.color.secondaryBackground)
        )

      } else {
        tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.txt_dselect))
        tvWidgetName.setText(widgetModel.getName())
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
      }

      cardView.setOnClickListener {
        if (widgetModel.name.isNotEmpty()) {
          publishSubject.onNext(
            FocoTargetUiEvent.ItemClick(
              widgetModel, position
            )
          )
        }

      }
    }
  }



}