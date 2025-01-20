package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragFocoDashboardReportBinding
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FocoDashboardReportFragment : CommonFragment(),
    FocoDashboardReportView {
  private lateinit var binding: FragFocoDashboardReportBinding

  @Inject
  lateinit var presenter: FocoDashboardReportPresenter
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<FocoDashboardReportTableRow>
  private val navigateEvent = PublishSubject.create<FocoDashboardReportUIEvent.OnReportClick>()

  private var strSystemName : String = ""
  private var strEmployeeCode : String = ""
  private var strSelectedPeriod : String = ""
  lateinit var periodAdapter: ArrayAdapter<FocoPeriodModel>
  lateinit var clientReactAdapter: ArrayAdapter<String>
  lateinit var userTypeAdapter : ArrayAdapter<String>

  override fun getLayout(): Int {
    return R.layout.frag_foco_dashboard_report
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragFocoDashboardReportBinding.bind(view)
    setUpFromArguments()
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpFromArguments() {
    try {
      strSystemName =this.arguments?.getString(MOSLConstants.SYSTEM_NAME, "") ?: ""
      strEmployeeCode = (this.arguments?.getString(MOSLConstants.EMPLOYEE_CODE, "") ?: userManager.getUser()?.employeeCode).toString()
      strSelectedPeriod = this.arguments?.getString(MOSLConstants.PERIOD_FILTER, "") ?: ""

      Log.d("systemName", strSystemName)
      Log.d("employeeCode", strEmployeeCode)
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  @SuppressLint("SetTextI18n")
  private fun setUpView() {
    binding.txtLables.text = strSystemName
    if(strSystemName == MOSLConstants.INDIRECT_RUNRATE_FOR_ADVISOR){
      binding.labelContainer.visibility = View.GONE
      binding.spActiveInactive.visibility = View.GONE
    }

    if(strSystemName == MOSLConstants.INDIRECT_CLIENT_REACTIVATION){
      binding.spClientReactivation.visibility = View.VISIBLE
    }

    periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spPeriod.adapter = periodAdapter

    clientReactAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spClientReactivation.adapter = clientReactAdapter
    clientReactAdapter.setSpinnerData(binding.spClientReactivation, getClientReactivationFilterList())

    userTypeAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spActiveInactive.adapter = userTypeAdapter
    userTypeAdapter.setSpinnerData(binding.spActiveInactive, getUserTypeFilter())

    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = HierarchyReportTableMapper.header(
        requireContext()
    ) as DynamicHeaderRow
    val mfAdapter = requireContext().createRowAdapter<FocoDashboardReportTableRow>(dynamicHeaderRow)
      .apply {

      }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)

  }

  private fun getClientReactivationFilterList() : ArrayList<String>{
    val list = arrayListOf<String>()
    list.add("Existing")
    list.add("New Account")
    return list
  }


  private fun getUserTypeFilter() : ArrayList<String>{
    val list = arrayListOf<String>()
    list.add("Select")
    list.add("Active")
    list.add("InActive")
    return list
  }

  override fun renderData(uiModel: UiModel<DashboardReportCombo>) {
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
      val hierarchyReport: List<FocoDashboardReportTableRow> =
        FocoDashboardReportTableMapper.map(data.data)
      binding.tableWrapper.setData(
          hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
      )
    }
  }

  override fun onReportClickEvent(): Observable<FocoDashboardReportUIEvent.OnReportClick> {
    return navigateEvent.ofType()
  }

  override fun getWidgetName(): String {
    return strSystemName
  }

  override fun getEmployeeCode(): String {
    return strEmployeeCode
  }
  override fun renderPeriodFilter(uiModel: UiModel<List<FocoPeriodModel>>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure { error ->
      Timber.d("renderPeriodFilter ${error.message}")
    }
    uiModel.onSuccess {
      periodAdapter.setSpinnerData(binding.spPeriod, it)
      val pos = getSpinnerRowDataByName(strSelectedPeriod, periodAdapter)
      binding.spPeriod.setSelection(pos)
    }
  }
  fun getSpinnerRowDataByName(period: String?, arrayAdapter: ArrayAdapter<FocoPeriodModel>): Int {

    for (i in 0 until arrayAdapter.count) {
      if (arrayAdapter.getItem(i)!!.displayText.equals(period, ignoreCase = true)) {
        return i
      }
    }
    return 0
  }
  override fun onPeriodChange(): Observable<Int> {
    return binding.spPeriod.itemSelections()
  }

  override fun getPeriod(): FocoPeriodModel? {
      return binding.spPeriod.selectedItem as? FocoPeriodModel
  }

  override fun onClientReactivationFilterChange(): Observable<Int> {
    return binding.spClientReactivation.itemSelections()
  }

  override fun getClientReactivationFilter(): String? {
    return binding.spClientReactivation.selectedItem as? String
  }


  override fun onUserTypeChange(): Observable<Int> {
    return binding.spActiveInactive.itemSelections()
  }

  override fun getUserType(): String? {
    return binding.spActiveInactive.selectedItem as? String
  }

}