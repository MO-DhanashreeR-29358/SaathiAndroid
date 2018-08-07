package com.mosl.tabapp.leadViewDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.MSFStatistics;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.AddedAttendeeModel;
import com.mosl.tabapp.model.AddedSplAttendeeModel;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.MeetingApptModel;
import com.mosl.tabapp.model.SplAttendeeModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.mosl.tabapp.ws.WebServiceDirectCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;
import static com.mosl.tabapp.leadViewDetail.SetAppointmentFragment.listHasAttende;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 3/10/2017.
 */

public class UpdateMeetingFragment extends MOSLCommonFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    View createMeetingView;
    private TextView tvMeetingTime, tvMeetingDate, tvProduct;
    private ImageView ivLead, ivDatePicker;
    private TextView tvCurrentStageSince, tvCurrentStage, tvBranchCode, tvLeadId, tvClientCode, tvName,
            tvSplAttendeeCount, tvSplAttendee, tvAttendeeCount, tvAttendee, tvStartDate;
    View llSubcategory;
    private Spinner spProduct, spMeetingDuration, spActivityType, spMeetingReason, spActionables, spSubCategory, spCategory, spBAClient, spOutStaition;
    private EditText etCustomerComment, etMeetingDescription, etCategory, etSubCategory, etClientCode, etClientName;
    private Button btnSave, btnReset;
    private ImageButton ibSplAttendee, ibAttendee;
    private AttendeeAdapter attendeeAdapter;
    private SplAttendeeAdapter splAttendeeAdapter;
    private AddedAttendeeAdapter addedAttendeeAdapter;
    private AddedSplAttendeeAdapter addedSplAttendeeAdapter;
    ArrayList<EmployeeModel> employeeModelList;
    ArrayList<SplAttendeeModel> SplAttendeeModelList;
    ArrayList<AddedAttendeeModel> addedAttendeeModelList = new ArrayList<>();
    ArrayList<AddedSplAttendeeModel> addedSplAttendeeModelList = new ArrayList<>();
    int addedAttendeeCount;
    int addedSplAttendeeCount;
    private String mainFromDate, mainToDate, fromPostingDate, productCodeStr, productNameStr,
            meetingDurationCodeStr, meetingDurationNameStr, activityTypeCodeStr, activityTypeNameStr, meetingReasonCodeStr, meetingReasonNameStr,
            actionablesCodeStr, actionablesNameStr, categoryCodeStr, categoryNameStr, subCategoryCodestr, subCategoryNameStr, outStaitionNameStr, outStaitionCodeStr, baClientCodeStr, baClientNameStr;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;
    private DatePicker dpFromDate;
    private ImageView close;
    private Button btnSubmit;
    private ArrayList<RowData> arrSelectProduct, arrMeetingDuration, arrActivityType, arrMeetingReason,
            arrActionables, arrCategory, arrSubCategory, arrBAClient, arrOutStation;
    private ArrayAdapter<RowData> arrAdapSelectProduct, arrAdapMeetingDuration, arrAdapActivityType,
            arrAdapMeetingReason, arrAdapActionables, arrAdapCategory, arrAdapSubCategory, arrAdapBAClient, arrAdapOutStation;
    private String leadId, meetingId, SubCategoryID, ProductTypeID, ActivityTypeID, MeetingReasonID,
            MeetingDurationID, ActionableID, CategoryID, MeetingDate;
    private int currentYear, month, day, hour, minute;
    private Calendar calendar = Calendar.getInstance();
    private TextView tvProductError, tvMeetingDurationError, tvMeetingReasonError, tvActivityTypeError, tvActionablesError, tvMeetingDescriptionError, tvBAClientError, tvOutStaitionError, tvClientCodeError, tvClientNameError, tvCategoryError, tvCategoryCommentsError, tvSubCategoryError, tvSubCategoryCommentsError;
    private RelativeLayout rlDate;
    private LinearLayout linaerLayoutNew, llActionables;
    private boolean profileType;
    private String mUCID = "";
    private String meetingObjectiveIdStr = "0";
    private ArrayList<RowData> arrMeetingObjective;
    private ArrayAdapter<RowData> arrAdapMeetingsObjective;
    private Spinner spMeetingObjective;
    private TextView tvMeetingObjectiveError;
    private ScrollView scrollView;
    //this says if those are below
    private boolean mandatory = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createMeetingView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_update_meeting);
        meetingId = this.getArguments().getString(MEETING, "0");
        getMeetingById(meetingId);
        setAppTitle(getClass().toString(), "Update Meeting");
        meetingObjective();
        setUpView(createMeetingView);
        return createMeetingView;
    }


    private void setUpView(View createMeetingView) {
        scrollView = (ScrollView) createMeetingView.findViewById(R.id.scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        rlDate = (RelativeLayout) createMeetingView.findViewById(R.id.rlDate);
        tvStartDate = (TextView) createMeetingView.findViewById(R.id.tvDate);
       /* mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        mainFromDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        // tvStartDate.setText(CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy"));
        tvStartDate.setText(CRMAppUtil.getCurrentDBDate());*/
        ivDatePicker = (ImageView) createMeetingView.findViewById(R.id.ivDatePicker);
        rlDate.setOnClickListener(this);

        tvSplAttendeeCount = (TextView) createMeetingView.findViewById(R.id.tvSplAttendeeCount);
        tvSplAttendee = (TextView) createMeetingView.findViewById(R.id.tvSplAttendee);
        tvAttendeeCount = (TextView) createMeetingView.findViewById(R.id.tvAttendeeCount);
        tvAttendee = (TextView) createMeetingView.findViewById(R.id.tvAttendee);
        llSubcategory = createMeetingView.findViewById(R.id.llSubcategory);
        tvMeetingTime = (TextView) createMeetingView.findViewById(R.id.tvMeetingTime);
        tvMeetingDate = (TextView) createMeetingView.findViewById(R.id.tvMeetingDate);
        tvProduct = (TextView) createMeetingView.findViewById(R.id.tvProduct);
        tvMeetingObjectiveError = (TextView) createMeetingView.findViewById(R.id.tvMeetingObjectiveError);
        tvProductError = (TextView) createMeetingView.findViewById(R.id.tvProductError);
        tvMeetingDurationError = (TextView) createMeetingView.findViewById(R.id.tvMeetingDurationError);
        tvMeetingReasonError = (TextView) createMeetingView.findViewById(R.id.tvMeetingReasonError);
        tvActivityTypeError = (TextView) createMeetingView.findViewById(R.id.tvActivityTypeError);
        tvActionablesError = (TextView) createMeetingView.findViewById(R.id.tvActionablesError);
        tvMeetingDescriptionError = (TextView) createMeetingView.findViewById(R.id.tvMeetingDescriptionError);
        tvCategoryError = (TextView) createMeetingView.findViewById(R.id.tvCategoryError);
        tvSubCategoryError = (TextView) createMeetingView.findViewById(R.id.tvSubCategoryError);
        tvSubCategoryCommentsError = (TextView) createMeetingView.findViewById(R.id.tvSubCategoryCommentsError);
        tvCategoryCommentsError = (TextView) createMeetingView.findViewById(R.id.tvCategoryCommentsError);

        btnSave = (Button) createMeetingView.findViewById(R.id.btnSave);
        btnReset = (Button) createMeetingView.findViewById(R.id.btnReset);
        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        tvBAClientError = (TextView) createMeetingView.findViewById(R.id.tvBAClientError);
        tvOutStaitionError = (TextView) createMeetingView.findViewById(R.id.tvOutStaitionError);
        tvClientCodeError = (TextView) createMeetingView.findViewById(R.id.tvClientCodeError);
        tvClientNameError = (TextView) createMeetingView.findViewById(R.id.tvClientNameError);

        ibSplAttendee = (ImageButton) createMeetingView.findViewById(R.id.ibSplAttendee);
        ibAttendee = (ImageButton) createMeetingView.findViewById(R.id.ibAttendee);

        etCustomerComment = (EditText) createMeetingView.findViewById(R.id.etCustomerComment);
        etCustomerComment.setFilters(new InputFilter[]{filterOnlyAlphabetAndNumbersAnsAllowedChar});
        etMeetingDescription = (EditText) createMeetingView.findViewById(R.id.etMeetingDescription);
        etMeetingDescription.setFilters(new InputFilter[]{filterOnlyAlphabetAndNumbersAnsAllowedChar});
        etCategory = (EditText) createMeetingView.findViewById(R.id.etCategory);
        etCategory.setFilters(new InputFilter[]{filterOnlyAlphabetAndNumbersAnsAllowedChar});
        etSubCategory = (EditText) createMeetingView.findViewById(R.id.etSubCategory);
        etSubCategory.setFilters(new InputFilter[]{filterOnlyAlphabetAndNumbersAnsAllowedChar});
        etClientCode = (EditText) createMeetingView.findViewById(R.id.etClientCode);
        etClientName = (EditText) createMeetingView.findViewById(R.id.etClientName);

      /*  etMeetingDescription.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etMeetingDescription.setOnEditorActionListener(this);
        etCustomerComment.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etCustomerComment.setOnEditorActionListener(this);
        etCategory.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etCategory.setOnEditorActionListener(this);*/

        spProduct = (Spinner) createMeetingView.findViewById(R.id.spProduct);
        spMeetingDuration = (Spinner) createMeetingView.findViewById(R.id.spMeetingDuration);
        spActivityType = (Spinner) createMeetingView.findViewById(R.id.spActivityType);
        spMeetingReason = (Spinner) createMeetingView.findViewById(R.id.spMeetingReason);
        spActionables = (Spinner) createMeetingView.findViewById(R.id.spActionables);
        spCategory = (Spinner) createMeetingView.findViewById(R.id.spCategory);
        spSubCategory = (Spinner) createMeetingView.findViewById(R.id.spSubCategory);
        spMeetingObjective = (Spinner) createMeetingView.findViewById(R.id.spMeetingObjective);
        spBAClient = (Spinner) createMeetingView.findViewById(R.id.spBAClient);
        spOutStaition = (Spinner) createMeetingView.findViewById(R.id.spOutStaition);
        linaerLayoutNew = (LinearLayout) createMeetingView.findViewById(R.id.linaerLayoutNew);
        llActionables = (LinearLayout) createMeetingView.findViewById(R.id.llActionables);
        ibAttendee.setOnClickListener(this);
        ibSplAttendee.setOnClickListener(this);
        tvAttendeeCount.setOnClickListener(this);
        tvSplAttendeeCount.setOnClickListener(this);
        try {
            leadId = this.getArguments().getString(LEAD_ID, "0");
            tvProduct.setText("Appointments" + "-" + this.getArguments().getString(PRODUCT));
            tvMeetingDate.setText(this.getArguments().getString(DATE));
            tvMeetingTime.setText("Time" + "-" + this.getArguments().getString(TIME));
            mUCID = this.getArguments().getString(UCID, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        AddedAttendeeModel.setAttendeeUserEmplyeeCode(AccountDetails.getEmployeeCode());
        //  selectProduct(null);
        if (AccountDetails.getProfileType().equalsIgnoreCase(FRANCHIES) || AccountDetails.getProfileType().equalsIgnoreCase(IFA)) {
            linaerLayoutNew.setVisibility(View.VISIBLE);
            profileType = true;
        }
        etClientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  String newStr = CRMValidationRule.trimFull(etName.getText().toString());
                if (!isBlank(etClientName.getText().toString().trim())) {
                    tvClientNameError.setVisibility(View.GONE);
                } else {
                    tvClientNameError.setVisibility(View.GONE);
                    tvClientNameError.setText("Enter client name");
                }
            }
        });

        etClientCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  String newStr = CRMValidationRule.trimFull(etName.getText().toString());
                if (!isBlank(etClientCode.getText().toString().trim())) {
                    tvClientCodeError.setVisibility(View.GONE);
                } else {
                    tvClientCodeError.setVisibility(View.GONE);
                    tvClientCodeError.setText("Enter client code");
                }
            }
        });

        etMeetingDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  String newStr = CRMValidationRule.trimFull(etName.getText().toString());
                if (!isBlank(etMeetingDescription.getText().toString().trim())) {
                    if (etMeetingDescription.getText().length() >= 10) {
                        tvMeetingDescriptionError.setVisibility(View.GONE);
                    } else {
                        tvMeetingDescriptionError.setVisibility(View.VISIBLE);
                        tvMeetingDescriptionError.setText(ATLEAST_TEN_CHARACTERS);
                    }
                } else {
                    tvMeetingDescriptionError.setVisibility(View.GONE);
                }
            }
        });

        etSubCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  String newStr = CRMValidationRule.trimFull(etName.getText().toString());
                if (!isBlank(etSubCategory.getText().toString().trim())) {
                    if (etSubCategory.getText().length() >= 10) {
                        tvSubCategoryCommentsError.setVisibility(View.GONE);
                    } else {
                        tvSubCategoryCommentsError.setVisibility(View.VISIBLE);
                        tvSubCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
                    }
                } else {
                    tvSubCategoryCommentsError.setVisibility(View.GONE);
                }
            }
        });

        etCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  String newStr = CRMValidationRule.trimFull(etName.getText().toString());

                if (!isBlank(etCategory.getText().toString().trim())) {
                    if (etCategory.getText().length() >= 10) {
                        tvCategoryCommentsError.setVisibility(View.GONE);
                    } else {
                        tvCategoryCommentsError.setVisibility(View.VISIBLE);
                        tvCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
                    }
                } else {
                    tvCategoryCommentsError.setVisibility(View.GONE);
                }
            }
        });
    }

    private AdapterView.OnItemSelectedListener selectCategoryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                categoryCodeStr = ((RowData) spCategory.getSelectedItem()).getKey();
                categoryNameStr = ((RowData) spCategory.getSelectedItem()).getValue();
                tvCategoryError.setVisibility(View.GONE);
                subCategory(SubCategoryID);
//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener selectSubCategoryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                subCategoryCodestr = ((RowData) spSubCategory.getSelectedItem()).getKey();
                subCategoryNameStr = ((RowData) spSubCategory.getSelectedItem()).getValue();
                tvSubCategoryError.setVisibility(View.GONE);
//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener selectProductListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                productCodeStr = ((RowData) spProduct.getSelectedItem()).getKey();
                productNameStr = ((RowData) spProduct.getSelectedItem()).getValue();
                tvProductError.setVisibility(View.GONE);
//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener selectMeetingDurationListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                meetingDurationCodeStr = ((RowData) spMeetingDuration.getSelectedItem()).getKey();
                meetingDurationNameStr = ((RowData) spMeetingDuration.getSelectedItem()).getValue();
                tvMeetingDurationError.setVisibility(View.GONE);

//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener selectActivityTypeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                activityTypeCodeStr = ((RowData) spActivityType.getSelectedItem()).getKey();
                activityTypeNameStr = ((RowData) spActivityType.getSelectedItem()).getValue();
                tvActivityTypeError.setVisibility(View.GONE);
                if (!spActivityType.getSelectedItem().toString().equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
                    meetingReason(MeetingReasonID);
                }

//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener selectMeetingReasonListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                meetingReasonCodeStr = ((RowData) spMeetingReason.getSelectedItem()).getKey();
                meetingReasonNameStr = ((RowData) spMeetingReason.getSelectedItem()).getValue();
                tvMeetingReasonError.setVisibility(View.GONE);
                if (meetingReasonNameStr.equalsIgnoreCase("Customer Service Related")) {
                    llActionables.setVisibility(View.VISIBLE);
                } else {
                    llActionables.setVisibility(View.GONE);
                }
//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener selectActionablesListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                actionablesCodeStr = ((RowData) spActionables.getSelectedItem()).getKey();
                actionablesNameStr = ((RowData) spActionables.getSelectedItem()).getValue();
                tvActionablesError.setVisibility(View.GONE);
//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener selectBAClientListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                baClientCodeStr = ((RowData) spBAClient.getSelectedItem()).getKey();
                baClientNameStr = ((RowData) spBAClient.getSelectedItem()).getValue();
                tvBAClientError.setVisibility(View.GONE);
//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener selectOutStationListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                outStaitionCodeStr = ((RowData) spOutStaition.getSelectedItem()).getKey();
                outStaitionNameStr = ((RowData) spOutStaition.getSelectedItem()).getValue();
                tvOutStaitionError.setVisibility(View.GONE);
//                productType(interestInCodeStr);
//                etPermStateError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public void onClick(View view) {
        int id = view.getId();
        try {
            if (id == R.id.ibAttendee) {
                dialogAttendee();
            } else if (id == R.id.ibSplAttendee) {
                dialogSplAttendee();
            } else if (id == R.id.tvAttendeeCount) {
                dialogAddedAttendee();
            } else if (id == R.id.tvSplAttendeeCount) {
                dialogAddedSplAttendee();
            } else if (id == R.id.rlDate) {
                // selectDate();
            } else if (id == R.id.btnReset) {
                resetData();
            } else if (id == R.id.btnSave) {

                if (validateForm()) {
                    MeetingApptModel meetingApptModel = new MeetingApptModel();
                    meetingApptModel.setMeetingType("M");
                    meetingApptModel.setDisplayType("U");
                    meetingApptModel.setLeadId(leadId);
                    meetingApptModel.setUcid(mUCID);
                    meetingApptModel.setMeetingID(meetingId);
                    meetingApptModel.setMeetingDate(CRMAppUtil.getDBDate(currentYear, month, day, hour, minute));
                    meetingApptModel.setProduct(productCodeStr);
                    meetingApptModel.setMeetingDuration(meetingDurationCodeStr);
                    meetingApptModel.setMeetingObjectiveID(meetingObjectiveIdStr);
                    meetingApptModel.setActivityTypeID(activityTypeCodeStr);
                    meetingApptModel.setMeetingReason(meetingReasonCodeStr);
                    meetingApptModel.setMeetingDes(getJsonSafeString(etMeetingDescription.getText().toString().trim()));
                    meetingApptModel.setMeetingAttendee(addedAttendeeModelList);
                    meetingApptModel.setSplMeetingAttendee(addedSplAttendeeModelList);
                    meetingApptModel.setCustomerComment(getJsonSafeString(etCustomerComment.getText().toString().trim()));
                    meetingApptModel.setActionables(actionablesCodeStr);
                    meetingApptModel.setCategory(categoryCodeStr);
                    meetingApptModel.setEtCategory(etCategory.getText().toString().trim());
                    meetingApptModel.setSubCategory(subCategoryCodestr);
                    meetingApptModel.setEtSubCategory(etSubCategory.getText().toString().trim());
                    meetingApptModel.setBaClient(baClientCodeStr);
                    meetingApptModel.setOutStation(outStaitionCodeStr);
                    meetingApptModel.setClientCode(etClientCode.getText().toString().trim());
                    meetingApptModel.setClientName(etClientName.getText().toString().trim());
                    updateMeetingSave(meetingApptModel);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetData() {
        addedAttendeeModelList.clear();
        addedSplAttendeeModelList.clear();
        tvAttendeeCount.setText("");
        tvSplAttendeeCount.setText("");
        tvStartDate.setText(MeetingDate);
        spProduct.setSelection(0);
        spMeetingDuration.setSelection(0);
        spActivityType.setSelection(0);
        spMeetingReason.setSelection(0);
        etMeetingDescription.setText("");
        etCustomerComment.setText("");
        spActionables.setSelection(0);
        spCategory.setSelection(0);
        etCategory.setText("");
        spSubCategory.setSelection(0);
        etSubCategory.setText("");
        if (profileType) {
            etClientName.setText("");
            etClientCode.setText("");
            spOutStaition.setSelection(0);
            spBAClient.setSelection(0);
        }
    }

    /**
     * form validation
     *
     * @param
     * @return
     */
    private boolean validateForm() {
        if (spProduct.getSelectedItem().toString().equalsIgnoreCase(SELECT_PRODUCT)) {
            tvProductError.setVisibility(View.VISIBLE);
            tvProductError.setText(SELECT_PRODUCT);
            tvProductError.requestFocus();
            return false;
        } else if (spMeetingDuration.getSelectedItem().toString().equalsIgnoreCase(SELECT_MEETING_DURATION)) {
            tvMeetingDurationError.setVisibility(View.VISIBLE);
            tvMeetingDurationError.setText(SELECT_MEETING_DURATION);
            tvMeetingDurationError.requestFocus();
            return false;
        } else if (spMeetingObjective.getSelectedItem().toString().equalsIgnoreCase(SELECT_MEETING_OBJECTIVE)) {
            tvMeetingObjectiveError.setVisibility(View.VISIBLE);
            tvMeetingObjectiveError.setText(SELECT_MEETING_OBJECTIVE);
            tvMeetingObjectiveError.requestFocus();
            return false;
        } else if (spActivityType.getSelectedItem().toString().equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
            tvActivityTypeError.setVisibility(View.VISIBLE);
            tvActivityTypeError.setText(SELECT_ACTIVITY_TYPE);
            tvActivityTypeError.requestFocus();
            return false;
        } else if (spMeetingReason.getSelectedItem().toString().equalsIgnoreCase(SELECT_MEETING_REASON)) {
            tvMeetingReasonError.setVisibility(View.VISIBLE);
            tvMeetingReasonError.setText(SELECT_MEETING_REASON);
            tvMeetingReasonError.requestFocus();
            return false;
        } else if (etMeetingDescription.getText().toString().trim().isEmpty()) {
            tvMeetingDescriptionError.setVisibility(View.VISIBLE);
            tvMeetingDescriptionError.setText(MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK);
            tvMeetingDescriptionError.requestFocus();
            return false;
        } else if (etMeetingDescription.getText().length() <= 10) {
            tvMeetingDescriptionError.setVisibility(View.VISIBLE);
            tvMeetingDescriptionError.setText(ATLEAST_TEN_CHARACTERS);
            tvMeetingDescriptionError.requestFocus();
            return false;
        } else if (profileType && spBAClient.getSelectedItem().toString().equalsIgnoreCase(SELECT_BA_CLIENT_MEETING)) {
            tvBAClientError.setVisibility(View.VISIBLE);
            tvBAClientError.setText(SELECT_BA_CLIENT_MEETING);
            tvBAClientError.requestFocus();
            return false;
        } else if (profileType && spOutStaition.getSelectedItem().toString().equalsIgnoreCase(SELECT_OUT_STATION_MEETING)) {
            tvOutStaitionError.setVisibility(View.VISIBLE);
            tvOutStaitionError.setText(SELECT_OUT_STATION_MEETING);
            tvOutStaitionError.requestFocus();
            return false;
        } else if (profileType && isBlank(etClientCode.getText().toString().trim())) {
            tvClientCodeError.setText("Enter client code");
            tvClientCodeError.setVisibility(View.VISIBLE);
            tvClientCodeError.requestFocus();
            return false;
        } else if (profileType && isBlank(etClientName.getText().toString().trim())) {
            tvClientNameError.setText("Enter client name");
            tvClientNameError.setVisibility(View.VISIBLE);
            tvClientNameError.requestFocus();
            return false;
        } else if ((llActionables.getVisibility() == View.VISIBLE) &&
                spActionables.getSelectedItem().toString().equalsIgnoreCase(SELECT_ACTIONABLES)) {
            tvActionablesError.setVisibility(View.VISIBLE);
            tvActionablesError.setText(SELECT_ACTIONABLES);
            tvActionablesError.requestFocus();
            return false;
        } else if ((llActionables.getVisibility() == View.VISIBLE) &&
                spCategory.getSelectedItem().toString().equalsIgnoreCase(SELECT_CATEGORY)) {
            tvCategoryError.setVisibility(View.VISIBLE);
            tvCategoryError.setText(SELECT_CATEGORY);
            tvCategoryError.requestFocus();
            return false;
        } else if ((llActionables.getVisibility() == View.VISIBLE) &&
                etCategory.getText().length() <= 10) {
            tvCategoryCommentsError.setVisibility(View.VISIBLE);
            tvCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
            tvCategoryCommentsError.requestFocus();
            return false;
        } else if (mandatory && (llActionables.getVisibility() == View.VISIBLE) &&
                spSubCategory.getSelectedItem().toString().equalsIgnoreCase(SELECT_SUB_CATEGORY)) {
            tvSubCategoryError.setVisibility(View.VISIBLE);
            tvSubCategoryError.setText(SELECT_SUB_CATEGORY);
            tvSubCategoryError.requestFocus();
            return false;
        } else if (mandatory && (llActionables.getVisibility() == View.VISIBLE) && etSubCategory.getText().length() <= 10) {
            tvSubCategoryCommentsError.setVisibility(View.VISIBLE);
            tvSubCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
            tvSubCategoryCommentsError.requestFocus();
            return false;
        }
        return true;
    }

    public void selectDate() {
        today = Calendar.getInstance();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDate = (DatePicker) dialog.findViewById(R.id.datePickerFromDate);

        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        //  dpToDate = (DatePicker) dialog.findViewById(R.id.datePickerToDate);
        close = (ImageView) dialog.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);

        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -3);
        dpFromDate.setMaxDate(System.currentTimeMillis());
        dpFromDate.setMinDate(cal.getTimeInMillis());

        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    currentYear = year;
                    month = monthOfYear + 1;
                    day = dayOfMonth;
                }
            });
        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    currentYear = year;
                    month = monthOfYear + 1;
                    day = dayOfMonth;
                }
            });
        }

        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                hour = arg1;
                minute = arg2;
            }
        });

       /* if (mainFromDate != null) {
            String[] dateSplit = mainToDate.split("-");
            dpToDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                    mainToDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);

                }
            });
        } else {
            dpToDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    today.set(year, monthOfYear, dayOfMonth);
                    long dayy = today.getActualMaximum(Calendar.DATE);
                    mainToDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);


                }
            });
        }*/

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                    toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                    if (fromDat.equals(toDate)) {
                        if (hour > calendar.get(Calendar.HOUR_OF_DAY)) {
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), "Invalid time", "OK", true, null);
                        } else if (minute > calendar.get(Calendar.MINUTE)) {
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), "Invalid time", "OK", true, null);
                        } else {
                            fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                            toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                            if (mainFromDate != null) {
                                fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                                fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
                                // tvStartDate.setText(fromPostingDate + " " + hour + ":" + minute + ":" + "00");
                                tvStartDate.setText(CRMAppUtil.getDBDate(CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute), DATE_FORMAT_TO_DISPLAY));
                            }
                            dialog.dismiss();
                        }
                    } else {
                        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                        if (mainFromDate != null) {
                            fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                            fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                            //  tvStartDate.setText(fromPostingDate + " " + hour + ":" + minute + ":" + "00");
                            tvStartDate.setText(CRMAppUtil.getDBDate(CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute), DATE_FORMAT_TO_DISPLAY));
                        }
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        dialog.show();
    }

    public String prefix(int input) {
        if (Integer.toString(input).length() == 1) {
            return "0" + input;
        }
        return input + "";
    }

    private void getMeetingById(String meetingId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
//            customProgress.setVisibility(View.VISIBLE);
            showProgress();
            //          Map inputMap = new HashMap<>();
            // inputMap.put("id", meetingId);


            String serviceMethod = "Meeting/GetMeetingByID";
            String tmpReq = String.format(ID_OBJECT, meetingId);

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrCategory = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
//                                    customProgress.setVisibility(View.GONE);
                                    JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                      /*  "Response": {
                                        "MeetingID": 3007,
                                                "LeadId": 6028,
                                                "MeetingDate": "2017-03-30T18:29:00",
                                                "ActivityTypeID": 76,
                                                "ProductTypeID": 21,
                                                "MeetingReasonID": 0,
                                                "MeetingDurationID": 0,
                                                "MeetingDescription": null,
                                                "BAClientMeetingID": 0,
                                                "OutstationMeetingID": 0,
                                                "Clientcode": null,
                                                "Clientname": null,
                                                "MeetingObjectiveID": 0,
                                                "ProductCategoryID": 0,
                                                "CustomerRemark": null,
                                                "ActionableID": 0,
                                                "CategoryID": 0,
                                                "CategoryDescription": null,
                                                "SubCategoryID": 0,
                                                "SubCategoryDescription": null,
                                                "MeetingWith": null,
                                                "AttendeeCode": null,
                                                "AttendeeName": null,
                                                "repeatOnCode": null,
                                                "repeatEveryWeekCode": null,
                                                "repeatEveryDayCode": null,
                                                "repeatationStartOn": null,
                                                "repeatationEndsOn": null,
                                                "MeetingType": "A",
                                                "ParentID": null,
                                                "meetingAttendee": [
                                        {
                                            "MeetingAttendeeID": 3010,
                                                "MeetingID": 3007,
                                                "AttendeeEmpcode": "mukesh",
                                                "AttendeeType": "R",
                                                "Attended": true,
                                                "IsAdminMode": true,
                                                "Status": 0,
                                                "InApproval": false,
                                                "ApprovalFieldList": null
                                        }
                                        ],
                                       */

                                    MeetingDate = (jsonObject.isNull("MeetingDate") || jsonObject.getString("MeetingDate").equals("")) ? "" : jsonObject.getString("MeetingDate");
                                    ActivityTypeID = (jsonObject.isNull("ActivityTypeID") || jsonObject.getString("ActivityTypeID").equals("")) ? "" : jsonObject.getString("ActivityTypeID");
                                    ProductTypeID = (jsonObject.isNull("ProductTypeID") || jsonObject.getString("ProductTypeID").equals("")) ? "" : jsonObject.getString("ProductTypeID");
                                    MeetingReasonID = (jsonObject.isNull("MeetingReasonID") || jsonObject.getString("MeetingReasonID").equals("")) ? "" : jsonObject.getString("MeetingReasonID");
                                    MeetingDurationID = (jsonObject.isNull("MeetingDurationID") || jsonObject.getString("MeetingDurationID").equals("")) ? "" : jsonObject.getString("MeetingDurationID");
                                    String MeetingDescription = (jsonObject.isNull("MeetingDescription") || jsonObject.getString("MeetingDescription").equals("")) ? "" : jsonObject.getString("MeetingDescription");
                                    String Clientcode = (jsonObject.isNull("Clientcode") || jsonObject.getString("Clientcode").equals("")) ? "" : jsonObject.getString("Clientcode");
                                    String Clientname = (jsonObject.isNull("Clientname") || jsonObject.getString("Clientname").equals("")) ? "" : jsonObject.getString("Clientname");
                                    String MeetingObjectiveID = (jsonObject.isNull("MeetingObjectiveID") || jsonObject.getString("MeetingObjectiveID").equals("")) ? "" : jsonObject.getString("MeetingObjectiveID");
                                    String ProductCategoryID = (jsonObject.isNull("ProductCategoryID") || jsonObject.getString("ProductCategoryID").equals("")) ? "" : jsonObject.getString("ProductCategoryID");
                                    String CustomerRemark = (jsonObject.isNull("CustomerRemark") || jsonObject.getString("CustomerRemark").equals("")) ? "" : jsonObject.getString("CustomerRemark");
                                    ActionableID = (jsonObject.isNull("ActionableID") || jsonObject.getString("ActionableID").equals("")) ? "" : jsonObject.getString("ActionableID");
                                    CategoryID = (jsonObject.isNull("CategoryID") || jsonObject.getString("CategoryID").equals("")) ? "" : jsonObject.getString("CategoryID");
                                    String CategoryDescription = (jsonObject.isNull("CategoryDescription") || jsonObject.getString("CategoryDescription").equals("")) ? "" : jsonObject.getString("CategoryDescription");
                                    SubCategoryID = (jsonObject.isNull("SubCategoryID") || jsonObject.getString("SubCategoryID").equals("")) ? "" : jsonObject.getString("SubCategoryID");
                                    String SubCategoryDescription = (jsonObject.isNull("SubCategoryDescription") || jsonObject.getString("SubCategoryDescription").equals("")) ? "" : jsonObject.getString("SubCategoryDescription");
                                    String ParentID = (jsonObject.isNull("ParentID") || jsonObject.getString("ParentID").equals("")) ? "" : jsonObject.getString("ParentID");
                                   /* "meetingAttendee": [
                                    {
                                        "MeetingAttendeeID": 3010,
                                            "MeetingID": 3007,
                                            "AttendeeEmpcode": "mukesh",
                                            "AttendeeType": "R",
                                            "Attended": true,
                                            "IsAdminMode": true,
                                            "Status": 0,
                                            "InApproval": false,
                                            "ApprovalFieldList": null
                                    }
                                    ]*/
                                    JSONArray jsonArray = jsonObject.getJSONArray("meetingAttendee");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject attendeeJsonObject = jsonArray.getJSONObject(i);

                                        String MeetingAttendeeID = (attendeeJsonObject.isNull("MeetingAttendeeID") || attendeeJsonObject.getString("MeetingAttendeeID").equals("")) ? "" : attendeeJsonObject.getString("MeetingAttendeeID");
                                        String MeetingID = (attendeeJsonObject.isNull("MeetingAttendeeID") || attendeeJsonObject.getString("MeetingAttendeeID").equals("")) ? "" : attendeeJsonObject.getString("MeetingAttendeeID");
                                        String AttendeeEmpcode = (attendeeJsonObject.isNull("AttendeeEmpcode") || attendeeJsonObject.getString("AttendeeEmpcode").equals("")) ? "" : attendeeJsonObject.getString("AttendeeEmpcode");
                                        String AttendeeType = (attendeeJsonObject.isNull("AttendeeType") || attendeeJsonObject.getString("AttendeeType").equals("")) ? "" : attendeeJsonObject.getString("AttendeeType");
                                        String Attended = (attendeeJsonObject.isNull("Attended") || attendeeJsonObject.getString("Attended").equals("")) ? "" : attendeeJsonObject.getString("Attended");
                                        String DefaultAttendee = (attendeeJsonObject.isNull("DefaultAttendee") || attendeeJsonObject.getString("DefaultAttendee").equals("")) ? "" : attendeeJsonObject.getString("DefaultAttendee");
                                        String AttendeeEmpName = (attendeeJsonObject.isNull("AttendeeEmpName") || attendeeJsonObject.getString("AttendeeEmpName").equals("")) ? "" : attendeeJsonObject.getString("AttendeeEmpName");

                                        if (AttendeeType.equalsIgnoreCase("S")) {
                                            addedSplAttendeeModelList.add(new AddedSplAttendeeModel(AttendeeType, true, AttendeeEmpName, AttendeeEmpcode, MeetingAttendeeID));
                                            addedSplAttendeeCount++;
                                        } else {
                                            if (!DefaultAttendee.equalsIgnoreCase("true")) {
                                                addedAttendeeModelList.add(new AddedAttendeeModel(AttendeeType, true, AttendeeEmpName, AttendeeEmpcode, MeetingAttendeeID));
                                                addedAttendeeCount++;
                                            }
                                        }
                                    }

                                    // tvSplAttendeeCount.setText("(" + String.valueOf(addedSplAttendeeCount) + ")");
                                    setCountForSpecial(addedSplAttendeeCount);
                                    //  tvAttendeeCount.setText("(" + String.valueOf(addedAttendeeCount) + ")");
                                    setCount(addedAttendeeCount);
                                    selectProduct(ProductTypeID);

                                    String metadataArray[] = MeetingDate.split("T");
                                    String dateArray[] = metadataArray[0].split("-");
                                    String timeArray[] = metadataArray[1].split(":");
                                    currentYear = ((Integer.parseInt(dateArray[0])));
                                    month = ((Integer.parseInt(dateArray[1])));
                                    day = ((Integer.parseInt(dateArray[2])));
                                    hour = ((Integer.parseInt(timeArray[0])));
                                    minute = ((Integer.parseInt(timeArray[1])));
                                    etMeetingDescription.setText(MeetingDescription);
                                    etCustomerComment.setText(CustomerRemark);
                                    etCategory.setText(CategoryDescription);
                                    etSubCategory.setText(SubCategoryDescription);
                                    MeetingDate = MeetingDate.replace("T", " ");
                                    // tvStartDate.setText(MeetingDate);
                                    tvStartDate.setText(CRMAppUtil.getDBDate(CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute), DATE_FORMAT_TO_DISPLAY));
                                   /* meetingDuration(MeetingDurationID);
                                    activityType(ActivityTypeID);
                                    meetingReason(MeetingReasonID);
                                    actionables(ActionableID);
                                    category(CategoryID);*/
                                    // subCategory(SubCategoryID);

                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }


    private void category(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", CATEGORY);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrCategory = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
//                                    customProgress.setVisibility(View.GONE);
                                    arrCategory.add(new RowData(0, "-1", SELECT_CATEGORY));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrCategory.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapCategory = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrCategory);
                                    arrAdapCategory.setDropDownViewResource(R.layout.mdropdown);
                                    spCategory.setAdapter(arrAdapCategory);
                                    spCategory.setOnItemSelectedListener(selectCategoryListener);
                                    arrAdapCategory.notifyDataSetChanged();
                                    RowData categoryData = getSpinnerRowDataById(id, arrAdapCategory);
                                    if (categoryData != null) {
                                        spCategory.setSelection(categoryData.getPosition());
                                    }
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void subCategory(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", SUBCATEGORY);
            inputMap.put("ParentId", categoryCodeStr);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrSubCategory = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
//                                    customProgress.setVisibility(View.GONE);
                                    arrSubCategory.add(new RowData(0, "-1", SELECT_SUB_CATEGORY));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);


                                    mandatory = (jsonArray.length() > 1);
                                    if (mandatory) {
                                        //You got no data for sub category so noe its dependent feilds afe not mandatory
                                        // mandatory=false;
                                        llSubcategory.setVisibility(View.VISIBLE);
                                    } else {
                                        //   mandatory=false;
                                        etSubCategory.setText("");
                                        llSubcategory.setVisibility(View.GONE);
                                    }

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrSubCategory.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapSubCategory = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrSubCategory);
                                    arrAdapSubCategory.setDropDownViewResource(R.layout.mdropdown);
                                    spSubCategory.setAdapter(arrAdapSubCategory);
                                    spSubCategory.setOnItemSelectedListener(selectSubCategoryListener);
                                    arrAdapSubCategory.notifyDataSetChanged();
                                    RowData subCategoryData = getSpinnerRowDataById(id, arrAdapSubCategory);
                                    if (subCategoryData != null) {
                                        spSubCategory.setSelection(subCategoryData.getPosition());
                                    }
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void selectProduct(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            meetingDuration(meetingId);

//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", INTERESTEDIN);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrSelectProduct = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
//                                    customProgress.setVisibility(View.GONE);
                                    arrSelectProduct.add(new RowData(0, "-1", SELECT_PRODUCT));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrSelectProduct.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapSelectProduct = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrSelectProduct);
                                    arrAdapSelectProduct.setDropDownViewResource(R.layout.mdropdown);
                                    spProduct.setAdapter(arrAdapSelectProduct);
                                    spProduct.setOnItemSelectedListener(selectProductListener);
                                    arrAdapSelectProduct.notifyDataSetChanged();
                                    RowData productData = getSpinnerRowDataById(id, arrAdapSelectProduct);
                                    if (productData != null) {
                                        spProduct.setSelection(productData.getPosition());
                                    }
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void meetingDuration(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
//            customProgress.setVisibility(View.VISIBLE);
            activityType(ActivityTypeID);

            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", MEETINGDURATION);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrMeetingDuration = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
//                                    customProgress.setVisibility(View.GONE);
                                    arrMeetingDuration.add(new RowData(0, "-1", SELECT_MEETING_DURATION));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrMeetingDuration.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapMeetingDuration = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrMeetingDuration);
                                    arrAdapMeetingDuration.setDropDownViewResource(R.layout.mdropdown);
                                    spMeetingDuration.setAdapter(arrAdapMeetingDuration);
                                    spMeetingDuration.setOnItemSelectedListener(selectMeetingDurationListener);
                                    arrAdapMeetingDuration.notifyDataSetChanged();
                                    RowData meetingDurationData = getSpinnerRowDataById(id, arrAdapMeetingDuration);
                                    if (meetingDurationData != null) {
                                        spMeetingDuration.setSelection(meetingDurationData.getPosition());
                                    }
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void meetingObjective() {
        showProgress();
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", MEETINGOBJECTIVE);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrMeetingObjective = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
//                                    customProgress.setVisibility(View.GONE);
                                    arrMeetingObjective.add(new RowData(0, "-1", SELECT_MEETING_OBJECTIVE));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        arrMeetingObjective.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapMeetingsObjective = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrMeetingObjective);
                                    arrAdapMeetingsObjective.setDropDownViewResource(R.layout.mdropdown);
                                    spMeetingObjective.setAdapter(arrAdapMeetingsObjective);
                                    spMeetingObjective.setOnItemSelectedListener(selectMeetingObjective);
                                    arrAdapMeetingsObjective.notifyDataSetChanged();
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        customProgress.setVisibility(View.GONE);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                        customProgress.setVisibility(View.GONE);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void activityType(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            actionables(ActionableID);
            showProgress();
//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", ACTIVITYTYPE);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrActivityType = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
//                                    customProgress.setVisibility(View.GONE);
                                    arrActivityType.add(new RowData(0, "-1", SELECT_ACTIVITY_TYPE));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrActivityType.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapActivityType = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrActivityType);
                                    arrAdapActivityType.setDropDownViewResource(R.layout.mdropdown);
                                    spActivityType.setAdapter(arrAdapActivityType);
                                    spActivityType.setOnItemSelectedListener(selectActivityTypeListener);
                                    arrAdapActivityType.notifyDataSetChanged();
                                    RowData activityTypeData = getSpinnerRowDataById(id, arrAdapActivityType);
                                    if (activityTypeData != null) {
                                        spActivityType.setSelection(activityTypeData.getPosition());
                                    }
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void meetingReason(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {

            baClient();
//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", MEETINGREASON);
            inputMap.put("ParentId", activityTypeCodeStr);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrMeetingReason = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
//                                    customProgress.setVisibility(View.GONE);
                                    arrMeetingReason.add(new RowData(0, "-1", SELECT_MEETING_REASON));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrMeetingReason.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapMeetingReason = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrMeetingReason);
                                    arrAdapMeetingReason.setDropDownViewResource(R.layout.mdropdown);
                                    spMeetingReason.setAdapter(arrAdapMeetingReason);
                                    spMeetingReason.setOnItemSelectedListener(selectMeetingReasonListener);
                                    arrAdapMeetingReason.notifyDataSetChanged();
                                    RowData meetingReasonData = getSpinnerRowDataById(id, arrAdapMeetingReason);
                                    if (meetingReasonData != null) {
                                        spMeetingReason.setSelection(meetingReasonData.getPosition());
                                    }
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void baClient() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            outStation();

//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", BACLIENTMEETING);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrBAClient = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
//                                    customProgress.setVisibility(View.GONE);
                                    arrBAClient.add(new RowData(0, "-1", SELECT_BA_CLIENT_MEETING));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrBAClient.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapBAClient = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrBAClient);
                                    arrAdapBAClient.setDropDownViewResource(R.layout.mdropdown);
                                    spBAClient.setAdapter(arrAdapBAClient);
                                    spBAClient.setOnItemSelectedListener(selectBAClientListener);
                                    arrAdapBAClient.notifyDataSetChanged();
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void outStation() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();


//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", OUTSTATIONMEETING);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrOutStation = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
//                                    customProgress.setVisibility(View.GONE);
                                    arrOutStation.add(new RowData(0, "-1", SELECT_OUT_STATION_MEETING));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrOutStation.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapOutStation = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrOutStation);
                                    arrAdapOutStation.setDropDownViewResource(R.layout.mdropdown);
                                    spOutStaition.setAdapter(arrAdapOutStation);
                                    spOutStaition.setOnItemSelectedListener(selectOutStationListener);
                                    arrAdapOutStation.notifyDataSetChanged();
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void actionables(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            category(CategoryID);
            showProgress();
//            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", ACTIONABLES);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrActionables = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
//                                    customProgress.setVisibility(View.GONE);
                                    arrActionables.add(new RowData(0, "-1", SELECT_ACTIONABLES));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        String parentId = (objDetail.isNull(PARENTID) || objDetail.getString(PARENTID).equals("")) ? "" : objDetail.getString(PARENTID);
                                        String lookUpTypeId = (objDetail.isNull(LOOKUPTYPEID) || objDetail.getString(LOOKUPTYPEID).equals("")) ? "" : objDetail.getString(LOOKUPTYPEID);
                                        String lookUpType = (objDetail.isNull(LOOKUPTYPE) || objDetail.getString(LOOKUPTYPE).equals("")) ? "" : objDetail.getString(LOOKUPTYPE);
                                        String businessUnitId = (objDetail.isNull(BUSINESSUNITID) || objDetail.getString(BUSINESSUNITID).equals("")) ? "" : objDetail.getString(BUSINESSUNITID);
                                        String sequence = (objDetail.isNull(SEQUENCE) || objDetail.getString(SEQUENCE).equals("")) ? "" : objDetail.getString(SEQUENCE);
                                        String lookUpStatus = (objDetail.isNull(LOOKUPSTATUS) || objDetail.getString(LOOKUPSTATUS).equals("")) ? "" : objDetail.getString(LOOKUPSTATUS);
                                        String active = (objDetail.isNull(ACTIVE) || objDetail.getString(ACTIVE).equals("")) ? "" : objDetail.getString(ACTIVE);
                                        arrActionables.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapActionables = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrActionables);
                                    arrAdapActionables.setDropDownViewResource(R.layout.mdropdown);
                                    spActionables.setAdapter(arrAdapActionables);
                                    spActionables.setOnItemSelectedListener(selectActionablesListener);
                                    arrAdapActionables.notifyDataSetChanged();
                                    RowData actionablesData = getSpinnerRowDataById(id, arrAdapActionables);
                                    if (actionablesData != null) {
                                        spActionables.setSelection(actionablesData.getPosition());
                                    }
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private AdapterView.OnItemSelectedListener selectMeetingObjective = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                //TODO meeting objective on select
              /*  outStaitionCodeStr = ((RowData) spOutStaition.getSelectedItem()).getKey();
                outStaitionNameStr = ((RowData) spOutStaition.getSelectedItem()).getValue();
        */
                meetingObjectiveIdStr = ((RowData) parent.getSelectedItem()).getKey();
                tvMeetingObjectiveError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void dialogAddedSplAttendee() {
        final Dialog dialogAddedSplAttendee = new Dialog(getContext());
        dialogAddedSplAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddedSplAttendee.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddedSplAttendee.setContentView(R.layout.dialog_addedd_spl_attendee_list);
        TextView tvEmpty = (TextView) dialogAddedSplAttendee.findViewById(R.id.tvEmpty);
        dialogAddedSplAttendee.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAddedSplAttendee.setCanceledOnTouchOutside(true);
        ListView lvAttendee = (ListView) dialogAddedSplAttendee.findViewById(R.id.lvAttendee);
        lvAttendee.setEmptyView(tvEmpty);
        addedSplAttendeeAdapter = new AddedSplAttendeeAdapter(getActivity(), addedSplAttendeeModelList);
        lvAttendee.setAdapter(addedSplAttendeeAdapter);
        addedSplAttendeeAdapter.notifyDataSetChanged();
        dialogAddedSplAttendee.show();
    }

    private void dialogAddedAttendee() {
        final Dialog dialogAddedAttendee = new Dialog(getContext());
        dialogAddedAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddedAttendee.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddedAttendee.setContentView(R.layout.dialog_addedd_attendee_list);
        TextView tvEmpty = (TextView) dialogAddedAttendee.findViewById(R.id.tvEmpty);
        dialogAddedAttendee.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAddedAttendee.setCanceledOnTouchOutside(true);
        ListView lvAttendee = (ListView) dialogAddedAttendee.findViewById(R.id.lvAttendee);
        lvAttendee.setEmptyView(tvEmpty);
        addedAttendeeAdapter = new AddedAttendeeAdapter(getActivity(), addedAttendeeModelList);
        lvAttendee.setAdapter(addedAttendeeAdapter);
        addedAttendeeAdapter.notifyDataSetChanged();
        dialogAddedAttendee.show();
    }

    private void dialogAttendee() {
        final Dialog dialogAttendeeList = new Dialog(getContext());
        dialogAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAttendeeList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAttendeeList.setContentView(R.layout.dialog_attendee_list);
        dialogAttendeeList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAttendeeList.setCanceledOnTouchOutside(true);
        ListView lvAttendee = (ListView) dialogAttendeeList.findViewById(R.id.lvAttendee);
        final DelayAutoCompleteTextView etSearchAttendee = (DelayAutoCompleteTextView) dialogAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee
                .setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSearchAttendee.setOnEditorActionListener(this);
        etSearchAttendee.setThreshold(3);
        attendeeAdapter = new AttendeeAdapter(getContext());
        etSearchAttendee.setAdapter(attendeeAdapter);
        etSearchAttendee.setLoadingIndicator((ProgressBar) dialogAttendeeList.findViewById(R.id.pbLoadingIndicator));
        etSearchAttendee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EmployeeModel clientModel = attendeeAdapter.getItem(position);
                boolean has = listHasAttende(addedAttendeeModelList, addedSplAttendeeModelList, clientModel.getAttendeeCode());
                if (!has) {
                    if (clientModel.isNoData()) {
                        return;
                    }
                    String attendeeType = "R"; //todo //R=Regular  & S=special
                    addedAttendeeModelList.add(new AddedAttendeeModel(attendeeType, true, clientModel.getAttendeeName(), clientModel.getAttendeeCode()));
                    addedAttendeeCount++;
                    //  tvAttendeeCount.setText("(" + String.valueOf(addedAttendeeCount) + ")");
                    setCount(addedAttendeeCount);
                    dialogAttendeeList.dismiss();
                } else {
                    //clear It
                    Toast.makeText(getMainActivity(), ATTENDEE_ALREADY_ADDED, Toast.LENGTH_SHORT).show();
                    etSearchAttendee.setText("");
                }

            }

        });

        dialogAttendeeList.show();
    }

    private void dialogSplAttendee() {
        final Dialog dialogSplAttendeeList = new Dialog(getContext());
        dialogSplAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSplAttendeeList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSplAttendeeList.setContentView(R.layout.dialog_attendee_list);
        dialogSplAttendeeList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSplAttendeeList.setCanceledOnTouchOutside(true);
        ListView lvAttendee = (ListView) dialogSplAttendeeList.findViewById(R.id.lvAttendee);

        final DelayAutoCompleteTextView etSearchAttendee = (DelayAutoCompleteTextView) dialogSplAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee
                .setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSearchAttendee.setOnEditorActionListener(this);
        etSearchAttendee.setThreshold(3);
        splAttendeeAdapter = new SplAttendeeAdapter(getContext());
        etSearchAttendee.setAdapter(splAttendeeAdapter);
        etSearchAttendee.setLoadingIndicator((ProgressBar) dialogSplAttendeeList.findViewById(R.id.pbLoadingIndicator));
        etSearchAttendee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SplAttendeeModel clientModel = splAttendeeAdapter.getItem(position);
                boolean has = listHasAttende(addedAttendeeModelList, addedSplAttendeeModelList, clientModel.getAttendeeCode());

                if (!has) {
                    if (clientModel.isNoData()) {
                        return;
                    }
                    String attendeeType = "S"; //todo //R=Regular  & S=special
                    addedSplAttendeeModelList.add(new AddedSplAttendeeModel(attendeeType, true, clientModel.getAttendeeName(), clientModel.getAttendeeCode()));
                    // etSearchAttendee.setText(clientModel.getAttendeeName());
                    addedSplAttendeeCount++;
                    tvSplAttendeeCount.setText("(" + String.valueOf(addedSplAttendeeModelList.size()) + ")");
                    dialogSplAttendeeList.dismiss();
                } else {
                    //clear It
                    Toast.makeText(getMainActivity(), SPL_ATTENDEE_ALREADY_ADDED, Toast.LENGTH_SHORT).show();
                    etSearchAttendee.setText("");
                }
            }

        });

        dialogSplAttendeeList.show();
    }


    private void updateMeetingSave(MeetingApptModel meetingApptModel) {
        /*addedAttendeeModelList;
        addedSplAttendeeModelList;*/
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            // customProgress.setVisibility(View.VISIBLE);
           /* Map inputMap = new HashMap<>();
            inputMap.put("LeadId", leadId);
            inputMap.put("MeetingDate", fromPostingDate);
            inputMap.put("ActivityTypeID", activityTypeCodeStr);
            inputMap.put("ProductTypeID", productCodeStr);
            inputMap.put("MeetingReasonID", meetingDurationCodeStr);
            inputMap.put("MeetingDurationID", meetingDurationCodeStr);
            inputMap.put("ActionableID", actionablesCodeStr);
            inputMap.put("MeetingDescription", etMeetingDescription.getText().toString().trim());
            inputMap.put("CustomerRemark", etCustomerComment.getText().toString().trim());
            inputMap.put("CategoryDescription", etCategory.getText().toString().trim());
            inputMap.put("SubCategoryDescription", etSubCategory.getText().toString().trim());
            inputMap.put("MeetingType", "M");
            inputMap.put("DisplayType", "A");*/


            String requestJson = meetingApptModel.getJson();
            String serviceMethod = "Meeting/AddUpdateMeeting";
            showProgress();
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), requestJson, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        arrActionables = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                hideProgress();
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
//
                                    //JSONArray jsonArray = response.getJSONArray(RESPONSE);
                                    //  alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), "Meeting Succefully", "Ok", true, null);
                                    alertDialog(getActivity(), 0, getResources().getString(R.string.motilal_oswal), UPDATED_MEETING, "OK", true, new MSFDialog.DialogListener() {
                                        @Override
                                        public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                            if (action == MSFDialog.Action.OK) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString(LEAD_ID, getArguments().getString(LEAD_ID));
                                                bundle.putString(UCID, getArguments().getString(UCID));
                                                navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(), false);

                                            }
                                        }
                                    });

                                } else {
                                    hideProgress();
//                                    customProgress.setVisibility(View.GONE);
                                    JSONArray jsonArray = response.getJSONArray("Erros");
                                    for (int i = 0; i < 1; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String message = jsonObject.getString(MESSAGE) == null ? "" : jsonObject.getString(MESSAGE);
                                        alertDialog(getContext(), 0, getResources().getString(R.string.motilal_oswal), message, "Ok", true, null);
                                    }
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }


    public class AttendeeAdapter extends BaseAdapter implements Filterable {
        //  FragmentActivity activity;
        Context context;
        private LayoutInflater inflater;
        private List<EmployeeModel> resultList = new ArrayList<>();

        public AttendeeAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            //  this.activity = (FragmentActivity) context;
            this.context = context;
        }

        public void clearAll() {
            resultList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public EmployeeModel getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final AttendeeAdapter.HolderView holderView;
            final EmployeeModel clientModel = resultList.get(position);
            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
                holderView = new AttendeeAdapter.HolderView(convertView);
                convertView.setTag(holderView);
            } else {
                holderView = (AttendeeAdapter.HolderView) convertView.getTag();
            }
            // viewColor(position, convertView);

            viewColor(position, convertView);
            if (clientModel.isNoData()) {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
                holderView.linaerLayout.setOnClickListener(null);

            } else {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode() + "-" + clientModel.getAttendeeName() + "-" + clientModel.getBranchcode());
         /*       holderView.tvAttendeeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
            }
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults filterResults = new FilterResults();
                    ArrayList<EmployeeModel> modelList = new ArrayList<>();
                    try {
                        if (constraint != null) {
                            // String url = "ClientSearch?Userid=" + AccountDetails.getLoginId().trim() + "&SearchText=" + URLEncoder.encode(constraint.toString(), "utf-8");

                            Map requestMap = new HashMap();
                            requestMap.put("EmployeeName", URLEncoder.encode(constraint.toString(), "utf-8"));
                            requestMap.put("EmployeeCode", URLEncoder.encode(constraint.toString(), "utf-8"));


                            String serviceMethod = "UserApi/AutoCompleteAttendee";

                            if (MSFStatistics.isNetworkEnabled(getContext())) {
                                JSONObject response = WebServiceDirectCall.getData(serviceMethod, getHeader(), requestMap);

                                if (response == null) {
                                    return null;
                                }

                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);
                                    //  JSONArray jsonArray = objDetail.getJSONArray(DATA);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                     /*   "Id": 0,
                                                "BusinessUnitName": "Retail",
                                                "EmployeeName": "Ramnarayan  ",
                                                "UserType": 1,
                                                "UserId": 15256,
                                                "EmployeeCode": "6508",
                                                "BusinessChannelId": 1,
                                                "ChannelName": "Direct",
                                                "BusinessUnitId": 3,
                                                "BusinessUnit": null,
                                                "BusinessProfileId": 3,
                                                "BusinessProfileName": "OAO TeleSales",*/
                                        String employeeName = jsonObject.getString(EMPLOYEE_NAME) == null ? "" : jsonObject.getString(EMPLOYEE_NAME);
                                        String employeeCode = jsonObject.getString(EMPLOYEE_CODE) == null ? "" : jsonObject.getString(EMPLOYEE_CODE);
                                        String BranchCode = jsonObject.getString("BranchCode") == null ? "" : jsonObject.getString("BranchCode");//region Check self Empcode and don't add to list
                                        if (employeeCode.equalsIgnoreCase(AccountDetails.getEmployeeCode())) {
                                            continue;
                                        }
//endregion
                                        modelList.add(new EmployeeModel(employeeName, employeeCode, BranchCode, false));

                                        // Assign the data to the FilterResults
                                        filterResults.values = modelList;
                                        filterResults.count = modelList.size();

                                    }
                                } else {
                                    modelList.add(EmployeeModel.getNoData(CRMMOSLCommon.getErrorMsgFromResponse(response)));
                                    // modelList.add(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
                                    // Assign the data to the FilterResults
                                    filterResults.values = modelList;
                                    filterResults.count = modelList.size();
                                    return filterResults;
                                }


                            } else {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (filterResults.count == 0) {
                        modelList.add(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
                        filterResults.values = modelList;
                        filterResults.count = modelList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        resultList = (List<EmployeeModel>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }


        private void viewColor(int position, View convertView) {
            if (position % 2 == 1) {
                convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        class HolderView {
            TextView tvAttendeeName, tvProfileName;
            LinearLayout linaerLayout;

            public HolderView(View parent) {
                tvAttendeeName = (TextView) parent.findViewById(R.id.tvAttendeeName);
                linaerLayout = (LinearLayout) parent.findViewById(R.id.linaerLayout);

            }
        }
    }


    public class SplAttendeeAdapter extends BaseAdapter implements Filterable {
        private LayoutInflater inflater;
        Context mContext;
        private List<SplAttendeeModel> attendeeModelList = new ArrayList<>();

        public SplAttendeeAdapter(Context c) {
            inflater = LayoutInflater.from(c);
            mContext = c;
        }

        @Override
        public int getCount() {
            return attendeeModelList.size();
        }

        @Override
        public SplAttendeeModel getItem(int position) {
            return attendeeModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final SplAttendeeAdapter.HolderView holderView;
            final SplAttendeeModel clientModel = attendeeModelList.get(position);
            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
                holderView = new SplAttendeeAdapter.HolderView(convertView);
                convertView.setTag(holderView);
            } else {
                holderView = (SplAttendeeAdapter.HolderView) convertView.getTag();
            }
            // viewColor(position, convertView);

            viewColor(position, convertView);
            if (clientModel.isNoData()) {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
                holderView.linaerLayout.setOnClickListener(null);
            } else {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode() + "-" + clientModel.getAttendeeName() + "-" + clientModel.getBranchCode());
                /*  holderView.tvAttendeeName.setText(clientModel.getAttendeeCode() + "-" + clientModel.getAttendeeName() + "-" + clientModel.getBranchCode());                holderView.tvAttendeeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
            }

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults filterResults = new FilterResults();
                    ArrayList<SplAttendeeModel> modelList = new ArrayList<>();
                    try {
                        if (constraint != null) {
                            // String url = "ClientSearch?Userid=" + AccountDetails.getLoginId().trim() + "&SearchText=" + URLEncoder.encode(constraint.toString(), "utf-8");

                            Map requestMap = new HashMap();
                            requestMap.put("EmployeeName", URLEncoder.encode(constraint.toString(), "utf-8"));
                            requestMap.put("EmployeeCode", URLEncoder.encode(constraint.toString(), "utf-8"));


                            String serviceMethod = "UserApi/AutoCompleteSplAttendee";

                            if (MSFStatistics.isNetworkEnabled(getContext())) {
                                JSONObject response = WebServiceDirectCall.getData(serviceMethod, getHeader(), requestMap);

                                if (response == null) {
                                    return null;
                                }

                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);
                                    //  JSONArray jsonArray = objDetail.getJSONArray(DATA);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                     /*   "Id": 0,
                                                "BusinessUnitName": "Retail",
                                                "EmployeeName": "Ramnarayan  ",
                                                "UserType": 1,
                                                "UserId": 15256,
                                                "EmployeeCode": "6508",
                                                "BusinessChannelId": 1,
                                                "ChannelName": "Direct",
                                                "BusinessUnitId": 3,
                                                "BusinessUnit": null,
                                                "BusinessProfileId": 3,
                                                "BusinessProfileName": "OAO TeleSales",*/
                                        String employeeName = jsonObject.getString(EMPLOYEE_NAME) == null ? "" : jsonObject.getString(EMPLOYEE_NAME);
                                        String employeeCode = jsonObject.getString(EMPLOYEE_CODE) == null ? "" : jsonObject.getString(EMPLOYEE_CODE);
                                        String BranchCode = jsonObject.getString("BranchCode") == null ? "" : jsonObject.getString("BranchCode");
//region Check self Empcode and don't add to list
                                        if (employeeCode.equalsIgnoreCase(AccountDetails.getEmployeeCode())) {
                                            continue;
                                        }
//endregion
                                        modelList.add(new SplAttendeeModel(employeeName, employeeCode, BranchCode));

                                        // Assign the data to the FilterResults
                                        filterResults.values = modelList;
                                        filterResults.count = modelList.size();

                                    }
                                } else {

                                    hideProgress();
                                    modelList.add(SplAttendeeModel.getNoData(CRMMOSLCommon.getErrorMsgFromResponse(response)));
                                    //  modelList.add(SplAttendeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
                                    // Assign the data to the FilterResults
                                    filterResults.values = modelList;
                                    filterResults.count = modelList.size();
                                    return filterResults;

                                }

                            } else {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (filterResults.count == 0) {
                        modelList.add(SplAttendeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
                        filterResults.values = modelList;
                        filterResults.count = modelList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        attendeeModelList = (List<SplAttendeeModel>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }

        private void viewColor(int position, View convertView) {
            if (position % 2 == 1) {
                convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        class HolderView {
            TextView tvAttendeeName, tvProfileName;
            LinearLayout linaerLayout;

            public HolderView(View parent) {
                tvAttendeeName = (TextView) parent.findViewById(R.id.tvAttendeeName);
                linaerLayout = (LinearLayout) parent.findViewById(R.id.linaerLayout);

            }
        }


    }

    public class AddedAttendeeAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        Context mContext;
        List<AddedAttendeeModel> attendeeModelList;
        private ArrayList<EmployeeModel> arraylist;

        public AddedAttendeeAdapter(Context c, List<AddedAttendeeModel> attendeeModelList) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.attendeeModelList = attendeeModelList;

        }

        @Override
        public int getCount() {
            return attendeeModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return attendeeModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            AddedAttendeeAdapter.Holder holder = new AddedAttendeeAdapter.Holder();
            view = inflater.inflate(R.layout.item_list_added_attendee, parent, false);

            AddedAttendeeModel attendeeModel = attendeeModelList.get(position);

            holder.tvAttendeeName = (TextView) view.findViewById(R.id.tvAttendeeName);
            holder.tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedAttendeeCount--;
                    //   tvAttendeeCount.setText("(" + String.valueOf(addedAttendeeCount) + ")");
                    setCount(addedAttendeeCount);
                    attendeeModelList.remove(position);
                    notifyDataSetChanged();
                }
            });

            return view;
        }


        class Holder {
            TextView tvAttendeeName, tvProfileName;
            ImageView ivDelete;
        }
    }

    public class AddedSplAttendeeAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        Context mContext;
        List<AddedSplAttendeeModel> attendeeModelList;

        public AddedSplAttendeeAdapter(Context c, List<AddedSplAttendeeModel> attendeeModelList) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.attendeeModelList = attendeeModelList;

        }

        @Override
        public int getCount() {
            return attendeeModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return attendeeModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            AddedSplAttendeeAdapter.Holder holder = new AddedSplAttendeeAdapter.Holder();
            view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false);

            AddedSplAttendeeModel attendeeModel = attendeeModelList.get(position);

            holder.tvAttendeeName = (TextView) view.findViewById(R.id.tvAttendeeName);
            holder.tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedSplAttendeeCount--;
                    //  tvSplAttendeeCount.setText("(" + String.valueOf(addedSplAttendeeCount) + ")");
                    setCountForSpecial(addedSplAttendeeCount);
                    attendeeModelList.remove(position);
                    notifyDataSetChanged();
                }
            });

            return view;
        }


        class Holder {
            TextView tvAttendeeName, tvProfileName;
            ImageView ivDelete;
        }


    }

    //Attende count
    private void setCount(int cnt) {
        if (cnt <= 0) {
            tvAttendeeCount.setText("");
        } else {
            tvAttendeeCount.setText("(" + String.valueOf(cnt) + ")");
        }
    }

    //AttendeForSpecial  count
    private void setCountForSpecial(int cnt) {
        if (cnt <= 0) {
            tvSplAttendeeCount.setText("");
        } else {
            tvSplAttendeeCount.setText("(" + String.valueOf(cnt) + ")");
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (view instanceof DelayAutoCompleteTextView) {
            hideKeyboard(view);
            return false;
        }
        if (((EditText) createMeetingView.findViewById(view.getId())) == ((EditText) createMeetingView.findViewById(R.id.etMeetingDescription))) {
            etCustomerComment.requestFocus();
        }
        if (((EditText) createMeetingView.findViewById(view.getId())) == ((EditText) createMeetingView.findViewById(R.id.etCustomerComment))) {
            if ((llActionables.getVisibility() == View.VISIBLE)) {
                etCategory.requestFocus();
            }
        }
        if (((EditText) createMeetingView.findViewById(view.getId())) == ((EditText) createMeetingView.findViewById(R.id.etCategory))) {
            etSubCategory.requestFocus();
        }
        return false;
    }
}