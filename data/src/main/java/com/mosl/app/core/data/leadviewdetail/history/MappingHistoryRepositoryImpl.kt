package com.mosl.app.core.data.leadviewdetail.history

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryCombo
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryData
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryRepository
import com.mosl.mobile.domain.productivity.ColHeader
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.clientviewhistory.ClientHistoryRequest
import com.mosl.saathi.api.model.leadviewactivities.AppointmentResponse
import com.mosl.saathi.api.model.leadviewhistory.IndirectLeadHistoryResponse
import com.mosl.saathi.api.model.leadviewhistory.IndirectLeadMappingHistoryResponse
import com.mosl.saathi.api.model.leadviewhistory.LeadHistoryRequest
import com.mosl.shared.util.AppDateFormats
import com.mosl.shared.util.formatMonth
import com.mosl.shared.util.getFormatedDate
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import java.lang.reflect.Array
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

  override fun getIndirectLeadHistoryData(
    leadId: String,
    ucId: String
  ): Observable<MappingHistoryCombo> {
    val request = LeadHistoryRequest()
    request.request.leadID = leadId
    return saathiIndirectApi.getLeadHistoryByLeadId(request).map {
       renderIndirectTable(it.data?.mapToListOfResponse() ?: mutableListOf())
    }
  }

  override fun getIndirectLeadMappingHistoryData(
    leadId: String,
    ucId: String
  ): Observable<MappingHistoryCombo> {
    val request = LeadHistoryRequest()
    request.request.leadID = leadId
    return saathiIndirectApi.getLeadProductHistoryByLeadId(request).map {
      renderIndirectProductTable(it.data?.mapToListOfProductResponse() ?: mutableListOf())
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

  private fun renderIndirectTable(list: List<IndirectLeadHistoryResponse>): MappingHistoryCombo {
    if (list.isNotEmpty()) {
      val headers = ArrayList<String>()
      headers.add("Business Unit")
      headers.add("Name")
      headers.add("Mobile")
      headers.add("Email")
      headers.add("Branch Code")
      headers.add("Category")
      headers.add("Lead Owner")
      headers.add("Comments")
      headers.add("Action")
      headers.add("Action Date")
      headers.add("Action User")
      val data: ArrayList<MappingHistoryData> = ArrayList()
      for (i in 0 until list.size) {
        val values: ArrayList<String> = ArrayList()
        values.add(list.get(i).BusinessUnit ?: "")
        values.add(list.get(i).Name ?: "")
        values.add(list.get(i).Mobile ?: "")
        values.add(list.get(i).EmailId ?: "")
        values.add(list.get(i).Branchcode ?: "")
        values.add(list.get(i).Profile ?: "")
        values.add(list.get(i).LeadOwner + "-" + list.get(i).LeadOwnerName)
        values.add(list.get(i).Comments ?: "")
        values.add(list.get(i).Action ?: "")
        values.add(getDate(list.get(i).ActionDate?: ""))
        values.add(list.get(i).ActionUser + "-" +  list.get(i).ActionUserName)

        data.add(MappingHistoryData(setData(values)))
      }

      return MappingHistoryCombo(headers, data = data)
    } else {
      return MappingHistoryCombo(emptyList(), data = emptyList())
    }
  }
  fun List<IndirectLeadHistoryResponse>.mapToListOfResponse(): List<IndirectLeadHistoryResponse> {
    return safeMap {
      it.mapToResponse()
    }
  }
  private fun IndirectLeadHistoryResponse.mapToResponse(): IndirectLeadHistoryResponse {
    return IndirectLeadHistoryResponse(
      BusinessUnit = BusinessUnit ?: "",
      Name = Name ?: "",
      Mobile = Mobile ?: "",
      EmailId = EmailId ?: "",
      Branchcode = Branchcode ?: "",
      Profile = Profile ?: "",
      LeadOwner = LeadOwner ?: "",
      LeadOwnerName = LeadOwnerName ?: "",
      Comments = Comments ?: "",
      Action = Action ?: "",
      ActionDate = ActionDate ?: "",
      ActionUserName = ActionUserName ?: "",
      ActionUser = ActionUser ?: ""
    )
  }

  private fun renderIndirectProductTable(list: List<IndirectLeadMappingHistoryResponse>): MappingHistoryCombo {
    if (list.isNotEmpty()) {
      val headers = ArrayList<String>()
      headers.add("Product")
      headers.add("Stage")
      headers.add("Status")
      headers.add("Primary RM")
      headers.add("Secondary RM")
      headers.add("Assigned By")
      headers.add("Assigned Date")
      headers.add("Action")
      headers.add("Action User")
      headers.add("Action Date")

      val data: ArrayList<MappingHistoryData> = ArrayList()
      for (i in 0 until list.size) {
        val values: ArrayList<String> = ArrayList()
        values.add(list.get(i).Product ?: "")
        values.add(list.get(i).Stage ?: "")
        values.add(list.get(i).Status ?: "")
        values.add(list.get(i).PrimaryRM + "-" + list.get(i).PrimaryRMname )
        values.add(list.get(i).SecondaryRM + "-" + list.get(i).SecondaryRMname)
        values.add(list.get(i).AssignedBy + "-" + list.get(i).AssignedByName)
        values.add(getDate(list.get(i).AssignedDate?: "") )
        values.add(list.get(i).Action ?: "")
        values.add(list.get(i).ActionUser + "-" + list.get(i).ActionUserName)
        values.add(getDate(list.get(i).ActionDate?: ""))
        data.add(MappingHistoryData(setData(values)))
      }

      return MappingHistoryCombo(headers, data = data)
    } else {
      return MappingHistoryCombo(emptyList(), data = emptyList())
    }
  }

  private  fun getDate(date : String) : String{
    try {
     return getFormatedDate(date, AppDateFormats.DF_JSON_DATE_4, AppDateFormats.DF_JSON_DATE6)
    }catch (e : Exception){
      return ""
    }
  }
  fun List<IndirectLeadMappingHistoryResponse>.mapToListOfProductResponse(): List<IndirectLeadMappingHistoryResponse> {
    return safeMap {
      it.mapToProductResponse()
    }
  }
  private fun IndirectLeadMappingHistoryResponse.mapToProductResponse(): IndirectLeadMappingHistoryResponse {
    return IndirectLeadMappingHistoryResponse(
      Product = Product ?: "",
      Stage = Stage ?: "",
      Status = Status ?: "",
      PrimaryRM = PrimaryRM ?: "",
      PrimaryRMname= PrimaryRMname ?: "",
      SecondaryRM = SecondaryRM ?: "",
      SecondaryRMname = SecondaryRMname ?: "",
      AssignedBy = AssignedBy ?: "",
      AssignedByName = AssignedByName ?: "",
      AssignedDate = AssignedDate ?: "",
      Action = Action ?: "",
      ActionUser = ActionUser ?: "",
      ActionUserName = ActionUserName ?: "",
      ActionDate = ActionDate ?: ""
    )
  }

}