package com.mosl.tabapp.dashboard.ausfb.leads

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
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.bank.*
import com.mosl.saathi.api.model.dashboard.ausfbLead.AusfbLeadModel
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMValueFormatter
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.ClientsLeadShowFragment
import com.mosl.tabapp.databinding.FragmentAuLeadsBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class AuLeadsFragment : CommonFragment(), AuLeadsView, View.OnClickListener {

    lateinit var binding: FragmentAuLeadsBinding
    private lateinit var periodAdapter: ArrayAdapter<FilterRowData>
    private lateinit var productAdapter: ArrayAdapter<FilterRowData>
    private lateinit var bankAdapter: ArrayAdapter<BankCodeModel>
    private lateinit var branchAdapter: ArrayAdapter<BranchCodeModel>
    private lateinit var subBranchAdapter: ArrayAdapter<SubBranchCodeModel>

    @Inject
    lateinit var presenter: AuLeadsPresenter

    @Inject
    lateinit var dashboardModel: DashboardModel

    private lateinit var model: WidgetModel

    private val barChartEvent =
        PublishSubject.create<AuLeadUiEvent>()

    override fun getLayout(): Int {
        return R.layout.fragment_au_leads
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAuLeadsBinding.bind(view)
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        binding.tvShowMore.setOnClickListener(this)
        makeChart()
        makeLegend()


    }

    private fun setUpView() {
        val context = requireContext()

        val groupModel = dashboardModel.getGroupModel(
            DashboardSystemModel.getLeadSystemName()
        )
        val widgetList = groupModel.widgetList
        model = if (widgetList.size == 1) {
            widgetList[0]
        } else {
            widgetList[1]
        }
        val filterList = model.widgetFilterList
        if (filterList.size == 2) {
            periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
            binding.spFilterPeriod.adapter = periodAdapter
            periodAdapter.setSpinnerData(binding.spFilterPeriod, filterList[0].filterData)
            arguments?.let { binding.spFilterPeriod.setSelection(it.getInt(FILTERFIRST)) }

            productAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
            binding.spFilterProduct.adapter = productAdapter
            productAdapter.setSpinnerData(binding.spFilterProduct, filterList[1].filterData)
            binding.spFilterProduct.visibility = View.VISIBLE
            arguments?.let { binding.spFilterProduct.setSelection(it.getInt(FILTERSECOND)) }

        } else if (filterList.size == 1) {
            periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
            binding.spFilterPeriod.adapter = periodAdapter
            periodAdapter.setSpinnerData(binding.spFilterPeriod, filterList[0].filterData)
            arguments?.let { binding.spFilterPeriod.setSelection(it.getInt(FILTERFIRST)) }

        }
        bankAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
        binding.spFilterBank.adapter = bankAdapter

        branchAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
        binding.spFilterBranch.adapter = branchAdapter

        subBranchAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
        binding.spFilterSubBranch.adapter = subBranchAdapter

    }

    override fun onPeriodChange(): Observable<Int> {
        return binding.spFilterPeriod.itemSelections()
    }

    override fun onProductChange(): Observable<Int> {
        return binding.spFilterProduct.itemSelections()
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

    override fun getProduct(): FilterRowData? {
        return binding.spFilterProduct.selectedItem as? FilterRowData
    }

    override fun getWidgetSystemsName(): String {
        return model.systemName
    }


    override fun onBranchChange(): Observable<Int> {
        return binding.spFilterBranch.itemSelections()
    }

    override fun onSubBranchChange(): Observable<Int> {
        return binding.spFilterSubBranch.itemSelections()
    }

    override fun onBarChartClick(): Observable<AuLeadUiEvent.BarChartClick> {

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
        when (dropdownTypes) {
            1 -> {
                bankAdapter.setSpinnerData(binding.spFilterBank, selectBank)
                branchAdapter.setSpinnerData(binding.spFilterBranch, selectBranch)
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
            2 -> {
                branchAdapter.setSpinnerData(binding.spFilterBranch, selectBranch)
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
            3 -> {
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
            else -> {
                bankAdapter.setSpinnerData(binding.spFilterBank, selectBank)
                branchAdapter.setSpinnerData(binding.spFilterBranch, selectBranch)
                subBranchAdapter.setSpinnerData(binding.spFilterSubBranch, selectSubBranch)
            }
        }
    }

    fun chartClear() {
        binding.chart.clear()
        binding.relativeLayout.visibility = View.GONE
    }

    override fun renderData(uiModel: UiModel<AusfbLeadModel>) {
        toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            showOnFailurePopup(error)
            chartClear()
        }

        uiModel.onSuccess {
            binding.relativeLayout.visibility = View.VISIBLE
            binding.tvTotal.text = it.getTotal()
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

            getBank()?.let {
                this[FLT_BANKNAME] = it.bankId.toString()
            }

            getBranch()?.let {
                this[FLT_BANKBRANCHCODE] = it.branchId.toString()
            }

            getSubBranch()?.let {
                this[FLT_BANKSUBBROKERCODE] = it.bankBranchId.toString()
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
        val mChart = binding.chart
        Collections.reverse(xAxisValue)
        Collections.reverse(yAxisValue.get(0))
        Collections.reverse(yAxisValue.get(1))

        val leftAxis = mChart.axisLeft
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val xAxis: XAxis = mChart.getXAxis()
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawAxisLine(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.black)
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValue))

        val data = getBarData(yAxisValue, lableString)
        data.setValueFormatter(CRMValueFormatter())
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(13f)
        data.barWidth = 0.6f

        mChart.setData(data)
        mChart.invalidate()
        mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(
                e: Entry,
                h: Highlight
            ) {
                if (e.y == 0.0f) return
                if (lableString[h.stackIndex].equals(ASSIGNED, ignoreCase = true)) {
                    if (!yAxisValue.get(1).get(e.x.toInt()).equals("0")) {
                        barChartEvent.onNext(
                            AuLeadUiEvent.BarChartClick(
                                lableString[h.stackIndex],
                                xAxisValue.get(e.x.toInt())
                            )
                        )
                    }
                } else {
                    barChartEvent.onNext(
                        AuLeadUiEvent.BarChartClick(
                            lableString[h.stackIndex],
                            xAxisValue.get(e.x.toInt())
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

        val barDataSet: BarDataSet
        val colors = ArrayList<Int>()
        val VORDIPLOM_COLORS = intArrayOf(
            Color.rgb(38, 127, 183), Color.rgb(255, 154, 0)
        )
        for (c in VORDIPLOM_COLORS) colors.add(c)
        barDataSet = BarDataSet(entries, "")
        barDataSet.colors = colors
        barDataSet.stackLabels = lableString

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barDataSet)
        return BarData(dataSets)
    }

    private fun makeChart() {
        binding.chart.apply {
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
        val legend = binding.chart.legend
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
                    ClientsLeadShowFragment.newInstance()
                args.putSerializable(DASHBOARD_MODEL, dashboardModel)
                args.putSerializable(
                    MAP,
                    getAddtionalParameterMap() as Serializable
                )
                args.putString(SYSTEM_NAME, getWidgetSystemsName())
                args.putInt(FILTERFIRST, binding.spFilterPeriod.selectedItemPosition)
                args.putInt(FILTERSECOND, binding.spFilterProduct.selectedItemPosition)
                args.putInt(FLT_BANKNAME, binding.spFilterBank.selectedItemPosition)
                args.putInt(FLT_BANKBRANCHCODE, binding.spFilterBranch.selectedItemPosition)
                args.putInt(FLT_BANKSUBBROKERCODE, binding.spFilterSubBranch.selectedItemPosition)
                args.putString(TYPESOFLEAD, AUSFB_LEAD)
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
        const val ASSIGNED = "Assigned"
    }

}