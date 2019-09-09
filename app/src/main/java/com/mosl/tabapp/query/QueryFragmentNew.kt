package com.mosl.tabapp.query

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.query.QueryUiEvent.OnSubmitConfirmClick
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.FragmentQueryNewBinding
import com.mosl.tabapp.util.initiateCall
import com.mosl.validator.exception.ValidationException
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class QueryFragmentNew : CommonFragment(), QueryView {
  lateinit var binding: FragmentQueryNewBinding

  @Inject
  lateinit var presenter: QueryPresenter
  internal val uiEventSubject: PublishSubject<QueryUiEvent> = PublishSubject.create()
  override fun getLayout(): Int {
    return R.layout.fragment_query_new
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentQueryNewBinding.bind(view)
    setAppTitle(getString(R.string.page_title_query))
    setUpValues()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpValues() {
    binding.apply {
      val user = userManager.user
      etPartnerName.setText(user.firstName)
      etEmailAddress.setText(user.email)
      etMobileNumber.setText(user.mobile)
      etBranchCode.setText(user.defaultBusinessProfile.branchCode)
    }
  }

  override fun getQueryMessgae(): String {
    return binding.etQueryDescription.text.trim()
        .toString()
  }

  override fun onSubmitBtnClick(): Observable<Unit> {
    return binding.btnSubmit.clicks()
  }

  override fun onSubmitDialogOpen(uiModel: UiModel<String>) {
    uiModel.error?.let {
      if (uiModel.error is ValidationException) {
        val message = (uiModel.error as ValidationException).error
            .error
        message?.let { msg -> alert(getString(msg)) }

      } else {
//        Handle error condition
//        binding.viewState.showError(it)
      }
    }

    uiModel.data?.let {
      AlertDialog.Builder(requireContext())
          .setTitle(getString(R.string.app_name))
          .setMessage(getString(R.string.btn_submit))
          .setCancelable(true)
          .setNegativeButton(getString(R.string.text_label_no), null)
          .setPositiveButton(getString(R.string.text_label_yes)) { _, _ ->
            uiEventSubject.onNext(OnSubmitConfirmClick)
          }
          .show()
    }

  }

  override fun onSubmitConfirmClicked(): Observable<OnSubmitConfirmClick> {
    return uiEventSubject.ofType()
  }

  override fun onResetBtnClick(): Observable<Unit> {
    return binding.btnReset.clicks()
  }

  override fun reset() {
    binding.etQueryDescription.setText("")
  }

  override fun onTelePhoneNoOneClicked(): Observable<Unit> {
    return binding.txtTelePhone1.clicks()
  }

  override fun onTelePhoneNoTwoClicked(): Observable<Unit> {
    return binding.txtTelePhone2.clicks()
  }

  override fun showAlertForCall(phoneNumber: String) {
    AlertDialog.Builder(requireContext())
        .setTitle(R.string.app_name)
        .setMessage(R.string.call_alert_message)
        .setCancelable(true)
        .setPositiveButton(getString(R.string.text_label_yes)) { _, _ ->
          dialNumber(phoneNumber)
        }
        .setNegativeButton(getString(R.string.text_label_no)) { _, _ ->
        }
        .show()
  }

  private fun dialNumber(phoneNumber: String) {
    requireContext().initiateCall(phoneNumber)
  }

  override fun renderSubmit(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)
    uiModel.error?.let {
      //      binding.viewState.showError(it)
    }
    uiModel.data?.let {
      AlertDialog.Builder(requireContext())
          .setMessage(it)
          .setCancelable(true)
          .setPositiveButton(android.R.string.ok) { _, _ -> }
          .show()
      binding.etQueryDescription.setText("")
    }
  }

  private fun toggleProgress(inProgress: Boolean) {
//    binding.viewState.toggleProgress(inProgress)
  }

  companion object {
    const val TELE_PHONE_1 = "02239982515"
    const val TELE_PHONE_2 = "02267490600"
  }

  override fun isDataEmpty(): Boolean {
    return true
  }

  override fun onDestroyView() {
    presenter.stop()
//    binding.viewState.setRetryListener(null)
    super.onDestroyView()
  }
}

