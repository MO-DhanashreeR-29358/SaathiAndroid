package com.mosl.app.core.data.teamtarget

import com.mosl.mobile.domain.teamtarget.TeamTargetInput
import com.mosl.mobile.domain.teamtarget.TeamTargetItemModel
import com.mosl.mobile.domain.teamtarget.TeamTargetRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class TeamTargetRepositoryImpl @Inject constructor(
  val api: SaathiApi
) : TeamTargetRepository {
  override fun getTeamTargetWidget(request: TeamTargetInput): Observable<TeamTargetItemModel> {
    val targetRequest = request.requestTransform()
    return api.getTeamTargetWidgetData(targetRequest).map {
      it.responseTransform()
    }
  }

}