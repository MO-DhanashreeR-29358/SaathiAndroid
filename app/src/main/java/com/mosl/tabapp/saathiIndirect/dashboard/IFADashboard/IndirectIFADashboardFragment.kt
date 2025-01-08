package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectIfaDashboardBinding
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Actionable.IFAActionableInsightDialogFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead.IndirectIFAMyLeadFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.snapshot.IndirectIFABuisnessSnapshotFragment
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class IndirectIFADashboardFragment  : CommonFragment(), IndirectIFADashboardView {
    internal lateinit var binding: FragIndirectIfaDashboardBinding
    @Inject
    lateinit var presenter: IndirectIFADasboardPresenter
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    private val subject =IndirectIFADashboardUIEvent.getFundUiEvent()
    override fun getLayout() = R.layout.frag_indirect_ifa_dashboard
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectIfaDashboardBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

        filterAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterBank.adapter = filterAdapter
        binding.ivTeamTargetAchievement.setOnClickListener {
            toggleSection(
                binding.llTeamTargetAchievement, binding.ivTeamTargetAchievement
            )
        }
        binding.ivBusinessSnapshotHeadip.setOnClickListener {
            toggleSection(binding.llBusinessSnapshotHeadip,binding.ivBusinessSnapshotHeadip)
        }
        binding.ivBusinessSnapshotHead.setOnClickListener {
            toggleSection(
                binding.llBusinessSnapshotHead,binding.ivBusinessSnapshotHead
            )
        }
        binding.ivBusinessSnapshotHeadlead.setOnClickListener {
            toggleSection(
                binding.llBusinessSnapshotHeadlead,binding.ivBusinessSnapshotHeadlead
            )
        }
        loadTeamTargetAchievementFragment()
        fnLoadMyBuisSnapshotip()
        fnLoadMyBuisSnapshotBroking()
        fnLoadMyLead()
        binding.spFilterBank.setOnItemSelectedListener(object :OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                subject.onNext(IndirectIFADashboardUIEvent.onFilterChanged(binding.spFilterBank.selectedItem.toString()))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        });


    }
    override fun onLeadCreationClicked(): Observable<Unit> {
        return binding.leadcreation.clicks()
    }
    override fun onActionableInsightsClicked(): Observable<Unit> {
        return binding.ivActionableInsights.clicks()
    }

    override fun showInsightsDialog() {
        hideKeyboard(binding.ivActionableInsights)
        val dialogFragment = IFAActionableInsightDialogFragment(navigator)
        dialogFragment.show(requireFragmentManager(), "Actionable Dialog")
    }

    private fun loadTeamTargetAchievementFragment() {
        val leadsFragment = IndirectIFATargetFragment()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llTeamTargetAchievement, leadsFragment)
            .commit()
    }

    private fun fnLoadMyBuisSnapshotip(){

    }
    private fun fnLoadMyBuisSnapshotBroking(){
        val fragment = IndirectIFABuisnessSnapshotFragment()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llBusinessSnapshotHead, fragment)
            .commit()
    }

    private fun fnLoadMyLead()
    {  val fragment = IndirectIFAMyLeadFragment()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llBusinessSnapshotHeadlead, fragment)
            .commit()

    }

    override fun fnRenderFilterList(uiModel: UiModel<List<LookUp>>) {
        uiModel.onSuccess {
            filterAdapter.setSpinnerData(binding.spFilterBank, it)
           // binding.spFilterBank.setSelection(0)
        }
        uiModel.onFailure {
            Timber.d(it)
            showOnFailurePopup(it)

        }
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

}