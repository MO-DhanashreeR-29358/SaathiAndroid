package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.cncadmin

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCncAdminDashboardBinding
import javax.inject.Inject

class CncAdminDashboardFragment :CommonFragment(),CncAdminDashboardView {
    internal lateinit var binding:FragIndirectCncAdminDashboardBinding

    @Inject
    lateinit var presenter: CncAdminDashboardPresenter

    override fun getLayout() = R.layout.frag_indirect_cnc_admin_dashboard

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCncAdminDashboardBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

    }

}