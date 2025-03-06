package com.mosl.tabapp.advancesearch

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction
import com.mosl.tabapp.R
import com.mosl.tabapp.advancesearch.advanceSearch.AdvanceSearchTabFragment
import com.mosl.tabapp.advancesearch.quickSearch.QuickSearchTabFragment
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragAdvanceSearchBinding
import java.util.Date
import javax.inject.Inject

class AdvanceSearchFragment:CommonFragment(),AdvanceSearchView {

    internal lateinit var binding:FragAdvanceSearchBinding
    @Inject
    lateinit var presenter:AdvanceSearchPresenter
    override fun getLayout() = R.layout.frag_advance_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragAdvanceSearchBinding.bind(view)
        hideAppTitle()
        presenter.start()
        setupViews()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {
        val adapter= VIewPagerAdapter(requireFragmentManager())
        adapter?.addFragment(QuickSearchTabFragment(),"Quick Search")
        adapter?.addFragment(AdvanceSearchTabFragment(),"Advance Search")
        binding.viewpagerAdvanceSearch.adapter=adapter
        binding.tabLayoutAdvanceSearch.setupWithViewPager(binding.viewpagerAdvanceSearch)

    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }
}