package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport

import androidx.core.content.ContextCompat
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.databinding.HorinzontalWidgetItemBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.rxjava3.subjects.PublishSubject

class HorinzatalIFAUniqueWidgetItem (
    val widgetModel: WidgetModel,
    private val publishSubject: PublishSubject<IndirectIFAUniqueUiEvent>
) : BindableItem<HorinzontalWidgetItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.horinzontal_widget_item
    }

    fun updateItem(status: Boolean) {
        widgetModel.isSelected = status
        notifyChanged()
    }

    override fun bind(viewBinding: HorinzontalWidgetItemBinding, position: Int) {
        val context = viewBinding.root.context
        viewBinding.apply {
            if (widgetModel.isSelected()) {
                tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.white))
                CRMAppUtil.setImageUrl(
                    widgetModel.getIconUrl2().replace(" ", "%20"),
                    imageView,
                    context
                )
                tvWidgetName.setText(widgetModel.getName())
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.secondaryBackground)
                )

            } else {
                tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.txt_dselect))
                CRMAppUtil.setImageUrl(
                    widgetModel.getIconUrl1().replace(" ", "%20"),
                    imageView,
                    context
                )
                tvWidgetName.setText(widgetModel.getName())
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            cardView.setOnClickListener {
                if (widgetModel.name.isNotEmpty()) {
                    publishSubject.onNext(
                        IndirectIFAUniqueUiEvent.ItemClick(
                            widgetModel, position
                        )
                    )
                }

            }
        }
    }
}