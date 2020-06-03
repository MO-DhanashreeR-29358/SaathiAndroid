package com.mosl.tabapp.report

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.report.AssignedLeadReportModel
import com.mosl.mobile.domain.report.HierarchyReportCombo
import com.mosl.shared.util.AppDateFormats
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME
import com.mosl.tabapp.constants.MoslConstant.CLIENT_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_ucid
import com.mosl.tabapp.constants.MoslConstant.EMPLOYEE_CODE
import com.mosl.tabapp.constants.MoslConstant.Employee_Code
import com.mosl.tabapp.constants.MoslConstant.LEADID_CLIENTCODE
import com.mosl.tabapp.constants.MoslConstant.LEAD_ID
import com.mosl.tabapp.constants.MoslConstant.LeadId
import com.mosl.tabapp.constants.MoslConstant.LeadId_ClientCode
import com.mosl.tabapp.constants.MoslConstant.Lead_Id
import com.mosl.tabapp.constants.MoslConstant.yyyy_MM_dd
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragReportBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.report.HierarchyReportUIEvent.CellEvent
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.NAME
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class ReportFragment : CommonFragment(),
  ReportView {
  private lateinit var binding: FragReportBinding
  val TAG: String = ReportFragment::class.java.simpleName
  @Inject
  lateinit var presenter: ReportPresenter
  private lateinit var model: WidgetModel
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<HierarchyReportTableRow>
  private val navigateEvent = PublishSubject.create<CellEvent>()
  private val datePickerEvent = PublishSubject.create<Int>()
  private val onRetryEvent = PublishSubject.create<Unit>()
  private lateinit var branchAdapter: ArrayAdapter<AssignedLeadReportModel>

  lateinit var mainFromDate: String
  private lateinit var mainToDate: String

  lateinit var fromDat: Date
  lateinit var toDate: Date
  lateinit var systemsName: String
  lateinit var title: String
  override fun getLayout(): Int {
    return layout.frag_report
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragReportBinding.bind(view)
    setUpView()
    setUpDate()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    datePickerEvent.onNext(0)

  }

  private fun setUpView() {

    branchAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spBranch.adapter = branchAdapter
    binding.txtLables.text = arguments?.getString(NAME)
    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = HierarchyReportTableMapper.header(
      requireContext()
    ) as DynamicHeaderRow
    val mfAdapter =
      requireContext().createRowAdapter<HierarchyReportTableRow>(dynamicHeaderRow)
        .apply {
          viewProvider.rowClickListener = { row, _ ->
            val reportData = tableRowAdapter.getItem(row).data
            val leadId = getValue(
              LEAD_ID,
              LeadId,
              Lead_Id,
              LeadId_ClientCode,
              LEADID_CLIENTCODE,
              hasMap = reportData.hasMap
            )
            val ucid = getValue(
              Client_UCID, CLIENT_UCID, Client_ucid, hasMap = reportData.hasMap
            )
            val employeeCode = getValue(
              Employee_Code, EMPLOYEE_CODE,
              hasMap = reportData.hasMap
            )
            navigateEvent.onNext(
              CellEvent(
                leadId
                , ucid, employeeCode
              )
            )
          }
        }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)
    systemsName = arguments?.getString(SYSTEM_NAME) ?: ""
    title = arguments?.getString(NAME) ?: ""

  }

  private fun setUpDate() {
    mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth(yyyy_MM_dd)
    fromDat = CRMAppUtil.getDate(mainFromDate, yyyy_MM_dd)
    mainToDate = CRMAppUtil.getDate(Date(), yyyy_MM_dd)
    toDate = CRMAppUtil.getDate(mainToDate, yyyy_MM_dd)

    binding.tvStartDate.text =
      AppDateFormats.DF_JSON_DATE6.format(fromDat)

    binding.rlEndDate.setOnClickListener {
      selectDate()
    }
    binding.tvEndDate.text =
      AppDateFormats.DF_JSON_DATE6.format(toDate)

    binding.rlStartDate.setOnClickListener {
      selectDate()
    }

  }

  private fun selectDate() {
    val dialog = Dialog(requireContext())
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(layout.date_picker_from_to)
    dialog.window
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
      ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
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
      ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        mainToDate = year.toString() + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth)
      }
    } catch (e: Exception) {
      Timber.d(e)
    }

    close.setOnClickListener { dialog.dismiss() }
    btnSubmit.setOnClickListener {
      fromDat = CRMAppUtil.getDate(mainFromDate, yyyy_MM_dd)
      toDate = CRMAppUtil.getDate(mainToDate, yyyy_MM_dd)
      if (fromDat.after(toDate)) {
        MSFDialog.alertDialog(
          requireContext(), 0, "Motilal Oswal", MOSLConstants.INVALID_DATE_RANGE,
          "OK", true, null
        )
      } else {
        mainFromDate.apply {
          fromDat = CRMAppUtil.getDate(this, yyyy_MM_dd)
          binding.tvStartDate.text = AppDateFormats.DF_JSON_DATE6.format(fromDat)
        }
        mainToDate.apply {
          toDate = CRMAppUtil.getDate(this, yyyy_MM_dd)
          binding.tvEndDate.text = AppDateFormats.DF_JSON_DATE6.format(toDate)
        }
        datePickerEvent.onNext(0)
        dialog.dismiss()
      }

    }
    dialog.show()
  }

  fun prefix(input: Int): String? {
    return if (input.toString().length == 1) {
      "0$input"
    } else input.toString() + ""
  }

  private fun getValue(vararg keys: String, hasMap: Map<String, String>): String {
    for (key in keys) {
      val value: String? = hasMap[key]
      if (value != null) {
        return value
      }
    }
    return "0"
  }

  override fun renderData(uiModel: UiModel<HierarchyReportCombo>) {
    binding.tableWrapper.toggleProgress(uiModel.inProgress)

    uiModel.onFailure { error ->
      binding.tableWrapper.showError(error)
    }

    uiModel.onSuccess { data ->
      val headerData = mutableListOf<String>()
      data.headers.map {
        headerData.add(CRMAppUtil.getRefreshColumnName(it))
      }
      dynamicHeaderRow.setHeader(headerData)
      val hierarchyReport: List<HierarchyReportTableRow> =
        HierarchyReportTableMapper.map(data.data)
      binding.tableWrapper.setData(
        hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
      )
    }
  }

  override fun widgetSystemsName(): String? {
    return systemsName
  }

  override fun titleName(): String? {
    return title
  }

  override fun fromDate(): String {
    return CRMAppUtil.getDateFromString(
      binding.tvStartDate.text.toString(),
      "dd-MMM-yyyy",
      yyyy_MM_dd
    )
  }

  override fun toDate(): String {
    return CRMAppUtil.getDateFromString(
      binding.tvEndDate.text.toString(),
      "dd-MMM-yyyy",
      yyyy_MM_dd
    )
  }

  override fun onBranchChange(): Observable<Int> {
    return binding.spBranch.itemSelections()
  }

  override fun onViewChange(): Observable<Unit> {
    return onRetryEvent.ofType()
  }

  override fun getBranch(): AssignedLeadReportModel? {
    return binding.spBranch.selectedItem as? AssignedLeadReportModel
  }

  override fun renderBranchFilter(uiModel: UiModel<List<AssignedLeadReportModel>>) {
    uiModel.onFailure { error ->
      showOnFailurePopup(error)
    }

    uiModel.onSuccess {
      branchAdapter.setSpinnerData(binding.spBranch, it)

    }
  }

  override fun onNavigateEvent(): Observable<CellEvent> {
    return navigateEvent.ofType()
  }

  override fun onDatePickerEvent(): Observable<Int> {
    return datePickerEvent.ofType()
  }

  override fun onFragmentReopened(args: Bundle) {
    super.onFragmentReopened(args)
    title = args.getString(NAME) ?: ""
    systemsName = args.getString(SYSTEM_NAME) ?: ""
    binding.txtLables.text = args.getString(MOSLConstants.NAME)
    onRetryEvent.onNext(Unit)
    datePickerEvent.onNext(0)
  }

}