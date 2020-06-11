package com.mosl.tabapp.leadViewDetail.leaddetail

import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.focusChanges
import com.jakewharton.rxbinding3.view.touches
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxbinding3.widget.textChanges
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentLeadDetailBinding
import com.mosl.tabapp.util.disableErrorMessages
import com.mosl.tabapp.util.emptyString
import com.mosl.tabapp.util.setErrorMessages
import com.mosl.tabapp.util.string
import io.reactivex.Observable
import javax.inject.Inject

class LeadDetailFragment : CommonFragment(), LeadDetailView {

  @Inject internal lateinit var presenter: LeadDetailPresenter
  private lateinit var binding: FragmentLeadDetailBinding

  private var leadId = "0"
  private var ucId = "0"

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

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}