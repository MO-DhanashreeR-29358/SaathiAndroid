package com.mosl.tabapp.reactivation.createrequestReactivation

import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.reactivation.ReactivationActualMarginPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.mobile.domain.reactivation.ReactivationMinDatePostModel
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import javax.inject.Inject

class CreateRequestReactivationPresenter @Inject constructor(
    view:CreateRequestReactivationView,
    val repository: ReactivationRepository,
    val userManager: UserManager
):BasePresenter<CreateRequestReactivationView>(view){
    override fun start() {


           /* .switchMap {
                repository.fnGetActualMargin(ReactivationActualMarginPostModel("1","RET6800","19/03/2024 00:00:00")).compose(applyUiModel())
            }.subscribe {
                view.fnRenderActualmargin(it)
            }**/
        onFetchCalled()
            .switchMap {
                repository.fnGetMinDate(ReactivationMinDatePostModel(userManager.user.employeeCode)).compose(applyUiModel())
            }.subscribe{
                view.fnRenderMinDate(it)
            }.addTo(disposable)

            view.fnOnSearchClientClick()
                .map {
                    view.fnGetData()
                }
            .switchMap {input->
                ReactivationRequestValidator.validate(input).flatMap {
                    repository.fnGetSearchClientCode(it)
                }.compose(applyUiModel())
            }.subscribe {
                view.fnRenderClientSearch(it)
            }.addTo(disposable)

        view.fnOnViewClientDetailClick()
            .subscribe {
                view.fnRenderViewClientDetailsClick()
            }.addTo(disposable)

        view.fnOnUploadDocClick()
            .subscribe {
                view.fnOpenFileSelet()
            }.addTo(disposable)

        view.onSelectedFileEventFired()
            .subscribe {
                Log.d("in presenter",it.uriString)
            }.addTo(disposable)

        view.onSelectedImageEventFired()
            .subscribe {
                Log.d("in presenter",it.uriString)
            }.addTo(disposable)

    }
}