package com.mosl.tabapp.drawermenu.item

import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ViewMenuHeaderBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem

class MenuHeaderItem(
  val partnerName: String,
  val businessProfileName: String?,
  val imageUrl: String
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

    Picasso.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.menu_profile_pic)
        .into(viewBinding.imgPartner)
  }
}
