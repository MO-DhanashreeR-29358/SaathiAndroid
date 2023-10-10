package com.mosl.tabapp.reactivation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.reactivation.ReactivationDetailCombo
import com.mosl.mobile.domain.reactivation.ReactivationFeedbackItem
import com.mosl.mobile.domain.reactivation.ReactivationFeedbackResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentReactivationDetailBinding
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject


class ReactivationDetailFragment : CommonFragment(), ReactivationDetailView {

    lateinit var binding : FragmentReactivationDetailBinding

    @Inject
    lateinit var presenter: ReactivationDetailPresenter

    var reactivationPeriodFilter : String = ""
    var reactivationCurrentStage : String =""
    var reactivationSource : String =""

    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<ReactivationDetailTableRow>
    var strFeedBackOption:String=""
    var strFeedbackTxt:String=""
    var refNo :String=""
    private val subject=ReactivationDetailUiEvent.getSubject()
    override fun getLayout(): Int {
        return R.layout.fragment_reactivation_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReactivationDetailBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    override fun extractArguments(bundle: Bundle) {
        super.extractArguments(bundle)
        reactivationPeriodFilter = bundle.getString(MOSLConstants.REACTIVATION_PERIOD, "") ?: ""
        reactivationCurrentStage = bundle.getString(MOSLConstants.REACTIVATION_TYPE, "") ?: ""
        reactivationSource = bundle.getString(MOSLConstants.REACTIVATION_SOURCE, "") ?: ""
    }

    private fun setupView(){
        if(userManager.getUser()?.isRetailSalesProfile() == true){
           binding.txtLables.text = "Reactivation Mapped Client Detail Report"
        }else{
            binding.txtLables.text = "Reactivation Mapped Client Hierarchy Report"
        }
        binding.tableWrapper.setRetryListener(onRetryClick)
        dynamicHeaderRow = ReactivationDetailTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<ReactivationDetailTableRow>(dynamicHeaderRow)
                .apply {
            viewProvider.rowClickListener = { row, view ->
                val reportData = tableRowAdapter.getItem(row).data
                if(userManager.getUser()?.isRetailSalesProfile() == true){
                    refNo = getValue("RefNo", hasMap = reportData.hasMap)
                    val isEditable = getValue("IsEditable", hasMap = reportData.hasMap)
                    if(isEditable.equals("True",ignoreCase = true)){
                        fnFeedbackAlert()
                    }
                }else{
                    val employeeCode = getValue("EmployeeCode", hasMap = reportData.hasMap)
                    val firstReportingTo = getValue("FirstReportingTo", hasMap = reportData.hasMap)
                    subject.onNext(ReactivationDetailUiEvent.OnHierarchyRowClick(employeeCode,firstReportingTo))
                }
            }
          }
        tableRowAdapter = mfAdapter
        binding.tableWrapper.setAdapter(tableRowAdapter)
    }

    private fun getValue(
        vararg keys: String,
        hasMap: Map<String, String>
    ): String {
        for (key in keys) {
            val value: String? = hasMap[key]
            if (value != null) {
                return value
            }
        }
        return "0"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    override fun getPeriodFilter(): String {
        return reactivationPeriodFilter
    }

    override fun getCurrentStage(): String {
        return reactivationCurrentStage
    }

    override fun getSourceFilter(): String {
        return reactivationSource
    }

    override fun renderData(uiModel: UiModel<ReactivationDetailCombo>) {
        binding.tableWrapper.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.tableWrapper.showError(error)
        }

        uiModel.onSuccess { data ->
            val headerData = mutableListOf<String>()
            data.headers.map {
                headerData.add(CRMAppUtil.getRefreshReactivationColumnName(it))
            }
            dynamicHeaderRow.setHeader(headerData)
            val hierarchyReport: List<ReactivationDetailTableRow> =
                ReactivationDetailTableMapper.map(data.data)
            binding.tableWrapper.setData(
                hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }


    private fun fnFeedbackAlert()
    {
            val feedbackOptList= listOf("Interested","Not Interested","Follow Up","Not Contactable")
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.layout_feedback_form,null)
        val spinnerFeedback=view.findViewById<Spinner>(R.id.spFeedbackCategory)
        val btnCancel=view.findViewById<AppCompatButton>(R.id.btnCancel)
        val btnCreate=view.findViewById<AppCompatButton>(R.id.btnCreate)
        val etRemark=view.findViewById<TextInputEditText>(R.id.etRemark)
        val tvRemarkError=view.findViewById<TextView>(R.id.tvRemarkError)
        val adapterFeedback = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, feedbackOptList)
        adapterFeedback.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFeedback.adapter=adapterFeedback
        spinnerFeedback.setOnItemSelectedListener(object :OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    strFeedBackOption=feedbackOptList.get(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })
        btnCancel.setOnClickListener {
            builder.dismiss()
        }
        btnCreate.setOnClickListener {
                strFeedbackTxt=etRemark.text.toString()
                if(strFeedbackTxt.length==0)
                {
                    tvRemarkError.visibility=View.VISIBLE
                    tvRemarkError.setText("Enter Remark")

                }
            else
                {
                    tvRemarkError.visibility=View.GONE
                    subject.onNext(ReactivationDetailUiEvent.OnFeedbackSubmitCalled)
                    builder.dismiss()
                }
        }
        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun getFeedbackSubmitData(): ReactivationFeedbackItem {
        return ReactivationFeedbackItem(userManager.getUser()?.employeeCode?:"",reactivationPeriodFilter,strFeedBackOption,
            refNo,strFeedbackTxt)
    }
    override fun fnCallFeedbackSubmit(): Observable<ReactivationDetailUiEvent.OnFeedbackSubmitCalled> {
        return subject.ofType()
    }

    override fun renderFeedbackSubmit(uiModel: UiModel<ReactivationFeedbackResponse>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
           if(it.Status)
           {
               subject.onNext(ReactivationDetailUiEvent.RefreshDetailTable)
           }
            else
           {
               showErrorToast(it.Message)
           }
        }
    }

    override fun onHierarchyRowClick(): Observable<ReactivationDetailUiEvent.OnHierarchyRowClick> {
        return  subject.ofType()
    }


    override fun onRefreshDetailTable(): Observable<ReactivationDetailUiEvent.RefreshDetailTable> {
        return subject.ofType()
    }

}