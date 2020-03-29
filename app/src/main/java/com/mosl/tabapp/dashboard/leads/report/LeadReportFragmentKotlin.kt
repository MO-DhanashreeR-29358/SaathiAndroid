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
import com.mosl.tabapp.dashboard.ClientsLeadsFragmentOld
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.databinding.FragTableLeadShowBinding
import com.mosl.tabapp.model.dashboard.DashboardModel
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap

class LeadReportFragmentKotlin : CommonFragment(), View.OnClickListener {
  lateinit var binding: FragTableLeadShowBinding
  lateinit var dashboardModel: DashboardModel
  lateinit var map: HashMap<String, String>
  lateinit var widgetSystemName: String
  val addtionalParameterMap: Map<String, String> =
    HashMap()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding = FragTableLeadShowBinding.bind(view)
    setUpView()
    //  presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
    hideAppTitle()
    setUpView()
  }

  override fun getLayout(): Int {
    return R.layout.frag_table_lead_show
  }

  private fun setUpView() {

    dashboardModel =
      arguments?.getSerializable(MOSLConstants.DASHBOARD_MODEL) as DashboardModel
    map = arguments
      ?.getSerializable(MOSLConstants.MAP) as HashMap<String, String>
    widgetSystemName = arguments?.getString(MOSLConstants.SYSTEM_NAME) ?: ""

    newWebServiceCall(widgetSystemName, map)
    binding.tvShowLess.setOnClickListener(this)
  }

  private fun renderLeadTable(responseJsonObj: Map<String, Any?>) {
    //hideProgress();
    val leadTableAdapter =
      LeadTableAdapter(requireActivity(), map, navigator, widgetSystemName)
    binding.table.adapter = leadTableAdapter
    //   newPrepareGroupMap(responseJsonObj, leadTableAdapter) //prepare group map
    val arrayDetail =
      responseJsonObj["Table"] as List<Map<String, Any>>?
    val columns = ArrayList<String>()
    val header = ArrayList<String>()
    if (arrayDetail!!.size != 0) {
      for (y in 2 until arrayDetail.size) {
        val objDetailSecond = arrayDetail[0]
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
        val objDetail = arrayDetail[y]
        var stockArr = arrayOfNulls<String>(columns.size)
        stockArr = columns.toArray(stockArr)
        leadTableAdapter.setHeaders(stockArr)
        val hm =
          LinkedHashMap<String, String?>()
        for (column in columns) {
          val columnData = if (objDetail[column]
              .toString()
            == ""
          ) "0" else objDetail[column].toString()
          hm[column] = columnData
        }
        leadTableAdapter.add(hm)
      }
      leadTableAdapter.notifyDataSetChanged()
      binding.table.visibility = View.VISIBLE
      binding.tvNochart.visibility = View.GONE
    } else {
      binding.table.visibility = View.GONE
      binding.tvNochart.visibility = View.VISIBLE
    }
  }

  private fun renderClientLeadShow(uiModel: UiModel<Map<String, Any?>>) {
    toggleProgress(true)
    uiModel.onFailure {
      toggleProgress(false)
      showOnFailurePopup(it)
      binding.tvNochart.visibility = View.VISIBLE
    }
    uiModel.onSuccess {
      toggleProgress(false)
      renderLeadTable(it)
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
          renderClientLeadShow(uiModel)
        }
    } else {
      noInternetConnectionDialog()
    }
  }

  override fun onClick(view: View) {
    val id = view.id
    if (id == R.id.tvShowLess) {
      val args = Bundle()
      val leadsFragment: CommonFragment
      if (this.arguments!!.getString(MOSLConstants.TYPESOFLEAD)
          .equals(MOSLConstants.LEAD, ignoreCase = true)
      ) {
        leadsFragment = ClientsLeadsFragmentOld()
      } else {
        leadsFragment = LeadsFragment()
        args.putInt(
          LeadsFragment.FLT_BANKNAME,
          this.arguments!!.getInt(LeadsFragment.FLT_BANKNAME)
        )
        args.putInt(
          LeadsFragment.FLT_BANKBRANCHCODE,
          this.arguments!!.getInt(LeadsFragment.FLT_BANKBRANCHCODE)
        )
        args.putInt(
          LeadsFragment.FLT_BANKSUBBROKERCODE,
          this.arguments!!.getInt(LeadsFragment.FLT_BANKSUBBROKERCODE)
        )
      }
      args.putSerializable(MOSLConstants.DASHBOARD_MODEL, dashboardModel)
      args.putInt(
        LeadsFragment.PERIOD_FILTER,
        this.arguments!!.getInt(LeadsFragment.PERIOD_FILTER)
      )
      args.putInt(
        LeadsFragment.PRODUCT_FILTER,
        this.arguments!!.getInt(LeadsFragment.PRODUCT_FILTER)
      )
      args.putInt(
        LeadsFragment.SOURCE_FILTER,
        this.arguments!!.getInt(LeadsFragment.SOURCE_FILTER)
      )
      leadsFragment.arguments = args
      requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.llReplaceClientFragment, leadsFragment)
        .commit()
    }
  }

}