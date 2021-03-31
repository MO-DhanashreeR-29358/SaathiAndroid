package com.mosl.tabapp.dashboard.teamtarget

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
import com.mosl.mobile.domain.bank.*
import com.mosl.mobile.domain.teamtarget.TeamTableItem3Model
import com.mosl.mobile.domain.teamtarget.TeamTableItemModel
import com.mosl.mobile.domain.teamtarget.TeamTargetItemModel
import com.mosl.mobile.user.login.user
import com.mosl.shared.util.Utility.VORDIPLOM_COLORS
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment.Companion.PERIOD_FILTER
import com.mosl.tabapp.databinding.FragmentTeamTargetBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class TeamTargetFragment : CommonFragment(), TeamTargetView, View.OnClickListener {

  lateinit var binding: FragmentTeamTargetBinding
  private lateinit var bankAdapter: ArrayAdapter<BankCodeModel>
  private lateinit var branchAdapter: ArrayAdapter<BranchCodeModel>
  private lateinit var subBranchAdapter: ArrayAdapter<SubBranchCodeModel>

  @Inject
  lateinit var presenter: TeamTargetPresenter

  @Inject
  lateinit var dashboardModel: DashboardModel
  private var widgetModelList: MutableList<WidgetModel> = mutableListOf()

  private val widgetUiEvent =
    PublishSubject.create<TeamWidgetUiEvent>()

  private val pieChartEvent =
    PublishSubject.create<TeamWidgetUiEvent>()

  private val summaryCountUiEvent =
    PublishSubject.create<TeamWidgetUiEvent>()

  private var previousPosition: Int = 0
  private var itemList = mutableListOf<HorinzatalTeamTargetWidgetItem>()
  private var summaryCountItemList = mutableListOf<TeamTargetSummaryCountItem>()
  private var lableItemList = mutableListOf<TeamTargetLableItem>()

  private var widgetModel: WidgetModel? = null

  private val summaryCountGroupAdapter = GroupAdapter<ViewHolder>()
  private val labelGroupAdapter = GroupAdapter<ViewHolder>()

  private var tableListdata: List<List<TeamTableItemModel>> = mutableListOf()

  private var count = 0

  override fun getLayout(): Int {
    return R.layout.fragment_team_target
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding = FragmentTeamTargetBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    if (itemList.size > 0) {
      widgetUiEvent.onNext(
        TeamWidgetUiEvent.ItemClick(
          itemList[0].widgetModel,
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

    widgetModelList.mapIndexed { _: Int, model: WidgetModel ->

      itemList.add(HorinzatalTeamTargetWidgetItem(model, widgetUiEvent))
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

    binding.includeTargetChart.ivLeftArrow.setOnClickListener(this)
    binding.includeTargetChart.ivRightArrow.setOnClickListener(this)

  }

  private fun makeChart() {

    binding.includeTargetChart.chart.apply {
      description.isEnabled = false
      isRotationEnabled = false
      setEntryLabelTextSize(resources.getDimension(R.dimen.chart_text_size))
      setUsePercentValues(true)
      setExtraOffsets(5f, 10f, 5f, 5f)
      dragDecelerationFrictionCoef = 0.85f
      legend.isEnabled = false
      isDrawHoleEnabled = true
      setTransparentCircleColor(Color.WHITE)
      setTransparentCircleAlpha(100)
      holeRadius = 70f
      transparentCircleRadius = 30f
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

  override fun onSummaryCountClick(): Observable<TeamWidgetUiEvent.SummaryCountItemClick> {
    return summaryCountUiEvent.ofType()
  }

  override fun onPieChartClick(): Observable<TeamWidgetUiEvent.PieChartClick> {
    return pieChartEvent.ofType()
  }

  override fun getColorsArray(): IntArray {
    return VORDIPLOM_COLORS
  }

  override fun onWidgetItemClick(): Observable<TeamWidgetUiEvent.ItemClick> {
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

  private fun setDefaultValues(dropdownTypes: Int) {
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

  private fun chartClear() {
    binding.includeTargetChart.chart.clear()
    lableItemList.clear()
    labelGroupAdapter.clear()
    binding.includeTargetChart.tvWidgetName.text = ""
  }

  override fun renderData(uiModel: UiModel<TeamTargetItemModel>) {
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
      tableListdata = it.tableListData()
      it.table3.apply {
        summaryCountView(this)
      }
      it.table.apply {
        if (this.isEmpty()) {
          binding.includeTargetChart.chart.clear()
          chartClear()
        } else {
          this.renderUI()
        }

      }

    }

  }

  private fun List<TeamTableItemModel>.renderUI() {
    setDataBroking(this)
    lableView(this)
    setTitle()
  }

  private fun setDataBroking(
    tableItemList: List<TeamTableItemModel>
  ) {

    val data = getPieData(tableItemList)
    data.setDrawValues(false) // for text hidden
    data.setValueTextSize(11f)
    data.setValueTextColor(Color.WHITE)
    data.setValueTypeface(Typeface.DEFAULT)
    binding.includeTargetChart.chart.data = data
    binding.includeTargetChart.chart.setOnChartValueSelectedListener(object :
      OnChartValueSelectedListener {
      override fun onValueSelected(
        e: Entry,
        h: Highlight
      ) {

        pieChartEvent.onNext(
          TeamWidgetUiEvent.PieChartClick(
            tableItemList[h.x.toInt()].label,
            tableItemList[h.x.toInt()].widgetType
          )
        )

      }

      override fun onNothingSelected() {}
    })
    binding.includeTargetChart.chart.invalidate()
  }

  private fun getPieData(
    tableItemList: List<TeamTableItemModel>

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

  private fun summaryCountView(tableList: List<TeamTableItem3Model>) {

    (binding.includeTargetChart.rvSummaryCount.layoutManager as GridLayoutManager).spanCount =
      gridSpanCount(tableList.size)

    val sortedList = tableList.sortedWith(compareBy { it.ord })
    sortedList.mapIndexed { _, tableItemModel ->
      summaryCountItemList.add(
        TeamTargetSummaryCountItem(
          tableItemModel, summaryCountUiEvent, requireContext(),
          this@TeamTargetFragment
        )
      )
    }
    summaryCountGroupAdapter.addAll(summaryCountItemList)

  }

  fun setTitle() {
    getWidgetModel()?.apply {
      if (this.widgetTypeList.size > 0) {
        binding.includeTargetChart.tvWidgetName.text = this.widgetTypeList[count].title
      }
    }
  }

  private fun lableView(tableList: List<TeamTableItemModel>) {
    labelGroupAdapter.clear()
    lableItemList.clear()
    (binding.includeTargetChart.rvLable.layoutManager as GridLayoutManager).spanCount =
      gridSpanCount(tableList.size)

    tableList.mapIndexed { _, tableItemModel ->
      lableItemList.add(
        TeamTargetLableItem(tableItemModel, requireContext(), this)
      )
    }
    labelGroupAdapter.addAll(lableItemList)

  }

  override fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int) {
    if (currentPosition != previousPosition) {
      itemList[currentPosition].updateItem(true)
      itemList[previousPosition].updateItem(false)
      previousPosition = currentPosition
    }
    this.widgetModel = widgetModel
    count = 0
  }

  private fun gridSpanCount(listCount: Int): Int {
    return when {
      listCount >= 3 -> 3
      listCount == 2 -> 2
      else -> 1
    }

  }

  override fun onClick(view: View?) {
    when (view?.id) {
      R.id.iv_left_arrow -> {
        count--
        if (count >= 0)
          if (tableListdata[count].isNotEmpty()) {
            tableListdata[count].renderUI()
          } else {
            setTitle()
            chartClear()
          }
        else
          count = 0
      }
      R.id.iv_right_arrow -> {
        if (count < 2)
          count++
        if (tableListdata[count].isNotEmpty()) {
          tableListdata[count].renderUI()
        } else {
          setTitle()
          chartClear()
        }
      }
    }
  }

}