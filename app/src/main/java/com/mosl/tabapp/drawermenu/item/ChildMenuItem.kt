package com.mosl.tabapp.drawermenu.item

import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemChildMenuBinding
import com.mosl.tabapp.drawermenu.data.AppMenu
import com.mosl.tabapp.drawermenu.data.ChildMenu
import com.xwray.groupie.databinding.BindableItem

/**
 * Created by lalkishore on 11/02/18.
 */

class ChildMenuItem(
  private val childMenu: ChildMenu
) : BindableItem<ItemChildMenuBinding>(), SelectableMenu {
  private var isSelected: Boolean = false

  /*private val backgroundResource: Int
    get() = if (isSelected) {
      R.drawable.drawer_menu_selected_bg
    } else {
      R.drawable.drawer_menu_bg
    }*/

  override fun getLayout(): Int {
    return R.layout.item_child_menu
  }

  override fun setSelected(selected: Boolean) {
    if (isSelected != selected) {
      isSelected = selected
      notifyChanged()
    }
  }

  override fun getAppMenu(): AppMenu {
    return childMenu
  }

  override fun bind(
    viewBinding: ItemChildMenuBinding,
    position: Int
  ) {
    val txtRoot = viewBinding.txtChildMenu
    val context = viewBinding.root.context
    viewBinding.root.setBackgroundResource(R.color.white)
    if (childMenu.label > 0) {
      txtRoot.setText(childMenu.label)
    } else {
      txtRoot.text = childMenu.labelStr
    }

    if (childMenu.enabled) {
      txtRoot.setTextColor(context.resources.getColor(R.color.black))
      txtRoot.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    } else {
      txtRoot.setTextColor(context.resources.getColor(R.color.text_color_disabled))
      txtRoot.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ico_lock, 0)
    }
  }

  override fun getId(): Long {
    return childMenu.label.toLong()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ChildMenuItem

    if (childMenu != other.childMenu) return false
    if (isSelected != other.isSelected) return false

    return true
  }

  override fun hashCode(): Int {
    var result = childMenu.hashCode()
    result = 31 * result + isSelected.hashCode()
    return result
  }
}