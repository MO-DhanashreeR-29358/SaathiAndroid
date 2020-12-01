package com.mosl.tabapp.common.searchableSpinnerNew

import java.io.Serializable

interface OnSearchableItemClick<T> : Serializable {
  fun onSearchableItemClicked(
    item: T?,
    position: Int
  )
}