package com.mosl.tabapp.navigation

import androidx.fragment.app.Fragment
import com.mosl.navigator.FragmentResolver

object AppFragmentResolver : FragmentResolver {
  override fun resolve(pageId: String): Fragment? {
    return when {
      MenuGetterNew.isHandled(pageId.toInt()) -> MenuGetterNew.getFragment(pageId.toInt())
      else -> null
    }
  }
}