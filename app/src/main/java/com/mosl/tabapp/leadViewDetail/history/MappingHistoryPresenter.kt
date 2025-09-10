package com.mosl.tabapp.leadViewDetail.history

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryRepository
import com.mosl.mobile.user.login.UserManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class MappingHistoryPresenter @Inject constructor(
  view: MappingHistoryView,
  val repository: MappingHistoryRepository,
  val userManager: UserManager
) : BasePresenter<MappingHistoryView>(view) {
  override fun start() {

    Observable.merge(onFetchCalled(), onRetryCalled(), view.onEventTypeChange()).switchMap {
        if(userManager.getUser()?.isIndirectUser() == true){
          if(view.getEventType() == "Lead Detail History"){
            repository.getIndirectLeadHistoryData(view.getLeadId(), view.getUcId())
              .compose(applyUiModel())
          }else{
            repository.getIndirectLeadMappingHistoryData(view.getLeadId(), view.getUcId())
              .compose(applyUiModel())
          }
        }else{
          repository.getDashboardData(view.getLeadId(), view.getUcId(), view.getEventType())
            .compose(applyUiModel())
        }
      }
      .subscribe(view::renderData)
      .addTo(disposable)

  }

}