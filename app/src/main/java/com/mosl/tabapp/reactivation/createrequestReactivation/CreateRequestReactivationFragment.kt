package com.mosl.tabapp.reactivation.createrequestReactivation

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.core.CommonFragment
import javax.inject.Inject
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.FragmentCreateRequestReactivationBinding
import com.mosl.validator.exception.ValidationException
import io.reactivex.rxjava3.core.Observable
import com.mosl.commons.rx.ofType
import com.mosl.tabapp.helper.fileuploader.SaathiFileUploader
import com.mosl.tabapp.helper.fileuploader.SaathiUploadUiSubject
import com.mosl.tabapp.reactivation.createrequestReactivationDetails.ReactivationDetailsFragment
import io.reactivex.rxjava3.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateRequestReactivationFragment : CommonFragment(),CreateRequestReactivationView {

    lateinit var binding:FragmentCreateRequestReactivationBinding

    @Inject
    lateinit var presenter: CreateRequestReactivationPresenter
    var strSelectedOption:String=""
    var objReactivationClientSearchResponseModel:ReactivationClientSearchResponseModel?=null
    private val subject =CreateReacivationUiEvent.getSubject()
    private val calendar = Calendar.getInstance()
     var formatedDate:LocalDate?=null
    lateinit var saathiFileUploader: SaathiFileUploader
    protected val FileUploadsubject: PublishSubject<SaathiUploadUiSubject> = PublishSubject.create<SaathiUploadUiSubject>()
    override fun getLayout(): Int {
       return R.layout.fragment_create_request_reactivation
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateRequestReactivationBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        Log.d("Token", userManager.user.token)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.stop()
    }

    private fun setupView()
    {
        saathiFileUploader= SaathiFileUploader(this,FileUploadsubject)
        binding.radioSelection.setOnCheckedChangeListener { group, checkedId ->
                    when(checkedId)
                    {
                        R.id.radio_btn_request ->
                            strSelectedOption="Reactivation Request"
                        R.id.radio_btn_rm_request ->
                            strSelectedOption="RM Change Request"
                        R.id.radio_btn_date_request ->
                            strSelectedOption="Activation Date ChangeRequest"

                    }
        }
        binding.edtDateActual.setOnClickListener {
            fnShowCalender()
        }
    }

    private fun fnShowCalender()
    {

        val minDay = formatedDate?.dayOfMonth
        val minMonth = formatedDate?.monthValue
        val minYear = formatedDate?.year
        val datePickerDialog = DatePickerDialog(
            requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                showSuccessToast(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        if (minYear != null) {
            if (minMonth != null) {
                if (minDay != null) {
                    calendar.set(minYear, minMonth-1, minDay)
                    datePickerDialog.datePicker.minDate = calendar.timeInMillis

                }
            }
        }
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        val maxDay = LocalDate.now().dayOfMonth
        val maxMonth = LocalDate.now().monthValue
        val maxYear = LocalDate.now().year
        calendar.set(maxYear, maxMonth-1,maxDay )
        datePickerDialog.datePicker.maxDate=calendar.timeInMillis
        // Show
        // the DatePicker dialog
        datePickerDialog.show()
    }

    override fun fnGetData(): ReactivationClientSearchPostModel {
        return ReactivationClientSearchPostModel(
            employeeCode = userManager.user.employeeCode,
            clientCode = binding.edtClientCode.text.toString(),
            requestTypeFlag = strSelectedOption,
            branchShift = "No"
        )
    }
    override fun fnOnSearchClientClick(): Observable<Unit> {
        return binding.imgSearchClient.clicks()
    }

    override fun fnRenderClientSearch(uiModel: UiModel<List<ReactivationClientSearchResponseModel>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            objReactivationClientSearchResponseModel=it.firstOrNull()

        }

        uiModel.onFailure {
            if (it is ValidationException) {
                val error = it.error.message
                showErrorToast(error.orEmpty())
            } else {
                val message = it.getErrorOnFailure().message
                if (message.isNullOrEmpty().not()) {
                    showErrorToast(message.orEmpty())
                }
            }
        }
    }

    override fun fnRenderMinDate(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            Log.d("Response",it)
            formatedDate= LocalDate.parse(it)
        }

        uiModel.onFailure {
            showOnFailurePopup(it)
        }

    }

    override fun fnOnViewClientDetailClick(): Observable<Unit> {
        return binding.constraintLayout2.clicks()
    }

    override fun fnRenderViewClientDetailsClick() {
        if(objReactivationClientSearchResponseModel!=null)
        {
              val showPopup= ReactivationDetailsFragment()
            showPopup.fnSetData(Gson().toJson(objReactivationClientSearchResponseModel))
           showPopup.show(childFragmentManager,"leadDetailsview")
        }
    }

    override fun fnRenderActualmargin(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            Log.d("Response",it)
        }

        uiModel.onFailure {
            showOnFailurePopup(it)
        }
    }

    override fun fnOnUploadDocClick(): Observable<Unit> {
        return binding.constraintLayout3.clicks()
    }

    override fun fnOpenFileSelet() {
        saathiFileUploader.openFileChooser("")
    }
}