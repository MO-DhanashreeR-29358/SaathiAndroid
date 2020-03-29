package com.mosl.tabapp.dashboard.leads.report

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.dimen
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.ItemTableBinding
import com.mosl.tabapp.databinding.ItemTableFamilyBinding
import com.mosl.tabapp.databinding.ItemTableFirstLeadBinding
import com.mosl.tabapp.databinding.ItemTableHeaderFirstLeadBinding
import com.mosl.tabapp.databinding.ItemTableHeaderLeadBinding
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap

class LeadTableAdapter internal constructor(
  requireActivity: FragmentActivity, map: Map<String, String?>,
  navigator: ScreenNavigatorImpl,
  widgetSystemsName: String
) : BaseTableAdapter() {
  private val commonHeaders: Array<CommonHeader>
  private var headers = arrayOf<String?>()
  private var headers2 = arrayOf<String?>()
  private val hashArray: ArrayList<HashMap<String, String?>>
  private val requireActivity: FragmentActivity
  private val widgetSystemsName: String
  private val navigator: ScreenNavigatorImpl
  private val map: Map<String, String?>
  var tableHeaderMap: Map<Int, List<Map<Int, String>>>

  init {
    commonHeaders =
      arrayOf(CommonHeader("asd"))
    hashArray = arrayListOf()
    this.requireActivity = requireActivity
    this.map = map
    this.navigator = navigator
    this.widgetSystemsName = widgetSystemsName
    this.tableHeaderMap = mutableMapOf()
  }

  fun setHeaders(headers: Array<String?>) {
    this.headers = headers
  }

  fun setTableMap(tableHeaderMap: Map<Int, List<Map<Int, String>>>) {
    this.tableHeaderMap = tableHeaderMap
  }

  fun setHeadersSecond(headers: Array<String?>) {
    headers2 = headers
  }

  override fun getRowCount(): Int {
    return commonHeaders[0].list.size
  }

  fun clear() {}
  fun renderData(row: HashMap<String, String?>) {
    hashArray.add(row)
    val rowData = arrayOfNulls<String>(headers.size)
    var ctr = 0
    for (columnName in headers) {
      val value = row[columnName]
      rowData[ctr] = value
      ctr++
    }
    commonHeaders[0].list.add(Item(rowData))
  }

  private fun getRowKeyName(childId: Double): String? {
    if (tableHeaderMap.size > 0)
      for ((_, value) in tableHeaderMap) {
        for (childMap: Map<Int, String> in value) {
          val childListSet: Set<Int> = childMap.keys
          for (childIdFromMap in childListSet) {
            if (childId == childIdFromMap.toDouble()) {
              return childMap[childIdFromMap]
            }
          }
        }
      }

    return null
  }

  private fun isGroup(id: Double): Boolean {
    if (tableHeaderMap.size > 0)
      for ((groupId) in tableHeaderMap) {
        if (groupId.toDouble() == id) return true
      }
    return false
  }

  override fun getColumnCount(): Int {
    return headers.size - 1
  }

  override fun getView(
    row: Int,
    column: Int,
    convertView: View?,
    parent: ViewGroup
  ): View {
    val view: View
    when (getItemViewType(row, column)) {
      0 -> view = getFirstHeader(requireActivity)
      1 -> view = getHeader(column, requireActivity)
      2 -> view = getFirstBody(row, column, requireActivity)
      3 -> {
        view = getBody(row, column, requireActivity)
        view.setOnClickListener { view1: View? ->
          val newColumn = column + 1
          val stringSet: Set<String> = hashArray[row].keys
          val columnName = stringSet.toTypedArray()[newColumn]
          val addtionalParameterMap: MutableMap<String, String?> =
            HashMap()
          addtionalParameterMap[MOSLConstants.COL_FILTER] = columnName
          addtionalParameterMap[MOSLConstants.ROW_FILTER] =
            getRowKeyName(hashArray[row][ID]!!.toDouble())
          addtionalParameterMap.putAll(map)
          val args = Bundle()
          args.putString(MOSLConstants.SYSTEM_NAME, widgetSystemsName)
          args.putString(MOSLConstants.LABEL, MOSLConstants.LEAD)
          args.putSerializable(MOSLConstants.MAP, addtionalParameterMap as Serializable)
          val lMap =
            hashArray[row] as LinkedHashMap<*, *>
          val columnData = lMap[columnName].toString()
          if (!columnData.trim { it <= ' ' }.equals("0", ignoreCase = true)) {
            navigator.openPage(
              NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD,
              args,
              true, false
            )
          }
        }
      }
      4 -> view = getFamilyView(row, column, requireActivity)
      else -> throw RuntimeException("runtime exception")
    }
    return view
  }

  //click on symbol header
  private fun getFirstHeader(context: FragmentActivity): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableHeaderFirstLeadBinding.inflate(inflater)
    binding.text1.text = headers[1]
    binding.text1.setTypeface(null, Typeface.BOLD)
    return binding.root
  }

  //click on header other than symbol
  private fun getHeader(column: Int, context: FragmentActivity): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableHeaderLeadBinding.inflate(inflater)
    binding.text1.text = headers[column + 1]
    binding.text2.text = headers2[column + 1]
    return binding.root
  }

  private fun getFirstBody(row: Int, column: Int, context: FragmentActivity): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableFirstLeadBinding.inflate(inflater)
    if (hashArray[row][ID] != null && isGroup(
        hashArray[row][ID]!!.toDouble()
      )
    ) {
      binding.tvTableFirst.setTypeface(null, Typeface.BOLD)
      binding.tvTableFirst.setTextColor(
        ContextCompat.getColor(
          context,
          color.flush_orange
        )
      )
      binding.itemTableFirstLead.setBackgroundResource(color.lead_self_back)
    } else if (hashArray[row][STAGE] != null && hashArray[row][STAGE].equals(
        TOTAL,
        ignoreCase = true
      )
    ) {
      binding.tvTableFirst.setTypeface(null, Typeface.BOLD)
      binding.tvTableFirst.setTextColor(
        ContextCompat.getColor(
          context,
          color.black
        )
      )
      binding.itemTableFirstLead.setBackgroundResource(color.secondaryBackground)
    } else {
      binding.tvTableFirst.setTypeface(null, Typeface.BOLD)
      binding.tvTableFirst.setTextColor(
        ContextCompat.getColor(
          context,
          color.black
        )
      )
      binding.itemTableFirstLead.setBackgroundResource(color.lead_ekyc_back)
    }
    binding.tvTableFirst.text = getDevice(row).data[column + 2]
    return binding.root
  }

  private fun getBody(row: Int, column: Int, context: FragmentActivity): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableBinding.inflate(inflater)
    if (hashArray[row][ID] != null && isGroup(
        hashArray[row][ID]!!.toDouble()
      )
    ) {
      binding.text1.setTextColor(
        ContextCompat.getColor(
          context,
          color.flush_orange
        )
      )
      binding.itemTable.setBackgroundResource(color.lead_self_back)
    } else if (hashArray[row][STAGE] != null && hashArray[row][STAGE]
        .equals(TOTAL, ignoreCase = true)
    ) {
      binding.text1.setTypeface(null, Typeface.BOLD)
      binding.text1.setTextColor(ContextCompat.getColor(requireActivity, color.black))
      binding.itemTable.setBackgroundResource(color.lead_total_back)
    } else {
      binding.text1.setTextColor(ContextCompat.getColor(requireActivity, color.black))
      binding.itemTable.setBackgroundResource(color.lead_ekyc_back)
    }
    binding.text1.text = getDevice(row).data[column + 1]
    return binding.root
  }

  private fun getFamilyView(row: Int, column: Int, context: FragmentActivity): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableFamilyBinding.inflate(inflater)
    val string: String
    string = if (column == -1) {
      getFamily(row).name
    } else {
      ""
    }
    binding.text1.text = string
    return binding.root
  }

  override fun getWidth(column: Int): Int {
    var widthSize = 0
    if (column == -1) {
      widthSize = requireActivity.resources.getDimensionPixelSize(dimen._90sdp)
    }
    for (i in 0 until column) {
      widthSize = requireActivity.resources.getDimensionPixelSize(dimen._135sdp)
    }
    return widthSize
  }

  override fun getHeight(row: Int): Int {
    return if (row == -1) {
      requireActivity.resources.getDimensionPixelSize(dimen._40sdp)
    } else {
      requireActivity.resources.getDimensionPixelSize(dimen._40sdp)
    }
  }

  override fun getItemViewType(row: Int, column: Int): Int {
    return if (row == -1 && column == -1) {
      0
    } else if (row == -1) {
      1
    } else if (column == -1) {
      2
    } else {
      3
    }
  }

  private fun getFamily(row: Int): CommonHeader {
    var rowCount = row
    val family = 0
    while (rowCount >= 0) {
      rowCount -= commonHeaders[family].size()
    }
    return commonHeaders[family]
  }

  private fun getDevice(row: Int): Item {
    var rowCount = row
    val family = 0
    while (rowCount >= 0) {
      rowCount -= commonHeaders[family].size()
    }
    return commonHeaders[family][rowCount + commonHeaders[family].size()]
  }

  override fun getViewTypeCount(): Int {
    return 5
  }

  private inner class CommonHeader internal constructor(val name: String) {
    val list: MutableList<Item>
    fun size(): Int {
      return list.size
    }

    operator fun get(i: Int): Item {
      return list[i]
    }

    init {
      list = ArrayList()
    }
  }
  inner class Item constructor(val data: Array<String?>)
  companion object {
    const val ID = "id"
    const val STAGE = "stage"
    const val TOTAL = "total"
  }

}