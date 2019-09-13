package com.mosl.tabapp.drawermenu.item

import com.mosl.tabapp.drawermenu.data.AppMenu

/**
 * Created by lalkishore on 11/02/18.
 */
interface SelectableMenu {
  fun setSelected(selected: Boolean)
  fun getAppMenu(): AppMenu
}