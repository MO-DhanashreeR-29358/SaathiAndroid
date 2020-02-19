package com.mosl.tabapp.leadViewDetail.profiling

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileRequestData
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
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class LeadProfilingFragment : CommonFragment(), LeadProfilingView {
  private var leadId = "0"
  private var viewPager: ViewPager? = null
  private var profileModel: ProfileModel? = null
  private var formElementList: MutableList<View>? = null
  //This list is added just to have reffrence of each question so if we can findthem in on click event of their siblings child
  private var questionMap: MutableMap<String, View> = emptyMap<String, View>().toMutableMap()
  private lateinit var binding: FragLeadProfilingBinding
  private val subject = PublishSubject.create<LeadProfileUiEvent>()

  @Inject
  internal lateinit var presenter: LeadProfilingPresenter

  override fun getLayout(): Int = layout.frag_lead_profiling

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
  private val selectedAnswersJson by lazy {
    val profileRequestList = ArrayList<ProfileRequestData>()
      for (entry in formElementList!!) {
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
              profileRequestList += ProfileRequestData(
                  tagArray[0], tagArray[1], leadId, textInEditText
              )
            } catch (e: Exception) {
              Timber.d(e)
            }
          }
          is RadioGroup -> for (i in 0 until entry.childCount) {
            val rbTemp = entry.getChildAt(i) as RadioButton
            val profileOption = rbTemp.tag as ProfileOption
            if (profileOption != null && rbTemp.isChecked) {
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

  override fun getProfileRequestDataList(): List<ProfileRequestData> = selectedAnswersJson
  override fun onSubmitProfileEventFired(): Observable<SubmitProfileEvent> = subject.ofType()
  override fun onSubmitButtonClicked(): Observable<Unit> = binding.btnSubmit.clicks()
  override fun renderSubmitButtonClicked() {
    val request = selectedAnswersJson
    when {
      request.isNotEmpty() -> subject.onNext(SubmitProfileEvent)
      else -> {
        alertDialog(
            requireContext(), 0,
            resources.getString(R.string.motilal_oswal), SOME_SEELECTION_METHOD,
            resources.getString(android.R.string.ok), true, null
        )
      }
    }
  }

  override fun onCancelButtonClicked(): Observable<Unit> = binding.btnCancel.clicks()
  override fun renderCancelButtonClicked() =
    viewPager!!.setCurrentItem(MOSLConstants.LEAD_DETAIL, false)

  private fun fillProfileModal(leadProfileItems: List<LeadProfile>) {

    profileModel = ProfileModel()
    val profileQuestionGroupList = ArrayList<ProfileQuestionGroup>()
    for (responseI in leadProfileItems.indices) {
      val (groupName1, _, inApproval, _, ProfileParameterGroupID, BusinessUnitId, _, IsAdminMode, parameterList, _, profileParameterId) = leadProfileItems[responseI]
      val groupName = stringOrEmpty(groupName1)
      val profileId = stringOrEmpty(profileParameterId)
      val tempProfileQuestionGroup = ProfileQuestionGroup().apply {
        this.groupName = groupName
        this.profileParameterID = profileId
        this.inApproval = inApproval
        this.profileParameterGroupID = ProfileParameterGroupID
        this.businessUnitId = BusinessUnitId
        this.isAdminMode = IsAdminMode
      }

      val mProfileQuestionList = ArrayList<ProfileQuestion>()
      if (parameterList != null) {
        for (profileParameterListI in parameterList.indices) {
          val (_, questionType1, selectedProfileSubParameterID1, _, _, subParameterList, isDefaultHidden, ProfileParameterListProfileParameterGroupID, _, _, _, isAdminMode, controlType, _, questionText1, profileParameterID) = parameterList[profileParameterListI]
          val tmpProfileQuestion = ProfileQuestion()

          tmpProfileQuestion.controlType = controlType
          tmpProfileQuestion.inApproval = inApproval
          tmpProfileQuestion.profileParameterGroupID = ProfileParameterListProfileParameterGroupID
          val questionType = questionType1!!
          tmpProfileQuestion.questionType = questionType
          val selectedProfileSubParameterID = selectedProfileSubParameterID1!!
          tmpProfileQuestion.questionType = selectedProfileSubParameterID
          val profileParameterListIsAdminMode = isAdminMode!!
          tmpProfileQuestion.isAdminMode = profileParameterListIsAdminMode
          val questionText = stringOrEmpty(questionText1)
          tmpProfileQuestion.questionText = questionText

          val yNStatus = when (isDefaultHidden) {
            null, "" -> "N"
            else -> isDefaultHidden
          }
          tmpProfileQuestion.isDefaultHidden = yNStatus

          val profileParameterListProfileParameterID = profileParameterID!!
          tmpProfileQuestion.profileParameterID = profileParameterListProfileParameterID

          val mProfileOptionList = ArrayList<ProfileOption>()
          if (subParameterList != null) {
            for (profileSubParameterListI in subParameterList.indices) {
              val (_, isHide, _, questionSubText, isSelected, extraInfo, _, extraInfoFieldName, extraInfoRowCount, inApproval1, profileSubParameterID1, _, value1, hideDepedentQuestion, _, isAdminMode1) = subParameterList[profileSubParameterListI]
              val tempProfileOption = ProfileOption()

              tempProfileOption.profileParameterID = tmpProfileQuestion.profileParameterID

              val profileSubParameterListInApproval = inApproval1!!
              tempProfileOption.inApproval = profileSubParameterListInApproval

              val value = stringOrEmpty(value1)
              tempProfileOption.value = value
              tempProfileOption.isSelected = isSelected!!

              val profileSubParameterID = profileSubParameterID1!!
              tempProfileOption.profileSubParameterID = profileSubParameterID
              tempProfileOption.hideDepedentQuestion = stringOrEmpty(hideDepedentQuestion)
              val noStr = stringOrEmpty(isHide)
              tempProfileOption.setIsHide(noStr)

              val strQuestionsubtext = stringOrEmpty(questionSubText)
              tempProfileOption.questionSubText = strQuestionsubtext

              val profileSubParameterListIsAdminMode = isAdminMode1!!
              tempProfileOption.isAdminMode = profileSubParameterListIsAdminMode

              tempProfileOption.extraInfo = stringOrEmpty(extraInfo)
              when {
                extraInfoRowCount != null -> tempProfileOption.extraInfoRowCount = extraInfoRowCount
              }
              tempProfileOption.extraInfoFieldName = stringOrEmpty(extraInfoFieldName)

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

    profileModel!!.profileQuestionGroup = profileQuestionGroupList
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
        llSegment.setBackgroundResource(R.color.dashbord_background)
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
    return if (extraViewList != null) {
      try {
        for (vList in extraViewList) {
          for (mView in vList) {
            val mData = (mView as EditText).text.toString()
                .trim { it <= ' ' }
            if (mData.trim { it <= ' ' }.length < 1) {
              valueString.append(" $ANS_SEPERATER")
            } else {
              valueString.append(mData)
                  .append(ANS_SEPERATER)
            }
          }//remove last ##
          valueString = StringBuilder(
              valueString.substring(0, valueString.length - ANS_SEPERATER.length)
          )
          valueString.append(ANS_GROUP_SEPERATER)
        }
        //remove last | symbol
        valueString.substring(
            0,
            valueString.length - ANS_GROUP_SEPERATER.length
        )
      } catch (eie: StringIndexOutOfBoundsException) {
        ""//return empty}
      }

    } else {
      ""//return empty
    }
  }

  /**
   * this method will render/design the profile page with question filled in model
   */

  @SuppressLint("DefaultLocale")
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
      var questionNo = 1
      for (question in group.profileQuestionList) {
        val padding = resources.getDimension(R.dimen._15sdp)
            .toInt()
        val tvQuestion = TextView(activity).apply {
          text = question.questionText
          layoutParams = LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
          )
          setTextColor(ContextCompat.getColor(requireContext(), R.color.light_black))
          setPadding(padding, padding, padding, padding)
          text = String.format("Q %d.%s", questionNo, question.questionText)
          questionNo++
          textSize = resources.getDimension(R.dimen.txt_size_profile)
          setBackgroundResource(R.drawable.profiling_question_background_text)
        }


        llBasicProfileQuestionLayoutInner.addView(tvQuestion)
        questionMap[question.profileParameterID!!.toString() + Head] = tvQuestion
        if (question.isDefaultHidden.equals("Y", ignoreCase = true)) {
          tvQuestion.visibility = View.GONE
        }
        if (!question.profileOptionList
                .isEmpty()
        ) {// && question.getProfileOptionList().size()<1
          when (question.controlType) {
            0 -> {
            }
            TEXT_BOX// question type edit text
            -> {

              val llBasicProfileQuestionLayoutGroup = LinearLayout(activity)
              llBasicProfileQuestionLayoutGroup.layoutParams = LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.WRAP_CONTENT
              )
              llBasicProfileQuestionLayoutGroup.orientation = LinearLayout.VERTICAL
              llBasicProfileQuestionLayoutGroup.setPadding(
                  padding, padding, padding,
                  padding
              )

              for (mOptions in question.profileOptionList) {
                val tvHead = TextView(activity)
                tvHead.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tvHead.setTextColor(resources.getColor(R.color.light_black))
                tvHead.setPadding(padding, 4, padding, 4)
                tvHead.text = mOptions.questionSubText
                //  tvHead.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                tvHead.textSize = resources.getDimension(R.dimen.txt_size_profile)
                val mEditTextOption = EditText(activity)
                mEditTextOption.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                mEditTextOption.setBackgroundResource(R.drawable.textview_border)
                mEditTextOption.tag = (question.profileParameterID.toString()
                    + "_"
                    + mOptions.profileSubParameterID
                    + "_E")
                mEditTextOption.animate()
                mEditTextOption.filters = arrayOf(CRMAppUtil.filterOnlyAlphabetAndNumbers)
                mEditTextOption.setText(mOptions.value)
                mEditTextOption.hint = mOptions.questionSubText
                tvHead.text = mOptions.questionSubText
                llBasicProfileQuestionLayoutGroup.addView(tvHead)
                llBasicProfileQuestionLayoutGroup.addView(mEditTextOption)
                //Adding to form List
                formElementList!!.add(mEditTextOption)
              }//for for options end
              llBasicProfileQuestionLayoutGroup.setBackgroundResource(
                  R.drawable.profiling_question_background_group
              )
              llBasicProfileQuestionLayoutInner.addView(
                  llBasicProfileQuestionLayoutGroup
              )
              questionMap[question.profileParameterID!!.toString()] =
                llBasicProfileQuestionLayoutGroup
            }
            RADIO_BUTTON// question type is radio button
            -> {
              //start radio group
              val rbGroup = RadioGroup(activity)
              llBasicProfileQuestionLayoutInner.addView(rbGroup)
              rbGroup.layoutParams = LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.WRAP_CONTENT
              )
              rbGroup.setPadding(padding, padding, padding, padding)
              rbGroup.setBackgroundResource(
                  R.drawable.profiling_question_background_group
              )
              rbGroup.orientation = RadioGroup.VERTICAL
              var rbOptionSelected: RadioButton? = null
              var previousAction = ProfileOption()
              //Adding linarlayout for info for extra
              val extraFieldLayout = LinearLayout(activity)
              extraFieldLayout.layoutParams = LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.WRAP_CONTENT
              )
              extraFieldLayout.orientation = LinearLayout.VERTICAL
              extraFieldLayout.setPadding(padding, padding, padding, padding)
              extraFieldLayout.visibility = View.GONE
              //Adding linarlayout for info for extra end
              llBasicProfileQuestionLayoutInner.addView(extraFieldLayout)
              for (mOptions in question.profileOptionList) {
                val rbOption = RadioButton(activity)
                rbOption.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val mPadding = resources.getDimension(R.dimen._8sdp)
                    .toInt()
                rbOption.setPadding(padding, mPadding, padding, mPadding)
                // rbOption.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                rbOption.textSize = resources.getDimension(R.dimen.txt_size_profile)
                rbOption.text = mOptions.questionSubText
                //rbOption.setTag(question.getProfileParameterID() + "_" + mOptions.getProfileSubParameterID() + "_"+m);
                rbOption.tag = mOptions
                rbOption.setOnClickListener {
                  extraFieldLayout.removeAllViews()
                  extraFieldLayout.removeAllViewsInLayout()

                  if (!CRMAppUtil.isBlank(mOptions.extraInfo) && mOptions
                          .extraInfo
                          .equals("Y", ignoreCase = true)
                  ) {
                    setExtraView(mOptions, extraFieldLayout)
                  } else {
                    extraFieldLayout.removeAllViews()
                    extraFieldLayout.visibility = View.GONE
                  }
                  if (!CRMAppUtil.isBlank(
                          mOptions.hideDepedentQuestion
                      )
                  ) {
                    if (questionMap.containsKey(
                            mOptions.hideDepedentQuestion
                        )
                    ) {
                      //questionMap
                      val hideView = questionMap[mOptions.hideDepedentQuestion]
                      val hideViewHead = questionMap[mOptions.hideDepedentQuestion + Head]
                      if (mOptions.isHide.equals("N", ignoreCase = true)) {
                        hideView!!.visibility = View.VISIBLE
                        hideViewHead!!.visibility = View.VISIBLE
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
                        hideView!!.visibility = View.GONE
                        hideViewHead!!.visibility = View.GONE
                      }
                    }
                  }
                }

                if (mOptions.isSelected) {
                  rbOptionSelected = rbOption
                  previousAction = mOptions
                  setExtraView(mOptions, extraFieldLayout)
                }

                rbGroup.addView(rbOption)
              }//end of for loop for option printing

              if (rbOptionSelected != null) {

                rbOptionSelected.isChecked = true
                previousActionMap[previousAction.hideDepedentQuestion] = previousAction.isHide
              }

              //Adding to form List
              formElementList!!.add(rbGroup)
              questionMap[question.profileParameterID!!.toString()] = rbGroup
              if (question.isDefaultHidden.equals("Y", ignoreCase = true)) {
                rbGroup.visibility = View.GONE
              }
            }
            CHECK_BOX// multiple choice
            -> {
              for (mOptions in question.profileOptionList) {
                val cbOption = CheckBox(activity)

                cbOption.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val mPadding = resources.getDimension(R.dimen._8sdp)
                    .toInt()
                cbOption.setPadding(mPadding, mPadding, mPadding, mPadding)
                cbOption.text = mOptions.questionSubText
                cbOption.textSize = resources.getDimension(R.dimen.txt_size_profile)

                cbOption.tag = mOptions
                cbOption.isChecked = mOptions.isSelected

                //Adding to form List
                formElementList!!.add(cbOption)
                //Adding linarlayout for info for extra
                val extraFieldLayout = LinearLayout(activity)
                extraFieldLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                extraFieldLayout.orientation = LinearLayout.VERTICAL
                extraFieldLayout.setPadding(padding, padding, padding, padding)
                extraFieldLayout.visibility = View.GONE
                //Adding linarlayout for info for extra end
                //on click and setting data end
                cbOption.setOnClickListener { v ->
                  if (v is CheckBox) {
                    if (v.isChecked) {
                      extraFieldLayout.removeAllViews()
                      extraFieldLayout.removeAllViewsInLayout()

                      if (!CRMAppUtil.isBlank(mOptions.extraInfo) && mOptions.extraInfo
                              .equals("Y", ignoreCase = true)
                      ) {
                        setExtraView(mOptions, extraFieldLayout)
                      } else {
                        extraFieldLayout.removeAllViews()
                        extraFieldLayout.visibility = View.GONE
                      }
                    } else {
                      //Not Checked State
                      extraFieldLayout.removeAllViews()
                      extraFieldLayout.visibility = View.GONE
                    }
                  }
                }

                if (mOptions.isSelected) {

                  setExtraView(mOptions, extraFieldLayout)
                }

                //on click and setting data end

                llBasicProfileQuestionLayoutInner.addView(cbOption)
                llBasicProfileQuestionLayoutInner.addView(extraFieldLayout)
              }//for for options end
            }
            DROP_DOWN -> {
              val childValue = activity!!.layoutInflater
                  .inflate(R.layout.include_spiner, null) as ViewGroup
              val tvHead = childValue.findViewById<View>(R.id.tvHead) as TextView
              val spSection = childValue.findViewById<View>(R.id.spSection) as Spinner
              tvHead.visibility = View.GONE

              //   spSection.setTag(question.getProfileParameterID() + "_" + "+" + "_SP");
              spSection.tag = question.profileParameterID.toString() + "_" + "+" + "_SP"
              val arrAdap = ArrayAdapter(
                  requireContext(), layout.simple_spinner_item,
                  question.profileOptionList
              )
              arrAdap.setDropDownViewResource(R.layout.mdropdown)

              spSection.adapter = arrAdap
              formElementList!!.add(spSection)
              llBasicProfileQuestionLayoutInner.addView(childValue)
              val pos = question.dropdownValueArrPosition

              //Adding linarlayout for info for extra
              val extraFieldLayout = LinearLayout(activity)
              extraFieldLayout.layoutParams = LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.WRAP_CONTENT
              )
              extraFieldLayout.orientation = LinearLayout.VERTICAL
              extraFieldLayout.setPadding(padding, padding, padding, padding)
              extraFieldLayout.visibility = View.GONE
              //Adding linarlayout for info for extra end
              spSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                  parent: AdapterView<*>,
                  view: View,
                  position: Int,
                  id: Long
                ) {
                  val poP = parent.getItemAtPosition(position) as ProfileOption

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
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
              }
              childValue.addView(extraFieldLayout)

              if (pos >= 0) {
                //you get selected position set it
                spSection.setSelection(pos)
              }
            }
            else -> {
            }
          }// ignore
        }
        val questionBar = View(activity)
        questionBar.layoutParams =
          LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30)
        questionBar.setBackgroundColor(resources.getColor(R.color.white))
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

  override fun renderSubmitProfile(uiModel: UiModel<String>) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      alertDialog(
          activity, 0,
          resources.getString(R.string.motilal_oswal),
          MOSLConstants.SET_USER_PROFILE, "OK", true
      ) { action, data1 ->
        if (action == MSFDialog.Action.OK) {
          viewPager!!.setCurrentItem(
              MOSLConstants.LEAD_DETAIL, false
          )
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
      showOnFailurePopup(it)
    }


    uiModel.onSuccess { dataList ->
      if (dataList.isNotEmpty()) {
        try {
          binding.mainProfileForm.visibility = View.VISIBLE
          fillProfileModal(dataList)
          renderForm()
        } catch (e: Exception) {
          alertDialog(
              requireContext(), 0, resources.getString(R.string.motilal_oswal),
              SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null
          )
        }
      }

    }

  }

  companion object {
    private const val TEXT_BOX = 1
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