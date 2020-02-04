package com.mosl.tabapp.leadViewDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.leadprofile.LeadProfileItem;
import com.mosl.saathi.api.model.leadprofile.LeadProfileRequest;
import com.mosl.saathi.api.model.leadprofile.ProfileParameterListItem;
import com.mosl.saathi.api.model.leadprofile.ProfileSubParameterListItem;
import com.mosl.saathi.api.model.leadprofile.SaveLeadProfileRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.profile.ProfileModel;
import com.mosl.tabapp.model.profile.ProfileOption;
import com.mosl.tabapp.model.profile.ProfileQuestion;
import com.mosl.tabapp.model.profile.ProfileQuestionGroup;
import com.mosl.tabapp.ui.MSFDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_SEELECTION_METHOD;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Vijay on 16/03/2017.
 */

public class LeadProfilingFragment extends CommonFragment
        implements View.OnClickListener, View.OnFocusChangeListener {
    public final static String TAG = LeadProfilingFragment.class.getSimpleName();
    private static final int TEXT_BOX = 1;
    private static final int DROP_DOWN = 4;
    private static final int RADIO_BUTTON = 2;
    private static final int CHECK_BOX = 3;
    private static final String ANS_SEPERATER = "#";
    private static final String ANS_GROUP_SEPERATER = "|";
    // json tags
    private static final String Head = "_Head";
    private String leadId = "";
    private View leadProfilingView;
    private View mainProfileFormView;
    private ViewPager viewPager;
    private ProfileModel profileModel;
    private LinearLayout mainProfileLinearLayout;
    private List<View> formElementList;
    //This list is added just to have reffrence of each question so if we can findthem in on click event of their siblings child
    private Map<String, View> questionList;

    public static LeadProfilingFragment newInstance(int position, String type, String leadId) {
        LeadProfilingFragment fragment = new LeadProfilingFragment();
        Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putString("Type", type);
        args.putString(MOSLConstants.LEAD_ID, leadId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_lead_profiling;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        leadProfilingView = super.onCreateView(inflater, container, savedInstanceState);
        //attachLayout(R.layout.frag_lead_profiling);
        try {

            leadId =
                    this.getArguments().getString(MOSLConstants.LEAD_ID, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpView(leadProfilingView);

        setUserVisibleHint(true);
        return leadProfilingView;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private void setUpView(View leadProfilingView) {
        mainProfileLinearLayout = leadProfilingView.findViewById(R.id.mainProfileLayouttarget);
        Button btnCancel = leadProfilingView.findViewById(R.id.btnCancel);
        Button btnSubmit = leadProfilingView.findViewById(R.id.btnSubmit);
        mainProfileFormView = leadProfilingView.findViewById(R.id.mainProfileForm);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && leadProfilingView != null && viewPager.getCurrentItem() == 2) {
                getProfileQuestionAnswerFromServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSubmit: {
                List<SaveLeadProfileRequest> request = getSelectedAnswersJson();
                if (request.size() > 0) {
                    saveLeadProfileParameter(request);
                } else {
                    MSFDialog.alertDialog(requireContext(), 0,
                            getResources().getString(R.string.motilal_oswal), SOME_SEELECTION_METHOD,
                            getResources().getString(android.R.string.ok), true, null);
                    return;
                }
                break;
            }

            case R.id.btnCancel: {
                //Goto first tab
                viewPager.setCurrentItem(MOSLConstants.LEAD_DETAIL, false);
                break;
            }

            // add other button click
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

    }

    private void fillProfileModal(List<LeadProfileItem> leadProfileItems) {

        profileModel = new ProfileModel();
        List<ProfileQuestionGroup> profileQuestionGroupList = new ArrayList<>();
        for (int responseI = 0; responseI < leadProfileItems.size(); responseI++) {
            ProfileQuestionGroup tempProfileQuestionGroup = new ProfileQuestionGroup();
            LeadProfileItem item = leadProfileItems.get(responseI);

            String groupName = stringOrEmpty(item.groupName);
            tempProfileQuestionGroup.setGroupName(groupName);

            String aris = stringOrEmpty(item.profileParameterId);
            tempProfileQuestionGroup.setProfileParameterID(aris);

            boolean inApproval = item.inApproval;
            tempProfileQuestionGroup.setInApproval(inApproval);

            int ProfileParameterGroupID = item.profileParameterGroupID;
            tempProfileQuestionGroup.setProfileParameterGroupID(ProfileParameterGroupID);

            int BusinessUnitId = item.businessUnitId;
            tempProfileQuestionGroup.setBusinessUnitId(BusinessUnitId);

            boolean IsAdminMode = item.isAdminMode;
            tempProfileQuestionGroup.setIsAdminMode(IsAdminMode);

            List<ProfileParameterListItem> parameterList = item.profileParameterList;
            List<ProfileQuestion> mProfileQuestionList = new ArrayList<>();
            if (parameterList != null) {
                for (int profileParameterListI = 0;
                     profileParameterListI < parameterList.size(); profileParameterListI++) {
                    ProfileParameterListItem parameter = parameterList.get(profileParameterListI);
                    ProfileQuestion tmpProfileQuestion = new ProfileQuestion();

                    tmpProfileQuestion.setControlType(parameter.controlType);
                    boolean ProfileParameterListInApproval = item.inApproval;
                    tmpProfileQuestion.setInApproval(ProfileParameterListInApproval);

                    int ProfileParameterListProfileParameterGroupID =
                            parameter.profileParameterGroupID;
                    tmpProfileQuestion.setProfileParameterGroupID(
                            ProfileParameterListProfileParameterGroupID);

                    int QuestionType = parameter.questionType;
                    tmpProfileQuestion.setQuestionType(QuestionType);
                    int SelectedProfileSubParameterID = parameter.selectedProfileSubParameterID;
                    tmpProfileQuestion.setQuestionType(SelectedProfileSubParameterID);

                    boolean ProfileParameterListIsAdminMode = parameter.isAdminMode;
                    tmpProfileQuestion.setIsAdminMode(ProfileParameterListIsAdminMode);

                    String QuestionText = stringOrEmpty(parameter.questionText);
                    tmpProfileQuestion.setQuestionText(QuestionText);

                    String YNStatus = (parameter.isDefaultHidden == null
                            || parameter.isDefaultHidden.equals(""))
                            ? "N" : parameter.isDefaultHidden;
                    tmpProfileQuestion.setDefaultHidden(YNStatus);

                    int ProfileParameterListProfileParameterID = parameter.profileParameterID;
                    tmpProfileQuestion.setProfileParameterID(
                            ProfileParameterListProfileParameterID);

                    List<ProfileSubParameterListItem> subParameterList =
                            parameter.profileSubParameterList;

                    List<ProfileOption> mProfileOptionList = new ArrayList<>();
                    if (subParameterList != null) {
                        for (int profileSubParameterListI = 0;
                             profileSubParameterListI < subParameterList.size();
                             profileSubParameterListI++) {
                            ProfileSubParameterListItem subParameter =
                                    subParameterList.get(profileSubParameterListI);
                            ProfileOption tempProfileOption = new ProfileOption();

                            tempProfileOption.setProfileParameterID(
                                    tmpProfileQuestion.getProfileParameterID());

                            boolean ProfileSubParameterListInApproval = subParameter.inApproval;
                            tempProfileOption.setInApproval(ProfileSubParameterListInApproval);

                            String value = stringOrEmpty(subParameter.value);
                            tempProfileOption.setValue(value);
                            tempProfileOption.setSelected(subParameter.isSelected);

                            int ProfileSubParameterID = subParameter.profileSubParameterID;
                            tempProfileOption.setProfileSubParameterID(ProfileSubParameterID);
                            tempProfileOption.setHideDepedentQuestion(
                                    stringOrEmpty(subParameter.hideDepedentQuestion));
                            String NoStr = stringOrEmpty(subParameter.isHide);
                            tempProfileOption.setIsHide(NoStr);

                            String str_QuestionSubText =
                                    stringOrEmpty(subParameter.questionSubText);
                            tempProfileOption.setQuestionSubText(str_QuestionSubText);

                            boolean ProfileSubParameterListIsAdminMode = subParameter.isAdminMode;
                            tempProfileOption.setIsAdminMode(ProfileSubParameterListIsAdminMode);

                            tempProfileOption.setExtraInfo(stringOrEmpty(subParameter.extraInfo));
                            if (subParameter.extraInfoRowCount != null) {
                                tempProfileOption.setExtraInfoRowCount(
                                        subParameter.extraInfoRowCount);
                            }
                            tempProfileOption.setExtraInfoFieldName(
                                    stringOrEmpty(subParameter.extraInfoFieldName));

                            mProfileOptionList.add(tempProfileOption);
                        }
                    }
                    tmpProfileQuestion.setProfileOptionList(mProfileOptionList);
                    mProfileQuestionList.add(tmpProfileQuestion);
                }
            }
            tempProfileQuestionGroup.setProfileQuestionList(mProfileQuestionList);
            profileQuestionGroupList.add(tempProfileQuestionGroup);
        }

        profileModel.setProfileQuestionGroup(profileQuestionGroupList);
    }

    private void setExtraView(ProfileOption mOptions, ViewGroup extraFieldLayout) {
        try {
            if (mOptions.getExtraInfo().equalsIgnoreCase("N")) {
                //No need to go further
                return;
            }
            List<List<View>> extraViewList = null;
            extraFieldLayout.setVisibility(View.VISIBLE);
            Integer rowCount = mOptions.getExtraInfoRowCount();
            String fieldNameMix = mOptions.getExtraInfoFieldName();
            String[] fieldNameArray = fieldNameMix.split("[" + ANS_GROUP_SEPERATER + "]");
            extraFieldLayout.setVisibility(View.VISIBLE);
            // if (rowCount > 0) {
            extraViewList = new ArrayList<List<View>>();
            //}

            for (int ctr = 0; ctr < rowCount; ctr++) {
                List<View> innerList = new ArrayList<>();
                LinearLayout llSegment = new LinearLayout(getActivity());
                llSegment.setOrientation(LinearLayout.VERTICAL);
                llSegment.setBackgroundResource(R.color.dashbord_background);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        DrawerLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins((int) getResources().getDimension(R.dimen._3sdp),
                        (int) getResources().getDimension(R.dimen._3sdp),
                        (int) getResources().getDimension(R.dimen._3sdp),
                        (int) getResources().getDimension(R.dimen._3sdp));
                llSegment.setLayoutParams(params);
                llSegment.setPadding((int) getResources().getDimension(R.dimen._3sdp),
                        (int) getResources().getDimension(R.dimen._3sdp),
                        (int) getResources().getDimension(R.dimen._3sdp),
                        (int) getResources().getDimension(R.dimen._3sdp));
                extraFieldLayout.addView(llSegment);
                for (String label1 : fieldNameArray) {

                    TextView extraTextView1 = new TextView(getActivity());
                    extraTextView1.setText(label1);
                    EditText extraEditText1 = new EditText(getActivity());
                    extraEditText1.setLayoutParams(
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                    extraEditText1.setBackgroundResource(R.drawable.textview_border);
                    extraEditText1.setFilters(
                            new InputFilter[]{CRMAppUtil.filterOnlyAlphabetAndNumbers});
                    llSegment.addView(extraTextView1);
                    llSegment.addView(extraEditText1);

                    innerList.add(extraEditText1);
                }
                extraViewList.add(innerList);
            }

            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            mOptions.setExtraViewList(extraViewList);

            String[] data = mOptions.getValue().split("[" + ANS_GROUP_SEPERATER + "]");
            if (data.length == extraViewList.size()) {
                //if you have proper data

                for (int i = 0; i < extraViewList.size(); i++) {// for for list of group
                    String[] dataAns = data[i].split("[" + ANS_SEPERATER + "]");
                    if (dataAns.length == extraViewList.get(i).size()) {
                        List<View> mAnsEdit = extraViewList.get(i);
                        for (int j = 0; j < dataAns.length; j++) {
                            EditText ed = (EditText) mAnsEdit.get(j);
                            ed.setText(dataAns[j].trim());
                        }
                    }
                }
            }

            //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param profileOption its object in views tag
     **/
    private String getExtraView(ProfileOption profileOption) {
        StringBuilder valueString = new StringBuilder();
        List<List<View>> extraViewList = profileOption.getExtraViewList();
        if (extraViewList != null) {
            try {
                for (List<View> vList : extraViewList) {
                    for (View mView : vList) {
                        String mData = ((EditText) mView).getText().toString().trim();
                        if (mData.trim().length() < 1) {
                            valueString.append(" " + ANS_SEPERATER);
                        } else {
                            valueString.append(mData).append(ANS_SEPERATER);
                        }
                    }//remove last ##
                    valueString = new StringBuilder(
                            valueString.substring(0, valueString.length() - ANS_SEPERATER.length()));
                    valueString.append(ANS_GROUP_SEPERATER);
                }
                //remove last | symbol
                return valueString.substring(0,
                        valueString.length() - ANS_GROUP_SEPERATER.length());
            } catch (StringIndexOutOfBoundsException eie) {
                return "";//return empty}
            }
        } else {
            return "";//return empty
        }
    }

    /**
     * this method will render/design the profile page with question filled in model
     */

    @SuppressLint("DefaultLocale")
    private void renderForm() {
        if (profileModel == null) {
            return;
        }
        formElementList = new ArrayList<>();
        questionList = new HashMap<>();
        Map<String, String> previousActionMap = new HashMap<>();

        mainProfileLinearLayout.removeAllViews();
        // this is just a flag to Set Open first group
        boolean mCoutFlagFirstGroup = false;
        for (ProfileQuestionGroup group : profileModel.getProfileQuestionGroup()) {
            String groupName = group.getGroupName();
            @SuppressLint("InflateParams") ViewGroup groupLayout =
                    (ViewGroup) requireActivity()
                            .getLayoutInflater()
                            .inflate(R.layout.include_section
                                    , null);

            final TextView tvGroupName = (TextView) groupLayout.findViewById(R.id.tvSectionName);
            tvGroupName.setText(groupName);

            //question section start
            final LinearLayout llBasicProfileQuestionLayoutInner =
                    (LinearLayout) groupLayout.findViewById(R.id.llSectionContent);

            tvGroupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName);
                }
            });

            //Question
            // group
            int questionNo = 1;
            for (ProfileQuestion question : group.getProfileQuestionList()) {
                TextView tvQuestion = new TextView(getActivity());
                tvQuestion.setText(question.getQuestionText());
                tvQuestion.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                tvQuestion.setTextColor(getResources().getColor(R.color.light_black));
                final int padding = (int) getResources().getDimension(R.dimen._15sdp);
                tvQuestion.setPadding(padding, padding, padding, padding);
                tvQuestion.setText(
                        String.format("Q %d.%s", questionNo, question.getQuestionText()));
                questionNo++;
                // tvQuestion.setTextSize(getResources().getDimension(R.dimen._10sdp));
                tvQuestion.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                tvQuestion.setBackgroundResource(R.drawable.profiling_question_background_text);

                llBasicProfileQuestionLayoutInner.addView(tvQuestion);
                questionList.put(question.getProfileParameterID().toString() + Head, tvQuestion);
                if (question.isDefaultHidden().equalsIgnoreCase("Y")) {
                    tvQuestion.setVisibility(View.GONE);
                }
                if (!question.getProfileOptionList()
                        .isEmpty()) {// && question.getProfileOptionList().size()<1
                    switch (question.getControlType()) {
                        case 0:
                            // ignore
                            break;
                        case TEXT_BOX:// question type edit text
                        {

                            LinearLayout llBasicProfileQuestionLayoutGroup =
                                    new LinearLayout(getActivity());
                            llBasicProfileQuestionLayoutGroup.setLayoutParams(
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                            llBasicProfileQuestionLayoutGroup.setOrientation(LinearLayout.VERTICAL);
                            llBasicProfileQuestionLayoutGroup.setPadding(padding, padding, padding,
                                    padding);

                            for (ProfileOption mOptions : question.getProfileOptionList()) {
                                TextView tvHead = new TextView(getActivity());
                                tvHead.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                tvHead.setTextColor(getResources().getColor(R.color.light_black));
                                tvHead.setPadding(padding, 4, padding, 4);
                                tvHead.setText(mOptions.getQuestionSubText());
                                //  tvHead.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                                tvHead.setTextSize(
                                        getResources().getDimension(R.dimen.txt_size_profile));
                                EditText mEditTextOption = new EditText(getActivity());
                                mEditTextOption.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                mEditTextOption.setBackgroundResource(R.drawable.textview_border);
                                mEditTextOption.setTag(question.getProfileParameterID()
                                        + "_"
                                        + mOptions.getProfileSubParameterID()
                                        + "_E");
                                mEditTextOption.animate();
                                mEditTextOption.setFilters(
                                        new InputFilter[]{CRMAppUtil.filterOnlyAlphabetAndNumbers});
                                mEditTextOption.setText(mOptions.getValue());
                                mEditTextOption.setHint(mOptions.getQuestionSubText());
                                tvHead.setText(mOptions.getQuestionSubText());
                                llBasicProfileQuestionLayoutGroup.addView(tvHead);
                                llBasicProfileQuestionLayoutGroup.addView(mEditTextOption);
                                //Adding to form List
                                formElementList.add(mEditTextOption);
                            }//for for options end
                            llBasicProfileQuestionLayoutGroup.setBackgroundResource(
                                    R.drawable.profiling_question_background_group);
                            llBasicProfileQuestionLayoutInner.addView(
                                    llBasicProfileQuestionLayoutGroup);
                            questionList.put(question.getProfileParameterID().toString(),
                                    llBasicProfileQuestionLayoutGroup);
                        }
                        break;
                        case RADIO_BUTTON:// question type is radio button
                        {
                            //start radio group
                            RadioGroup rbGroup = new RadioGroup(getActivity());
                            llBasicProfileQuestionLayoutInner.addView(rbGroup);
                            rbGroup.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            rbGroup.setPadding(padding, padding, padding, padding);
                            rbGroup.setBackgroundResource(
                                    R.drawable.profiling_question_background_group);
                            rbGroup.setOrientation(RadioGroup.VERTICAL);
                            RadioButton rbOptionSelected = null;
                            ProfileOption previousAction = new ProfileOption();
                            //Adding linarlayout for info for extra
                            final LinearLayout extraFieldLayout = new LinearLayout(getActivity());
                            extraFieldLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            extraFieldLayout.setOrientation(LinearLayout.VERTICAL);
                            extraFieldLayout.setPadding(padding, padding, padding, padding);
                            extraFieldLayout.setVisibility(View.GONE);
                            //Adding linarlayout for info for extra end
                            llBasicProfileQuestionLayoutInner.addView(extraFieldLayout);
                            for (final ProfileOption mOptions : question.getProfileOptionList()) {
                                RadioButton rbOption = new RadioButton(getActivity());
                                rbOption.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                int mPadding = (int) getResources().getDimension(R.dimen._8sdp);
                                rbOption.setPadding(padding, mPadding, padding, mPadding);
                                // rbOption.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                                rbOption.setTextSize(
                                        getResources().getDimension(R.dimen.txt_size_profile));
                                rbOption.setText(mOptions.getQuestionSubText());
                                //rbOption.setTag(question.getProfileParameterID() + "_" + mOptions.getProfileSubParameterID() + "_"+m);
                                rbOption.setTag(mOptions);
                                rbOption.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        extraFieldLayout.removeAllViews();
                                        extraFieldLayout.removeAllViewsInLayout();

                                        if (!CRMAppUtil.isBlank(mOptions.getExtraInfo()) && mOptions
                                                .getExtraInfo()
                                                .equalsIgnoreCase("Y")) {
                                            setExtraView(mOptions, extraFieldLayout);
                                        } else {
                                            extraFieldLayout.removeAllViews();
                                            extraFieldLayout.setVisibility(View.GONE);
                                        }
                                        if (!CRMAppUtil.isBlank(
                                                mOptions.getHideDepedentQuestion())) {
                                            if (questionList.containsKey(
                                                    mOptions.getHideDepedentQuestion())) {
                                                //questionList
                                                View hideView = questionList.get(
                                                        mOptions.getHideDepedentQuestion());
                                                View hideViewHead = questionList.get(
                                                        mOptions.getHideDepedentQuestion() + Head);
                                                if (mOptions.isHide().equalsIgnoreCase("N")) {
                                                    hideView
                                                            .setVisibility(View.VISIBLE);
                                                    hideViewHead
                                                            .setVisibility(View.VISIBLE);
                                                } else if (mOptions.isHide()
                                                        .equalsIgnoreCase("Y")) {
                                                    if (hideView instanceof RadioGroup) {
                                                        RadioGroup grp = (RadioGroup) hideView;
                                                        int cnt = grp.getChildCount();
                                                        for (int i = 0; i < cnt; i++) {
                                                            View Child = grp.getChildAt(i);
                                                            if (Child instanceof RadioButton) {
                                                                RadioButton rbTemp =
                                                                        (RadioButton) Child;
                                                                rbTemp.setChecked(false);
                                                            }
                                                        }
                                                    }
                                                    hideView
                                                            .setVisibility(View.GONE);
                                                    hideViewHead
                                                            .setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                });

                                if (mOptions.isSelected()) {
                                    rbOptionSelected = rbOption;
                                    previousAction = mOptions;
                                    setExtraView(mOptions, extraFieldLayout);
                                }

                                rbGroup.addView(rbOption);
                            }//end of for loop for option printing

                            if (rbOptionSelected != null) {

                                rbOptionSelected.setChecked(true);
                                previousActionMap.put(previousAction.getHideDepedentQuestion(),
                                        previousAction.isHide());
                            }

                            //Adding to form List
                            formElementList.add(rbGroup);
                            questionList.put(question.getProfileParameterID().toString(), rbGroup);
                            if (question.isDefaultHidden().equalsIgnoreCase("Y")) {
                                rbGroup.setVisibility(View.GONE);
                            }
                        }
                        break;
                        case CHECK_BOX:// multiple choice
                        {
                            for (final ProfileOption mOptions : question.getProfileOptionList()) {
                                CheckBox cbOption = new CheckBox(getActivity());

                                cbOption.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));

                                int mPadding = (int) getResources().getDimension(R.dimen._8sdp);
                                cbOption.setPadding(mPadding, mPadding, mPadding, mPadding);
                                cbOption.setText(mOptions.getQuestionSubText());
                                cbOption.setTextSize(
                                        getResources().getDimension(R.dimen.txt_size_profile));

                                cbOption.setTag(mOptions);
                                cbOption.setChecked(mOptions.isSelected());

                                //Adding to form List
                                formElementList.add(cbOption);
                                //Adding linarlayout for info for extra
                                final LinearLayout extraFieldLayout =
                                        new LinearLayout(getActivity());
                                extraFieldLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                extraFieldLayout.setOrientation(LinearLayout.VERTICAL);
                                extraFieldLayout.setPadding(padding, padding, padding, padding);
                                extraFieldLayout.setVisibility(View.GONE);
                                //Adding linarlayout for info for extra end
                                //on click and setting data end
                                cbOption.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        if (v instanceof CheckBox) {
                                            CheckBox cb = (CheckBox) v;
                                            if (cb.isChecked()) {
                                                extraFieldLayout.removeAllViews();
                                                extraFieldLayout.removeAllViewsInLayout();

                                                if (!CRMAppUtil.isBlank(mOptions.getExtraInfo())
                                                        && mOptions.getExtraInfo()
                                                        .equalsIgnoreCase("Y")) {
                                                    setExtraView(mOptions, extraFieldLayout);
                                                } else {
                                                    extraFieldLayout.removeAllViews();
                                                    extraFieldLayout.setVisibility(View.GONE);
                                                }
                                            } else {
                                                //Not Checked State
                                                extraFieldLayout.removeAllViews();
                                                extraFieldLayout.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                });

                                if (mOptions.isSelected()) {

                                    setExtraView(mOptions, extraFieldLayout);
                                }

                                //on click and setting data end

                                llBasicProfileQuestionLayoutInner.addView(cbOption);
                                llBasicProfileQuestionLayoutInner.addView(extraFieldLayout);
                            }//for for options end
                        }
                        break;
                        case DROP_DOWN: {
                            ViewGroup childValue = (ViewGroup) getActivity().getLayoutInflater()
                                    .inflate(R.layout.include_spiner
                                            , null);
                            TextView tvHead = (TextView) childValue.findViewById(R.id.tvHead);
                            Spinner spSection = (Spinner) childValue.findViewById(R.id.spSection);
                            tvHead.setVisibility(View.GONE);

                            //   spSection.setTag(question.getProfileParameterID() + "_" + "+" + "_SP");
                            spSection.setTag(question.getProfileParameterID() + "_" + "+" + "_SP");
                            ArrayAdapter<ProfileOption> arrAdap =
                                    new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item,
                                            question.getProfileOptionList());
                            arrAdap.setDropDownViewResource(R.layout.mdropdown);

                            spSection.setAdapter(arrAdap);
                            formElementList.add(spSection);
                            llBasicProfileQuestionLayoutInner.addView(childValue);
                            int pos = question.getDropdownValueArrPosition();

                            //Adding linarlayout for info for extra
                            final LinearLayout extraFieldLayout = new LinearLayout(getActivity());
                            extraFieldLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            extraFieldLayout.setOrientation(LinearLayout.VERTICAL);
                            extraFieldLayout.setPadding(padding, padding, padding, padding);
                            extraFieldLayout.setVisibility(View.GONE);
                            //Adding linarlayout for info for extra end
                            spSection.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view,
                                                                   int position, long id) {
                                            ProfileOption poP =
                                                    (ProfileOption) parent.getItemAtPosition(position);

                                            extraFieldLayout.removeAllViews();
                                            extraFieldLayout.removeAllViewsInLayout();

                                            if (!CRMAppUtil.isBlank(poP.getExtraInfo())
                                                    && poP.getExtraInfo().equalsIgnoreCase("Y")) {
                                                setExtraView(poP, extraFieldLayout);
                                            } else {
                                                extraFieldLayout.removeAllViews();
                                                extraFieldLayout.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                            childValue.addView(extraFieldLayout);

                            if (pos >= 0) {
                                //you get selected position set it
                                spSection.setSelection(pos);
                            }
                        }
                        break;
                        default: {
                        }
                        break;
                    }
                }
                View questionBar = new View(getActivity());
                questionBar.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30));
                questionBar.setBackgroundColor(getResources().getColor(R.color.white));
                llBasicProfileQuestionLayoutInner.addView(questionBar);
            }

            //question section end

            mainProfileLinearLayout.addView(groupLayout);
            View v = new View(getActivity());

            v.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
            mainProfileLinearLayout.addView(v);

            if (mCoutFlagFirstGroup) {
                //collapse others
                toggleSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName);
            } else {
                openSectionExpansion(llBasicProfileQuestionLayoutInner, tvGroupName);
            }
            mCoutFlagFirstGroup = true;
        }
        //perform proper opioin
        for (String StringAction : previousActionMap.keySet()) {
            View hideView = questionList.get(StringAction);
            View hideViewHead = questionList.get(StringAction + Head);
            if (previousActionMap.get(StringAction).equalsIgnoreCase("N")) {
                hideView.setVisibility(View.VISIBLE);
                hideViewHead.setVisibility(View.VISIBLE);
            } else if (previousActionMap.get(StringAction).equalsIgnoreCase("Y")) {
                hideView.setVisibility(View.GONE);
                hideViewHead.setVisibility(View.GONE);
            }
        }
    }

    /**
     * is any value added to map
     */
   /* private boolean isFormModified() {
        return (mapProfileData == null || mapProfileData.isEmpty()) ? false : true;
    }*/

    /**
     * this method will return a json string for selected answer of a single field
     */
    public String getSelectedAnswerJsonObjectForAField(String getProfileParameterID,
                                                       String getProfileSubParameterID, String getLeadID, String value) {
        String json = "{";
        json += "\"ProfileParameterID\":" + getProfileParameterID;
        json += ",\"ProfileSubParameterID\":" + getProfileSubParameterID;
        json += ",\"LeadID\":" + getLeadID;
        if (value != null) {
            json += ",\"Value\":\"" + value + "\"";
        } else {
            json += ",\"Value\":" + value;
        }
        json += "}";
        return json;
    }

    /**
     * This method will a json string of all option selected on the form
     */
    private List<SaveLeadProfileRequest> getSelectedAnswersJson() {
        List<SaveLeadProfileRequest> saveLeadProfileRequestList = new ArrayList<>();
        for (View entry : formElementList) {
            if (entry instanceof EditText) {
                String mTag = entry.getTag().toString().trim();
                try {
                    String[] aarStr = mTag.split("_");
                    String valueStr = ((EditText) entry).getText().toString().trim();
                    saveLeadProfileRequestList.add(
                            new SaveLeadProfileRequest(aarStr[0], aarStr[1], leadId, valueStr));
                } catch (Exception e) {
                    Timber.d(e, TAG);
                }

            } else if (entry instanceof RadioGroup) {
                RadioGroup rgTemp = (RadioGroup) entry;
                for (int i = 0; i < rgTemp.getChildCount(); i++) {
                    RadioButton rbTemp = (RadioButton) rgTemp.getChildAt(i);
                    ProfileOption profileOption = (ProfileOption) rbTemp.getTag();
                    if (profileOption != null && rbTemp.isChecked()) {
                        String valueString = "";
                        if (!CRMAppUtil.isBlank(profileOption.getExtraInfo())
                                && profileOption.getExtraInfo().equalsIgnoreCase("Y")) {
                            valueString = getExtraView(
                                    profileOption);
                        }

                        saveLeadProfileRequestList.add(new SaveLeadProfileRequest(
                                profileOption.getProfileParameterID().toString(),
                                profileOption.getProfileSubParameterID().toString(), leadId,
                                valueString
                        ));
                    }
                }
            } else if (entry instanceof CheckBox) {
                ProfileOption profileOption = (ProfileOption) entry.getTag();
                CheckBox cbTemp = (CheckBox) entry;
                if (cbTemp.isChecked()) {
                    String valueString = "";
                    if (!CRMAppUtil.isBlank(profileOption.getExtraInfo())
                            && profileOption.getExtraInfo().equalsIgnoreCase("Y")) {
                        valueString = getExtraView(profileOption);
                    }
                    saveLeadProfileRequestList.add(new SaveLeadProfileRequest(
                            profileOption.getProfileParameterID().toString(),
                            profileOption.getProfileSubParameterID().toString(), leadId,
                            valueString
                    ));
                }
            } else if (entry instanceof Spinner) {
                Spinner spTemp = (Spinner) entry;
                ProfileOption opTion = (ProfileOption) spTemp.getSelectedItem();
                String valueString = "";

                if (!CRMAppUtil.isBlank(opTion.getExtraInfo()) && opTion.getExtraInfo()
                        .equalsIgnoreCase("Y")) {
                    valueString = getExtraView(opTion);
                }

                saveLeadProfileRequestList.add(new SaveLeadProfileRequest(
                        opTion.getProfileParameterID().toString(),
                        opTion.getProfileSubParameterID().toString(),
                        leadId,
                        valueString
                ));
            }
        }

        return saveLeadProfileRequestList;
    }

    private void renderSubmitProfile(UiModel<String> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        String data = uiModel.data;
        if (data != null) {
            MSFDialog.alertDialog(getActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    MOSLConstants.SET_USER_PROFILE, "OK", true,
                    (action, data1) -> {
                        if (action == MSFDialog.Action.OK) {
                            viewPager.setCurrentItem(
                                    MOSLConstants.LEAD_DETAIL, false);
                        }
                    });
        }
    }

    /**
     * Send selected answers to server
     */
    private void saveLeadProfileParameter(List<SaveLeadProfileRequest> request) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            Disposable subscribe = getSaathiApi().postLeadProfile(request)
                    .compose(applyUiModel())
                    .subscribe(this::renderSubmitProfile);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderProfileParameters(UiModel<List<LeadProfileItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<LeadProfileItem> dataList = uiModel.data;
        if (dataList != null && dataList.size() > 0) {
            try {
                mainProfileFormView.setVisibility(View.VISIBLE);
                fillProfileModal(dataList);
                renderForm();
            } catch (Exception e) {
                alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void getProfileQuestionAnswerFromServer() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LeadProfileRequest request = new LeadProfileRequest(leadId);
            Disposable subscribe = getSaathiApi().getLeadProfile(request)
                    .compose(applyUiModel())
                    .subscribe(this::renderProfileParameters);
        } else {
            noInternetConnectionDialog();
        }
    }
}