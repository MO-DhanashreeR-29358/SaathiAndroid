package com.mosl.app.core.data.productivity

import com.mosl.mobile.domain.productivity.*
import com.mosl.saathi.api.SaathiApi
import com.mosl.shared.util.toConvertDecimals
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class ProductivityRepositoryImpl @Inject constructor(
  val api: SaathiApi
) : ProductivityRepository {

  override fun getDashboardData(request: ProductivityInput): Observable<ProductivityCombo> {
    val leadRequest = request.requestTransform()
    return api.getProductivity(leadRequest).map { response ->
      val jsonObject = JSONObject(response)
      val jsonArray = if (jsonObject.has("Response")) {
        jsonObject.getJSONObject("Response").getJSONArray("Table")
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

        val data: ArrayList<ProductivityData> = ArrayList()
        for (i in 0 until jsonArray.length()) {
          val json = jsonArray.getJSONObject(i)
          val values: ArrayList<String> = ArrayList()

          headers.forEach { header ->
            values.add(json.get(header).toString())
          }
          data.add(ProductivityData(setData(values)))
        }
        ProductivityCombo(headers, data = data)
      } else {
        ProductivityCombo(emptyList(), data = emptyList())
      }
    }
  }

  private fun setData(values: ArrayList<String>): ArrayList<String> {
    val list: ArrayList<String> = ArrayList()
    list.add(values[0])
    for (i in 1 until values.size) {
      if (values[i] != "null" && values[i] != "")
        list.add(values[i].toConvertDecimals())
      else if (values[i] == "null")
        list.add(values[i].replace("null", ""))
      else list.add(values[i])
    }
    return list
  }
}