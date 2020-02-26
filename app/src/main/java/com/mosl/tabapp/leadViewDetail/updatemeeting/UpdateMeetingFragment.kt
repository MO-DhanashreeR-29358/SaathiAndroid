package com.mosl.tabapp.leadViewDetail.updatemeeting

import android.os.Bundle
import android.view.View
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragUpdateMeetingBinding
import com.mosl.tabapp.model.AddedAttendeeModel
import com.mosl.tabapp.model.AddedSplAttendeeModel
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

class UpdateMeetingFragment : CommonFragment(), UpdateMeetingView {

  @Inject
  internal lateinit var presenter: UpdateMeetingPresenter

  lateinit var binding: FragUpdateMeetingBinding

  override fun getLayout(): Int {
    return R.layout.frag_update_meeting
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    setAppTitle("Update Meeting")
    binding = FragUpdateMeetingBinding.bind(view)
    presenter.start()
    dataActionSubject.onNext(Fetch)
  }

  private var meetingDate: String? = null
  private var activityTypeId: String? = null

  private var meetingReasonID: String? = null
  private var actionableID: String? = null
  private var categoryID: String? = null
  private var subCategoryID: String? = null

  private val addedAttendeeModelList = ArrayList<AddedAttendeeModel>()
  private val addedSplAttendeeModelList = ArrayList<AddedSplAttendeeModel>()
  private var addedAttendeeCount: Int = 0
  private var addedSplAttendeeCount: Int = 0
  private var currentYear: Int = 0
  private var month: Int = 0
  private var day: Int = 0
  private var hour: Int = 0
  private var minute: Int = 0

  override fun renderMeetingById(uiModel: UiModel<Meeting>) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess { data ->
      meetingDate = stringOrEmpty(data.meetingDate)
      activityTypeId = stringOrEmpty(data.activityTypeID)
      val productTypeID = stringOrEmpty(data.productTypeID)

      meetingReasonID = stringOrEmpty(data.meetingReasonID)
      val meetingDescription = stringOrEmpty(data.meetingDescription)
      val customerRemark = stringOrEmpty(data.customerRemark)
      actionableID = stringOrEmpty(data.actionableID)
      categoryID = stringOrEmpty(data.categoryID)
      val categoryDescription = stringOrEmpty(data.categoryDescription)
      subCategoryID = stringOrEmpty(data.subCategoryID)
      val subCategoryDescription = stringOrEmpty(data.subCategoryDescription)
      val meetingAttendee = data.meetingAttendeeList
      for (attendee in meetingAttendee) {
        val meetingAttendeeID = stringOrEmpty(attendee.meetingAttendeeID)
        val attendeeEmpcode = stringOrEmpty(attendee.attendeeEmpcode)
        val attendeeType = stringOrEmpty(attendee.attendeeType)
        val defaultAttendee = attendee.defaultAttendee
        val attendeeEmpName = stringOrEmpty(attendee.attendeeEmpName)

        if (attendeeType.equals("S", ignoreCase = true)) {
          addedSplAttendeeModelList.add(
              AddedSplAttendeeModel(
                  attendeeType, true,
                  attendeeEmpName, attendeeEmpcode,
                  meetingAttendeeID
              )
          )
          addedSplAttendeeCount++
        } else {
          if (defaultAttendee) {
            addedAttendeeModelList.add(
                AddedAttendeeModel(
                    attendeeType, true,
                    attendeeEmpName, attendeeEmpcode,
                    meetingAttendeeID
                )
            )
            addedAttendeeCount++
          }
        }
      }

      setCountForSpecial(addedSplAttendeeCount)
      setCount(addedAttendeeCount)
      selectProduct(productTypeID)

      try {
        val metadataArray = meetingDate!!.split("T".toRegex())
            .dropLastWhile({ it.isEmpty() })
            .toTypedArray()
        val dateArray = metadataArray[0].split("-".toRegex())
            .dropLastWhile({ it.isEmpty() })
            .toTypedArray()
        val timeArray = metadataArray[1].split(":".toRegex())
            .dropLastWhile({ it.isEmpty() })
            .toTypedArray()
        currentYear = Integer.parseInt(dateArray[0])
        month = Integer.parseInt(dateArray[1])
        day = Integer.parseInt(dateArray[2])
        hour = Integer.parseInt(timeArray[0])
        minute = Integer.parseInt(timeArray[1])
      } catch (e: Exception) {
        Timber.d(e)
      }

      binding.etMeetingDescription.setText(meetingDescription)
      binding.etCustomerComment.setText(customerRemark)
      binding.etCategory.setText(categoryDescription)
      binding.etSubCategory.setText(subCategoryDescription)
      meetingDate = meetingDate!!.replace("T", " ")
      binding.tvDate.setText(
          CRMAppUtil.getDBDate(
              CRMAppUtil.getDBDateObj(
                  currentYear, month, day, hour,
                  minute
              ), DATE_FORMAT_TO_DISPLAY
          )
      )

    }
  }

  private fun setCountForSpecial(cnt: Int) {
    binding.tvSplAttendeeCount.text = if (cnt <= 0) {
      ""
    } else {
      String.format("(%s)", cnt.toString())
    }
  }

  private fun setCount(cnt: Int) {
    binding.tvAttendeeCount.text = if (cnt <= 0) {
      ""
    } else {
      String.format("(%s)", cnt.toString())
    }
  }

  private fun selectProduct(id: String) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      meetingDuration(meetingId)
      val request = LookupRequest(
          userManager.getUser()!!.currentBusinessProfile.businessUnitId!!, INTERESTEDIN, null, null
      )
      val subscribe = saathiApi.getAllLookup(request)
          .compose<UiModel<List<LookupItem>>>(applyUiModel<List<LookupItem>>())
          .subscribe { uiModelList -> renderProduct(uiModelList, id) }
    } else {
      noInternetConnectionDialog()
    }
  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}