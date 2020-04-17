package com.mosl.tabapp.pendingApproval.report

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
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
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragPendingForApprovalBinding
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
import java.util.Date
import java.util.LinkedHashMap
import javax.inject.Inject
import org.json.JSONObject as JSONObject1

class PendingForApprovalFragment : CommonFragment(),
  OnClickListener, MOSLConstants, PendingForApprovalView {
  lateinit var binding: FragPendingForApprovalBinding
  lateinit var pendingApprovalTableAdapter: PendingApprovalTableAdapter
  lateinit var mainFromDate: String
  lateinit var mainToDate: String
  lateinit var fromDat: Date
  lateinit var toDate: Date
  private lateinit var employeeAdapter: ArrayAdapter<EmployeeModel>
  private lateinit var profileAdapter: ArrayAdapter<RowData>
  private val datePickerEvent = PublishSubject.create<Int>()
  private val subject = PublishSubject.create<PendingForApprovalUIEvent>()
  private var checkBoxStatus = false
  private var selectedApprovalList = mutableListOf<String>()

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
    pendingApprovalTableAdapter = PendingApprovalTableAdapter(requireActivity(), subject)
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
      R.id.btnApprove -> if (selectedApprovalList.size > 0) {
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
      R.id.btnReject -> if (selectedApprovalList.size > 0) {
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
      for (approvalId in selectedApprovalList) {
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

  override fun onSelectedApprovalList(): Observable<PendingForApprovalUIEvent.CheckBoxSelectedArrayList> {
    return subject.ofType()
  }

  override fun selectedApprovalList(arrayList: java.util.ArrayList<String>) {
    selectedApprovalList = arrayList
  }

  companion object {
    const val date6 = "yyyy-MM-dd"
  }
}