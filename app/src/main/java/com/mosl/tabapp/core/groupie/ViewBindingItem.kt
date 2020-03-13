package com.mosl.tabapp.core.groupie

import android.view.View
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.Item

abstract class ViewBindingItem<T : ViewBinding> : Item<VBViewHolder<T>> {
  constructor() : super()
  protected constructor(id: Long) : super(id)

  abstract fun initBinding(itemView: View): T
  override fun createViewHolder(itemView: View): VBViewHolder<T> {
    val viewBinding = initBinding(itemView)
    return VBViewHolder(viewBinding)
  }

  override fun bind(viewHolder: VBViewHolder<T>, position: Int) {
    throw RuntimeException("Doesn't get called")
  }

  override fun bind(
    viewHolder: VBViewHolder<T>,
    position: Int,
    payloads: List<Any>
  ) {
    bind(viewHolder.binding, position, payloads)
  }

  /**
   * Perform any actions required to set up the view for display.
   *
   * @param viewBinding The ViewDataBinding to bind
   * @param position    The adapter position
   */
  abstract fun bind(viewBinding: T, position: Int)

  /**
   * Perform any actions required to set up the view for display.
   *
   *
   * If you don't specify how to handle payloads in your implementation, they'll be ignored and
   * the adapter will do a full rebind.
   *
   * @param viewBinding The ViewDataBinding to bind
   * @param position    The adapter position
   * @param payloads    A list of payloads (may be empty)
   */
  fun bind(
    viewBinding: T,
    position: Int,
    payloads: List<Any>
  ) {
    bind(viewBinding, position)
  }
}