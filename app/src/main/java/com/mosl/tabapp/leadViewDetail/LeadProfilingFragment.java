package com.mosl.tabapp.leadViewDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.profile.ProfileModel;
import com.mosl.tabapp.model.profile.ProfileOption;
import com.mosl.tabapp.model.profile.ProfileQuestion;
import com.mosl.tabapp.model.profile.ProfileQuestionGroup;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vijay on 16/03/2017.
 */

public class LeadProfilingFragment extends MOSLCommonFragment implements View.OnClickListener, View.OnFocusChangeListener {
    public final static String TAG = LeadProfilingFragment.class.getSimpleName();

    private String leadId = "";
    private View leadProfilingView;
    private View mainProfileFormView;
    private TextView tvPlaceholder;
    private ViewPager viewPager;
    private ProfileModel profileModel;
    private LinearLayout mainProfileLinearLayout;
    // json tags

    private List<View> formElementList;
    //This list is added just to have reffrence of each question so if we can findthem in on click event of their siblings child
    private Map<String, View> questionList;
    private Map<String, String> previousActionMap;
    private Button btnSubmit, btnCancel;
    public static final int TEXT_BOX = 1;
    public static final int DROP_DOWN = 4;
    public static final int RADIO_BUTTON = 2;
    public static final int CHECK_BOX = 3;

    public static final String ANS_SEPERATER = "#";
    public static final String ANS_GROUP_SEPERATER = "|";
    public static final String Head = "_Head";


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadProfilingView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_lead_profiling);
        try {

            leadId = this.getArguments().getString(MOSLConstants.LEAD_ID, "");
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
        mainProfileLinearLayout = (LinearLayout) leadProfilingView.findViewById(R.id.mainProfileLayouttarget);
        btnCancel = (Button) leadProfilingView.findViewById(R.id.btnCancel);
        btnSubmit = (Button) leadProfilingView.findViewById(R.id.btnSubmit);
        tvPlaceholder = (TextView) leadProfilingView.findViewById(R.id.tvPlaceholder);
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
                String requesrtStr = getSelectedAnswersJson();
                if (requesrtStr.length() > 4) {//requesrt is more than []
                    saveLeadProfileParameter(requesrtStr);
                } else {
                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SEELECTION_METHOD, getResources().getString(android.R.string.ok), true, null);
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

    private void fillProfileModal(JSONArray Response) throws JSONException {

        profileModel = new ProfileModel();
        // adding groupname
        List<ProfileQuestionGroup> profileQuestionGroupList = new ArrayList<>();
        for (int responseI = 0; responseI < Response.length(); responseI++) {
            ProfileQuestionGroup tempProfileQuestionGroup = new ProfileQuestionGroup();
            JSONObject responseObj = Response.getJSONObject(responseI);
            String groupName = (responseObj.isNull(MOSLConstants.GROUPNAME) || responseObj.getString(MOSLConstants.GROUPNAME).equals("")) ? "" : responseObj.getString(MOSLConstants.GROUPNAME);
            tempProfileQuestionGroup.setGroupName(groupName);
            String aris = (responseObj.isNull(PROFILEPARAMETERID) || responseObj.getString(PROFILEPARAMETERID).equals("")) ? "" : responseObj.getString(PROFILEPARAMETERID);
            tempProfileQuestionGroup.setProfileParameterID(aris);

            boolean inApproval = responseObj.getBoolean(MOSLConstants.INAPPROVAL);
            tempProfileQuestionGroup.setInApproval(inApproval);

            int ProfileParameterGroupID = responseObj.getInt(MOSLConstants.PROFILEPARAMETERGROUPID);
            tempProfileQuestionGroup.setProfileParameterGroupID(ProfileParameterGroupID);

            int BusinessUnitId = responseObj.getInt(MOSLConstants.BUSINESS_UNIT_ID);
            tempProfileQuestionGroup.setBusinessUnitId(BusinessUnitId);

            boolean IsAdminMode = responseObj.getBoolean(MOSLConstants.ISADMINMODE);
            tempProfileQuestionGroup.setIsAdminMode(IsAdminMode);

            JSONArray ProfileParameterList = responseObj.getJSONArray(MOSLConstants.PROFILEPARAMETERLIST);
            List<ProfileQuestion> mProfileQuestionList = new ArrayList<>();
            for (int profileParameterListI = 0; profileParameterListI < ProfileParameterList.length(); profileParameterListI++) {
                JSONObject profileParameterListObj = ProfileParameterList.getJSONObject(profileParameterListI);
                ProfileQuestion tmpProfileQuestion = new ProfileQuestion();

                tmpProfileQuestion.setControlType(profileParameterListObj.getInt(ControlType));
                boolean ProfileParameterListInApproval = responseObj.getBoolean(MOSLConstants.INAPPROVAL);
                tmpProfileQuestion.setInApproval(ProfileParameterListInApproval);

                int ProfileParameterListProfileParameterGroupID = profileParameterListObj.getInt(MOSLConstants.PROFILEPARAMETERGROUPID);
                tmpProfileQuestion.setProfileParameterGroupID(ProfileParameterListProfileParameterGroupID);


                int QuestionType = profileParameterListObj.getInt(MOSLConstants.QUESTIONTYPE);
                tmpProfileQuestion.setQuestionType(QuestionType);
                int SelectedProfileSubParameterID = profileParameterListObj.getInt(MOSLConstants.SELECTEDPROFILESUBPARAMETERID);
                tmpProfileQuestion.setQuestionType(SelectedProfileSubParameterID);

                boolean ProfileParameterListIsAdminMode = profileParameterListObj.getBoolean(MOSLConstants.ISADMINMODE);
                tmpProfileQuestion.setIsAdminMode(ProfileParameterListIsAdminMode);

                String QuestionText = (profileParameterListObj.isNull(MOSLConstants.QUESTIONTEXT) || profileParameterListObj.getString(MOSLConstants.QUESTIONTEXT).equals("")) ? "" : profileParameterListObj.getString(MOSLConstants.QUESTIONTEXT);
                tmpProfileQuestion.setQuestionText(QuestionText);

                String YNStatus = (profileParameterListObj.isNull(MOSLConstants.IS_DEFAULTHIDDEN) || profileParameterListObj.getString(MOSLConstants.IS_DEFAULTHIDDEN).equals("")) ? "N" : profileParameterListObj.getString(MOSLConstants.IS_DEFAULTHIDDEN);
                tmpProfileQuestion.setDefaultHidden(YNStatus);

                int ProfileParameterListProfileParameterID = profileParameterListObj.getInt(MOSLConstants.PROFILEPARAMETERID);
                tmpProfileQuestion.setProfileParameterID(ProfileParameterListProfileParameterID);


                JSONArray ProfileSubParameterList = profileParameterListObj.getJSONArray(PROFILESUBPARAMETERLIST);
                List<ProfileOption> mProfileOptionList = new ArrayList<>();
                for (int profileSubParameterListI = 0; profileSubParameterListI < ProfileSubParameterList.length(); profileSubParameterListI++) {
                    JSONObject profileSubParameterListObj = ProfileSubParameterList.getJSONObject(profileSubParameterListI);
                    ProfileOption tempProfileOption = new ProfileOption();

                    tempProfileOption.setProfileParameterID(tmpProfileQuestion.getProfileParameterID());

                    boolean ProfileSubParameterListInApproval = profileSubParameterListObj.getBoolean(MOSLConstants.INAPPROVAL);
                    tempProfileOption.setInApproval(ProfileSubParameterListInApproval);

                    String value = (profileSubParameterListObj.isNull(VALUE) || profileSubParameterListObj.getString(VALUE).equals("")) ? "" : profileSubParameterListObj.getString(VALUE);
                    tempProfileOption.setValue(value);
                    tempProfileOption.setSelected(profileSubParameterListObj.getBoolean(IS_SELECTED));

                    int ProfileSubParameterID = profileSubParameterListObj.getInt(PROFILESUBPARAMETERID);
                    tempProfileOption.setProfileSubParameterID(ProfileSubParameterID);
                    tempProfileOption.setHideDepedentQuestion((profileSubParameterListObj.isNull(HIDEDEPEDENTQUESTION) || profileSubParameterListObj.getString(HIDEDEPEDENTQUESTION).equals("")) ? "" : profileSubParameterListObj.getString(HIDEDEPEDENTQUESTION));
                    String NoStr = (profileSubParameterListObj.isNull(IsHide) || profileSubParameterListObj.getString(IsHide).equals("")) ? "" : profileSubParameterListObj.getString(IsHide);
                    tempProfileOption.setIsHide(NoStr);

                    String str_QuestionSubText = (profileSubParameterListObj.isNull(QUESTIONSUBTEXT) || profileSubParameterListObj.getString(MOSLConstants.QUESTIONSUBTEXT).equals("")) ? "" : profileSubParameterListObj.getString(MOSLConstants.QUESTIONSUBTEXT);
                    tempProfileOption.setQuestionSubText(str_QuestionSubText);

                    boolean ProfileSubParameterListIsAdminMode = profileSubParameterListObj.getBoolean(MOSLConstants.ISADMINMODE);
                    tempProfileOption.setIsAdminMode(ProfileSubParameterListIsAdminMode);


                    //  if (!profileSubParameterListObj.isNull(EXTRAINFO)) {
                    tempProfileOption.setExtraInfo(profileSubParameterListObj.isNull(EXTRAINFO) ? "" : profileSubParameterListObj.getString(EXTRAINFO));
                    //   }
                    if (!profileSubParameterListObj.isNull(EXTRAINFOROWCOUNT)) {
                        tempProfileOption.setExtraInfoRowCount(profileSubParameterListObj.getInt(EXTRAINFOROWCOUNT));
                    }
                    // if (!profileSubParameterListObj.isNull(EXTRAINFOFIELDNAME)) {
                    tempProfileOption.setExtraInfoFieldName(profileSubParameterListObj.isNull(EXTRAINFOFIELDNAME) ? "" : profileSubParameterListObj.getString(EXTRAINFOFIELDNAME));
                    // }

                    mProfileOptionList.add(tempProfileOption);
                }
                tmpProfileQuestion.setProfileOptionList(mProfileOptionList);
                // add list to

                mProfileQuestionList.add(tmpProfileQuestion);
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
            String fieldNameArray[] = fieldNameMix.split("[" + ANS_GROUP_SEPERATER + "]");
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
                params.setMargins((int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp));
                llSegment.setLayoutParams(params);
                llSegment.setPadding((int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._3sdp));
                extraFieldLayout.addView(llSegment);
                for (int i = 0; i < fieldNameArray.length; i++) {

                    String label1 = fieldNameArray[i];

                    TextView extraTextView1 = new TextView(getActivity());
                    extraTextView1.setText(label1);
                    EditText extraEditText1 = new EditText(getActivity());
                    extraEditText1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    extraEditText1.setBackgroundResource(R.drawable.textview_border);
                    extraEditText1.setFilters(new InputFilter[]{CRMAppUtil.filterOnlyAlphabetAndNumbers});
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
     * @return
     **/
    private String getExtraView(ProfileOption profileOption) {
        String valueString = "";
        List<List<View>> extraViewList = profileOption.getExtraViewList();
        //   int columnCount = profileOption.getExtraInfoFieldName().split("[" + ANS_GROUP_SEPERATER + "]").length;
        if (extraViewList != null) {
            try {
                for (List<View> vList : extraViewList) {
                    for (View mView : vList) {
                        String mData = ((EditText) mView).getText().toString().trim();
                        if (mData.trim().length() < 1) {
                            valueString += " " + ANS_SEPERATER;
                        } else {
                            valueString += mData + ANS_SEPERATER;
                        }


                    }//remove last ##
                    valueString = valueString.substring(0, valueString.length() - ANS_SEPERATER.length());
                    valueString += ANS_GROUP_SEPERATER;

                }
                //remove last | symbol
                return valueString.substring(0, valueString.length() - ANS_GROUP_SEPERATER.length());
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

    private void renderForm() {
        if (profileModel == null) {
            return;
        }
        formElementList = new ArrayList<>();
        questionList = new HashMap<>();
        previousActionMap = new HashMap<>();

        mainProfileLinearLayout.removeAllViews();
        // this is just a flag to Set Open first group
        boolean mCoutFlagFirstGroup = false;
        for (ProfileQuestionGroup group : profileModel.getProfileQuestionGroup()) {
            String groupName = group.getGroupName();
            //  LinearLayout groupLayout = new LinearLayout(getActivity());
            ViewGroup groupLayout = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.include_section
                    , null);

            final TextView tvGroupName = (TextView) groupLayout.findViewById(R.id.tvSectionName);
            tvGroupName.setText(groupName);

            //question section start
            final LinearLayout llBasicProfileQuestionLayoutInner = (LinearLayout) groupLayout.findViewById(R.id.llSectionContent);


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
                tvQuestion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tvQuestion.setTextColor(getResources().getColor(R.color.light_black));
                final int padding = (int) getResources().getDimension(R.dimen._15sdp);
                tvQuestion.setPadding(padding, padding, padding, padding);
                tvQuestion.setText("Q " + questionNo + "." + question.getQuestionText());
                questionNo++;
                // tvQuestion.setTextSize(getResources().getDimension(R.dimen._10sdp));
                tvQuestion.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                tvQuestion.setBackgroundResource(R.drawable.profiling_question_background_text);


                llBasicProfileQuestionLayoutInner.addView(tvQuestion);
                questionList.put(question.getProfileParameterID().toString() + Head, tvQuestion);
                if (question.isDefaultHidden().equalsIgnoreCase("Y")) {
                    tvQuestion.setVisibility(View.GONE);
                }
                if (!question.getProfileOptionList().isEmpty()) {// && question.getProfileOptionList().size()<1
                    switch (question.getControlType()) {
                        case 0:
                            // ignore
                            break;
                        case TEXT_BOX:// question type edit text
                        {

                            LinearLayout llBasicProfileQuestionLayoutGroup = new LinearLayout(getActivity());
                            llBasicProfileQuestionLayoutGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            llBasicProfileQuestionLayoutGroup.setOrientation(LinearLayout.VERTICAL);
                            llBasicProfileQuestionLayoutGroup.setPadding(padding, padding, padding, padding);

                            for (ProfileOption mOptions : question.getProfileOptionList()) {
                                TextView tvHead = new TextView(getActivity());
                                tvHead.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                tvHead.setTextColor(getResources().getColor(R.color.light_black));
                                tvHead.setPadding(padding, 4, padding, 4);
                                tvHead.setText(mOptions.getQuestionSubText());
                                //  tvHead.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                                tvHead.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                                EditText mEditTextOption = new EditText(getActivity());
                                mEditTextOption.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                mEditTextOption.setBackgroundResource(R.drawable.textview_border);
                                mEditTextOption.setTag(question.getProfileParameterID() + "_" + mOptions.getProfileSubParameterID() + "_E");
                                mEditTextOption.animate();
                                mEditTextOption.setFilters(new InputFilter[]{CRMAppUtil.filterOnlyAlphabetAndNumbers});
                                mEditTextOption.setText(mOptions.getValue());
                                mEditTextOption.setHint(mOptions.getQuestionSubText());
                                tvHead.setText(mOptions.getQuestionSubText());
                                llBasicProfileQuestionLayoutGroup.addView(tvHead);
                                llBasicProfileQuestionLayoutGroup.addView(mEditTextOption);
                                //Adding to form List
                                formElementList.add(mEditTextOption);
                            }//for for options end
                            llBasicProfileQuestionLayoutGroup.setBackgroundResource(R.drawable.profiling_question_background_group);
                            llBasicProfileQuestionLayoutInner.addView(llBasicProfileQuestionLayoutGroup);
                            questionList.put(question.getProfileParameterID().toString(), llBasicProfileQuestionLayoutGroup);

                        }
                        break;
                        case RADIO_BUTTON:// question type is radio button
                        {
                            //start radio group
                            RadioGroup rbGroup = new RadioGroup(getActivity());
                            llBasicProfileQuestionLayoutInner.addView(rbGroup);
                            rbGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            rbGroup.setPadding(padding, padding, padding, padding);
                            rbGroup.setBackgroundResource(R.drawable.profiling_question_background_group);
                            rbGroup.setOrientation(RadioGroup.VERTICAL);
                            RadioButton rbOptionSelected = null;
                            ProfileOption previousAction = new ProfileOption();
                            //Adding linarlayout for info for extra
                            final LinearLayout extraFieldLayout = new LinearLayout(getActivity());
                            extraFieldLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            extraFieldLayout.setOrientation(LinearLayout.VERTICAL);
                            extraFieldLayout.setPadding(padding, padding, padding, padding);
                            extraFieldLayout.setVisibility(View.GONE);
                            //Adding linarlayout for info for extra end
                            llBasicProfileQuestionLayoutInner.addView(extraFieldLayout);
                            for (final ProfileOption mOptions : question.getProfileOptionList()) {
                                RadioButton rbOption = new RadioButton(getActivity());
                                rbOption.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                int mPadding = (int) getResources().getDimension(R.dimen._8sdp);
                                rbOption.setPadding(padding, mPadding, padding, mPadding);
                                // rbOption.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                                rbOption.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                                rbOption.setText(mOptions.getQuestionSubText());
                                //rbOption.setTag(question.getProfileParameterID() + "_" + mOptions.getProfileSubParameterID() + "_"+m);
                                rbOption.setTag(mOptions);
                                rbOption.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        extraFieldLayout.removeAllViews();
                                        extraFieldLayout.removeAllViewsInLayout();

                                        if (!CRMAppUtil.isBlank(mOptions.getExtraInfo()) && mOptions.getExtraInfo().equalsIgnoreCase("Y")) {
                                            setExtraView(mOptions, extraFieldLayout);

                                        } else {
                                            extraFieldLayout.removeAllViews();
                                            extraFieldLayout.setVisibility(View.GONE);
                                        }
                                        if (!CRMAppUtil.isBlank(mOptions.getHideDepedentQuestion())) {
                                            if (questionList.containsKey(mOptions.getHideDepedentQuestion())) {
                                                //questionList
                                                View hideView = questionList.get(mOptions.getHideDepedentQuestion());
                                                View hideViewHead = questionList.get(mOptions.getHideDepedentQuestion() + Head);
                                                if (mOptions.isHide().equalsIgnoreCase("N")) {
                                                    hideView.setVisibility(View.VISIBLE);
                                                    hideViewHead.setVisibility(View.VISIBLE);
                                                } else if (mOptions.isHide().equalsIgnoreCase("Y")) {
                                                    if(hideView instanceof  RadioGroup){
                                                        RadioGroup grp=  (RadioGroup) hideView;
                                                    int cnt=    grp.getChildCount();
                                                        for (int i = 0; i < cnt; i++) {
                                                          View Child=  grp.getChildAt(i);
                                                          if(Child instanceof  RadioButton){
                                                              RadioButton rbTemp=(RadioButton)Child;
                                                              rbTemp.setChecked(false);
                                                          }

                                                        }
                                                    }
                                                    hideView.setVisibility(View.GONE);
                                                    hideViewHead.setVisibility(View.GONE);
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
                                previousActionMap.put(previousAction.getHideDepedentQuestion(), previousAction.isHide());
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

                                cbOption.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                                int mPadding = (int) getResources().getDimension(R.dimen._8sdp);
                                cbOption.setPadding(mPadding, mPadding, mPadding, mPadding);
                                cbOption.setText(mOptions.getQuestionSubText());
                                // cbOption.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));
                                cbOption.setTextSize(getResources().getDimension(R.dimen.txt_size_profile));

                                //  cbOption.setTag(question.getProfileParameterID() + "_" + mOptions.getProfileSubParameterID() + "_CB");
                                cbOption.setTag(mOptions);
                                cbOption.setChecked(mOptions.isSelected());

                                //Adding to form List
                                formElementList.add(cbOption);
                                //Adding linarlayout for info for extra
                                final LinearLayout extraFieldLayout = new LinearLayout(getActivity());
                                extraFieldLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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

                                                if (!CRMAppUtil.isBlank(mOptions.getExtraInfo()) && mOptions.getExtraInfo().equalsIgnoreCase("Y")) {
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
                            ViewGroup childValue = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.include_spiner
                                    , null);
                            TextView tvHead = (TextView) childValue.findViewById(R.id.tvHead);
                            Spinner spSection = (Spinner) childValue.findViewById(R.id.spSection);
                            tvHead.setVisibility(View.GONE);

                            //   spSection.setTag(question.getProfileParameterID() + "_" + "+" + "_SP");
                            spSection.setTag(question.getProfileParameterID() + "_" + "+" + "_SP");
                            ArrayAdapter<ProfileOption> arrAdap = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, question.getProfileOptionList());
                            arrAdap.setDropDownViewResource(R.layout.mdropdown);


                            spSection.setAdapter(arrAdap);
                            formElementList.add(spSection);
                            llBasicProfileQuestionLayoutInner.addView(childValue);
                            int pos = question.getDropdownValueArrPosition();


                            //Adding linarlayout for info for extra
                            final LinearLayout extraFieldLayout = new LinearLayout(getActivity());
                            extraFieldLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            extraFieldLayout.setOrientation(LinearLayout.VERTICAL);
                            extraFieldLayout.setPadding(padding, padding, padding, padding);
                            extraFieldLayout.setVisibility(View.GONE);
                            //Adding linarlayout for info for extra end
                            spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    ProfileOption poP = (ProfileOption) parent.getItemAtPosition(position);


                                    extraFieldLayout.removeAllViews();
                                    extraFieldLayout.removeAllViewsInLayout();

                                    if (!CRMAppUtil.isBlank(poP.getExtraInfo()) && poP.getExtraInfo().equalsIgnoreCase("Y")) {
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
                questionBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30));
                questionBar.setBackgroundColor(getResources().getColor(R.color.white));
                llBasicProfileQuestionLayoutInner.addView(questionBar);
            }

            //question section end

            mainProfileLinearLayout.addView(groupLayout);
            View v = new View(getActivity());

            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
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
     *
     * @param getProfileParameterID
     * @param getProfileSubParameterID
     * @param getLeadID
     * @param value
     * @return
     */
    public String getSelectedAnswerJsonObjectForAField(String getProfileParameterID, String getProfileSubParameterID, String getLeadID, String value) {
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
     *
     * @return
     */
    private String getSelectedAnswersJson() {
        StringBuilder strBuilder = new StringBuilder(100);
        strBuilder.append('[');
        for (View entry : formElementList) {
            String strData = "{}";
            if (entry instanceof EditText) {
                String mTag = entry.getTag().toString().trim();
                String[] aarStr = mTag.split("_");
                String valueStr = ((EditText) entry).getText().toString().trim();
                strData = getSelectedAnswerJsonObjectForAField(aarStr[0], aarStr[1], leadId, valueStr);
                strBuilder.append(strData).append(',');
            } else if (entry instanceof RadioGroup) {
                RadioGroup rgTemp = (RadioGroup) entry;
                for (int i = 0; i < rgTemp.getChildCount(); i++) {
                    RadioButton rbTemp = (RadioButton) rgTemp.getChildAt(i);
                    //String mTag = rbTemp.getTag().toString().trim();
                    ProfileOption profileOption = (ProfileOption) rbTemp.getTag();
                    if (profileOption != null && rbTemp.isChecked()) {
                        String valueString = "";
                        if (!CRMAppUtil.isBlank(profileOption.getExtraInfo()) && profileOption.getExtraInfo().equalsIgnoreCase("Y")) {
                            valueString = getExtraView(profileOption);//valueString.substring(0, valueString.length() - ANS_GROUP_SEPERATER.length());
                        }
                        strData = getSelectedAnswerJsonObjectForAField(profileOption.getProfileParameterID().toString(),
                                profileOption.getProfileSubParameterID().toString(), leadId, valueString);

                        strBuilder.append(strData).append(',');
                    }
                }
            } else if (entry instanceof CheckBox) {
                ProfileOption profileOption = (ProfileOption) entry.getTag();
                CheckBox cbTemp = (CheckBox) entry;
                if (cbTemp.isChecked()) {
                    // strData = getSelectedAnswerJsonObjectForAField(profileOption.getProfileParameterID(), profileOption.getProfileSubParameterID(), leadId, "");
                    String valueString = "";
                    if (!CRMAppUtil.isBlank(profileOption.getExtraInfo()) && profileOption.getExtraInfo().equalsIgnoreCase("Y")) {
                        valueString = getExtraView(profileOption);
                    }
                    strData = getSelectedAnswerJsonObjectForAField(profileOption.getProfileParameterID().toString(),
                            profileOption.getProfileSubParameterID().toString(), leadId, valueString);
                    strBuilder.append(strData).append(',');
                }
            } else if (entry instanceof Spinner) {
//                String mTag = entry.getTag().toString().trim();
//                String[] aarStr = mTag.split("_");
                Spinner spTemp = (Spinner) entry;
                ProfileOption opTion = (ProfileOption) spTemp.getSelectedItem();
                //  strData = getSelectedAnswerJsonObjectForAField(aarStr[0], opTion.getProfileSubParameterID().toString(), leadId, "");
                String valueString = "";

                if (!CRMAppUtil.isBlank(opTion.getExtraInfo()) && opTion.getExtraInfo().equalsIgnoreCase("Y")) {
                    valueString = getExtraView(opTion);
                }
                strData = getSelectedAnswerJsonObjectForAField(opTion.getProfileParameterID().toString(),
                        opTion.getProfileSubParameterID().toString(), leadId, valueString);

                strBuilder.append(strData).append(',');
            }
        }
        if (strBuilder.length() > 2) {
            strBuilder.setLength(strBuilder.length() - 1);
        }
        strBuilder.append(']');
        return strBuilder.toString();
    }

    /**
     * Send selected answers to server
     */
    private void saveLeadProfileParameter(final String requestJson) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {

            // = getSelectedAnswersJson();
            String serviceMethod = "Lead/SaveLeadProfileParameter";
            showProgress();
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), requestJson, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                hideProgress();
                                String status = response.getString(MOSLConstants.STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    MSFDialog.alertDialog(getActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        MOSLConstants.SET_USER_PROFILE, "OK", true,
                                        new MSFDialog.DialogListener() {
                                        @Override
                                        public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                            if (action == MSFDialog.Action.OK) {
                                                viewPager.setCurrentItem(MOSLConstants.LEAD_DETAIL, false);
                                            }
                                        }
                                    });

                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), errorMsg,
                                        "OK", true, null);
                                }

                            } else {
                                hideProgress();
                            }
                        } catch (Exception e) {
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                MOSLConstants.SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            return;
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == MOSLConstants.UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    MOSLConstants.UNAUTHORIZE_USER, "OK", false,
                                    new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(errorMsg)) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                } else {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), errorMsg,
                                        "OK", true, null);
                                }

                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            MOSLConstants.SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            MOSLConstants.SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION,
                "OK", true, null);

        }
    }

    private void getProfileQuestionAnswerFromServer() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();

            String serviceMethod = "Lead/GetLeadProfileParameters";// + leadId;
            String tmpReq = String.format(ID_OBJECT, leadId);
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(MOSLConstants.STATUS);
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    if (response.getJSONArray(RESPONSE).length() > 0) {
                                        mainProfileFormView.setVisibility(View.VISIBLE);

                                        fillProfileModal(response.getJSONArray(RESPONSE));
                                        // fillProfileModal(new JSONArray(DummyString));
                                        renderForm();
                                    }
                                    hideProgress();
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), errorMsg,
                                        "OK", true, null);
                                    hideProgress();
                                    return;
                                }

                            } else {
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    MOSLConstants.SOME_SERVER_ERROR_OCCURRED, "Ok", true, null);
                                hideProgress();
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY, "Ok", true, null);
                            hideProgress();
                            return;

                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == MOSLConstants.UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    MOSLConstants.UNAUTHORIZE_USER, "OK", false,
                                    new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    MOSLConstants.SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            MOSLConstants.SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                        return;
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        MSFDialog.alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            MOSLConstants.SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                        return;
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
                return;
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION,
                "OK", true, null);
            return;
        }
    }


}