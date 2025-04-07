package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.cncUniqueReportTable

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragCncAdminUniqueReportTableBinding
import com.mosl.tabapp.databinding.FragUniqueReportTableBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.IndirectIFADashboardDetailsUiEvent
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard.IndirectCncFocoDashboardDetailsUiEvent
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target.IndirectCncTargetView
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class IndirectCncAdminUniqueReportTableFragment : CommonFragment(), IndirectCncAdminUniqueReportTableView{
    private lateinit var binding: FragCncAdminUniqueReportTableBinding
    @Inject
    lateinit var presenter: IndirectCncAdminUniqueReportTablePresenter

    private var model: WidgetModel = WidgetModel()
    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<IndirectCncAdminUniqueReportTableRow>
    private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()
    private val subject = IndirectCncFocoDashboardDetailsUiEvent.getFundUiEvent()
    var strFilterVal:String=""
    var strWidgetType:String =""
    var strFilterCatVal:String="Select"
    private val widgetUiEventIFA = PublishSubject.create<IndirectCncAdminUniqueReportTableUiEvent>()
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    override fun getLayout(): Int {
        return R.layout.frag_cnc_admin_unique_report_table
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragCncAdminUniqueReportTableBinding.bind(view)
        fnSetUpArgument()
        setUpView()
        presenter.start()

    }
    private fun setUpView() {

        binding.tableWrapperCncAdmin.setRetryListener(onRetryClick)
        dynamicHeaderRow = IndirectCncAdminUniqueReportTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter = requireContext().createRowAdapter<IndirectCncAdminUniqueReportTableRow>(dynamicHeaderRow)
            .apply {
                viewProvider.rowClickListener = { row, view ->
                    val reportData = tableRowAdapter.getItem(row).data
                    var leadId=reportData.hasMap["LeadID"]
                    if(leadId.toString().endsWith(".0"))
                    {
                        leadId=leadId.toString().substringBefore(".")
                    }
                  //  widgetUiEventIFA.onNext(IndirectCncAdminUniqueReportTableUiEvent.onReportRowClicked(leadId?:""))
                }
            }
        tableRowAdapter = mfAdapter
        binding.tableWrapperCncAdmin.setAdapter(tableRowAdapter)

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
        columnData?.let {
            additionalParameterMap[MOSLConstants.COL_FILTER] =
                columnData
        }
        rowData?.let { additionalParameterMap[MOSLConstants.ROW_FILTER] = rowData }
        return additionalParameterMap
    }

    override fun renderData(uiModel: UiModel<BusinessSnapshotCombo>) {
        binding.tableWrapperCncAdmin.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapperCncAdmin.showError(error)
        }

        uiModel.onSuccess { data ->
            val headerData = mutableListOf<String>()
            data.headers.map {
                headerData.add(CRMAppUtil.getRefreshIndirectColumnName(it))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<IndirectCncAdminUniqueReportTableRow> =
                IndirectCncAdminUniqueReportTableMapper.map(data.data)
            binding.tableWrapperCncAdmin.setData(
                hierarchyReport,
                ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }

    override fun onFilterEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.onFilterChanged> {
        return  subject.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
        this.strFilterVal=strFilterVal
        if(strWidgetType.equals(IndirectCncTargetView.strReve))
        {
            widgetUiEventIFA.onNext(IndirectCncAdminUniqueReportTableUiEvent.onRevenuBroEventCalled)
        }
        else if(strWidgetType.equals(IndirectCncTargetView.strNoOfClient)){
            widgetUiEventIFA.onNext(IndirectCncAdminUniqueReportTableUiEvent.onMeetingEventCalled)
        }

    }

    override fun fngetFilterValue(): String {
        return this.strFilterVal
    }

    override fun onFilterCategoryEventCalled(): Observable<IndirectCncFocoDashboardDetailsUiEvent.onFilterCategoryChanged> {
        return  subject.ofType()
    }

    override fun fnRenderFilterCategoryEventCalled(strFilterCatVal: String) {
        this.strFilterCatVal=strFilterCatVal
        if(strWidgetType.equals(IndirectCncTargetView.strReve))
        {
            widgetUiEventIFA.onNext(IndirectCncAdminUniqueReportTableUiEvent.onRevenuBroEventCalled)
        }
        else if(strWidgetType.equals(IndirectCncTargetView.strNoOfClient)){
            widgetUiEventIFA.onNext(IndirectCncAdminUniqueReportTableUiEvent.onMeetingEventCalled)
        }

    }

    override fun fngetFilterProductValue(): String {
        return  this.strFilterCatVal
    }

    override fun fnonMeetingEventCalled(): Observable<IndirectCncAdminUniqueReportTableUiEvent.onMeetingEventCalled> {
        return widgetUiEventIFA.ofType()
    }

    override fun fnonRevenuBroEventCalled(): Observable<IndirectCncAdminUniqueReportTableUiEvent.onRevenuBroEventCalled> {
        return widgetUiEventIFA.ofType()
    }

}