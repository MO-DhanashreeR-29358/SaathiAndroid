package com.mosl.tabapp.dashboard.snapshot

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.SELF_KEY
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragBusinessSnapshotNewBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetFilterModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory.getDefaultAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BusinessSnapshotFragment : CommonFragment(), BusinessSnapshotView {
  private lateinit var binding: FragBusinessSnapshotNewBinding

  @Inject
  lateinit var presenter: BusinessSnapshotPresenter

  @Inject
  lateinit var dashboardModel: DashboardModel
  private var model: WidgetModel = WidgetModel()
  private lateinit var dynamicHeaderRow: DynamicHeaderRow
  private lateinit var tableRowAdapter: TableRowAdapter<BusinessSnapshotTableRow>
  private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()

  override fun getLayout(): Int {
    return R.layout.frag_business_snapshot_new
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragBusinessSnapshotNewBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    val groupModel = dashboardModel.getGroupModel(
      DashboardSystemModel.getBusinessSnapshotSystemName()
    )
    val widgetList = groupModel.widgetList
    if (widgetList.size > 0) {
      model = widgetList[0]
    }
    val filterList = model.widgetFilterList
    setFilterSpinner(filterList.size, filterList)
    binding.tableWrapper.setRetryListener(onRetryClick)
    dynamicHeaderRow = BusinessSnapshotTableMapper.header(requireContext()) as DynamicHeaderRow
    val mfAdapter =
      requireContext().createRowAdapter<BusinessSnapshotTableRow>(dynamicHeaderRow)
        .apply {
          viewProvider.rowClickListener = { row, view ->
            val column = view.getTag(R.id.tag_column) as Int
            val reportData = tableRowAdapter.getItem(row).data
            navigateEvent.onNext(
              getAdditionalParameterMap(
                reportData.data[0],
                reportData.headers[column + 1]
              )
            )
          }
        }
    tableRowAdapter = mfAdapter
    binding.tableWrapper.setAdapter(tableRowAdapter)
  }

  private fun setFilterSpinner(
    count: Int,
    filterList: MutableList<WidgetFilterModel>
  ) {
    val context = requireContext()
    binding.apply {
      val adapter1 = getDefaultAdapter<FilterRowData>(context)
      spFilter1.adapter = adapter1
      adapter1.setSpinnerData(spFilter1, filterList[0].filterData)
      if (count == 2 || count == 3) {
        val adapter2 = getDefaultAdapter<FilterRowData>(context)
        spFilter2.adapter = adapter2
        adapter2.setSpinnerData(spFilter2, filterList[1].filterData)
        spFilter2.visibility = View.VISIBLE
      }
      if (count == 3) {
        val adapter3 = getDefaultAdapter<FilterRowData>(context)
        spFilter3.adapter = adapter3
        adapter3.setSpinnerData(spFilter3, filterList[1].filterData)
        spFilter3.visibility = View.VISIBLE
      }
    }
  }

  override fun getWidgetSystemsName(): String? {
    return model.systemName
  }

  override fun getFilter1(): FilterRowData? {
    return binding.spFilter1.selectedItem as? FilterRowData
  }

  override fun getFilter2(): FilterRowData? {
    return binding.spFilter2.selectedItem as? FilterRowData
  }

  override fun getFilter3(): FilterRowData? {
    return binding.spFilter3.selectedItem as? FilterRowData
  }

  override fun getAdditionalParameterMap(
    rowData: String?,
    columnData: String?
  ): MutableMap<String, String> {
    val additionalParameterMap: MutableMap<String, String> = HashMap()
    getFilter1()?.let { additionalParameterMap[it.keyValue] = it.value }
    getFilter2()?.let { additionalParameterMap[it.keyValue] = it.value }
    getFilter3()?.let { additionalParameterMap[it.keyValue] = it.value }
    columnData?.let {
      additionalParameterMap[MOSLConstants.COL_FILTER] =
        columnData
    }
    rowData?.let { additionalParameterMap[MOSLConstants.ROW_FILTER] = rowData }
    additionalParameterMap[SELF_KEY] = dashboardModel.dashboardFor
    return additionalParameterMap
  }

  override fun onFilter1Change(): Observable<Int> {
    return binding.spFilter1.itemSelections()
  }

  override fun onFilter2Change(): Observable<Int> {
    return binding.spFilter2.itemSelections()
  }

  override fun onFilter3Change(): Observable<Int> {
    return binding.spFilter3.itemSelections()
  }

  override fun onNavigateEvent(): Observable<MutableMap<String, String>> {
    return navigateEvent.ofType()
  }

  override fun renderData(uiModel: UiModel<BusinessSnapshotCombo>) {
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
      val hierarchyReport: List<BusinessSnapshotTableRow> =
        BusinessSnapshotTableMapper.map(data.data)
      binding.tableWrapper.setData(
        hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
      )
    }
  }
}
