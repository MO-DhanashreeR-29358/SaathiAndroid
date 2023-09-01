package com.mosl.tabapp.reactivation.detailTL

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
import com.mosl.tabapp.reactivation.detail.ReactivationDetailTableMapper
import com.mosl.tabapp.reactivation.detail.ReactivationDetailTableRow
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject


class ReactivationDetailTLFragment : CommonFragment(), ReactivationDetailTLView {

    lateinit var binding : FragmentReactivationDetailBinding

    @Inject
    lateinit var presenter: ReactivationDetailTLPresenter

    var reactivationPeriodFilter : String = ""
    var reactivationCurrentStage : String =""
    var reactivationEmpCode : String = ""
    var reactivationFirstReportingTo : String =""

    private lateinit var dynamicHeaderRow: DynamicHeaderRow
    private lateinit var tableRowAdapter: TableRowAdapter<ReactivationDetailTableRow>


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
        reactivationEmpCode = bundle.getString(MOSLConstants.REACTIVATION_EMP_CODE, "") ?: ""
        reactivationFirstReportingTo = bundle.getString(MOSLConstants.REACTIVATION_FIRST_REPORTING_TO, "") ?: ""
    }

    private fun setupView(){
        binding.tableWrapper.setRetryListener(onRetryClick)
        dynamicHeaderRow = ReactivationDetailTableMapper.header(requireContext()) as DynamicHeaderRow
        val mfAdapter =
            requireContext().createRowAdapter<ReactivationDetailTableRow>(dynamicHeaderRow)
                .apply {
            viewProvider.rowClickListener = { row, view ->

            }
          }
        tableRowAdapter = mfAdapter
        binding.tableWrapper.setAdapter(tableRowAdapter)
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

    override fun getEmpCode(): String {
        return reactivationEmpCode
    }

    override fun getFirstReportingTo(): String {
        return reactivationFirstReportingTo
    }

    override fun renderData(uiModel: UiModel<ReactivationDetailCombo>) {
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
            val hierarchyReport: List<ReactivationDetailTableRow> =
                ReactivationDetailTableMapper.map(data.data)
            binding.tableWrapper.setData(
                hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
            )
        }
    }


}