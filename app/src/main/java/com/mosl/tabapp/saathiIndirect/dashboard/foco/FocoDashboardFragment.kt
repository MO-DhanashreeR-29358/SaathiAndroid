package com.mosl.tabapp.saathiIndirect.dashboard.foco

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectFocoDashboardBinding
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.dashboard.base.DashboardFragment
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetFragment
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class FocoDashboardFragment : CommonFragment(), View.OnClickListener,FocoDashboardView{
    internal lateinit var binding: FragIndirectFocoDashboardBinding


    @Inject
    lateinit var presenter: FocoDashboardPresenter
    lateinit var periodAdapter: ArrayAdapter<FocoPeriodModel>
    override fun getLayout() = R.layout.frag_indirect_foco_dashboard
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectFocoDashboardBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {
        periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spPeriod.adapter = periodAdapter
        if (binding.llTargetAchievementADV.visibility != View.VISIBLE) {
            toggleSection(binding.llTargetAchievementADV, binding.ivTargetAchievementADV)
        }
        if (binding.llTargetAchievementADV.visibility != View.VISIBLE) {
            toggleSection(binding.llRunrateforAdvisorADV, binding.ivRunrateforAdvisorHeader)
        }
        loadTargetAchievementFragment()
        loadRunrateForAdvisorFragment()
        binding.rlTargetAchievementADV.setOnClickListener(this)
        binding.rlRunrateForAdvisorADV.setOnClickListener(this)
    }



    override fun renderPeriodFilter(uiModel: UiModel<List<FocoPeriodModel>>) {

        toggleProgress(uiModel.inProgress)

        uiModel.onFailure { error ->
            Timber.d("renderPeriodFilter ${error.message}")
        }


        uiModel.onSuccess {
            periodAdapter.setSpinnerData(binding.spPeriod, it)
        }
    }


    override fun onActionableInsightsClicked(): Observable<Unit> {
        return binding.ivActionableInsights.clicks()
    }

    override fun showInsightsDialog() {
        hideKeyboard(binding.ivActionableInsights)
        val dialogFragment = FocoActionableInsightDialogFragment(navigator)
        dialogFragment.show(requireFragmentManager(), "Actionable Dialog")
    }

    override fun onLeadCreationClicked(): Observable<Unit> {
        return binding.leadcreation.clicks()
    }

    val args = Bundle()
    private fun loadTargetAchievementFragment() {
        val targetFragment = FocoTargetFragment()
        args.putSerializable(DashboardFragment.DASHBOARD_MODEL, "currentDashboard")
        targetFragment.arguments = args
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llTargetAchievementADV, targetFragment)
            .commit()
    }

    private fun loadRunrateForAdvisorFragment() {
        val targetFragment = FocoDashboardReportFragment()
        args.putString(MOSLConstants.SYSTEM_NAME, MOSLConstants.INDIRECT_RUNRATE_FOR_ADVISOR)
        targetFragment.arguments = args
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llRunrateforAdvisorADV, targetFragment)
            .commit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlTargetAchievementADV -> toggleSection(
                binding.llTargetAchievementADV, binding.ivTargetAchievementADV
            )
            R.id.rlRunrateForAdvisorADV -> {
                toggleSection(
                    binding.llRunrateforAdvisorADV, binding.ivRunrateforAdvisorHeader
                )
            }
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