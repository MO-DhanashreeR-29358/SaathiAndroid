package com.mosl.tabapp.pendingApproval.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.dimen
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.ItemTableBinding
import com.mosl.tabapp.databinding.ItemTableFamilyBinding
import com.mosl.tabapp.databinding.ItemTableFirstPendingBinding
import com.mosl.tabapp.databinding.ItemTableHeaderBinding
import com.mosl.tabapp.databinding.ItemTableHeaderFirstPendingBinding
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.LinkedHashMap

class PendingApprovalTableAdapter(
  val requireActivity: FragmentActivity,
  val publicSubject: PublishSubject<PendingForApprovalUIEvent>
) : BaseTableAdapter() {
  private val commonHeaders: Array<CommonHeader>
  private val headers = arrayOf(
    MOSLConstants.EMPLOYEE_CODE_APP,
    MOSLConstants.EMP_NAME,
    MOSLConstants.LEAD_ID_APP,
    MOSLConstants.CLIENT_CODE,
    MOSLConstants.UCID,
    MOSLConstants.NEW_CLIENT_NAME,
    MOSLConstants.MODULE_NAME,
    MOSLConstants.FIELD_NAME,
    MOSLConstants.SUB_OPTION,
    MOSLConstants.STATUS,
    MOSLConstants.REQUEST_DATE,
    MOSLConstants.APPROVED_DATE,
    MOSLConstants.APPROVAL_REQUEST_ID
  )
  var hashArray = ArrayList<HashMap<String, String>>()
  private var clickedOn = ""
  private var click = 1
  private var isAllChecked = false
  private var isSingleAllChecked = false
  private val selectedRowList =
    ArrayList<String>()
  private val selectedRowMap: MutableMap<Int, Boolean> =
    HashMap()

  override fun getRowCount(): Int {
    return commonHeaders[0].list.size
  }

  fun add(row: HashMap<String, String>) {
    hashArray.add(row)
    commonHeaders[0].list.add(
      Item(
        row[MOSLConstants.EMPLOYEE_CODE_APP]!!,
        row[MOSLConstants.EMP_NAME]!!,
        row[MOSLConstants.LEAD_ID_APP]!!,
        row[MOSLConstants.CLIENT_CODE]!!,
        row[MOSLConstants.UCID]!!,
        row[MOSLConstants.NEW_CLIENT_NAME]!!,
        row[MOSLConstants.MODULE_NAME]!!,
        row[MOSLConstants.FIELD_NAME]!!,
        row[MOSLConstants.SUB_OPTION]!!,
        row[MOSLConstants.STATUS]!!,
        row[MOSLConstants.REQUEST_DATE]!!,
        row[MOSLConstants.APPROVED_DATE]!!,
        row[MOSLConstants.APPROVAL_REQUEST_ID]!!
      )
    )
  }

  override fun getColumnCount(): Int {
    return headers.size
  }

  override fun getView(
    row: Int,
    column: Int,
    convertView: View?,
    parent: ViewGroup
  ): View {
    val view: View
    view = when (getItemViewType(row, column)) {
      0 -> getFirstHeader(column, requireActivity)
      1 -> getHeader(column, requireActivity)
      2 -> getFirstBody(row, column, requireActivity)
      3 -> getBody(row, column, requireActivity)
      4 -> getFamilyView(row, column, requireActivity)
      else -> throw RuntimeException("runtime exception")
    }
    return view
  }

  //click on symbol header
  private fun getFirstHeader(
    column: Int,
    context: FragmentActivity
  ): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableHeaderFirstPendingBinding.inflate(inflater)
    if (clickedOn == headers[column + 1] && click > 1) {
      binding.text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.up_button, 0)
    } else {
      binding.text1.setCompoundDrawablesWithIntrinsicBounds(
        0, 0, drawable.down_button, 0
      )
    }
    binding.text1.text = headers[0]
    if (isAllChecked) {
      binding.cbClientName.setChecked(true)
    } else {
      binding.cbClientName.setChecked(false)
    }
    binding.cbClientName.setOnClickListener { view: View ->
      isAllChecked = (view as CheckBox).isChecked
      for (ctr in 0 until rowCount) {
        val approvalRequestId = getDevice(ctr - 1).data[12]
        selectedRowMap[ctr] = view.isChecked
        if (binding.cbClientName.isChecked()) {
          selectedRowList.add(approvalRequestId)
          isSingleAllChecked = false
        } else {
          selectedRowList.remove(approvalRequestId)
        }
      }
      publicSubject.onNext(PendingForApprovalUIEvent.CheckBoxSelectedArrayList(selectedRowList))
      notifyDataSetChanged()
    }
    if (selectedRowList.isEmpty() || isSingleAllChecked) {
      binding.cbClientName.setChecked(false)
    }
    if (selectedRowList.size == 0) {
      binding.text1.setOnClickListener { v: View ->
        val temp = v as TextView
        clickedOn = temp.text.toString()
        if (click == 1) {
          sortByAMCName(click)
          click++
        } else if (click > 1) {
          sortByAMCName(click)
          click = 1
        }
      }
    }
    return binding.root
  }

  private fun sortByAMCName(sortVal: Int) {
    val sortCol = MOSLConstants.EMPLOYEE_CODE_APP
    val out = ArrayList<String?>()
    for (j in hashArray.indices) {
      val linkedHashMap =
        hashArray[j] as LinkedHashMap<*, *>
      val `val` = linkedHashMap[sortCol] as String?
      out.add(`val`)
    }
    if (sortVal == 1) {
      Collections.sort(
        out
      ) { obj: String?, anotherString: String? ->
        obj!!.compareTo(anotherString!!)
      }
    } else {
      Collections.reverse(out)
    }
    commonHeaders[0].list.clear()
    val tempHashArray =
      hashArray.clone() as ArrayList<HashMap<String, String>>
    hashArray.clear()
    for (i in out.indices) {
      val sortedValue = out[i]
      for (j in tempHashArray.indices) {
        val sortLinkedMap =
          tempHashArray[j] as LinkedHashMap<String, String>
        val sortColVal = sortLinkedMap[sortCol]
        if (sortColVal == sortedValue) {
          add(sortLinkedMap)
          tempHashArray.remove(sortLinkedMap)
        }
      }
    }
    notifyDataSetChanged()
  }

  //click on header other than symbol
  private fun getHeader(
    column: Int,
    context: FragmentActivity
  ): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableHeaderBinding.inflate(inflater)
    if (clickedOn == headers[column] && click > 1) {
      binding.text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.up_button, 0)
    } else {
      binding.text1.setCompoundDrawablesWithIntrinsicBounds(
        0, 0,
        drawable.down_button, 0
      )
    }
    binding.text1.text = headers[column]
    if (selectedRowList.size == 0) {
      binding.text1.setOnClickListener { v: View ->
        val sortCol = (v as TextView).text.toString()
        val isNumber = sortCol == MOSLConstants.REJECTED
        val outStr = ArrayList<String?>()
        val outDbl = ArrayList<Double?>()
        for (j in hashArray.indices) {
          val linkedHashMap =
            hashArray[j] as LinkedHashMap<*, *>
          val sortedData = linkedHashMap[sortCol] as String?
          if (isNumber) {
            outDbl.add(java.lang.Double.valueOf(sortedData))
          } else {
            outStr.add(sortedData)
          }
        }
        if (isNumber) {
          clickedOn = v.text.toString()
          if (click == 1) {
            Collections.sort(
              outDbl
            ) { obj: Double?, anotherDouble: Double? ->
              obj!!.compareTo(anotherDouble!!)
            }
            click++
          } else if (click > 1) {
            Collections.reverse(outDbl)
            click = 1
          }
          commonHeaders[0].list.clear()
          val tempHashArray =
            hashArray.clone() as ArrayList<HashMap<String, String>>
          hashArray.clear()
          for (i in outDbl.indices) {
            val sortedValue = outDbl[i]
            for (j in tempHashArray.indices) {
              val sortLinkedMap =
                tempHashArray[j] as LinkedHashMap<String, String>
              val sortColVal = sortLinkedMap[sortCol] as String?
              val sortColValue =
                sortColVal!!.toDouble()
              val retval = java.lang.Double.compare(sortedValue!!, sortColValue)
              if (retval == 0) {
                add(sortLinkedMap)
                tempHashArray.remove(sortLinkedMap)
              }
            }
          }
        } else {
          clickedOn = v.text.toString()
          if (click == 1) {
            Collections.sort(
              outStr
            ) { obj: String?, anotherString: String? ->
              obj!!.compareTo(anotherString!!)
            }
            click++
          } else if (click > 1) {
            Collections.reverse(outStr)
            click = 1
          }
          commonHeaders[0].list.clear()
          val tempHashArray =
            hashArray.clone() as ArrayList<HashMap<String, String>>
          hashArray.clear()
          for (i in outStr.indices) {
            val sortedValue = outStr[i]
            for (j in tempHashArray.indices) {
              val sortLinkedMap =
                tempHashArray[j] as LinkedHashMap<String, String>
              val sortColVal = sortLinkedMap[sortCol] as String?
              if (sortedValue == sortColVal) {
                add(sortLinkedMap)
                tempHashArray.remove(sortLinkedMap)
              }
            }
          }
        }
        notifyDataSetChanged()
      }
    }
    return binding.root
  }

  private fun getFirstBody(
    row: Int,
    column: Int,
    context: FragmentActivity
  ): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableFirstPendingBinding.inflate(inflater)
    binding.itemTableFirst.setBackgroundResource(
      if (row % 2 == 0) color.alternateBackground else color.white
    )
    binding.tvTableFirst.text =
      getDevice(row).data[column + 1] //row= 1 to ... & col=-1
    var isChecked = selectedRowMap[row]
    if (isChecked == null) {
      isChecked = false
    }
    val approvalRequestId = getDevice(row).data[column + 13]
    if (isChecked) {
      binding.cbClientNameBody.isChecked = true
    } else {
      selectedRowList.remove(approvalRequestId)
      binding.cbClientNameBody.isChecked = false
    }
    binding.cbClientNameBody.setOnClickListener { v: View ->
      val isChecked1 = (v as CheckBox).isChecked
      selectedRowMap[row] = isChecked1
      if (isChecked1) {
        selectedRowList.add(approvalRequestId)
      } else {
        selectedRowList.remove(approvalRequestId)
        isSingleAllChecked = true
      }
      publicSubject.onNext(PendingForApprovalUIEvent.CheckBoxSelectedArrayList(selectedRowList))
      notifyDataSetChanged()
    }
    return binding.root
  }

  private fun getBody(
    row: Int,
    column: Int,
    context: FragmentActivity
  ): View {
    val inflater = LayoutInflater.from(context)
    val binding = ItemTableBinding.inflate(inflater)
    binding.itemTable.setBackgroundResource(
      if (row % 2 == 0) color.alternateBackground else color.white
    )
    binding.text1.text =
      getDevice(row).data[column]
    binding.text1.setBackgroundResource(0)
    return binding.root
  }

  private fun getFamilyView(
    row: Int,
    column: Int,
    context: FragmentActivity
  ): View {

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
      widthSize = requireActivity.resources.getDimensionPixelSize(dimen._100sdp)
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
    val itemViewType: Int
    itemViewType = if (row == -1 && column == -1) {
      0
    } else if (row == -1) {
      1
    } else if (column == -1) {
      2
    } else {
      3
    }
    return itemViewType
  }

  private fun getFamily(row: Int): CommonHeader {
    var familyRow = row
    val family = 0
    while (row >= 0) {
      familyRow -= commonHeaders[family].size()
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

  init {
    hashArray = ArrayList()
    commonHeaders =
      arrayOf(
        CommonHeader("asd")
      )
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
      list =
        ArrayList()
    }
  }

  private inner class Item(
    employeeCode: String,
    empName: String,
    leadID: String,
    clientCode: String,
    ucid: String,
    clientName: String,
    moduleName: String,
    fieldName: String,
    subOption: String,
    status: String,
    requestDate: String,
    approvedDate: String,
    appovalRequestId: String
  ) {
    val data: Array<String>

    init {
      data = arrayOf(
        employeeCode,
        empName,
        leadID,
        clientCode,
        ucid,
        clientName,
        moduleName,
        fieldName,
        subOption,
        status,
        requestDate,
        approvedDate,
        appovalRequestId
      )
    }
  }
}