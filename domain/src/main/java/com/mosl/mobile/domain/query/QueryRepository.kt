package com.mosl.mobile.domain.query

import io.reactivex.Observable

interface QueryRepository {
  fun getUserInsertQuery(message: String): Observable<String>
}