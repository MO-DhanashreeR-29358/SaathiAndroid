package com.mosl.tabapp.helper.shortcuts

import android.content.Context
import android.os.Build

interface ShortcutHelper {
  fun setLoggedInUserShortcuts()
  fun setLogoutUserShortcuts()
}

object NoOpShortcutHelper : ShortcutHelper {
  override fun setLoggedInUserShortcuts() {}

  override fun setLogoutUserShortcuts() {}
}

fun getShortcutHelper(
  context: Context
): ShortcutHelper {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
    ShortcutHelperImpl(context)
  } else {
    NoOpShortcutHelper
  }
}

