package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.main.ClientDetailAndImage
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.core.EkycModule
import com.mosl.tabapp.databinding.DialogLeadviewBinding
import com.mosl.tabapp.databinding.FragIndirectLeadviewMainBinding
import com.mosl.tabapp.databinding.FragLeadviewMainBinding
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailFragmentStateAdapter
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.saathiIndirect.leadViewDetail.domain.IndirectLeadDetail
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.DiyLinkClickListner
import com.mosl.tabapp.util.initiateCall
import com.mosl.tabapp.util.initiateMessage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject


class IndirectLeadDetailMainFragment : CommonFragment(), IndirectLeadDetailMainView {
    private lateinit var binding: FragIndirectLeadviewMainBinding

    @Inject
    internal lateinit var presenter : IndirectLeadDetailMainPresenter

    private var ucId: String = "0"
    private var leadId: String = "0"
    private var initialPage = 0

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

    override fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetail>) {

    }
}