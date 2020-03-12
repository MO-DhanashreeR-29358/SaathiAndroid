package com.mosl.mobile.domain.teamtarget

import io.reactivex.Observable

interface TeamTargetRepository {

  fun getTeamTargetWidget(request: TeamTargetInput): Observable<TeamTargetItemModel>

}