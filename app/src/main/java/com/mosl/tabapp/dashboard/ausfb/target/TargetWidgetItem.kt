package com.mosl.tabapp.dashboard.ausfb.target

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetAchievementWidgetItemBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.subjects.PublishSubject

class TargetWidgetItem(
    val widgetModel: WidgetModel,
    private val publishSubject: PublishSubject<WidgetUiEvent>,
    val context: Context
) : BindableItem<TargetAchievementWidgetItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.target_achievement_widget_item
    }

    fun updateItem(status: Boolean) {
        widgetModel.isSelected = status
        notifyChanged()
    }

    override fun bind(viewBinding: TargetAchievementWidgetItemBinding, position: Int) {
        viewBinding.apply {
            if (widgetModel.isSelected()) {
                tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.white))
                setImage(widgetModel.getIconUrl2().replace(" ", "%20"), imageView)
                tvWidgetName.setText(widgetModel.getName())
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.secondaryBackground)
                )

            } else {
                tvWidgetName.setTextColor(ContextCompat.getColor(context, R.color.txt_dselect))
                setImage(widgetModel.getIconUrl1().replace(" ", "%20"), imageView)
                tvWidgetName.setText(widgetModel.getName())
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            cardView.setOnClickListener {
                if (widgetModel.name.isNotEmpty()) {
                    publishSubject.onNext(
                        WidgetUiEvent.ItemClick(
                            widgetModel, position
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