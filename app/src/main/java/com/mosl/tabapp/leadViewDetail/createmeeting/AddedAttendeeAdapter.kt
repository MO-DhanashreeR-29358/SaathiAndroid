package com.mosl.tabapp.leadViewDetail.createmeeting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mosl.mobile.domain.createmeeting.AddedAttendee
import com.mosl.tabapp.R

class AddedAttendeeAdapter(
  context: Context,
  val list: ArrayList<AddedAttendee>,
  private val clickListenerHandler: ClickListenerHandler
) : BaseAdapter() {
  private val inflater: LayoutInflater = LayoutInflater.from(context)
  override fun getView(
    position: Int,
    oldView: View?,
    parent: ViewGroup?
  ): View {
    val convertView = oldView
      ?: inflater.inflate(R.layout.item_list_added_attendee, parent, false)
        .apply {
          tag = HolderView(this)
        }
    val holderView: HolderView = convertView.tag as HolderView
    val attendee = list[position]
    holderView.attendeeName.text =
      String.format("%s-%s", attendee.attendeeCode, attendee.attendeeName)
    holderView.delete.setOnClickListener {
      clickListenerHandler.onDeleteClicked(position, attendee.isAttendee())
    }

    return convertView
  }

  override fun getItem(position: Int): AddedAttendee {
    return list[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return list.size
  }

  internal class HolderView(parent: View) {
    val attendeeName: TextView = parent.findViewById(R.id.tvAttendeeName)
    val delete: ImageView = parent.findViewById(R.id.ivDelete)
  }

  interface ClickListenerHandler {
    fun onDeleteClicked(
      position: Int,
      isAttendee: Boolean
    )
  }
}