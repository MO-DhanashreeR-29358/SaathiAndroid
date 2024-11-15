package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragIndirectLeadviewMainBinding
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import javax.inject.Inject


class IndirectLeadDetailMainFragment : CommonFragment(), IndirectLeadDetailMainView {
    private lateinit var binding: FragIndirectLeadviewMainBinding

    @Inject
    internal lateinit var presenter : IndirectLeadDetailMainPresenter

    private var ucId: String = "0"
    private var leadId: String = "0"
    private var initialPage = 0
    private var leadOwner: String? = null
    private var mobile: String? = null
    private var branchName: String? = null
    private var prospectSourceName: String? = null
    private var clientType: String = ""
    private var interestedIn: String? = null
    private var email: String? = null

    override fun getLayout(): Int {
        return R.layout.frag_indirect_leadview_main
    }

    private val tabArray: Array<String> by lazy {
        resources.getStringArray(R.array.indirectLeadViewDetailTabArray
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectLeadviewMainBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }

    private fun setupView() {
        binding.includeClient.detail.ivLeadViewArrow.visibility = View.VISIBLE
        setupPager()
    }
    private fun setupPager() {

        val newAdapter =
            IndirectLeadDetailMainStateAdapter(activity as AppCompatActivity, leadId, ucId)
        binding.vpLeadView.adapter = newAdapter
        binding.vpLeadView.currentItem = initialPage
        TabLayoutMediator(
            binding.indicatorLeadView,
            binding.vpLeadView
        ) { tab, position ->
            tab.text = tabArray[position]
            tab.tag = position
        }.attach()
        binding.vpLeadView.isUserInputEnabled = false
        binding.vpLeadView.currentItem = 0
    }

    override fun extractArguments(bundle: Bundle) {
        super.extractArguments(bundle)
        leadId = bundle.getString(MOSLConstants.LEADID, "0") ?: "0"
        ucId = bundle.getString(MOSLConstants.UCID, "0") ?: "0"
        val notificationData =
            bundle.getParcelable<NotificationData>(MOSLConstants.NOTIFICATION_DATA)
        if (notificationData?.section == "ACTIVITIES") {
            initialPage = 1
        }
    }

    override fun getLeadId(): String {
        return  leadId
    }

    override fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetailMain>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            binding.apply {
                leadOwner = it.leadOwner
                prospectSourceName = it.prospectSource
                clientType = it.clientType
                it.leadProducts
                    .forEach {
                        interestedIn = it.interestedIn.toString()
                        if (it.isDefault) {
                            includeClient.detail.apply {
                                tvClientCode.text = it.clientCode
                                tvCurrentStage.text = it.stageName
                            }
                        }
                    }
                ucId = stringOrZero(it.personalDetail.ucId)
                email = it.personalDetail.email
                mobile = it.personalDetail.mobile
                branchName = it.branchCode
                includeClient.detail.tvName.text = it.personalDetail.name
                hideAppTitle()
                includeClient.detail.tvLeadId.text = it.id
                includeClient.detail.tvBranchCode.text = it.branchCode
            }
        }
    }
}