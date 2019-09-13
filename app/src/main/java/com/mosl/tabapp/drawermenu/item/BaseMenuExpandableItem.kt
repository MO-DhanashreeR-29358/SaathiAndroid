package com.mosl.tabapp.drawermenu.item

import android.graphics.Typeface
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemBaseMenuBinding
import com.mosl.tabapp.drawermenu.data.BaseMenu
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.databinding.BindableItem

class BaseMenuExpandableItem(
  @JvmField val baseMenu: BaseMenu,
  @JvmField val itemExpansionListener: OnItemClickListener
) : BindableItem<ItemBaseMenuBinding>(), ExpandableItem {
  internal lateinit var expandableGroup: ExpandableGroup
  private var binding: ItemBaseMenuBinding? = null

  override fun bind(
    viewBinding: ItemBaseMenuBinding,
    position: Int
  ) {
    binding = viewBinding
    viewBinding.root.apply {
      //      setBackgroundResource(R.drawable.drawer_item_shape_bg)
      setOnClickListener { v ->
        expandableGroup.onToggleExpanded()
        setIcon()
        itemExpansionListener.onItemClick(this@BaseMenuExpandableItem, v)
      }
    }
    viewBinding.imgGroupImage.setImageResource(baseMenu.icon)
    viewBinding.txtBaseMenu.setText(baseMenu.label)
    viewBinding.txtBaseMenu.setTypeface(null, Typeface.NORMAL)
    setIcon()
  }

  override fun getLayout(): Int {
    return R.layout.item_base_menu
  }

  override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
    this.expandableGroup = onToggleListener
  }

  private val isExpanded: Boolean
    get() = expandableGroup.isExpanded

  fun collapse() {
    if (isExpanded) {
      expandableGroup.onToggleExpanded()
      setIcon()
    }
  }

  fun expand() {
    if (!isExpanded) {
      expandableGroup.onToggleExpanded()
      setIcon()
    }
  }

  internal fun setIcon() {
    val correctIcon: Int = if (isExpanded)
      R.drawable.ic_collapse
    else
      R.drawable.ic_expand
    binding?.txtBaseMenu?.setCompoundDrawablesWithIntrinsicBounds(
        0, 0, correctIcon,
        0
    )
  }
}
