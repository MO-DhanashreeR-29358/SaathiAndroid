package com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.view.touches
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.itemSelections
import com.jakewharton.rxbinding4.widget.textChanges
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.spinner.getItems
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.app.coreui.spinner.setSpinnerDataWithSelection
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.commons.rx.ofType
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.model.lead.*
import com.mosl.tabapp.BuildConfig
import com.mosl.tabapp.R
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.common.*
import com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString
import com.mosl.tabapp.common.CRMAppUtil.isBlank
import com.mosl.tabapp.common.CustomListPickerDialog.ListPickerListneable
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogLeadviewAddProductBinding
import com.mosl.tabapp.databinding.DialogUpdateProfileBinding
import com.mosl.tabapp.databinding.FragmentIndirectLeadDetailBinding
import com.mosl.tabapp.databinding.FragmentLeadDetailBinding
import com.mosl.tabapp.leadViewDetail.*
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailUiEvent
import com.mosl.tabapp.leadcreation.data.LeadCreationRepositoryImpl.Companion.SELECT_ROW_DATA
import com.mosl.tabapp.leadcreation.domain.LeadDetailPageData
import com.mosl.tabapp.leadcreation.domain.SearchReferee
import com.mosl.tabapp.leaddetail.*
import com.mosl.tabapp.leaddetail.ClientDetail
import com.mosl.tabapp.model.*
import com.mosl.tabapp.model.leaddetail.CrossSellingProductsModel
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel
import com.mosl.tabapp.model.leaddetail.LeadDetailModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.saathiIndirect.LeadCreation.domain.IndirectLeadDetailPageData
import com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail.data.IndirectLeadDetail
import com.mosl.tabapp.saathiIndirect.leadViewDetail.main.IndirectLeadViewDetailUiEvent
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.*
import com.mosl.validator.data.ValidationError
import com.mosl.validator.exception.ValidationException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class IndirectLeadDetailFragment : CommonFragment(), IndirectLeadDetailView, IndirectLeadDetailAddProductCallBack {

    @Inject
    internal lateinit var presenter: IndirectLeadDetailPresenter
    private lateinit var binding: FragmentIndirectLeadDetailBinding
    private var isReferee = false

    private var leadId = DEFAULT_SELECTION_ID
    private var ucId = DEFAULT_SELECTION_ID
    private var sourceIdStr = ""
    private var leadProspectSourceIdStr = ""

    private var stage: String? = null
    private var status: String? = null

    private val subject = PublishSubject.create<IndirectLeadDetailUiEvent>()
    private val mainSubject = IndirectLeadViewDetailUiEvent.getSubject()

    private var categoryData = emptyList<RowData>()
    private var backgroundData = emptyList<RowData>()
    private var workExpData = emptyList<RowData>()
    private var associateBrokerData = emptyList<RowData>()

    private var selectedCityId = DEFAULT_SELECTION_ID
    private var selectedCityOffId = DEFAULT_SELECTION_ID
    private var selectedStateId = DEFAULT_SELECTION_ID
    private var selectedStateOffId = DEFAULT_SELECTION_ID

    private val addressDetailList: ArrayList<AddressDetailModel> = ArrayList()
    private val contactDetailList: ArrayList<ContactDetailModel> = ArrayList()

    private val categoryAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }
    private val backgroundAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }
    private val workExpAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val AssociateBrokerAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val countryAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val countryAdapterOff: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val stateAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val stateAdapterOff: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val cityAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val cityAdapterOff: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val defaultSelectionList = ArrayList<RowData>()

    override fun getLayout(): Int = R.layout.fragment_indirect_lead_detail

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIndirectLeadDetailBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }

    private fun setupView() {
        defaultSelectionList.add(0, SELECT_ROW_DATA)
        with(binding) {
            setIcon(llLeadDetail, tvLeadDetail)
            etDob.inputType = InputType.TYPE_NULL
            etAnniversary.inputType = InputType.TYPE_NULL
            etPanNumber.filters = arrayOf(
                InputFilter.LengthFilter(10),
                InputFilter.AllCaps()
            )

        }


    }
    private fun setIcon(parent: View, icon: TextView, Section: String) {
        if (Section.equals(CONST_OUTER, ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_minus, 0)
            } else {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_plus, 0)
            }
        } else if (Section.equals(CONST_INNER, ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.up_button, 0)
            } else {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.down_button, 0)
            }
        }
    }

    override fun renderArguments(data: LeadDetailArgument) {
        leadId = data.leadId
        ucId = data.ucId
    }

    override fun onNestedScrollViewTouched(): Observable<MotionEvent> =
        binding.nestedScrollView.touches { false }

    override fun renderOnNestedScrollViewTouch() {
        binding.nestedScrollView.requestFocusFromTouch()
    }

    override fun onPrimaryAddCheckboxClicked(): Observable<Unit> = binding.cbPrimaryAddress.clicks()

    override fun onOfficePrimaryAddCheckboxClicked(): Observable<Unit> =
        binding.cbOffPrimaryAddress.clicks()

    override fun onSameAsCheckboxClicked(): Observable<Unit> = binding.cbSameAs.clicks()

    override fun getUpdateLeadUSerInput(): UpdateLeadRequest = getUpdateLead()

    override fun onSubmitButtonClicked(): Observable<Unit> = binding.btnSubmit.clicks()

     override fun onUpdateProfileEventFired(): Observable<UpdateProfile> = subject.ofType()

    override fun renderUpdateProfile(uiModel: UiModel<Pair<String, String>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { Timber.d("LeadDetailFragment renderUpdateProfile showOnFailurePopup %s", it.message)
            showOnFailurePopup(it) }
        uiModel.onSuccess {
                MSFDialog.alertDialog(
                    requireContext(),
                    0,
                    getString(R.string.motilal_oswal),
                    it.first,
                    getString(R.string.button_label_ok),
                    true,
                    null
                )
        }
    }

    override fun onAddProductClicked(): Observable<Unit> = binding.rlAddProduct.clicks()

    override fun isLeadEditable(): Boolean = isLeadEditable

    private var specialistAdd: String? = null
    private var primaryRm: String? = null

    private lateinit var addProductDialogBinding: DialogLeadviewAddProductBinding

    private var productSpecialDialog: CustomListPickerDialog? = null
    private var assignToDialog: CustomListPickerDialog? = null

    override fun onProductSpecialEventFired(): Observable<ProductSpecialistEvent> = subject.ofType()

    override fun renderAssignTo(uiModel: UiModel<List<LeadEmployees>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            val model = listOf(
                EmployeeModel.getNoData(getErrorOnFailureNew(it))
            )
            assignToDialog?.setAdapter(
                AttendeeAdapter(
                    requireContext(),
                    model
                )
            )
        }

        uiModel.onSuccess {
            val list = emptyList<EmployeeModel>()
            if (it.isNotEmpty()) {
                it.filter { it1 ->
                    it1.code == userManager.user.employeeCode
                }.map { leadEmployees ->
                    EmployeeModel(
                        leadEmployees.name, leadEmployees.code, leadEmployees.branchCode, false
                    )
                }
            } else {
                EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE)
            }

            assignToDialog?.setAdapter(
                AttendeeAdapter(
                    requireContext(), list
                )
            )
        }

    }

    override fun addProductEventFired(): Observable<AddProductEvent> = subject.ofType()

    override fun renderAddProduct(uiModel: UiModel<List<AddProductData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            Timber.d("LeadDetailFragment renderAddProduct showOnFailurePopup %s", it.message)

            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            Toast.makeText(
                requireContext(),
                getString(R.string.text_message_success),
                Toast.LENGTH_LONG
            ).show()

            if ("0".equals(leadId, ignoreCase = true)) {

                val leadID = it[0].leadId
                val uCID = it[0].ucId

                MSFDialog.alertDialog(
                    requireActivity(), 0,
                    resources.getString(
                        R.string.motilal_oswal
                    ), getString(R.string.text_msg_success_add_product),
                    getString(R.string.button_label_ok), true
                ) { action: Action, _: Array<Any?>? ->
                    if (action == OK) {
                        val bundle = Bundle()
                        bundle.putString(LEAD_ID, leadID)
                        bundle.putString(UCID, uCID)
                        navigator.openPage(
                            NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER.toString(), bundle,
                            true,
                            forceReload = false
                        )
                    }
                }
            } else {
                if (it.isNotEmpty()) {
                    for (datum in it) {

                        leadDetailAddProductList.add(getLeadDetailAddProductModel(datum))
                    }
                    leadDetailAddProductAdapter =
                        IndirectLeadDetailAddProductAdapter(
                            saathiApi,
                            requireContext(),
                            leadDetailAddProductList,
                            binding.lvAddProduct,
                            leadId,
                            getPrimaryRM(),
                            isLeadEditable,
                            defaultSelectionList,
                            categoryAdapter,
                            workExpAdapter,
                            userManager.getUser()?.employeeCode,
                            userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                             emptyString(),
                            this
                        )
                    binding.lvAddProduct.adapter = leadDetailAddProductAdapter
                   //CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail(binding.lvAddProduct)
                }
                MSFDialog.alertDialog(
                    requireActivity(),
                    0,
                    resources.getString(R.string.motilal_oswal),
                    getString(R.string.text_msg_success_add_product),
                    getString(R.string.button_label_ok),
                    true
                ) { _: Action?, _: Array<Any?>? ->
                }
            }
        }
    }

    private fun getPrimaryRM(): String =
        if (isBlank(leadOwner.trim())) advisoryCode else leadOwner.trim()

    override fun renderProductSpecial(uiModel: UiModel<List<LeadEmployees>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            val model = listOf(
                EmployeeModel.getNoData(getErrorOnFailureNew(it))
            )
            productSpecialDialog?.setAdapter(
                AttendeeAdapter(
                    requireContext(),
                    model
                )
            )
        }
        uiModel.onSuccess {
            val list = if (it.isNotEmpty()) {
                it.filter { it1 ->
                    it1.code != userManager.user.employeeCode
                }
                    .map { it1 ->
                        EmployeeModel(
                            it1.name, it1.code, it1.branchCode, false
                        )
                    }
            } else {
                listOf(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE))
            }

            productSpecialDialog?.setAdapter(
                AttendeeAdapter(
                    requireContext(), list
                )
            )
        }

    }

    override fun onAssignToEventFired(): Observable<AssignToEvent> = subject.ofType()

    private var isPotentialPMSProduct = false

    override fun openAddProductDialog() {
        Dialog(requireContext()).apply {
            addProductDialogBinding = DialogLeadviewAddProductBinding.bind(
                View.inflate(
                    requireContext(),
                    R.layout.dialog_leadview_add_product,
                    null
                )
            )
            val dialogBinding = addProductDialogBinding
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogBinding.dacProductSpecialist.hint = resources.getString(R.string.str_prod_spl)
            dialogBinding.actAssignTo.hint = resources.getString(R.string.str_assing)
            dialogBinding.dacProductSpecialist.setListener {
                specialistAdd = ""
            }
            dialogBinding.dacProductSpecialist.setOnClickListener {

                requireActivity().window
                    .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    )

                val searchListProductSpecialist =
                    CustomListPickerDialog(requireContext())

                searchListProductSpecialist.threshold = SEARCH_LENTH

                searchListProductSpecialist.show()
                searchListProductSpecialist.setCustomTitle(
                    resources.getString(R.string.str_prod_spl)
                )
                val productSpecialistViewListner: ListPickerListneable =
                    object : ListPickerListneable {
                        override fun onListItemSelect(data: Parcelable) {
                            val clientModel =
                                data as EmployeeModel
                            if (clientModel.isNoData) {
                                return
                            }
                            dialogBinding.dacProductSpecialist.setText(clientModel.attendeeName)
                            specialistAdd = clientModel.attendeeCode
                            dialogBinding.dacProductSpecialist.setClearIconVisible(true)
                            searchListProductSpecialist.dismiss()
                        }

                        override fun onSearchTextChanged(constraint: CharSequence?) {
                            if (constraint != null) {

                                primaryRm = if (isBlank(leadOwner.trim())) {
                                    advisoryCode
                                } else {
                                    leadOwner
                                }
                                val rm = primaryRm.nullSafe()
                                productSpecialDialog = searchListProductSpecialist
                                subject.onNext(
                                    ProductSpecialistEvent(
                                        selectedProduct = constraint.toString(),
                                        primaryRM = rm
                                    )
                                )
                            }
                        }
                    }
                searchListProductSpecialist.setmListner(productSpecialistViewListner)

            }
            dialogBinding.actAssignTo.setListener {
                primaryRm = ""
            }
            dialogBinding.actAssignTo.setOnClickListener {
                requireActivity().window
                    .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    )
                val searchList = CustomListPickerDialog(requireContext())
                searchList.show()
                searchList.threshold = SEARCH_LENTH
                searchList.setCustomTitle(resources.getString(R.string.str_assing))
                val assingViewListner: ListPickerListneable = object : ListPickerListneable {
                    override fun onListItemSelect(data: Parcelable) {
                        val clientModel =
                            data as EmployeeModel
                        if (clientModel.isNoData) {
                            return
                        }
                        dialogBinding.actAssignTo.setText(clientModel.attendeeName)
                        dialogBinding.actAssignTo.isEnabled = true
                        primaryRm = clientModel.attendeeCode
                        dialogBinding.actAssignTo.setClearIconVisible(true)
                        searchList.dismiss()
                    }

                    override fun onSearchTextChanged(constraint: CharSequence?) {

                        if (constraint != null) {
                            assignToDialog = searchList
                            subject.onNext(AssignToEvent(constraint.toString()))
                        }
                    }
                }
                searchList.setmListner(assingViewListner)
            }
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
           /* dialogBinding.spInterestedIn.adapter = interestedAdapter
            dialogBinding.spStage.adapter = stageAdapter
            dialogBinding.spStatus.adapter = statusAdapter
            interestedAdapter.setSpinnerData(dialogBinding.spInterestedIn, interestedData)*/

            dialogBinding.spInterestedIn.itemSelections()
                .filter(InvalidSelectionPredicate)
                .map { dialogBinding.spInterestedIn.selectedItem as RowData }
                .filter { it.key != IndirectLeadDetailView.INVALID_SELECTION_KEY }
                .subscribe {
                    subject.onNext(SelectInterestedProductEvent(it))
                }
                .addTo(disposable)

            dialogBinding.spStage.itemSelections()
                .filter(InvalidSelectionPredicate)
                .map { dialogBinding.spStage.selectedItem as RowData }
                .filter { it.key != IndirectLeadDetailView.INVALID_SELECTION_KEY }
                .subscribe {
                    subject.onNext(SelectStageEvent(it))
                }
                .addTo(disposable)




            dialogBinding.btnSave.setOnClickListener {

                if (dialogBinding.spInterestedIn.selectedItemPosition < 1) {
                    dialogBinding.tvspIntrestError.visible()
                    dialogBinding.tvspIntrestError.text =
                        getString(R.string.error_message_select_interest)
                    return@setOnClickListener
                }
                if (dialogBinding.spStage.selectedItemPosition < 1) {
                    dialogBinding.tvspStageError.visible()
                    dialogBinding.tvspStageError.text =
                        getString(R.string.error_message_select_stage)
                    return@setOnClickListener
                }
                if (dialogBinding.spStatus.selectedItemPosition < 1) {
                    dialogBinding.tvspStatusError.visible()
                    dialogBinding.tvspStatusError.text =
                        getString(R.string.error_message_select_status)
                    return@setOnClickListener
                }


                val interestedInKey =
                    (dialogBinding.spInterestedIn.selectedItem as RowData).key
                val interestedInDisplayText =
                    (dialogBinding.spInterestedIn.selectedItem as RowData).value
                if (leadDetailAddProductList.size == 0) {
                    leadDetailAddProductAdapter =
                        IndirectLeadDetailAddProductAdapter(
                            saathiApi,
                            requireContext(),
                            leadDetailAddProductList,
                            binding.lvAddProduct,
                            leadId,
                            getPrimaryRM(),
                            isLeadEditable,
                            defaultSelectionList,
                            categoryAdapter,
                            workExpAdapter,
                            userManager.getUser()?.employeeCode,
                            userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                            emptyString(),
                            this@IndirectLeadDetailFragment
                        )
                }
                if (leadDetailAddProductAdapter?.checkProduct(interestedInKey) == true) {
                    MSFDialog.alertDialog(
                        requireContext(),
                        0,
                        resources.getString(R.string.motilal_oswal),
                        getString(R.string.error_message_already_added, interestedInDisplayText),
                        getString(R.string.button_label_ok),
                        true,
                        null
                    )
                } else {
                    if (isPotentialPMSProduct) {
                        MSFDialog.alertDialog(
                            requireActivity(),
                            0,
                            resources.getString(R.string.motilal_oswal),
                            POTENTIAL_INVEST_IN_PRODUCT,
                            YES,
                            NO,
                            true
                        ) { action: Action, _: Array<Any?>? ->
                            val productMapping = if (action == OK) YES else NO
                            callAddProduct(productMapping)
                        }
                    } else
                        callAddProduct()
                }
                dismiss()
            }

            dialogBinding.btnClose.setOnClickListener {
                hideKeyboard(binding.root)
                dismiss()
            }
        }
            .show()
       /* stageData.clear()
        stageData.add(0, SELECT_ROW_DATA)
        stageAdapter.setSpinnerData(addProductDialogBinding.spStage, stageData)

        statusData.clear()
        statusData.add(0, SELECT_ROW_DATA)
        statusAdapter.setSpinnerData(addProductDialogBinding.spStatus, statusData)*/
    }

    private fun callAddProduct(productMapping: String = "") {
        addProduct(
            (addProductDialogBinding.spInterestedIn.selectedItem as RowData).key,
            (addProductDialogBinding.spStage.selectedItem as RowData).key,
            (addProductDialogBinding.spStatus.selectedItem as RowData).key,
            productMapping
        )
    }

    private fun addProduct(
        interestedIn: String,
        stage: String,
        status: String,
        productMapping: String
    ) {
        //data
        val data = InsertLeadProductRequest(
            leadId,
            ucId,
            interestedIn,
            interestedIn,
            primaryRm ?: "",
            specialistAdd ?: "",
            userManager.getUser()?.employeeCode.toString(),
            CRMAppUtil.getDBDate(),
            stage,
            status,
            productMapping
        )
        //event
        subject.onNext(AddProductEvent(data))
    }

    override fun onEditLeadDetailClicked(): Observable<Unit> = binding.llEditLeadDetail.clicks()

    private var editableFlag = false

    override fun renderEditLeadDetailClick() {
        if (leadId != "0" && isLeadEditable) {
            editableFlag = if (editableFlag) {
                setEnableDisable(true)
                false
            } else {
                setEnableDisable(false)
                true
            }
        }
    }

    override fun renderPrimaryAddCheckBoxClicked() {
        binding.run {
            if (cbPrimaryAddress.isChecked) {
                handleWhenPrimaryAddChecked(true)
            } else {
                handleWhenPrimaryAddUnckecked(true)
            }
        }
    }

    private fun FragmentIndirectLeadDetailBinding.handleWhenPrimaryAddChecked(isEnabledRequired: Boolean = false) {
        cbOffPrimaryAddress.gone()
        cbSameAs.visible()
        cbOffPrimaryAddress.isChecked = false
        if (isEnabledRequired) {
            enableField(llResAdd)
            enableField(llOffAdd)
        }
    }

    private fun FragmentIndirectLeadDetailBinding.handleWhenPrimaryAddUnckecked(isDisabledRequired: Boolean = false) {
        cbOffPrimaryAddress.visible()
        cbSameAs.gone()
        cbOffPrimaryAddress.isChecked = false
        cbSameAs.isChecked = false
        if (isDisabledRequired) {
            disableField(llResAdd)
            disableField(llOffAdd)
        }
    }

    override fun renderOffPrimaryAddCheckBoxClicked() {
        binding.run {
            if (cbOffPrimaryAddress.isChecked) {
                enableField(llResAdd)
                enableField(llOffAdd)
            } else {
                disableField(llResAdd)
                disableField(llOffAdd)
            }
        }
    }

    override fun renderSameAsAboveClicked() {
        binding.run {
            when {
                cbSameAs.isChecked -> handlingWhenSameAsCheckboxClickedAndChecked()
                else -> handlingWhenSameAsCheckboxClickedAndUnChecked()
            }
        }
    }

    private fun handlingWhenSameAsCheckboxClickedAndUnChecked() {
        binding.run {
            enableField(llOffAdd)
            etOffAdd1.setText("")
            etOffAdd2.setText("")
            etOffAdd3.setText("")
            etOffPincode.setText("")
            spOffCountry.setSelection(0)
            spOffState.setSelection(0)
            spOffCity.setSelection(0)
        }
    }


    private fun handlingWhenSameAsCheckboxClickedAndChecked() {
        binding.run {
            disableField(llOffAdd)
            etOffAdd1.text = etAdd1.text
            etOffAdd2.text = etAdd2.text
            etOffAdd3.text = etAdd3.text
            etOffPincode.text = etPincode.text
            spOffCountry.setSelection(spCountry.selectedItemPosition)
            spOffState.setSpinnerData(stateAdapterOff, stateAdapter.getItems())
            spOffState.setSelection(spState.selectedItemPosition)
            spOffCity.setSpinnerData(cityAdapterOff, cityAdapter.getItems())
            spOffCity.setSelection(spCity.selectedItemPosition)
        }
    }

    override fun onLeadDetailClicked(): Observable<Unit> = binding.tvLeadDetail.clicks()

    override fun onDocumentClicked(): Observable<Unit> = binding.tvDocuments.clicks()

    override fun onDOBClicked(): Observable<Unit> = binding.etDob.clicks()

    override fun renderDOBClick() {
        CustomDatePickerDialog(
            requireContext(),
            false,
            binding.etDob.text.toString(),
            true
        )
        { date ->
            binding.tvAnniversaryError.gone()
            binding.etDob.setText(date)
            binding.etDob.setClearIconVisible(true)
        }.show()
    }

    override fun renderAnniversaryClick() {
        CustomDatePickerDialog(
            requireContext(),
            false,
            binding.etAnniversary.text.toString(),
            true
        )
        { date ->
            binding.tvAnniversaryError.gone()
            binding.etAnniversary.setText(date)
            binding.etAnniversary.setClearIconVisible(true)
        }.show()
    }

    override fun onAnniversaryClicked(): Observable<Unit> = binding.etAnniversary.clicks()

    override fun onRefereeCodeFocusChanged(): Observable<Boolean> =
        binding.etRefereeCode.focusChanges()

    override fun onLeadDetailSectionClicked(): Observable<Unit> = binding.tvLeadDetail.clicks()



    override fun toggleLeadDetailSection() {
        toggleSectionExpansion(binding.llLeadDetail, binding.tvLeadDetail)
    }


    override fun renderOnRefereeCodeFocusChange() {
        if (binding.etRefereeCode.string().length < 4) {
            binding.etRefereeName.setText(emptyString())
            binding.etRefereeEmail.setText(emptyString())
            binding.etRefereeNumber.setText(emptyString())
        }
    }

    override fun onRefereeNameFocusChanged(): Observable<Boolean> =
        binding.etRefereeName.focusChanges()

    override fun renderOnRefereeNameFocusChange() {
        if (binding.etRefereeName.string().length < 3) {
            binding.etRefereeCode.setText(emptyString())
            binding.etRefereeEmail.setText(emptyString())
            binding.etRefereeNumber.setText(emptyString())
        }
    }

    private var advisoryCode = ""


    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }


    private fun toggleSectionExpansion(
        parent: View,
        icon: TextView,
        expansion: String
    ) {
        if (expansion.equals(CONST_OUTER_SECTION_EXPANSION, ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                parent.gone()
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_plus, 0)
            } else {
                parent.visible()
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_minus, 0)
            }
        } else if (expansion.equals(CONST_INNER_SECTION_EXPANSION, ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                parent.gone()
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.down_button, 0)
            } else {
                parent.visible()
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.up_button, 0)
            }
        }
    }

    override fun isCbSameAsChecked(): Boolean = binding.cbSameAs.isChecked

    override fun renderOnUpdateButtonClick(uiModel: UiModel<String>) {
        toggleProgressBar(uiModel.inProgress)
        removeErrorMessage()
        uiModel.onFailure {
            if (it is ValidationException) {
                if (binding.llLeadDetail.visibility == View.GONE)
                    openSectionExpansion(binding.llLeadDetail, binding.tvLeadDetail)
                showErrorWithFocus(it.error)
            }
        }
        uiModel.onSuccess {
            removeErrorMessage()

                MSFDialog.alertDialog(
                    requireContext(),
                    0,
                    resources.getString(R.string.motilal_oswal),
                    CONFIRM_LEAD_UPDATE,
                    getString(R.string.text_label_yes),
                    getString(R.string.text_label_no),
                    true
                ) { action: Action, _: Array<Any?>? ->
                    if (action == OK) {
                        subject.onNext(UpdateLeadDetail)
                    }
                }



        }
    }

    override fun onUpdateLeadEventFired(): Observable<UpdateLeadDetail> = subject.ofType()

    private fun showErrorWithFocus(error: ValidationError) {
        error.error?.let {
            when (error.identifier as Int) {
                1 ->
                    binding.tlLeadName.setErrorMessages(getString(it, ""))
                2 ->
                    binding.tlLeadName.setErrorMessages(getString(it))
                3 ->
                    binding.tlEmail.setErrorMessages(getString(it))
                4, 5 ->
                    binding.tlAlternateEmail.setErrorMessages(getString(it))
                6 ->
                    binding.tlAlternateMobile.setErrorMessages(getString(it))
                7 ->
                    binding.tlAlternateMobile.setErrorMessages(getString(it))
                9 ->
                    binding.tlLand.setErrorMessages(getString(it))
                10 ->
                    binding.tlPan.setErrorMessages(getString(it))
                else -> {
                }
            }
        }
    }

    private fun removeErrorMessage() {
        binding.apply {
            tlLeadName.disableErrorMessages()
            tlEmail.disableErrorMessages()
            tlAlternateEmail.disableErrorMessages()
            tlAlternateMobile.disableErrorMessages()
            tlMobile.disableErrorMessages()
            tlLand.disableErrorMessages()
            tlPan.disableErrorMessages()
            tlRefereeCode.disableErrorMessages()
            tlRefereeName.disableErrorMessages()
            tlPin.disableErrorMessages()
            tlOffPin.disableErrorMessages()
        }
    }


    private fun getUpdateLead(): UpdateLeadRequest {
        addressDetailList.clear()
        addressDetailList.add(
            AddressDetailModel(
                addressTypeID = RESIDENCEADDRESS,
                address1 = getJsonSafeString(binding.etAdd1.text.toString().trim()),
                address2 = getJsonSafeString(binding.etAdd2.text.toString().trim()),
                address3 = getJsonSafeString(binding.etAdd3.text.toString().trim()),
                countryID = (binding.spCountry.selectedItem as RowData).key,
                stateID = (binding.spState.selectedItem as RowData).key,
                cityID = (binding.spCity.selectedItem as RowData).key,
                pincode = binding.etPincode.text.toString().trim(),
                isPrimary = binding.cbPrimaryAddress.isChecked,
                businessUnitID = userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString()
            )
        )
        addressDetailList.add(
            AddressDetailModel(
                OFFICEADDRESS,
                getJsonSafeString(binding.etOffAdd1.text.toString().trim()),
                getJsonSafeString(binding.etOffAdd2.text.toString().trim()),
                getJsonSafeString(binding.etOffAdd3.text.toString().trim()),
                (binding.spOffCountry.selectedItem as RowData).key,
                (binding.spOffState.selectedItem as RowData).key,
                (binding.spOffCity.selectedItem as RowData).key,
                binding.etOffPincode.text.toString().trim(),
                binding.cbOffPrimaryAddress.isChecked,
                userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString()
            )
        )

        val leadDetailModel = LeadDetailModel()
        leadDetailModel.isSameAsAbove = binding.cbSameAs.isChecked
        leadDetailModel.seteMail(binding.etEmail.text.toString().trim())
        leadDetailModel.alternateEmail = binding.etAlternateEmail.text.toString().trim()
        leadDetailModel.alternateMobileNo = binding.etAlternateMobileNumber.text.toString().trim()
        leadDetailModel.mobileNo = binding.etMobileNumber.text.toString().trim()
        leadDetailModel.ekycMobileNo = emptyString()
        leadDetailModel.landLineNo = binding.etLandlineNumber.text.toString().trim()
        leadDetailModel.aadharNum =emptyString()
        leadDetailModel.panNum = binding.etPanNumber.text.toString().trim()
        leadDetailModel.isPotentialPMSProduct = emptyString()
        leadDetailModel.prospectRefereName =emptyString()
        leadDetailModel.prospectRefereMobile =
            emptyString()
        leadDetailModel.prospectRefereEmail = emptyString()

        try {
            if (!isBlank(binding.etDob.text.toString())) {
                val strDob: String = binding.etDob.text.toString().trim()
                leadDetailModel.dob = CRMAppUtil.getDateFromString(
                    strDob, CAL_DATE_FORMAT,
                    DATE_FORMAT_TO_SERVER_MEETING_SEND
                ) + "T00:00:00"
            } else {
                leadDetailModel.dob = ""
            }
            if (!isBlank(binding.etAnniversary.text.toString())) {
                val strANN: String = binding.etAnniversary.text.toString().trim()
                leadDetailModel.anniversary = CRMAppUtil.getDateFromString(
                    strANN, CAL_DATE_FORMAT,
                    DATE_FORMAT_TO_SERVER_MEETING_SEND
                ) + "T00:00:00"
            } else {
                leadDetailModel.anniversary = ""
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        leadDetailModel.leadID = binding.etLeadID.text.toString().trim()
        leadDetailModel.businessUnitID =
            userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString()
        leadDetailModel.contactPerson =emptyString()
        leadDetailModel.sourceID = sourceIdStr
        leadDetailModel.leadSourceID = leadProspectSourceIdStr
        leadDetailModel.leadSourceDetailsID = "0"
        leadDetailModel.leadSourceDetails = binding.etProspectSourceDetail.text.toString()
        leadDetailModel.name = binding.etLeadName.text.toString().trim()
        if (binding.etRefereeCode.text.toString().trim().length >= 3 /*&& isRefreeCode*/) {
            leadDetailModel.refereeCode = binding.etRefereeCode.text.toString().trim()
        }
        if (binding.etRefereeName.text.toString().trim().length >= 3 /*&& isRefreeCode*/) {
            leadDetailModel.refereeName = binding.etRefereeName.text.toString().trim()
        }
        leadDetailModel.refereeEmail = binding.etRefereeEmail.text.toString().trim()
        leadDetailModel.refereeMobile = binding.etRefereeNumber.text.toString().trim()
        leadDetailModel.comments = getJsonSafeString(binding.etComments.text.toString().trim())
        leadDetailModel.profileID = emptyString()
        leadDetailModel.brokingProfileID = emptyString()
        leadDetailModel.ipProfileID = emptyString()
        leadDetailModel.finalProfileID = emptyString()
        leadDetailModel.professionID = emptyString()
        leadDetailModel.createdBy = emptyString()
        leadDetailModel.createdThrough = binding.etCreatedThrough.text.toString().trim()
        leadDetailModel.creationDate = binding.etCreationDate.text.toString().trim()
            .replace("p.m.", "PM")
            .replace("a.m", "AM")
        leadDetailModel.contactDetailModels = contactDetailList
        leadDetailModel.addressDetailModels = addressDetailList
        leadDetailModel.secondaryRM = null
        leadDetailModel.leadDetailAddProductModels = leadDetailAddProductList
        leadDetailModel.leadFieldValueModels = getLeadFieldValues(mLeadFieldSectionModelList)
        leadDetailModel.sectorId = emptyString()
        leadDetailModel.sector = emptyString()
        leadDetailModel.desigId = emptyString()
        leadDetailModel.desig = emptyString()
        leadDetailModel.networthId = emptyString()
        leadDetailModel.networth = emptyString()
        leadDetailModel.referralEmpCode =emptyString()
        leadDetailModel.referralEmpName = emptyString()
        leadDetailModel.accountTypeId = emptyString()
        leadDetailModel.chipsEventId = emptyString()
        leadDetailModel.isBandhanBankEmp = emptyString()
        leadDetailModel.bandhanBankEmp =emptyString()
        leadDetailModel.auBankName = emptyString()
        leadDetailModel.auBankBranchCode =emptyString()
        return updateLead(leadDetailModel)
    }

    private fun updateLead(model: LeadDetailModel): UpdateLeadRequest {
        val contactAddresses: MutableList<ContactAddressRequest> = ArrayList()
        val detailModels: List<AddressDetailModel> = model.addressDetailModels
        for (detailModel in detailModels) {
            contactAddresses.add(
                ContactAddressRequest(
                    detailModel.addressTypeID.toString(),
                    detailModel.address1,
                    detailModel.address2,
                    detailModel.address3,
                    detailModel.stateID,
                    detailModel.countryID,
                    detailModel.cityID,
                    detailModel.pincode,
                    detailModel.isPrimary,
                    detailModel.businessUnitID
                )
            )
        }
        val contactRequest = ContactRequest(
            model.name,
            model.geteMail(),
            model.alternateEmail,
            model.mobileNo,
            model.ekycMobileNo,
            model.landLineNo,
            model.alternateMobileNo,
            model.aadharNum,
            model.panNum,
            model.dob,
            model.anniversary,
            model.isSameAsAbove,
            contactAddresses
        )
        return UpdateLeadRequest(
            model.leadID, model.businessUnitID, model.isPotentialPMSProduct,
            model.leadCategoryID, model.contactPerson, model.sourceID,
            model.leadSourceID, model.leadSourceDetailsID,
            model.leadSourceDetails, model.refereeCode.nullSafe(), model.refereeName.nullSafe(),
            model.refereeEmail, model.refereeMobile, model.comments,
            model.profileID, model.brokingProfileID, model.ipProfileID, model.finalProfileID,
            model.professionID, model.createdBy, model.prospectRefereName,
            model.prospectRefereMobile, model.prospectRefereEmail,
            model.createdThrough, model.creationDate,
            contactRequest,
            model.leadFieldValueModels,
            model.sectorId,
            model.sector,
            model.desigId,
            model.desig,
            model.networthId,
            model.networth,
            model.referralEmpCode,
            model.referralEmpName,
            model.accountTypeId,
            model.chipsEventId,
            model.isBandhanBankEmp,
            model.bandhanBankEmp,
            model.auBankName,
            model.auBankBranchCode
        )
    }

    override fun renderUpdateDetail(uiModel: UiModel<UpdateLeadResponse>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure { Timber.d("LeadDetailFragment renderUpdateDetail showOnFailurePopup %s", it.message)
            showOnFailurePopup(it) }
        uiModel.onSuccess{
            Log.d("AAA MeetingID", it.createMeeting.toString())
            mainSubject.onNext(IndirectLeadViewDetailUiEvent.RetryClick)
            leadUpdatedDialog()
        }
    }

    private fun leadUpdatedDialog() {
        MSFDialog.alertDialog(
            requireActivity(), 0,
            resources.getString(
                R.string.motilal_oswal
            ), resources.getString(R.string.lead_updated), getString(R.string.button_label_ok), true
        ) { action: Action, _: Array<Any?>? ->
            if (action == OK) {
                binding.llEditLeadDetail.performClick()
            }
        }
    }

    private fun getLeadFieldValues(
        fieldSectionModels: List<LeadFieldSectionModel>?
    ): List<LeadFieldValueRequest?> {
        val requests: MutableList<LeadFieldValueRequest?> = ArrayList()
        if (fieldSectionModels == null) {
            return requests
        }
        for (mSection in fieldSectionModels) {
            val selectionList = mSection.selectionList ?: continue
            for (modal in selectionList) {
                requests.add(
                    LeadFieldValueRequest(
                        modal.lF_SysName,
                        modal.uiValue,
                        modal.leadFieldId,
                        LeadFieldValueModel.getLeadid()
                    )
                )
            }
        }
        return requests
    }

    override fun onAdd1AfterTextChanged(): Observable<String> = binding.etAdd1.textChanges()
        .map { it.toString() }

    override fun renderOnAdd1AfterTextChange(text: String) {
        binding.etOffAdd1.setText(text)
    }

    override fun onAdd2AfterTextChanged(): Observable<String> = binding.etAdd2.textChanges()
        .map { it.toString() }

    override fun renderOnAdd2AfterTextChange(text: String) {
        binding.etOffAdd2.setText(text)
    }

    override fun onAdd3AfterTextChanged(): Observable<String> = binding.etAdd3.textChanges()
        .map { it.toString() }

    override fun renderOnAdd3AfterTextChange(text: String) {
        binding.etOffAdd3.setText(text)
    }

    override fun onRefereeCodeChanged(): Observable<String> =
        binding.etRefereeCode.afterTextChangeEvents()
            .map { it.editable?.toString() ?: emptyString() }

    override fun getRefereeCode(): String = binding.etRefereeCode.text?.toString().nullSafe()

    override fun resetRefereeOnCodeChange() {
        binding.apply {
            etRefereeName.setText(emptyString())
            etRefereeEmail.setText(emptyString())
            etRefereeNumber.setText(emptyString())
        }
    }

    override fun onRefereeNameChanged(): Observable<String> =
        binding.etRefereeName.afterTextChangeEvents()
            .map { it.editable?.toString() ?: "" }

    override fun resetRefereeOnNameChange() {
        binding.apply {
            etRefereeCode.setText(emptyString())
            etRefereeEmail.setText(emptyString())
            etRefereeNumber.setText(emptyString())
        }
    }

    override fun renderOnRefereeCodeChange(uiModel: UiModel<SearchReferee>) {
        binding.pbLoadingIndicatorRefreeCode toggle uiModel.inProgress
        renderOnRefereeChange(uiModel)
    }

    override fun renderOnRefereeNameChange(uiModel: UiModel<SearchReferee>) {
        binding.pbLoadingIndicatorRefreeName toggle uiModel.inProgress
        renderOnRefereeChange(uiModel)
    }

    private fun renderOnRefereeChange(uiModel: UiModel<SearchReferee>) {
        uiModel.onFailure {
            isReferee = false
            binding.etRefereeEmail.setText(emptyString())
            binding.etRefereeNumber.setText(emptyString())
        }
        uiModel.onSuccess {
            when (it.type) {
                "ByCode" -> binding.etRefereeName.setText(it.name.trim())
                else -> binding.etRefereeCode.setText(it.code.trim())
            }
            binding.etRefereeEmail.setText(it.email)
            binding.etRefereeNumber.setText(it.mobile)
            isReferee = true
        }
    }

    private infix fun ProgressBar.toggle(inProgress: Boolean) {
        if (inProgress) visible() else gone()
    }

    override fun isRefereeCodeFocused(): Boolean = binding.etRefereeCode.isFocused

    override fun isRefereeNameFocused(): Boolean = binding.etRefereeName.isFocused

    override fun render(uiModel: UiModel<IndirectLeadDetailPageData>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            Timber.d("LeadDetailFragment render showOnFailurePopup %s", it.message)
            showOnFailurePopup(it)
        }
        uiModel.onSuccess { data ->
            workExpData = data.workExpRowDatas
            workExpAdapter.addAll(data.workExpRowDatas)
            binding.spWorkExp.setSpinnerData(workExpAdapter, workExpData)

            categoryData = data.categoryRowDatas
            categoryAdapter.addAll(data.categoryRowDatas)
            binding.spCategory.setSpinnerData(categoryAdapter, categoryData)

            backgroundData = data.backgroundRowDatas
            backgroundAdapter.addAll(data.backgroundRowDatas)
            binding.spBackground.setSpinnerData(backgroundAdapter, workExpData)

            associateBrokerData = data.associateBroker
            AssociateBrokerAdapter.addAll(data.associateBroker)
            binding.spAssociatedBroker.setSpinnerData(AssociateBrokerAdapter, associateBrokerData)

            binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
            binding.spOffCountry.setSpinnerData(countryAdapterOff, data.countryRowDatas)
            binding.llEditLeadDetail.performClick()
        }
    }



    override fun renderLeadDetail(uiModel: UiModel<IndirectLeadDetail>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { Timber.d("LeadDetailFragment renderLeadDetailAndProfession showOnFailurePopup %s", it.message)
            showOnFailurePopup(it) }
        uiModel.onSuccess { data ->
            data.renderLeadDetail()
        }
    }

    private var leadDetailAddProductList = ArrayList<LeadDetailAddProductModel>()

    private fun setEnableDisable(status: Boolean) {
        var isEnabledEmailId = status
        if(userManager.user.isValidateEmailIdFromLead() && status == true){
            if(binding.etEmail.text.toString().isEmpty()){
                isEnabledEmailId = true
            }else{
                isEnabledEmailId = false
            }
        }

        binding.apply {
            etLeadName.isEnabled = status
            etBusiness.isEnabled = status
            spAssociatedBroker.isEnabled = status
            spCategory.isEnabled = status
            spBackground.isEnabled = status
            etBrokerage.isEnabled = status
            etPanNumber.isEnabled = status
            etLandlineNumber.isEnabled = status
            etProspectSourceDetail.isEnabled = status
            etRefereeCode.isEnabled = status
            etRefereeName.isEnabled = status
            etLandlineNumber.isEnabled = status
            etDob.isEnabled = status
            etAnniversary.isEnabled = status
            etAlternateMobileNumber.isEnabled = status
            etAlternateEmail.isEnabled = status
            etEmail.isEnabled = isEnabledEmailId
            etComments.isEnabled = status
            btnSubmit.isEnabled = status
            cbSameAs.isEnabled = status
            cbPrimaryAddress.isEnabled = status
            cbOffPrimaryAddress.isEnabled = status

            if (status) {
                if (cbPrimaryAddress.isChecked) {
                    enableField(llResAdd)
                    enableField(llOffAdd)
                }
                if (cbOffPrimaryAddress.isChecked) {
                    enableField(llOffAdd)
                    enableField(llResAdd)
                }
                if (cbSameAs.isChecked)
                    disableField(llOffAdd)
            } else {
                disableField(llOffAdd)
                disableField(llResAdd)
            }

            if (mLeadFieldSectionModelList.isNotEmpty()) {
                for (mSection in mLeadFieldSectionModelList) {
                    if (mSection.selectionList != null) {
                        for (modal in mSection.selectionList) {
                            val cObj = modal.controlObject
                            if (cObj != null) {
                                if (cObj is EditText) {
                                    cObj.isEnabled = status
                                } else if (cObj is Spinner) {
                                    cObj.isEnabled = status
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val mLeadFieldSectionModelList = emptyList<LeadFieldSectionModel>()

    private var leadOwner = ""

    private fun IndirectLeadDetail.renderLeadDetail() {
        isLeadEditable = when {
            canEditLead == "0" || isProductSpecialist.equals("Y", true) -> false
            else -> true
        }
        this@IndirectLeadDetailFragment.leadOwner = leadOwner

        binding.etRefereeCode.setText(refereeCode)
        binding.etRefereeName.setText(refereeName)
        binding.etRefereeEmail.setText(referEmail)
        binding.etRefereeNumber.setText(referMobile)
        binding.etCreatedThrough.setText(createdThrough)
        binding.etLastModifiedBy.setText(lastModifiedBy)
        try {
            binding.etLastModifiedDate.setText(
                CRMAppUtil.getDateFromString(
                    lastModifiedDate,
                    mFormat, DATE_FORMAT_TO_DISPLAY
                )
            )
        } catch (e: Exception) {
            binding.etLastModifiedDate.setText(lastModifiedDate.replace("T", " "))
        }

        binding.etProspectSource.setText(prospectSource)
        binding.etProspectSourceDetail.setText(leadSourceDetails)
        binding.etComments.setText(comments)

        sourceIdStr = sourceId
        leadProspectSourceIdStr = leadSourceId.toString()
        binding.etLeadID.setText(leadId)
        binding.etLeadName.setText(leadName)
        try {//Jeet
            binding.etCreationDate.setText(
                CRMAppUtil.getDateFromString(
                    createdDate,
                    mFormat, DATE_FORMAT_TO_DISPLAY
                )
            )
        } catch (e: Exception) {
            binding.etCreationDate.setText(createdDate.replace("T", " "))
        }//Jeet
        binding.etSource.setText(sourceName)

        binding.etExpectedMargin.setText(expectedMargin)

        val dataCategory = RowData.getSpinnerRowDataByValue(leadCategoryText, categoryAdapter)
        dataCategory?.let {
            binding.spCategory.setSelection(it.position)
        }

        val dataBackground = RowData.getSpinnerRowDataById(bgId, backgroundAdapter)
        dataBackground?.let {
            binding.spBackground.setSelection(it.position)
        }

        val dataWorkExp = RowData.getSpinnerRowDataById(workExpId, workExpAdapter)
        dataWorkExp?.let {
            binding.spWorkExp.setSelection(it.position)
        }

        val dataAssoBorker = RowData.getSpinnerRowDataByValue(associatedBroker, AssociateBrokerAdapter)
        dataAssoBorker?.let {
            binding.spAssociatedBroker.setSelection(it.position)
        }

        if (leadProductMapping != null && leadProductMapping.isNotEmpty()) {
            leadProductMapping.renderMapping()
        }

        contact?.renderContact()

    }


    private fun com.mosl.saathi.api.model.lead.Contact.renderContact() {
        binding.run {
            cbSameAs.isChecked = isSameAsAbove
            etPanNumber.setText(panNumber)
            etMobileNumber.setText(mobile)
            etEmail.setText(emailId)
            etAlternateEmail.setText(alternateEmailId)
            etLandlineNumber.setText(landlineNo)
            etAlternateMobileNumber.setText(alternateMobileNo)


            try {
                val newDoa = stringOrEmpty(dOA).split("T").toTypedArray()
                val newDob = stringOrEmpty(dOB).split("T").toTypedArray()
                if (newDob[0].isNotEmpty()) {
                    val dob = CRMAppUtil.getDate(newDob[0], DATE_FORMAT_TO_SERVER_MEETING_SEND)
                    etDob.setText(CRMAppUtil.getDateStr(dob, CAL_DATE_FORMAT))
                }
                if (newDoa[0].isNotEmpty()) {
                    val doa = CRMAppUtil.getDate(newDoa[0], DATE_FORMAT_TO_SERVER_MEETING_SEND)
                    etAnniversary.setText(CRMAppUtil.getDateStr(doa, CAL_DATE_FORMAT))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            contactAddress.renderContactAddresses()
        }
    }

    private fun List<ContactAddressItem>.renderContactAddresses() {
        forEach {
            it.run {
                when (addressTypeID) {
                    RESIDENCEADDRESS -> {
                        binding.cbPrimaryAddress.isChecked = isPrimary
                        binding.etAdd1.setText(address1)
                        binding.etAdd2.setText(address2)
                        binding.etAdd3.setText(address3)
                        binding.etPincode.setText(pincode)
                        selectedStateId = stateID.toString()
                        selectedCityId = cityID.toString()
                        val dataCountry = RowData.getSpinnerRowDataById(
                            countryID.toString(),
                            countryAdapter
                        )
                        dataCountry?.run {
                            binding.spCountry.setSelection(position)
                        }
                        val dataState = RowData.getSpinnerRowDataById(
                            stateID.toString(),
                            stateAdapter
                        )
                        dataState?.run {
                            binding.spState.setSelection(position)
                            selectedCityId = cityID.toString()
                        }
                        if (isPrimary)
                            binding.handleWhenPrimaryAddChecked()
                        else
                            binding.handleWhenPrimaryAddUnckecked()
                    }
                    OFFICEADDRESS -> {
                        binding.cbOffPrimaryAddress.isChecked = isPrimary
                        binding.etOffAdd1.setText(address1)
                        binding.etOffAdd2.setText(address2)
                        binding.etOffAdd3.setText(address3)
                        binding.etOffPincode.setText(pincode)
                        selectedStateOffId = stateID.toString()
                        selectedCityOffId = cityID.toString()

                        val dataCountry = RowData.getSpinnerRowDataById(
                            countryID.toString(),
                            countryAdapterOff
                        )
                        dataCountry?.run {
                            binding.spOffCountry.setSelection(position)
                        }
                        val dataState = RowData.getSpinnerRowDataById(
                            stateID.toString(),
                            stateAdapterOff
                        )
                        dataState?.run {
                            binding.spOffState.setSelection(position)
                            selectedCityOffId = cityID.toString()
                        }
                    }
                    else -> {
                    }
                }

            }
        }
    }

    override fun updateProduct(leadDetailAddProductList: List<LeadDetailAddProductModel>) {
        this.leadDetailAddProductList =
            leadDetailAddProductList as ArrayList<LeadDetailAddProductModel>
    }

    private var leadDetailAddProductAdapter: IndirectLeadDetailAddProductAdapter? = null

    private fun List<LeadProductMappingItem>.renderMapping() {
        leadDetailAddProductList.clear()
        mapNotNull {
            stage = it.stageID?.toString() ?: emptyString()
            status = it.statusID?.toString() ?: emptyString()
            leadDetailAddProductList.add(getLeadDetailAddProductModel(it))
        }


        leadDetailAddProductAdapter =
            IndirectLeadDetailAddProductAdapter(
                saathiApi,
                requireContext(),
                leadDetailAddProductList,
                binding.lvAddProduct,
                leadId,
                getPrimaryRM(),
                isLeadEditable,
                defaultSelectionList,
                categoryAdapter,
                workExpAdapter,
                userManager.getUser()?.employeeCode,
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                 emptyString(),
                this@IndirectLeadDetailFragment
            )
        binding.lvAddProduct.adapter = leadDetailAddProductAdapter
        CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail(binding.lvAddProduct)
        binding.nestedScrollView.apply {
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            isFocusable = true
            isFocusableInTouchMode = true
        }
    }

    private fun getLeadDetailAddProductModel(
        item: LeadProductMappingItem
    ): LeadDetailAddProductModel {
        val crossSellingProducts: List<CrossSellingProductsModel> =
            getCrossSellingProductsModel(item.crossSellingProducts)
        return LeadDetailAddProductModel(
            item.stageID.toString(),
            stringOrEmpty(item.stageName), item.statusID.toString(),
            stringOrEmpty(item.statusName), item.interestedIn.toString(),
            stringOrEmpty(item.productName),
            stringOrEmpty(item.primaryRMName),
            stringOrEmpty(item.productSpecialistName),
            stringOrEmpty(item.assignedBy),
            stringOrEmpty(item.assignedByDate),
            stringOrEmpty(item.clientCode), item.leadProductMappingID.toString(),
            item.isDefault,
            crossSellingProducts,
            item.isCrossSellingProduct,
            stringOrEmpty(item.isProductSpecialistEditDisabled),
            stringOrEmpty(item.isProductDisabled),
            stringOrEmpty(item.productSpecialist),
            stringOrEmpty(item.secondaryRM)
        )
    }

    private fun getLeadDetailAddProductModel(
        item: AddProductData
    ): LeadDetailAddProductModel {
        val crossSellingProducts: List<CrossSellingProductsModel> =
            getCrossSellingProductsModel(item.crossSellingProducts)
        return LeadDetailAddProductModel(
            item.stageId,
            stringOrEmpty(item.stageName), item.statusId,
            stringOrEmpty(item.statusName), item.interestedIn,
            stringOrEmpty(item.prodName),
            stringOrEmpty(item.primRMName),
            stringOrEmpty(item.productSpecialistName),
            stringOrEmpty(item.assignedBy),
            stringOrEmpty(item.assignedByDate),
            stringOrEmpty(item.clientCode), item.leadProductMappingId,
            item.isDefault,
            crossSellingProducts,
            item.isCrossSellingProduct,
            stringOrEmpty(item.isProdSpecEditDisabled),
            stringOrEmpty(item.isProdDisabled),
            stringOrEmpty(item.prodSpecialist),
            stringOrEmpty(item.secondaryRm)
        )
    }

    private fun getCrossSellingProductsModel(
        products: List<CrossSellingProduct>?
    ): List<CrossSellingProductsModel> {
        val crossSellingProducts: MutableList<CrossSellingProductsModel> =
            ArrayList()
        if (products != null && products.isNotEmpty()) {
            for ((leadProductId, productId, statusId, productName, statusName) in products) {
                crossSellingProducts.add(
                    CrossSellingProductsModel(
                        leadProductId,
                        productId,
                        statusId,
                        productName,
                        statusName
                    )
                )
            }
        }
        return crossSellingProducts
    }

    private var isLeadEditable = true

    override fun isSameAsAboveChecked(): Boolean = binding.cbSameAs.isChecked
    override fun onCountrySelected(): Observable<Int> = binding.spCountry.itemSelections()
    override fun onOfficeCountrySelected(): Observable<Int> = binding.spOffCountry.itemSelections()

    override fun onStateSelected(): Observable<Int> = binding.spState.itemSelections()
    override fun onOfficeStateSelected(): Observable<Int> = binding.spOffState.itemSelections()
    override fun onCitySelected(): Observable<Int> = binding.spCity.itemSelections()

    override fun renderOnCitySelectAndSameAsAboveChecked(position: Int) {
        binding.spOffCity.setSpinnerData(cityAdapterOff, cityAdapter.getItems())
        binding.spOffCity.setSelection(position)
    }

    override fun getSelectedCountry(): RowData = binding.spCountry.selectedItem as RowData
    override fun getSelectedOfficeCountry(): RowData = binding.spOffCountry.selectedItem as RowData
    override fun getSelectedState(): RowData = binding.spState.selectedItem as RowData

    override fun getSelectedOfficeState(): RowData = binding.spOffState.selectedItem as RowData

    override fun renderStateOnCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { Timber.d("LeadDetailFragment renderStateOnCountrySelection showOnFailurePopup %s", it.message)
            showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spState.setSpinnerData(stateAdapter, it)
            if (selectedStateId != DEFAULT_SELECTION_ID) {
                val dataState = RowData.getSpinnerRowDataById(
                    selectedStateId,
                    stateAdapter
                )
                dataState?.let {
                    binding.spState.setSelection(it.position)
                    selectedStateId = DEFAULT_SELECTION_ID
                }
            }
            binding.spCity.setSpinnerData(cityAdapter, defaultSelectionList)
            if (binding.cbSameAs.isChecked) {
                binding.spOffCountry.setSelection(binding.spCountry.selectedItemPosition)
            }
        }
    }

    override fun renderOfficeStateOnOfficeCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { Timber.d("LeadDetailFragment renderOfficeStateOnOfficeCountrySelection showOnFailurePopup %s", it.message)
            showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spOffState.setSpinnerData(stateAdapterOff, it)
            if (selectedStateOffId != DEFAULT_SELECTION_ID) {
                val dataStateOff = RowData.getSpinnerRowDataById(
                    selectedStateOffId,
                    stateAdapterOff
                )
                dataStateOff?.let {
                    binding.spOffState.setSelection(it.position)
                    selectedStateOffId = DEFAULT_SELECTION_ID
                }
            }
            binding.spOffCity.setSpinnerData(cityAdapterOff, defaultSelectionList)
        }
    }

    override fun renderCityOnStateSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {  Timber.d("LeadDetailFragment renderCityOnStateSelection showOnFailurePopup %s", it.message)
            showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spCity.setSpinnerData(cityAdapter, it)
            if (selectedCityId != DEFAULT_SELECTION_ID) {
                val dataCity = RowData.getSpinnerRowDataById(
                    selectedCityId,
                    cityAdapter
                )
                dataCity?.let {
                    binding.spCity.setSelection(it.position)
                    selectedCityId = DEFAULT_SELECTION_ID
                }
            }
            if (binding.cbSameAs.isChecked) {
                binding.spOffState.setSelection(binding.spState.selectedItemPosition)
            }
        }
    }

    override fun renderOfficeCityOnOfficeStateSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { toggleProgressBar(uiModel.inProgress) }
        uiModel.onSuccess {
            binding.spOffCity.setSpinnerData(cityAdapterOff, it)
            if (selectedCityOffId != DEFAULT_SELECTION_ID) {
                val dataCity = RowData.getSpinnerRowDataById(
                    selectedCityOffId,
                    cityAdapterOff
                )
                dataCity?.run {
                    binding.spOffCity.setSelection(position)
                    selectedCityOffId = DEFAULT_SELECTION_ID
                }
            }
        }
    }

    override fun onDestroyView() {
        presenter.stop()
//        addProductDialogBinding = null
        productSpecialDialog = null
        assignToDialog = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            position: Int,
            type: String,
            leadID: String,
            ucId: String
        ): IndirectLeadDetailFragment {
            return IndirectLeadDetailFragment().apply {
                val args = Bundle().apply {
                    putInt(POSITION, position)
                    putString(TYPE, type)
                    putString(LEADID, leadID)
                    putString(UCID, ucId)
                }
                arguments = args
            }
        }

        const val CONST_OUTER_SECTION_EXPANSION = "OuterSectionExpansion"
        const val CONST_INNER_SECTION_EXPANSION = "InnerSectionExpansion"
        const val CONST_OUTER = "Inner"
        const val CONST_INNER = "Outer"
        const val DEFAULT_SELECTION_ID = "-1"
        const val CONST_HYPHEN_PREFIX = "-"
        const val CONST_DEFAULT_SELECTION_SIZE = 1

    }

    override fun newInvestmentProductAdded(
        position: Int,
        crossSellingProductsModel: CrossSellingProductsModel
    ) {
        val selectedProduct =
            leadDetailAddProductAdapter?.getItem(position) as LeadDetailAddProductModel
        crossSellingProductsModel.run {
            selectedProduct.crossSellingProductsModelList.add(this)
        }
        leadDetailAddProductAdapter?.notifyDataSetChanged()
    }

    override fun updateInvestmentProduct(
        position: Int,
        selectedRowData: RowData,
        productId: String
    ) {
        val selectedProduct =
            leadDetailAddProductAdapter?.getItem(position) as LeadDetailAddProductModel
        selectedProduct.crossSellingProductsModelList.find { it.crossSellingProductId == productId }
            ?.run {
                crossSellingStatusId = selectedRowData.key
                crossSellingStatusName = selectedRowData.value
            }
        leadDetailAddProductAdapter?.notifyDataSetChanged()

    }

    override fun showFailureDialog(error: Throwable) {
        Timber.d("LeadDetailFragment showFailureDialog showOnFailurePopup %s", error.message)

        showOnFailurePopup(error)
    }

    override fun showNoInternetDialog() {
        noInternetConnectionDialog()
    }

    override fun getErrorFailure(error: Throwable): String {
        return getErrorOnFailureNew(error)
    }

    override fun toggleProgressDialog(inProgress: Boolean) {
        toggleProgressBar(inProgress)
    }

    override fun hideKeyBoard(view: View) {
        hideKeyboard(view)
    }

    override fun openHome() {
        navigator.openHome()
    }

}