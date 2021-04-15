package com.mosl.mobile.domain.teamtarget

import io.reactivex.rxjava3.core.Observable

interface TeamTargetRepository {

  fun getTeamTargetWidget(request: TeamTargetInput): Observable<TeamTargetItemModel>

}