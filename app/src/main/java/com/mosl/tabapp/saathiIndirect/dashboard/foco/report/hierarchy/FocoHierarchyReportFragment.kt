package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter
import com.mosl.tabapp.databinding.FragDashboardReportBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportTableMapper
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportTableRow
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class FocoHierarchyReportFragment : CommonFragment(),
    FocoHierarchyReportView {
  private lateinit var binding: FragDashboardReportBinding

  @Inject
  lateinit var presenter: FocoHierarchyReportPresenter
  private lateinit var model: WidgetModel
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<FocoDashboardReportTableRow>
  private val navigateEvent = PublishSubject.create<FocoHierarchyReportUIEvent.OnReportClick>()

  private var strSystemName : String = ""

  override fun getLayout(): Int {
    return R.layout.frag_dashboard_report
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragDashboardReportBinding.bind(view)
    setUpFromArguments()
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpFromArguments() {
    try {
      strSystemName =this.arguments?.getString(MOSLConstants.SYSTEM_NAME, "") ?: ""
      Log.d("systemName", strSystemName)
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  @SuppressLint("SetTextI18n")
  private fun setUpView() {
    binding.txtLables.text = "$strSystemName Hierarchy"

    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = HierarchyReportTableMapper.header(
        requireContext()
    ) as DynamicHeaderRow
    val mfAdapter = requireContext().createRowAdapter<FocoDashboardReportTableRow>(dynamicHeaderRow)
      .apply {
        viewProvider.rowClickListener = { row, _ ->
         /* val reportData = tableRowAdapter.getItem(row).data
          val rmName = reportData.hasMap["SalesRM"] ?: ""*/
          navigateEvent.onNext(FocoHierarchyReportUIEvent.OnReportClick(strSystemName))
        }
      }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)

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

  override fun onReportClickEvent(): Observable<FocoHierarchyReportUIEvent.OnReportClick> {
    return navigateEvent.ofType()
  }

  override fun getWidgetName(): String {
    return strSystemName
  }


}