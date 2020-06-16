package com.mosl.tabapp.leadViewDetail.leaddetail

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.focusChanges
import com.jakewharton.rxbinding3.view.touches
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.tabapp.R
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.CRMValidationRule
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentLeadDetailBinding
import com.mosl.tabapp.leadcreation.domain.LeadDetailPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leaddetail.ClientDetailWithPortfolioAndProfitLoss
import com.mosl.tabapp.leaddetail.LeadDetailAndProfession
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.disableErrorMessages
import com.mosl.tabapp.util.emptyString
import com.mosl.tabapp.util.gone
import com.mosl.tabapp.util.setErrorMessages
import com.mosl.tabapp.util.setSpinnerData
import com.mosl.tabapp.util.string
import com.mosl.tabapp.util.visible
import io.reactivex.Observable
import javax.inject.Inject

class LeadDetailFragment : CommonFragment(), LeadDetailView {

  @Inject internal lateinit var presenter: LeadDetailPresenter
  private lateinit var binding: FragmentLeadDetailBinding
  private var isReferee = false

  private var leadId = "0"
  private var ucId = "0"

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

  override fun onAddProductClicked(): Observable<Unit> {
    return binding.rlAddProduct.clicks()
  }

  override fun onEditLeadDetailClicked(): Observable<Unit> {
    return binding.llEditLeadDetail.clicks()
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

  override fun onAnniversaryClicked(): Observable<Unit> {
    return binding.etAnniversary.clicks()
  }

  override fun onRefereeCodeFocusChanged(): Observable<Boolean> {
    return binding.etRefereeCode.focusChanges()
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

  override fun renderClientDetailWithPortfolioAndProfitLoss(uiModel: UiModel<ClientDetailWithPortfolioAndProfitLoss>) {

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
      binding.spProfile.setSpinnerData(profileAdapter, data.profileRowDatas)
      interestedAdapter.addAll(data.interestInRowDatas)
      // binding.sp.setSpinnerData(interestInAdapter, data.interestInRowDatas)
      binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
      binding.spOffCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
    }
  }

  override fun renderLeadDetail(uiModel: UiModel<LeadDetailAndProfession>) {
    toggleProgressBar(uiModel.inProgress)
    uiModel.onFailure { showOnFailurePopup(it) }
    uiModel.onSuccess { data ->
      binding.spProfession.setSpinnerData(professionAdapter, data.professionRowDatas)
    }
  }

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
}