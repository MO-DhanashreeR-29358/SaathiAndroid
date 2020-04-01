package com.mosl.tabapp.report.hierarchy

import android.os.Bundle
import android.view.View
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.report.HierarchyReportCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.STRING_TITLE
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragHierarchyReportBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.report.HierarchyReportTableRow
import com.mosl.tabapp.report.HierarchyReportUIEvent.CellEvent
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class HierarchyReportFragment : CommonFragment(),
  HierarchyReportView {
  private lateinit var binding: FragHierarchyReportBinding

  @Inject
  lateinit var presenter: HierarchyReportPresenter

  private lateinit var model: WidgetModel
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<HierarchyReportTableRow>
  private val navigateEvent = PublishSubject.create<CellEvent>()

  override fun getLayout(): Int {
    return R.layout.frag_hierarchy_report
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragHierarchyReportBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {

    binding.txtLables.text = arguments?.getString(STRING_TITLE)
    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = HierarchyReportTableMapper.header(
      requireContext()
    ) as DynamicHeaderRow
    val mfAdapter =
      requireContext().createRowAdapter<HierarchyReportTableRow>(dynamicHeaderRow)
        .apply {
          viewProvider.rowClickListener = { row, view ->
            val reportData = tableRowAdapter.getItem(row).data
            val leadId = getValue(
              "LEAD ID", "LeadId", "Lead_Id",
              "LeadId ClientCode", "LEADID/CLIENTCODE", hasMap = reportData.hasMap
            )
            val UCID = getValue(
              "Client UCID", "CLIENT UCID",
              "Client ucid", hasMap = reportData.hasMap
            )
            navigateEvent.onNext(
              CellEvent(
                leadId
                , UCID
              )
            )
          }
        }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)
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

  override fun onNavigateEvent(): Observable<CellEvent> {
    return navigateEvent.ofType()
  }

  override fun getAdditionalParameterMap(): Map<String, String> {
    return arguments
      ?.getSerializable(MOSLConstants.STRING_MAP) as Map<String, String>
  }

}