package com.mosl.tabapp.dashboard.leads.report

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.core.rx.applyUiModel
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.saathi.api.model.dashboard.CommonRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.databinding.FragTableLeadShowBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap
import javax.inject.Inject

class LeadsReportFragmentKotlin : CommonFragment(), LeadsReportView, View.OnClickListener {
  lateinit var binding: FragTableLeadShowBinding
  lateinit var dashboardModel: DashboardModel
  lateinit var map: HashMap<String, String>
  lateinit var widgetSystemName: String
  private val addtionalParameterMap: Map<String, String> =
    HashMap()

  @Inject
  lateinit var presenter: LeadsReportPresenter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding = FragTableLeadShowBinding.bind(view)
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    hideAppTitle()
    setUpView()
  }

  override fun getLayout(): Int {
    return R.layout.frag_table_lead_show
  }

  private fun setUpView() {
    binding.tvShowLess.setOnClickListener(this)
  }

  private fun renderTable(responseJsonObj: Map<String, Any>) {
    val leadTableAdapter =
      LeadsTableAdapter(
        requireActivity(),
        getAddtionalParameterMap(),
        navigator,
        getWidgetSystemsName()
      )
    binding.table.adapter = leadTableAdapter
    newPrepareGroupMap(responseJsonObj, leadTableAdapter) //prepare group map
    val arrayDetail =
      responseJsonObj["Table"] as List<Map<String, Any>>?
    val columns = ArrayList<String>()
    val header = ArrayList<String>()
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
            var stockArr = arrayOfNulls<String>(header.size)
            stockArr = header.toArray(stockArr)
            leadTableAdapter.setHeadersSecond(stockArr)
          }
          val objDetail = this[y]
          var stockArr = arrayOfNulls<String>(columns.size)
          stockArr = columns.toArray(stockArr)
          leadTableAdapter.setHeaders(stockArr)
          val data =
            LinkedHashMap<String, String?>()
          for (column in columns) {
            val columnData = if (objDetail[column]
                .toString()
              == ""
            ) "0" else objDetail[column].toString()
            data[column] = columnData
          }
          leadTableAdapter.renderData(data)
        }
        leadTableAdapter.notifyDataSetChanged()
        binding.table.visibility = View.VISIBLE
        binding.tvNochart.visibility = View.GONE
      } else {
        binding.table.visibility = View.GONE
        binding.tvNochart.visibility = View.VISIBLE
      }
    }
  }

  override fun renderData(uiModel: UiModel<Map<String, Any>>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
      binding.table.visibility = View.GONE
      binding.tvNochart.visibility = View.VISIBLE
    }
    uiModel.onSuccess {
      renderTable(it)
    }
  }

  private fun newWebServiceCall(
    systemName: String?,
    getAddtionalParameterMap: Map<String, String>?
  ) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      val businessProfileId = dashboardModel.businessProfileIdForDashboard
      val request = CommonRequest(
        businessProfileId,
        getAddtionalParameterMap,
        systemName,
        dashboardModel.productId,
        dashboardModel.employeeCode
      )
      val subscribe = saathiApi.getDashboardData(request)
        .map<Map<String, Any?>> { responseS: String? ->
          val mGson = Gson()
          val type =
            object : TypeToken<HashMap<String?, Any?>?>() {}.type
          val response =
            mGson.fromJson<HashMap<String, Any>>(responseS, type)
          val responseJsonObj =
            response["Response"] as Map<String, Any>?
          val status = response[MOSLConstants.STATUS].toString()
          if (status.equals("true", ignoreCase = true)) {
            return@map responseJsonObj
          } else {
            val message = response[MOSLConstants.MESSAGE].toString()
            throw ServiceFailureException(message, message)
          }
        }
        .compose(applyUiModel())
        .subscribe { uiModel: UiModel<Map<String, Any?>> ->
          // renderData(uiModel)
        }
    } else {
      noInternetConnectionDialog()
    }
  }

  override fun onClick(view: View) {
    val id = view.id
    val args = Bundle()
    val leadsFragment: CommonFragment = LeadsFragment()
    if (id == R.id.tvShowLess) {
      arguments?.apply {
        args.putInt(
          LeadsFragment.FLT_BANKNAME,
          this.getInt(LeadsFragment.FLT_BANKNAME)
        )
        args.putInt(
          LeadsFragment.FLT_BANKBRANCHCODE,
          this.getInt(LeadsFragment.FLT_BANKBRANCHCODE)
        )
        args.putInt(
          LeadsFragment.FLT_BANKSUBBROKERCODE,
          this.getInt(LeadsFragment.FLT_BANKSUBBROKERCODE)
        )
        args.putSerializable(MOSLConstants.DASHBOARD_MODEL, dashboardModel)
        args.putInt(
          LeadsFragment.PERIOD_FILTER,
          this.getInt(LeadsFragment.PERIOD_FILTER)
        )
        args.putInt(
          LeadsFragment.PRODUCT_FILTER,
          this.getInt(LeadsFragment.PRODUCT_FILTER)
        )
        args.putInt(
          LeadsFragment.SOURCE_FILTER,
          this.getInt(LeadsFragment.SOURCE_FILTER)
        )
      }
      leadsFragment.arguments = args
      requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, leadsFragment)
        .commit()
    }
  }

  fun newPrepareGroupMap(
    responseJsonObj: Map<String, Any?>,
    leadTableAdapter: LeadsTableAdapter
  ) {
    try {
      if (responseJsonObj["Table2"] == null) return
      val jsonArray =
        responseJsonObj["Table2"] as List<Map<String, Any>>?
      val tableHeaderMap: MutableMap<Int, MutableList<Map<Int, String>>> =
        HashMap()
      if (jsonArray == null) return
      for (ctr in jsonArray.indices) {
        val jsonRowObject = jsonArray[ctr]
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
        leadTableAdapter.setTableMap(tableHeaderMap)
      }
    } catch (ex: Exception) {
      ex.printStackTrace()
    }
  }

  override fun getWidgetSystemsName(): String? {
    return arguments?.getString(MOSLConstants.SYSTEM_NAME)
  }

  override fun getAddtionalParameterMap(): Map<String, String> {
    return arguments?.getSerializable(MOSLConstants.MAP) as HashMap<String, String>
  }

}