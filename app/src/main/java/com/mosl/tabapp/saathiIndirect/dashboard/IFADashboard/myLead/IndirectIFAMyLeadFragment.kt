package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.mosl.tabapp.databinding.FragBusinessSnapshotNewBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardUIEvent
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class IndirectIFAMyLeadFragment :CommonFragment(),IndirectIFAMyLeadView {
    private lateinit var binding: FragBusinessSnapshotNewBinding
    @Inject
    lateinit var presenter: IndirectIFAMyLeadPresenter
    private var model: WidgetModel = WidgetModel()
    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<IndirectIFAMyLeadTableRow>
    private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()
    private val subject = IndirectIFADashboardUIEvent.getFundUiEvent()
    var strFilterVal:String=""

    override fun getLayout(): Int {
        return R.layout.frag_business_snapshot_new
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragBusinessSnapshotNewBinding.bind(view)
        setUpView()
        presenter.start()

    }
    private fun setUpView() {
        binding.spFilter1.visibility= View.GONE
        binding.spFilter2.visibility= View.GONE
        binding.spFilter3.visibility= View.GONE


        binding.tableWrapper.setRetryListener(onRetryClick)
        dynamicHeaderRow = IndirectIFAMyViewTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<IndirectIFAMyLeadTableRow>(dynamicHeaderRow)
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
            val hierarchyReport: List<IndirectIFAMyLeadTableRow> =
                IndirectIFAMyViewTableMapper.map(data.data)
            binding.tableWrapper.setData(
                hierarchyReport,
                ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }


    override fun onFilterEventCalled(): Observable<IndirectIFADashboardUIEvent.onFilterChanged> {
        return  subject.ofType()
    }

    override fun fnRenderFilterEventCalled(strFilterVal: String) {
        this.strFilterVal=strFilterVal
        Log.d("my lead",this.strFilterVal)
        dataActionSubject.onNext(DataAction.Fetch)
    }

    override fun fngetFilterValue(): String {
        return this.strFilterVal
    }


}