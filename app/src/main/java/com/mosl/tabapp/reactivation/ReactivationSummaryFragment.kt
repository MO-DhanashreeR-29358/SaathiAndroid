package com.mosl.tabapp.reactivation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentQrCodeBinding
import com.mosl.tabapp.databinding.FragmentReactivationSummaryBinding
import javax.inject.Inject


class ReactivationSummaryFragment : CommonFragment(), ReactivationSummaryView {

    lateinit var binding : FragmentReactivationSummaryBinding

    @Inject
    lateinit var presenter: ReactivationSummaryPresenter

    override fun getLayout(): Int {
        return R.layout.fragment_reactivation_summary
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReactivationSummaryBinding.bind(view)
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }
}