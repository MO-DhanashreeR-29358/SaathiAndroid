package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragDashboardReportBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead.IndirectIFAMyLeadUiEvent
import com.mosl.tabapp.util.SpinnerAdapterFactory
import timber.log.Timber
import javax.inject.Inject

class IndirectIFADashboardReportFragment:CommonFragment(),IndirectIFADashboardReportView {
    private lateinit var binding: FragDashboardReportBinding
    @Inject
    lateinit var presenter: IndirectIFADashboardReportPresenter
    private lateinit var model: WidgetModel
    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<IndirectIFADashboardReportTableRow>
    private val navigateEvent = IndirectIFADashboardReportUIEvent.getFundUiEvent()

    private var strSystemName : String = ""
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    var strFilterVal:String=""
    var strDateFilterVal:String="2024-10-15"

    override fun getLayout(): Int {
        return R.layout.frag_dashboard_report
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragDashboardReportBinding.bind(view)
        setUpFromArguments()
        presenter.start()
        setUpView()

    }
    private fun setUpFromArguments() {
        try {
            strSystemName =this.arguments?.getString(MOSLConstants.SYSTEM_NAME, "") ?: ""
            Log.d("systemName", strSystemName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpView() {
        binding.txtLables.text = strSystemName
        if(strSystemName == MOSLConstants.INDIRECT_RUNRATE_FOR_ADVISOR){
            binding.labelContainer.visibility = View.GONE
        }
        binding.tableWrapper.setRetryListener(onRetryClick)
        dynamicHeaderRow = HierarchyReportTableMapper.header(
            requireContext()
        ) as DynamicHeaderRow
        val mfAdapter = requireContext().createRowAdapter<IndirectIFADashboardReportTableRow>(dynamicHeaderRow)
            .apply {
                if(getWidgetName()==MOSLConstants.INDIRECT_1ST_MEETING || getWidgetName()==MOSLConstants.INDIRECT_LEADS_NOT_UPDATED)
                {
                    viewProvider.rowClickListener = { row, view ->
                        val column = view.getTag(R.id.tag_column) as Int
                        val reportData = tableRowAdapter.getItem(row).data
                        var strStageCode=""
                        if(getWidgetName()==MOSLConstants.INDIRECT_1ST_MEETING)
                        {
                            strStageCode=reportData.hasMap["StageCodeDays"].toString()
                        }
                        if(getWidgetName()==MOSLConstants.INDIRECT_LEADS_NOT_UPDATED)
                        {
                            strStageCode=reportData.hasMap["StagecodeDays"].toString()
                        }
                        navigateEvent.onNext(IndirectIFADashboardReportUIEvent.OnReportClick(getWidgetName(),strStageCode))
                    }
                }
            }
        tableRowAdapter = mfAdapter
        binding.tableWrapper.setAdapter(tableRowAdapter)
        if(getWidgetName()==MOSLConstants.INDIRECT_TODAY_BIRTHDAY)
        {

            binding.linearLayout.visibility=View.GONE
            binding.spPeriod.visibility=View.GONE
            navigateEvent.onNext(IndirectIFADashboardReportUIEvent.onFilterChanged(""))

        }
        else if(getWidgetName()==MOSLConstants.INDIRECT_TEAM_APPOINTMENT ||getWidgetName()==MOSLConstants.INDIRECT_NEW_LEAD)
        {
            binding.linearLayout.visibility=View.GONE
            binding.spPeriod.visibility=View.GONE
            navigateEvent.onNext(IndirectIFADashboardReportUIEvent.onDateFilterChanged(strDateFilterVal))
        }
        else
        {

            dataActionSubject.onNext(DataAction.Fetch)
            filterAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
            binding.spPeriod.adapter = filterAdapter
            binding.linearLayout.visibility=View.VISIBLE
            binding.spPeriod.visibility=View.VISIBLE
            binding.spPeriod.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    navigateEvent.onNext(IndirectIFADashboardReportUIEvent.onFilterChanged(binding.spPeriod.selectedItem.toString()))

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            });
        }



    }
    override fun renderData(uiModel: UiModel<DashboardReportCombo>) {
        binding.tableWrapper.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapper.showError(error)
        }

        uiModel.onSuccess { data ->
            val headerData = mutableListOf<String>()
            data.headers.map {
                var strtempHeader=it
                if(strtempHeader.equals("Greaterhenseven",true))
                {
                    strtempHeader=">7"
                }
                if(strtempHeader.equals("Greaterthenfiftin",true))
                {
                    strtempHeader=">15"
                }

                if(strtempHeader.equals("Greaterthenthirty",true))
                {
                    strtempHeader=">30"
                }

                if(strtempHeader.equals("Greaterthensixty",true))
                {
                    strtempHeader=">60"
                }
                headerData.add(CRMAppUtil.getRefreshIndirectColumnName(strtempHeader))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<IndirectIFADashboardReportTableRow> =
                IndirectIFADashboardReportTableMapper.map(data.data)
            binding.tableWrapper.setData(
                hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }

    override fun onReportClickEvent(): Observable<IndirectIFADashboardReportUIEvent.OnReportClick> {
        return navigateEvent.ofType()
    }

    override fun getWidgetName(): String {
        return strSystemName
    }

    override fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>) {
        uiModel.onSuccess {
            filterAdapter.setSpinnerData(binding.spPeriod, it.lstPeriods)
            // binding.spFilterBank.setSelection(0)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)

        }
    }

    override fun onFilterEventCalled(): Observable<IndirectIFADashboardReportUIEvent.onFilterChanged> {
        return  navigateEvent.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
        this.strFilterVal=strFilterVal
        navigateEvent.onNext(IndirectIFADashboardReportUIEvent.OnCallDataEvent)
    }

    override fun fngetFilterValue(): String {
        return this.strFilterVal
    }

    override fun onCallDataEvent(): Observable<IndirectIFADashboardReportUIEvent.OnCallDataEvent> {
        return navigateEvent.ofType()
    }

    override fun onDateFilterEventCalled(): Observable<IndirectIFADashboardReportUIEvent.onDateFilterChanged> {
        return navigateEvent.ofType()
    }

    override fun fnRenderDateFilterEventCalled(strDateFilterVal: String) {
        this.strDateFilterVal=strDateFilterVal
        navigateEvent.onNext(IndirectIFADashboardReportUIEvent.OnCallDateDataEvent)
    }

    override fun fngetDateFilterValue(): String {
        return this.strDateFilterVal
    }

    override fun onCallDateDataEvent(): Observable<IndirectIFADashboardReportUIEvent.OnCallDateDataEvent> {
        return navigateEvent.ofType()
    }
}