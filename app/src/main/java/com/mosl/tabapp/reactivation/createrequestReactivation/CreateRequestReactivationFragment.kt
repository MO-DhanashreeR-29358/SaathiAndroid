package com.mosl.tabapp.reactivation.createrequestReactivation

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.core.CommonFragment
import javax.inject.Inject
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.FragmentCreateRequestReactivationBinding
import com.mosl.tabapp.reactivation.createrequestReactivationDetails.ReactivationDetailsFragment

class CreateRequestReactivationFragment : CommonFragment(),CreateRequestReactivationView {

    lateinit var binding:FragmentCreateRequestReactivationBinding

    @Inject
    lateinit var presenter: CreateRequestReactivationPresenter

    override fun getLayout(): Int {
       return R.layout.fragment_create_request_reactivation
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateRequestReactivationBinding.bind(view)
        setupView()
        presenter.start()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    private fun setupView()
    {
            val showPopup=ReactivationDetailsFragment()
            showPopup.show(childFragmentManager,"leadDetailsview")
    }

}