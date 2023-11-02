package com.mosl.tabapp.dashboard.trends

import androidx.core.content.ContextCompat
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil.setImageUrl
import com.mosl.tabapp.databinding.HorinzontalWidgetItemBinding
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.rxjava3.subjects.PublishSubject

class HorinzatalTrendsWidgetItem(
    val dashboardGroupModel: DashboardGroupModel,
    private val publishSubject: PublishSubject<TrendsWidgetUiEvent>
) : BindableItem<HorinzontalWidgetItemBinding>() {
    override fun getLayout(): Int {
        return R.layout.horinzontal_widget_item
    }

    fun updateItem(status: Boolean) {
        dashboardGroupModel.isSelected = status
        notifyChanged()
    }

    override fun bind(viewBinding: HorinzontalWidgetItemBinding, position: Int) {
        val context = viewBinding.root.context
        viewBinding.apply {
            if (dashboardGroupModel.widgetList.size > 0) {
                val widgetModel = dashboardGroupModel.widgetList.get(0)
                if (dashboardGroupModel.isSelected()) {
                    tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.white))
                    setImageUrl(widgetModel.getIconUrl2().replace(" ", "%20"), imageView, context)
                    tvWidgetName.setText(dashboardGroupModel.displayName)
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.secondaryBackground)
                    )

                } else {
                    tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.txt_dselect))
                    setImageUrl(widgetModel.getIconUrl1().replace(" ", "%20"), imageView, context)
                    tvWidgetName.setText(dashboardGroupModel.displayName)
                    cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }

                cardView.setOnClickListener {
                    if (dashboardGroupModel.displayName.isNotEmpty()) {
                        publishSubject.onNext(
                            TrendsWidgetUiEvent.ItemClick(
                                dashboardGroupModel, position
                            )
                        )
                    }

                }
            }
        }
    }


}