package com.mosl.app.core.data.dashboardreport

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.dashboardreport.DashboardReportCombo
import com.mosl.mobile.domain.dashboardreport.DashboardReportData
import com.mosl.mobile.domain.dashboardreport.DashboardReportRepository
import com.mosl.mobile.domain.dashboardreport.DashboardReportRequest
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.emptyList
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.sortedBy

class DashboardReportRepositoryImpl @Inject constructor(
  val api: SaathiApi,
  val gson: Gson,
  val userManagerImpl: UserManagerImpl
) : DashboardReportRepository {

  override fun getDashboardData(
    dashboardReportRequest: DashboardReportRequest
  ): Observable<DashboardReportCombo> {

    val request =
      dashboardReportRequest.requestTransform(userManagerImpl.getUser()?.currentBusinessProfile?.businessProfileId)
    return api.getDashboardSecondLevel(request)
      .map { it ->
        val type =
          object : TypeToken<HashMap<String?, Any?>?>() {}.type
        val response: HashMap<String, Any> =
          gson.fromJson(it, type)
        val status = response["Status"].toString()
        if (status.equals("true", ignoreCase = true)) {
          return@map it
        } else {
          val message = response["Message"].toString()
          throw ServiceFailureException(message, message)
        }
      }
      .map {
        renderTable(it)
      }

  }

  private fun renderTable(response: String): DashboardReportCombo {
    val jsonObject = JSONObject(response)
    val jsonArray = if (jsonObject.has("Response")) {
      jsonObject.getJSONObject("Response")
        .getJSONArray("Data")
    } else null
    if (jsonArray != null && jsonArray.length() > 0) {
      val colHeaders: MutableList<ColHeader> = mutableListOf()
      val first = jsonArray.getJSONObject(0)
      val iterator = first.keys()
      while (iterator.hasNext()) {
        val header = iterator.next()
          .toString()
        colHeaders.add(ColHeader(header))
      }

      val headers = colHeaders.map {
        it.copy(index = response.indexOf(it.label))
      }
        .sortedBy { it.index }
        .map { it.label }

      val data: ArrayList<DashboardReportData> = ArrayList()
      val headerData = mutableListOf<String>()
      for (i in 0 until jsonArray.length()) {
        headerData.clear()
        val json = jsonArray.getJSONObject(i)
        val values: ArrayList<String> = ArrayList()
        val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()
        headers.forEach { header ->
          if (!header.equals("WidgetSystemName", ignoreCase = true) &&
            !header.equals("flg_empcode", ignoreCase = true)
          ) {
            headerData.add(header)
            values.add(json.get(header).toString())
          }
          hasMap[header] = json.get(header)
            .toString()
        }
        data.add(DashboardReportData(setData(values), headerData, hasMap))
      }
      return DashboardReportCombo(headerData, data = data)
    } else {
      return DashboardReportCombo(emptyList(), data = emptyList())
    }

  }

  private fun setData(values: ArrayList<String>): ArrayList<String> {
    val list: ArrayList<String> = ArrayList()
    list.add(values[0])
    for (i in 1 until values.size) {
      if (values[i] != "null" && values[i] != "")
        list.add(values[i])
      else if (values[i] == "null")
        list.add(values[i].replace("null", ""))
      else list.add(values[i])
    }
    return list
  }
}