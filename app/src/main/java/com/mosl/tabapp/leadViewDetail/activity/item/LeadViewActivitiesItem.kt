package com.mosl.tabapp.leadViewDetail.activity.item

import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemLeadViewActivitiesBinding
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.databinding.BindableItem

class LeadViewActivitiesItem(
  private val title: String
) : BindableItem<ItemLeadViewActivitiesBinding>(), ExpandableItem {

  private var expandableGroup: ExpandableGroup? = null

  override fun getLayout(): Int {
    return R.layout.item_lead_view_activities
  }

  override fun bind(
    viewBinding: ItemLeadViewActivitiesBinding,
    position: Int
  ) {
    viewBinding.tvTitle.text = title
    viewBinding.root.setOnClickListener {
      expandableGroup?.let {
        it.onToggleExpanded()
        if (it.isExpanded) {
          viewBinding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0)
        } else {
          viewBinding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0)
        }
      }
    }
  }

  override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
    expandableGroup = onToggleListener
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LeadViewActivitiesItem

    if (title != other.title) return false

    return true
  }

  override fun hashCode(): Int {
    return title.hashCode()
  }

}