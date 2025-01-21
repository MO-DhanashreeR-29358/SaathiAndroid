package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReportHiercytable

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragUniqueReportHierchyTableBinding
import com.mosl.tabapp.databinding.FragUniqueReportTableBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.IndirectIFADashboardDetailsUiEvent
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetView
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class IndirectIFAUniqueReportHierchyTableFragment : CommonFragment(),
    IndirectIFAUniqueReportHierchyTableView {
    private lateinit var binding: FragUniqueReportHierchyTableBinding
    @Inject
    lateinit var presenter: IndirectIFAUniqueReportHierchyTablePresenter
    private var model: WidgetModel = WidgetModel()
    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<IndirectIFAUniqueReportHierchyTableRow>
    private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()
    private val subject = IndirectIFADashboardDetailsUiEvent.getFundUiEvent()
    var strFilterVal:String="Last FY"
    var strWidgetType:String =""
    private val widgetUiEventIFA = PublishSubject.create<IndirectIFAUniqueReportHierchyUiEvent>()

    override fun getLayout(): Int {
        return R.layout.frag_unique_report_hierchy_table
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragUniqueReportHierchyTableBinding.bind(view)
        fnSetUpArgument()
        setUpView()
        presenter.start()

    }
    private fun setUpView() {
        binding.spFilter1.visibility= View.GONE
        binding.spFilter2.visibility= View.GONE
        binding.spFilter3.visibility= View.GONE


        binding.tableWrapper.setRetryListener(onRetryClick)
        dynamicHeaderRow = IndirectIFAUniqueReportHierchyTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<IndirectIFAUniqueReportHierchyTableRow>(dynamicHeaderRow)
                .apply {
                    viewProvider.rowClickListener = { row, view ->
                        val column = view.getTag(R.id.tag_column) as Int
                        val reportData = tableRowAdapter.getItem(row).data
                        navigateEvent.onNext(
                            getAdditionalParameterMap(
                                reportData.data[0],
                                reportData.headers[column + 1]
                            )
                        )
                    }
                }
        tableRowAdapter = mfAdapter
        binding.tableWrapper.setAdapter(tableRowAdapter)
    }
    private fun fnSetUpArgument()
    {
        strWidgetType= this.arguments?.getString(MOSLConstants.SYSTEM_NAME, "") ?: ""
    }
    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }


    override fun getAdditionalParameterMap(
        rowData: String?,
        columnData: String?
    ): MutableMap<String, String> {
        val additionalParameterMap: MutableMap<String, String> = HashMap()
        /*  getFilter1()?.let { additionalParameterMap[it.keyValue] = it.value }
          getFilter2()?.let { additionalParameterMap[it.keyValue] = it.value }
          getFilter3()?.let { additionalParameterMap[it.keyValue] = it.value }*/
        columnData?.let {
            additionalParameterMap[MOSLConstants.COL_FILTER] =
                columnData
        }
        rowData?.let { additionalParameterMap[MOSLConstants.ROW_FILTER] = rowData }
        return additionalParameterMap
    }

    override fun renderData(uiModel: UiModel<BusinessSnapshotCombo>) {
        binding.tableWrapper.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapper.showError(error)
        }

        uiModel.onSuccess { data ->
            val headerData = mutableListOf<String>()
            data.headers.map {
                headerData.add(CRMAppUtil.getRefreshColumnName(it))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<IndirectIFAUniqueReportHierchyTableRow> =
                IndirectIFAUniqueReportHierchyTableMapper.map(data.data)
            binding.tableWrapper.setData(
                hierarchyReport,
                ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }


    override fun onFilterEventCalled(): Observable<IndirectIFADashboardDetailsUiEvent.onFilterChanged> {
        return  subject.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
        this.strFilterVal=strFilterVal
        if(strWidgetType.equals(IndirectIFATargetView.strMeeting))
        {
            widgetUiEventIFA.onNext(IndirectIFAUniqueReportHierchyUiEvent.onMeetingEventCalled)
        }
        else if(strWidgetType.equals(IndirectIFATargetView.strIFAActivation)){
            widgetUiEventIFA.onNext(IndirectIFAUniqueReportHierchyUiEvent.onIfaActivationEventCalled)
        }
        else if(strWidgetType.equals(IndirectIFATargetView.strReveIP)){
            widgetUiEventIFA.onNext(IndirectIFAUniqueReportHierchyUiEvent.onRevenuIpEventCalled)
        }
        else if(strWidgetType.equals(IndirectIFATargetView.strReveBro)){
            widgetUiEventIFA.onNext(IndirectIFAUniqueReportHierchyUiEvent.onRevenuBroEventCalled)
        }
    }

    override fun fngetFilterValue(): String {
        return this.strFilterVal
    }

    override fun fnonMeetingEventCalled(): Observable<IndirectIFAUniqueReportHierchyUiEvent.onMeetingEventCalled> {
        return widgetUiEventIFA.ofType()
    }

    override fun fnonIfaActivationEventCalled(): Observable<IndirectIFAUniqueReportHierchyUiEvent.onIfaActivationEventCalled> {
        return widgetUiEventIFA.ofType()
    }

    override fun fnonRevenuIpEventCalled(): Observable<IndirectIFAUniqueReportHierchyUiEvent.onRevenuIpEventCalled> {
        return widgetUiEventIFA.ofType()
    }

    override fun fnonRevenuBroEventCalled(): Observable<IndirectIFAUniqueReportHierchyUiEvent.onRevenuBroEventCalled> {
        return widgetUiEventIFA.ofType()
    }

}