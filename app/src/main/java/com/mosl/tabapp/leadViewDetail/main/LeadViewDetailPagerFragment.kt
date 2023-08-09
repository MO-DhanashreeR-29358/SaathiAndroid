package com.mosl.tabapp.leadViewDetail.main

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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.view.clicks
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
import com.mosl.tabapp.databinding.FragLeadviewMainBinding
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.DiyLinkClickListner
import com.mosl.tabapp.util.initiateCall
import com.mosl.tabapp.util.initiateMessage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import javax.inject.Inject


class LeadViewDetailPagerFragment : CommonFragment(), LeadViewDetailPagerView, DiyLinkClickListner {
    private lateinit var binding: FragLeadviewMainBinding
    @Inject
    internal lateinit var presenter: LeadViewDetailPagerPresenter

    private var mobile: String? = null
    private var branchName: String? = null
    private var oaoUserId: Int = 0
    private var diyCode: Int = 0
    private var clientType: String = ""
    private var diyLink: String = ""
    public var createMeetingId: Int = 0
    private var interestedIn: String? = null
    private var ucId: String = "0"
    private var leadId: String = "0"
    private var email: String? = null
    private var leadOwner: String? = null
    private var sourceName: String? = null
    private var prospectSourceName: String? = null
    private var prospectSourceDetail: String? = null
    private var initialPage = 0
    private val subject = LeadViewDetailUiEvent.getSubject()

    private val tabArray: Array<String> by lazy {
        resources.getStringArray(
            if (leadId == "0")
                R.array.leadViewDetailTabArrayWhenLeadIdZero
            else
                R.array.leadViewDetailTabArrayWhenLeadIdNotZero
        )
    }

    override fun getLayout(): Int {
        return R.layout.frag_leadview_main
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragLeadviewMainBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }

    private fun setupView() {
        binding.includeClient.detail.ivLeadViewArrow.visibility = View.VISIBLE
        setupPager()
    }

    private fun setupPager() {
        if (!leadId.equals("0", ignoreCase = true)) {
            CRMAppUtil.setImage(ByteArray(0), binding.includeClient.detail.ivLead, requireContext())
        }
        val newAdapter =
            LeadViewDetailFragmentStateAdapter(activity as AppCompatActivity, leadId, ucId)
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

    override fun onThreeVerticalDotsClicked(): Observable<Unit> {
        return binding.includeClient.detail.ivLeadViewArrow.clicks()
    }

    override fun showLeadViewDialog() {
        Dialog(requireContext()).apply {
            val dialogBinding = DialogLeadviewBinding.inflate(layoutInflater)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (userManager.getUser() != null && userManager.getUser()!!
                    .isAufsb()
            ) {
                dialogBinding.ivCreateMeeting.visibility = View.GONE
                dialogBinding.ivSetAppointment.visibility = View.GONE
                dialogBinding.ivSuggestMe.visibility = View.GONE
            }
            if (userManager.getUser() != null && userManager.getUser()!!
                    .isSMTEnabled()
            ) {
                dialogBinding.view3.visibility = View.VISIBLE
                dialogBinding.ivSuggestMe.visibility = View.VISIBLE
            } else {
                dialogBinding.ivSuggestMe.visibility = View.GONE
            }
            if (userManager.getUser() != null && userManager.getUser()!!
                    .isEkycEnable()
            ) {
                if (oaoUserId > 0) {
                    dialogBinding.ivOpenAccount.visibility = View.GONE
                    dialogBinding.ivOaoForm.visibility = View.VISIBLE
                } else {
                    dialogBinding.ivOpenAccount.visibility = View.VISIBLE
                    dialogBinding.ivOaoForm.visibility = View.GONE
                }
            }

            userManager.getUser()?.apply {
                if (this.isShowRINRI()) {
                    if (clientType.isNotEmpty() && clientType.equals("NRI")) {
                        dialogBinding.ivOpenAccount.visibility = View.GONE
                        dialogBinding.ivOaoForm.visibility = View.GONE
                    }
                }
            }

            if (diyCode > 0) {
                if (diyLink.isNotEmpty()) {
                    dialogBinding.view3.visibility = View.VISIBLE
                }
                dialogBinding.ivShareLink.visibility = View.VISIBLE
            } else {
                dialogBinding.ivShareLink.visibility = View.GONE
            }
            if (diyLink.isEmpty() || diyCode == 0) {
                dialogBinding.ivCopyLink.visibility = View.GONE
            } else {
                dialogBinding.ivCopyLink.visibility = View.VISIBLE
            }

            dialogBinding.ivOaoForm.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.OaoFormClick(oaoUserId))
            }
            dialogBinding.ivOpenAccount.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.OpenAccountClick)
            }
            dialogBinding.ivCreateMeeting.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.CreateMeetingClick(createMeetingId))
            }
            dialogBinding.ivSetAppointment.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.SetAppointmentClick)
            }
            dialogBinding.ivCall.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.CallClick)
            }
            dialogBinding.ivSms.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.SMSClick)
            }
            dialogBinding.ivClose.setOnClickListener { dismiss() }
            dialogBinding.ivShareLink.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.ShareLinkClick)
            }
            dialogBinding.ivSuggestMe.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.SuggestMeClick)
            }
            dialogBinding.ivCopyLink.setOnClickListener {
                dismiss()
                subject.onNext(LeadViewDetailUiEvent.CopyLinkClick)
            }
        }
            .show()
    }

    override fun onCreateMeetingClicked(): Observable<LeadViewDetailUiEvent.CreateMeetingClick> {
        return subject.ofType()
    }

    override fun renderCreateMeetingClick(id: Int) {
        val message = when (id) {
            1 -> getString(string.mgs_update_address)
            2 -> getString(string.mgs_update_profile)
            4 -> PROFILE_ADDRESS
            5 -> PROFILE
            else -> getString(string.mgs_update_profile_address)
        }
        erroMsgDialog(message)
    }

    override fun onCallClicked(): Observable<LeadViewDetailUiEvent.CallClick> {
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
                resources.getString(string.motilal_oswal), "Do you want to call?",
                "Yes", "No", true
            ) { action: Action, _: Array<Any?>? ->
                if (action == OK) {
                    mobile?.apply { requireContext().initiateCall(this) }
                }
            }
        }
    }

    override fun getUcid(): String = if (ucId.isEmpty()) "0" else ucId

    override fun getLeadId(): String {
        return leadId
    }

    override fun onSMSClicked(): Observable<LeadViewDetailUiEvent.SMSClick> {
        return subject.ofType()
    }

    override fun onRetryClick(): Observable<LeadViewDetailUiEvent.RetryClick> {
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

    override fun onUpdateCreateMeetingId(): Observable<LeadViewDetailUiEvent.UpdateCreateMeetingId> {
        return subject.ofType()
    }

    override fun renderUpdateCreateMeetingId(id: Int) {

        createMeetingId = id
        Log.d("AAA setted MeetingID", createMeetingId.toString())
    }

    override fun onSetAppointmentClick(): Observable<LeadViewDetailUiEvent.SetAppointmentClick> {
        return subject.ofType()
    }

    override fun onOaoFormClicked(): Observable<LeadViewDetailUiEvent.OaoFormClick> {
        return subject.ofType()
    }

    override fun onOpenAccountClicked(): Observable<LeadViewDetailUiEvent.OpenAccountClick> {
        return subject.ofType()
    }

    override fun renderOpenAccountClick() {
        gotoEkycPage(
            EkycModule(
                mobile, branchName, email, leadOwner, sourceName, prospectSourceName,
                prospectSourceDetail, interestedIn, oaoUserId.toString(), leadOwner
            )
        )
    }

    override fun onShareLinkClicked(): Observable<LeadViewDetailUiEvent.ShareLinkClick> {
        return subject.ofType()
    }

    override fun renderOnShareLinkClick(leadId: String) {
        renderDiyLink(leadId, this)
    }

    override fun onSuggestMeClicked(): Observable<LeadViewDetailUiEvent.SuggestMeClick> {
        return subject.ofType()
    }

    override fun onCopyLinkClicked(): Observable<LeadViewDetailUiEvent.CopyLinkClick> {
        return subject.ofType()
    }

    override fun renderOnCopyLinkClick() {
        if (diyLink.isEmpty()) {
            Toast.makeText(
                requireContext(), string.txt_lable_link_error_msg,
                Toast.LENGTH_LONG
            )
                .show()

        } else {
            val clipboard = requireContext().getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager
            val clip = ClipData.newPlainText("label", diyLink)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), string.txt_lable_link_msg, Toast.LENGTH_LONG)
                .show()

        }
    }

    override fun renderOnSuggestMeClick(uiModel: UiModel<String>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
    }

    override fun renderLeadDetail(uiModel: UiModel<LeadDetail>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            binding.apply {
                createMeetingId = it.createMeetingId
                leadOwner = it.leadOwner
                sourceName = it.source
                prospectSourceName = it.prospectSource
                prospectSourceDetail = it.prospectSourceDetail
                oaoUserId = it.oaoUserId
                diyCode = it.diyCode
                diyLink = it.diyLink
                clientType = it.clientType
                it.leadProducts
                    .forEach {
                        interestedIn = it.interestedIn
                        if (it.isDefault) {
                            includeClient.detail.apply {
                                tvClientCode.text = it.clientCode
                                tvCurrentStage.text = it.stageName
                                tvCurrentStageSince.text =
                                    String.format("%s %s", it.pendingDays, "Days")
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

    override fun renderClientDetailAndImage(uiModel: UiModel<ClientDetailAndImage>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            binding.includeClient.detail.apply {
                tvName.text = it.clientDetail.name.toUpperCase(Locale.ROOT)
                hideAppTitle()
                tvLeadId.text = it.clientDetail.id
                tvCurrentStage.text = it.clientDetail.currentStage
                if (it.clientDetail.currentStageSince.isNotEmpty()) {
                    tvCurrentStageSince.text = String.format(
                        "%s Days",
                        it.clientDetail.currentStageSince
                    )
                }
                tvClientCode.text = it.clientDetail.clientCode
                tvBranchCode.text = it.clientDetail.branchCode
                mobile = it.clientDetail.mobile
                oaoUserId = it.clientDetail.oaoUserId
                val byte = Base64.decode(it.image, Base64.DEFAULT)
                CRMAppUtil.setImage(
                    when {
                        byte != null -> byte
                        else -> byteArrayOf()
                    },
                    ivLead, requireContext()

                )
            }
        }
    }

    override fun getDiyUrl(url: String) {
        diyLink = url
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

    override fun onFragmentReopened(args: Bundle) {
        super.onFragmentReopened(args)
        leadId = args.getString(MOSLConstants.LEADID, "0") ?: "0"
        ucId = args.getString(MOSLConstants.UCID, "0") ?: "0"
        setupView()
        subject.onNext(LeadViewDetailUiEvent.RetryClick)
    }

    companion object {
        const val PROFILE_ADDRESS =
            "Kindly update address and all Profile Fields(BrokingProfile, IPProfile, FinalProfile)"
        const val PROFILE =
            "Kindly update all Profile Fields (BrokingProfile, IPProfile, FinalProfile)"
    }

}