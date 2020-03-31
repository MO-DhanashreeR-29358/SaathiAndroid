package com.mosl.app.core.data.report

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.domain.report.HierarchyReportCombo
import com.mosl.mobile.domain.report.HierarchyReportData
import com.mosl.mobile.domain.report.HierarchyReportInput
import com.mosl.mobile.domain.report.HierarchyReportRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import org.json.JSONObject
import java.util.HashMap
import javax.inject.Inject

class HierarchyReportRepositoryImpl @Inject constructor(
  val api: SaathiApi,
  val gson: Gson
) : HierarchyReportRepository {

  override fun getDashboardData(
    hierarchyReportInput: HierarchyReportInput
  ): Observable<HierarchyReportCombo> {

    val request = hierarchyReportInput.requestTransform()
    return api.postLeadReport(request).map { it ->
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
    }.map {
      renderTable(it)
    }

  }

  private fun renderTable(response: String): HierarchyReportCombo {
    val jsonObject = JSONObject(response)
    val jsonArray = if (jsonObject.has("Response")) {
      jsonObject.getJSONObject("Response").getJSONArray("Data")
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

      val data: ArrayList<HierarchyReportData> = ArrayList()
      for (i in 0 until jsonArray.length()) {
        val json = jsonArray.getJSONObject(i)
        val values: ArrayList<String> = ArrayList()
        val hasMap: MutableMap<String, String> = mutableMapOf<String, String>()
        headers.forEach { header ->
          values.add(json.get(header).toString())
          hasMap[header] = json.get(header).toString()
        }
        data.add(HierarchyReportData(setData(values), headers, hasMap))
      }
      return HierarchyReportCombo(headers, data = data)
    } else {
      return HierarchyReportCombo(emptyList(), data = emptyList())
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