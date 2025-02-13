package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLeadDetials

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.mosl.tabapp.databinding.FragBusinessSnapshotNewBinding
import com.mosl.tabapp.databinding.FragMyLeadDetailsBinding
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardUIEvent
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardView
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class IndirectIFAMyLeadDetailsFragment: CommonFragment(),IndirectIFAMyLeadDetailsView {

    private lateinit var binding: FragMyLeadDetailsBinding
    @Inject
    lateinit var presenter:IndirectIFAMyLeadDetailsPresenter

    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<IndirectIFAMyLeadDetailsTableRow>
    private val navigateEvent = PublishSubject.create<MutableMap<String, String>>()
    private val subject = IndirectIFADashboardUIEvent.getFundUiEvent()
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    private lateinit var filterAdapterPeriod: ArrayAdapter<LookUp>
    private lateinit var filterAdapterCategory: ArrayAdapter<LookUp>
    private lateinit var filterAdapterFilterBy: ArrayAdapter<LookUp>
    private  val myleadSubject= PublishSubject.create<IndirectIFAMyLeadDetailsUiEvent>()
    var strSelStaqeCode:String="C0"
    var strSelPeriods:String="Since Inception"
    var strSelCategory:String="Category"
    var strSelFilterBy:String="Select"
    override fun getLayout(): Int {
        return R.layout.frag_my_lead_details
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragMyLeadDetailsBinding.bind(view)
        setUpFromArguments()
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }
    private fun setUpView() {

        filterAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterStageCode.adapter = filterAdapter

        filterAdapterPeriod=SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterPeriod.adapter=filterAdapterPeriod

        filterAdapterCategory= SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterCategory.adapter=filterAdapterCategory

        filterAdapterFilterBy=SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterBy.adapter=filterAdapterFilterBy

        binding.tableWrapperMyLead.setRetryListener(onRetryClick)
        dynamicHeaderRow = IndirectIFAMyLeadDetailsTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<IndirectIFAMyLeadDetailsTableRow>(dynamicHeaderRow)
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
        binding.tableWrapperMyLead.setAdapter(tableRowAdapter)

        binding.spFilterStageCode.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                strSelStaqeCode=binding.spFilterStageCode.selectedItem.toString()
                myleadSubject.onNext(IndirectIFAMyLeadDetailsUiEvent.onDataRenderCall)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        })
        binding.spFilterPeriod.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                strSelPeriods=binding.spFilterPeriod.selectedItem.toString()
                myleadSubject.onNext(IndirectIFAMyLeadDetailsUiEvent.onDataRenderCall)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        })
        binding.spFilterCategory.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                strSelCategory=binding.spFilterCategory.selectedItem.toString()
                myleadSubject.onNext(IndirectIFAMyLeadDetailsUiEvent.onDataRenderCall)

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
                myleadSubject.onNext(IndirectIFAMyLeadDetailsUiEvent.onDataRenderCall)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        })

    }

    private fun setUpFromArguments() {
        try {
            strSelCategory =this.arguments?.getString(MOSLConstants.SEL_CATEGORY, "Category") ?: "Category"
            strSelPeriods=this.arguments?.getString(MOSLConstants.SEL_FILTER, "Since Inception") ?: "Since Inception"
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
        binding.tableWrapperMyLead.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapperMyLead.showError(error)
        }

        uiModel.onSuccess { data ->
            val headerData = mutableListOf<String>()
            data.headers.map {
                headerData.add(CRMAppUtil.getRefreshIndirectColumnName(it))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<IndirectIFAMyLeadDetailsTableRow> =
                IndirectIFAMyLeadDetailsTableMapper.map(data.data)
            binding.tableWrapperMyLead.setData(
                hierarchyReport,
                ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }

    override fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>) {
        uiModel.onSuccess {
            filterAdapter.setSpinnerData(binding.spFilterStageCode, it.lstStageCode)
            var lstTempPeriods=mutableListOf<LookUp>()
            it.lstPeriods.forEachIndexed { index, lookUp ->
                lstTempPeriods.add(lookUp)
            }
            lstTempPeriods.add(LookUp(100,"Since Inception"))
            filterAdapterPeriod.setSpinnerData(binding.spFilterPeriod, lstTempPeriods)

            lstTempPeriods.forEachIndexed { index, lookUp ->
                if(lookUp.lookUpName.equals(strSelPeriods,true))
                {
                    binding.spFilterPeriod.setSelection(index)
                }
            }

            var lstCategory= mutableListOf<LookUp>()
            lstCategory.add(LookUp(lookUpID = 0, lookUpName = "Category"))
            lstCategory.add(LookUp(lookUpID = 0, lookUpName = "Assigned"))
            lstCategory.add(LookUp(lookUpID = 0, lookUpName = "Self"))
            lstCategory.add(LookUp(lookUpID = 0, lookUpName = "Total"))
            filterAdapterCategory.setSpinnerData(binding.spFilterCategory,lstCategory)
            lstCategory.forEachIndexed { index, lookUp ->
                if(strSelCategory.equals(lookUp.lookUpName,true))
                {
                    binding.spFilterCategory.setSelection(index)
                }
            }



            var lstFilterBy=mutableListOf<LookUp>()
            lstFilterBy.add(LookUp(lookUpID = 0, lookUpName ="Select"))
            lstFilterBy.add(LookUp(lookUpID = 0, lookUpName ="Active"))
            lstFilterBy.add(LookUp(lookUpID = 0, lookUpName ="InActive"))
            filterAdapterFilterBy.setSpinnerData(binding.spFilterBy,lstFilterBy)

            myleadSubject.onNext(IndirectIFAMyLeadDetailsUiEvent.onDataRenderCall)

        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)

        }
    }

    override fun onDataRenderCalled(): Observable<IndirectIFAMyLeadDetailsUiEvent.onDataRenderCall> {
        return  myleadSubject.ofType()
    }

    override fun fnGetFilterData(): IndirectIFAMyLeadDetailsFilter {
        return IndirectIFAMyLeadDetailsFilter(
            strSelStaqeCode=this.strSelStaqeCode,
            strSelPeriods=this.strSelPeriods,
            strSelCategory=this.strSelCategory,
            strSelFilterBy=this.strSelFilterBy
        )
    }



}