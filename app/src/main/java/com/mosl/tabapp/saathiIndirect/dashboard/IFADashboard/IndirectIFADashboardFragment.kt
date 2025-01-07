package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightDialogFragment
import com.mosl.tabapp.dashboard.base.DashboardFragment
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetFragment
import com.mosl.tabapp.databinding.FragIndirectDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectIfaDashboardBinding
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Actionable.IFAActionableInsightDialogFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetFragment
import com.mosl.tabapp.saathiIndirect.dashboard.IndirectDashboardPresenter
import com.mosl.tabapp.saathiIndirect.dashboard.IndirectDashboardView
import com.mosl.tabapp.saathiIndirect.dashboard.foco.FocoActionableInsightDialogFragment
import io.reactivex.rxjava3.core.Observable
import java.util.ArrayList
import javax.inject.Inject

class IndirectIFADashboardFragment  : CommonFragment(), IndirectIFADashboardView {
    internal lateinit var binding: FragIndirectIfaDashboardBinding
    @Inject
    lateinit var presenter: IndirectIFADasboardPresenter
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

        binding.ivTeamTargetAchievement.setOnClickListener {
            toggleSection(
                binding.llTeamTargetAchievement, binding.ivTeamTargetAchievement
            )
        }
        loadTeamTargetAchievementFragment()

    }
    override fun onLeadCreationClicked(): Observable<Unit> {
        return binding.leadcreation.clicks()
    }
    override fun onActionableInsightsClicked(): Observable<Unit> {
        return binding.ivActionableInsights.clicks()
    }

    override fun showInsightsDialog() {
        hideKeyboard(binding.ivActionableInsights)
        val dialogFragment = IFAActionableInsightDialogFragment()
        dialogFragment.show(requireFragmentManager(), "Actionable Dialog")
    }

    private fun loadTeamTargetAchievementFragment() {
        val leadsFragment = IndirectIFATargetFragment()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llTeamTargetAchievement, leadsFragment)
            .commit()
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