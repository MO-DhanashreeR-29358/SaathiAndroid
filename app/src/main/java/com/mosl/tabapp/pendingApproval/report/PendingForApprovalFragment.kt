package com.mosl.tabapp.pendingApproval.report

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.pendingapproval.EmployeeModel
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalDropdownData
import com.mosl.mobile.domain.pendingapproval.PendingForApprovalItem
import com.mosl.saathi.api.model.pendingforapproval.MultiUpdateApprovalRequest
import com.mosl.shared.util.AppDateFormats
import com.mosl.shared.util.AppDateFormats.DF_JSON_DATE10
import com.mosl.shared.util.AppDateFormats.DF_JSON_DATE4
import com.mosl.shared.util.getFormatedDate
import com.mosl.tabapp.R
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.dimen
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragPendingForApprovalBinding
import com.mosl.tabapp.databinding.ItemTableBinding
import com.mosl.tabapp.databinding.ItemTableFamilyBinding
import com.mosl.tabapp.databinding.ItemTableFirstPendingBinding
import com.mosl.tabapp.databinding.ItemTableHeaderBinding
import com.mosl.tabapp.databinding.ItemTableHeaderFirstPendingBinding
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory.getDefaultAdapter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import org.json.JSONException
import timber.log.Timber
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.HashMap
import java.util.LinkedHashMap
import javax.inject.Inject
import org.json.JSONObject as JSONObject1

class PendingForApprovalFragment : CommonFragment(),
  OnClickListener, MOSLConstants, PendingForApprovalView {
  lateinit var binding: FragPendingForApprovalBinding
  lateinit var pendingApprovalTableAdapter: PendingApprovalTableAdapter
  private var clickedOn = ""
  private var click = 1
  private var isAllChecked = false
  private var isSingleAllChecked = false
  lateinit var cbClientName: CheckBox
  private val selectedRowList =
    ArrayList<String>()
  private val selectedRowMap: MutableMap<Int, Boolean> =
    HashMap()
  lateinit var mainFromDate: String
  lateinit var mainToDate: String
  lateinit var fromDat: Date
  lateinit var toDate: Date
  private lateinit var employeeAdapter: ArrayAdapter<EmployeeModel>
  private lateinit var profileAdapter: ArrayAdapter<RowData>
  private val datePickerEvent = PublishSubject.create<Int>()

  @Inject
  lateinit var presenter: PendingForApprovalPresenter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragPendingForApprovalBinding.bind(view)
    setAppTitle("Pending For Approvals")
    setUpView()
    setUpDate()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    datePickerEvent.onNext(0)
  }

  override fun getLayout(): Int {
    return layout.frag_pending_for_approval
  }

  private fun setUpView() {

    binding.btnApprove.setOnClickListener(this)
    binding.btnReject.setOnClickListener(this)
    employeeAdapter = getDefaultAdapter(requireContext())
    binding.spSelectEmployee.adapter = employeeAdapter
    profileAdapter = getDefaultAdapter(requireContext())
    binding.spProfileChange.adapter = profileAdapter
    pendingApprovalTableAdapter = PendingApprovalTableAdapter()
    binding.table.adapter = pendingApprovalTableAdapter
  }

  private fun setUpDate() {
    mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth(date6)
    fromDat = CRMAppUtil.getDate(mainFromDate, date6)
    mainToDate = CRMAppUtil.getDate(Date(), date6)
    toDate = CRMAppUtil.getDate(mainToDate, date6)

    binding.tvStartDate.setText(
      AppDateFormats.DF_JSON_DATE6.format(fromDat)
    )
    binding.llEndDate.setOnClickListener {
      selectDate()
    }
    binding.tvEndDate.setText(
      AppDateFormats.DF_JSON_DATE6.format(toDate)
    )
    binding.llStartDate.setOnClickListener {
      selectDate()
    }

  }

  private fun selectDate() {
    val dialog = Dialog(requireContext())
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(layout.date_picker_from_to)
    dialog.getWindow()
      ?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    val dpFromDate: DatePicker = dialog.findViewById(R.id.datePickerFromDate)
    val dpToDate: DatePicker = dialog.findViewById(R.id.datePickerToDate)
    dpToDate.maxDate = System.currentTimeMillis() - 1000
    dpFromDate.maxDate = System.currentTimeMillis() - 1000
    val close: ImageView =
      dialog.findViewById(R.id.ibtnClose)
    val btnSubmit: Button =
      dialog.findViewById(R.id.btnSubmit)

    try {
      val dateSplit: Array<String> = mainFromDate.split("-").toTypedArray()
      dpFromDate.init(
        dateSplit[0].toInt(),
        dateSplit[1].toInt() - 1,
        dateSplit[2].toInt()
      ) { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        mainFromDate =
          year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
      }
    } catch (e: Exception) {
      Timber.d(e)
    }

    try {
      val dateSplit: Array<String> = mainToDate.split("-").toTypedArray()
      dpToDate.init(
        dateSplit[0].toInt(),
        dateSplit[1].toInt() - 1,
        dateSplit[2].toInt()
      ) { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        mainToDate = year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
      }
    } catch (e: Exception) {
      Timber.d(e)
    }

    close.setOnClickListener { v: View? -> dialog.dismiss() }
    btnSubmit.setOnClickListener { v: View? ->
      fromDat = CRMAppUtil.getDate(mainFromDate, date6)
      toDate = CRMAppUtil.getDate(mainToDate, date6)
      if (fromDat.after(toDate)) {
        MSFDialog.alertDialog(
          requireContext(), 0, "Motilal Oswal", MOSLConstants.INVALID_DATE_RANGE,
          "OK", true, null
        )
      } else {
        mainFromDate.apply {
          fromDat = CRMAppUtil.getDate(this, date6)
          binding.tvStartDate.setText(AppDateFormats.DF_JSON_DATE6.format(fromDat))
        }
        mainToDate.apply {
          toDate = CRMAppUtil.getDate(this, date6)
          binding.tvEndDate.setText(AppDateFormats.DF_JSON_DATE6.format(toDate))
        }
        datePickerEvent.onNext(0)
        dialog.dismiss()
      }

    }
    dialog.show()
  }

  fun prefix(input: Int): String {
    return if (Integer.toString(input).length == 1) {
      "0$input"
    } else input.toString() + ""
  }

  override fun onClick(v: View) {
    when (v.id) {
      R.id.btnApprove -> if (selectedRowCount > 0 || cbClientName.isChecked) {
        MSFDialog.alertDialog(
          requireContext(), 0,
          resources.getString(string.motilal_oswal),
          "Are you sure, you want to approve selected request?", "Yes", "No", true
        ) { action: Action, data: Array<Any?>? ->
          if (action == OK) {
            sendUpdateAppoovalWebService("2")
          }
        }
      } else {
        MSFDialog.alertDialog(
          requireContext(), 0,
          resources.getString(string.motilal_oswal),
          "Please select at least one record", "OK", true, null
        )
      }
      R.id.btnReject -> if (selectedRowCount > 0 || cbClientName.isChecked) {
        MSFDialog.alertDialog(
          requireContext(), 0,
          resources.getString(string.motilal_oswal),
          "Are you sure, you want to reject selected request?", "Yes", "No", true
        ) { action: Action, data: Array<Any?>? ->
          if (action == OK) {
            sendUpdateAppoovalWebService("3")
          }
        }
      } else {
        MSFDialog.alertDialog(
          requireContext(), 0,
          resources.getString(string.motilal_oswal),
          "Please select at least one record", "OK", true, null
        )
      }
      R.id.llStartDate -> selectDate()
      R.id.llEndDate -> selectDate()
    }
  }

  private val selectedRowCount: Int
    get() = selectedRowList.size

  private fun renderMultiUpdate(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)
    val error = uiModel.error
    error?.let { showOnFailurePopup(it) }
    val data = uiModel.data
    if (data != null) {
      var message = ""
      try {
        val response = JSONObject1(data)
        message = response[MOSLConstants.MESSAGE].toString()
      } catch (e: JSONException) {
        e.printStackTrace()
      }
      MSFDialog.alertDialog(
        requireContext(),
        0,
        resources.getString(string.motilal_oswal),
        message,
        "OK",
        true
      ) { action: Action, data1: Array<Any?>? ->
        if (action == OK) {
          // webServiceCall(employeeCodeStr, profileCodeStr)
        }
      }
    }
  }

  private fun sendUpdateAppoovalWebService(status: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      var approvalRequestId = StringBuilder()
      for (approvalId in selectedRowList) {
        if (approvalRequestId.toString() == "") {
          approvalRequestId = StringBuilder(approvalId)
        } else {
          approvalRequestId.append(",").append(approvalId)
        }
      }
      val request =
        MultiUpdateApprovalRequest(status, approvalRequestId.toString())
      val subscribe: Disposable = saathiApi.postMultiUpdateApproval(request)
        .compose(applyUiModel())
        .subscribe({ uiModel: UiModel<String> -> renderMultiUpdate(uiModel) })
    } else {
      noInternetConnectionDialog()
    }
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

  inner class PendingApprovalTableAdapter() : BaseTableAdapter() {
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
    private var inflater: LayoutInflater? = null
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
        0 -> getFirstHeader(column, requireActivity())
        1 -> getHeader(column, requireActivity())
        2 -> getFirstBody(row, column, requireActivity())
        3 -> getBody(row, column, requireActivity())
        4 -> getFamilyView(row, column, requireActivity())
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
      cbClientName = binding.cbClientName
      if (isAllChecked) {
        cbClientName.setChecked(true)
      } else {
        cbClientName.setChecked(false)
      }
      cbClientName.setOnClickListener({ view: View ->
        isAllChecked = (view as CheckBox).isChecked
        for (ctr in 0 until rowCount) {
          val approvalRequestId = getDevice(ctr - 1).data[12]
          selectedRowMap[ctr] = view.isChecked
          if (cbClientName.isChecked()) {
            selectedRowList.add(approvalRequestId)
            isSingleAllChecked = false
          } else {
            selectedRowList.remove(approvalRequestId)
          }
        }
        notifyDataSetChanged()
      })
      if (selectedRowList.isEmpty() || isSingleAllChecked) {
        cbClientName.setChecked(false)
      }
      if (selectedRowCount > 0) {
      } else {
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
          val sortColVal = sortLinkedMap[sortCol] as String?
          if (sortColVal == sortedValue) {
            pendingApprovalTableAdapter.add(sortLinkedMap)
            tempHashArray.remove(sortLinkedMap)
          }
        }
      }
      pendingApprovalTableAdapter.notifyDataSetChanged()
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
      if (selectedRowCount > 0) {
      } else {
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
                  pendingApprovalTableAdapter.add(sortLinkedMap)
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
                  pendingApprovalTableAdapter.add(sortLinkedMap)
                  tempHashArray.remove(sortLinkedMap)
                }
              }
            }
          }
          pendingApprovalTableAdapter.notifyDataSetChanged()
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
        widthSize = resources.getDimensionPixelSize(dimen._100sdp)
      }
      for (i in 0 until column) {
        widthSize = resources.getDimensionPixelSize(dimen._135sdp)
      }
      return widthSize
    }

    override fun getHeight(row: Int): Int {
      val height: Int
      height = if (row == -1) {
        resources.getDimensionPixelSize(dimen._40sdp)
      } else {
        resources.getDimensionPixelSize(dimen._40sdp)
      }
      return height
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
  }

  override fun render(uiModel: UiModel<PendingForApprovalDropdownData>) {
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {
      employeeAdapter.setSpinnerData(binding.spSelectEmployee, it.listOfEmployee)
      val arrRepeatMode = ArrayList<RowData>()
      it.listOfProfile.safeMap { item ->
        arrRepeatMode.add(
          RowData(+1, item.entityField, item.referenceName)
        )
      }
      profileAdapter.setSpinnerData(binding.spProfileChange, arrRepeatMode)
      val productData =
        RowData.getSpinnerRowDataById(
          arguments?.getString(MOSLConstants.ENTITY_FIELD),
          profileAdapter
        )
      if (productData != null) {
        binding.spProfileChange.setSelection(productData.position)
      }
    }
  }

  override fun renderApprovalList(uiModel: UiModel<List<PendingForApprovalItem>>) {
    toggleProgress(uiModel.inProgress)
    selectedRowList.clear()
    selectedRowMap.clear()
    isAllChecked = false
    isSingleAllChecked = false
    uiModel.onFailure {
      binding.table.visibility = View.GONE
      binding.tvNochart.visibility = View.VISIBLE
      binding.llButton.visibility = View.GONE
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      if (it.size > 0) {
        it.map {
          val requestDate = getFormatedDate(it.updationDate, DF_JSON_DATE10, DF_JSON_DATE4)
          val approvedDate = if (it.approver2StatusUpdateDate.isNotEmpty()) {
            getFormatedDate(it.approver2StatusUpdateDate, DF_JSON_DATE10, DF_JSON_DATE4)
          } else {
            it.approver2StatusUpdateDate
          }
          val hm =
            LinkedHashMap<String, String>()
          hm[MOSLConstants.EMPLOYEE_CODE_APP] = it.updatedBy
          hm[MOSLConstants.EMP_NAME] = it.employeeName
          hm[MOSLConstants.LEAD_ID_APP] = it.leadID
          hm[MOSLConstants.CLIENT_CODE] = it.clientCode
          hm[MOSLConstants.UCID] = it.ucid
          hm[MOSLConstants.NEW_CLIENT_NAME] = it.clientName
          hm[MOSLConstants.MODULE_NAME] = it.entity
          hm[MOSLConstants.FIELD_NAME] = it.referenceName
          hm[MOSLConstants.SUB_OPTION] = it.approvalFieldValue
          hm[MOSLConstants.STATUS] = it.getStatus()
          hm[MOSLConstants.REQUEST_DATE] = requestDate
          hm[MOSLConstants.APPROVED_DATE] = approvedDate
          hm[MOSLConstants.APPROVAL_REQUEST_ID] = it.approvalRequestId
          pendingApprovalTableAdapter.add(hm)
        }
        binding.table.visibility = View.VISIBLE
        binding.tvNochart.visibility = View.GONE
        binding.llButton.visibility = View.VISIBLE
        pendingApprovalTableAdapter.notifyDataSetChanged()
      } else {
        binding.table.visibility = View.GONE
        binding.tvNochart.visibility = View.VISIBLE
        binding.llButton.visibility = View.GONE
      }

    }

  }

  override fun onEmployeeChange(): Observable<Int> {
    return binding.spSelectEmployee.itemSelections()
  }

  override fun onProfileChange(): Observable<Int> {
    return binding.spProfileChange.itemSelections()
  }

  override fun getEmployee(): EmployeeModel {
    return binding.spSelectEmployee.selectedItem as EmployeeModel
  }

  override fun getProfile(): RowData {
    return binding.spProfileChange.selectedItem as RowData
  }

  override fun fromDate(): String {
    return binding.tvStartDate.getText().toString() + " 00:00:00"
  }

  override fun toDate(): String {
    return binding.tvEndDate.getText().toString() + " 00:00:00"
  }

  override fun onDatePickerEvent(): Observable<Int> {
    return datePickerEvent.ofType()
  }

  companion object {
    const val date6 = "yyyy-MM-dd"
  }
}