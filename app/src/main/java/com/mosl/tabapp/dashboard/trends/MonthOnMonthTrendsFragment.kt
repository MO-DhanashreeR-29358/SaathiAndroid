package com.mosl.tabapp.dashboard.trends

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.YAxis
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.trends.TrendsChartModel
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentTrendsBinding
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MonthOnMonthTrendsFragment : CommonFragment(), MonthsOnMonthsView {
  lateinit var binding: FragmentTrendsBinding

  @Inject
  lateinit var dashboardModel: DashboardModel
  private var widgetModelList: MutableList<DashboardGroupModel> = mutableListOf()
  private var itemList = mutableListOf<HorinzatalTrendsWidgetItem>()
  private val widgetUiEvent =
    PublishSubject.create<TrendsWidgetUiEvent>()
  @Inject
  lateinit var presenter: TrendsPresenter
  private var previousPosition: Int = 0
  private var dashboardGroupModel: DashboardGroupModel? = null
  private lateinit var rightAxis: YAxis
  private lateinit var mMonths: Array<String>
  private var dataFoundFlag = false
  override fun getLayout(): Int {
    return R.layout.fragment_trends
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentTrendsBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    if (itemList.size > 0) {
      widgetUiEvent.onNext(
        TrendsWidgetUiEvent.ItemClick(
          itemList.get(0).dashboardGroupModel,
          0
        )
      )
    }
  }

  private fun setUpView() {
    val groupAdapter = GroupAdapter<ViewHolder>()
    binding.recyclerView.apply {
      layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      setHasFixedSize(true)
      adapter = groupAdapter
    }

    widgetModelList = dashboardModel
      .getGroupModel(DashboardSystemModel.getMonthTrends())
      .subGroupList
    if (widgetModelList.size < 3) {
      widgetModelList.add(DashboardGroupModel())
      widgetModelList.add(DashboardGroupModel())
    } else if (widgetModelList.size < 4) {
      widgetModelList.add(DashboardGroupModel())
    }

    widgetModelList.mapIndexed { position: Int, model: DashboardGroupModel ->
      itemList.add(HorinzatalTrendsWidgetItem(model, widgetUiEvent, requireContext()))
    }
    groupAdapter.addAll(itemList)

  }

  override fun onWidgetItemClick(): Observable<TrendsWidgetUiEvent.ItemClick> {
    return widgetUiEvent.ofType()
  }

  override fun renderWidgetData(dashboardGroupModel: DashboardGroupModel, currentPosition: Int) {
    if (currentPosition != previousPosition) {
      itemList.get(currentPosition).updateItem(true)
      itemList.get(previousPosition).updateItem(false)
      previousPosition = currentPosition
    }
    this.dashboardGroupModel = dashboardGroupModel
  }

  override fun getDashboardGroupModel(): DashboardGroupModel? {
    return dashboardGroupModel
  }

  override fun renderData(uiModel: UiModel<List<TrendsChartModel>>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      binding.includeChart.chart.clear()
    }
    uiModel.onSuccess {
      if (it.size > 0) {
//        setChartData(it)
      } else {
        binding.includeChart.chart.clear()
      }
    }

  }

}