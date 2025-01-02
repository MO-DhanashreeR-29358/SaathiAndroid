package com.mosl.tabapp.saathiIndirect.dashboard.foco

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectFocoDashboardBinding
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.tabapp.util.SpinnerAdapterFactory
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class FocoDashboardFragment : CommonFragment(), FocoDashboardView{
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

}