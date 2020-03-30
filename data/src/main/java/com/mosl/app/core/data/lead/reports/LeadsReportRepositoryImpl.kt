package com.mosl.app.core.data.lead.reports

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.lead.report.LeadsReportInput
import com.mosl.mobile.domain.lead.report.LeadsReportRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import java.util.HashMap
import javax.inject.Inject

class LeadsReportRepositoryImpl @Inject constructor(
  val api: SaathiApi,
  val gson: Gson
) : LeadsReportRepository {
  override fun getLeadWidget(request: LeadsReportInput): Observable<Map<String, Any>> {
    val leadRequest = request.requestTransform()
    return api.getLeadReportWidgetData(leadRequest).map {
      val type =
        object : TypeToken<HashMap<String?, Any?>?>() {}.type
      val response: HashMap<String, Any> =
        gson.fromJson(it, type)
      val responseJsonObj =
        response["Response"] as Map<String, Any>
      val status = response["Status"].toString()
      if (status.equals("true", ignoreCase = true)) {
        return@map responseJsonObj
      } else {
        val message = response["Message"].toString()
        throw ServiceFailureException(message, message)
      }
    }

  }
}
