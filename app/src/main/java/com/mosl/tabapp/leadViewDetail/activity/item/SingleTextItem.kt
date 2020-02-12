package com.mosl.tabapp.leadViewDetail.activity.item

import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemSingleTextBinding
import com.xwray.groupie.databinding.BindableItem

class SingleTextItem(
  private val text: String
) : BindableItem<ItemSingleTextBinding>() {

  override fun getLayout(): Int {
    return R.layout.item_single_text
  }

  override fun bind(
    viewBinding: ItemSingleTextBinding,
    position: Int
  ) {
    viewBinding.tvText.text = text
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as SingleTextItem

    if (text != other.text) return false

    return true
  }

  override fun hashCode(): Int {
    return text.hashCode()
  }

}