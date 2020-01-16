package com.mosl.tabapp.drawermenu

import com.mosl.tabapp.drawermenu.item.BaseMenuExpandableItem
import com.mosl.tabapp.drawermenu.item.ChildMenuItem
import com.xwray.groupie.ExpandableGroup

class MenuExpandableGroup(
  @JvmField val header: BaseMenuExpandableItem,
  @JvmField val children: List<ChildMenuItem>
) : ExpandableGroup(header) {
  init {
    addAll(children)
  }
}