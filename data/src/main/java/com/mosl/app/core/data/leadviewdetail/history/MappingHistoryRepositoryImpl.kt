package com.mosl.app.core.data.leadviewdetail.history

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryCombo
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryData
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryRepository
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.clientviewhistory.ClientHistoryRequest
import com.mosl.saathi.api.model.leadviewhistory.LeadHistoryRequest
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.emptyList
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.sortedBy

class MappingHistoryRepositoryImpl @Inject constructor(
  val api: SaathiApi,
  val gson: Gson,
  val saathiIndirectApi: SaathiIndirectApi,
  val userManager: UserManager

) : MappingHistoryRepository {

  override fun getDashboardData(
    leadId: String,
    ucId: String,
    eventType: String
  ): Observable<MappingHistoryCombo> {

    if(userManager.getUser()?.isIndirectUser() == true){
      val apiRequest = when (eventType) {
        "Lead Detail History" -> {
          val request = LeadHistoryRequest()
          request.request.leadID = leadId
          saathiIndirectApi.getLeadHistoryByLeadId(request)
        }
        "Lead Product Mapping History" -> {
          val request = LeadHistoryRequest()
          request.request.leadID = leadId
          saathiIndirectApi.getLeadProductHistoryByLeadId(request)
        }
        else -> {
          val request = LeadHistoryRequest()
          request.request.leadID = leadId
          saathiIndirectApi.getLeadHistoryByLeadId(request)
        }
      }

      return apiRequest.map { it ->
       Log.d("LeadMapping" , gson.toJson(it))
        val type =
          object : TypeToken<HashMap<String?, Any?>?>() {}.type
        val response: HashMap<String, Any> =
          gson.fromJson(gson.toJson(it), type)
        val status = response["Status"].toString()
        Log.d("LeadMappingStatus" , status)
        if (status.equals("true", ignoreCase = true)) {
          Log.d("LeadMappingStatus:" , "true")
          return@map it
        } else {
          val message = response["Message"].toString()
          Log.d("LeadMappingMessage:" , message)
          throw ServiceFailureException(message, message)
        }
      }.map {

        renderIndirectTable(gson.toJson(it))
      }
    }else{
      val apiRequest = when (eventType) {
        "Lead Detail History" -> {
          val request = LeadHistoryRequest()
          request.request.leadID = leadId
         api.getLeadHistoryByLeadId(request)
        }
        "Lead Product Mapping History" -> {
          val request = LeadHistoryRequest()
          request.request.leadID = leadId
          api.getLeadProductHistoryByLeadId(request)
        }
        "Client Current Mapping" -> {
          val request = ClientHistoryRequest()
          request.request.uCID = ucId
          api.getCurrentMapping(request)
        }
        "Client Mapping History" -> {
          val request = ClientHistoryRequest()
          request.request.uCID = ucId
          api.getMappingHistory(request)
        }
        else -> {
          val request = LeadHistoryRequest()
          request.request.leadID = leadId
          api.getLeadHistoryByLeadId(request)
        }
      }

      return apiRequest.map { it ->
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


  }

  private fun renderTable(response: String): MappingHistoryCombo {
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

      val data: ArrayList<MappingHistoryData> = ArrayList()
      for (i in 0 until jsonArray.length()) {
        val json = jsonArray.getJSONObject(i)
        val values: ArrayList<String> = ArrayList()

        headers.forEach { header ->
          values.add(json.get(header).toString())
        }
        data.add(MappingHistoryData(setData(values)))
      }
      return MappingHistoryCombo(headers, data = data)
    } else {
      return MappingHistoryCombo(emptyList(), data = emptyList())
    }

  }

  private fun renderIndirectTable(response: String): MappingHistoryCombo {
    Log.d("LeadMappingTable:" , "true")
    Log.d("leadProductResponse", response)
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

      val data: ArrayList<MappingHistoryData> = ArrayList()
      for (i in 0 until jsonArray.length()) {
        val json = jsonArray.getJSONObject(i)
        val values: ArrayList<String> = ArrayList()

        headers.forEach { header ->
          values.add(json.get(header).toString())
        }
        data.add(MappingHistoryData(setData(values)))
      }
      return MappingHistoryCombo(headers, data = data)
    } else {
      return MappingHistoryCombo(emptyList(), data = emptyList())
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