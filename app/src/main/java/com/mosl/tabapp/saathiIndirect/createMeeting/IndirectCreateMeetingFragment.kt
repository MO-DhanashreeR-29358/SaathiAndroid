package com.mosl.tabapp.saathiIndirect.createMeeting

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectCreateMeetingBinding
import com.mosl.tabapp.databinding.FragIndirectSetAppointmentBinding
import javax.inject.Inject

class IndirectCreateMeetingFragment:CommonFragment(),IndirectCreateMeetingView {
    internal lateinit var binding:FragIndirectCreateMeetingBinding

    @Inject
    lateinit var presenter: IndirectCreateMeetingPresenter

    override fun getLayout() = R.layout.frag_indirect_create_meeting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectCreateMeetingBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

    }
}