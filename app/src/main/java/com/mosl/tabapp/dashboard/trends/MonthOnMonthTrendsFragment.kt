package com.mosl.tabapp.dashboard.trends

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment
import com.github.mikephil.charting.components.Legend.LegendOrientation
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
    rightAxis.setValueFormatter(LargeValueFormatter())
    rightAxis.setDrawGridLines(false)
    rightAxis.setAxisMinimum(0f)
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
      setValueFormatter(IndexAxisValueFormatter(mMonths))
    }
  }

  private fun generateLineData(
    arrayList: List<TrendsChartModel>
  ): LineData? {
    var lineData: TrendsChartModel? = null
    for (model in arrayList) {
      val CHART_TYPE_LINE = "SpLine"
      if (model.chartType == CHART_TYPE_LINE) {
        lineData = model
      }
    }
    if (lineData == null) return null
    generateAxisLabel(lineData)
    val itemcount = lineData.monthData.size
    val d = LineData()
    val entries =
      ArrayList<Entry>()
    var index: Int
    var set: LineDataSet? = null
    index = 0
    while (index < itemcount) {
      entries.add(
        Entry(
          index + 0.05f,
          lineData.monthData[mMonths.get(index)]!!
        )
      )
      set = LineDataSet(entries, lineData.legendText)
      set.color = Color.rgb(64, 0, 255)
      set.lineWidth = 2.5f
      set.setCircleColor(Color.rgb(240, 238, 70))
      set.circleRadius = 5f
      set.fillColor = Color.rgb(64, 0, 255)
      set.mode = LineDataSet.Mode.CUBIC_BEZIER
      set.setDrawValues(true)
      set.valueTextSize = 10f
      set.valueTextColor = Color.rgb(64, 0, 255)
      if (lineData.monthData[mMonths.get(index)]!! < 1.00) {
        rightAxis.setValueFormatter(CRMIAxisValueFormatter())
        // if last value below 1
        dataFoundFlag = true
      } else {
        if (dataFoundFlag) {
          rightAxis.setValueFormatter(CRMIAxisValueFormatter())
          dataFoundFlag = false
        } else {
          rightAxis.setValueFormatter(LargeValueFormatter())
        }
      }
      set.valueFormatter = CRMDecimalValueFormater()
      set.axisDependency = YAxis.AxisDependency.RIGHT
      index++
    }
    d.addDataSet(set)
    return d
  }

  private fun getColor(index: Int): Int {
    return when (index) {
      0 -> Color.rgb(60, 220, 78)
      1 -> Color.rgb(61, 165, 255)
      else -> Color.rgb(61, 165, 255)
    }
  }

  private fun generateBarData(
    arrayList: List<TrendsChartModel>
  ): BarData? {
    val barDataList: MutableList<TrendsChartModel> =
      ArrayList()
    for (model in arrayList) {
      val CHART_TYPE_COLUMN = "Coloumn"
      if (model.chartType == CHART_TYPE_COLUMN) {
        generateAxisLabel(model)
        barDataList.add(model)
      }
    }
    if (barDataList.size == 0) return null
    val d = BarData()
    val entryList: MutableList<ArrayList<BarEntry>> =
      ArrayList()
    for (x in barDataList.indices) {
      val entry = ArrayList<BarEntry>()
      entryList.add(entry)
    }
    var set1: BarDataSet? = null
    for (x in barDataList.indices) {
      val model = barDataList[x]
      val entries = entryList[x]
      for (y in mMonths.indices) {
        if (barDataList.size == 1) {
          entries.add(BarEntry(y + 0.2f, model.monthData[mMonths.get(y)]!!))
        } else {
          entries.add(BarEntry(y + 0.0f, model.monthData[mMonths.get(y)]!!))
        }
        set1 = BarDataSet(entries, model.legendText)
        set1.color = getColor(x)
        set1.valueTextColor = getColor(x)
        set1.valueTextSize = 10f
        set1.axisDependency = YAxis.AxisDependency.LEFT
        if (model.monthData[mMonths.get(y)]!! % 1 == 0f) {
          set1.valueFormatter = CRMValueFormatter()
        } else {
          set1.valueFormatter = CRMDecimalValueFormater()
        }
      }
      d.addDataSet(set1)
    }
    val groupSpace = 0.1f
    val barSpace = 0.025f // x2 dataset
    val barWidth = 0.35f // x2 dataset
    // make this BarData object grouped
    d.barWidth = barWidth
    if (barDataList.size > 1) {
      d.groupBars(0f, groupSpace, barSpace) // start at x = 0
    }
    return d
  }

}