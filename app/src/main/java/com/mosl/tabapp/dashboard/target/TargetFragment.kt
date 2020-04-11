package com.mosl.tabapp.dashboard.target

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.bank.BankCodeModel
import com.mosl.mobile.domain.bank.BranchCodeModel
import com.mosl.mobile.domain.bank.SelectBank
import com.mosl.mobile.domain.bank.SelectBranch
import com.mosl.mobile.domain.bank.SelectSubBranch
import com.mosl.mobile.domain.bank.SubBranchCodeModel
import com.mosl.mobile.domain.target.Table1ItemModel
import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.mobile.domain.target.TargetItemModel
import com.mosl.mobile.user.login.user
import com.mosl.shared.util.Utility.VORDIPLOM_COLORS
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment.Companion.PERIOD_FILTER
import com.mosl.tabapp.databinding.FragmentTargetBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class TargetFragment : CommonFragment(), TargetView {

  lateinit var binding: FragmentTargetBinding
  private lateinit var bankAdapter: ArrayAdapter<BankCodeModel>
  private lateinit var branchAdapter: ArrayAdapter<BranchCodeModel>
  private lateinit var subBranchAdapter: ArrayAdapter<SubBranchCodeModel>

  @Inject
  lateinit var presenter: TargetPresenter

  @Inject
  lateinit var dashboardModel: DashboardModel
  private var widgetModelList: MutableList<WidgetModel> = mutableListOf()

  private val widgetUiEvent =
    PublishSubject.create<WidgetUiEvent>()

  private val pieChartEvent =
    PublishSubject.create<WidgetUiEvent>()

  private val summaryCountUiEvent =
    PublishSubject.create<WidgetUiEvent>()

  private var previousPosition: Int = 0
  private var itemList = mutableListOf<HorinzatalTargetWidgetItem>()
  private var summaryCountItemList = mutableListOf<TargetSummaryCountItem>()
  private var lableItemList = mutableListOf<TargetLableItem>()

  private var widgetModel: WidgetModel? = null

  private val summaryCountGroupAdapter = GroupAdapter<ViewHolder>()
  private val labelGroupAdapter = GroupAdapter<ViewHolder>()

  override fun getLayout(): Int {
    return R.layout.fragment_target
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding = FragmentTargetBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    if (itemList.size > 0) {
      widgetUiEvent.onNext(
        WidgetUiEvent.ItemClick(
          itemList.get(0).widgetModel,
          0
        )
      )
    }
    makeChart()

  }

  private fun setUpView() {
    val groupAdapter = GroupAdapter<ViewHolder>()
    binding.horizontalRecyclerView.apply {
      layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      setHasFixedSize(true)
      adapter = groupAdapter
    }

    binding.includeTargetChart.rvSummaryCount.apply {
      layoutManager =
        GridLayoutManager(
          requireContext(),
          1,
          GridLayoutManager.VERTICAL,
          false
        )
      adapter = summaryCountGroupAdapter
    }

    binding.includeTargetChart.rvLable.apply {
      layoutManager = GridLayoutManager(
        requireContext(), 3,
        GridLayoutManager.VERTICAL,
        false
      )
      adapter = labelGroupAdapter
    }

    val groupModel = dashboardModel.getGroupModel(
      DashboardSystemModel.getTargetAchivementSystemName()
    )
    val filterList = groupModel.groupFilterList
    lateinit var periodAdapter: ArrayAdapter<FilterRowData>
    if (filterList.size == 1) {
      periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
      binding.spFilterPeriod.adapter = periodAdapter
      periodAdapter.setSpinnerData(binding.spFilterPeriod, filterList[0].filterData)
      arguments?.let { binding.spFilterPeriod.setSelection(it.getInt(PERIOD_FILTER)) }

    }

    widgetModelList = dashboardModel
      .getGroupModel(DashboardSystemModel.getTargetAchivementSystemName())
      .widgetList
    if (widgetModelList.size < 3) {
      widgetModelList.add(WidgetModel())
      widgetModelList.add(WidgetModel())
    } else if (widgetModelList.size < 4) {
      widgetModelList.add(WidgetModel())
    }

    widgetModelList.mapIndexed { position: Int, model: WidgetModel ->

      itemList.add(HorinzatalTargetWidgetItem(model, widgetUiEvent))
    }

    groupAdapter.addAll(itemList)

    bankAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spFilterBank.adapter = bankAdapter

    branchAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spFilterBranch.adapter = branchAdapter

    subBranchAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
    binding.spFilterSubBranch.adapter = subBranchAdapter
    if (userManager.user.isAuBankSalesSupport()) {
      binding.group.visibility = View.VISIBLE
    } else {
      binding.group.visibility = View.GONE
    }

  }

  private fun makeChart() {

    binding.includeTargetChart.chart.apply {
      description.isEnabled = false
      setRotationEnabled(false)
      setEntryLabelTextSize(resources.getDimension(R.dimen.chart_text_size))
      setUsePercentValues(true)
      setExtraOffsets(5f, 10f, 5f, 5f)
      setDragDecelerationFrictionCoef(0.85f)
      getLegend().setEnabled(false)
      setDrawHoleEnabled(true)
      setTransparentCircleColor(Color.WHITE)
      setTransparentCircleAlpha(100)
      setHoleRadius(70f)
      setTransparentCircleRadius(30f)
      setDrawCenterText(true)
      setDrawEntryLabels(false)
      setBackgroundColor(Color.WHITE)
      animateY(1400, Easing.EasingOption.EaseInOutQuad)
      highlightValues(null)
    }

  }

  override fun onPeriodChange(): Observable<Int> {
    return binding.spFilterPeriod.itemSelections()
  }

  override fun onBankChange(): Observable<Int> {
    return binding.spFilterBank.itemSelections()
  }

  override fun getBank(): BankCodeModel? {
    return binding.spFilterBank.selectedItem as? BankCodeModel
  }

  override fun getBranch(): BranchCodeModel? {
    return binding.spFilterBranch.selectedItem as? BranchCodeModel
  }

  override fun getSubBranch(): SubBranchCodeModel? {
    return binding.spFilterSubBranch.selectedItem as? SubBranchCodeModel
  }

  override fun getPeriod(): FilterRowData? {
    return binding.spFilterPeriod.selectedItem as? FilterRowData
  }

  override fun onBranchChange(): Observable<Int> {
    return binding.spFilterBranch.itemSelections()
  }

  override fun onSubBranchChange(): Observable<Int> {
    return binding.spFilterSubBranch.itemSelections()
  }

  override fun onSummaryCountClick(): Observable<WidgetUiEvent.SummaryCountItemClick> {
    return summaryCountUiEvent.ofType()
  }

  override fun onPieChartClick(): Observable<WidgetUiEvent.PieChartClick> {
    return pieChartEvent.ofType()
  }

  override fun getColorsArray(): IntArray {
    return VORDIPLOM_COLORS
  }

  override fun onWidgetItemClick(): Observable<WidgetUiEvent.ItemClick> {
    return widgetUiEvent.ofType()
  }

  override fun getWidgetModel(): WidgetModel? {
    return widgetModel
  }

  override fun renderBankFilter(uiModel: UiModel<List<BankCodeModel>>) {
    toggleProgress(uiModel.inProgress)

    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      chartClear()
      setDefaultValues(1)
    }

    uiModel.onSuccess {
      bankAdapter.setSpinnerData(binding.spFilterBank, it)

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
      branchAdapter.setSpinnerData(binding.spFilterBranch, it)
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
      subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, it)
    }
  }

  fun setDefaultValues(dropdownTypes: Int) {
    val selectBank = mutableListOf<BankCodeModel>()
    selectBank.add(0, SelectBank)
    val selectBranch = mutableListOf<BranchCodeModel>()
    selectBranch.add(0, SelectBranch)
    val selectSubBranch = mutableListOf<SubBranchCodeModel>()
    selectSubBranch.add(0, SelectSubBranch)
    binding.apply {

      when (dropdownTypes) {
        1 -> {
          bankAdapter.setSpinnerData(spFilterBank, selectBank)
          branchAdapter.setSpinnerData(spFilterBranch, selectBranch)
          subBranchAdapter.setSpinnerData(spFilterSubBranch, selectSubBranch)
        }
        2 -> {
          branchAdapter.setSpinnerData(spFilterBranch, selectBranch)
          subBranchAdapter.setSpinnerData(spFilterSubBranch, selectSubBranch)
        }
        3 -> {
          subBranchAdapter.setSpinnerData(spFilterSubBranch, selectSubBranch)
        }
        else -> {
          bankAdapter.setSpinnerData(spFilterBank, selectBank)
          branchAdapter.setSpinnerData(spFilterBranch, selectBranch)
          subBranchAdapter.setSpinnerData(spFilterSubBranch, selectSubBranch)
        }
      }
    }
  }

  override fun getAddtionalParameterMap(): Map<String, String> {
    val addtionalParameterMap: MutableMap<String, String> = HashMap()

    addtionalParameterMap.apply {

      getPeriod()?.let { this[it.keyValue] = it.value }

      getBank()?.let { this[LeadsFragment.FLT_BANKNAME] = it.bankId.toString() }

      getBranch()?.let { this[LeadsFragment.FLT_BANKBRANCHCODE] = it.branchId.toString() }

      getSubBranch()?.let {
        this[LeadsFragment.FLT_BANKSUBBROKERCODE] = it.bankBranchId.toString()
      }

      this[MOSLConstants.SELF_KEY] = dashboardModel.dashboardFor
    }

    return addtionalParameterMap
  }

  fun chartClear() {
    binding.includeTargetChart.chart.clear()
    binding.includeTargetChart.tvWidgetName.visibility = View.GONE
  }

  override fun renderData(uiModel: UiModel<TargetItemModel>) {
    toggleProgressBar(uiModel.inProgress)
    summaryCountItemList.clear()
    summaryCountGroupAdapter.clear()
    labelGroupAdapter.clear()
    lableItemList.clear()
    uiModel.onFailure { error ->
      showOnFailurePopup(error)
      chartClear()
    }

    uiModel.onSuccess {
      it.table?.apply {
        SummaryCountView(this)
      }
      it.table1?.apply {
        if (this.isEmpty()) {
          binding.includeTargetChart.chart.clear()
        } else {
          setDataBroking(this)
          LableView(this)
        }

      }

    }

  }

  private fun setDataBroking(
    tableItemList: List<Table1ItemModel>
  ) {

    val data = getPieData(tableItemList)
    data.setDrawValues(false) // for text hidden
    data.setValueTextSize(11f)
    data.setValueTextColor(Color.WHITE)
    data.setValueTypeface(Typeface.DEFAULT)
    binding.includeTargetChart.chart.setData(data)
    binding.includeTargetChart.chart.setOnChartValueSelectedListener(object :
      OnChartValueSelectedListener {
      override fun onValueSelected(
        e: Entry,
        h: Highlight
      ) {

        pieChartEvent.onNext(WidgetUiEvent.PieChartClick(tableItemList[h.x.toInt()].label))

      }

      override fun onNothingSelected() {}
    })
    binding.includeTargetChart.chart.invalidate()
  }

  private fun getPieData(
    tableItemList: List<Table1ItemModel>

  ): PieData {
    val entries = ArrayList<PieEntry>()
    for (i in tableItemList.indices) {
      val value = tableItemList[i].value?.toFloat() ?: 0.0f
      entries.add(
        PieEntry(
          value,
          tableItemList[i].label, i
        )
      )
    }
    val dataSet = PieDataSet(entries, "Total Client")
    dataSet.sliceSpace = 5f
    dataSet.selectionShift = 5f
    val colors = ArrayList<Int>()
    for (c in VORDIPLOM_COLORS) colors.add(c)
    colors.add(ColorTemplate.getHoloBlue())
    dataSet.colors = colors
    return PieData(dataSet)
  }

  fun SummaryCountView(tableList: List<TableItemModel>) {

    (binding.includeTargetChart.rvSummaryCount.getLayoutManager() as GridLayoutManager).spanCount =
      gridSpanCount(tableList.size)

    val sortedList = tableList.sortedWith(compareBy { it.ord })
    sortedList.mapIndexed { index, tableItemModel ->
      summaryCountItemList.add(
        TargetSummaryCountItem(
          tableItemModel, summaryCountUiEvent, requireContext(),
          this@TargetFragment
        )
      )
    }
    summaryCountGroupAdapter.addAll(summaryCountItemList)

  }

  fun LableView(tableList: List<Table1ItemModel>) {
    (binding.includeTargetChart.rvLable.getLayoutManager() as GridLayoutManager).spanCount =
      gridSpanCount(tableList.size)

    tableList.mapIndexed { index, tableItemModel ->
      lableItemList.add(
        TargetLableItem(tableItemModel, requireContext(), this)
      )
    }
    labelGroupAdapter.addAll(lableItemList)

  }

  override fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int) {
    binding.includeTargetChart.tvWidgetName.setText("")
    if (currentPosition != previousPosition) {
      itemList.get(currentPosition).updateItem(true)
      itemList.get(previousPosition).updateItem(false)
      previousPosition = currentPosition
    }
    if (widgetModel.widgetTypeList.size > 0) {
      binding.includeTargetChart.tvWidgetName.setText(widgetModel.widgetTypeList.get(0).getTitle())
    }
    this.widgetModel = widgetModel
  }

  fun gridSpanCount(listCount: Int): Int {
    return when {
      listCount >= 3 -> 3
      listCount == 2 -> 2

      else -> 1
    }

  }

}