package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.ReportDetails

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFAMyLeadDetailsFilter
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragMyLeadDetailsBinding
import com.mosl.tabapp.databinding.FragMyReportDetailsBinding
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLeadDetials.IndirectIFAMyLeadDetailsUiEvent
import com.mosl.tabapp.saathiIndirect.setAppointment.IndirectSetAppointmentUiEvent
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class IndirectIFAReportDetailsFragment : CommonFragment(),IndirectIFAReportDetailsView {

    private lateinit var binding: FragMyReportDetailsBinding
    @Inject
    lateinit var presenter: IndirectIFAReportDetailsPresenter

    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<IndirectIFAReportDetailsTableRow>
    private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()
    //private val subject = IndirectIFADashboardUIEvent.getFundUiEvent()
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    private lateinit var filterAdapterPeriod: ArrayAdapter<LookUp>
    private lateinit var filterAdapterFilterBy: ArrayAdapter<LookUp>

    private  val myRepotSubject= PublishSubject.create<IndirectIFAReportDetailsUiEvent>()
    var strSelStaqeCode:String="C0"
    var strSelDays:String="Since Inception"
    var strSelFilterBy:String="Select"
    var strPageType:String=""
    override fun getLayout(): Int {
        return R.layout.frag_my_report_details
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragMyReportDetailsBinding.bind(view)
        setUpFromArguments()
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }
    private fun setUpView() {

        filterAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterStageCode.adapter = filterAdapter

        filterAdapterPeriod= SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterDays.adapter=filterAdapterPeriod
        
        filterAdapterFilterBy= SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterBy.adapter=filterAdapterFilterBy

        binding.tableWrapperMyReportDet.setRetryListener(onRetryClick)
        dynamicHeaderRow = IndirectIFAReportDetailsTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<IndirectIFAReportDetailsTableRow>(dynamicHeaderRow)
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
        binding.tableWrapperMyReportDet.setAdapter(tableRowAdapter)

        binding.spFilterStageCode.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                strSelStaqeCode=binding.spFilterStageCode.selectedItem.toString()
                if(strPageType==MOSLConstants.INDIRECT_1ST_MEETING)
                {
                    myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onDataRenderCall)
                }
                if(strPageType==MOSLConstants.INDIRECT_LEADS_NOT_UPDATED) {
                    myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onLeadNotUpdateDataRenderCall)
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        })
        binding.spFilterDays.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                strSelDays=binding.spFilterDays.selectedItem.toString()
                if(strPageType==MOSLConstants.INDIRECT_1ST_MEETING)
                {
                    myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onDataRenderCall)
                }
                if(strPageType==MOSLConstants.INDIRECT_LEADS_NOT_UPDATED) {
                    myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onLeadNotUpdateDataRenderCall)
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        })
         binding.spFilterBy.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                strSelFilterBy=binding.spFilterBy.selectedItem.toString()
                if(strPageType==MOSLConstants.INDIRECT_1ST_MEETING)
                {
                    myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onDataRenderCall)
                }
                if(strPageType==MOSLConstants.INDIRECT_LEADS_NOT_UPDATED) {
                    myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onLeadNotUpdateDataRenderCall)
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        })

    }

    private fun setUpFromArguments() {
        try {
            strPageType =this.arguments?.getString(MOSLConstants.PAGE_TYPE, "") ?: ""
            strSelStaqeCode=this.arguments?.getString(MOSLConstants.STAGE_CODE, "C0") ?: "C0"

        } catch (e: Exception) {
            e.printStackTrace()
        }

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
        binding.tableWrapperMyReportDet.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapperMyReportDet.showError(error)
        }

        uiModel.onSuccess { data ->
            val headerData = mutableListOf<String>()
            data.headers.map {
                headerData.add(CRMAppUtil.getRefreshIndirectColumnName(it))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<IndirectIFAReportDetailsTableRow> =
                IndirectIFAReportDetailsTableMapper.map(data.data)
            binding.tableWrapperMyReportDet.setData(
                hierarchyReport,
                ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }

    override fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>) {
        uiModel.onSuccess {
            filterAdapter.setSpinnerData(binding.spFilterStageCode, it.lstStageCode)
            it.lstStageCode.forEachIndexed { index, lookUp ->
                if(lookUp.lookUpName.equals(strSelStaqeCode,true))
                {
                    binding.spFilterStageCode.setSelection(index)
                }
            }

            var lstFilterDate=mutableListOf<LookUp>()
            if(strPageType==MOSLConstants.INDIRECT_1ST_MEETING)
            {
                lstFilterDate.add(LookUp(lookUpID = 0, lookUpName = ">7"))
                lstFilterDate.add(LookUp(lookUpID = 0, lookUpName = ">15"))
            }


            if(strPageType==MOSLConstants.INDIRECT_LEADS_NOT_UPDATED) {
                lstFilterDate.add(LookUp(lookUpID = 0, lookUpName = ">30"))
                lstFilterDate.add(LookUp(lookUpID = 0, lookUpName = ">60"))
            }
            lstFilterDate.add(LookUp(lookUpID = 0, lookUpName = "Total"))

            filterAdapterPeriod.setSpinnerData(binding.spFilterDays, lstFilterDate)

            var lstFilterBy=mutableListOf<LookUp>()
            lstFilterBy.add(LookUp(lookUpID = 0, lookUpName ="Select"))
            lstFilterBy.add(LookUp(lookUpID = 0, lookUpName ="Active"))
            lstFilterBy.add(LookUp(lookUpID = 0, lookUpName ="InActive"))
            filterAdapterFilterBy.setSpinnerData(binding.spFilterBy,lstFilterBy)

            if(strPageType==MOSLConstants.INDIRECT_1ST_MEETING)
            {
                myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onDataRenderCall)
            }
            if(strPageType==MOSLConstants.INDIRECT_LEADS_NOT_UPDATED) {
                myRepotSubject.onNext(IndirectIFAReportDetailsUiEvent.onLeadNotUpdateDataRenderCall)
            }

        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)

        }
    }

    override fun onDataRenderCalled(): Observable<IndirectIFAReportDetailsUiEvent.onDataRenderCall> {
        return  myRepotSubject.ofType()
    }

    override fun onLeadNotUpdateDataRenderCalled(): Observable<IndirectIFAReportDetailsUiEvent.onLeadNotUpdateDataRenderCall> {
        return  myRepotSubject.ofType()
    }

    override fun fnGetFilterData(): IndirectIFAMyLeadDetailsFilter {
        return IndirectIFAMyLeadDetailsFilter(
            strSelStaqeCode=this.strSelStaqeCode,
            strSelPeriods=this.strSelDays,
            strSelCategory="",
            strSelFilterBy=this.strSelFilterBy
        )
    }



}