package com.mosl.tabapp.util.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.mosl.tabapp.R
import com.mosl.tabapp.actionbar.Actionbar.Companion.SEARCH_TYPE_CLIENT
import com.mosl.tabapp.actionbar.Actionbar.Companion.SEARCH_TYPE_LEAD
import com.mosl.tabapp.constants.MOSLConstants.LEADID
import com.mosl.tabapp.constants.MOSLConstants.MOBILE
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.model.SearchResultModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl

class AutoCompleteAdapter(
  context: Context
) : BaseAdapter(), Filterable {
  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private val resultsList = ArrayList<SearchResultModel>()
  lateinit var navigator: ScreenNavigatorImpl

  fun setScreenNavigator(screenNavigator: ScreenNavigatorImpl) {
    this.navigator = screenNavigator
  }

  override fun getView(
    position: Int,
    oldView: View?,
    parent: ViewGroup?
  ): View {
    val convertView = if (oldView == null) {
      inflater.inflate(R.layout.crm_client_search_item, parent, false)
          .apply {
            tag = HolderView(this)
          }
    } else oldView

    val holderView: HolderView = convertView.tag as HolderView
    val resultModel = resultsList.get(position)
    val name = resultModel.name
    if (name.isEmpty() || name.isBlank()) {
      holderView.llExpand.visibility = View.GONE
    } else {
      holderView.llExpand.visibility = View.VISIBLE
    }
    if (resultModel.isEmpType) {
      if (name.equals(NO_RECORDS_FOUND)) {
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientID.setText("")
        holderView.clientUcid.setText("")
        holderView.clientName.setText("")
        holderView.error.visibility = View.GONE
      } else {
        holderView.llExpand.setOnClickListener(View.OnClickListener {

        })
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientName.setText("Name : $name")
        holderView.clientUcid.setText(resultModel.businessProfileName)
        holderView.clientID.setText("Emp Id: $resultModel.empCode")
        holderView.error.visibility = View.GONE
      }
    } else {
      holderView.clientName.setText(name)
      val clientCode = resultModel.clientCode
      if (resultModel.hasClientCode()) {
        holderView.clientID.setText("Lead Id/Client Code: $clientCode")
      } else {
        holderView.clientID.setText("Lead Id/Client Code: ${resultModel.leadId}")
      }
      val ucid = resultModel.ucid
      holderView.clientUcid.setText("UCID: $ucid")
      var ekyMenuType = SEARCH_TYPE_LEAD
      if (!ucid.equals("0")) {
        ekyMenuType = SEARCH_TYPE_CLIENT
      } else {
        ekyMenuType = SEARCH_TYPE_LEAD
      }
      if (ekyMenuType.equals(SEARCH_TYPE_LEAD) || ekyMenuType.equals(SEARCH_TYPE_CLIENT)) {
        if (resultModel.isOaoUserIdValid) {
          holderView.rlOpenAccount.visibility = View.GONE
          holderView.rlOAOForm.visibility = View.VISIBLE
        } else {
          holderView.rlOpenAccount.visibility = View.VISIBLE
          holderView.rlOAOForm.visibility = View.GONE
        }
      }
      //AUFSB USer Cond
      val bundle = Bundle()
      bundle.putString(LEADID, resultModel.leadId)
      bundle.putString(UCID, resultModel.ucid)
      holderView.rlSetAppointment.setOnClickListener({
        openPage(NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT, bundle)
      })
      holderView.rlCreateMeeting.setOnClickListener({
        openPage(NavigationConstants.NAVIGATE_TO_CREATE_MEETING, bundle)
      })
      holderView.rlViewDetail.setOnClickListener({
        bundle.putString(MOBILE, resultModel.mobileNo)
        openPage(NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle)
      })
      holderView.rlOAOForm.setOnClickListener({
        navigator.openNewEkycApp(true, resultModel.oaoUserId.toString())
      })
      holderView.llExpand.setOnClickListener({
        if (!resultModel.isExpanded) {
          holderView.llDropDownMenuLayout.visibility = View.VISIBLE
          resultModel.isExpanded = true
        } else {
          holderView.llDropDownMenuLayout.visibility = View.GONE
          resultModel.isExpanded = false
        }
      })
      if (resultModel.leadId.isEmpty()) {
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientID.setText("")
        holderView.clientUcid.setText("")
        holderView.clientName.setText("")
      } else {
        holderView.ivDropDownArrow.visibility = View.VISIBLE
        holderView.error.visibility = View.GONE
      }
    }
    return convertView
  }

  fun openPage(
    navigationConstants: Int,
    bundle: Bundle
  ) {
    navigator.openPage(navigationConstants, bundle)
  }

  override fun getItem(position: Int): SearchResultModel {
    return resultsList[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return resultsList.size
  }

  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(constraint: CharSequence?): Filter.FilterResults? {
        return null
      }

      override fun publishResults(
        constraint: CharSequence?,
        results: Filter.FilterResults?
      ) {
      }
    }
  }

  fun setResultList(results: List<SearchResultModel>) {
    if (results.isEmpty()) {
      notifyDataSetInvalidated()
    } else {
      resultsList.clear()
      resultsList.addAll(results)
      notifyDataSetChanged()
    }
  }

  fun clearAll() {
    resultsList.clear()
    notifyDataSetChanged()
  }

  internal class HolderView(parent: View) {
    val clientName: TextView = parent.findViewById(R.id.tvClientname)
    val clientUcid: TextView = parent.findViewById(R.id.tvUCID)
    val clientID: TextView = parent.findViewById(R.id.tvClientID)
    val error: TextView = parent.findViewById(R.id.tvError)
    val llExpand: LinearLayout = parent.findViewById(R.id.llExpand)
    val llDropDownMenuLayout: LinearLayout = parent.findViewById(R.id.llDropDownMenuLayout)
    val rlSetAppointment: RelativeLayout = parent.findViewById(R.id.rlSetAppointment)
    val rlCreateMeeting: RelativeLayout = parent.findViewById(R.id.rlCrateMeeting)
    val rlViewDetail: RelativeLayout = parent.findViewById(R.id.rlViewDetail)
    val rlOpenAccount: RelativeLayout = parent.findViewById(R.id.rlOpenAccount)
    val rlOAOForm: RelativeLayout = parent.findViewById(R.id.rlOAOForm)
    val ivDropDownArrow: ImageView = parent.findViewById(R.id.ivDropDownArrow)
  }

  companion object {
    const val NO_RECORDS_FOUND = "No records found"
  }
}