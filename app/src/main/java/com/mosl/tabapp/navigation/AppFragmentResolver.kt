package com.mosl.tabapp.navigation

import androidx.fragment.app.Fragment
import com.mosl.navigator.FragmentResolver

object AppFragmentResolver : FragmentResolver {
  override fun resolve(pageId: Int): Fragment? {
    return when {
      MenuGetterNew.isHandled(pageId) -> MenuGetterNew.getFragment(pageId)
      else -> null
    }
  }
}