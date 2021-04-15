package com.mosl.mobile.domain.query

import io.reactivex.rxjava3.core.Observable

interface QueryRepository {
  fun getUserInsertQuery(message: String): Observable<String>
}