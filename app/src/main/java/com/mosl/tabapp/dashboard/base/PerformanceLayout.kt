package com.mosl.tabapp.dashboard.base

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT
import com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.mosl.mobile.domain.dashboard.IncentiveTableData1
import com.mosl.shared.util.Utility.stringOrEmpty
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMDecimalValueFormater
import com.mosl.tabapp.databinding.FragPerformanceTeamTabRowBinding
import com.mosl.tabapp.databinding.ViewPerformanceBinding
import java.util.ArrayList
import java.util.LinkedHashMap

class PerformanceLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  private val yDataGraphArrAdv =
    ArrayList<String>()
  private var mMonths: Array<String> = emptyArray()

  internal var binding: ViewPerformanceBinding = DataBindingUtil.inflate(
    LayoutInflater.from(context), R.layout.view_performance, this, true
  )

  override fun onFinishInflate() {
    super.onFinishInflate()
  }

  fun loadChart(
    table1Items: List<IncentiveTableData1>
  ) {
    setLegend()
    var mYtd = ""
    var groupSpace = 0.25f
    var barSpace = 0.05f
    var barWidth = 0.35f
    val dataList: MutableList<ChartDataModel> =
      ArrayList()
    yDataGraphArrAdv.clear()
    for (i in table1Items.indices) {
      val model =
        ChartDataModel()
      val (q1, q2, q3, q4, name, widgetType, ytd) = table1Items[i]
      val mName: String = stringOrEmpty(name)
      val mQ1: String = stringOrZero(q1)
      val mQ2: String = stringOrZero(q2)
      val mQ3: String = stringOrZero(q3)
      val mQ4: String = stringOrZero(q4)
      val mWidgetType: String = stringOrEmpty(widgetType)
      if (ytd != null) {
        mYtd = stringOrZero(ytd)
        yDataGraphArrAdv.add(mYtd)
      }
      yDataGraphArrAdv.add(mName)
      yDataGraphArrAdv.add(mQ1)
      yDataGraphArrAdv.add(mQ2)
      yDataGraphArrAdv.add(mQ3)
      yDataGraphArrAdv.add(mQ4)
      yDataGraphArrAdv.add(mWidgetType)
      model.monthData["Q1"] = mQ1.toFloat()
      model.monthData["Q2"] = mQ2.toFloat()
      model.monthData["Q3"] = mQ3.toFloat()
      model.monthData["Q4"] = mQ4.toFloat()
      if (ytd != null) {
        model.monthData["YTD"] = mYtd.toFloat()
      }
      dataList.add(model)
    }
    if (generateBarData(dataList) != null) {
      binding.chart.data = generateBarData(dataList)
    }
    val groupCount: Int = mMonths.size
    val startYear = 2010
    binding.tablePerformance.removeAllViews()
    val inflater: LayoutInflater = LayoutInflater.from(context)
    val headerBinding =
      FragPerformanceTeamTabRowBinding.inflate(inflater)

    headerBinding.tvIncentives.text = yDataGraphArrAdv[0]
    headerBinding.tvIncentives.setTypeface(null, Typeface.BOLD)
    headerBinding.tvIncentives.setTextColor(Color.rgb(60, 220, 78))
    headerBinding.tvTeamAvg.text = yDataGraphArrAdv[6]
    headerBinding.tvTeamAvg.setTypeface(null, Typeface.BOLD)
    headerBinding.tvTeamAvg.setTextColor(Color.rgb(61, 165, 255))
    if (dataList.size == 3) {
      headerBinding.tvNationalAvg.visibility = View.VISIBLE
      headerBinding.tvNationalAvg.text = yDataGraphArrAdv[12]
      headerBinding.tvNationalAvg.setTypeface(null, Typeface.BOLD)
      headerBinding.tvNationalAvg.setTextColor(Color.rgb(38, 127, 183))
    }
    binding.tablePerformance.addView(headerBinding.root)
    for (i in 1 until mMonths.size + 1) {
      val childBinding =
        FragPerformanceTeamTabRowBinding.inflate(inflater)
      childBinding.tvIncentives.text = yDataGraphArrAdv[i]
      childBinding.tvTeamAvg.text = yDataGraphArrAdv[6 + i]
      if (dataList.size == 3) {
        childBinding.tvNationalAvg.visibility = View.VISIBLE
        childBinding.tvNationalAvg.text = yDataGraphArrAdv[12 + i]
        groupSpace = 0.18f
        barSpace = 0.03f
        barWidth = 0.25f
      }
      binding.tablePerformance.addView(childBinding.root)
    }
    binding.chart.barData.barWidth = barWidth
    binding.chart.xAxis.axisMinimum = startYear.toFloat()
    binding.chart.xAxis.axisMaximum = (startYear
        + binding.chart.barData.getGroupWidth(groupSpace, barSpace) * groupCount)
    binding.chart.groupBars(startYear.toFloat(), groupSpace, barSpace)
    binding.chart.invalidate()
    binding.chart.visibility = View.VISIBLE
    binding.tablePerformance.visibility = View.VISIBLE
    binding.tvPerformance.text = resources.getString(string.txt_label_performance)
  }

  private fun setLegend() {
    binding.chart.apply {
      description.isEnabled = false
      setPinchZoom(false)
      setDrawBarShadow(false)
      setDrawGridBackground(false)

      val legendAdv: Legend = legend
      legendAdv.verticalAlignment = TOP
      legendAdv.horizontalAlignment = RIGHT
      legendAdv.orientation = VERTICAL
      legendAdv.setDrawInside(true)
      legendAdv.yOffset = 0f
      legendAdv.xOffset = 10f
      legendAdv.yEntrySpace = 0f
      legendAdv.textSize = 8f
      legendAdv.isEnabled = false

      val xAxis: XAxis = xAxis
      xAxis.position = BOTTOM
      xAxis.setDrawGridLines(false)
      xAxis.setCenterAxisLabels(true)
      xAxis.granularity = 1f

      val leftAxis: YAxis = axisLeft
      leftAxis.setDrawGridLines(false)
      leftAxis.axisMinimum = 0f
      leftAxis.spaceTop = 5f

      axisRight.isEnabled = false
      isScaleXEnabled = false
      isScaleYEnabled = false

      xAxis.valueFormatter = object : IAxisValueFormatter {
        private val mActivities =
          arrayOf("Q1", "Q2", "Q3", "Q4", "YTD")

        override fun getFormattedValue(value: Float, axis: AxisBase): String {
          return mActivities[value.toInt() % mActivities.size]
        }
      }
    }
  }

  private fun generateBarData(barDataList: List<ChartDataModel>): BarData? {
    generateAxisLabel(barDataList[0])
    if (barDataList.isEmpty()) return null
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
          entries.add(BarEntry(y + 0.2f, model.monthData[mMonths[y]] ?: 0.0f))
        } else {
          entries.add(BarEntry(0.0f, model.monthData[mMonths[y]] ?: 0.0f))
        }
        set1 = BarDataSet(entries, "")
        set1.color = getColor(x)
        set1.valueTextColor = getColor(x)
        set1.valueTextSize = 10f
        set1.axisDependency = LEFT
        set1.valueFormatter = CRMDecimalValueFormater()
      }
      d.addDataSet(set1)
    }

    val groupSpace = 0.06f
    val barSpace = 0.02f // x2 dataset
    val barWidth = 0.37f // x2 dataset
    // make this BarData object grouped
    d.barWidth = barWidth
    if (barDataList.size > 1) {
      d.groupBars(0f, groupSpace, barSpace) // start at x = 0
    }
    return d
  }

  private fun generateAxisLabel(data: ChartDataModel) {
    val map: LinkedHashMap<String, Float> = data.monthData
    if (map.size > 0) {
      val monthNames: Set<String> = map.keys
      mMonths = monthNames.toTypedArray()
    }
  }

  private fun getColor(index: Int): Int {
    return when (index) {
      0 -> Color.rgb(60, 220, 78)
      1 -> Color.rgb(61, 165, 255)
      else -> Color.rgb(38, 127, 183)
    }
  }

  private class ChartDataModel {
    val monthData =
      LinkedHashMap<String, Float>()

  }

  fun stringOrZero(input: String?): String {
    return if (input != null && input != "") {
      input
    } else {
      "0"
    }
  }
}

