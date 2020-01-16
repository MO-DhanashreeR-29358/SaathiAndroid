package com.mosl.tabapp.drawermenu.data;

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mosl.tabapp.navigation.NavigationConstants

/**
 * Created by apple on 17/03/18.
 */

sealed class AppMenu(
  @NavigationConstants val pageId: Int,
  val enabled: Boolean = true,
  val secure: Boolean = false,
  val navigateKey: String,
  val labelStr: String?

)

class BaseMenu constructor(
  @DrawableRes val icon: Int,
  @StringRes val label: Int,
  pageId: Int = NavigationConstants.NAVIGATE_TO_NONE,
  enabled: Boolean = true,
  val children: List<ChildMenu> = emptyList()
) : AppMenu(pageId, enabled, false, "", "")

class ChildMenu constructor(
  @StringRes val label: Int,
  labelStr: String? = null,
  pageId: Int = NavigationConstants.NAVIGATE_TO_NONE,
  enabled: Boolean = true,
  secure: Boolean = false,
  navigateKey: String
) : AppMenu(pageId, enabled, secure, navigateKey, labelStr)

class OtherMenu(pageId: Int) : AppMenu(
    pageId, enabled = true, secure = false, navigateKey = "", labelStr = ""
)

