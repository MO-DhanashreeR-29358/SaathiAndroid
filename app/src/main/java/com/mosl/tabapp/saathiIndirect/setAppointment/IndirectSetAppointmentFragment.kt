package com.mosl.tabapp.saathiIndirect.setAppointment

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectSetAppointmentBinding
import javax.inject.Inject

class IndirectSetAppointmentFragment:CommonFragment(),IndirectSetAppointmentView  {
    internal lateinit var binding:FragIndirectSetAppointmentBinding
    @Inject
    lateinit var presenter: IndirectSetAppointmentPresenter

    override fun getLayout() = R.layout.frag_indirect_set_appointment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectSetAppointmentBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

    }
}