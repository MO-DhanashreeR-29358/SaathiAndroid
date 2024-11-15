package com.mosl.tabapp.saathiIndirect.prospestMeeting

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectProspestMeetingBinding
import com.mosl.tabapp.databinding.FragIndirectSetAppointmentBinding
import javax.inject.Inject


class IndirectProspestmeetingFragment: CommonFragment(),IndirectProspestMeetingView {

    internal  lateinit var binding:FragIndirectProspestMeetingBinding

    @Inject
    lateinit var presenter: IndirectProspestMeetingPresenter

    override fun getLayout() = R.layout.frag_indirect_prospest_meeting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectProspestMeetingBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

    }
}