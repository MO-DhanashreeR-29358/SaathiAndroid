package com.mosl.app.core.data.lead.reports

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.lead.report.LeadsReportInput
import com.mosl.mobile.domain.lead.report.LeadsReportModel
import com.mosl.mobile.domain.lead.report.LeadsReportRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.emptyList
import kotlin.collections.emptyMap
import kotlin.collections.indices
import kotlin.collections.set

class LeadsReportRepositoryImpl @Inject constructor(
  val api: SaathiApi,
  val gson: Gson
) : LeadsReportRepository {
  override fun getLeadWidget(request: LeadsReportInput): Observable<LeadsReportModel> {
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
    }.map {
      rendarData(it)
    }

  }

  fun rendarData(responseJsonObj: Map<String, Any>): LeadsReportModel {
    val prepareGroupMap = prepareGroupMap(responseJsonObj)
    val arrayDetail =
      responseJsonObj["Table"] as List<Map<String, Any>>?
    val columns = ArrayList<String>()
    val header = ArrayList<String>()
    val listData = ArrayList<LinkedHashMap<String, String?>>()
    arrayDetail?.apply {
      if (this.size > 0) {
        for (y in 2 until this.size) {
          val objDetailSecond = this[0]
          if (columns.size == 0) {
            for (keySetE in objDetailSecond.keys) {
              columns.add(keySetE)
            }
            for (column in columns) {
              val columnData = if (objDetailSecond[column]
                  .toString() == ""
              ) "" else objDetailSecond[column].toString()
              header.add(columnData)
            }
          }
          listData.add(setData(this[y], columns))
        }
        return LeadsReportModel(
          setHeaderData(columns), setSecondHeaderData(header), listData,
          prepareGroupMap
        )
      } else {
        return LeadsReportModel(
          emptyArray(),
          emptyArray(),
          emptyList<LinkedHashMap<String, String?>>(),
          prepareGroupMap
        )
      }
    }
    return LeadsReportModel(
      emptyArray(),
      emptyArray(),
      emptyList<LinkedHashMap<String, String?>>(),
      prepareGroupMap
    )
  }

  fun prepareGroupMap(
    responseJsonObj: Map<String, Any?>
  ): Map<Int, MutableList<Map<Int, String>>> {
    val tableHeaderMap: MutableMap<Int, MutableList<Map<Int, String>>> =
      HashMap()
    if (responseJsonObj["Table2"] == null) return emptyMap()
    val jsonArray =
      responseJsonObj["Table2"] as List<Map<String, Any>>?
    jsonArray?.apply {
      for (ctr in this.indices) {
        val jsonRowObject = this[ctr]
        val groupId = jsonRowObject["GroupId"].toString().toInt()
        val childid = jsonRowObject["childid"].toString().toInt()
        val rowFilter = jsonRowObject["RowFilter"].toString()
        var childList =
          tableHeaderMap[groupId]
        if (childList == null) {
          childList = ArrayList()
        }
        val childMap: MutableMap<Int, String> =
          HashMap<Int, String>()
        childMap[childid] = rowFilter
        childList.add(childMap)
        tableHeaderMap[groupId] = childList

      }
    }

    return tableHeaderMap

  }

  private fun setData(
    objDetail: Map<String, Any>,
    columns: ArrayList<String>
  ): LinkedHashMap<String, String?> {
    val data =
      LinkedHashMap<String, String?>()
    for (column in columns) {
      val columnData = if (objDetail[column]
          .toString()
        == ""
      ) "0" else objDetail[column].toString()
      data[column] = columnData
    }
    return data
  }

  private fun setHeaderData(columns: ArrayList<String>): Array<String?> {
    var stockArr = arrayOfNulls<String>(columns.size)
    stockArr = columns.toArray(stockArr)

    return stockArr
  }

  private fun setSecondHeaderData(header: ArrayList<String>): Array<String?> {
    var stockArr = arrayOfNulls<String>(header.size)
    stockArr = header.toArray(stockArr)

    return stockArr
  }

}
