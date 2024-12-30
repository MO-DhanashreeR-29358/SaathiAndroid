package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectIfaDashboardBinding
import com.mosl.tabapp.saathiIndirect.dashboard.IndirectDashboardPresenter
import com.mosl.tabapp.saathiIndirect.dashboard.IndirectDashboardView
import io.reactivex.rxjava3.core.Observable
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

    }
    override fun onLeadCreationClicked(): Observable<Unit> {
        return binding.leadcreation.clicks()
    }

}