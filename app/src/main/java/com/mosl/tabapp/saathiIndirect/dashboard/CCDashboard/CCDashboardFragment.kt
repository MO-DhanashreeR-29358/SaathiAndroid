package com.mosl.tabapp.saathiIndirect.dashboard.CCDashboard

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCcDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectDashboardBinding
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CCDashboardFragment : CommonFragment(), CCDashboardView{
    internal lateinit var binding: FragIndirectCcDashboardBinding
    @Inject
    lateinit var presenter: CCDashboardPresenter
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

    }
    override fun onLeadCreationClicked(): Observable<Unit> {
        return binding.leadcreation.clicks()
    }
    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

}