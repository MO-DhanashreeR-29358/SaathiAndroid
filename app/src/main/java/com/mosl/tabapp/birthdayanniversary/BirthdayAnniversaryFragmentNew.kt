package com.mosl.tabapp.birthdayanniversary

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryItemResponse
import com.mosl.mobile.domain.birthdayanniversary.FilterInput
import com.mosl.mobile.domain.birthdayanniversary.FiltersItemInput
import com.mosl.mobile.domain.birthdayanniversary.ProfileSpinner
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.GetDataList
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.SendEmailAll
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.SendGift
import com.mosl.tabapp.birthdayanniversary.item.ItemBirthdayAnniversary
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentBirthdayAnniversaryNewBinding
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BirthdayAnniversaryFragmentNew : CommonFragment(), BirthdayAnniversaryView {
  lateinit var binding: FragmentBirthdayAnniversaryNewBinding
  @Inject lateinit var presenter: BirthdayAnniversaryPresenter
  private lateinit var section: DataManagerSection
  private lateinit var spinnerProfileAdapter: ArrayAdapter<ProfileSpinner>
  private lateinit var spinnerEventAdapter: ArrayAdapter<String>
  private val subject = PublishSubject.create<BirthdayAnniversaryUiEvent>()
  private var filterType = "2"
  private var dayType = TODAY_FLAG
  private var dataFoundFlag = false
  override fun getLayout(): Int {
    return R.layout.fragment_birthday_anniversary_new
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentBirthdayAnniversaryNewBinding.bind(view)
    setUpRecyclerView()
    setUpView()
    presenter.start()
    dataActionSubject.onNext(DataAction.Fetch)
  }

  private fun setUpView() {
    binding.apply {
      val context = requireContext()
      spinnerProfileAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      spProfile.adapter = spinnerProfileAdapter

      spinnerEventAdapter = SpinnerAdapterFactory.getDefaultAdapter(context)
      spEventType.adapter = spinnerEventAdapter
      val eventTypeArray =
        context.resources.getStringArray(R.array.BirthdayAniversary)
      val arrEventType = listOf(*eventTypeArray)
      spinnerEventAdapter.setSpinnerData(spEventType, arrEventType)
    }

  }

  private fun setUpRecyclerView() {
    val groupAdapter = GroupAdapter<ViewHolder>()
    section = DataManagerSection(onRetryClick)
    section.retrySubject = dataActionSubject
    groupAdapter.add(section)
    binding.rvBirthdayAnniversary.apply {
      layoutManager = GridLayoutManager(requireContext(), 3)
      setHasFixedSize(true)
      adapter = groupAdapter
    }
  }

  override fun onProfileSpinnerSelection(): Observable<Int> {
    return binding.spProfile.itemSelections()
  }

  override fun onEventTypeSpinnerSelection(): Observable<Int> {
    return binding.spEventType.itemSelections()
  }

  override fun getProfileValue(): ProfileSpinner {
    return binding.spProfile.selectedItem as ProfileSpinner
  }

  override fun getBusinessId(): String {
    return userManager.getUser()?.currentBusinessProfile?.businessUnitId!!
  }

  override fun getFilterType(): String {
    return filterType
  }

  override fun getEventType(): String {
    return binding.spEventType.selectedItem as String
  }

  override fun renderProfileSpinner(uiModel: UiModel<List<LookupItemResponse>>) {
    val profileSpinnerList = ArrayList<ProfileSpinner>()
    profileSpinnerList.add(ProfileSpinner(-1, "All"))
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {

      it.safeMap { data ->
        profileSpinnerList.add(ProfileSpinner(data.lookUpid, data.lookUpName!!))
      }

    }
    spinnerProfileAdapter.setSpinnerData(binding.spProfile, profileSpinnerList)
  }

  override fun getDataList(): Observable<GetDataList> {
    return subject.ofType()
  }

  override fun renderGetData() {
    getData()
  }

  override fun onEmailAllClick(): Observable<Unit> {
    return binding.ivEmailAll.clicks()
  }

  override fun onButtonTodayClick(): Observable<Unit> {
    return binding.btnToday.clicks().map {
      filterType = "2"
      dayType = "today"
      CRMAppUtil.expand(binding.ivEmailAll)
    }
  }

  override fun onButtonBelatedClick(): Observable<Unit> {
    return binding.btnBelated.clicks().map {
      filterType = "1"
      dayType = "belated"
      CRMAppUtil.collapse(binding.ivEmailAll)
    }
  }

  override fun onButtonUpcomingClick(): Observable<Unit> {
    return binding.btnUpcomming.clicks().map {
      filterType = "3"
      dayType = "upcoming"
      CRMAppUtil.collapse(binding.ivEmailAll)
    }
  }

  override fun sendEmailAll(): Observable<SendEmailAll> {
    return subject.ofType()
  }

  override fun sendGift(): Observable<SendGift> {
    return subject.ofType()
  }

  override fun renderSendGift(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {
      MSFDialog.alertDialog(
        requireContext(), 0,
        resources.getString(string.motilal_oswal),
        MOSLConstants.GIFT_SEND,
        "Ok",
        true, null
      )
    }
  }

  override fun renderEmailAllClick() {
    if (dataFoundFlag) {
      if (!filterType.equals("3", ignoreCase = true)) {
        MSFDialog.alertDialog(
          requireContext(), 0,
          resources.getString(string.motilal_oswal), MOSLConstants.EMAIL_ALL_MESSAGE,
          "Yes", "No", true
        ) { action: Action, _: Array<Any?>? ->
          if (action == OK) {
            //sendEmailAll()
            val eventType = if (getEventType().equals("Birthday", ignoreCase = true)) {
              "B"
            } else {
              "A"
            }
            val dayTypes = if (dayType.equals("belated", ignoreCase = true)) {
              "-L"
            } else {
              "-T"
            }
            subject.onNext(
              SendEmailAll(
                null,
                true,
                eventType + dayTypes,
                userManager.getUser()?.employeeCode!!,
                filterType,
                "E"
              )
            )
          }
        }
      }
    }
  }

  override fun renderSendEmailAll(uiModel: UiModel<String>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {
      MSFDialog.alertDialog(
        requireContext(), 0,
        resources.getString(string.motilal_oswal),
        MOSLConstants.EMAIL_SEND,
        "Ok",
        true, null
      )
    }
  }

  override fun renderBirthdayAnniversary(uiModel: UiModel<BirthdayAnniversaryItemResponse>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
      dataFoundFlag = false

    }
    uiModel.onSuccess {
      section.clearContent()
      if (it.data.isNotEmpty()) {
        binding.tvNoData.visibility = View.GONE
        section.setContent(it.data.map { data ->
          ItemBirthdayAnniversary(
            data,
            filterType,
            requireContext(),
            subject,
            getEventType()
          )
        })
        dataFoundFlag = true
      } else {
        binding.tvNoData.visibility = View.VISIBLE
        if (getEventType().equals("Birthday", ignoreCase = true)) {
          binding.tvNoData.text =
            String.format(resources.getString(string.txt_no_clients_having_birthday), dayType)
        } else {
          binding.tvNoData.text =
            String.format(getString(string.txt_no_clients_having_anniversary), dayType)
        }
      }

    }
  }

  private fun getData() {
    val profileId: String
    val profileCodeStr = getProfileValue().id.toString()
    val profileNameStr = getProfileValue().lookUpName
    profileId = if (profileCodeStr.equals("-1", ignoreCase = true)) {
      ""
    } else {
      profileNameStr
    }

    val filtersItem = FiltersItemInput()
    val valueList = java.util.ArrayList<String>()
    valueList.add(profileId)
    filtersItem.value = valueList
    val filters = ArrayList<FiltersItemInput>()

    filters.add(filtersItem)
    val filter = FilterInput(filters, "and")

    subject.onNext(
      GetDataList(
        filter, true
      )
    )

  }

  companion object {
    private const val TODAY_FLAG = "today"

  }

}
