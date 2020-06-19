package com.mosl.tabapp.leadViewDetail.leaddetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.mosl.tabapp.R
import com.mosl.tabapp.leadViewDetail.createmeeting.AddedAttendeeAdapter.HolderView
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel

class LeadDetailAddProductAdapter(
  val context: Context,
  val list: ArrayList<LeadDetailAddProductModel>
) : BaseAdapter() {
  private val inflater = LayoutInflater.from(context)

  override fun getView(
    position: Int,
    oldView: View?,
    parent: ViewGroup?
  ): View {
    val convertView = oldView
        ?: inflater.inflate(R.layout.item_list_add_product, parent, false)
            .apply {
              tag = HolderView(this)
            }
    val holderView: HolderView = convertView.tag as HolderView
    // val data = list[position]
    return convertView
  }

  override fun getItem(position: Int): Any {
    return list.get(position)
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return list.size
  }
}