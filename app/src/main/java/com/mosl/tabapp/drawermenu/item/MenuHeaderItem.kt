package com.mosl.tabapp.drawermenu.item

import android.view.View
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ViewMenuHeaderBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.rxjava3.subjects.PublishSubject

class MenuHeaderItem(
  val partnerName: String,
  val businessProfileName: String?,
  val imageUrl: String,
  val closeSelectSubject: PublishSubject<View>
) : BindableItem<ViewMenuHeaderBinding>() {

  override fun getLayout(): Int {
    return R.layout.view_menu_header
  }

  override fun bind(
    viewBinding: ViewMenuHeaderBinding,
    position: Int
  ) {
    val context = viewBinding.root.context
    viewBinding.txtPartnerName.setText(partnerName)
    viewBinding.txtBusinessProfileName.setText(businessProfileName)

    viewBinding.imgClose.setOnClickListener(View.OnClickListener { it ->
      closeSelectSubject.onNext(it);
    })

    if (!imageUrl.isEmpty()) {
      Picasso.with(context)
          .load(imageUrl)
          .placeholder(R.drawable.menu_profile_pic)
          .into(viewBinding.imgPartner)
    }

  }
}
