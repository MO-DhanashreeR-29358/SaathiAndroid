package com.mosl.tabapp.bankmeeting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mosl.tabapp.R
import com.mosl.tabapp.bankmeeting.BankMeetingFragment
import com.mosl.tabapp.bankmeeting.model.AddedBankEmployeeModel
import java.util.ArrayList

class AddedBankEmployeeAdapter(
  private val context: Context,
  private var resultList: ArrayList<AddedBankEmployeeModel>,
  private val fragment: BankMeetingFragment
) : BaseAdapter() {

  private var inflater = LayoutInflater.from(context)

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    var convertedView = convertView
    val holderView: HolderView
    val attendeeModel = resultList[position]
    if (convertedView == null) {
      inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      convertedView = inflater.inflate(R.layout.item_list_added_attendee, parent, false)
      holderView = HolderView(convertedView!!)
      convertedView.tag = holderView
    } else {
      holderView = convertedView.tag as HolderView
    }



    holderView.tvAttendeeName.text = attendeeModel.employeeName
    holderView.ivDelete.setOnClickListener {
      val count = attendeeModel.employeeCount - 1
      fragment.setCount(count)
      resultList.removeAt(position)
      notifyDataSetChanged()
    }

    return convertedView
  }

  override fun getCount(): Int {
    return resultList.size
  }

  override fun getItem(position: Int): Any {
    return resultList[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  internal inner class HolderView(parent: View) {
    var tvAttendeeName: TextView = parent.findViewById(R.id.tvAttendeeName)
    var ivDelete: ImageView = parent.findViewById(R.id.ivDelete)
  }
}