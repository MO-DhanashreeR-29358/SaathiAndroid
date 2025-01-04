package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMAppUtil.getDateStr
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MoslConstant.CLIENT_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_UCID
import com.mosl.tabapp.constants.MoslConstant.Client_ucid
import com.mosl.tabapp.constants.MoslConstant.LEADID_CLIENTCODE
import com.mosl.tabapp.constants.MoslConstant.LEAD_ID
import com.mosl.tabapp.constants.MoslConstant.LeadId
import com.mosl.tabapp.constants.MoslConstant.LeadId_ClientCode
import com.mosl.tabapp.constants.MoslConstant.Lead_Id
import com.mosl.tabapp.constants.MoslConstant.UCID
import com.mosl.tabapp.constants.MoslConstant.dd_MMM_yyyy
import com.mosl.tabapp.constants.MoslConstant.yyyy_MM_dd
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FLT_HIERARCHY_BRANCH_CODE
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FlgEmpcode
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.Flg_Hierarchy
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FltHierarchyEmpCode
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.WidgetSystemName
import com.mosl.tabapp.dashboard.report.DashboardReportUIEvent.CellEvent
import com.mosl.tabapp.databinding.DatePickerBinding
import com.mosl.tabapp.databinding.FragDashboardReportBinding
import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.saathiIndirect.createMeeting.IndirectCreateMeetingUiEvent
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class FocoDashboardReportFragment : CommonFragment(),
    FocoDashboardReportView {
  private lateinit var binding: FragDashboardReportBinding

  @Inject
  lateinit var presenter: FocoDashboardReportPresenter
  private lateinit var model: WidgetModel
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<FocoDashboardReportTableRow>
  private val navigateEvent = PublishSubject.create<CellEvent>()

  var strSystemName : String = ""
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

  private fun setUpView() {
    binding.txtLables.text = strSystemName
    if(strSystemName == MOSLConstants.INDIRECT_RUNRATE_FOR_ADVISOR){
      binding.labelContainer.visibility = View.GONE
    }
    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = HierarchyReportTableMapper.header(
        requireContext()
    ) as DynamicHeaderRow
    val mfAdapter = requireContext().createRowAdapter<FocoDashboardReportTableRow>(dynamicHeaderRow)
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

  fun prefix(input: Int): String? {
    return if (input.toString().length == 1) {
      "0$input"
    } else input.toString() + ""
  }

  override fun getWidgetName(): String {
    return strSystemName
  }


}