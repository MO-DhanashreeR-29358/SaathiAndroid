package com.mosl.tabapp.birthdayanniversary

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.mosl.app.core.data.util.safeMap
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.error.ErrorState.NO_DATA_ERROR
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
import com.mosl.tabapp.databinding.FragmentBirthdayAnniversaryBinding
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BirthdayAnniversaryFragment : CommonFragment(), BirthdayAnniversaryView {
  lateinit var binding: FragmentBirthdayAnniversaryBinding
  @Inject lateinit var presenter: BirthdayAnniversaryPresenter
  private lateinit var section: DataManagerSection
  private lateinit var spinnerProfileAdapter: ArrayAdapter<ProfileSpinner>
  private lateinit var spinnerEventAdapter: ArrayAdapter<String>
  private val subject = PublishSubject.create<BirthdayAnniversaryUiEvent>()
  private var filterType = FILTER_TYPE_TWO
  private var dayType = TODAY_FLAG
  private var dataFoundFlag = false
  override fun getLayout(): Int {
    return R.layout.fragment_birthday_anniversary
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentBirthdayAnniversaryBinding.bind(view)
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
      layoutManager = GridLayoutManager(requireContext(), 1)
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

  override fun getDayType(): String {
    return dayType
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
      filterType = FILTER_TYPE_TWO
      dayType = TODAY_FLAG
      CRMAppUtil.expand(binding.ivEmailAll)
    }
  }

  override fun onButtonBelatedClick(): Observable<Unit> {
    return binding.btnBelated.clicks().map {
      filterType = FILTER_TYPE_ONE
      dayType = BELATED_FLAG
      CRMAppUtil.collapse(binding.ivEmailAll)
    }
  }

  override fun onButtonUpcomingClick(): Observable<Unit> {
    return binding.btnUpcomming.clicks().map {
      filterType = FILTER_TYPE_THREE
      dayType = UPCOMING_FLAG
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
            subject.onNext(
              SendEmailAll(
                "0",
                true
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
    section.toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      section.showError(it)
      dataFoundFlag = false
    }
    uiModel.onSuccess {
      section.clearContent()
      if (it.data.isNotEmpty()) {
        (binding.rvBirthdayAnniversary.getLayoutManager() as GridLayoutManager).spanCount = 3
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
        if (getEventType().equals(KEY_BIRTHDAY, ignoreCase = true)) {
          section.showEmptyView(
            ErrorModel(
              String.format(
                resources.getString(string.txt_no_clients_having_birthday),
                dayType
              ), NO_DATA_ERROR
            )
          )
        } else {
          section.showEmptyView(
            ErrorModel(
              String.format(
                resources.getString(string.txt_no_clients_having_anniversary),
                dayType
              ), NO_DATA_ERROR
            )
          )

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
    const val TODAY_FLAG = "today"
    const val BELATED_FLAG = "belated"
    const val UPCOMING_FLAG = "upcoming"
    const val FILTER_TYPE_ONE = "1"
    const val FILTER_TYPE_TWO = "2"
    const val FILTER_TYPE_THREE = "3"
    const val KEY_BIRTHDAY = "Birthday"
    const val KEY_ANNIVERSARY = "Anniversary"
    const val KEY_A = "A"
    const val KEY_B = "B"
    const val KEY_MSG_TYPE = "E"
    const val KEY_TYPE_L = "-L"
    const val KEY_TYPE_T = "-T"
  }

}
