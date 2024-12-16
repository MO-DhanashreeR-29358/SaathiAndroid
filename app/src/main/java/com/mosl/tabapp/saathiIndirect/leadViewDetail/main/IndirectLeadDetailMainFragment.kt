package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogIndirectLeadviewBinding
import com.mosl.tabapp.databinding.DialogLeadviewBinding
import com.mosl.tabapp.databinding.FragIndirectLeadviewMainBinding
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailPagerFragment
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailUiEvent
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.data.IndirectLeadDetailMain
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.initiateCall
import com.mosl.tabapp.util.initiateMessage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import javax.inject.Inject


class IndirectLeadDetailMainFragment : CommonFragment(), IndirectLeadDetailMainView {
    private lateinit var binding: FragIndirectLeadviewMainBinding

    @Inject
    internal lateinit var presenter : IndirectLeadDetailMainPresenter

    private val subject = IndirectLeadViewDetailUiEvent.getSubject()

    private var ucId: String = "0"
    private var leadId: String = "0"
    private var initialPage = 0
    private var createMeetingId = 0
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
        CRMAppUtil.setImage(ByteArray(0), binding.includeClient.detail.ivLead, requireContext())
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

    override fun getUcid(): String {
        return  ucId
    }

    override fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetailMain>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            binding.apply {
                createMeetingId = it.createMeetingId
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
                includeClient.detail.tvName.text = it.leadName
                hideAppTitle()
                includeClient.detail.tvLeadId.text = it.id
                includeClient.detail.tvBranchCode.text = it.branchCode
            }
        }
    }

    override fun onThreeVerticalDotsClicked(): Observable<Unit> {
        return binding.includeClient.detail.ivLeadViewArrow.clicks()
    }

    override fun showLeadViewDialog() {
        Dialog(requireContext()).apply {
            val dialogBinding = DialogIndirectLeadviewBinding.inflate(layoutInflater)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialogBinding.ivCreateMeeting.setOnClickListener {
                dismiss()
                subject.onNext(IndirectLeadViewDetailUiEvent.CreateMeetingClick(createMeetingId))
            }
            dialogBinding.ivSetAppointment.setOnClickListener {
                dismiss()
                subject.onNext(IndirectLeadViewDetailUiEvent.SetAppointmentClick)
            }
            dialogBinding.ivCall.setOnClickListener {
                dismiss()
                subject.onNext(IndirectLeadViewDetailUiEvent.CallClick)
            }
            dialogBinding.ivSms.setOnClickListener {
                dismiss()
                subject.onNext(IndirectLeadViewDetailUiEvent.SMSClick)
            }
            dialogBinding.ivClose.setOnClickListener { dismiss() }

        }
            .show()
    }

    override fun onCreateMeetingClicked(): Observable<IndirectLeadViewDetailUiEvent.CreateMeetingClick> {
        return subject.ofType()
    }

    override fun renderCreateMeetingClick(id: Int) {
        val message = when (id) {
            1 -> getString(R.string.mgs_update_address)
            2 -> getString(R.string.mgs_update_profile)
            4 -> LeadViewDetailPagerFragment.PROFILE_ADDRESS
            5 -> LeadViewDetailPagerFragment.PROFILE
            else -> getString(R.string.mgs_update_profile_address)
        }
        erroMsgDialog(message)
    }

    override fun onSetAppointmentClick(): Observable<IndirectLeadViewDetailUiEvent.SetAppointmentClick> {
        return subject.ofType()
    }

    override fun onCallClicked(): Observable<IndirectLeadViewDetailUiEvent.CallClick> {
        return subject.ofType()
    }

    override fun renderCallClick() {
        if (mobile.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(), "Contact number not available",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            MSFDialog.alertDialog(
                requireContext(), 0,
                resources.getString(R.string.motilal_oswal), "Do you want to call?",
                "Yes", "No", true
            ) { action: MSFDialog.Action, _: Array<Any?>? ->
                if (action == MSFDialog.Action.OK) {
                    mobile?.apply { requireContext().initiateCall(this) }
                }
            }
        }
    }

    override fun onSMSClicked(): Observable<IndirectLeadViewDetailUiEvent.SMSClick> {
        return subject.ofType()
    }

    override fun onRetryClick(): Observable<IndirectLeadViewDetailUiEvent.RetryClick> {
        return subject.ofType()
    }

    override fun renderSMSClick() {
        if (mobile.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(), "Contact number not available",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            mobile?.apply { requireContext().initiateMessage(this) }
        }
    }
}