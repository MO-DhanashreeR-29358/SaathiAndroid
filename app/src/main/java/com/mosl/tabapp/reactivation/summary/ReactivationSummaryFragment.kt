package com.mosl.tabapp.reactivation.summary

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.reactivation.ReactivationSummaryItem
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentReactivationSummaryBinding
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject


class ReactivationSummaryFragment : CommonFragment(), ReactivationSummaryView {

    lateinit var binding : FragmentReactivationSummaryBinding
    private lateinit var sourceAdapter: ArrayAdapter<String>
    @Inject
    lateinit var presenter: ReactivationSummaryPresenter

    var reactivationCurrentStage : String = ""

    override fun getLayout(): Int {
        return R.layout.fragment_reactivation_summary
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReactivationSummaryBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)

    }

    private fun setupView(){
        lateinit var periodAdapter: ArrayAdapter<FilterRowData>
        periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spPeriod.adapter = periodAdapter
        var list : ArrayList<FilterRowData> = ArrayList()
        val filter1 = FilterRowData(0,"MTD","MTD", "")
        val filter2 = FilterRowData(1,"YTD","YTD", "")
        val filter7 = FilterRowData(2,"Last Month","Last Month", "")
        val filter3 = FilterRowData(3,"Q1","Q1", "")
        val filter4 = FilterRowData(4,"Q2","Q2", "")
        val filter5 = FilterRowData(5,"Q3","Q3", "")
        val filter6 = FilterRowData(6,"Q4","Q4", "")
        val filter8 = FilterRowData(7,"Last FY","Last FY", "")
        list.add(filter1)
        list.add(filter2)
        list.add(filter3)
        list.add(filter4)
        list.add(filter5)
        list.add(filter6)
        list.add(filter7)
        list.add(filter8)

        periodAdapter.setSpinnerData(binding.spPeriod, list)

        sourceAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spSource.adapter = sourceAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    override fun getSelectedPeriod(): FilterRowData {
        return binding.spPeriod.selectedItem as FilterRowData
    }

    override fun getSelectedSource(): String {
        return binding.spSource.selectedItem as String
    }

    override fun onSearchClick(): Observable<Unit> {
        return binding.btnSearch.clicks()
    }

    override fun renderSearchClick(uiModel: UiModel<ReactivationSummaryItem>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.apply {
                tvAssigned.text = it.assigned
                tvContacted.text = it.contacted
                tvNonContacted.text = it.nonContacted
                tvReactivatedClients.text = it.reactivatedClients
                tvTraded.text = it.traded
                tvRevenue.text = it.revenue
            }
        }
    }

    override fun renderSourceList(uiModel: UiModel<List<String>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            sourceAdapter.setSpinnerData(binding.spSource, it)
        }
    }

    override fun getCurrentStage(): String {
        return reactivationCurrentStage
    }

    override fun onAssignedClick(): Observable<Unit> {
        return binding.assignedContainer.clicks().map {
            reactivationCurrentStage = "Assigned"
        }
    }

    override fun onContactedClick(): Observable<Unit> {
        return binding.contactedContainer.clicks().map {
            reactivationCurrentStage = "Contacted"
        }
    }

    override fun onNonContactedClick(): Observable<Unit> {
        return binding.nonContactedContainer.clicks().map {
            reactivationCurrentStage = "Not Contacted"
        }
    }

    override fun onReactivatedClientslick(): Observable<Unit> {
        return binding.reactivatedClientsContainer.clicks().map {
            reactivationCurrentStage = "Reactivated"
        }
    }

    override fun onTradedClick(): Observable<Unit> {
        return binding.tradedContainer.clicks().map {
            reactivationCurrentStage = "Traded"
        }
    }

    override fun onRevenueClick(): Observable<Unit> {
        return binding.revenueContainer.clicks().map {
            reactivationCurrentStage = "Revenue"
        }
    }


}