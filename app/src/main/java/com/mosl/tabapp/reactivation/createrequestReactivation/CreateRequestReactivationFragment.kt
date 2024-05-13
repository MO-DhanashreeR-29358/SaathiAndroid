package com.mosl.tabapp.reactivation.createrequestReactivation

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.error.ErrorModel
import com.mosl.app.core.events.DataAction
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.reactivation.ReactivationActualMarginPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchPostModel
import com.mosl.mobile.domain.reactivation.ReactivationClientSearchResponseModel
import com.mosl.mobile.domain.reactivation.ReactivationSubmitPostModel
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentCreateRequestReactivationBinding
import com.mosl.tabapp.helper.fileuploader.SaathiFileUploader
import com.mosl.tabapp.helper.fileuploader.SaathiUploadUiSubject
import com.mosl.tabapp.reactivation.createrequestReactivationDetails.ReactivationDetailsFragment
import com.mosl.validator.exception.ValidationException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CreateRequestReactivationFragment : CommonFragment(),CreateRequestReactivationView {

    lateinit var binding:FragmentCreateRequestReactivationBinding

    @Inject
    lateinit var presenter: CreateRequestReactivationPresenter
    var strSelectedOption:String="Reactivation Request"
    var strSelectedDate:String=""
    var objReactivationClientSearchResponseModel:ReactivationClientSearchResponseModel?=null
    private val subject =CreateReacivationUiEvent.getSubject()
    private val calendar = Calendar.getInstance()
     var formatedDate:LocalDate?=null
    lateinit var saathiFileUploader: SaathiFileUploader
    protected val FileUploadsubject: PublishSubject<SaathiUploadUiSubject> = PublishSubject.create<SaathiUploadUiSubject>()
    var isSearched:Boolean=false
    var strSelectedFile:String=""

    override fun getLayout(): Int {
       return R.layout.fragment_create_request_reactivation
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateRequestReactivationBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
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
        binding.edtClientCode.addTextChangedListener (object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.isEmpty() == true)
                {
                    objReactivationClientSearchResponseModel=null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun fnShowCalender()
    {

        val minDay = formatedDate?.dayOfMonth
        val minMonth = formatedDate?.monthValue
        val minYear = formatedDate?.year
        val datePickerDialog = DatePickerDialog(
            requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateFormatdsiplay = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                val formattedDateDisp=dateFormatdsiplay.format(selectedDate.time)
                strSelectedDate=formattedDate
                binding.edtDateActual.setText(formattedDateDisp)
                subject.onNext(CreateReacivationUiEvent.OnCallActualMargin)
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
            binding.txtSearchError.isVisible=false
            objReactivationClientSearchResponseModel=it.firstOrNull()
            alert("Client search successfully")
            isSearched=true
        }

        uiModel.onFailure {
            if (it is ValidationException) {
                val error = it.error.message
                binding.txtSearchError.setText(error.orEmpty())
                binding.txtSearchError.isVisible=true
            } else {
                showOnFailurePopup(it)
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

    override fun fnGetMarginPostData(): ReactivationActualMarginPostModel {
        return ReactivationActualMarginPostModel("1",binding.edtClientCode.text.toString(),strSelectedDate)
    }
    override fun fnCallActualMargin(): Observable<CreateReacivationUiEvent.OnCallActualMargin> {
        return subject.ofType()
    }

    override fun fnRenderActualmargin(uiModel: UiModel<List<ReactivationClientSearchResponseModel>>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            objReactivationClientSearchResponseModel?.cashMarginCollected=it.firstOrNull()?.cashMarginCollected
            objReactivationClientSearchResponseModel?.stockMarginCollected=it.firstOrNull()?.stockMarginCollected
            objReactivationClientSearchResponseModel?.tGSEdumoRevenue=it.firstOrNull()?.tGSEdumoRevenue
            objReactivationClientSearchResponseModel?.totalMarginCollected=it.firstOrNull()?.totalMarginCollected
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

    override fun onSelectedFileEventFired(): Observable<SaathiUploadUiSubject.SelectedFileEvent> {
       return FileUploadsubject.ofType()
    }

    override fun onSelectedImageEventFired(): Observable<SaathiUploadUiSubject.SelectedImageEvent> {
        return FileUploadsubject.ofType()
    }

    override fun fnRenderImageSelect(strFileUrl: String) {
        strSelectedFile=strFileUrl
        val objFile =File(strSelectedFile)
        binding.txtFileName.setText(objFile.name+".jpg")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        saathiFileUploader.onActivityResult(requestCode, resultCode, data)
    }

    override fun fnGetReactivationPostModel(): ReactivationSubmitPostModel {
        val objFile =File(strSelectedFile)
        return ReactivationSubmitPostModel(
            tGSEdumoRevenue=objReactivationClientSearchResponseModel?.tGSEdumoRevenue?.substringBefore(".")?:"0",

         remarks=binding.edtRemark.text.toString(),

         marginProofFileName=objFile.name+".jpg",

         clientCode=binding.edtClientCode.text.toString(),

         requestTypeFlag=strSelectedOption,

         actualActivationDate=strSelectedDate,

         documentName=objFile.name+".jpg",

         totalMarginCollected=objReactivationClientSearchResponseModel?.totalMarginCollected?:"0",

         uriString=strSelectedFile,
            isSearched = isSearched
        )
    }
    override fun fnOnUpdateClick(): Observable<Unit> {
        return binding.btnUpdateLead.clicks()
    }

    override fun fnRenderUpdate(uiModel: UiModel<String>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onSuccess {
            alert("Reactivation Request sent successfully")
            objReactivationClientSearchResponseModel=null
            binding.edtClientCode.setText("")
            binding.edtDateActual.setText("")
            binding.edtRemark.setText("")
            binding.txtFileName.setText("Upload")
            strSelectedDate=""
            strSelectedFile=""
            isSearched=false
               }

        uiModel.onFailure {

            if (it is ValidationException) {
                val error = it.error.message
               alert(error?:"")
            } else {
                showOnFailurePopup(it)
            }
        }
    }
    override fun fnOnResetClick(): Observable<Unit> {
        return binding.btnResetLead.clicks()
    }

    override fun fnRenderReset() {
        objReactivationClientSearchResponseModel=null
        binding.edtClientCode.setText("")
        binding.edtDateActual.setText("")
        binding.edtRemark.setText("")
        binding.txtFileName.setText("Upload")
        strSelectedFile=""
        strSelectedDate=""
        isSearched=false
    }
}