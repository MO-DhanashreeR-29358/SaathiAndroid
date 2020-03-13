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
        val jsonObject = JSONObject(it)
        val status: Boolean = jsonObject.getBoolean(STATUS)
        if (status) {
          val response = jsonObject.getJSONObject(RESPONSE)
          renderGraph(response.getJSONArray(TABLE))
        } else {
          val message: String = jsonObject.getString(MESSAGE)
          throw ServiceFailureException(message, message)
        }
      }

  }

  private fun renderGraph(jsonArray: JSONArray): List<TrendsChartModel> {
    val dataList: MutableList<TrendsChartModel> = mutableListOf()
    var paramValue: String
    if (jsonArray.length() > 0) {
      for (array in 0 until jsonArray.length()) {
        val model = TrendsChartModel()
        val jsonObject = jsonArray.getJSONObject(array)
        val paramNameArr = jsonObject.names()
        for (param in 0 until paramNameArr.length()) {
          val paramName = paramNameArr.getString(param)
          if (paramName.equals(PARAMETERS, ignoreCase = true)) {
            paramValue = jsonObject.getString(paramName)
            model.legendText = paramValue
          } else if (paramName.equals(CHARTTYPE, ignoreCase = true)) {
            paramValue = jsonObject.getString(paramName)
            model.chartType = paramValue
          } else {
            if (!paramName.equals(WIDGETTYPE, ignoreCase = true)) {
              paramValue = jsonObject.getString(paramName)
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
    const val PARAMETERS = "Parameters"
    const val CHARTTYPE = "ChartType"
    const val WIDGETTYPE = "WidgetType"
    const val TABLE = "Table"
  }
}



