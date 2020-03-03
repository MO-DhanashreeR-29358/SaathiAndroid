package com.mosl.tabapp.dashboard.trends

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.HorinzontalWidgetItemBinding
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.subjects.PublishSubject

class HorinzatalTrendsWidgetItem(
    val dashboardGroupModel: DashboardGroupModel,
    private val publishSubject: PublishSubject<TrendsWidgetUiEvent>,
    val context: Context
) : BindableItem<HorinzontalWidgetItemBinding>() {
    val widgetModel = dashboardGroupModel.widgetList.get(0)
    override fun getLayout(): Int {
        return R.layout.horinzontal_widget_item
    }

    fun updateItem(status: Boolean) {
        dashboardGroupModel.isSelected = status
        notifyChanged()
    }

    override fun bind(viewBinding: HorinzontalWidgetItemBinding, position: Int) {
        viewBinding.apply {
            if (dashboardGroupModel.isSelected()) {
                tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.white))
                setImage(widgetModel.getIconUrl2().replace(" ", "%20"), imageView)
                tvWidgetName.setText(dashboardGroupModel.displayName)
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.secondaryBackground)
                )

            } else {
                tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.txt_dselect))
                setImage(widgetModel.getIconUrl1().replace(" ", "%20"), imageView)
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

    private fun setImage(
        imageUrl: String,
        imageView: ImageView
    ) {
        Glide.with(context)
            .load(imageUrl) //remove Chache
            .asBitmap()
            .placeholder(R.drawable.ic_white)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(
                            context.getResources(),
                            resource
                        )
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }


}