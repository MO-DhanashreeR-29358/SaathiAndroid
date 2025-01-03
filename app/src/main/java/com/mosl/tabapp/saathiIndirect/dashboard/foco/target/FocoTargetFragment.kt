package com.mosl.tabapp.saathiIndirect.dashboard.foco.target

import android.graphics.Color
import android.graphics.Typeface
import android.mtp.MtpConstants
import android.os.Bundle
import android.util.Log
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
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.bank.*
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.Graph
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
import com.mosl.tabapp.dashboard.target.WidgetUiEvent
import com.mosl.tabapp.databinding.FragmentFocoTargetBinding
import com.mosl.tabapp.databinding.FragmentTargetBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.CLIENT_REACH
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.IP_SALES
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.NO_OF_CLIENT_MEETINGS
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.ONLINE_REVENUE
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.REVENUE
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.REVENUE_GROWTH_DEGROWTH
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.TOTAL_MOB_APP
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FocoTargetFragment : CommonFragment(), FocoTargetView {

  lateinit var binding: FragmentFocoTargetBinding

  @Inject
  lateinit var presenter: FocoTargetPresenter

  private var widgetModelList: MutableList<WidgetModel> = mutableListOf()

  private val widgetUiEvent =
    PublishSubject.create<FocoTargetUiEvent>()

  private val pieChartEvent =
    PublishSubject.create<FocoTargetUiEvent>()

  private val summaryCountUiEvent =
    PublishSubject.create<FocoTargetUiEvent>()

  private var previousPosition: Int = 0
  private var itemList = mutableListOf<FocoHorinzatalTargetWidgetItem>()
  private var lableItemList = mutableListOf<FocoTargetLableItem>()

  private var widgetModel: WidgetModel? = null

  private val summaryCountGroupAdapter = GroupAdapter<GroupieViewHolder>()
  private val labelGroupAdapter = GroupAdapter<GroupieViewHolder>()

  override fun getLayout(): Int {
    return R.layout.fragment_foco_target
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding = FragmentFocoTargetBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)

    if (itemList.size > 0) {
      widgetUiEvent.onNext(
        FocoTargetUiEvent.ItemClick(
          itemList.get(0).widgetModel,
          0
        )
      )
    }
    makeChart()

  }

  private fun setUpView() {
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    binding.horizontalRecyclerView.apply {
      layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      setHasFixedSize(true)
      adapter = groupAdapter
    }


    binding.includeTargetChart.rvLable.apply {
      layoutManager = GridLayoutManager(
        requireContext(), 3,
        GridLayoutManager.VERTICAL,
        false
      )
      adapter = labelGroupAdapter
    }
    var model1 = WidgetModel()
    model1.name = REVENUE
    model1.isSelected = true
    widgetModelList.add(model1)

    var model2 = WidgetModel()
    model2.name = REVENUE_GROWTH_DEGROWTH
    widgetModelList.add(model2)

    var model3 = WidgetModel()
    model3.name = NO_OF_CLIENT_MEETINGS
    widgetModelList.add(model3)

    var model4 = WidgetModel()
    model4.name = ONLINE_REVENUE
    widgetModelList.add(model4)

    var model5 = WidgetModel()
    model5.name = CLIENT_REACH
    widgetModelList.add(model5)

    var model6 = WidgetModel()
    model6.name = IP_SALES
    widgetModelList.add(model6)

    var model7 = WidgetModel()
    model7.name = TOTAL_MOB_APP
    widgetModelList.add(model7)

    widgetModelList.mapIndexed { position: Int, model: WidgetModel ->
      itemList.add(FocoHorinzatalTargetWidgetItem(model, widgetUiEvent))
    }

    groupAdapter.addAll(itemList)


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
      setTransparentCircleColor(Color.WHITE)
      setTransparentCircleAlpha(100)
      setHoleRadius(70f)
      setTransparentCircleRadius(30f)
      setDrawCenterText(true)
      setDrawEntryLabels(false)
      setBackgroundColor(Color.WHITE)
      animateY(1400, Easing.EasingOption.EaseInOutQuad)
      highlightValues(null)
      isDrawHoleEnabled = true
    }

  }

  override fun onWidgetItemClick(): Observable<FocoTargetUiEvent.ItemClick> {
    return widgetUiEvent.ofType()
  }

  override fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int) {
    if (currentPosition != previousPosition) {
      itemList.get(currentPosition).updateItem(true)
      itemList.get(previousPosition).updateItem(false)
      previousPosition = currentPosition
    }
    this.widgetModel = widgetModel
  }

  override fun renderData(uiModel: UiModel<FocoDashboardModel>) {
    toggleProgress(uiModel.inProgress)
    chartClear()
    labelGroupAdapter.clear()
    lableItemList.clear()
    binding.includeTargetChart.txtTarget.text = "0"
    binding.includeTargetChart.txtAchievement.text = "0"
    uiModel.onFailure { error ->
      Timber.d("renderPeriodFilter ${error.message}")
      chartClear()
    }

    uiModel.onSuccess {
      Timber.d("strTarget ${it.strTarget}")
      Timber.d("strAchieved ${it.strAchieved}")
      binding.includeTargetChart.txtTarget.text = it.strTarget
      binding.includeTargetChart.txtAchievement.text = it.strAchieved
      for( gr in it.graph){
        Timber.d("graph Product ${gr.product}")
        Timber.d("graph value ${gr.Value}")
      }
      if (it.graph.isEmpty()) {
        binding.includeTargetChart.chart.clear()
      } else {
        setDataBroking(it.graph)
        LableView(it.graph)
      }

    }
  }

  override fun getColorsArray(): IntArray {
    return VORDIPLOM_COLORS
  }


  fun chartClear() {
    binding.includeTargetChart.chart.clear()
  }


  private fun setDataBroking(
    tableItemList: ArrayList<Graph>
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

       // pieChartEvent.onNext(FocoTargetUiEvent.PieChartClick(tableItemList[h.x.toInt()].label))

      }

      override fun onNothingSelected() {}
    })
    binding.includeTargetChart.chart.invalidate()
  }

  private fun getPieData(
    tableItemList: ArrayList<Graph>

  ): PieData {
    val entries = ArrayList<PieEntry>()
    for (i in tableItemList.indices) {
      val value = tableItemList[i].Value?.toFloat() ?: 0.0f
      // Log the value and product to make sure they are correct
      Log.d("PieChart", "Product: ${tableItemList[i].product}, Value: $value")
      entries.add(
        PieEntry(
          value,
          tableItemList[i].product, i
        )
      )
    }
    val dataSet = PieDataSet(entries, "Total Client")
    Log.d("PieChart", " dataSet.entryCount: ${dataSet.entryCount}")
    dataSet.sliceSpace = 2f
    dataSet.selectionShift = 5f
    val colors = ArrayList<Int>()
    for (i in tableItemList.indices) {
      colors.add(getColorsArray()[i])
    }
    dataSet.colors = colors
    return PieData(dataSet)
  }

  fun LableView(tableList: ArrayList<Graph>) {
    (binding.includeTargetChart.rvLable.getLayoutManager() as GridLayoutManager).spanCount =
      gridSpanCount(tableList.size)

    tableList.mapIndexed { index, tableItemModel ->
      lableItemList.add(
        FocoTargetLableItem(tableItemModel, requireContext(), this)
      )
    }
    labelGroupAdapter.addAll(lableItemList)

  }


  fun gridSpanCount(listCount: Int): Int {
    return when {
      listCount >= 3 -> 3
      listCount == 2 -> 2

      else -> 1
    }

  }

}