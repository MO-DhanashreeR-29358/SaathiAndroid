package com.mosl.app.core.data.trends

import com.google.gson.Gson
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.trends.TrendsChartModel
import com.mosl.mobile.domain.trends.TrendsInput
import com.mosl.mobile.domain.trends.TrendsRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class TrendsRepositoryImpl @Inject constructor(
  val api: SaathiApi,
  val gson: Gson
) : TrendsRepository {
  override fun getTrendsWidget(request: TrendsInput): Observable<List<TrendsChartModel>> {
    val targetRequest = request.requestTransform()
    return api.getTrendsWidgetData(targetRequest)
      .map {
        //        val userInfoStr = gson.fromJson(it, String::class.java)
        val response = JSONObject(it)
        val status: String = response.get(STATUS).toString()
        if (status.equals("true", ignoreCase = true)) {
          renderGraph(response.getJSONObject(RESPONSE).getJSONArray("Table"))
        } else {
          val message: String = response.get(MESSAGE).toString()
          throw ServiceFailureException(message, message)
        }
      }

  }

  private fun renderGraph(jsonArray: JSONArray): List<TrendsChartModel> {
    val dataList: MutableList<TrendsChartModel> = mutableListOf()
    var paramValue: String
    if (jsonArray.length() > 0) {
      dataList.clear()
      for (ctr in 0 until jsonArray.length()) {
        val model = TrendsChartModel()
        val `object` = jsonArray.getJSONObject(ctr)
        val paramNameArr = `object`.names()
        for (y in 0 until paramNameArr.length()) {
          val paramName = paramNameArr.getString(y)
          if (paramName == "Parameters") {
            paramValue = `object`.getString(paramName)
            model.legendText = paramValue
          } else if (paramName == "ChartType") {
            paramValue = `object`.getString(paramName)
            model.chartType = paramValue
          } else {
            if (!paramName.equals("WidgetType", ignoreCase = true)) {
              paramValue = `object`.getString(paramName)
              val monthData = paramValue.toFloat()
              model.monthData.put(paramName, monthData)
            }
          }
        }
        dataList.add(model)
      }
    }
    return dataList
  }

  companion object {
    const val STATUS = "Status"
    const val MESSAGE = "Message"
    const val RESPONSE = "Response"
  }
}



