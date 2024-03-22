package com.mosl.tabapp.reactivation.createrequestReactivation

import android.os.Bundle
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.core.CommonFragment
import javax.inject.Inject
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.FragmentCreateRequestReactivationBinding
import com.mosl.validator.exception.ValidationException
import io.reactivex.rxjava3.core.Observable
import com.mosl.commons.rx.ofType
class CreateRequestReactivationFragment : CommonFragment(),CreateRequestReactivationView {

    lateinit var binding:FragmentCreateRequestReactivationBinding

    @Inject
    lateinit var presenter: CreateRequestReactivationPresenter
    var strSelectedOption:String=""
    var objReactivationClientSearchResponseModel:ReactivationClientSearchResponseModel?=null
    private val subject =CreateReacivationUiEvent.getSubject()

    override fun getLayout(): Int {
       return R.layout.fragment_create_request_reactivation
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateRequestReactivationBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    private fun setupView()
    {
          /*  val showPopup=ReactivationDetailsFragment()
            showPopup.show(childFragmentManager,"leadDetailsview")*/
        binding.radioSelection.setOnCheckedChangeListener { group, checkedId ->
                    when(checkedId)
                    {
                        R.id.radio_btn_request ->
                            strSelectedOption="Reactivation Request"
                        R.id.radio_btn_rm_request ->
                            strSelectedOption="RM Change Request"
                        R.id.radio_btn_date_request ->
                            strSelectedOption="Activation Date ChangeRequest"

                    }
            toast(strSelectedOption)
        }
    }

    override fun fnGetData(): ReactivationClientSearchPostModel {
        return ReactivationClientSearchPostModel(
            employeeCode = userManager.user.employeeCode,
            clientCode = binding.edtClientCode.text.toString(),
            requestTypeFlag = strSelectedOption,
            branchShift = "No"
        )
    }
    override fun fnOnSearchClientClick(): Observable<Unit> {
        return binding.imgSearchClient.clicks()
    }

    override fun fnRenderClientSearch(uiModel: UiModel<List<ReactivationClientSearchResponseModel>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            objReactivationClientSearchResponseModel=it.firstOrNull()
            subject.onNext(CreateReacivationUiEvent.OnCallMinDate)
        }

        uiModel.onFailure {
            if (it is ValidationException) {
                val error = it.error.message
                showErrorToast(error.orEmpty())
            } else {
                val message = it.getErrorOnFailure().message
                if (message.isNullOrEmpty().not()) {
                    showErrorToast(message.orEmpty())
                }
            }
        }
    }

    override fun fnOnCallMinDate(): Observable<CreateReacivationUiEvent.OnCallMinDate> {
        return subject.ofType()
    }
    override fun fnRenderMinDate(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            Log.d("Response",it)
        }

        uiModel.onFailure {
            showOnFailurePopup(it)
        }

    }

    override fun fnRenderActualmargin(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            Log.d("Response",it)
        }

        uiModel.onFailure {
            showOnFailurePopup(it)
        }
    }
}