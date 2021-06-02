package com.mosl.tabapp.dashboard.leads

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.bank.*
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.model.dashboard.lead.LeadModel
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMValueFormatter
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.leads.report.LeadsReportFragment
import com.mosl.tabapp.databinding.FragmentLeadsBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.Serializable
import java.util.*
import javax.inject.Inject
import io.reactivex.rxjava3.core.Observable

class LeadsFragment : CommonFragment(), LeadsView, View.OnClickListener {

  lateinit var binding: FragmentLeadsBinding
  private lateinit var bankAdapter: ArrayAdapter<BankCodeModel>
  private lateinit var branchAdapter: ArrayAdapter<BranchCodeModel>
  private lateinit var subBranchAdapter: ArrayAdapter<SubBranchCodeModel>

  @Inject
  lateinit var presenter: LeadsPresenter

  @Inject
  lateinit var dashboardModel: DashboardModel

  private var model: WidgetModel = WidgetModel()

  private val barChartEvent =
    PublishSubject.create<LeadUiEvent>()

  override fun getLayout(): Int {
    return R.layout.fragment_leads
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentLeadsBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    binding.includeChart.tvShowMore.setOnClickListener(this)
    makeChart()
    makeLegend()

  }

  private fun setUpView() {
    val context = requireContext()

    val groupModel = dashboardModel.getGroupModel(
      DashboardSystemModel.getLeadSystemName()
    )
    val widgetList = groupModel.widgetList
    if (widgetList.size == 1) {
      model = widgetList[0]
    } else if (widgetList.size >= 2) {
      model = widgetList[1]
    }
    val filterList = model.widgetFilterList
    lateinit var periodAdapter: ArrayAdapter<FilterRowData>
    lateinit var productAdapter: ArrayAdapter<FilterRowData>
    lateinit var sourceAdapter: ArrayAdapter<FilterRowData>
    if (filterList.size == 3) {
      periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      binding.spPeriod.adapter = periodAdapter
      periodAdapter.setSpinnerData(binding.spPeriod, filterList[2].filterData)
      arguments?.let { binding.spPeriod.setSelection(it.getInt(PERIOD_FILTER)) }

      productAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      binding.spProduct.adapter = productAdapter
      productAdapter.setSpinnerData(binding.spProduct, filterList[1].filterData)
      binding.spProduct.visibility = View.VISIBLE
      arguments?.let { binding.spProduct.setSelection(it.getInt(PRODUCT_FILTER)) }

      sourceAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      binding.spSource.adapter = sourceAdapter
      sourceAdapter.setSpinnerData(binding.spSource, filterList[0].filterData)
      binding.spSource.visibility = View.VISIBLE
      arguments?.let { binding.spSource.setSelection(it.getInt(SOURCE_FILTER)) }

    }
    if (filterList.size == 2) {
      periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      binding.spPeriod.adapter = periodAdapter
      periodAdapter.setSpinnerData(binding.spPeriod, filterList[0].filterData)
      arguments?.let { binding.spPeriod.setSelection(it.getInt(PERIOD_FILTER)) }

      productAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      binding.spProduct.adapter = productAdapter
      productAdapter.setSpinnerData(binding.spProduct, filterList[1].filterData)
      binding.spProduct.visibility = View.VISIBLE
      arguments?.let { binding.spProduct.setSelection(it.getInt(PRODUCT_FILTER)) }

    } else if (filterList.size == 1) {
      periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      binding.spPeriod.adapter = periodAdapter
      periodAdapter.setSpinnerData(binding.spPeriod, filterList[0].filterData)
      arguments?.let { binding.spPeriod.setSelection(it.getInt(PERIOD_FILTER)) }

    }
    bankAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
    binding.spBank.adapter = bankAdapter

    branchAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
    binding.spBranch.adapter = branchAdapter

    subBranchAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
    binding.spSubBranch.adapter = subBranchAdapter

    if (userManager.user.isAuBankSalesSupport()) {
      binding.bankGroup.visibility = View.VISIBLE
    } else {
      binding.bankGroup.visibility = View.GONE
    }
  }

  override fun onPeriodChange(): Observable<Int> {
    return binding.spPeriod.itemSelections()
  }

  override fun onSourceChange(): Observable<Int> {
    return binding.spSource.itemSelections()
  }

  override fun onProductChange(): Observable<Int> {
    return binding.spProduct.itemSelections()
  }

  override fun onBankChange(): Observable<Int> {
    return binding.spBank.itemSelections()
  }

  override fun getBank(): BankCodeModel? {
    return binding.spBank.selectedItem as? BankCodeModel
  }

  override fun getBranch(): BranchCodeModel? {
    return binding.spBranch.selectedItem as? BranchCodeModel
  }

  override fun getSubBranch(): SubBranchCodeModel? {
    return binding.spSubBranch.selectedItem as? SubBranchCodeModel
  }

  override fun getPeriod(): FilterRowData? {
    return binding.spPeriod.selectedItem as? FilterRowData
  }

  override fun getSource(): FilterRowData? {
    return binding.spSource.selectedItem as? FilterRowData
  }

  override fun getProduct(): FilterRowData? {
    return binding.spProduct.selectedItem as? FilterRowData
  }

  override fun getWidgetSystemsName(): String {
    return model.systemName
  }

  override fun onBranchChange(): Observable<Int> {
    return binding.spBranch.itemSelections()
  }

  override fun onSubBranchChange(): Observable<Int> {
    return binding.spSubBranch.itemSelections()
  }

  override fun onBarChartClick(): Observable<LeadUiEvent.BarChartClick> {

    return barChartEvent.ofType()
  }

  override fun renderBankFilter(uiModel: UiModel<List<BankCodeModel>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      chartClear()
      setDefaultValues(1)
    }

    uiModel.onSuccess {
      bankAdapter.setSpinnerData(binding.spBranch, it)

    }
  }

  override fun renderBranchFilter(uiModel: UiModel<List<BranchCodeModel>>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      chartClear()
      setDefaultValues(2)
    }

    uiModel.onSuccess {
      branchAdapter.setSpinnerData(binding.spBranch, it)
    }
  }

  override fun renderSubBranchFilter(uiModel: UiModel<List<SubBranchCodeModel>>) {
    toggleProgress(uiModel.inProgress)

    val selectSubBranch = mutableListOf<SubBranchCodeModel>()
    selectSubBranch.add(0, SelectSubBranch)

    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      chartClear()
      setDefaultValues(3)
    }

    uiModel.onSuccess {
      subBranchAdapter.setSpinnerData(binding.spSubBranch, it)
    }
  }

  fun setDefaultValues(dropdownTypes: Int) {
    val selectBank = mutableListOf<BankCodeModel>()
    selectBank.add(0, SelectBank)
    val selectBranch = mutableListOf<BranchCodeModel>()
    selectBranch.add(0, SelectBranch)
    val selectSubBranch = mutableListOf<SubBranchCodeModel>()
    selectSubBranch.add(0, SelectSubBranch)
    when (dropdownTypes) {
      1 -> {
        bankAdapter.setSpinnerData(binding.spBank, selectBank)
        branchAdapter.setSpinnerData(binding.spBranch, selectBranch)
        subBranchAdapter.setSpinnerData(binding.spSubBranch, selectSubBranch)
      }
      2 -> {
        branchAdapter.setSpinnerData(binding.spBranch, selectBranch)
        subBranchAdapter.setSpinnerData(binding.spSubBranch, selectSubBranch)
      }
      3 -> {
        subBranchAdapter.setSpinnerData(binding.spSubBranch, selectSubBranch)
      }
      else -> {
        bankAdapter.setSpinnerData(binding.spBank, selectBank)
        branchAdapter.setSpinnerData(binding.spBranch, selectBranch)
        subBranchAdapter.setSpinnerData(binding.spSubBranch, selectSubBranch)
      }
    }
  }

  fun chartClear() {
    binding.includeChart.chart.clear()
  }

  override fun renderData(uiModel: UiModel<LeadModel>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      chartClear()
    }

    uiModel.onSuccess {
      binding.includeChart.tvTotal.text = it.getTotal()
      stackedBarData(it.getXAxisList(), it.getYAxisList(), it.getLabelString())

    }
  }

  override fun getAddtionalParameterMap(): Map<String, String> {
    val addtionalParameterMap: MutableMap<String, String> = HashMap()

    addtionalParameterMap.apply {

      getPeriod()?.let {
        this[it.keyValue] = it.value
      }
      getProduct()?.let {
        this[it.keyValue] = it.value
      }

      getSource()?.let {
        this[it.keyValue] = it.value
      }
      getBank()?.let {
        this[FLT_BANKNAME] = it.bankName
      }

      getBranch()?.let {
        this[FLT_BANKBRANCHCODE] = it.branchName
      }

      getSubBranch()?.let {
        this[FLT_BANKSUBBROKERCODE] = it.subBrokerName
      }

      this[SELF_KEY] = dashboardModel.dashboardFor
    }

    return addtionalParameterMap
  }

  private fun stackedBarData(
    xAxisValue: MutableList<String>,
    yAxisValue: MutableList<MutableList<String>>,
    lableString: Array<String>
  ) {
    val mChart = binding.includeChart.chart
    xAxisValue.reverse()
    yAxisValue[0].reverse()
    yAxisValue[1].reverse()

    val leftAxis = mChart.axisLeft
    leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

    val xAxis: XAxis = mChart.xAxis
    xAxis.setDrawGridLines(false)
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setCenterAxisLabels(true)
    xAxis.setDrawAxisLine(false)
    xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.black)
    xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValue)

    val data = getBarData(yAxisValue, lableString)
    data.setValueFormatter(CRMValueFormatter())
    data.setValueTextColor(Color.WHITE)
    data.setValueTextSize(13f)
    data.barWidth = 0.6f

    mChart.data = data
    mChart.invalidate()
    mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
      override fun onValueSelected(
        e: Entry,
        h: Highlight
      ) {
        if (e.y == 0.0f) return

        if (yAxisValue[h.stackIndex][e.x.toInt()] != "0") {
          barChartEvent.onNext(
            LeadUiEvent.BarChartClick(
              lableString[h.stackIndex],
              xAxisValue[e.x.toInt()]
            )
          )
        }

      }

      override fun onNothingSelected() {
      }

    })
  }

  private fun getBarData(
    yAxisValue: MutableList<MutableList<String>>,
    lableString: Array<String>
  ): BarData {
    val entries = ArrayList<BarEntry>()
    for (i in yAxisValue[0].indices) {
      try {
        val val1 = yAxisValue[0][i].toFloat()
        val val2 = yAxisValue[1][i].toFloat()
        entries.add(BarEntry(i + 0.5f, floatArrayOf(val1, val2)))
      } catch (e: NumberFormatException) {
        e.printStackTrace()
      }
    }

    val colors = ArrayList<Int>()
    val vordiplomColors = intArrayOf(
      Color.rgb(38, 127, 183), Color.rgb(255, 154, 0)
    )
    for (c in vordiplomColors) colors.add(c)
    val barDataSet = BarDataSet(entries, "")
    barDataSet.colors = colors
    barDataSet.stackLabels = lableString

    val dataSets = ArrayList<IBarDataSet>()
    dataSets.add(barDataSet)
    return BarData(dataSets)
  }

  private fun makeChart() {
    binding.includeChart.chart.apply {
      description.isEnabled = false
      setBackgroundColor(Color.WHITE)
      setDrawGridBackground(false)
      setDrawBarShadow(false)
      isHighlightFullBarEnabled = false
      setPinchZoom(false)
      isScaleXEnabled = true
      isScaleYEnabled = false
      setDrawValueAboveBar(false)
      axisRight.isEnabled = false
      axisLeft.isEnabled = false
    }
  }

  private fun makeLegend(
  ) {
    val legend = binding.includeChart.chart.legend
    legend.form = Legend.LegendForm.CIRCLE
    legend.textSize = resources.getDimension(R.dimen.chart_text_size)
    legend.isWordWrapEnabled = true
    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
    legend.orientation = Legend.LegendOrientation.HORIZONTAL
    legend.setDrawInside(false)
    legend.textColor = ContextCompat.getColor(requireContext(), R.color.black)

  }

  override fun onClick(view: View) {
    when (view.id) {
      R.id.tvShowMore -> {
        val args = Bundle()
        val clientsFragment =
          LeadsReportFragment()
        args.putSerializable(DASHBOARD_MODEL, dashboardModel)
        args.putSerializable(
          MAP,
          getAddtionalParameterMap() as Serializable
        )
        args.putInt(PERIOD_FILTER, binding.spPeriod.selectedItemPosition)
        args.putInt(PRODUCT_FILTER, binding.spProduct.selectedItemPosition)
        args.putInt(SOURCE_FILTER, binding.spSource.selectedItemPosition)
        args.putInt(FLT_BANKNAME, binding.spBank.selectedItemPosition)
        args.putInt(FLT_BANKBRANCHCODE, binding.spBranch.selectedItemPosition)
        args.putInt(FLT_BANKSUBBROKERCODE, binding.spSubBranch.selectedItemPosition)
        args.putString(SYSTEM_NAME, getWidgetSystemsName())
        clientsFragment.arguments = args
        requireActivity().supportFragmentManager
          .beginTransaction()
          .replace(R.id.llReplaceClientFragment, clientsFragment)
          .commit()
      }
    }
  }

  companion object {
    const val FLT_BANKNAME = "flt_bankname"
    const val FLT_BANKBRANCHCODE = "flt_bankbranchcode"
    const val FLT_BANKSUBBROKERCODE = "flt_banksubbrokercode"
    const val PERIOD_FILTER = "period filter"
    const val PRODUCT_FILTER = "product filter"
    const val SOURCE_FILTER = "source filter"
  }

}