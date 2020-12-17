package com.mosl.tabapp.leadViewDetail.leaddetail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.focusChanges
import com.jakewharton.rxbinding3.view.touches
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.mosl.app.core.events.DataAction.Fetch
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
import com.mosl.tabapp.R
import com.mosl.tabapp.R.color
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.common.*
import com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString
import com.mosl.tabapp.common.CRMAppUtil.isBlank
import com.mosl.tabapp.common.CustomListPickerDialog.ListPickerListneable
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.DialogLeadviewAddProductBinding
import com.mosl.tabapp.databinding.DialogUpdateProfileBinding
import com.mosl.tabapp.databinding.FragmentLeadDetailBinding
import com.mosl.tabapp.leadViewDetail.*
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
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.util.*
import com.mosl.validator.data.ValidationError
import com.mosl.validator.exception.ValidationException
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.frag_lead_creation.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class LeadDetailFragment : CommonFragment(), LeadDetailView {

    @Inject
    internal lateinit var presenter: LeadDetailPresenter
    private lateinit var binding: FragmentLeadDetailBinding
    private var isReferee = false

    private var leadId = "-1"
    private var ucId = "-1"
    private var sourceIdStr = ""
    private var leadProspectSourceIdStr = ""

    private var stage: String? = null
    private var status: String? = null

    private val subject = PublishSubject.create<LeadDetailUiEvent>()

    private var interestedData = emptyList<RowData>()
    private var statusData = ArrayList<RowData>()
    private var stageData = ArrayList<RowData>()

    private var selectedCityId = "-1"
    private var selectedCityOffId = "-1"
    private var pmsList: List<String> = emptyList()

    private val addressDetailList: ArrayList<AddressDetailModel> = ArrayList()
    private val contactDetailList: ArrayList<ContactDetailModel> = ArrayList()

    private val professionAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val profileAdapter: ArrayAdapter<RowData> by lazy {

        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val interestedAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val stageAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val statusAdapter: ArrayAdapter<RowData> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<RowData>(requireContext())
    }

    private val pmsProductAdapter: ArrayAdapter<String> by lazy {
        SpinnerAdapterFactory.getDefaultAdapter<String>(requireContext())
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

    override fun getLayout(): Int = R.layout.fragment_lead_detail

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLeadDetailBinding.bind(view)
        setupView()
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }

    private fun setupView() {
        defaultSelectionList.add(0, SELECT_ROW_DATA)
        with(binding) {
            val arrayPmsProduct =
                requireActivity().resources.getStringArray(R.array.selectPmsProduct)
            pmsList = ArrayList(listOf(*arrayPmsProduct))
            setIcon(llClientDetail, tvClientDetail)
            setIcon(llLeadDetail, tvLeadDetail)
            setIcon(llClientPortfolio, tvClientPortfolioDetails)
            etDob.inputType = InputType.TYPE_NULL
            etAnniversary.inputType = InputType.TYPE_NULL
            etPanNumber.filters = arrayOf(
                InputFilter.LengthFilter(10),
                InputFilter.AllCaps()
            )
            includePortfolio.apply {
                btnBrokPortfolio.gone()
                btnAssetPortfolio.gone()
                llPortfolio.visible()
                llBorkingPortfolio.visible()
                llCash.visible()
                llAssestPortfolio.visible()
                setIcon(llPortfolio, txtPortfolioSnapshot, "OUTER")
                setIcon(llProfitLoss, txtPorfitLoss, "OUTER")
                setIcon(llAvailableLimits, txtAvailableLimits, "OUTER")

                setIcon(llBorkingPortfolio, txtBrokingPortfolio, "INNER")
                setIcon(llAssestPortfolio, txtAssestPortfolio, "INNER")
                setIcon(llCash, txtCash, "INNER")

                setIcon(llBorkingProducts, txtBrokingProducts, "INNER")
                setIcon(llAssestProducts, txtAssestProducts, "INNER")
            }
        }
    }

    private fun setIcon(parent: View, icon: TextView, Section: String) {
        if (Section.equals("OUTER", ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_minus, 0)
            } else {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_plus, 0)
            }
        } else if (Section.equals("INNER", ignoreCase = true)) {
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

    override fun onProfileLayoutClicked(): Observable<Unit> = binding.llProfile.clicks()

    override fun onUpdateProfileEventFired(): Observable<UpdateProfile> = subject.ofType()

    override fun renderUpdateProfile(uiModel: UiModel<Pair<String, String>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            if (it.first.isEmpty())
                binding.tvProfile.text = it.second
            else
                MSFDialog.alertDialog(
                    requireContext(),
                    0,
                    getString(R.string.motilal_oswal),
                    it.first,
                    "OK",
                    true,
                    null
                )
        }
    }

    override fun renderProfileClick() {
        Dialog(requireContext()).apply {
            val dialogBinding = DialogUpdateProfileBinding.inflate(layoutInflater)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogBinding.spProfile.apply {
                adapter = profileAdapter
            }
            dialogBinding.ivClose.setOnClickListener { dismiss() }
            dialogBinding.btnUpdate.setOnClickListener {
                val selectedProfile = dialogBinding.spProfile.selectedItem as RowData
                subject.onNext(UpdateProfile(selectedProfile))
                dismiss()
            }

        }
            .show()
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
                    ), "${getString(R.string.text_msg_success_add_product)}$leadID",
                    "OK", true
                ) { action: Action, _: Array<Any?>? ->
                    if (action == OK) {
                        val bundle = Bundle()
                        bundle.putString(LEAD_ID, leadID)
                        bundle.putString(MOSLConstants.UCID, uCID)
                        navigator.openPage(
                            NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true,
                            forceReload = false
                        )
                    }
                }
            } else {
                if (it.isNotEmpty()) {
                    for (datum in it) {
                        val potentialPMSProduct = stringOrEmpty(datum.potentialPmsProduct)
                        isPotentialPMSProduct = potentialPMSProduct.isEmpty()
                        pmsProductAdapter.setSpinnerDataWithSelection(
                            binding.spPmsProduct, pmsList, potentialPMSProduct
                        )
                        leadDetailAddProductList.add(getLeadDetailAddProductModel(datum))
                    }
                    leadDetailAddProductAdapter =
                        LeadDetailAddProductAdapter(
                            saathiApi,
                            requireContext(),
                            leadDetailAddProductList,
                            binding.lvAddProduct,
                            leadId,
                            isLeadEditable,
                            statusData,
                            statusAdapter,
                            stageAdapter,
                            userManager.getUser()?.employeeCode,
                            status,
                            userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                            (binding.spPmsProduct.selectedItem as? RowData)?.value
                                ?: emptyString()
                        )
                    binding.lvAddProduct.adapter = leadDetailAddProductAdapter
                    CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail(binding.lvAddProduct)
                }
                MSFDialog.alertDialog(
                    requireActivity(), 0,
                    resources.getString(R.string.motilal_oswal),
                    "Product Added Successfully", "OK",
                    true
                ) { _: Action?, _: Array<Any?>? ->
                }
            }
        }
    }

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
                layoutInflater.inflate(
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

                                primaryRm = if (isBlank(leadOwne.trim())) {
                                    advisoryCode
                                } else {
                                    leadOwne
                                }
                                val rm = primaryRm!!
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
            dialogBinding.spInterestedIn.adapter = interestedAdapter
            dialogBinding.spStage.adapter = stageAdapter
            dialogBinding.spStatus.adapter = statusAdapter
            interestedAdapter.setSpinnerData(dialogBinding.spInterestedIn, interestedData)

            dialogBinding.spInterestedIn.itemSelections()
                .filter(InvalidSelectionPredicate)
                .map { dialogBinding.spInterestedIn.selectedItem as RowData }
                .filter { it.key != LeadDetailView.INVALID_SELECTION_KEY }
                .subscribe {
                    subject.onNext(SelectInterestedProductEvent(it))
                }
                .addTo(disposable)

            dialogBinding.spStage.itemSelections()
                .filter(InvalidSelectionPredicate)
                .map { dialogBinding.spStage.selectedItem as RowData }
                .filter { it.key != LeadDetailView.INVALID_SELECTION_KEY }
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
                        LeadDetailAddProductAdapter(
                            saathiApi,
                            requireContext(),
                            leadDetailAddProductList,
                            binding.lvAddProduct,
                            leadId,
                            isLeadEditable,
                            statusData,
                            statusAdapter,
                            stageAdapter,
                            userManager.getUser()?.employeeCode,
                            status,
                            userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                            (binding.spPmsProduct.selectedItem as? RowData)?.value
                                ?: emptyString()
                        )
                }
                if (leadDetailAddProductAdapter?.checkProduct(interestedInKey) == true) {
                    MSFDialog.alertDialog(
                        requireContext(), 0,
                        resources.getString(R.string.motilal_oswal),
                        "$interestedInDisplayText is already selected", "OK", true, null
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
        stageData.clear()
        stageData.add(0, SELECT_ROW_DATA)
        stageAdapter.setSpinnerData(addProductDialogBinding.spStage, stageData)

        statusData.clear()
        statusData.add(0, SELECT_ROW_DATA)
        statusAdapter.setSpinnerData(addProductDialogBinding.spStatus, statusData)
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

    private fun FragmentLeadDetailBinding.handleWhenPrimaryAddChecked(isEnabledRequired: Boolean = false) {
        cbOffPrimaryAddress.gone()
        cbSameAs.visible()
        cbOffPrimaryAddress.isChecked = false
        if (isEnabledRequired) {
            enableField(llResAdd)
            enableField(llOffAdd)
        }
    }

    private fun FragmentLeadDetailBinding.handleWhenPrimaryAddUnckecked(isDisabledRequired: Boolean = false) {
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

    override fun onClientDetailClicked(): Observable<Unit> = binding.tvClientDetail.clicks()

    override fun onClientPortfolioDetailClicked(): Observable<Unit> =
        binding.tvClientPortfolioDetails.clicks()

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

    override fun onClientDetailSectionClicked(): Observable<Unit> = binding.tvClientDetail.clicks()

    override fun onClientPortfolioSectionClicked(): Observable<Unit> =
        binding.tvClientPortfolioDetails.clicks()

    override fun onLeadDetailSectionClicked(): Observable<Unit> = binding.tvLeadDetail.clicks()

    override fun toggleClientDetailSection() {
        toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
    }

    override fun toggleLeadDetailSection() {
        toggleSectionExpansion(binding.llLeadDetail, binding.tvLeadDetail)
    }

    override fun toggleClientPortfolioSection() {
        toggleSectionExpansion(binding.llClientPortfolio, binding.tvClientPortfolioDetails)
    }

    override fun onPortfolioSnapshotClicked(): Observable<Unit> {
        return binding.includePortfolio.txtPortfolioSnapshot.clicks()
    }

    override fun togglePortfolio() {
        toggleSectionExpansion(
            binding.includePortfolio.llPortfolio,
            binding.includePortfolio.txtPortfolioSnapshot,
            OUTER
        )
    }

    override fun onProfitLossSnapshotClicked(): Observable<Unit> =
        binding.includePortfolio.txtPorfitLoss.clicks()

    override fun onAvailableLimitSnapshotClicked(): Observable<Unit> =
        binding.includePortfolio.txtAvailableLimits.clicks()

    override fun toggleProfitLoss() {
        toggleSectionExpansion(
            binding.includePortfolio.llProfitLoss,
            binding.includePortfolio.txtPorfitLoss,
            OUTER
        )
    }

    override fun toggleAvailableLimit() {
        toggleSectionExpansion(
            binding.includePortfolio.llAvailableLimits,
            binding.includePortfolio.txtAvailableLimits,
            OUTER
        )
    }

    override fun onBrokingPortfolioClicked(): Observable<Unit> =
        binding.includePortfolio.txtBrokingPortfolio.clicks()

    override fun toggleBrokingPortfolio() {
        toggleSectionExpansion(
            binding.includePortfolio.llBorkingPortfolio,
            binding.includePortfolio.txtBrokingPortfolio,
            INNER
        )
    }

    override fun onAssetPortfolioClicked(): Observable<Unit> =
        binding.includePortfolio.txtAssestPortfolio.clicks()

    override fun toggleAssetPortfolio() {
        toggleSectionExpansion(
            binding.includePortfolio.llAssestPortfolio,
            binding.includePortfolio.txtAssestPortfolio,
            INNER
        )
    }

    override fun onLedgerClicked(): Observable<Unit> = binding.includePortfolio.txtCash.clicks()

    override fun toggleLedger() {
        toggleSectionExpansion(
            binding.includePortfolio.llCash,
            binding.includePortfolio.txtCash,
            INNER
        )
    }

    override fun onBrokingProductClicked(): Observable<Unit> =
        binding.includePortfolio.txtBrokingProducts.clicks()

    override fun toggleBrokingProduct() {
        toggleSectionExpansion(
            binding.includePortfolio.llBorkingProducts,
            binding.includePortfolio.txtBrokingProducts,
            INNER
        )
    }

    override fun onAssetProductClicked(): Observable<Unit> =
        binding.includePortfolio.txtAssestProducts.clicks()

    override fun toggleAssetProduct() {
        toggleSectionExpansion(
            binding.includePortfolio.llAssestProducts,
            binding.includePortfolio.txtAssestProducts,
            INNER
        )
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

    override fun renderClientDetail(uiModel: UiModel<ClientDetail>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->

            if (data.products.isNotEmpty()) {
                val product = data.products[0]
                binding.tvClientCode.text = product.clientCode
                binding.tvUCID.text = product.ucId
                binding.tvBranchCode.text = product.branchCode
                binding.tvChannel.text = product.channel
                advisoryCode = product.advisorCode
            }

            binding.tvClientAddress.text = data.address
            binding.tvClientName.text = data.name
            binding.tvPanCard.text = data.pan
            binding.tvAccountType.text = data.accountType
            binding.tvFamilyUCID.text = data.familyUcid
            binding.tvAdvisoryType.text = data.advisoryType
            binding.tvAadharCard.text = data.aadhar
            binding.tvAccountNumber.text = data.account
            binding.tvProfile.text = data.profile
            binding.tvDefaultBank.text = data.defaultBank

            binding.llClient.visible()
            binding.llClientPortfolioDetails.gone()
            toggleSectionExpansion(binding.llClientDetail, binding.tvClientDetail)
        }
    }

    private fun ClientDetail.renderClient() {
        if (products.isNotEmpty()) {
            val product = products[0]
            binding.run {
                tvClientCode.text = product.clientCode
                tvUCID.text = product.ucId
                tvBranchCode.text = product.branchCode
                tvChannel.text = product.channel
                advisoryCode = product.advisorCode
            }
        }

        binding.run {
            tvClientAddress.text = address
            tvClientName.text = name
            tvPanCard.text = pan
            tvAccountType.text = accountType
            tvFamilyUCID.text = familyUcid
            tvAdvisoryType.text = advisoryType
            tvAadharCard.text = aadhar
            tvAccountNumber.text = account
            tvProfile.text = profile
            tvDefaultBank.text = defaultBank

            llClient.visible()
            llClientPortfolioDetails.visible()
            toggleSectionExpansion(llClientDetail, tvClientDetail)
        }
    }

    override fun renderClientDetailWithPortfolioAndProfitLoss(uiModel: UiModel<ClientDetailWithPortfolioAndProfitLoss>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->
            data.detail.renderClient()
            data.portfolio.renderPortfolio()
            data.profitLoss.renderProfit()

        }
    }

    private fun ClientPortfolio.renderPortfolio() {
        binding.includePortfolio.apply {
            networth.text = StringStuff.commaDecorator(netWorth)
            daychgamt.text = StringStuff.commaDecorator(dayChangeAmount)
            daychgperst.text = getString(
                R.string.text_common_with_percentile,
                StringStuff.commaDecorator(dayChangePer)
            )

            overallchgamt.text = StringStuff.commaDecorator(overallChangeAmt)
            overallchgperst.text = getString(
                R.string.text_common_with_percentile,
                StringStuff.commaDecorator(overallChangePer)
            )

            val bgColor = if (dayChangeAmount.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            daychgamt.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColor))

            val bgColorPer = if (dayChangePer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            daychgperst.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorPer))

            val bgColorOverall = if (overallChangeAmt.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            overallchgamt.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    bgColorOverall
                )
            )

            val bgColorOverallPer = if (overallChangePer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            overallchgperst.setBackgroundColor(
                ContextCompat.getColor(requireContext(), bgColorOverallPer)
            )

            ///////////broking portfolio

            val brokingMapList = mutableListOf<HashMap<String, String>>()
            brokingPortfolios.forEach {
                val map = hashMapOf(
                    PRODUCT to it.productName,
                    INVEST_AMT to StringStuff.commaDecorator(it.investmentValue),
                    PURCHASE_VALUE to StringStuff.commaDecorator(it.marketValue),
                    OVERALL_GAIN_LOSS to StringStuff.commaDecorator(it.overallGainLoss),
                    PERCENTAGE_GAIN_LOSS to StringStuff.commaDecorator(
                        "${it.percentageGainLoss} %"
                    ),
                    MOSLConstants.PRODUCT_ID to it.productId
                )
                brokingMapList.add(map)
            }

            val brokingAdapter =
                PortfolioSnapshotBrokAdapter(requireContext(), brokingMapList as ArrayList)
            listBrokingPortfolio.adapter = brokingAdapter
            brokingAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(listBrokingPortfolio)

            //////////asset portfolio

            val assetMapList = mutableListOf<HashMap<String, String>>()
            assetPortfolios.forEach {
                val map = hashMapOf(
                    PRODUCT to it.productName,
                    INVEST_AMT to StringStuff.commaDecorator(it.marketValue),
                    PURCHASE_VALUE to StringStuff.commaDecorator(it.purchaseValue),
                    OVERALL_GAIN_LOSS to StringStuff.commaDecorator(it.overallGainLoss),
                    PERCENTAGE_GAIN_LOSS to StringStuff.commaDecorator(
                        "${it.percentageGainLoss} %"
                    ),
                    MOSLConstants.PRODUCT_ID to it.productId,
                    PRODUCT_TYPE to it.productType
                )
                assetMapList.add(map)
            }

            val assetAdapter =
                PortfolioSnapshotAssetAdapter(requireContext(), assetMapList as ArrayList)
            listAssestPortfolio.adapter = assetAdapter
            assetAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(listAssestPortfolio)

            ///////////Ledger

            val ledgerMapList = mutableListOf<HashMap<String, String>>()
            availableLedgers.forEach {
                val map = hashMapOf(
                    PRODUCT to it.productName,
                    TOTAL to StringStuff.commaDecorator(it.balanceLedger)
                )
                ledgerMapList.add(map)
            }

            val ledgerAdapter =
                PortfolioSnapshotCashAdapter(requireContext(), ledgerMapList as ArrayList)
            listAssestPortfolio.adapter = ledgerAdapter
            ledgerAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(listAssestPortfolio)
        }
    }

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

    private fun ClientProfitLoss.renderProfit() {

        binding.includePortfolio.apply {
            tvTotalGainLoss.text = StringStuff.commaDecorator(totalGainLoss)

            tvLTGainLoss.text = StringStuff.commaDecorator(ltGainLoss)
            tvSTGainLossPer.text = getString(
                R.string.text_common_with_percentile,
                StringStuff.commaDecorator(ltGainLossPer)
            )

            val bgColor = if (ltGainLoss.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvLTGainLoss.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColor))

            val bgColorPer = if (ltGainLossPer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvLTGainLossPer.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorPer))

            tvSTGainLoss.text = StringStuff.commaDecorator(stGainLoss)
            tvSTGainLossPer.text = getString(
                R.string.text_common_with_percentile,
                StringStuff.commaDecorator(stGainLossPer)
            )

            val bgColorSt = if (stGainLoss.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvSTGainLoss.setBackgroundColor(ContextCompat.getColor(requireContext(), bgColorSt))

            val bgColorPerSt = if (stGainLossPer.startsWith("-")) {
                color.action_bar_negative
            } else {
                color.action_bar_positive
            }
            tvSTGainLossPer.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    bgColorPerSt
                )
            )

            ///////////broking product

            val brokingMapList = mutableListOf<HashMap<String, String>>()
            brokingProducts.forEach {
                val map = hashMapOf(
                    MOSLConstants.PRODUCT_ID to it.productId,
                    PRODUCT to it.productName,
                    STGL to StringStuff.commaDecorator(it.stGainLoss),
                    LTGL to StringStuff.commaDecorator(it.ltGainLoss),
                    TOTAL_GAIN_LOSS to StringStuff.commaDecorator(it.totalGainLoss)
                )
                brokingMapList.add(map)
            }

            val brokingAdapter =
                PortfolioPLBrokingAdapter(requireContext(), brokingMapList as ArrayList)
            lvPLBrok.adapter = brokingAdapter
            brokingAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(lvPLBrok)

            //////////asset product

            val assetMapList = mutableListOf<HashMap<String, String>>()
            assetProducts.forEach {
                val map = hashMapOf(
                    MOSLConstants.PRODUCT_ID to it.productId,
                    PRODUCT to it.productName,
                    STGL to StringStuff.commaDecorator(it.stGainLoss),
                    LTGL to StringStuff.commaDecorator(it.ltGainLoss),
                    TOTAL_GAIN_LOSS to StringStuff.commaDecorator(it.totalGainLoss)
                )
                assetMapList.add(map)
            }

            val assetAdapter = PortfolioPLAssetAdapter(requireContext(), assetMapList as ArrayList)
            lvPLAsset.adapter = assetAdapter
            assetAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(lvPLAsset)

            ///////////Limit

            val limitMapList = mutableListOf<HashMap<String, String>>()
            availableLimits.forEach {
                val map = hashMapOf(
                    CASH to it.cashType,
                    AMOUNT to StringStuff.commaDecorator(it.amount)
                )
                limitMapList.add(map)
            }

            val limitAdapter = PortfolioPLLimitsAdapter(requireContext(), limitMapList as ArrayList)
            lvPLLimits.adapter = limitAdapter
            limitAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnChildren(lvPLLimits)
        }

    }

    private fun toggleSectionExpansion(
        parent: View,
        icon: TextView,
        expansion: String
    ) {
        if (expansion.equals("OuterSectionExpansion", ignoreCase = true)) {
            if (parent.visibility == View.VISIBLE) {
                parent.gone()
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_plus, 0)
            } else {
                parent.visible()
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable.ic_upload_minus, 0)
            }
        } else if (expansion.equals("InnerSectionExpansion", ignoreCase = true)) {
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
            MSFDialog.alertDialog(
                requireContext(), 0,
                resources.getString(R.string.motilal_oswal), CONFIRM_LEAD, "YES",
                "NO", true
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
                    binding.tlLeadName.setErrorMessages(getString(it, binding.etLeadCategory.text))
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
                8 ->
                    binding.tlLand.setErrorMessages(getString(it))
                9 ->
                    binding.tlPan.setErrorMessages(getString(it))
                10 ->
                    binding.tlAadhar.setErrorMessages(getString(it))
                11, 12 ->
                    binding.tlRefereeNumber.setErrorMessages(getString(it))
                13 ->
                    binding.tlRefereeEmail.setErrorMessages(getString(it))
                else -> {
                }
            }
        }
    }

    private fun removeErrorMessage() {
        binding.apply {
            tlLeadName.disableErrorMessages()
            tlContactPerson.disableErrorMessages()
            tlEmail.disableErrorMessages()
            tlAlternateEmail.disableErrorMessages()
            tlAlternateMobile.disableErrorMessages()
            tlMobile.disableErrorMessages()
            tlLand.disableErrorMessages()
            tlPan.disableErrorMessages()
            tlAadhar.disableErrorMessages()
            tlRefereeCode.disableErrorMessages()
            tlRefereeName.disableErrorMessages()
            tlRefereeName.disableErrorMessages()
            tlRefereeNumber.disableErrorMessages()
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
        leadDetailModel.isSameAsAbove = cbSameAs.isChecked
        leadDetailModel.seteMail(binding.etEmail.text.toString().trim())
        leadDetailModel.alternateEmail = binding.etAlternateEmail.text.toString().trim()
        leadDetailModel.alternateMobileNo = binding.etAlternateMobileNumber.text.toString().trim()
        leadDetailModel.mobileNo = binding.etMobileNumber.text.toString().trim()
        leadDetailModel.ekycMobileNo = binding.etEkycMobileNumber.text.toString().trim()
        leadDetailModel.landLineNo = binding.etLandlineNumber.text.toString().trim()
        leadDetailModel.aadharNum = binding.etAadharNumber.text.toString().trim()
        leadDetailModel.panNum = binding.etPanNumber.text.toString().trim()
        leadDetailModel.isPotentialPMSProduct = binding.spPmsProduct.selectedItem.toString()
        leadDetailModel.prospectRefereName = etProspectRefereeName.text.toString().trim()
        leadDetailModel.prospectRefereMobile = etProspectRefereerMobile.text.toString().trim()
        leadDetailModel.prospectRefereEmail = etProspectRefereerEmail.text.toString().trim()

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
        leadDetailModel.contactPerson = binding.etContactPerson.text.toString().trim()
        leadDetailModel.sourceID = sourceIdStr
        leadDetailModel.leadSourceID = leadProspectSourceIdStr
        leadDetailModel.leadSourceDetailsID = "0"
        leadDetailModel.leadSourceDetails = binding.etProspectSourceDetail.text.toString()
        leadDetailModel.name = binding.etLeadName.text.toString().trim()
        if (etRefereeCode.text.toString().trim().length >= 3 /*&& isRefreeCode*/) {
            leadDetailModel.refereeCode = binding.etRefereeCode.text.toString().trim()
        }
        if (etRefereeName.text.toString().trim().length >= 3 /*&& isRefreeCode*/) {
            leadDetailModel.refereeName = binding.etRefereeName.text.toString().trim()
        }
        leadDetailModel.refereeEmail = binding.etRefereeEmail.text.toString().trim()
        leadDetailModel.refereeMobile = binding.etRefereeNumber.text.toString().trim()
        leadDetailModel.comments = getJsonSafeString(binding.etComments.text.toString().trim())
        leadDetailModel.profileID = (binding.spProfile.selectedItem as RowData).key
        leadDetailModel.professionID = (binding.spProfession.selectedItem as RowData).key
        leadDetailModel.createdBy = binding.etCreatedBy.text.toString().trim()
        leadDetailModel.createdThrough = "0"
        leadDetailModel.creationDate = binding.etCreationDate.text.toString().trim()
            .replace("p.m.", "PM")
            .replace("a.m", "AM")
        leadDetailModel.contactDetailModels = contactDetailList
        leadDetailModel.addressDetailModels = addressDetailList
        leadDetailModel.secondaryRM = null
        leadDetailModel.leadDetailAddProductModels = leadDetailAddProductList
        leadDetailModel.leadFieldValueModels = getLeadFieldValues(mLeadFieldSectionModelList)

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
            model.profileID,
            model.professionID, model.createdBy, model.prospectRefereName,
            model.prospectRefereMobile, model.prospectRefereEmail,
            model.createdThrough, model.creationDate,
            contactRequest,
            model.leadFieldValueModels
        )
    }

    override fun renderUpdateDetail(uiModel: UiModel<UpdateLeadResponse>) {
        toggleProgress(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            leadUpdatedDialog()
        }
    }

    private fun leadUpdatedDialog() {
        MSFDialog.alertDialog(
            requireActivity(), 0,
            resources.getString(
                R.string.motilal_oswal
            ), resources.getString(R.string.lead_updated), "OK", true
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

    override fun render(uiModel: UiModel<LeadDetailPageData>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            interestedData.toMutableList()
                .clear()
            showOnFailurePopup(it)
        }
        uiModel.onSuccess { data ->
            profileAdapter.addAll(data.profileRowDatas)
            binding.spProfile.setSpinnerData(profileAdapter, data.profileRowDatas)
            interestedData = data.interestInRowDatas
            interestedAdapter.addAll(data.interestInRowDatas)
            binding.spCountry.setSpinnerData(countryAdapter, data.countryRowDatas)
            binding.spOffCountry.setSpinnerData(countryAdapterOff, data.countryRowDatas)
            binding.llEditLeadDetail.performClick()
        }
    }

    override fun renderLeadDetailAndProfession(uiModel: UiModel<LeadDetailAndProfession>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess { data ->
            binding.spProfession.setSpinnerData(professionAdapter, data.professionRowDatas)
            data.detail.renderLeadDetail()
        }
    }

    private var leadDetailAddProductList = ArrayList<LeadDetailAddProductModel>()

    private fun setEnableDisable(status: Boolean) {
        binding.apply {
            etLeadName.isEnabled = status
            etPanNumber.isEnabled = status
            etAadharNumber.isEnabled = status
            etContactPerson.isEnabled = status
            etLandlineNumber.isEnabled = status
            etRefereeCode.isEnabled = status
            etRefereeName.isEnabled = status
            etProspectRefereeName.isEnabled = status
            etProspectRefereerMobile.isEnabled = status
            etProspectRefereerEmail.isEnabled = status
            etLandlineNumber.isEnabled = status
            etDob.isEnabled = status
            etAnniversary.isEnabled = status
            etAlternateMobileNumber.isEnabled = status
            etAlternateEmail.isEnabled = status
            etEmail.isEnabled = status
            etContactPerson.isEnabled = status
            etComments.isEnabled = status
            btnSubmit.isEnabled = status
            spProfile.isEnabled = status
            spProfession.isEnabled = status
            cbSameAs.isEnabled = status
            cbPrimaryAddress.isEnabled = status
            cbOffPrimaryAddress.isEnabled = status
            spPmsProduct.isEnabled = status
            if (status) {
                if (cbPrimaryAddress.isChecked) {
                    enableField(llResAdd)
                    enableField(llOffAdd)
                }
                if (cbOffPrimaryAddress.isChecked) {
                    enableField(llOffAdd)
                    enableField(llResAdd)
                }
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

    private var leadOwne = ""

    private fun LeadDetail.renderLeadDetail() {
        isLeadEditable = when {
            canEditLead == "0" || isProductSpecialist.equals("Y", true) -> false
            else -> true
        }

        binding.spPmsProduct.apply {
            adapter = pmsProductAdapter
        }

        leadOwne = leadOwner

        pmsProductAdapter.setSpinnerDataWithSelection(
            binding.spPmsProduct, pmsList, isPotentialPMSProduct
        )
        binding.etProspectRefereerEmail.setText(prospectReferEmail)
        binding.etProspectRefereeName.setText(prospectReferName)
        binding.etRefereeCode.setText(refereeCode)
        binding.etRefereeName.setText(refereeName)
        binding.etRefereeEmail.setText(referEmail)
        binding.etRefereeNumber.setText(referMobile)
        binding.etCreatedBy.setText(createdBy)
        binding.etCreatedThrough.setText(createdThrough)
        binding.etLastModifiedBy.setText(lastModifiedBy)
        try {
            binding.etLastModifiedDate.setText(
                CRMAppUtil.getDateFromString(
                    lastModifiedDate,
                    "yyyy-MM-dd'T'kk:mm:ss", DATE_FORMAT_TO_DISPLAY
                )
            )
        } catch (e: Exception) {
            binding.etLastModifiedDate.setText(lastModifiedDate.replace("T", " "))
        }

        binding.etProspectSource.setText(prospectSource)
        binding.etProspectSourceDetail.setText(leadSourceDetails)
        binding.etComments.setText(comments)
        binding.etContactPerson.visibility =
            if (leadCategoryText.equals("Individual", ignoreCase = true)) {
                isIndividualsFlag = true
                View.GONE
            } else {
                isIndividualsFlag = false
                View.VISIBLE
            }

        sourceIdStr = sourceId
        leadProspectSourceIdStr = leadSourceId.toString()
        binding.etLeadID.setText(leadId)
        binding.etLeadName.setText(contact?.name)
        try {//Jeet
            binding.etCreationDate.setText(
                CRMAppUtil.getDateFromString(
                    createdDate,
                    "yyyy-MM-dd'T'kk:mm:ss", DATE_FORMAT_TO_DISPLAY
                )
            )
        } catch (e: Exception) {
            binding.etCreationDate.setText(createdDate.replace("T", " "))
        }//Jeet
        binding.etSource.setText(sourceName)

        binding.etLeadCategory.setText(leadCategoryText)

        if (leadProductMapping != null && leadProductMapping.isNotEmpty()) {
            leadProductMapping.renderMapping()
        }

        contact?.renderContact()

    }

    private fun com.mosl.saathi.api.model.lead.Contact.renderContact() {
        binding.run {
            etPanNumber.setText(panNumber)
            etAadharNumber.setText(aadharNumber)
            etEkycMobileNumber.setText(eKYCMobileNo)
            etMobileNumber.setText(mobile)
            etEmail.setText(emailId)
            etAlternateEmail.setText(alternateEmailId)
            etLandlineNumber.setText(landlineNo)
            etAlternateMobileNumber.setText(alternateMobileNo)


            try {
                val newDoa = stringOrEmpty(dOA).split("T").toTypedArray()
                val newDob = stringOrEmpty(dOB).split("T").toTypedArray()
                if (newDob[0].isNotEmpty()) {
                    val dob = CRMAppUtil.getDate(newDob[0], "yyyy-MM-dd")
                    etDob.setText(CRMAppUtil.getDateStr(dob, "dd-MM-yyyy"))
                }
                if (newDoa[0].isNotEmpty()) {
                    val doa = CRMAppUtil.getDate(newDoa[0], "yyyy-MM-dd")
                    etAnniversary.setText(CRMAppUtil.getDateStr(doa, "dd-MM-yyyy"))
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
                        cbPrimaryAddress.isChecked = isPrimary
                        etAdd1.setText(address1)
                        etAdd2.setText(address2)
                        etAdd3.setText(address3)
                        etPincode.setText(pincode)
                        val dataCountry = RowData.getSpinnerRowDataById(
                            countryID.toString().nullSafe("-1"),
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
                        cbOffPrimaryAddress.isChecked = isPrimary
                        etOffAdd1.setText(address1)
                        etOffAdd2.setText(address2)
                        etOffAdd3.setText(address3)
                        etOffPincode.setText(pincode)
                        val dataCountry = RowData.getSpinnerRowDataById(
                            countryID.toString().nullSafe("-1"),
                            countryAdapterOff
                        )
                        dataCountry?.run {
                            binding.spOffCountry.setSelection(position)
                        }
                        val dataState = RowData.getSpinnerRowDataById(
                            stateID.toString().nullSafe("-1"),
                            stateAdapterOff
                        )
                        dataState?.run {
                            binding.spOffState.setSelection(position)
                            selectedCityOffId = cityID.toString().nullSafe("-1")
                        }
                    }
                    else -> {
                    }
                }

            }
        }
    }


    private var leadDetailAddProductAdapter: LeadDetailAddProductAdapter? = null

    private fun List<LeadProductMappingItem>.renderMapping() {
        mapNotNull {
            stage = it.stageID?.toString() ?: emptyString()
            status = it.statusID?.toString() ?: emptyString()
            binding.tvClientCode.text = it.clientCode
            leadDetailAddProductList.add(getLeadDetailAddProductModel(it))
        }
        leadDetailAddProductAdapter =
            LeadDetailAddProductAdapter(
                saathiApi,
                requireContext(),
                leadDetailAddProductList,
                binding.lvAddProduct,
                leadId,
                isLeadEditable,
                statusData,
                statusAdapter,
                stageAdapter,
                userManager.getUser()?.employeeCode,
                status,
                userManager.getUser()?.currentBusinessProfile?.businessUnitId,
                (binding.spPmsProduct.selectedItem as? RowData)?.value ?: emptyString()
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
            stringOrEmpty(item.productSpecialist)
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
            stringOrEmpty(item.prodSpecialist)
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
    private var isIndividualsFlag = false

    override fun isSameAsAboveChecked(): Boolean = binding.cbSameAs.isChecked
    override fun onCountrySelected(): Observable<Int> = binding.spCountry.itemSelections()
    override fun onOfficeCountrySelected(): Observable<Int> = binding.spOffCountry.itemSelections()

    override fun onStateSelected(): Observable<Int> = binding.spState.itemSelections()
    override fun onOfficeStateSelected(): Observable<Int> = binding.spOffState.itemSelections()
    override fun onCitySelected(): Observable<Int> = binding.spCity.itemSelections()

    override fun renderOnCitySelectAndSameAsAboveChecked(position: Int) {
        binding.spOffCity.setSelection(position)
    }

    override fun getSelectedCountry(): RowData = binding.spCountry.selectedItem as RowData
    override fun getSelectedOfficeCountry(): RowData = binding.spOffCountry.selectedItem as RowData
    override fun getSelectedState(): RowData = binding.spState.selectedItem as RowData

    override fun getSelectedOfficeState(): RowData = binding.spOffState.selectedItem as RowData

    override fun renderStateOnCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spState.setSpinnerData(stateAdapter, it)
            binding.spCity.setSpinnerData(cityAdapter, defaultSelectionList)
            if (binding.cbSameAs.isChecked) {
                binding.spOffCountry.setSelection(binding.spCountry.selectedItemPosition)
            }
        }
    }

    override fun renderOfficeStateOnOfficeCountrySelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spOffState.setSpinnerData(stateAdapterOff, it)
            binding.spOffCity.setSpinnerData(cityAdapterOff, defaultSelectionList)
        }
    }

    override fun renderCityOnStateSelection(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure { showOnFailurePopup(it) }
        uiModel.onSuccess {
            binding.spCity.setSpinnerData(cityAdapter, it)
            if (selectedCityId != "-1") {
                val dataCity = RowData.getSpinnerRowDataById(
                    selectedCityId,
                    cityAdapter
                )
                dataCity?.run {
                    binding.spCity.setSelection(position)
                    selectedCityId = "-1"
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
            if (selectedCityOffId != "-1") {
                val dataCity = RowData.getSpinnerRowDataById(
                    selectedCityOffId,
                    cityAdapterOff
                )
                dataCity?.run {
                    binding.spOffCity.setSelection(position)
                    selectedCityOffId = "-1"
                }
            }
        }
    }

    override fun interestedProductSelected(): Observable<SelectInterestedProductEvent> =
        subject.ofType()

    override fun selectedStage(): Observable<SelectStageEvent> = subject.ofType()

    override fun renderStage(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            stageData.clear()
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            stageData.clear()
            stageData.addAll(it)
            stageData.add(0, SELECT_ROW_DATA)
            stageAdapter.setSpinnerData(addProductDialogBinding.spStage, stageData)
        }
    }

    override fun renderStatus(uiModel: UiModel<List<RowData>>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            statusData.clear()
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            statusData.clear()
            statusData.addAll(it)
            stageData.add(0, SELECT_ROW_DATA)
            statusAdapter.setSpinnerData(
                addProductDialogBinding.spStatus,
                statusData
            )
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
        ): LeadDetailFragment {
            return LeadDetailFragment().apply {
                val args = Bundle().apply {
                    putInt(POSITION, position)
                    putString(TYPE, type)
                    putString(MOSLConstants.LEADID, leadID)
                    putString(MOSLConstants.UCID, ucId)
                }
                arguments = args
            }
        }

        const val OUTER = "OuterSectionExpansion"
        const val INNER = "InnerSectionExpansion"

    }

}