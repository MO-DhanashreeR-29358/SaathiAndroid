package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCncfocoDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectFocoDashboardBinding
import javax.inject.Inject

class CncFocoDashboardFragment : CommonFragment(),CncFocoDashboardView {
    internal  lateinit var binding:FragIndirectCncfocoDashboardBinding

    @Inject
    lateinit var presenter:CncFocoDashboardPresenter

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

    }

}