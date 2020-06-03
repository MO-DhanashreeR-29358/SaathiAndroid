package com.mosl.tabapp.leadViewDetail.createmeeting

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.tabapp.R

class AttendeeAdapter(
  context: Context
) : BaseAdapter(), Filterable {

  private var resultList = ArrayList<Attendee>()
  private val inflater: LayoutInflater = LayoutInflater.from(context)
  override fun getView(
    position: Int,
    oldView: View?,
    parent: ViewGroup?
  ): View {
    val convertView = oldView
      ?: inflater.inflate(R.layout.item_list_attendee, parent, false)
        .apply {
          tag = HolderView(this)
        }
    val holderView: HolderView = convertView.tag as HolderView
    val attendee = resultList[position]
    viewColor(position, convertView)
    if (attendee.hasData) {
      holderView.attendeeName.text =
        String.format("%s-%s-%s", attendee.empCode, attendee.empName, attendee.branchCode)
    } else {
      holderView.attendeeName.text = attendee.empName
      holderView.layout.setOnClickListener(null)
    }
    return convertView
  }

  private fun viewColor(
    position: Int,
    convertView: View
  ) {
    if (position % 2 == 1) {
      convertView.setBackgroundColor(Color.parseColor("#F5F5F5"))
    } else {
      convertView.setBackgroundColor(Color.parseColor("#FFFFFF"))
    }
  }

  override fun getItem(position: Int): Attendee {
    return resultList[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return resultList.size
  }

  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults? {
        return null
      }

      override fun publishResults(
        constraint: CharSequence?,
        results: FilterResults?
      ) {

      }
    }

  }

  fun setErrorData(throwable: Throwable) {
    if (throwable is ServiceFailureException) {
      resultList.addAll(
        listOf(
          Attendee(
            empName = throwable.errorMessage, empCode = " ",
            branchCode = " ", hasData = false
          )
        )
      )
      notifyDataSetChanged()
    }
  }

  fun setResultList(results: List<Attendee>) {
    if (results.isEmpty()) {
      notifyDataSetInvalidated()
    } else {
      resultList.clear()
      resultList.addAll(results)
      notifyDataSetChanged()
    }
  }

  internal class HolderView(parent: View) {
    val attendeeName: TextView = parent.findViewById(R.id.tvAttendeeName)
    val layout: LinearLayout = parent.findViewById(R.id.linaerLayout)
  }
}