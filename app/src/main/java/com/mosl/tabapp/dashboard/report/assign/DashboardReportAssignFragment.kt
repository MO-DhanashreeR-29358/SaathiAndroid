package com.mosl.tabapp.dashboard.report.assign

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.LeadAssignItem
import com.mosl.mobile.domain.dashboardreport.STPLeadTransferResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.LAST_CONNECTED
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.LEAD_STATUS
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.NAME
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.PROSPECT_ID
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.REGISTRATION_DATE
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.STAGE
import com.mosl.tabapp.dashboard.report.assign.DashboardReportAssignPresenter.Companion.TEAM_LEADER
import com.mosl.tabapp.databinding.FragDashboardReportAssignBinding
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory.getDefaultAdapter
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject
import kotlin.collections.set

class DashboardReportAssignFragment : CommonFragment(),
    OnClickListener, MOSLConstants,
    DashboardReportAssignlView {
    lateinit var binding: FragDashboardReportAssignBinding
    private lateinit var dashboardReportAssignTableAdapter: DashboardReportAssignTableAdapter
    private lateinit var employeeAdapter: ArrayAdapter<FilterRowData>
    private val subject = PublishSubject.create<DashboardReportAssignUIEvent>()
    private var selectedApprovalList = mutableListOf<String>()
    private val refreshEvent = PublishSubject.create<Int>()

    @Inject
    lateinit var presenter: DashboardReportAssignPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragDashboardReportAssignBinding.bind(view)
        setAppTitle("Lead Assign")
        setUpView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        refreshEvent.onNext(0)

    }

    override fun getLayout(): Int {
        return layout.frag_dashboard_report_assign
    }

    private fun setUpView() {

        binding.btnAssign.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        employeeAdapter = getDefaultAdapter(requireContext())
        binding.spSelectEmployee.adapter = employeeAdapter
        dashboardReportAssignTableAdapter =
            DashboardReportAssignTableAdapter(
                requireActivity(),
                subject
            )
        binding.table.adapter = dashboardReportAssignTableAdapter
    }

    fun prefix(input: Int): String {
        return if (input.toString().length == 1) {
            "0$input"
        } else input.toString() + ""
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAssign -> if (getSelectedAppovalList().size > 0) {
                MSFDialog.alertDialog(
                    requireContext(), 0,
                    resources.getString(string.motilal_oswal),
                    "Are you sure, you want to assign selected request?", "Yes", "No", true
                ) { action: Action, _ ->
                    if (action == OK) {
                        subject.onNext(
                            DashboardReportAssignUIEvent.BtnEvent(
                                "2"
                            )
                        )
                    }
                }
            } else {
                MSFDialog.alertDialog(
                    requireContext(), 0,
                    resources.getString(string.motilal_oswal),
                    "Please select at least one record", "OK", true, null
                )
            }
            R.id.btnReset -> if (getSelectedAppovalList().size > 0) {
                MSFDialog.alertDialog(
                    requireContext(), 0,
                    resources.getString(string.motilal_oswal),
                    "Are you sure, you want to cancel selected request?", "Yes", "No", true
                ) { action: Action, _ ->
                    if (action == OK) {
                        dashboardReportAssignTableAdapter.clearList()
                        dashboardReportAssignTableAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                MSFDialog.alertDialog(
                    requireContext(), 0,
                    resources.getString(string.motilal_oswal),
                    "Please select at least one record", "OK", true, null
                )
            }
        }
    }


    override fun renderEmployeeFilter(typeFilter: List<FilterRowData>) {
        if (typeFilter.isNotEmpty()) {
            employeeAdapter.setSpinnerData(binding.spSelectEmployee, typeFilter)
        }
    }

    override fun renderLeadAssignStpList(uiModel: UiModel<List<LeadAssignItem>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure {
            binding.table.visibility = View.GONE
            binding.tvNochart.visibility = View.VISIBLE
            binding.llButton.visibility = View.GONE
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {

            if (it.isNotEmpty()) {
                it.map {
                    val hm =
                        LinkedHashMap<String, String>()
                    hm[NAME] = it.name
                    hm[PROSPECT_ID] = it.prospectId
                    hm[REGISTRATION_DATE] = it.registrationDate
                    hm[LAST_CONNECTED] = it.lastConnected
                    hm[STAGE] = it.stage
                    hm[LEAD_STATUS] = it.leadStatus
                    hm[TEAM_LEADER] = it.teamLeader
                    dashboardReportAssignTableAdapter.add(hm)
                }
                binding.table.visibility = View.VISIBLE
                binding.tvNochart.visibility = View.GONE
                binding.llButton.visibility = View.VISIBLE
                dashboardReportAssignTableAdapter.notifyDataSetChanged()
            } else {
                binding.table.visibility = View.GONE
                binding.tvNochart.visibility = View.VISIBLE
                binding.llButton.visibility = View.GONE
            }

        }

    }

    override fun onEmployeeFilterChange(): Observable<Int> {
        return binding.spSelectEmployee.itemSelections()
    }

    override fun getEmployeeFilter(): FilterRowData {
        return binding.spSelectEmployee.selectedItem as FilterRowData
    }

    override fun onRefreshEvent(): Observable<Int> {
        return refreshEvent.ofType()
    }

    override fun onBtnEvent(): Observable<DashboardReportAssignUIEvent.BtnEvent> {
        return subject.ofType()
    }

    override fun onSelectedApprovalList(): Observable<DashboardReportAssignUIEvent.CheckBoxSelectedArrayList> {
        return subject.ofType()
    }

    override fun selectedApprovalList(arrayList: java.util.ArrayList<String>) {
        selectedApprovalList = arrayList
    }

    override fun multiUpdateApproval(uiModel: UiModel<STPLeadTransferResponse>) {
        toggleProgressBar(uiModel.inProgress)

        uiModel.onFailure { error ->
            showOnFailurePopup(error)
        }

        uiModel.onSuccess { data ->
            if (data.response != null) {
                MSFDialog.alertDialog(
                    requireContext(),
                    0,
                    resources.getString(string.motilal_oswal),
                    data.message,
                    "OK",
                    true
                ) { action: Action, _ ->
                    if (action == OK) {
                        refreshEvent.onNext(0)
                        dashboardReportAssignTableAdapter.clearList()
                    }
                }

            }
        }

    }

    override fun getSelectedAppovalList(): MutableList<String> {
        return selectedApprovalList
    }

    companion object {
        const val date6 = "yyyy-MM-dd"
    }
}