package com.mosl.app.core.data.snapshot

import com.mosl.mobile.domain.snapshot.*
import com.mosl.saathi.api.SaathiApi
import com.mosl.shared.util.toConvertDecimals
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class BusinessSnapshotRepositoryImpl @Inject constructor(
  val api: SaathiApi
) : BusinessSnapshotRepository {
  override fun getSnapshot(request: BusinessSnapInput): Observable<BusinessSnapshotResponseModel> {
    val leadRequest = request.requestTransform()
    return api.getBusinessSnapshot(leadRequest).map {
      it.responseTransform()
    }
  }

  override fun getDashboardData(request: BusinessSnapInput): Observable<BusinessSnapshotCombo> {
    val leadRequest = request.transformDashboardRequest()
    return api.getBusinessSnapshotAdv(leadRequest).map { response ->
      val jsonObject = JSONObject(response)
      val jsonArray =
        if (jsonObject.has("Response") && jsonObject.getJSONObject("Response").has("Table")) {
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

        val data: ArrayList<BusinessSnapshotData> = ArrayList()
        for (i in 0 until jsonArray.length()) {
          val json = jsonArray.getJSONObject(i)
          val values: ArrayList<String> = ArrayList()

          headers.forEach { header ->
            values.add(json.get(header).toString())
          }
          data.add(BusinessSnapshotData(setData(values), headers))
        }
        BusinessSnapshotCombo(headers, data = data)
      } else {
        BusinessSnapshotCombo(emptyList(), data = emptyList())
      }
    }
  }

  private fun setData(values: ArrayList<String>): ArrayList<String> {
    val list: ArrayList<String> = ArrayList()
    list.add(values[0])
    for (i in 1 until values.size) {
      if (values[i] != "null" && values[i] != "" && !values[i].equals(
          "UnMapped",
          ignoreCase = true
        )
      )
        list.add(values[i].toConvertDecimals())
      else if (values[i] == "null")
        list.add(values[i].replace("null", ""))
      else list.add(values[i])
    }
    return list
  }
}