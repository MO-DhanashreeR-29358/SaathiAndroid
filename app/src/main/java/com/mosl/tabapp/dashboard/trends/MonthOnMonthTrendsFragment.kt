package com.mosl.tabapp.dashboard.trends

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.trends.TrendsChartModel
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMDecimalValueFormater
import com.mosl.tabapp.common.CRMIAxisValueFormatter
import com.mosl.tabapp.common.CRMValueFormatter
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentTrendsBinding
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
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
          itemList[0].dashboardGroupModel,
          0
        )
      )
    }
  }

  private fun setUpView() {
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
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

    widgetModelList.mapIndexed { _: Int, model: DashboardGroupModel ->
      itemList.add(HorinzatalTrendsWidgetItem(model, widgetUiEvent))
    }
    groupAdapter.addAll(itemList)

  }

  override fun onWidgetItemClick(): Observable<TrendsWidgetUiEvent.ItemClick> {
    return widgetUiEvent.ofType()
  }

  override fun renderWidgetData(dashboardGroupModel: DashboardGroupModel, currentPosition: Int) {
    if (currentPosition != previousPosition) {
      itemList[currentPosition].updateItem(true)
      itemList[previousPosition].updateItem(false)
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
      if (it.isNotEmpty()) {
        setChartData(it)
      } else {
        binding.includeChart.chart.clear()
      }
    }

  }

  private fun setChartData(arrayLists: List<TrendsChartModel>) {

    makeLegend()
    val mChart = makeChart()
    rightAxis = mChart.axisRight
    rightAxis.valueFormatter = LargeValueFormatter()
    rightAxis.setDrawGridLines(false)
    rightAxis.axisMinimum = 0f
    val leftAxis = mChart.axisLeft
    leftAxis.setDrawGridLines(false)
    leftAxis.valueFormatter = LargeValueFormatter()
    leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

    val data = CombinedData()
    val lineData = generateLineData(arrayLists)
    if (lineData != null) {
      data.setData(generateLineData(arrayLists))
    }
    val barData = generateBarData(arrayLists)
    if (barData != null) {
      data.setData(generateBarData(arrayLists))
    }
    binding.includeChart.chart.xAxis.axisMaximum = data.xMax + 0.25f
    mChart.data = data
    mChart.invalidate()
  }

  private fun makeChart(): CombinedChart {
    val chart = binding.includeChart.chart
    chart.apply {
      description.isEnabled = false
      setBackgroundColor(Color.WHITE)
      setDrawGridBackground(false)
      setDrawBarShadow(false)
      isHighlightFullBarEnabled = false
      drawOrder = arrayOf(
        DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
      )
    }
    return chart
  }

  private fun makeLegend(
  ) {
    val legend = binding.includeChart.chart.legend
    legend.apply {
      isWordWrapEnabled = true
      verticalAlignment = LegendVerticalAlignment.BOTTOM
      horizontalAlignment = LegendHorizontalAlignment.CENTER
      orientation = LegendOrientation.HORIZONTAL
      setDrawInside(false)
      textSize = 13f
    }

  }

  private fun generateAxisLabel(
    data: TrendsChartModel?
  ) {
    data?.apply {
      val map: LinkedHashMap<String, Float> = this.monthData
      if (map.size > 0) {
        val monthNames: Set<String> = map.keys
        mMonths = monthNames.toTypedArray()
      }

    }

    binding.includeChart.chart.xAxis.apply {
      position = XAxis.XAxisPosition.BOTH_SIDED
      setDrawGridLines(false)
      axisMinimum = 0f
      granularity = 1f
      setDrawAxisLine(true)
      valueFormatter = IndexAxisValueFormatter(mMonths)
    }
  }

  private fun generateLineData(
    arrayList: List<TrendsChartModel>
  ): LineData? {
    var trendsChartModel: TrendsChartModel? = null
    for (model in arrayList) {
      if (model.chartType == CHART_TYPE_LINE) {
        trendsChartModel = model
      }
    }
    val lineData = LineData()
    trendsChartModel?.apply {
      generateAxisLabel(trendsChartModel)
      val itemcount = this.monthData.size
      val entries = ArrayList<Entry>()
      var lineDataSet: LineDataSet? = null
      var index: Int = 0
      while (index < itemcount) {
        val yValue = this.monthData[mMonths[index]] ?: 0.0f
        entries.add(Entry(index + 0.05f, yValue))
        lineDataSet = LineDataSet(entries, this.legendText)
        lineDataSet.apply {
          color = Color.rgb(64, 0, 255)
          lineWidth = 2.5f
          setCircleColor(Color.rgb(240, 238, 70))
          circleRadius = 5f
          fillColor = Color.rgb(64, 0, 255)
          mode = LineDataSet.Mode.CUBIC_BEZIER
          setDrawValues(true)
          valueTextSize = 10f
          valueTextColor = Color.rgb(64, 0, 255)
          valueFormatter = CRMDecimalValueFormater()
          axisDependency = YAxis.AxisDependency.RIGHT
        }
        if (yValue < 1.00) {
          rightAxis.valueFormatter = CRMIAxisValueFormatter()
          // if last value below 1
          dataFoundFlag = true
        } else {
          if (dataFoundFlag) {
            rightAxis.valueFormatter = CRMIAxisValueFormatter()
            dataFoundFlag = false
          } else {
            rightAxis.valueFormatter = LargeValueFormatter()
          }
        }
        index++
      }
      lineData.addDataSet(lineDataSet)
    }
    return lineData
  }

  private fun getBarColor(index: Int): Int {
    return when (index) {
      0 -> Color.rgb(60, 220, 78)
      1 -> Color.rgb(61, 165, 255)
      else -> Color.rgb(61, 165, 255)
    }
  }

  private fun generateBarData(
    arrayList: List<TrendsChartModel>
  ): BarData? {
    val barDataList: MutableList<TrendsChartModel> = ArrayList()
    for (model in arrayList) {
      if (model.chartType == CHART_TYPE_COLUMN) {
        generateAxisLabel(model)
        barDataList.add(model)
      }
    }
    val barData = BarData()
    if (barDataList.size > 0) {
      val entryList: MutableList<ArrayList<BarEntry>> = ArrayList()
      for (x in barDataList.indices) {
        val entry = ArrayList<BarEntry>()
        entryList.add(entry)
      }
      var barDataSet: BarDataSet? = null
      for (x in barDataList.indices) {
        val model = barDataList[x]
        val entries = entryList[x]
        for (y in mMonths.indices) {
          val yValue = model.monthData[mMonths[y]] ?: 0.0f
          if (barDataList.size == 1) {
            entries.add(BarEntry(y + 0.2f, yValue))
          } else {
            entries.add(BarEntry(y + 0.0f, yValue))
          }
          barDataSet = BarDataSet(entries, model.legendText)
          barDataSet.apply {
            color = getBarColor(x)
            valueTextColor = getBarColor(x)
            valueTextSize = 10f
            axisDependency = YAxis.AxisDependency.LEFT
            valueFormatter = if (yValue % 1 == 0f) {
              CRMValueFormatter()
            } else {
              CRMDecimalValueFormater()
            }
          }

        }
        barData.addDataSet(barDataSet)
      }
      val groupSpace = 0.1f
      val barSpace = 0.025f // x2 dataset
      val barWidth = 0.35f // x2 dataset
      // make this BarData object grouped
      barData.barWidth = barWidth
      if (barDataList.size > 1) {
        barData.groupBars(0f, groupSpace, barSpace) // start at x = 0
      }
    }
    return barData
  }

  companion object {
    const val CHART_TYPE_COLUMN = "Coloumn"
    const val CHART_TYPE_LINE = "SpLine"
  }

}