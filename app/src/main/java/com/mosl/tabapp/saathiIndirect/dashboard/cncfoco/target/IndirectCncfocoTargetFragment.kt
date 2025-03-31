package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.Graph
import com.mosl.shared.util.Utility
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCncFocoTargetBinding
import com.mosl.tabapp.databinding.FragIndirectIfaTargetBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.IndirectcncfocoDashboardUIEvent
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class IndirectCncfocoTargetFragment:CommonFragment(),IndirectCncTargetView {
    internal lateinit var binding:FragIndirectCncFocoTargetBinding
    @Inject
    lateinit var presenter:IndirectCncfocoTragetPresenter
    private var itemList = mutableListOf<HorinzatalCncfofoTargetWidgetItem>()
    private val subject = IndirectcncfocoDashboardUIEvent.getFundUiEvent()
    var strFilterVal:String=""

    private var widgetModelList: MutableList<WidgetModel> = mutableListOf()

    private val widgetUiEventIFA =
        PublishSubject.create<IndirectCncfocoTargetUiEvent>()

    private val pieChartEvent =
        PublishSubject.create<IndirectCncfocoTargetUiEvent>()

    private val summaryCountUiEvent =
        PublishSubject.create<IndirectCncfocoTargetUiEvent>()

    private var previousPosition: Int = 0
    private var lableItemList = mutableListOf<IndirectCncfocoTargetLableItem>()

    private var widgetModel: WidgetModel? = null

    private val summaryCountGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val labelGroupAdapter = GroupAdapter<GroupieViewHolder>()
    override fun getLayout() = R.layout.frag_indirect_cnc_foco_target

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCncFocoTargetBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()

        if (itemList.size > 0) {
            widgetUiEventIFA.onNext(
                IndirectCncfocoTargetUiEvent.ItemClick(
                    itemList.get(0).widgetModel,
                    0
                )
            )
        }
        makeChart()


    }

    @SuppressLint("WrongConstant")
    private fun setupViews() {
        binding.includeTargetChart.rvSummaryCount.visibility= View.VISIBLE
        binding.includeTargetChart.imageView.visibility= View.VISIBLE
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

        val objWidgetMode:WidgetModel= WidgetModel()
        objWidgetMode.name= IndirectCncTargetView.strReve
        objWidgetMode.iconUrl2="https://saathiuat.motilaloswal.com:1023/image/Icon/W_TA_Self_Advisor_Talktime.png"
        objWidgetMode.isSelected=true
        itemList.add(HorinzatalCncfofoTargetWidgetItem(objWidgetMode, widgetUiEventIFA))

        val objWidgetMode2:WidgetModel=WidgetModel()
        objWidgetMode2.name= IndirectCncTargetView.strNoOfClient
        objWidgetMode2.iconUrl2="https://saathiuat.motilaloswal.com:1023/image/Icon/W_TA_Self_Advisor_Talktime.png"
        objWidgetMode2.isSelected=false
        itemList.add(HorinzatalCncfofoTargetWidgetItem(objWidgetMode2, widgetUiEventIFA))


        groupAdapter.addAll(itemList)

        binding.includeTargetChart.llAchivement.setOnClickListener {
            val args = Bundle()
            args.putString(MOSLConstants.SYSTEM_NAME, widgetModel?.name)
            navigator.openPage(
                NavigationConstants.NAVIGATE_TO_INDIRECT_IFA_DASHBOARD_DETAILS.toString(), args,
                addToStack = true,
                forceReload = false
            )
        }
    }


    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

    override fun onFilterEventCalled(): Observable<IndirectcncfocoDashboardUIEvent.onFilterChanged> {
        return  subject.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
        this.strFilterVal=strFilterVal
        dataActionSubject.onNext(DataAction.Fetch)
    }

    override fun fngetFilterValue(): String {
        return this.strFilterVal
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
    fun chartClear() {
        binding.includeTargetChart.chart.clear()
    }
    fun barChartClear() {
        binding.includeTargetChart.barChart.clear()
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
                IndirectCncfocoTargetLableItem(widgetModel?.name,tableItemModel, requireContext(), this)
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

    private fun setBarChart(graph: ArrayList<Graph>) {
        val data = getBarData(graph)

        data.setDrawValues(false) // for text hidden
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        data.setValueTypeface(Typeface.DEFAULT)
        binding.includeTargetChart.barChart.description.isEnabled = false // Disable description text
        binding.includeTargetChart.barChart.setData(data)
        binding.includeTargetChart.barChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(
                e: Entry,
                h: Highlight
            ) {

                // pieChartEvent.onNext(FocoTargetUiEvent.PieChartClick(tableItemList[h.x.toInt()].label))

            }

            override fun onNothingSelected() {}
        })

        val groupSpace = 0.3f
        val barSpace = 0.1f // x2 dataset
        val barWidth = 0.20f // x2 dataset

        binding.includeTargetChart.barChart.groupBars(0f, groupSpace, barSpace)  // Extra padding on the bottom if needed
        var labels = ArrayList<String?>()
        for (i in graph.indices) {
            labels.add(graph[i].product)
        }
        val xAxis: XAxis = binding.includeTargetChart.barChart.xAxis
        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> graph[value.toInt()].product!! }
        with(binding.includeTargetChart.barChart) {
            description.isEnabled = false
            setDragEnabled(true)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                isGranularityEnabled = true
                axisMinimum = 0f
                setCenterAxisLabels(false)
                axisMaximum = 0 + barData.getGroupWidth(groupSpace, barSpace) * labels.size
                labelCount = labels.size - 1
                animateY(1000)
                spaceMin = 1F
                spaceMax = 1f
            }
        }

        // Y-Axis: Make sure the Y-Axis starts at 0
        val yAxis: YAxis = binding.includeTargetChart.barChart.axisLeft
        yAxis.axisMinimum = 0f

        val yAxisRight: YAxis = binding.includeTargetChart.barChart.axisRight
        yAxisRight.isEnabled = false // Hide the right Y-Axis

        binding.includeTargetChart.barChart.invalidate()
    }

    private fun getBarData(graph: ArrayList<Graph>): BarData {
        val barCurrentMonthEntries = ArrayList<BarEntry>()
        val barLastMonthEntries = ArrayList<BarEntry>()
        for (index in graph.indices) {
            val currentMonth = graph[index].currentMonth?.toFloat() ?: 0.0f
            barCurrentMonthEntries.add(
                BarEntry(index.toFloat(), currentMonth)
            )
        }
        for (index in graph.indices) {
            val lastMonth = graph[index].lastMonth?.toFloat() ?: 0.0f
            barLastMonthEntries.add(
                BarEntry(index.toFloat(), lastMonth)
            )
        }
        val barDataSet1 = BarDataSet(barCurrentMonthEntries, "Current Month").apply {
            color = ContextCompat.getColor(requireContext(), R.color.red)
        }
        val barDataSet2 = BarDataSet(barLastMonthEntries, "Last Month").apply {
            color = ContextCompat.getColor(requireContext(), R.color.blue)
        }
        val data = BarData(barDataSet1, barDataSet2).apply {
            barWidth = 0.20f
        }

        return data
    }

    override fun renderData(uiModel: UiModel<FocoDashboardModel>) {
        toggleProgress(uiModel.inProgress)
        binding.includeTargetChart.chart.visibility = View.VISIBLE
        binding.includeTargetChart.barChart.visibility = View.GONE
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
            binding.includeTargetChart.txtTarget.text = it.strTarget?.replace(".0","")
            binding.includeTargetChart.txtAchievement.text = it.strAchieved?.replace(".0","")
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

    override fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int) {
        if (currentPosition != previousPosition) {
            itemList.get(currentPosition).updateItem(true)
            itemList.get(previousPosition).updateItem(false)
            previousPosition = currentPosition
        }
        this.widgetModel = widgetModel
    }

    override fun renderBarChartData(uiModel: UiModel<FocoDashboardModel>) {
        toggleProgress(uiModel.inProgress)
        barChartClear()
        binding.includeTargetChart.chart.visibility = View.GONE
        binding.includeTargetChart.barChart.visibility = View.VISIBLE
        labelGroupAdapter.clear()
        lableItemList.clear()
        binding.includeTargetChart.txtTarget.text = "0"
        binding.includeTargetChart.txtAchievement.text = "0"
        uiModel.onFailure { error ->
            Timber.d("renderPeriodFilter ${error.message}")
            barChartClear()
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
                barChartClear()
            } else {
                setBarChart(it.graph)
                LableView(it.graph)
            }

        }
    }

    override fun getColorsArray(): IntArray {
        return Utility.VORDIPLOM_COLORS
    }

    override fun onWidgetItemClick(): Observable<IndirectCncfocoTargetUiEvent.ItemClick> {
        return widgetUiEventIFA.ofType()
    }


}