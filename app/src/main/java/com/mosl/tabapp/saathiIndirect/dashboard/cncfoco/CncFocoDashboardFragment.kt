package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCncfocoDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectFocoDashboardBinding
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardView
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target.IndirectIFATargetFragment
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target.IndirectCncfocoTargetFragment
import com.mosl.tabapp.util.SpinnerAdapterFactory
import timber.log.Timber
import javax.inject.Inject

class CncFocoDashboardFragment : CommonFragment(),CncFocoDashboardView {
    internal  lateinit var binding:FragIndirectCncfocoDashboardBinding

    @Inject
    lateinit var presenter:CncFocoDashboardPresenter
    private lateinit var filterAdapter: ArrayAdapter<LookUp>
    private val subject =IndirectcncfocoDashboardUIEvent.getFundUiEvent()
    override fun getLayout() = R.layout.frag_indirect_cncfoco_dashboard

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCncfocoDashboardBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {
        filterAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext())
        binding.spFilterBank.adapter = filterAdapter
        loadTeamTargetAchievementFragment()

        binding.ivTargetAchievementADV.setOnClickListener {
            toggleSection(
                binding.llTargetAchievementADV, binding.ivTargetAchievementADV
            )
        }

        binding.spFilterBank.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                IndirectIFADashboardView.strSelectedFilter=binding.spFilterBank.selectedItem.toString()
                subject.onNext(IndirectcncfocoDashboardUIEvent.onFilterChanged(binding.spFilterBank.selectedItem.toString()))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        });

    }

    private fun loadTeamTargetAchievementFragment() {
        val leadsFragment = IndirectCncfocoTargetFragment()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.llTargetAchievementADV, leadsFragment)
            .commit()
    }
    override fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>) {
        uiModel.onSuccess {
            filterAdapter.setSpinnerData(binding.spFilterBank, it.lstPeriods)
             binding.spFilterBank.setSelection(0)
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