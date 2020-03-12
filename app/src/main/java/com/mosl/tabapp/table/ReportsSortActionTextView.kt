package com.mosl.tabapp.table

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.mosl.tabapp.R

class ReportsSortActionTextView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

  private var mIsSortedAsc = false

  fun setSorted(
    sorted: Boolean,
    asc: Boolean
  ) {
    val icon = if (sorted) {
      if (asc) {
        R.drawable.down_button
      } else {
        R.drawable.up_button
      }
    } else {
      R.drawable.down_button
    }
    setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
  }

  fun setSortedAsc(mIsSortedAsc: Boolean) {
    this.mIsSortedAsc = mIsSortedAsc
    refreshDrawableState()
  }

  override fun onCreateDrawableState(extraSpace: Int): IntArray {
    val drawableState = super.onCreateDrawableState(extraSpace + 2)
    if (mIsSortedAsc) {
      View.mergeDrawableStates(drawableState, STATE_SORTED_ASC)
    }
    return drawableState
  }

  companion object {
    private val STATE_SORTED_ASC = intArrayOf(R.attr.state_sorted_ascending)
  }
}