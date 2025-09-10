package com.mosl.tabapp.drawermenu.item

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemBaseMenuBinding
import com.mosl.tabapp.drawermenu.data.AppMenu
import com.mosl.tabapp.drawermenu.data.BaseMenu
import com.xwray.groupie.databinding.BindableItem

class BaseMenuItem(
  @JvmField val baseMenu: BaseMenu
) : BindableItem<ItemBaseMenuBinding>(), SelectableMenu {
  private var isSelected: Boolean = false

  override fun getLayout(): Int {
    return R.layout.item_base_menu
  }

  override fun setSelected(selected: Boolean) {
    if (this.isSelected != selected) {
      this.isSelected = selected
      notifyChanged()
    }
  }

  override fun getAppMenu(): AppMenu {
    return baseMenu
  }

  override fun bind(
    viewBinding: ItemBaseMenuBinding,
    position: Int
  ) {
    val txtRoot = viewBinding.txtBaseMenu
    val context = viewBinding.root.context
    txtRoot.setTypeface(null, Typeface.NORMAL)
    txtRoot.setText(baseMenu.label)

    if (this.isSelected) {
      txtRoot.setTextColor(ContextCompat.getColor(context, R.color.white))
      viewBinding.llListItem.setBackgroundColor(
          ContextCompat.getColor(context, R.color.titleBarBackground)
      )
    } else {
      txtRoot.setTextColor(ContextCompat.getColor(context, R.color.black))
      viewBinding.llListItem.setBackgroundColor(
          ContextCompat.getColor(context, R.color.light_white)
      )

    }
    viewBinding.imgGroupImage.setImageResource(baseMenu.icon)
    if (baseMenu.enabled) {
      txtRoot.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }
  }

  override fun getId(): Long {
    return baseMenu.hashCode()
        .toLong()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as BaseMenuItem

    if (baseMenu != other.baseMenu) return false
    if (isSelected != other.isSelected) return false

    return true
  }

  override fun hashCode(): Int {
    var result = baseMenu.hashCode()
    result = 31 * result + isSelected.hashCode()
    return result
  }
}