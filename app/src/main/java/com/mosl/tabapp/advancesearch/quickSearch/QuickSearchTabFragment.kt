package com.mosl.tabapp.advancesearch.quickSearch

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.snapshot.AdvanceSearchDataCombo
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.R
import com.mosl.tabapp.advancesearch.AdvanceSearchPresenter
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragTabQuickSearchBinding
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.advanceSearch.AdvanceSearchInput
import com.mosl.tabapp.databinding.DialogIndirectLeadviewBinding
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.IndirectLeadViewDetailUiEvent
import javax.inject.Inject

class QuickSearchTabFragment :CommonFragment(),QuickSearchTabView {
    internal lateinit var binding: FragTabQuickSearchBinding
    @Inject
    lateinit var presenter: QuickSearchTabPresenter
    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<QuickSearchTableRow>
    private val Subject= PublishSubject.create<QuickSearchUiEvent>()
    var isLeadSearch:Boolean=true
    override fun getLayout() = R.layout.frag_tab_quick_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragTabQuickSearchBinding.bind(view)
        hideAppTitle()
        presenter.start()
        setupViews()
    }

    private fun setupViews() {
        binding.radioSelection.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId)
            {
                R.id.radio_btn_lead ->{
                    binding.llClientSearch.visibility=View.GONE
                    binding.llleadSearch.visibility=View.VISIBLE
                    isLeadSearch=true
                }
                R.id.radio_btn_client->{
                    binding.llleadSearch.visibility=View.GONE
                    binding.llClientSearch.visibility=View.VISIBLE
                    isLeadSearch=false
                }

            }
        }

        binding.tableWrapper.setRetryListener(onRetryClick)
        dynamicHeaderRow = QuickSearchTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<QuickSearchTableRow>(dynamicHeaderRow)
                .apply {
                    viewProvider.rowClickListener = { row, view ->
                        val column = view.getTag(R.id.tag_column) as Int
                        val reportData = tableRowAdapter.getItem(row).data
                        Subject.onNext(QuickSearchUiEvent.onTableRowClicked(reportData.hasMap["LeadID"].toString()))
                    }
                }
        tableRowAdapter = mfAdapter
        binding.tableWrapper.setAdapter(tableRowAdapter)
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

    override fun renderData(uiModel: UiModel<AdvanceSearchDataCombo>) {
        binding.tableWrapper.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapper.showError(error)

        }

        uiModel.onSuccess { data ->
            binding.tableWrapper.visibility=View.VISIBLE
            val headerData = mutableListOf<String>()
            data.headers.map {
                headerData.add(CRMAppUtil.getRefreshIndirectColumnName(it.toString()))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<QuickSearchTableRow> =
                QuickSearchTableMapper.map(data.data)
            binding.tableWrapper.setData(
                hierarchyReport,
                ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }

    override fun onTableRowClicked(): Observable<QuickSearchUiEvent.onTableRowClicked> {
        return Subject.ofType()
    }

    override fun fnRenderTableRowClicked(strLeadId: String) {
        Dialog(requireContext()).apply {
            val dialogBinding = DialogIndirectLeadviewBinding.inflate(layoutInflater)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialogBinding.ivCall.visibility=View.GONE
            dialogBinding.ivSms.visibility=View.GONE

//            dialogBinding.ivCreateMeeting.setOnClickListener {
//                dismiss()
//                subject.onNext(IndirectLeadViewDetailUiEvent.CreateMeetingClick(createMeetingId))
//            }
//            dialogBinding.ivSetAppointment.setOnClickListener {
//                dismiss()
//                subject.onNext(IndirectLeadViewDetailUiEvent.SetAppointmentClick)
//            }
//            dialogBinding.ivCall.setOnClickListener {
//                dismiss()
//                subject.onNext(IndirectLeadViewDetailUiEvent.CallClick)
//            }
//            dialogBinding.ivSms.setOnClickListener {
//                dismiss()
//                subject.onNext(IndirectLeadViewDetailUiEvent.SMSClick)
//            }
            dialogBinding.ivClose.setOnClickListener { dismiss() }

        }
            .show()
    }

    override fun fnGetSearchInput(): AdvanceSearchInput {
        if(isLeadSearch)
        {
            var leadId:Int?=null
            if(binding.edtleadID.text.toString().isNotEmpty())
            {
                leadId=Integer.parseInt(binding.edtleadID.text.toString())
            }

            var mobileNo:String?=null
            if(binding.edtMobileNumber.text.toString().isNotEmpty())
            {
                mobileNo=binding.edtMobileNumber.text.toString()
            }
            var emailId:String?=null
            if(binding.edtEmailAddress.text.toString().isNotEmpty())
            {
                emailId=binding.edtEmailAddress.text.toString()
            }
            var name:String?=null
            if(binding.edtleadName.text.toString().isNotEmpty())
            {
                name = binding.edtleadName.text.toString()
            }
            var aadharNo:String?=null

            if(binding.edtAadharNo.text.toString().isNotEmpty())
            {
                aadharNo = binding.edtAadharNo.text.toString()
            }
            var panNo:String?=null
            if(binding.edtPanNo.text.toString().isNotEmpty())
            {
                panNo = binding.edtPanNo.text.toString()
            }

            return  AdvanceSearchInput(
                leadId = leadId,
                mobileNo = mobileNo,
                emailId = emailId,
                name = name,
                aadharNo = aadharNo,
                panNo = panNo
            )
        }
        else{
            return AdvanceSearchInput(
                clientCode = binding.edtclientCode.text.toString(),
                mobileNo = binding.edtCMobileNumber.text.toString(),
                emailId = binding.edtCEmailAddress.text.toString(),
                aadharNo = binding.edtCAadharNo.text.toString(),
                panNo = binding.edtCPanNo.text.toString(),
                ucId = Integer.parseInt(binding.edtUcid.text.toString()),
                fucId = Integer.parseInt(binding.edtfamilyUcid.text.toString())
            )
        }
    }

    override fun onSearchButtonClicked(): Observable<Unit> {
        return binding.btnSearchLead.clicks()
    }

    override fun onResetButtonClicked(): Observable<Unit> {
        return binding.btnResetLead.clicks()
    }

    override fun fnRenderSearchButtonClicked() {
        dataActionSubject.onNext(DataAction.Fetch)
    }

    override fun fnRenderResetButtonClicked() {
        binding.tableWrapper.visibility=View.GONE
        if(isLeadSearch)
        {
                 binding.edtleadID.setText("")
               binding.edtMobileNumber.setText("")
                 binding.edtEmailAddress.setText("")
                binding.edtleadName.setText("")
                 binding.edtAadharNo.setText("")
                 binding.edtPanNo.setText("")

        }
        else{
                 binding.edtclientCode.setText("")
                 binding.edtCMobileNumber.setText("")
               binding.edtCEmailAddress.setText("")
                 binding.edtCAadharNo.setText("")
                 binding.edtCPanNo.setText("")
                 binding.edtUcid.setText("")
               binding.edtfamilyUcid.setText("")
        }
    }
}