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
import com.mosl.tabapp.constants.MoslConstant
import com.mosl.tabapp.constants.MoslConstant.CLIENT_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_ucid
import com.mosl.tabapp.constants.MoslConstant.LEADID_CLIENTCODE
import com.mosl.tabapp.constants.MoslConstant.LEAD_ID
import com.mosl.tabapp.constants.MoslConstant.LeadId
import com.mosl.tabapp.constants.MoslConstant.LeadId_ClientCode
import com.mosl.tabapp.constants.MoslConstant.Lead_Id
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragHierarchyReportBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.report.HierarchyReportTableRow
import com.mosl.tabapp.report.HierarchyReportUIEvent.CellEvent
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
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
  private lateinit var additionalMap: HashMap<String, String>
  private val onRetryEvent = PublishSubject.create<Unit>()

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
    hideAppTitle()
    binding.txtLables.text = arguments?.getString(STRING_TITLE)
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
              MoslConstant.Employee_Code, MoslConstant.EMPLOYEE_CODE,
              hasMap = reportData.hasMap
            )
            navigateEvent.onNext(
              CellEvent(
                leadId
                , ucid,
                employeeCode
              )
            )
          }
        }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)
    additionalMap = arguments
      ?.getSerializable(MOSLConstants.STRING_MAP) as HashMap<String, String>
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

  override fun onNavigateEvent(): Observable<CellEvent> {
    return navigateEvent.ofType()
  }

  override fun titleName(): String {
    return binding.txtLables.text.toString()
  }

  override fun getAdditionalParameterMap(): HashMap<String, String> {
    return additionalMap
  }

  override fun onViewChange(): Observable<Unit> {
    return onRetryEvent.ofType()
  }

  override fun onFragmentReopened(args: Bundle) {
    super.onFragmentReopened(args)
    additionalMap = args
      .getSerializable(MOSLConstants.STRING_MAP) as HashMap<String, String>
    binding.txtLables.text = args.getString(STRING_TITLE)
    onRetryEvent.onNext(Unit)
  }

}