package com.mosl.tabapp.leadViewDetail.profiling

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileParameter
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileRequestData
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileSubParameter
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfilingResponseModel
import com.mosl.tabapp.R
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY
import com.mosl.tabapp.constants.MOSLConstants.SOME_SEELECTION_METHOD
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadProfilingBinding
import com.mosl.tabapp.leadViewDetail.profiling.LeadProfileUiEvent.SubmitProfileEvent
import com.mosl.tabapp.model.profile.ProfileModel
import com.mosl.tabapp.model.profile.ProfileOption
import com.mosl.tabapp.model.profile.ProfileQuestion
import com.mosl.tabapp.model.profile.ProfileQuestionGroup
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.Iterator
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.dropLastWhile
import kotlin.collections.emptyList
import kotlin.collections.emptyMap
import kotlin.collections.indices
import kotlin.collections.isNotEmpty
import kotlin.collections.plusAssign
import kotlin.collections.set
import kotlin.collections.toMutableList
import kotlin.collections.toMutableMap
import kotlin.collections.toTypedArray

class LeadProfilingFragment : CommonFragment(), LeadProfilingView {
    private var leadId = "0"
    private var viewPager: ViewPager? = null
    private var profileModel: ProfileModel? = null
    private var formElementList: MutableList<View> = emptyList<View>().toMutableList()

    //This list is added just to have reffrence of each question so if we can findthem in on click event of their siblings child
    private var questionMap: MutableMap<String, View> = emptyMap<String, View>().toMutableMap()
    private lateinit var binding: FragLeadProfilingBinding
    private val subject = PublishSubject.create<LeadProfileUiEvent>()

    @Inject
    internal lateinit var presenter: LeadProfilingPresenter

    override fun getLayout(): Int = layout.frag_lead_profiling

    var profileNewRequestList = ArrayList<ProfileRequestData>()
    var isRequiredListMap = HashMap<Int, String>()
    var objListOriginal :List<LeadProfile> = mutableListOf()
    var strSelectedClientType :String =""
    var isShowSuccessMessage : Boolean = false
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragLeadProfilingBinding.bind(view)
        presenter.start()
        dataActionSubject.onNext(Fetch)
    }

    /**
     * This method will a json string of all option selected on the form
     */

    private fun deletePreviousItem(parameterID : String, subParameterId : String){
        if (profileNewRequestList.isNotEmpty()){

            val iterator = profileNewRequestList.iterator()

            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element.profileParameterID == parameterID ){
                    iterator.remove()
                }
            }

        }
    }

    private val selectedAnswersJson by lazy {
        val profileRequestList = ArrayList<ProfileRequestData>()

        for (entry in formElementList) {
            when (entry) {
                is EditText -> {
                    val mTag = entry.getTag()
                        .toString()
                        .trim { it <= ' ' }
                    try {
                        val tagArray = mTag.split("_".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        val textInEditText = entry.text.toString()
                            .trim { it <= ' ' }
                        if (textInEditText.isNotEmpty()) {
                            profileRequestList += ProfileRequestData(
                                tagArray[0], tagArray[1], leadId, textInEditText
                            )
                        }
                    } catch (e: Exception) {
                        Timber.d(e)
                    }
                }
                is AppCompatTextView -> {
                    val mTag = entry.getTag()
                        .toString()
                        .trim { it <= ' ' }
                    try {
                        val tagArray = mTag.split("_".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        val textInTextView = entry.text.toString()
                            .trim { it <= ' ' }
                        if (textInTextView.isNotEmpty()) {
                            profileRequestList += ProfileRequestData(
                                tagArray[0], tagArray[1], leadId, textInTextView
                            )
                        }
                    } catch (e: Exception) {
                        Timber.d(e)
                    }
                }
                is RadioGroup -> for (i in 0 until entry.childCount) {
                    val rbTemp = entry.getChildAt(i) as RadioButton
                    val profileOption = rbTemp.tag as ProfileOption
                    if (rbTemp.isChecked) {
                        var valueString = ""
                        if (!CRMAppUtil.isBlank(profileOption.extraInfo) && profileOption.extraInfo.equals(
                                "Y", ignoreCase = true
                            )
                        ) {
                            valueString = getExtraView(
                                profileOption
                            )
                        }

                        profileRequestList.add(
                            ProfileRequestData(
                                profileOption.profileParameterID!!.toString(),
                                profileOption.profileSubParameterID!!.toString(), leadId,
                                valueString
                            )
                        )
                    }
                }
                is CheckBox -> {
                    val profileOption = entry.getTag() as ProfileOption
                    if (entry.isChecked) {
                        var valueString = ""
                        if (!CRMAppUtil.isBlank(profileOption.extraInfo) && profileOption.extraInfo.equals(
                                "Y", ignoreCase = true
                            )
                        ) {
                            valueString = getExtraView(profileOption)
                        }
                        profileRequestList.add(
                            ProfileRequestData(
                                profileOption.profileParameterID!!.toString(),
                                profileOption.profileSubParameterID!!.toString(), leadId,
                                valueString
                            )
                        )
                    }
                }
                is Spinner -> {
                    val opTion = entry.selectedItem as ProfileOption
                    var valueString = ""

                    if (!CRMAppUtil.isBlank(opTion.extraInfo) && opTion.extraInfo
                            .equals("Y", ignoreCase = true)
                    ) {
                        valueString = getExtraView(opTion)
                    }

                    profileRequestList.add(
                        ProfileRequestData(
                            opTion.profileParameterID!!.toString(),
                            opTion.profileSubParameterID!!.toString(),
                            leadId,
                            valueString
                        )
                    )
                }
            }
        }

        profileRequestList
    }

    fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
    }

    override fun getProfileRequestDataList(): List<ProfileRequestData> = profileNewRequestList
    override fun onSubmitProfileEventFired(): Observable<SubmitProfileEvent> = subject.ofType()
    override fun onSubmitButtonClicked(): Observable<Unit> = binding.btnSubmit.clicks()
    override fun renderSubmitButtonClicked() {
        val request = profileNewRequestList

        when {
            request.isNotEmpty() ->{
               /* var tempMap = HashMap<Int, String>()
                tempMap.putAll(isRequiredListMap)
                for ((key, value) in isRequiredListMap) {
                    println("Key: $key, Value: $value")
                    for(answer in profileNewRequestList) {
                        if (Integer.parseInt(answer.profileParameterID) == key) {
                            tempMap.remove(key)
                        }
                    }
                }

              var list =  tempMap.values.toList()
              if(list.distinct().isNotEmpty()){
                    val result = list.distinct().joinToString(separator = "\n") + "\nPlease fill mandatory questions in above groups."
                    alertDialog(
                        requireContext(), 0,
                        resources.getString(R.string.motilal_oswal), result,
                        resources.getString(android.R.string.ok), true, null
                    )
                }else{
                    subject.onNext(SubmitProfileEvent)
                }*/

                //-------------------
                var arrTempGroupName = ArrayList<String>()

                for (group in profileModel!!.profileQuestionGroup) {
                     for (question in group.profileQuestionList) {
                         if (question.isRequired == 1) {
                             var isSelected = false
                             for(answer in profileNewRequestList) {
                               if(Integer.parseInt(answer.profileParameterID) == question.profileParameterID){
                                   isSelected = true
                                   break
                               }
                             }
                             if(!isSelected){
                                 val str  = getQuestionValidationText(question.questionText, question.controlType)
                                 arrTempGroupName.add(str)
                             }
                        }
                    }

                }

                if(arrTempGroupName.isNotEmpty()){
                    isShowSuccessMessage = false
                    subject.onNext(SubmitProfileEvent)
                    val result = arrTempGroupName.joinToString(separator = "\n")
                    alertDialog(
                        requireContext(), 0,
                        resources.getString(R.string.motilal_oswal), result,
                        resources.getString(android.R.string.ok), true, null
                    )
                }else{
                    isShowSuccessMessage = true
                    subject.onNext(SubmitProfileEvent)
                }
            }
            else -> {
                alertDialog(
                    requireContext(), 0,
                    resources.getString(R.string.motilal_oswal), SOME_SEELECTION_METHOD,
                    resources.getString(android.R.string.ok), true, null
                )
            }
        }
    }

    private fun getQuestionValidationText(questionText: String?, controlType: Int): String{
        when (controlType) {
            0 -> {
            }
            TEXT_BOX, NUMBER_TEXT_BOX -> {
                return "$questionText is required."
            }
            RADIO_BUTTON, CHECK_BOX, DROP_DOWN, DATE_PICKER-> {
                return "$questionText must be selected."
            }
        }
        return ""
    }
    override fun onCancelButtonClicked(): Observable<Unit> = binding.btnCancel.clicks()
    override fun renderCancelButtonClicked() {
        viewPager?.setCurrentItem(MOSLConstants.LEAD_DETAIL, false)
    }

    private fun fillProfileModal(objList : List<LeadProfile>) {

      var leadProfileList  :  MutableList<LeadProfile> = objList as MutableList<LeadProfile>

        /*** for Client profiling changes start here****/

       var objProfileParameter: ProfileParameter?= leadProfileList.find {  it.profileParameterGroupID ==117 }?.profileParameterList?.find {
           it.profileParameterID==168
       };
        leadProfileList.find {  it.profileParameterGroupID ==117 }?.profileParameterList?.removeAt(1);
        leadProfileList.find {  it.profileParameterGroupID ==117 }?.profileParameterList?.add(0,
            objProfileParameter!!
        )
        var isHidden = true
        var selectedClientType = ""
        objProfileParameter?.profileSubParameterList?.forEach {
            if (it.isSelected == true) {
                selectedClientType = it.profileSubParameterID.toString()
                isHidden = false
            }
        }
        if(selectedClientType.equals("524"))
        {
            leadProfileList.find { it.profileParameterGroupID ==117 }?.profileParameterList?.forEach {
                if(it.profileParameterID!=169)
                {
                    it.isRequired=null
                }
            }
        }
        else if(selectedClientType.equals("523"))
        {
            leadProfileList.find { it.profileParameterGroupID ==117 }?.profileParameterList?.forEach {
                it.isRequired=1
            }
        }
         leadProfileList.find {  it.profileParameterGroupID ==117 }?.profileParameterList?.forEach {
            if(it.profileParameterID!=168)
             {
                 if(isHidden){
                     it.isDefaultHidden="Y"
                 }else{
                     it.isDefaultHidden="N"
                 }

             }
         }


        /*** End here****/
        /*** for Family Members changes start here****/

        var objMaritaleStatusParameter: ProfileParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find {
            it.profileParameterID==261
        };


        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.removeAt(4);
        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.add(0,
            objMaritaleStatusParameter!!
        )
        var objAnniversaryParameter: ProfileParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find {
            it.profileParameterID==205
        };
        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.removeAt(4);
        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.add(1,
            objAnniversaryParameter!!
        )

        var objSpouseParameter: ProfileParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find {
            it.profileParameterID==262
        };

        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.removeAt(5);
        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.add(2,
            objSpouseParameter!!
        )

        var objSpousebdayParameter: ProfileParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find {
            it.profileParameterID==263
        };

        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.removeAt(6);
        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.add(3,
            objSpousebdayParameter!!
        )

        var objkidsParameter: ProfileParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find {
            it.profileParameterID==264
        };

        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.removeAt(7);
        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.add(4,
            objkidsParameter!!
        )

        var objkidsNameParameter: ProfileParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find {
            it.profileParameterID==265
        };

        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.removeAt(8);
        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.add(5,
            objkidsNameParameter!!
        )


        leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.forEach {
            if(it.profileParameterID==205 || it.profileParameterID==262 || it.profileParameterID==263 || it.profileParameterID==264 || it.profileParameterID==265 )
            {
                it.isDefaultHidden="Y"
                it.isRequired = null
            }
        }

        var objOptionsYes: ProfileSubParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find { it.profileParameterID == 261 }?.profileSubParameterList?.find { it.profileSubParameterID == 814 }
        if(objOptionsYes?.isSelected == true){
            leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.forEach {
                if(it.profileParameterID==205 || it.profileParameterID==262 || it.profileParameterID==263 || it.profileParameterID==264 || it.profileParameterID==265 )
                {
                    it.isDefaultHidden="N"
                }

                if(it.profileParameterID==205 || it.profileParameterID==262 || it.profileParameterID==263 || it.profileParameterID==261 )
                {
                    it.isRequired=1
                }
            }
        }
        var objOptionsNo: ProfileSubParameter?= leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.find { it.profileParameterID == 261 }?.profileSubParameterList?.find { it.profileSubParameterID == 815 }
        if(objOptionsNo?.isSelected == true){
            leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.forEach {
                if(it.profileParameterID==205 || it.profileParameterID==262 || it.profileParameterID==263 || it.profileParameterID==264 || it.profileParameterID==265 )
                {
                    it.isDefaultHidden="Y"
                    it.isRequired=null
                }
            }
        }

        /*** End here****/
        objListOriginal= leadProfileList

        profileModel = ProfileModel()
        val profileQuestionGroupList = ArrayList<ProfileQuestionGroup>()
        for (leadProfile in leadProfileList) {
            val groupName = stringOrEmpty(leadProfile.groupName)
            val profileId = stringOrEmpty(leadProfile.profileParameterId)
            val tempProfileQuestionGroup = ProfileQuestionGroup().apply {
                this.groupName = groupName
                this.profileParameterID = profileId
                this.inApproval = leadProfile.inApproval
                this.profileParameterGroupID = leadProfile.profileParameterGroupID
                this.businessUnitId = leadProfile.businessUnitId
                this.isAdminMode = leadProfile.isAdminMode
            }

            val mProfileQuestionList = ArrayList<ProfileQuestion>()
            leadProfile.profileParameterList?.let {
                for (parameter in it) {
                    val tmpProfileQuestion = ProfileQuestion()

                    tmpProfileQuestion.controlType = parameter.controlType
                    tmpProfileQuestion.inApproval = parameter.inApproval
                    tmpProfileQuestion.profileParameterGroupID = parameter.profileParameterGroupID
                    tmpProfileQuestion.questionType = parameter.questionType!!
                    tmpProfileQuestion.questionType = parameter.selectedProfileSubParameterID!!
                    tmpProfileQuestion.isAdminMode = parameter.isAdminMode!!
                    tmpProfileQuestion.status = parameter.status
                    tmpProfileQuestion.isRequired = parameter.isRequired

                    val questionText = stringOrEmpty(parameter.questionText)
                    tmpProfileQuestion.questionText = questionText

                    val yNStatus = when (parameter.isDefaultHidden) {
                        null, "" -> "N"
                        else -> parameter.isDefaultHidden
                    }
                    tmpProfileQuestion.isDefaultHidden = yNStatus

                    tmpProfileQuestion.profileParameterID = parameter.profileParameterID!!

                    val mProfileOptionList = ArrayList<ProfileOption>()
                    parameter.profileSubParameterList?.let { list ->
                        for (subParameter in list) {
                            val tempProfileOption = ProfileOption()

                            tempProfileOption.profileParameterID =
                                tmpProfileQuestion.profileParameterID

                            val profileSubParameterListInApproval = subParameter.inApproval!!
                            tempProfileOption.inApproval = profileSubParameterListInApproval

                            val value = stringOrEmpty(subParameter.value)
                            tempProfileOption.value = value
                            tempProfileOption.isSelected = subParameter.isSelected!!

                            val profileSubParameterID = subParameter.profileSubParameterID!!
                            tempProfileOption.profileSubParameterID = profileSubParameterID
                            tempProfileOption.hideDepedentQuestion =
                                stringOrEmpty(subParameter.hideDependentQuestion)
                            val noStr = stringOrEmpty(subParameter.isHide)
                            tempProfileOption.setIsHide(noStr)

                            tempProfileOption.questionSubText =
                                stringOrEmpty(subParameter.questionSubText)
                            tempProfileOption.isAdminMode = subParameter.isAdminMode!!
                            tempProfileOption.extraInfo = stringOrEmpty(subParameter.extraInfo)

                            subParameter.extraInfoRowCount?.let { rowCount ->
                                tempProfileOption.extraInfoRowCount = rowCount
                            }

                            tempProfileOption.extraInfoFieldName =
                                stringOrEmpty(subParameter.extraInfoFieldName)

                            mProfileOptionList.add(tempProfileOption)
                        }
                    }
                    tmpProfileQuestion.profileOptionList = mProfileOptionList

                    mProfileQuestionList.add(tmpProfileQuestion)
                }
            }


            tempProfileQuestionGroup.profileQuestionList = mProfileQuestionList
            profileQuestionGroupList.add(tempProfileQuestionGroup)
        }

        profileModel?.profileQuestionGroup = profileQuestionGroupList
    }

    private fun setExtraView(
        mOptions: ProfileOption,
        extraFieldLayout: ViewGroup
    ) {
        try {
            if (mOptions.extraInfo.equals("N", ignoreCase = true)) {
                //No need to go further
                return
            }
            var extraViewList: MutableList<List<View>>? = null
            extraFieldLayout.visibility = View.VISIBLE
            val rowCount = mOptions.extraInfoRowCount
            val fieldNameMix = mOptions.extraInfoFieldName
            val fieldNameArray = fieldNameMix.split("[$ANS_GROUP_SEPERATER]".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            extraFieldLayout.visibility = View.VISIBLE
            // if (rowCount > 0) {
            extraViewList = ArrayList()
            //}

            for (ctr in 0 until rowCount) {
                val innerList = ArrayList<View>()
                val llSegment = LinearLayout(activity)
                llSegment.orientation = LinearLayout.VERTICAL
                llSegment.setBackgroundResource(R.color.dashboard_background)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    DrawerLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(
                    resources.getDimension(R.dimen._3sdp).toInt(),
                    resources.getDimension(R.dimen._3sdp).toInt(),
                    resources.getDimension(R.dimen._3sdp).toInt(),
                    resources.getDimension(R.dimen._3sdp).toInt()
                )
                llSegment.layoutParams = params
                llSegment.setPadding(
                    resources.getDimension(R.dimen._3sdp).toInt(),
                    resources.getDimension(R.dimen._3sdp).toInt(),
                    resources.getDimension(R.dimen._3sdp).toInt(),
                    resources.getDimension(R.dimen._3sdp).toInt()
                )
                extraFieldLayout.addView(llSegment)
                for (label1 in fieldNameArray) {

                    val extraTextView1 = TextView(activity)
                    extraTextView1.text = label1
                    val extraEditText1 = EditText(activity)
                    extraEditText1.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    extraEditText1.setBackgroundResource(R.drawable.textview_border)
                    extraEditText1.filters = arrayOf(CRMAppUtil.filterOnlyAlphabetAndNumbers)
                    llSegment.addView(extraTextView1)
                    llSegment.addView(extraEditText1)

                    innerList.add(extraEditText1)
                }
                extraViewList.add(innerList)
            }

            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            mOptions.extraViewList = extraViewList

            val data = mOptions.value.split("[$ANS_GROUP_SEPERATER]".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (data.size == extraViewList.size) {
                //if you have proper data

                for (i in extraViewList.indices) {// for for list of group
                    val dataAns = data[i].split("[$ANS_SEPERATER]".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    if (dataAns.size == extraViewList[i].size) {
                        val mAnsEdit = extraViewList[i]
                        for (j in dataAns.indices) {
                            val ed = mAnsEdit[j] as EditText
                            ed.setText(dataAns[j].trim { it <= ' ' })
                        }
                    }
                }
            }

            //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    /**
     * @param profileOption its object in views tag
     */
    private fun getExtraView(profileOption: ProfileOption): String {
        var valueString = StringBuilder()
        val extraViewList = profileOption.extraViewList
        return extraViewList?.let {
            try {
                for (vList in extraViewList) {
                    for (mView in vList) {
                        val mData = (mView as EditText).text.toString()
                            .trim { it <= ' ' }
                        if (mData.trim { it <= ' ' }.isEmpty()) {
                            valueString.append(" $ANS_SEPERATER")
                        } else {
                            valueString.append(mData)
                                .append(ANS_SEPERATER)
                        }
                    }
                    valueString = StringBuilder(
                        valueString.substring(0, valueString.length - ANS_SEPERATER.length)
                    )
                    valueString.append(ANS_GROUP_SEPERATER)
                }
                valueString.substring(
                    0,
                    valueString.length - ANS_GROUP_SEPERATER.length
                )
            } catch (eie: StringIndexOutOfBoundsException) {
                ""
            }

        } ?: ""

    }


    private fun updateRender(objList : List<LeadProfile>)
    {

        var leadProfileList  :  MutableList<LeadProfile> = objList as MutableList<LeadProfile>


        if(strSelectedClientType.equals("523") || strSelectedClientType.equals("524"))
        {
            leadProfileList.find {  it.profileParameterGroupID ==117 }?.profileParameterList?.forEach {
                if(it.profileParameterID!=168)
                {
                    it.isDefaultHidden="N"
                }
            }
        }


        if(strSelectedClientType.equals("524"))
        {
            leadProfileList.find { it.profileParameterGroupID ==117 }?.profileParameterList?.forEach {
                if(it.profileParameterID!=169)
                {
                        it.isRequired=null
                }
                if(it.profileParameterID == 168) {
                    it.profileSubParameterList?.forEach {
                        it.isSelected = it.profileSubParameterID == 524

                    }
                }
            }


        }
        else if(strSelectedClientType.equals("523"))
        {
            leadProfileList.find { it.profileParameterGroupID ==117 }?.profileParameterList?.forEach {
                it.isRequired=1
                if(it.profileParameterID == 168) {
                    it.profileSubParameterList?.forEach {
                        it.isSelected = it.profileSubParameterID == 523

                    }
                }
            }
        }
        else if(strSelectedClientType.equals("814"))
        {

            leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.forEach {
                if(it.profileParameterID==205 || it.profileParameterID==262 || it.profileParameterID==263 || it.profileParameterID==264 || it.profileParameterID==265 )
                {
                    it.isDefaultHidden="N"
                }

                if(it.profileParameterID==205 || it.profileParameterID==262 || it.profileParameterID==263 || it.profileParameterID==261 )
                {
                    it.isRequired=1
                }
                if(it.profileParameterID == 261) {
                    it.profileSubParameterList?.forEach {
                        it.isSelected = it.profileSubParameterID == 814

                    }
                }
            }
        }
        else if(strSelectedClientType.equals("815"))
        {
            leadProfileList.find {  it.profileParameterGroupID ==121 }?.profileParameterList?.forEach {
                if(it.profileParameterID==205 || it.profileParameterID==262 || it.profileParameterID==263 || it.profileParameterID==264 || it.profileParameterID==265 )
                {
                    it.isDefaultHidden="Y"
                    it.isRequired=null
                }

                if(it.profileParameterID == 261) {
                    it.profileSubParameterList?.forEach {
                        it.isSelected = it.profileSubParameterID == 815

                    }
                }

            }

        }


        objListOriginal=leadProfileList


        profileModel = ProfileModel()
        val profileQuestionGroupList = ArrayList<ProfileQuestionGroup>()
        for (leadProfile in leadProfileList) {
            val groupName = stringOrEmpty(leadProfile.groupName)
            val profileId = stringOrEmpty(leadProfile.profileParameterId)
            val tempProfileQuestionGroup = ProfileQuestionGroup().apply {
                this.groupName = groupName
                this.profileParameterID = profileId
                this.inApproval = leadProfile.inApproval
                this.profileParameterGroupID = leadProfile.profileParameterGroupID
                this.businessUnitId = leadProfile.businessUnitId
                this.isAdminMode = leadProfile.isAdminMode
            }

            val mProfileQuestionList = ArrayList<ProfileQuestion>()
            leadProfile.profileParameterList?.let {
                for (parameter in it) {
                    val tmpProfileQuestion = ProfileQuestion()

                    tmpProfileQuestion.controlType = parameter.controlType
                    tmpProfileQuestion.inApproval = parameter.inApproval
                    tmpProfileQuestion.profileParameterGroupID = parameter.profileParameterGroupID
                    tmpProfileQuestion.questionType = parameter.questionType!!
                    tmpProfileQuestion.questionType = parameter.selectedProfileSubParameterID!!
                    tmpProfileQuestion.isAdminMode = parameter.isAdminMode!!
                    tmpProfileQuestion.status = parameter.status
                    tmpProfileQuestion.isRequired = parameter.isRequired

                    val questionText = stringOrEmpty(parameter.questionText)
                    tmpProfileQuestion.questionText = questionText

                    val yNStatus = when (parameter.isDefaultHidden) {
                        null, "" -> "N"
                        else -> parameter.isDefaultHidden
                    }
                    tmpProfileQuestion.isDefaultHidden = yNStatus

                    tmpProfileQuestion.profileParameterID = parameter.profileParameterID!!

                    val mProfileOptionList = ArrayList<ProfileOption>()
                    parameter.profileSubParameterList?.let { list ->
                        for (subParameter in list) {
                            val tempProfileOption = ProfileOption()

                            tempProfileOption.profileParameterID =
                                tmpProfileQuestion.profileParameterID

                            val profileSubParameterListInApproval = subParameter.inApproval!!
                            tempProfileOption.inApproval = profileSubParameterListInApproval

                            val value = stringOrEmpty(subParameter.value)
                            tempProfileOption.value = value
                            tempProfileOption.isSelected = subParameter.isSelected!!

                            val profileSubParameterID = subParameter.profileSubParameterID!!
                            tempProfileOption.profileSubParameterID = profileSubParameterID
                            tempProfileOption.hideDepedentQuestion =
                                stringOrEmpty(subParameter.hideDependentQuestion)
                            val noStr = stringOrEmpty(subParameter.isHide)
                            tempProfileOption.setIsHide(noStr)

                            tempProfileOption.questionSubText =
                                stringOrEmpty(subParameter.questionSubText)
                            tempProfileOption.isAdminMode = subParameter.isAdminMode!!
                            tempProfileOption.extraInfo = stringOrEmpty(subParameter.extraInfo)

                            subParameter.extraInfoRowCount?.let { rowCount ->
                                tempProfileOption.extraInfoRowCount = rowCount
                            }

                            tempProfileOption.extraInfoFieldName =
                                stringOrEmpty(subParameter.extraInfoFieldName)

                            mProfileOptionList.add(tempProfileOption)
                        }
                    }
                    tmpProfileQuestion.profileOptionList = mProfileOptionList

                    mProfileQuestionList.add(tmpProfileQuestion)
                }
            }


            tempProfileQuestionGroup.profileQuestionList = mProfileQuestionList
            profileQuestionGroupList.add(tempProfileQuestionGroup)
        }

        profileModel?.profileQuestionGroup = profileQuestionGroupList
        renderForm()

    }

    /**
     * this method will render/design the profile page with question filled in model
     */

    private fun renderForm() {
        if (profileModel == null) {
            return
        }
        formElementList = ArrayList()
        questionMap = HashMap()
        val previousActionMap = HashMap<String, String>()

        binding.mainProfileLayouttarget.removeAllViews()
        // this is just a flag to Set Open first group
        var mCoutFlagFirstGroup = false
        for (group in profileModel!!.profileQuestionGroup) {
            val groupName = group.groupName
            @SuppressLint("InflateParams") val groupLayout = requireActivity()
                .layoutInflater
                .inflate(R.layout.include_section, null) as ViewGroup

            val tvGroupName = groupLayout.findViewById<View>(R.id.tvSectionName) as TextView
            tvGroupName.text = groupName

            //question section start
            val llBasicProfileQuestionLayoutInner = groupLayout.findViewById<View>(
                R.id.llSectionContent
            ) as LinearLayout

            tvGroupName.setOnClickListener {
                toggleSectionExpansion(
                    llBasicProfileQuestionLayoutInner, tvGroupName
                )
            }

            //Question
            // group
            //var questionNo = 1
            for (question in group.profileQuestionList) {
                val padding = resources.getDimension(R.dimen._15sdp)
                    .toInt()
                val tvQuestion = TextView(activity).apply {

                    if(question.isRequired == 1){
                        val str = question.questionText + " *"
                        text = str
                        isRequiredListMap.put(question.profileParameterID,group.groupName )
                    }else{
                        text = question.questionText
                    }

                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.light_black))
                    setPadding(padding, padding, padding, padding)
                    //as per QA requirement hide the question number to match the view with web
                    //text = String.format("Q %d.%s", questionNo, question.questionText)
                    // questionNo++
                    textSize = resources.getDimension(R.dimen.txt_size_profile)
                    setBackgroundResource(R.drawable.profiling_question_background_text)
                }

                llBasicProfileQuestionLayoutInner.addView(tvQuestion)
                questionMap[question.profileParameterID!!.toString() + Head] = tvQuestion
                if (question.isDefaultHidden.equals("Y", ignoreCase = true)) {
                    tvQuestion.visibility = View.GONE
                }
                if (question.status == 0) {
                    tvQuestion.visibility = View.GONE
                }
                if (!question.profileOptionList.isEmpty() && question.status != 0) {// && question.getProfileOptionList().size()<1
                    when (question.controlType) {
                        0 -> {
                        }
                        TEXT_BOX -> {

                            val llBasicProfileQuestionLayoutGroup = LinearLayout(activity).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                orientation = LinearLayout.VERTICAL
                                setPadding(padding, padding, padding, padding)
                            }


                            for (mOptions in question.profileOptionList) {
                                val tvHead = TextView(activity).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.light_black
                                        )
                                    )
                                    setPadding(padding, 4, padding, 4)
                                    text = mOptions.questionSubText
                                    textSize = resources.getDimension(R.dimen.txt_size_profile)
                                }


                                val mEditTextOption = EditText(activity).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    setBackgroundResource(R.drawable.textview_border)
                                    tag = (question.profileParameterID.toString()
                                            + "_"
                                            + mOptions.profileSubParameterID
                                            + "_E")
                                    animate()
                                    filters = arrayOf(CRMAppUtil.filterOnlyAlphabetAndNumbers)
                                    setText(mOptions.value)
                                    hint = mOptions.questionSubText
                                }

                                for( objprofileNewRequestList in profileNewRequestList){
                                    if((mOptions.profileParameterID.toString().equals(objprofileNewRequestList.profileParameterID))&& (mOptions.profileSubParameterID.toString().equals(objprofileNewRequestList.profileSubParameterID)))
                                    {
                                        mEditTextOption.setText("${objprofileNewRequestList.value}")
                                        break
                                    }
                                }

                                tvHead.text = mOptions.questionSubText
                                llBasicProfileQuestionLayoutGroup.addView(tvHead)
                                llBasicProfileQuestionLayoutGroup.addView(mEditTextOption)
                                formElementList.add(mEditTextOption)
                                mEditTextOption.doAfterTextChanged {
                                    if (mEditTextOption.text.toString().isNotEmpty()) {
                                        deletePreviousItem(question.profileParameterID.toString(), mOptions.profileSubParameterID.toString())
                                        val text = mEditTextOption.text.toString()
                                            .trim { it <= ' ' }
                                        profileNewRequestList += ProfileRequestData(
                                            question.profileParameterID.toString(), mOptions.profileSubParameterID.toString(), leadId, text
                                        )
                                    }else{
                                        deletePreviousItem(question.profileParameterID.toString(), mOptions.profileSubParameterID.toString())
                                    }
                                }
                                if (mEditTextOption.text.toString().isNotEmpty()) {
                                    deletePreviousItem(question.profileParameterID.toString(), mOptions.profileSubParameterID.toString())
                                    val text = mEditTextOption.text.toString()
                                        .trim { it <= ' ' }
                                    profileNewRequestList += ProfileRequestData(
                                        question.profileParameterID.toString(), mOptions.profileSubParameterID.toString(), leadId, text
                                    )
                                }else{
                                    deletePreviousItem(question.profileParameterID.toString(), mOptions.profileSubParameterID.toString())
                                }
                            }
                            llBasicProfileQuestionLayoutGroup.setBackgroundResource(
                                R.drawable.profiling_question_background_group
                            )
                            llBasicProfileQuestionLayoutInner.addView(
                                llBasicProfileQuestionLayoutGroup
                            )
                            questionMap[question.profileParameterID!!.toString()] =
                                llBasicProfileQuestionLayoutGroup
                            if (question.isDefaultHidden.equals("Y", ignoreCase = true)) {
                                llBasicProfileQuestionLayoutGroup.visibility = View.GONE
                            }
                        }
                        NUMBER_TEXT_BOX -> {

                            val llBasicProfileQuestionLayoutGroup = LinearLayout(activity).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                orientation = LinearLayout.VERTICAL
                                setPadding(padding, padding, padding, padding)
                            }


                            for (mOptions in question.profileOptionList) {
                                val tvHead = TextView(activity).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.light_black
                                        )
                                    )
                                    setPadding(padding, 4, padding, 4)
                                    text = mOptions.questionSubText
                                    textSize = resources.getDimension(R.dimen.txt_size_profile)
                                }

                                val mEditTextOption = EditText(activity).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    inputType =
                                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED

                                    setBackgroundResource(R.drawable.textview_border)
                                    tag = (question.profileParameterID.toString()
                                            + "_"
                                            + mOptions.profileSubParameterID
                                            + "_E")
                                    animate()
                                    filters = arrayOf(CRMAppUtil.filterOnlyAlphabetAndNumbers)
                                    setText(mOptions.value)
                                    hint = mOptions.questionSubText
                                }

                                for( objprofileNewRequestList in profileNewRequestList){
                                    if((mOptions.profileParameterID.toString().equals(objprofileNewRequestList.profileParameterID))&& (mOptions.profileSubParameterID.toString().equals(objprofileNewRequestList.profileSubParameterID)))
                                    {
                                        mEditTextOption.setText("${objprofileNewRequestList.value}")
                                        break
                                    }
                                }
                                tvHead.text = mOptions.questionSubText
                                llBasicProfileQuestionLayoutGroup.addView(tvHead)
                                llBasicProfileQuestionLayoutGroup.addView(mEditTextOption)
                                formElementList.add(mEditTextOption)
                                mEditTextOption.doAfterTextChanged {
                                    if (mEditTextOption.text.toString().isNotEmpty()) {
                                        deletePreviousItem(question.profileParameterID.toString(), mOptions.profileSubParameterID.toString())
                                        val text = mEditTextOption.text.toString()
                                            .trim { it <= ' ' }
                                        profileNewRequestList += ProfileRequestData(
                                            question.profileParameterID.toString(), mOptions.profileSubParameterID.toString(), leadId, text
                                        )
                                    }
                                }
                                if (mEditTextOption.text.toString().isNotEmpty()) {
                                    deletePreviousItem(question.profileParameterID.toString(), mOptions.profileSubParameterID.toString())
                                    val text = mEditTextOption.text.toString()
                                        .trim { it <= ' ' }
                                    profileNewRequestList += ProfileRequestData(
                                        question.profileParameterID.toString(), mOptions.profileSubParameterID.toString(), leadId,text
                                    )
                                }
                            }
                            llBasicProfileQuestionLayoutGroup.setBackgroundResource(
                                R.drawable.profiling_question_background_group
                            )
                            llBasicProfileQuestionLayoutInner.addView(
                                llBasicProfileQuestionLayoutGroup
                            )
                            questionMap[question.profileParameterID!!.toString()] =
                                llBasicProfileQuestionLayoutGroup
                        }
                        RADIO_BUTTON -> {
                            val rbGroup = RadioGroup(activity).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                setPadding(padding, padding, padding, padding)
                                setBackgroundResource(
                                    R.drawable.profiling_question_background_group
                                )
                                orientation = RadioGroup.VERTICAL
                            }


                            llBasicProfileQuestionLayoutInner.addView(rbGroup)
                            var rbOptionSelected: RadioButton? = null
                            var previousAction = ProfileOption()
                            val extraFieldLayout = LinearLayout(activity).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                orientation = LinearLayout.VERTICAL
                                setPadding(padding, padding, padding, padding)
                                visibility = View.GONE
                            }

                            //Adding linarlayout for info for extra end
                            llBasicProfileQuestionLayoutInner.addView(extraFieldLayout)
                            for (mOptions in question.profileOptionList) {
                                var rbOption :RadioButton = RadioButton(activity).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    val mPadding = resources.getDimension(R.dimen._8sdp)
                                        .toInt()
                                    setPadding(padding, mPadding, padding, mPadding)
                                    textSize = resources.getDimension(R.dimen.txt_size_profile)
                                    text = mOptions.questionSubText
                                    tag = mOptions

                                }


                                rbOption.setOnClickListener {
                                    extraFieldLayout.removeAllViews()
                                    extraFieldLayout.removeAllViewsInLayout()


                                    when {
                                        !CRMAppUtil.isBlank(mOptions.extraInfo) && mOptions.extraInfo.equals(
                                            "Y", ignoreCase = true
                                        ) -> setExtraView(mOptions, extraFieldLayout)
                                        else -> {
                                            extraFieldLayout.removeAllViews()
                                            extraFieldLayout.visibility = View.GONE
                                        }
                                    }
                                    if (!CRMAppUtil.isBlank(mOptions.hideDepedentQuestion)) {
                                        if (questionMap.containsKey(
                                                mOptions.hideDepedentQuestion
                                            )
                                        ) {
                                            //questionMap
                                            val hideView =
                                                questionMap[mOptions.hideDepedentQuestion]
                                            val hideViewHead =
                                                questionMap[mOptions.hideDepedentQuestion + Head]
                                            if (mOptions.isHide.equals("N", ignoreCase = true)) {
                                                hideView?.visibility = View.VISIBLE
                                                hideViewHead?.visibility = View.VISIBLE
                                            } else if (mOptions.isHide
                                                    .equals("Y", ignoreCase = true)
                                            ) {
                                                if (hideView is RadioGroup) {
                                                    val grp = hideView as RadioGroup?
                                                    val cnt = grp!!.childCount
                                                    for (i in 0 until cnt) {
                                                        val child = grp.getChildAt(i)
                                                        if (child is RadioButton) {
                                                            child.isChecked = false
                                                        }
                                                    }
                                                }
                                                hideView?.visibility = View.GONE
                                                hideViewHead?.visibility = View.GONE
                                            }
                                        }
                                    }
                                    var valueString = ""
                                    if (rbOption.isChecked) {

                                        if (!CRMAppUtil.isBlank(mOptions.extraInfo) && mOptions.extraInfo.equals(
                                                "Y", ignoreCase = true
                                            )
                                        ) {
                                            valueString = getExtraView(
                                                mOptions
                                            )
                                        }
                                        deletePreviousItem( mOptions.profileParameterID!!.toString(),
                                            mOptions.profileSubParameterID!!.toString())
                                        profileNewRequestList.add(
                                            ProfileRequestData(
                                                mOptions.profileParameterID!!.toString(),
                                                mOptions.profileSubParameterID!!.toString(), leadId,
                                                valueString
                                            )
                                        )
                                    }
                                    try{
                                        if(question.profileParameterID==168 || question.profileParameterID==261)
                                        {
                                            strSelectedClientType= mOptions.profileSubParameterID!!.toString()
                                            requireActivity().runOnUiThread {
                                                    updateRender(objListOriginal)
                                            }


                                        }
                                    }
                                    catch (ex: Exception){

                                    }
                                }

                                if (mOptions.isSelected) {
                                    rbOptionSelected = rbOption
                                    previousAction = mOptions
                                    setExtraView(mOptions, extraFieldLayout)

                                    val valueString =  getExtraView( mOptions)
                                    profileNewRequestList.add(
                                        ProfileRequestData(
                                            question.profileParameterID!!.toString(),
                                            mOptions.profileSubParameterID!!.toString(), leadId,
                                            valueString
                                        )
                                    )
                                }

                                rbGroup.addView(rbOption)
                            }//end of for loop for option printing

                            if (rbOptionSelected != null) {
                                rbGroup.check(rbOptionSelected.id)
                               // rbOptionSelected.isChecked = false
                                previousActionMap[previousAction.hideDepedentQuestion] =
                                    previousAction.isHide
                            }

                            //Adding to form List
                            formElementList.add(rbGroup)
                            questionMap[question.profileParameterID!!.toString()] = rbGroup
                            if (question.isDefaultHidden.equals("Y", ignoreCase = true)) {
                                rbGroup.visibility = View.GONE
                            }
                        }
                        CHECK_BOX -> {

                            for (mOptions in question.profileOptionList) {
                                val cbOption = CheckBox(activity).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )

                                    val mPadding = resources.getDimension(R.dimen._8sdp)
                                        .toInt()
                                    setPadding(mPadding, mPadding, mPadding, mPadding)
                                    text = mOptions.questionSubText
                                    textSize = resources.getDimension(R.dimen.txt_size_profile)
                                    tag = mOptions
                                    isChecked = mOptions.isSelected
                                }

                                formElementList.add(cbOption)
                                val extraFieldLayout = LinearLayout(activity).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    orientation = LinearLayout.VERTICAL
                                    setPadding(padding, padding, padding, padding)
                                    visibility = View.GONE
                                }

                                for( objprofileNewRequestList in profileNewRequestList){
                                    if((mOptions.profileParameterID.toString().equals(objprofileNewRequestList.profileParameterID))&& (mOptions.profileSubParameterID.toString().equals(objprofileNewRequestList.profileSubParameterID)))
                                    {
                                        cbOption.isChecked=true;
                                        break
                                    }
                                }

                                cbOption.setOnClickListener { view ->
                                    if (view is CheckBox) {
                                        when {
                                            view.isChecked -> {
                                                extraFieldLayout.removeAllViews()
                                                extraFieldLayout.removeAllViewsInLayout()

                                                when {
                                                    !CRMAppUtil.isBlank(mOptions.extraInfo) && mOptions.extraInfo.equals(
                                                        "Y", ignoreCase = true
                                                    ) -> setExtraView(mOptions, extraFieldLayout)
                                                    else -> {
                                                        extraFieldLayout.removeAllViews()
                                                        extraFieldLayout.visibility = View.GONE
                                                    }
                                                }

                                                    var valueString = ""
                                                    if (!CRMAppUtil.isBlank(mOptions.extraInfo) && mOptions.extraInfo.equals(
                                                            "Y", ignoreCase = true
                                                        )
                                                    ) {
                                                        valueString = getExtraView(mOptions)
                                                    }
                                                deletePreviousItem( mOptions.profileParameterID!!.toString(),
                                                    mOptions.profileSubParameterID!!.toString())
                                                    profileNewRequestList.add(
                                                        ProfileRequestData(
                                                            mOptions.profileParameterID!!.toString(),
                                                            mOptions.profileSubParameterID!!.toString(), leadId,
                                                            valueString
                                                        )
                                                    )

                                            }
                                            else -> {
                                                extraFieldLayout.removeAllViews()
                                                extraFieldLayout.visibility = View.GONE
                                            }
                                        }
                                    }
                                }

                                if (mOptions.isSelected) {
                                    setExtraView(mOptions, extraFieldLayout)
                                    val valueString =  getExtraView( mOptions)
                                    profileNewRequestList.add(
                                        ProfileRequestData(
                                            mOptions.profileParameterID!!.toString(),
                                            mOptions.profileSubParameterID!!.toString(), leadId,
                                            valueString
                                        )
                                    )
                                }
                                llBasicProfileQuestionLayoutInner.addView(cbOption)
                                llBasicProfileQuestionLayoutInner.addView(extraFieldLayout)
                            }
                        }
                        DROP_DOWN -> {
                            val childValue = layoutInflater
                                .inflate(R.layout.include_spiner, null) as ViewGroup
                            val tvHead = childValue.findViewById<View>(R.id.tvHead) as TextView
                            val spSection = childValue.findViewById<View>(R.id.spSection) as Spinner
                            tvHead.visibility = View.GONE

                            spSection.tag =
                                question.profileParameterID.toString() + "_" + "+" + "_SP"
                            val arrAdap = ArrayAdapter(
                                requireContext(), layout.simple_spinner_item,
                                question.profileOptionList
                            )
                            arrAdap.setDropDownViewResource(R.layout.mdropdown)

                            spSection.adapter = arrAdap
                            formElementList.add(spSection)
                            llBasicProfileQuestionLayoutInner.addView(childValue)
                            val pos = question.dropdownValueArrPosition

                            val extraFieldLayout = LinearLayout(activity).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                orientation = LinearLayout.VERTICAL
                                setPadding(padding, padding, padding, padding)
                                visibility = View.GONE
                            }
                            question.profileOptionList.forEachIndexed { index, mOptions ->
                                for (objprofileNewRequestList in profileNewRequestList) {

                                    if ((mOptions.profileParameterID.toString()
                                            .equals(objprofileNewRequestList.profileParameterID)) && (mOptions.profileSubParameterID.toString()
                                            .equals(objprofileNewRequestList.profileSubParameterID))
                                    ) {
                                        spSection.setSelection(index);
                                        break
                                    }
                                }
                            }

                            //Adding linarlayout for info for extra end
                            spSection.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val poP =
                                            parent.getItemAtPosition(position) as ProfileOption

                                        extraFieldLayout.removeAllViews()
                                        extraFieldLayout.removeAllViewsInLayout()

                                        if (!CRMAppUtil.isBlank(poP.extraInfo) && poP.extraInfo.equals(
                                                "Y", ignoreCase = true
                                            )
                                        ) {
                                            setExtraView(poP, extraFieldLayout)
                                        } else {
                                            extraFieldLayout.removeAllViews()
                                            extraFieldLayout.visibility = View.GONE
                                        }

                                        var valueString = ""
                                        if (!CRMAppUtil.isBlank(poP.extraInfo) && poP.extraInfo
                                                .equals("Y", ignoreCase = true)
                                        ) {
                                            valueString = getExtraView(poP)
                                        }
                                        deletePreviousItem( poP.profileParameterID!!.toString(),
                                            poP.profileSubParameterID!!.toString())

                                        profileNewRequestList.add(
                                            ProfileRequestData(
                                                poP.profileParameterID!!.toString(),
                                                poP.profileSubParameterID!!.toString(),
                                                leadId,
                                                valueString
                                            )
                                        )
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {

                                    }
                                }
                            childValue.addView(extraFieldLayout)

                            if (pos >= 0) {
                                //you get selected position set it
                                spSection.setSelection(pos)

                                val poP =
                                    spSection.getItemAtPosition(pos) as ProfileOption

                                val valueString =  getExtraView( poP)
                                profileNewRequestList.add(
                                    ProfileRequestData(
                                        poP.profileParameterID!!.toString(),
                                        poP.profileSubParameterID!!.toString(), leadId,
                                        valueString
                                    )
                                )
                            }
                        }
                        DATE_PICKER -> {
                            val childValue = layoutInflater
                                .inflate(R.layout.include_datepicker, null) as ViewGroup
                            val tvDate = childValue.findViewById<View>(R.id.tvDate) as TextView
                            val rlDate =
                                childValue.findViewById<View>(R.id.rlDate) as RelativeLayout
                            llBasicProfileQuestionLayoutInner.addView(childValue)
                            rlDate.visibility = View.GONE
                            if (question.profileOptionList.isNotEmpty()) {
                                rlDate.visibility = View.VISIBLE
                                tvDate.apply {
                                    text = question.profileOptionList[0].value
                                    tag =
                                        question.profileParameterID.toString() +
                                                "_" +
                                                question.profileOptionList[0].profileSubParameterID +
                                                "_T"
                                }
                            }

                            for( objprofileNewRequestList in profileNewRequestList){
                                if((question.profileOptionList[0].profileParameterID.toString().equals(objprofileNewRequestList.profileParameterID))&& (question.profileOptionList[0].profileSubParameterID.toString().equals(objprofileNewRequestList.profileSubParameterID)))
                                {
                                    tvDate.setText("${objprofileNewRequestList.value}")
                                    break
                                }
                            }
                            rlDate.setOnClickListener {
                                val cal = Calendar.getInstance()
                                val y = cal.get(Calendar.YEAR)
                                val m = cal.get(Calendar.MONTH)
                                val d = cal.get(Calendar.DAY_OF_MONTH)

                                val datePickerDialog = DatePickerDialog(
                                    requireContext(),
                                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                                        val dateStr = "$dayOfMonth-${monthOfYear + 1}-$year"
                                        val pattern = "dd-MM-yyyy"
                                        val simpleDateFormat =
                                            SimpleDateFormat(pattern, Locale.ENGLISH)
                                        val date = simpleDateFormat.parse(dateStr)
                                        tvDate.text = Editable.Factory.getInstance()
                                            .newEditable(simpleDateFormat.format(date))
                                        deletePreviousItem( question.profileParameterID.toString(),
                                            question.profileOptionList[0].profileSubParameterID.toString())
                                        if (tvDate.text.toString().isNotEmpty()) {
                                            profileNewRequestList += ProfileRequestData(
                                                question.profileParameterID.toString(),  question.profileOptionList[0].profileSubParameterID.toString(), leadId, tvDate.text.toString()
                                            )
                                        }

                                    },
                                    y,
                                    m,
                                    d
                                )
                                datePickerDialog.datePicker.maxDate = cal.timeInMillis
                                datePickerDialog.show()
                            }

                            if (tvDate.text.toString().isNotEmpty()) {
                                profileNewRequestList += ProfileRequestData(
                                    question.profileParameterID.toString(),  question.profileOptionList[0].profileSubParameterID.toString(), leadId, tvDate.text.toString()
                                )
                            }
                            formElementList.add(tvDate)
                            questionMap[question.profileParameterID!!.toString()] = childValue

                            if (question.isDefaultHidden.equals("Y", ignoreCase = true)) {
                                childValue.visibility = View.GONE
                            }
                        }
                        else -> {
                        }
                    }// ignore
                }
                val questionBar = View(activity)
                questionBar.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30)
                questionBar.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )

                llBasicProfileQuestionLayoutInner.addView(questionBar)

            }

            //question section end

            binding.mainProfileLayouttarget.addView(groupLayout)
            val v = View(activity)

            v.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5)
            binding.mainProfileLayouttarget.addView(v)

            if (mCoutFlagFirstGroup) {
                //collapse others
                    toggleSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName)
            } else {
                openSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName)
            }
            if(strSelectedClientType.equals("814") || strSelectedClientType.equals("815") )
            {
                if(group.profileParameterGroupID==121 )
                {
                    openSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName)
                }
                else if(group.profileParameterGroupID==109)
                {
                    toggleSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName)
                }

            }
            else if(strSelectedClientType.equals("524") ||strSelectedClientType.equals("523"))
            {
                if( group.profileParameterGroupID==117)
                {
                    openSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName)
                }
                else if(group.profileParameterGroupID==109)
                {
                    toggleSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName)
                }
            }


            mCoutFlagFirstGroup = true
        }
        //perform proper opioin
        for (StringAction in previousActionMap.keys) {
            val hideView = questionMap[StringAction]
            val hideViewHead = questionMap[StringAction + Head]
            if (previousActionMap[StringAction]!!.equals("N", ignoreCase = true)) {
                hideView!!.visibility = View.VISIBLE
                hideViewHead!!.visibility = View.VISIBLE
            } else if (previousActionMap[StringAction]!!.equals("Y", ignoreCase = true)) {
                hideView!!.visibility = View.GONE
                hideViewHead!!.visibility = View.GONE
            }
        }
    }

    fun prefix(input: Int): String? {
        return if (input.toString().length == 1) {
            "0$input"
        } else input.toString() + ""
    }

    override fun renderSubmitProfile(uiModel: UiModel<ProfilingResponseModel>) {
        toggleProgressBar(uiModel.inProgress)
        uiModel.onFailure {
            Timber.d("LeadProfilingFragment renderSubmitProfile showOnFailurePopup ${it.message}")
            showOnFailurePopup(it)
        }

        uiModel.onSuccess {
            if (it.status) {
                if(isShowSuccessMessage) {
                    alertDialog(
                        requireContext(), 0,
                        resources.getString(R.string.motilal_oswal),
                        MOSLConstants.SET_USER_PROFILE, "OK", true
                    ) { action, data1 ->
                        if (action == MSFDialog.Action.OK) {
                            viewPager?.setCurrentItem(
                                MOSLConstants.LEAD_DETAIL, false
                            )
                        }
                    }
                }

            }
        }
    }

    override fun render(leadId: String) {
        this.leadId = leadId
    }

    override fun renderProfileParameters(uiModel: UiModel<List<LeadProfile>>) {
        toggleProgressBar(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d("LeadProfilingFragment renderProfileParameters showOnFailurePopup ${it.message}")
            showOnFailurePopup(it)
        }


        uiModel.onSuccess { dataList ->
            if (dataList.isNotEmpty()) {
                try {
                    binding.mainProfileForm.visibility = View.VISIBLE
                    fillProfileModal(dataList)
                    renderForm()
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
//                    alertDialog(
//                        requireContext(), 0, resources.getString(R.string.motilal_oswal),
//                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null
//                    )
                }
            }

        }

    }

    companion object {
        private const val TEXT_BOX = 1
        private const val NUMBER_TEXT_BOX = 5
        private const val DATE_PICKER = 6
        private const val DROP_DOWN = 4
        private const val RADIO_BUTTON = 2
        private const val CHECK_BOX = 3
        private const val ANS_SEPERATER = "#"
        private const val ANS_GROUP_SEPERATER = "|"
        private const val Head = "_Head"

        @JvmStatic
        fun newInstance(
            leadId: String
        ): LeadProfilingFragment {
            val args = Bundle().apply {
                putString(MOSLConstants.LEAD_ID, leadId)
            }
            return LeadProfilingFragment().apply {
                arguments = args
            }
        }
    }
}