package com.mosl.tabapp.util.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.actionbar.Actionbar.Companion.SEARCH_TYPE_CLIENT
import com.mosl.tabapp.actionbar.Actionbar.Companion.SEARCH_TYPE_LEAD
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.EkycModule
import com.mosl.tabapp.model.SearchResultModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl

class AutoCompleteAdapter(
  val context: Context,
  val userManager: UserManager,
  val clickListener: SearchResultClickListener
) : BaseAdapter(), Filterable {
  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private val resultsList = ArrayList<SearchResultModel>()
  lateinit var navigator: ScreenNavigatorImpl
  lateinit var convertToEkyc: ConvertToEkyc
  lateinit var msgDialog: MsgDialog

  fun setScreenNavigator(screenNavigator: ScreenNavigatorImpl) {
    this.navigator = screenNavigator
  }

  fun setConvertToEkycListener(convertToEkyc: ConvertToEkyc) {
    this.convertToEkyc = convertToEkyc
  }

  fun setErrorMsgDialogListener(msgDialog: MsgDialog) {
    this.msgDialog = msgDialog
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
    if (resultModel.isEmpType(resultModel.searchType)) {
      if (name.equals(NO_RECORDS_FOUND)) {
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientID.visibility = View.GONE
        holderView.clientUcid.visibility = View.GONE
        holderView.error.visibility = View.VISIBLE
      } else {
        holderView.llExpand.setOnClickListener({
          val bundle = Bundle()
          bundle.putString(PROFILE_ID, resultModel.businessProfileId)
          bundle.putString(PRODUCT_ID, resultModel.productId)
          bundle.putString(EMPLOYEE_CODE, resultModel.empCode)
          openPage(NavigationConstants.NAVIGATE_TO_DASHBOARD, bundle)
        })
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientName.setText(String.format("Name :%s", name))
        holderView.clientUcid.setText(
            String.format("Business Profile: %s", resultModel.businessProfileName)
        )
        holderView.clientID.setText(String.format("Emp Id: %s,", resultModel.empCode))
        holderView.error.visibility = View.GONE
      }
    } else {
      holderView.clientName.setText(name)
      val clientCode = resultModel.clientCode
      val creacteMeetingId = resultModel.createMeetingId
      if (resultModel.hasClientCode()) {
        holderView.clientID.setText(String.format("Lead Id/Client Code: %s", clientCode))
      } else {
        holderView.clientID.setText(String.format("Lead Id/Client Code: %s", resultModel.leadId))
      }
      val ucid = resultModel.ucid
      holderView.clientUcid.setText(String.format("UCID: %s", ucid))
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

      if (userManager.user.isAufsb()) {
        holderView.rlSetAppointment.visibility = View.GONE
        holderView.rlCreateMeeting.visibility = View.GONE
      }
      val bundle = Bundle()
      bundle.putString(LEADID, resultModel.leadId)
      bundle.putString(UCID, resultModel.ucid)
      holderView.rlSetAppointment.setOnClickListener({
        openPage(NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT, bundle)
      })
      holderView.rlCreateMeeting.setOnClickListener({
        if (creacteMeetingId == 0) {
          openPage(NavigationConstants.NAVIGATE_TO_CREATE_MEETING, bundle)
        } else if (creacteMeetingId == 1) {
          msgDialog.erroMsgDialog(context.resources.getString(R.string.mgs_update_address))
        } else if (creacteMeetingId == 2) {
          msgDialog.erroMsgDialog(context.resources.getString(R.string.mgs_update_profile))
        } else if (creacteMeetingId == 3) {
          msgDialog.erroMsgDialog(context.resources.getString(R.string.mgs_update_profile_address))
        }

      })
      holderView.rlViewDetail.setOnClickListener({
        bundle.putString(MOBILE, resultModel.mobileNo)
        openPage(NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle)
      })
      holderView.rlOpenAccount.setOnClickListener({
        clearAll()
        val data = EkycModule(
            mobile = resultModel.mobileNo, email = resultModel.email,
            branchEmpCode = resultModel.leadOwner, interestedIn = resultModel.interestedIn,
            prospectSourceDetails = resultModel.prospectSourceDetails,
            prospectSource = resultModel.prospectSourceName,
            source = resultModel.sourceName
        )
        convertToEkyc.gotoEkycPage(data)
        clickListener.onSearchResultClick()
      })
      holderView.rlOAOForm.setOnClickListener({
        navigator.openNewEkycApp(true, resultModel.oaoUserId.toString())
        clickListener.onSearchResultClick()
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
      if (resultModel.leadId.isNullOrEmpty()) {
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientID.visibility = View.GONE
        holderView.clientUcid.visibility = View.GONE
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
    clearAll()
    navigator.openPage(navigationConstants, bundle, true, false)
    clickListener.onSearchResultClick()
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

  interface ConvertToEkyc {
    fun gotoEkycPage(ekycModule: EkycModule)
  }

  interface MsgDialog {
    fun erroMsgDialog(msg: String)
  }

  interface SearchResultClickListener {
    fun onSearchResultClick()
  }
}