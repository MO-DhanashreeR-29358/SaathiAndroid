package com.mosl.tabapp.advancesearch.advanceSearch

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.advancesearch.AdvanceSearchPresenter
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragTabAdvSearchBinding
import javax.inject.Inject

class AdvanceSearchTabFragment :CommonFragment(),AdvanceSearchTabView {
    internal lateinit var binding: FragTabAdvSearchBinding
    @Inject
    lateinit var presenter: AdvanceSearchTabPresenter
    override fun getLayout() = R.layout.frag_tab_adv_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragTabAdvSearchBinding.bind(view)
        hideAppTitle()
        presenter.start()
        setupViews()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {
        binding.radioSelection.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId)
            {
                R.id.radio_btn_ad_lead ->{
                    binding.llClientSearch.visibility=View.GONE
                    binding.llleadSearch.visibility=View.VISIBLE
                }
                R.id.radio_btn_ad_client->{
                    binding.llleadSearch.visibility=View.GONE
                    binding.llClientSearch.visibility=View.VISIBLE
                }

            }
        }
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }
}