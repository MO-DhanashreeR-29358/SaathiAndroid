package com.mosl.tabapp.dashboard.productivity

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.productivity.ProductivityCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName
import com.mosl.tabapp.constants.MOSLConstants.SELF_KEY
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.productivity.ProductivityTableMapper.map
import com.mosl.tabapp.databinding.FragmentProductivityBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ProductivityFragment : CommonFragment(), productivityView {
  private lateinit var binding: FragmentProductivityBinding

  @Inject
  lateinit var presenter: ProductivityPresenter

  @Inject
  lateinit var dashboardModel: DashboardModel
  private lateinit var model: WidgetModel
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<ProductivityTableRow>
  private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()

  override fun getLayout(): Int {
    return R.layout.fragment_productivity
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentProductivityBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    val groupModel = dashboardModel.getGroupModel(
      DashboardSystemModel.getProductivitySystemName()
    )
    val widgetList = groupModel.widgetList
    if (widgetList.size >= 2) {
      model = widgetList[2]
    }
    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = ProductivityTableMapper.header(requireContext()) as DynamicHeaderRow
    val mfAdapter =
      requireContext().createRowAdapter<ProductivityTableRow>(dynamicHeaderRow)
        .apply {
//          viewProvider.rowClickListener = { row, view ->
//            val reportData = tableRowAdapter.getItem(row).data
//            navigateEvent.onNext(getAdditionalParameterMap(reportData.data[0]))
//          }
        }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)

    if (dashboardModel.dashboardFor.equals(TEAM, ignoreCase = true)) {
      binding.tvShowMore.setVisibility(View.VISIBLE)
    }
  }

  override fun getWidgetSystemsName(): String? {
    return model.systemName
  }

  override fun getAdditionalParameterMap(): MutableMap<String, String> {
    val additionalParameterMap: MutableMap<String, String> = HashMap()
    additionalParameterMap[SELF_KEY] = dashboardModel.dashboardFor
    return additionalParameterMap
  }

  override fun onNavigateEvent(): Observable<MutableMap<String, String>> {
    return navigateEvent.ofType()
  }

  override fun onShowMoreClick(): Observable<Unit> {
    return binding.tvShowMore.clicks()
  }

  override fun renderData(uiModel: UiModel<ProductivityCombo>) {
    binding.tableWrapper.toggleProgress(uiModel.inProgress)

    uiModel.onFailure { error ->
      binding.tableWrapper.showError(error)
    }

    uiModel.onSuccess { data ->
      val headerData = mutableListOf<String>()
      data.headers.map {
        headerData.add(getRefreshColumnName(it))
      }
      dynamicHeaderRow.setHeader(headerData)
      val hierarchyReport: List<ProductivityTableRow> =
        map(data.data)
      binding.tableWrapper.setData(
        hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
      )
    }
  }

  companion object {
    const val TEAM = "TEAM"
    const val PRODUCTIVITY = "Productivity"
  }

}
