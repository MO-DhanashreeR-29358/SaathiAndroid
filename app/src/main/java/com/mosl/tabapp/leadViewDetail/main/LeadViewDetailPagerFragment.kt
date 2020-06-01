package com.mosl.tabapp.leadViewDetail.main

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.main.ClientDetailAndImage
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.core.EkycModule
import com.mosl.tabapp.databinding.DialogLeadviewBinding
import com.mosl.tabapp.databinding.FragLeadviewMainBinding
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.DiyLinkClickListner
import com.mosl.tabapp.util.initiateCall
import com.mosl.tabapp.util.initiateMessage
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LeadViewDetailPagerFragment : CommonFragment(), LeadViewDetailPagerView, DiyLinkClickListner {

  private lateinit var binding: FragLeadviewMainBinding
  @Inject internal lateinit var presenter: LeadViewDetailPagerPresenter

  private var mobile: String? = null
  private var oaoUserId: String? = null
  private var diyCode: String? = null
  private var diyLink: String? = null
  private var createMeetingId: String? = null
  private var interestedIn: String? = null
  private var ucId: String? = null
  private var email: String? = null
  private var leadOwner: String? = null
  private var sourceName: String? = null
  private var prospectSourceName: String? = null
  private var prospectSourceDetail: String? = null
  private val subject = PublishSubject.create<LeadViewDetailUiEvent>()

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
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
        if (oaoUserId != null && oaoUserId!!.toInt() > 0) {
          dialogBinding.ivOpenAccount.visibility = View.GONE
          dialogBinding.ivOaoForm.visibility = View.VISIBLE
        } else {
          dialogBinding.ivOpenAccount.visibility = View.VISIBLE
          dialogBinding.ivOaoForm.visibility = View.GONE
        }
      }

      if (diyCode!!.toInt() > 0) {
        if (diyLink!!.isNotEmpty()) {
          dialogBinding.view3.visibility = View.VISIBLE
        }
        dialogBinding.ivShareLink.visibility = View.VISIBLE
      } else {
        dialogBinding.ivShareLink.visibility = View.GONE
      }
      if (diyLink!!.isEmpty() || diyCode?.toInt() == 0) {
        dialogBinding.ivCopyLink.visibility = View.GONE
      } else {
        dialogBinding.ivCopyLink.visibility = View.VISIBLE
      }

      dialogBinding.ivOaoForm.setOnClickListener {
        dismiss()
        subject.onNext(OaoFormClick(requireNotNull(oaoUserId)))
      }
      dialogBinding.ivOpenAccount.setOnClickListener {
        dismiss()
        subject.onNext(OpenAccountClick)
      }
      dialogBinding.ivCreateMeeting.setOnClickListener {
        dismiss()
        subject.onNext(CreateMeetingClick(createMeetingId ?: "3"))
      }
      dialogBinding.ivSetAppointment.setOnClickListener {
        dismiss()
        subject.onNext(SetAppointmentClick)
      }
      dialogBinding.ivCall.setOnClickListener {
        dismiss()
        subject.onNext(CallClick)
      }
      dialogBinding.ivSms.setOnClickListener {
        dismiss()
        subject.onNext(SMSClick)
      }
      dialogBinding.ivClose.setOnClickListener { dismiss() }
      dialogBinding.ivShareLink.setOnClickListener {
        dismiss()
        subject.onNext(ShareLinkClick)
      }
      dialogBinding.ivSuggestMe.setOnClickListener {
        dismiss()
        subject.onNext(SuggestMeClick)
      }
      dialogBinding.ivCopyLink.setOnClickListener {
        dismiss()
        subject.onNext(CopyLinkClick)
      }
    }
        .show()
  }

  override fun onCreateMeetingClicked(): Observable<CreateMeetingClick> {
    return subject.ofType()
  }

  override fun renderCreateMeetingClick(id: String) {
    val message = when (id.toInt()) {
      1 -> getString(R.string.mgs_update_address)
      2 -> getString(R.string.mgs_update_profile)
      else -> getString(R.string.mgs_update_profile_address)
    }
    erroMsgDialog(message)
  }

  override fun onCallClicked(): Observable<CallClick> {
    return subject.ofType()
  }

  override fun renderCallClick() {
    if (mobile == null || CRMAppUtil.isBlank(mobile)) {
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
      ) { action: Action, data: Array<Any?>? ->
        if (action == OK) {
          requireContext().initiateCall(mobile!!)
        }
      }
    }
  }

  override fun onSMSClicked(): Observable<SMSClick> {
    return subject.ofType()
  }

  override fun renderSMSClick() {
    if (mobile == null || CRMAppUtil.isBlank(mobile)) {
      Toast.makeText(
          requireContext(), "Contact number not available",
          Toast.LENGTH_LONG
      )
          .show()
    } else {
      requireContext().initiateMessage(mobile!!)
    }
  }

  override fun onSetAppointmentClick(): Observable<SetAppointmentClick> {
    return subject.ofType()
  }

  override fun onOaoFormClicked(): Observable<OaoFormClick> {
    return subject.ofType()
  }

  override fun onOpenAccountClicked(): Observable<OpenAccountClick> {
    return subject.ofType()
  }

  override fun renderOpenAccountClick() {
    gotoEkycPage(
        EkycModule(
            mobile, email, leadOwner, sourceName, prospectSourceName,
            prospectSourceDetail, interestedIn
        )
    )
  }

  override fun onShareLinkClicked(): Observable<ShareLinkClick> {
    return subject.ofType()
  }

  override fun renderOnShareLinkClick(leadId: String) {
    renderDiyLink(leadId, this)
  }

  override fun onSuggestMeClicked(): Observable<SuggestMeClick> {
    return subject.ofType()
  }

  override fun onCopyLinkClicked(): Observable<CopyLinkClick> {
    return subject.ofType()
  }

  override fun renderOnCopyLinkClick() {
    if (diyLink?.isEmpty() == true) {
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
      clipboard.primaryClip = clip
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
        it.leadProducts
            .forEach {
              interestedIn = it.interestedIn
              if (it.isDefault) {
                includeClient.detail.apply {
                  tvClientCode.text = it.clientCode
                  tvCurrentStage.text = it.stageName
                  tvCurrentStageSince.text = "${it.pendingDays} Days"
                }
              }
            }
        ucId = it.personalDetail.ucId
        email = it.personalDetail.email
        mobile = it.personalDetail.mobile
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
        tvName.text = it.clientDetail.name.toUpperCase()
        hideAppTitle()
        tvLeadId.text = it.clientDetail.id
        tvCurrentStage.text = it.clientDetail.currentStage
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

}