package com.mosl.app.core.data.query

import com.mosl.mobile.domain.query.QueryRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.query.QueryRequest
import io.reactivex.Observable
import javax.inject.Inject

class QueryRepositoryImpl @Inject constructor(val saathiApi: SaathiApi) : QueryRepository {
  @Inject
  lateinit var userManager: UserManager

  override fun getUserInsertQuery(message: String): Observable<String> {
    val user = userManager.user
    val request = QueryRequest(
        description = message, mobile = user.mobile,
        branchCode = user.defaultBusinessProfile.branchCode
        , partnerName = user.firstName, email = user.email
    )
    return saathiApi.postQuery(request)
        .map { "Thanks for submitting your query." }
  }
}