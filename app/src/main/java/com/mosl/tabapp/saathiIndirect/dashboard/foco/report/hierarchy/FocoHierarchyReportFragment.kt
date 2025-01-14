package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
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
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.visible
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FocoHierarchyReportFragment : CommonFragment(),
    FocoHierarchyReportView {
  private lateinit var binding: FragIndirectHierarchyReportBinding

  @Inject
  lateinit var presenter: FocoHierarchyReportPresenter
  private val navigateEvent = PublishSubject.create<FocoHierarchyReportUIEvent>()

  private var strSystemName : String = ""
  private var strSelectedPeriod : String = ""
  lateinit var periodAdapter: ArrayAdapter<FocoPeriodModel>
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
      strSelectedPeriod = this.arguments?.getString(MOSLConstants.PERIOD_FILTER, "") ?: ""

      Log.d("systemName", strSystemName)
    } catch (e: Exception) {
      e.printStackTrace()
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
  @SuppressLint("SetTextI18n")
  private fun setUpView() {
    binding.txtLables.text = "$strSystemName Hierarchy"
    if(strSystemName == FocoTargetPresenter.NO_OF_CLIENT_MEETINGS){
      binding.revenue.text = "Meeting"
    }
    periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spPeriod.adapter = periodAdapter
  }

  override fun renderReportData(uiModel: UiModel<List<SalesRM>>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure { error ->
    }

    uiModel.onSuccess { data ->
      binding.recyclerView.visibility = View.VISIBLE
      val adapter = HierarchyListAdapter(data, strSystemName, false, navigateEvent)
      binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
      binding.recyclerView.adapter = adapter
    }
  }

  override fun onReportClickEvent(): Observable<FocoHierarchyReportUIEvent.OnReportClick> {
    return navigateEvent.ofType()
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

  override fun onPeriodChange(): Observable<Int> {
    return binding.spPeriod.itemSelections()
  }

  override fun getPeriod(): FocoPeriodModel? {
    return binding.spPeriod.selectedItem as? FocoPeriodModel
  }

  override fun getWidgetName(): String {
    return strSystemName
  }


}