package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.SalesRM
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter
import com.mosl.tabapp.databinding.FragDashboardReportBinding
import com.mosl.tabapp.databinding.FragIndirectHierarchyReportBinding
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
  private lateinit var binding: FragIndirectHierarchyReportBinding

  @Inject
  lateinit var presenter: FocoHierarchyReportPresenter
  private val navigateEvent = PublishSubject.create<FocoHierarchyReportUIEvent>()

  private var strSystemName : String = ""

  override fun getLayout(): Int {
    return R.layout.frag_indirect_hierarchy_report
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragIndirectHierarchyReportBinding.bind(view)
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
  }

  override fun renderReportData(uiModel: UiModel<List<SalesRM>>) {
    binding.tableWrapper.toggleProgress(uiModel.inProgress)

    uiModel.onFailure { error ->

    }

    uiModel.onSuccess { data ->
      val adapter = HierarchyListAdapter(data, strSystemName, navigateEvent)
      binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
      binding.recyclerView.adapter = adapter
    }
  }

  override fun onReportClickEvent(): Observable<FocoHierarchyReportUIEvent.OnReportClick> {
    return navigateEvent.ofType()
  }

  override fun getWidgetName(): String {
    return strSystemName
  }


}