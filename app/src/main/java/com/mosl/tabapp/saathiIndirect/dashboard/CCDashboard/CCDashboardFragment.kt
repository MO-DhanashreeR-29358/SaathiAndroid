package com.mosl.tabapp.saathiIndirect.dashboard.CCDashboard

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.IndirectCCFilterDataClass
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCcDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectDashboardBinding
import com.mosl.tabapp.report.HierarchyReportTableMapper
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportTableMapper
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportTableRow
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportUIEvent
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetFragment
import com.mosl.tabapp.table.DynamicHeaderRow
import com.mosl.tabapp.table.createRowAdapter
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tableview.rowadapter.TableRowAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class CCDashboardFragment : CommonFragment(), CCDashboardView, View.OnClickListener{
    internal lateinit var binding: FragIndirectCcDashboardBinding
    @Inject
    lateinit var presenter: CCDashboardPresenter

    private lateinit var periodAdapter: ArrayAdapter<LookUp>
    private lateinit var advisorAdapter: ArrayAdapter<LookUp>

    private lateinit var dynamicHeaderRowMS: DynamicHeaderRow
    private lateinit var tableRowAdapterMS: TableRowAdapter<FocoDashboardReportTableRow>

    private lateinit var dynamicHeaderRowMRS: DynamicHeaderRow
    private lateinit var tableRowAdapterMRS: TableRowAdapter<FocoDashboardReportTableRow>

    private val subject = PublishSubject.create<CCDashboardUIEvent.OnMeetingReachSummaryLoad>()

    override fun getLayout() = R.layout.frag_indirect_cc_dashboard
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCcDashboardBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {
        periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterPeriod.adapter = periodAdapter

        advisorAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterAdvisor.adapter = advisorAdapter

        if (binding.twMeetingSummary.visibility != View.VISIBLE) {
            toggleSection(binding.twMeetingSummary, binding.ivMeetSumADV)
        }
        if (binding.twMeetingReachSummary.visibility != View.VISIBLE) {
            toggleSection(binding.twMeetingReachSummary, binding.ivMeetReachSum)
        }
        binding.twMeetingSummary.setRetryListener(onRetryClick)
        binding.twMeetingReachSummary.setRetryListener(onRetryClick)
        dynamicHeaderRowMS = HierarchyReportTableMapper.header(
            requireContext()
        ) as DynamicHeaderRow
        val mfAdapter = requireContext().createRowAdapter<FocoDashboardReportTableRow>(dynamicHeaderRowMS)
            .apply {

            }
        tableRowAdapterMS = mfAdapter
        binding.twMeetingSummary.setAdapter(tableRowAdapterMS)

        dynamicHeaderRowMRS = HierarchyReportTableMapper.header(
            requireContext()
        ) as DynamicHeaderRow
        val mfAdapter1 = requireContext().createRowAdapter<FocoDashboardReportTableRow>(dynamicHeaderRowMRS)
            .apply {

            }
        tableRowAdapterMRS = mfAdapter1
        binding.twMeetingReachSummary.setAdapter(tableRowAdapterMRS)

        binding.rlMeetSumADV.setOnClickListener(this)
        binding.rlMeetReachSumADV.setOnClickListener(this)

    }
    val args = Bundle()
    private fun loadMeetingSummary(data : DashboardReportCombo) {
        val headerData = mutableListOf<String>()
        data.headers.map {
            headerData.add(CRMAppUtil.getRefreshIndirectColumnName(it))
        }
        dynamicHeaderRowMS.setHeader(headerData)
        val hierarchyReport: List<FocoDashboardReportTableRow> =
            FocoDashboardReportTableMapper.map(data.data)
        binding.twMeetingSummary.setData(
            hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
        )
    }

    private fun loadMeetingReachSummary(data : DashboardReportCombo) {
        val headerData = mutableListOf<String>()
        data.headers.map {
            headerData.add(CRMAppUtil.getRefreshIndirectColumnName(it))
        }
        dynamicHeaderRowMRS.setHeader(headerData)
        val hierarchyReport: List<FocoDashboardReportTableRow> =
            FocoDashboardReportTableMapper.map(data.data)
        binding.twMeetingReachSummary.setData(
            hierarchyReport, ErrorModel.noData(getString(R.string.error_message_no_data_available))
        )
    }
    override fun fnRenderFilterList(uiModel: UiModel<IndirectCCFilterDataClass>) {
        uiModel.onSuccess {
            periodAdapter.setSpinnerData(binding.spFilterPeriod, it.lstPeriods)
            advisorAdapter.setSpinnerData(binding.spFilterAdvisor, it.lstAdvisor)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)

        }
    }

    override fun onPeriodChange(): Observable<Int> {
        return binding.spFilterPeriod.itemSelections()
    }

    override fun onAdvisorChange(): Observable<Int> {
        return binding.spFilterAdvisor.itemSelections()
    }

    override fun getPeriod(): String? {
        return (binding.spFilterPeriod.selectedItem as? LookUp)?.lookUpName
    }

    override fun getAdvisor(): String? {
        return (binding.spFilterAdvisor.selectedItem as? LookUp)?.lookUpName
    }

    override fun renderMeetingSummaryData(uiModel: UiModel<DashboardReportCombo>) {
        binding.twMeetingSummary.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.twMeetingSummary.showError(error)
        }

        uiModel.onSuccess { data ->
            loadMeetingSummary(data)
            subject.onNext(CCDashboardUIEvent.OnMeetingReachSummaryLoad)
        }
    }

    override fun renderMeetingReachSummaryData(uiModel: UiModel<DashboardReportCombo>) {
        binding.twMeetingReachSummary.toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            binding.twMeetingReachSummary.showError(error)
        }

        uiModel.onSuccess { data ->
            loadMeetingReachSummary(data)
        }
    }

    override fun onMRSLoadEvent(): Observable<CCDashboardUIEvent.OnMeetingReachSummaryLoad> {
        return subject.ofType()
    }

    override fun onLeadCreationClicked(): Observable<Unit> {
        return binding.leadcreation.clicks()
    }

    private fun toggleSection(
        parent: View,
        icon: ImageView
    ) {
        if (parent.visibility == View.VISIBLE) {
            parent.visibility = View.GONE
            icon.setImageResource(R.drawable.plus)
        } else {
            parent.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.minus)
        }
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlMeetSumADV -> toggleSection(
                binding.twMeetingSummary, binding.ivMeetSumADV
            )
            R.id.rlMeetReachSumADV -> {
                toggleSection(
                    binding.twMeetingReachSummary, binding.ivMeetReachSum
                )
            }
        }
    }

}