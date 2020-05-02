package com.mosl.tabapp.dashboard.report

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MoslConstant.CLIENT_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_ucid
import com.mosl.tabapp.constants.MoslConstant.LEADID_CLIENTCODE
import com.mosl.tabapp.constants.MoslConstant.LEAD_ID
import com.mosl.tabapp.constants.MoslConstant.LeadId
import com.mosl.tabapp.constants.MoslConstant.LeadId_ClientCode
import com.mosl.tabapp.constants.MoslConstant.Lead_Id
import com.mosl.tabapp.constants.MoslConstant.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FlgEmpcode
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.Flg_Hierarchy
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FltHierarchyEmpCode
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.WidgetSystemName
import com.mosl.tabapp.dashboard.report.DashboardReportUIEvent.CellEvent
import com.mosl.tabapp.databinding.FragDashboardReportBinding
import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DashboardReportFragment : CommonFragment(),
  DashboardReportView {
  private lateinit var binding: FragDashboardReportBinding

  @Inject
  lateinit var presenter: DashboardReportPresenter
  private lateinit var model: WidgetModel
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<DashboardReportTableRow>
  private val navigateEvent = PublishSubject.create<CellEvent>()
  private lateinit var typeAdapter: ArrayAdapter<TypeActionableFilterModel>
  private lateinit var periodAdapter: ArrayAdapter<FilterRowData>

  override fun getLayout(): Int {
    return R.layout.frag_dashboard_report
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragDashboardReportBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = HierarchyReportTableMapper.header(
      requireContext()
    ) as DynamicHeaderRow
    val mfAdapter =
      requireContext().createRowAdapter<DashboardReportTableRow>(dynamicHeaderRow)
        .apply {
          viewProvider.rowClickListener = { row, view ->
            val reportData = tableRowAdapter.getItem(row).data
            val leadId = getValue(
              LEAD_ID, LeadId, Lead_Id, LeadId_ClientCode,
              LEADID_CLIENTCODE, hasMap = reportData.hasMap
            )
            val ucid = getValue(
              UCID, Client_UCID, CLIENT_UCID, Client_ucid, hasMap = reportData.hasMap
            )
            val empCode = getValue(FlgEmpcode, hasMap = reportData.hasMap)
            val systemsName = getValue(WidgetSystemName, hasMap = reportData.hasMap)
            val rmName = reportData.hasMap.get(FltHierarchyEmpCode) ?: ""
            val level = getValue(Flg_Hierarchy, hasMap = reportData.hasMap)

            navigateEvent.onNext(
              CellEvent(
                leadId, ucid, empCode, level, systemsName, rmName
              )
            )
          }
        }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)

    typeAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spType.adapter = typeAdapter

    periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spPeriod.adapter = periodAdapter
  }

  private fun getValue(vararg keys: String, hasMap: Map<String, String>): String {
    for (key in keys) {
      val value: String? = hasMap.get(key)
      if (value != null) {
        return value
      }
    }
    return "0"
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
      val hierarchyReport: List<DashboardReportTableRow> =
        DashboardReportTableMapper.map(data.data)
      binding.tableWrapper.setData(
        hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
      )
    }
  }

  override fun renderLabel(labelName: String?) {
    labelName.let {
      binding.txtLables.text = it
    }

  }

  override fun renderDatePicker(isDateFilter: Boolean) {
    if (isDateFilter) {
      binding.linearLayout.visibility = View.VISIBLE
      binding.ivDatePicker.visibility = View.VISIBLE
    }
  }

  override fun renderPeriodFilter(periodFilter: List<FilterRowData>) {
    if (periodFilter.isNotEmpty()) {
      binding.linearLayout.visibility = View.VISIBLE
      binding.spPeriod.visibility = View.VISIBLE
      periodAdapter.setSpinnerData(binding.spPeriod, periodFilter)
    }
  }

  override fun renderTypeFilter(typeFilter: List<TypeActionableFilterModel>) {
    if (typeFilter.isNotEmpty()) {
      binding.linearLayout.visibility = View.VISIBLE
      binding.spType.visibility = View.VISIBLE
      typeAdapter.setSpinnerData(binding.spType, typeFilter)
    }
  }

  override fun onNavigateEvent(): Observable<CellEvent> {
    return navigateEvent.ofType()
  }

  override fun onFragmentReopened(args: Bundle) {
    super.onFragmentReopened(args)
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)

  }

}