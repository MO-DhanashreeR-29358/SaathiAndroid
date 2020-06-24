package com.mosl.tabapp.leadViewDetail.leaddetail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.focusChanges
import com.jakewharton.rxbinding3.view.touches
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.coreui.spinner.setSpinnerDataWithSelection
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.tabapp.R
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.common.CustomDatePickerDialog
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.common.StringStuff
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogUpdateProfileBinding
import com.mosl.tabapp.databinding.FragmentLeadDetailBinding
import com.mosl.tabapp.leadViewDetail.LeadViewDetailFragment.LeadDetailAddProductAdapter
import com.mosl.tabapp.leadViewDetail.PortfolioSnapshotAssetAdapter
import com.mosl.tabapp.leadViewDetail.PortfolioSnapshotBrokAdapter
import com.mosl.tabapp.leadViewDetail.PortfolioSnapshotCashAdapter
import com.mosl.tabapp.leadcreation.domain.LeadDetailPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leaddetail.ClientDetail
import com.mosl.tabapp.leaddetail.ClientDetailWithPortfolioAndProfitLoss
import com.mosl.tabapp.leaddetail.ClientPortfolio
import com.mosl.tabapp.leaddetail.ClientProfitLoss
import com.mosl.tabapp.leaddetail.Contact
import com.mosl.tabapp.leaddetail.LeadDetail
import com.mosl.tabapp.leaddetail.LeadDetailAndProfession
import com.mosl.tabapp.leaddetail.LeadProductMap
import com.mosl.tabapp.model.LeadFieldSectionModel
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.disableErrorMessages
import com.mosl.tabapp.util.emptyString
import com.mosl.tabapp.util.gone
import com.mosl.tabapp.util.setErrorMessages
import com.mosl.tabapp.util.setSpinnerData
import com.mosl.tabapp.util.string
import com.mosl.tabapp.util.visible
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList
import javax.inject.Inject

class LeadDetailFragment : CommonFragment(), LeadDetailView {

  @Inject internal lateinit var presenter: LeadDetailPresenter
  private lateinit var binding: FragmentLeadDetailBinding
  private var isReferee = false

  private var leadId = "0"
  private var ucId = "0"

  private var stage: String? = null
  private var status: String? = null

  private val subject = PublishSubject.create<LeadDetailUiEvent>()

  private val professionAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  private val profileAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  private val interestedAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  private val stageAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  private val statusAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  private val pmsProductAdapter: ArrayAdapter<String> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<String>(requireContext())
  }

  private val countryAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  private val stateAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }
  private val cityAdapter: ArrayAdapter<RowData> by lazy {
    SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
  }

  override fun getLayout(): Int {
    return R.layout.fragment_lead_detail
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentLeadDetailBinding.bind(view)
    setupView()
    presenter.start()
    dataActionSubject.onNext(Fetch)
  }

  private fun setupView() {
    with(binding) {
      nestedScrollView.apply {
        descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        isFocusable = true
        isFocusableInTouchMode = true
      }
      setIcon(llClientDetail, tvClientDetail)
      setIcon(llLeadDetail, tvLeadDetail)
      setIcon(llClientPortfolio, tvClientPortfolioDetails)
      etDob.inputType = InputType.TYPE_NULL
      etAnniversary.inputType = InputType.TYPE_NULL
      etPanNumber.filters = arrayOf(
          InputFilter.LengthFilter(10),
          InputFilter.AllCaps()
      )
      includePortfolio.apply {
        btnBrokPortfolio.gone()
        btnAssetPortfolio.gone()
        llPortfolio.visible()
        llBorkingPortfolio.visible()
        llCash.visible()
        llAssestPortfolio.visible()
        setIcon(llPortfolio, txtPortfolioSnapshot, "OUTER")
        setIcon(llProfitLoss, txtPorfitLoss, "OUTER")
        setIcon(llAvailableLimits, txtAvailableLimits, "OUTER")

        setIcon(llBorkingPortfolio, txtBrokingPortfolio, "INNER")
        setIcon(llAssestPortfolio, txtAssestPortfolio, "INNER")
        setIcon(llCash, txtCash, "INNER")

        setIcon(llBorkingProducts, txtBrokingProducts, "INNER")
        setIcon(llAssestProducts, txtAssestProducts, "INNER")
      }
    }
  }

  private fun setIcon(
    parent: View,
    icon: TextView,
    Section: String
  ) {
    if (Section.equals("OUTER", ignoreCase = true)) {
      if (parent.visibility == View.VISIBLE) {
        icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_minus, 0)
      } else {
        icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_plus, 0)
      }
    } else if (Section.equals("INNER", ignoreCase = true)) {
      if (parent.visibility == View.VISIBLE) {
        icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.up_button, 0)
      } else {
        icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.down_button, 0)
      }
    }
  }

  override fun renderArguments(data: LeadDetailArgument) {
    leadId = data.leadId
    ucId = data.ucId
  }

  override fun onNestedScrollViewTouched(): Observable<MotionEvent> {
    return binding.nestedScrollView.touches { false }
  }

  override fun renderOnNestedScrollViewTouch() {
    binding.nestedScrollView.requestFocusFromTouch()
  }

  override fun onPrimaryAddCheckboxClicked(): Observable<Unit> {
    return binding.cbPrimaryAddress.clicks()
  }

  override fun onOfficePrimaryAddCheckboxClicked(): Observable<Unit> {
    return binding.cbOffPrimaryAddress.clicks()
  }

  override fun onSameAsCheckboxClicked(): Observable<Unit> {
    return binding.cbSameAs.clicks()
  }

  override fun onSubmitButtonClicked(): Observable<Unit> {
    return binding.btnSubmit.clicks()
  }

  override fun onProfileLayoutClicked(): Observable<Unit> {
    return binding.llProfile.clicks()
  }

  override fun onUpdateProfileEventFired(): Observable<UpdateProfile> {
    return subject.ofType()
  }

  override fun renderUpdateProfile(uiModel: UiModel<Pair<String, String>>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess {
      if (it.first.isEmpty())
        binding.tvProfile.text = it.second
      else
        MSFDialog.alertDialog(
            requireContext(), 0, getString(R.string.motilal_oswal), it.first
            , "OK", true, null
        )
    }
  }

  override fun renderProfileClick() {
    Dialog(requireContext()).apply {
      val dialogBinding = DialogUpdateProfileBinding.inflate(layoutInflater)
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      setCanceledOnTouchOutside(false)
      setContentView(dialogBinding.root)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      dialogBinding.spProfile.apply {
        adapter = profileAdapter
      }
      dialogBinding.ivClose.setOnClickListener { dismiss() }
      dialogBinding.btnUpdate.setOnClickListener {
        val selectedProfile = dialogBinding.spProfile.selectedItem as RowData
        subject.onNext(UpdateProfile(selectedProfile))
        dismiss()
      }

    }
        .show()
  }

  override fun onAddProductClicked(): Observable<Unit> {
    return binding.rlAddProduct.clicks()
  }

  override fun onEditLeadDetailClicked(): Observable<Unit> {
    return binding.llEditLeadDetail.clicks()
  }

  private var editableFlag = true

  override fun renderEditLeadDetailClick() {
    if (leadId != "0" && isLeadEditable) {
      if (editableFlag) {
        setEnableDisable(true)
        editableFlag = false
      } else {
        setEnableDisable(false)
        editableFlag = true
      }
    }
  }

  override fun onClientDetailClicked(): Observable<Unit> {
    return binding.tvClientDetail.clicks()
  }

  override fun onClientPortfolioDetailClicked(): Observable<Unit> {
    return binding.tvClientPortfolioDetails.clicks()
  }

  override fun onLeadDetailClicked(): Observable<Unit> {
    return binding.tvLeadDetail.clicks()
  }

  override fun onDocumentClicked(): Observable<Unit> {
    return binding.tvDocuments.clicks()
  }

  override fun onDOBClicked(): Observable<Unit> {
    return binding.etDob.clicks()
  }

  override fun renderDOBClick() {
    CustomDatePickerDialog(
        requireContext(),
        false,
        binding.etDob.text.toString(),
        true
    )
    { date ->
      binding.tvAnniversaryError.visibility = View.GONE
      binding.etDob.setText(date)
      binding.etDob.setClearIconVisible(true)
    }.show()
  }

  override fun renderAnniversaryClick() {
    CustomDatePickerDialog(
        requireContext(),
        false,
        binding.etAnniversary.text.toString(),
        true
    )
    { date ->
      binding.tvAnniversaryError.visibility = View.GONE
      binding.etAnniversary.setText(date)
      binding.etAnniversary.setClearIconVisible(true)
    }.show()
  }

  override fun onAnniversaryClicked(): Observable<Unit> {
    return binding.etAnniversary.clicks()
  }

  override fun onRefereeCodeFocusChanged(): Observable<Boolean> {
    return binding.etRefereeCode.focusChanges()
  }

  override fun onClientDetailSectionClicked(): Observable<Unit> {
    return binding.tvClientDetail.clicks()
  }

  override fun onClientPortfolioSectionClicked(): Observable<Unit> {
    return binding.tvClientPortfolioDetails.clicks()
  }

  override fun onLeadDetailSectionClicked(): Observable<Unit> {
    return binding.tvLeadDetail.clicks()
  }

  override fun toggleClientDetailSection() {
    toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
  }

  override fun toggleLeadDetailSection() {
    toggleSectionExpansion(binding.llLeadDetail, binding.tvLeadDetail)
  }

  override fun toggleClientPortfolioSection() {
    toggleSectionExpansion(binding.llClientPortfolio, binding.tvClientPortfolioDetails)
  }

  override fun renderOnRefereeCodeFocusChange() {
    if (binding.etRefereeCode.string().length < 4) {
      binding.etRefereeName.setText(emptyString())
      binding.etRefereeEmail.setText(emptyString())
      binding.etRefereeNumber.setText(emptyString())
    }
  }

  override fun onRefereeNameFocusChanged(): Observable<Boolean> {
    return binding.etRefereeName.focusChanges()
  }

  override fun renderOnRefereeNameFocusChange() {
    if (binding.etRefereeName.string().length < 3) {
      binding.etRefereeCode.setText(emptyString())
      binding.etRefereeEmail.setText(emptyString())
      binding.etRefereeNumber.setText(emptyString())
    }
  }

  override fun onLeadNameAfterTextChanged(): Observable<String> {
    return binding.etLeadName.afterTextChangeEvents()
        .map { it.editable?.toString() ?: emptyString() }
  }

  override fun renderOnLeadNameAfterTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
              text
          ) -> binding.tlLeadName.disableErrorMessages()
          else -> {
            binding.tlLeadName.setErrorMessages(MOSLConstants.ErrorMsgName)
          }
        }
      }
      else -> {
        binding.tlLeadName.disableErrorMessages()
      }
    }
  }

  override fun onProsRefereeNameAfterTextChanged(): Observable<String> {
    return binding.etProspectRefereeName.afterTextChangeEvents()
        .map { it.editable?.toString() ?: emptyString() }
  }

  override fun renderOnProsRefereeNameAfterTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
              text
          ) -> binding.tlProspectRefereeName.disableErrorMessages()
          else -> {
            binding.tlProspectRefereeName.setErrorMessages(MOSLConstants.ErrorMsgName)
          }
        }
      }
      else -> {
        binding.tlProspectRefereeName.disableErrorMessages()
      }
    }
  }

  override fun onProsRefereeMobileTextChanged(): Observable<String> {
    return binding.etProspectRefereerMobile.textChanges()
        .map { it.toString() }
  }

  override fun renderOnProsRefereeMobileTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
              text
          ) -> binding.tlProspectRefereeMob.disableErrorMessages()
          else -> {
            binding.tlProspectRefereeMob.setErrorMessages(MOSLConstants.ErrorMsgName)
          }
        }
      }
      else -> {
        binding.tlProspectRefereeMob.disableErrorMessages()
      }
    }
  }

  override fun onProsRefereeEmailTextChanged(): Observable<String> {
    return binding.etProspectRefereerEmail.textChanges()
        .map { it.toString() }
  }

  override fun renderOnProsRefereeEmailTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          CRMValidationRule.isValidEmail(
              text
          ) -> binding.tlProspectRefereeEmail.disableErrorMessages()
          else -> {
            binding.tlProspectRefereeEmail.setErrorMessages(MOSLConstants.INVALID_EMAIL_ID)
          }
        }
      }
      else -> {
        binding.tlProspectRefereeEmail.disableErrorMessages()
      }
    }
  }

  override fun onContactPersonAfterTextChanged(): Observable<String> {
    return binding.etContactPerson.afterTextChangeEvents()
        .map { it.editable?.toString() ?: emptyString() }
  }

  override fun renderOnContactPersonAfterTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
              text
          ) -> binding.tlContactPerson.disableErrorMessages()
          else -> {
            binding.tlContactPerson.setErrorMessages(MOSLConstants.ErrorMsgName)
          }
        }
      }
      else -> {
        binding.tlContactPerson.disableErrorMessages()
      }
    }
  }

  override fun onLandLineAfterTextChanged(): Observable<String> {
    return binding.etLandlineNumber.afterTextChangeEvents()
        .map { it.editable?.toString() ?: emptyString() }
  }

  override fun renderOnLandLineAfterTextChange(text: String) {
    if (binding.etLandlineNumber.isEnabled) {
      when {
        text.isNotBlank() -> {
          when {
            CRMValidationRule.validateTel(
                text
            ) -> binding.tlLand.disableErrorMessages()
            else -> {
              binding.tlLand.setErrorMessages(MOSLConstants.INVALID_LAND_LINE)
            }
          }
        }
        else -> {
          binding.tlLand.disableErrorMessages()
        }
      }
    }
  }

  override fun onAlterMobileTextChanged(): Observable<String> {
    return binding.etAlternateMobileNumber.textChanges()
        .map { it.toString() }
  }

  override fun renderOnAlterMobileTextChange(text: String) {
    if (binding.etAlternateMobileNumber.isEnabled) {
      when {
        text.isNotBlank() -> {
          when {
            CRMValidationRule.validateMobile(
                text
            ) -> binding.tlAlternateMobile.disableErrorMessages()
            else -> {
              binding.tlAlternateMobile.setErrorMessages(MOSLConstants.INVALID_MOBILE_NO)
            }
          }
        }
        else -> {
          binding.tlAlternateMobile.disableErrorMessages()
        }
      }
    }
  }

  override fun onAlterEmailTextChanged(): Observable<String> {
    return binding.etAlternateEmail.textChanges()
        .map { it.toString() }
  }

  override fun renderOnAlterEmailTextChange(text: String) {
    if (binding.llEditLeadDetail.isEnabled) {
      when {
        text.isNotBlank() -> {
          when {
            CRMValidationRule.isValidEmail(
                text
            ) -> binding.tlAlternateEmail.disableErrorMessages()
            else -> {
              binding.tlAlternateEmail.setErrorMessages(MOSLConstants.INVALID_EMAIL_ID)
            }
          }
        }
        else -> {
          binding.tlAlternateEmail.disableErrorMessages()
        }
      }
    }
  }

  override fun onEmailTextChanged(): Observable<String> {
    return binding.etEmail.textChanges()
        .map { it.toString() }
  }

  override fun renderOnEmailTextChange(text: String) {
    if (binding.llEditLeadDetail.isEnabled) {
      when {
        text.isNotBlank() -> {
          when {
            CRMValidationRule.isValidEmail(
                text
            ) -> binding.tlEmail.disableErrorMessages()
            else -> {
              binding.tlEmail.setErrorMessages(MOSLConstants.INVALID_EMAIL_ID)
            }
          }
        }
        else -> {
          binding.tlEmail.disableErrorMessages()
        }
      }
    }
  }

  override fun onPanTextChanged(): Observable<String> {
    return binding.etPanNumber.textChanges()
        .map { it.toString() }
  }

  override fun renderOnPanTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          CRMValidationRule.isValidPannumber(
              text
          ) -> binding.tlPan.disableErrorMessages()
          else -> {
            binding.tlPan.setErrorMessages(MOSLConstants.INVALID_PAN)
          }
        }
      }
      else -> {
        binding.tlPan.disableErrorMessages()
      }
    }
  }

  private var advisoryCode = ""

  override fun renderClientDetail(uiModel: UiModel<ClientDetail>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess { data ->

      if (data.products.isNotEmpty()) {
        val product = data.products[0]
        binding.tvClientCode.text = product.clientCode
        binding.tvUCID.text = product.ucId
        binding.tvBranchCode.text = product.branchCode
        binding.tvChannel.text = product.channel
        advisoryCode = product.advisorCode
      }

      binding.tvClientAddress.text = data.address
      binding.tvClientName.text = data.name
      binding.tvPanCard.text = data.pan
      binding.tvAccountType.text = data.accountType
      binding.tvFamilyUCID.text = data.familyUcid
      binding.tvAdvisoryType.text = data.advisoryType
      binding.tvAadharCard.text = data.aadhar
      binding.tvAccountNumber.text = data.account
      binding.tvProfile.text = data.profile
      binding.tvDefaultBank.text = data.defaultBank

      binding.llClient.visibility = View.VISIBLE
      binding.llClientPortfolioDetails.visibility = View.VISIBLE
      toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
    }
  }

  private fun ClientDetail.renderClient() {
    if (products.isNotEmpty()) {
      val product = products[0]
      binding.tvClientCode.text = product.clientCode
      binding.tvUCID.text = product.ucId
      binding.tvBranchCode.text = product.branchCode
      binding.tvChannel.text = product.channel
      advisoryCode = product.advisorCode
    }

    binding.tvClientAddress.text = address
    binding.tvClientName.text = name
    binding.tvPanCard.text = pan
    binding.tvAccountType.text = accountType
    binding.tvFamilyUCID.text = familyUcid
    binding.tvAdvisoryType.text = advisoryType
    binding.tvAadharCard.text = aadhar
    binding.tvAccountNumber.text = account
    binding.tvProfile.text = profile
    binding.tvDefaultBank.text = defaultBank

    binding.llClient.visibility = View.VISIBLE
    binding.llClientPortfolioDetails.visibility = View.VISIBLE
    toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
  }

  override fun renderClientDetailWithPortfolioAndProfitLoss(uiModel: UiModel<ClientDetailWithPortfolioAndProfitLoss>) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess { data ->
      data.detail.renderClient()
      data.portfolio.renderPortfolio()
      data.profitLoss.renderProfit()

    }
  }

  private fun ClientPortfolio.renderPortfolio() {
    binding.includePortfolio.apply {
      networth.text = StringStuff.commaDecorator(netWorth)
      daychgamt.text = StringStuff.commaDecorator(dayChangeAmount)
      daychgperst.text = "${StringStuff.commaDecorator(dayChangePer)} %%"

      overallchgamt.text = StringStuff.commaDecorator(overallChangeAmt)
      overallchgperst.text = "${StringStuff.commaDecorator(overallChangePer)} %%"

      val bgColor = if (dayChangeAmount.startsWith("-")) {
        color.action_bar_negative
      } else {
        color.action_bar_positive
      }
      daychgamt.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColor))

      val bgColorPer = if (dayChangePer.startsWith("-")) {
        color.action_bar_negative
      } else {
        color.action_bar_positive
      }
      daychgperst.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorPer))

      val bgColorOverall = if (overallChangeAmt.startsWith("-")) {
        color.action_bar_negative
      } else {
        color.action_bar_positive
      }
      overallchgamt.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorOverall))

      val bgColorOverallPer = if (overallChangePer.startsWith("-")) {
        color.action_bar_negative
      } else {
        color.action_bar_positive
      }
      overallchgperst.setBackgroundColor(
          ContextCompat.getColor(requireContext(), bgColorOverallPer)
      )

      ///////////broking portfolio

      val brokingMapList = mutableListOf<HashMap<String, String>>()
      brokingPortfolios.forEach {
        val map = hashMapOf(
            MOSLConstants.PRODUCT to it.productName,
            MOSLConstants.INVEST_AMT to StringStuff.commaDecorator(it.investmentValue),
            MOSLConstants.PURCHASE_VALUE to StringStuff.commaDecorator(it.marketValue),
            MOSLConstants.OVERALL_GAIN_LOSS to StringStuff.commaDecorator(it.overallGainLoss),
            MOSLConstants.PERCENTAGE_GAIN_LOSS to StringStuff.commaDecorator(
                "${it.percentageGainLoss} %"
            ),
            MOSLConstants.PRODUCT_ID to it.productId
        )
        brokingMapList.add(map)
      }

      val brokingAdapter =
        PortfolioSnapshotBrokAdapter(requireContext(), brokingMapList as ArrayList)
      listBrokingPortfolio.adapter = brokingAdapter

      //////////asset portfolio

      val assetMapList = mutableListOf<HashMap<String, String>>()
      assetPortfolios.forEach {
        val map = hashMapOf(
            MOSLConstants.PRODUCT to it.productName,
            MOSLConstants.INVEST_AMT to StringStuff.commaDecorator(it.marketValue),
            MOSLConstants.PURCHASE_VALUE to StringStuff.commaDecorator(it.purchaseValue),
            MOSLConstants.OVERALL_GAIN_LOSS to StringStuff.commaDecorator(it.overallGainLoss),
            MOSLConstants.PERCENTAGE_GAIN_LOSS to StringStuff.commaDecorator(
                "${it.percentageGainLoss} %"
            ),
            MOSLConstants.PRODUCT_ID to it.productId,
            MOSLConstants.PRODUCT_TYPE to it.productType
        )
        assetMapList.add(map)
      }

      val assetAdapter = PortfolioSnapshotAssetAdapter(requireContext(), assetMapList as ArrayList)
      listAssestPortfolio.adapter = assetAdapter

      ///////////Ledger

      val ledgerMapList = mutableListOf<HashMap<String, String>>()
      availableLedgers.forEach {
        val map = hashMapOf(
            MOSLConstants.PRODUCT to it.productName,
            MOSLConstants.TOTAL to StringStuff.commaDecorator(it.balanceLedger)
        )
        ledgerMapList.add(map)
      }

      val ledgerAdapter = PortfolioSnapshotCashAdapter(requireContext(), ledgerMapList as ArrayList)
      listAssestPortfolio.adapter = ledgerAdapter
    }
  }

  private fun ClientProfitLoss.renderProfit() {

  }

  override fun onAadharAfterTextChanged(): Observable<String> {
    return binding.etAadharNumber.afterTextChangeEvents()
        .map { it.editable?.toString() ?: emptyString() }
  }

  override fun renderOnAadharAfterTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          CRMValidationRule.validateAadhar(
              text
          ) -> binding.tlAadhar.disableErrorMessages()
          else -> {
            binding.tlAadhar.setErrorMessages(MOSLConstants.INVALID_AADHAR)
          }
        }
      }
      else -> {
        binding.tlAadhar.disableErrorMessages()
      }
    }
  }

  override fun onPinTextChanged(): Observable<String> {
    return binding.etPincode.textChanges()
        .map { it.toString() }
  }

  override fun renderOnPinTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          text.length < 6 -> binding.tlPin.setErrorMessages(MOSLConstants.INVALID_PINCODE_NUMBER)
          else -> binding.tlPin.disableErrorMessages()
        }
      }
      else -> {
        binding.tlPin.disableErrorMessages()
      }

    }
    if (binding.cbSameAs.isChecked) {
      binding.etOffPincode.setText(text)
    }
  }

  override fun onOfficePinTextChanged(): Observable<String> {
    return binding.etOffPincode.textChanges()
        .map { it.toString() }
  }

  override fun renderOnOfficePinTextChange(text: String) {
    when {
      text.isNotBlank() -> {
        when {
          text.length < 6 -> binding.tlOffPin.setErrorMessages(MOSLConstants.INVALID_PINCODE_NUMBER)
          else -> binding.tlOffPin.disableErrorMessages()
        }
      }
      else -> {
        binding.tlOffPin.disableErrorMessages()
      }

    }
  }

  override fun isCbSameAsChecked(): Boolean {
    return binding.cbSameAs.isChecked
  }

  override fun onAdd1AfterTextChanged(): Observable<String> {
    return binding.etAdd1.textChanges()
        .map { it.toString() }
  }

  override fun renderOnAdd1AfterTextChange(text: String) {
    binding.etOffAdd1.setText(text)
  }

  override fun onAdd2AfterTextChanged(): Observable<String> {
    return binding.etAdd2.textChanges()
        .map { it.toString() }
  }

  override fun renderOnAdd2AfterTextChange(text: String) {
    binding.etAdd2.setText(text)
  }

  override fun onAdd3AfterTextChanged(): Observable<String> {
    return binding.etAdd3.textChanges()
        .map { it.toString() }
  }

  override fun renderOnAdd3AfterTextChange(text: String) {
    binding.etAdd3.setText(text)
  }

  override fun onRefereeCodeChanged(): Observable<String> {
    return binding.etRefereeCode.afterTextChangeEvents()
        .map { it.editable?.toString() ?: emptyString() }
  }

  override fun getRefereeCode(): String {
    return binding.etRefereeCode.text?.toString() ?: emptyString()
  }

  override fun resetRefereeOnCodeChange() {
    binding.apply {
      etRefereeName.setText(emptyString())
      etRefereeEmail.setText(emptyString())
      etRefereeNumber.setText(emptyString())
    }
  }

  override fun onRefereeNameChanged(): Observable<String> {
    return binding.etRefereeName.afterTextChangeEvents()
        .map { it.editable?.toString() ?: "" }
  }

  override fun resetRefereeOnNameChange() {
    binding.apply {
      etRefereeCode.setText(emptyString())
      etRefereeEmail.setText(emptyString())
      etRefereeNumber.setText(emptyString())
    }
  }

  override fun renderOnRefereeCodeChange(uiModel: UiModel<SearchReferee>) {
    binding.pbLoadingIndicatorRefreeCode toggle uiModel.inProgress
    renderOnRefereeChange(uiModel)
  }

  override fun renderOnRefereeNameChange(uiModel: UiModel<SearchReferee>) {
    binding.pbLoadingIndicatorRefreeName toggle uiModel.inProgress
    renderOnRefereeChange(uiModel)
  }

  private fun renderOnRefereeChange(uiModel: UiModel<SearchReferee>) {
    uiModel.onFailure {
      isReferee = false
      binding.etRefereeEmail.setText(emptyString())
      binding.etRefereeNumber.setText(emptyString())
    }
    uiModel.onSuccess {
      when (it.type) {
        "ByCode" -> binding.etRefereeName.setText(it.name)
        else -> binding.etRefereeCode.setText(it.code)
      }
      binding.etRefereeEmail.setText(it.email)
      binding.etRefereeNumber.setText(it.mobile)
      isReferee = true
    }
  }

  private infix fun ProgressBar.toggle(inProgress: Boolean) {
    if (inProgress) visible() else gone()
  }

  override fun isRefereeCodeFocused(): Boolean {
    return binding.etRefereeCode.isFocused
  }

  override fun isRefereeNameFocused(): Boolean {
    return binding.etRefereeName.isFocused
  }

  override fun render(uiModel: UiModel<LeadDetailPageData>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess { data ->
      profileAdapter.addAll(data.profileRowDatas)
      binding.spProfile.setSpinnerData(profileAdapter, data.profileRowDatas)
      interestedAdapter.addAll(data.interestInRowDatas)
      // binding.sp.setSpinnerData(interestInAdapter, data.interestInRowDatas)
      binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
      binding.spOffCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
    }
  }

  override fun renderLeadDetailAndProfession(uiModel: UiModel<LeadDetailAndProfession>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess { data ->
      binding.spProfession.setSpinnerData(professionAdapter, data.professionRowDatas)
      data.detail.renderLeadDetail()
    }
  }

  var pmsList: List<String> = emptyList()

  private val leadDetailAddProductList = ArrayList<LeadDetailAddProductModel>()

  private fun setEnableDisable(status: Boolean) {
    binding.apply {
      etLeadName.isEnabled = status
      etPanNumber.isEnabled = status
      etAadharNumber.isEnabled = status
      etContactPerson.isEnabled = status
      etLandlineNumber.isEnabled = status
      etRefereeCode.isEnabled = status
      etRefereeName.isEnabled = status
      etProspectRefereeName.isEnabled = status
      etProspectRefereerMobile.isEnabled = status
      etProspectRefereerEmail.isEnabled = status
      etLandlineNumber.isEnabled = status
      etDob.isEnabled = status
      etAnniversary.isEnabled = status
      etAlternateMobileNumber.isEnabled = status
      etAlternateEmail.isEnabled = status
      etEmail.isEnabled = status
      etContactPerson.isEnabled = status
      etComments.isEnabled = status
      btnSubmit.isEnabled = status
      spProfile.isEnabled = status
      spProfession.isEnabled = status
      cbSameAs.isEnabled = status
      cbPrimaryAddress.isEnabled = status
      cbOffPrimaryAddress.isEnabled = status
      spPmsProduct.isEnabled = status
      if (status) {
        if (cbPrimaryAddress.isChecked) {
          enableField(llResAdd)
          enableField(llOffAdd)
        }
        if (cbOffPrimaryAddress.isChecked) {
          enableField(llOffAdd)
          enableField(llResAdd)
        }
      } else {
        disableField(llOffAdd)
        disableField(llResAdd)
      }
      if (mLeadFieldSectionModelList.isNotEmpty()) {
        for (mSection in mLeadFieldSectionModelList) {
          if (mSection.selectionList != null) {
            for (modal in mSection.selectionList) {
              val cObj = modal.controlObject
              if (cObj != null) {
                if (cObj is EditText) {
                  cObj.isEnabled = status
                } else if (cObj is Spinner) {
                  cObj.isEnabled = status
                }
              }
            }
          }
        }
      }
    }
  }

  private val mLeadFieldSectionModelList = emptyList<LeadFieldSectionModel>()

  private fun LeadDetail.renderLeadDetail() {
    isLeadEditable = when {
      canEditLead == "0" || isProductSpecialist.equals("Y", true) -> false
      else -> true
    }

    binding.spPmsProduct.apply {
      adapter = pmsProductAdapter
    }

    pmsProductAdapter.setSpinnerDataWithSelection(
        binding.spPmsProduct, pmsList, isPotentialPMSProduct
    )
    binding.etProspectRefereerEmail.setText(prospectReferEmail)
    binding.etProspectRefereeName.setText(prospectReferName)
    binding.etRefereeCode.setText(refereeCode)
    binding.etRefereeName.setText(refereeName)
    binding.etRefereeEmail.setText(referEmail)
    binding.etRefereeNumber.setText(referMobile)
    binding.etCreatedBy.setText(createdBy)
    binding.etCreatedThrough.setText(createdThrough)
    binding.etLastModifiedBy.setText(lastModifiedBy)
    try {
      binding.etLastModifiedDate.setText(
          CRMAppUtil.getDateFromString(
              lastModifiedDate,
              "yyyy-MM-dd'T'kk:mm:ss", MOSLConstants.DATE_FORMAT_TO_DISPLAY
          )
      )
    } catch (e: Exception) {
      binding.etLastModifiedDate.setText(lastModifiedDate.replace("T", " "))
    }

    binding.etProspectSource.setText(prospectSource)
    binding.etProspectSourceDetail.setText(leadSourceDetails)
    binding.etComments.setText(comments)
    binding.etContactPerson.visibility =
      if (leadCategoryText.equals("Individual", ignoreCase = true)) {
        isIndividualsFlag = true
        View.GONE
      } else {
        isIndividualsFlag = false
        View.VISIBLE
      }
    binding.etLeadCategory.setText(leadCategoryText)

    /*if (leadProductMapping != null && leadProductMapping.isNotEmpty()) {
      leadProductMapping.renderMapping()
    }

    contact?.run {
      renderContact(leadCategoryText)
    }*/

  }

  private var leadDetailAddProductAdapter: LeadDetailAddProductAdapter? = null

  private fun List<LeadProductMap>.renderMapping() {
    mapNotNull {
      stage = it.stageId
      status = it.statusId
      binding.tvClientCode.setText(it.clientCode)
      //leadDetailAddProductList.add(getLeadDetailAddProductModel(this))
    }
    //leadDetailAddProductAdapter = LeadDetailAddProductAdapter()
  }
/*
  private fun getLeadDetailAddProductModel(
    item: LeadProductMappingItem
  ): LeadDetailAddProductModel? {
    val crossSellingProducts: List<CrossSellingProductsModel> =
      getCrossSellingProductsModel(item.crossSellingProducts)
    return LeadDetailAddProductModel(
        item.stageID.toString(),
        stringOrEmpty(item.stageName), item.statusID.toString(),
        stringOrEmpty(item.statusName), item.interestedIn.toString(),
        stringOrEmpty(item.productName),
        stringOrEmpty(item.primaryRMName),
        stringOrEmpty(item.productSpecialistName),
        stringOrEmpty(item.assignedBy),
        stringOrEmpty(item.assignedByDate),
        stringOrEmpty(item.clientCode), item.leadProductMappingID.toString(),
        item.isDefault,
        crossSellingProducts,
        item.isCrossSellingProduct,
        stringOrEmpty(item.isProductSpecialistEditDisabled),
        stringOrEmpty(item.isProductDisabled),
        stringOrEmpty(item.productSpecialist)
    )
  }*/

  private fun Contact.renderContact(category: String) {

  }

  private var isLeadEditable = true
  var isIndividualsFlag = false

  override fun isSameAsAboveChecked(): Boolean = binding.cbSameAs.isChecked
  override fun onCountrySelected(): Observable<Int> = binding.spCountry.itemSelections()
  override fun onOfficeCountrySelected(): Observable<Int> = binding.spOffCountry.itemSelections()

  override fun onStateSelected(): Observable<Int> = binding.spState.itemSelections()
  override fun onOfficeStateSelected(): Observable<Int> = binding.spOffState.itemSelections()
  override fun onCitySelected(): Observable<Int> = binding.spCity.itemSelections()

  override fun renderOnCitySelectAndSameAsAboveChecked(position: Int) {
    binding.spOffCity.setSelection(position)
  }

  override fun getSelectedCountry(): RowData = binding.spCountry.selectedItem as RowData
  override fun getSelectedOfficeCountry(): RowData = binding.spOffCountry.selectedItem as RowData
  override fun getSelectedState(): RowData = binding.spState.selectedItem as RowData
  override fun getSelectedOfficeState(): RowData = binding.spOffState.selectedItem as RowData

  override fun renderStateOnCountrySelection(uiModel: UiModel<List<RowData>>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess {
      binding.spState.setSpinnerData(stateAdapter, it)
      binding.spOffState.setSpinnerData(stateAdapter, it)
      if (binding.cbSameAs.isChecked) {
        binding.spOffCountry.setSelection(binding.spCountry.selectedItemPosition)
      }
    }
  }

  override fun renderOfficeStateOnOfficeCountrySelection(uiModel: UiModel<List<RowData>>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess {
      binding.spOffState.setSpinnerData(stateAdapter, it)
    }
  }

  override fun renderCityOnStateSelection(uiModel: UiModel<List<RowData>>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess {
      binding.spCity.setSpinnerData(cityAdapter, it)
      binding.spOffCity.setSpinnerData(cityAdapter, it)
      if (binding.cbSameAs.isChecked) {
        binding.spOffState.setSelection(binding.spState.selectedItemPosition)
      }
    }
  }

  override fun renderOfficeCityOnOfficeStateSelection(uiModel: UiModel<List<RowData>>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { toggleProgressBar(uiModel.inProgress) }
    uiModel.onSuccess {
      binding.spOffCity.setSpinnerData(cityAdapter, it)
    }
  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }

  private var pager: ViewPager? = null
  fun setViewPager(viewPager: ViewPager) {
    pager = viewPager
  }

  companion object {
    @JvmStatic
    fun newInstance(
      position: Int,
      type: String,
      leadID: String,
      ucId: String
    ): LeadDetailFragment {
      return LeadDetailFragment().apply {
        val args = Bundle().apply {
          putInt(MOSLConstants.POSITION, position)
          putString(MOSLConstants.TYPE, type)
          putString(MOSLConstants.LEADID, leadID);
          putString(MOSLConstants.UCID, ucId);
        }
        arguments = args
      }
    }

  }
}