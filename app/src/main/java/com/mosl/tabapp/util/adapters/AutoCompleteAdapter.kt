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
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.R
import com.mosl.tabapp.actionbar.Actionbar.Companion.SEARCH_TYPE_CLIENT
import com.mosl.tabapp.actionbar.Actionbar.Companion.SEARCH_TYPE_LEAD
import com.mosl.tabapp.constants.MOSLConstants.EMPLOYEE_CODE
import com.mosl.tabapp.constants.MOSLConstants.LEADID
import com.mosl.tabapp.constants.MOSLConstants.MOBILE
import com.mosl.tabapp.constants.MOSLConstants.PRODUCT_ID
import com.mosl.tabapp.constants.MOSLConstants.PROFILE_ID
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.EkycModule
import com.mosl.tabapp.model.SearchModelNew
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl

class AutoCompleteAdapter(
  val context: Context,
  val userManager: UserManager,
  private val clickListener: SearchResultClickListener
) : BaseAdapter(), Filterable {
  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private val resultsList = ArrayList<SearchModelNew>()
  lateinit var navigator: ScreenNavigatorImpl
  private lateinit var convertToEkyc: ConvertToEkyc
  private lateinit var msgDialog: MsgDialog

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
    val convertView = oldView
      ?: inflater.inflate(R.layout.crm_client_search_item, parent, false)
        .apply {
          tag = HolderView(this)
        }

    val holderView: HolderView = convertView.tag as HolderView
    val resultModel = resultsList[position]
    val name = resultModel.name
    if (name.isEmpty() || name.isBlank()) {
      holderView.llExpand.visibility = View.GONE
    } else {
      holderView.llExpand.visibility = View.VISIBLE
    }
    if (resultModel.isEmpType(resultModel.searchType)) {
      if (name == NO_RECORDS_FOUND) {
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientID.visibility = View.GONE
        holderView.clientUcid.visibility = View.GONE
        holderView.error.visibility = View.VISIBLE
      } else {
        holderView.llExpand.setOnClickListener {
          val bundle = Bundle()
          bundle.putString(PROFILE_ID, resultModel.businessProfileId)
          bundle.putString(PRODUCT_ID, resultModel.productId)
          bundle.putString(EMPLOYEE_CODE, resultModel.empCode)
          openPage(NavigationConstants.NAVIGATE_TO_DASHBOARD, bundle)
        }
        holderView.ivDropDownArrow.visibility = View.GONE
        holderView.clientName.text = String.format("Name :%s", name)
        holderView.clientUcid.text =
          String.format("Business Profile: %s", resultModel.businessProfileName)

        holderView.clientID.text = String.format("Emp Id: %s,", resultModel.empCode)
        holderView.error.visibility = View.GONE
      }
    } else {
      holderView.clientName.text = name
      val clientCode = resultModel.clientCode
      val creacteMeetingId = resultModel.createMeetingId
      if (resultModel.hasClientCode()) {
        holderView.clientID.text = String.format("Lead Id/Client Code: %s", clientCode)
      } else {
        holderView.clientID.text = String.format("Lead Id/Client Code: %s", resultModel.leadId)
      }
      val ucid = resultModel.ucid
      holderView.clientUcid.text = String.format("UCID: %s", ucid)
      val ekyMenuType: String
      ekyMenuType = if (ucid != "0") {
        SEARCH_TYPE_CLIENT
      } else {
        SEARCH_TYPE_LEAD
      }
      if (ekyMenuType == SEARCH_TYPE_LEAD || ekyMenuType == SEARCH_TYPE_CLIENT) {
        userManager.getUser()?.apply {
          if (this.isEkycEnable()) {
            if (resultModel.isOaoUserIdValid) {
              holderView.rlOpenAccount.visibility = View.GONE
              holderView.rlOAOForm.visibility = View.VISIBLE
            } else {
              holderView.rlOpenAccount.visibility = View.VISIBLE
              holderView.rlOAOForm.visibility = View.GONE
            }
          }
        }
        if (resultModel.isDiyLinkValid) {
          holderView.rlDiyLink.visibility = View.VISIBLE
        } else {
          holderView.rlDiyLink.visibility = View.GONE
        }
      }


      userManager.getUser()?.apply {
        if (this.isAufsb()) {
          holderView.rlSetAppointment.visibility = View.GONE
          holderView.rlCreateMeeting.visibility = View.GONE
        }
      }

      val bundle = Bundle()
      bundle.putString(LEADID, resultModel.leadId)
      bundle.putString(UCID, resultModel.ucid)
      holderView.rlSetAppointment.setOnClickListener {
        openPage(NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT, bundle)
      }
      holderView.rlCreateMeeting.setOnClickListener {
        when (creacteMeetingId) {
          0 -> {
            openPage(NavigationConstants.NAVIGATE_TO_CREATE_MEETING, bundle)
          }
          1 -> {
            msgDialog.erroMsgDialog(context.resources.getString(R.string.mgs_update_address))
          }
          2 -> {
            msgDialog.erroMsgDialog(context.resources.getString(R.string.mgs_update_profile))
          }
          3 -> {
            msgDialog.erroMsgDialog(context.resources.getString(R.string.mgs_update_profile_address))
          }
        }

      }
      holderView.rlViewDetail.setOnClickListener {
        bundle.putString(MOBILE, resultModel.mobileNo)
        openPage(NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle)
      }
      holderView.rlOpenAccount.setOnClickListener {
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
      }

      holderView.rlDiyLink.setOnClickListener {
        convertToEkyc.renderDiyLink(resultModel.leadId, null)
        clickListener.onSearchResultClick()
      }
      holderView.rlOAOForm.setOnClickListener {
        navigator.openNewEkycApp(true, resultModel.oaoUserId.toString())
        clickListener.onSearchResultClick()
      }
      holderView.llExpand.setOnClickListener {
        if (!resultModel.isExpanded) {
          holderView.llDropDownMenuLayout.visibility = View.VISIBLE
          resultModel.isExpanded = true
        } else {
          holderView.llDropDownMenuLayout.visibility = View.GONE
          resultModel.isExpanded = false
        }
      }
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

  override fun getItem(position: Int): SearchModelNew {
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
      override fun performFiltering(constraint: CharSequence?): FilterResults? {
        return null
      }

      override fun publishResults(
        constraint: CharSequence?,
        results: FilterResults?
      ) {
      }
    }
  }

  fun setResultList(results: List<SearchModelNew>) {
    if (results.isEmpty()) {
      notifyDataSetInvalidated()
    } else {
      resultsList.clear()
      resultsList.addAll(results)
      notifyDataSetChanged()
    }
  }

  private fun clearAll() {
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
    val rlDiyLink: RelativeLayout = parent.findViewById(R.id.rlDiyLink)
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
    fun renderDiyLink(
      leadId: String,
      callBack: DiyLinkClickListner?
    )
  }

  interface MsgDialog {
    fun erroMsgDialog(msg: String)
  }

  interface SearchResultClickListener {
    fun onSearchResultClick()
  }

  interface DiyLinkClickListner {
    fun getDiyUrl(url: String)
  }
}