package com.mosl.mobile.domain.advanceSearch

import com.mosl.mobile.domain.snapshot.AdvanceSearchDataCombo
import io.reactivex.rxjava3.core.Observable

interface AdvanceSearchRepository {

    fun fnGetAdvanceSearch(objAdvanceSearchInput:AdvanceSearchInput):Observable<AdvanceSearchDataCombo>

}