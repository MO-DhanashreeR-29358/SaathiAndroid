package com.mosl.tabapp.leadViewDetail;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
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

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
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
import com.mosl.tabapp.model.ClientDetail;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.CRMMOSLCommon.disableField;
import static com.mosl.tabapp.common.CRMMOSLCommon.enableLoc;
import static com.mosl.tabapp.common.CRMMOSLCommon.hasGPSDevice;
import static com.mosl.tabapp.common.CRMMOSLCommon.setPermisson;
import static com.mosl.tabapp.leadViewDetail.SetAppointmentFragment.listHasAttende;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;


/**
 * Created by raju on 3/7/2017.
 */

public class CreateMeetingFragment extends MOSLCommonFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    View createMeetingView;
    private ImageView ivLead, ivDatePicker;
    EditText etCustomerComment, etMeetingDescription, etCategory, etSubCategory, etClientCode, etClientName,
            etAmount, etLocation, etPinCode;
    Location mCurrentLocation; // location
    private TextView tvCurrentStageSince, tvCurrentStage, tvBranchCode, tvLeadId, tvClientCode, tvName,
            tvSplAttendeeCount, tvSplAttendee, tvAttendeeCount, tvAttendee, tvStartDate, tvLocationError,
            tvPinCodeError, tvMeetingLocationError;
    View llSubcategory;
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
    private Spinner spProduct, spMeetingDuration, spActivityType, spMeetingReason, spActionables,
            spSubCategory, spCategory, spBAClient, spOutStaition, spMeetingObjective, spMeetingLocation,
            spTravellingMode;
    private ArrayList<RowData> arrSelectProduct, arrMeetingDuration, arrActivityType, arrMeetingReason,
            arrActionables, arrCategory, arrSubCategory, arrBAClient, arrOutStation, arrMeetingObjective,
            arrMeetingLocation, arrTravellingMode;
    private String leadId;
    private int currentYear, month, day, hour, minute;
    private Calendar calendar = Calendar.getInstance();
    private TextView tvProductError, tvMeetingDurationError, tvMeetingObjectiveError, tvMeetingReasonError, tvActivityTypeError,
            tvActionablesError, tvMeetingDescriptionError, tvCategoryError, tvCategoryCommentsError, tvSubCategoryError, tvSubCategoryCommentsError, tvActionables, tvBAClientError, tvOutStaitionError, tvClientCodeError, tvClientNameError;
    private RelativeLayout rlDate;
    private ScrollView scrollView;
    private LinearLayout llActionables, linaerLayoutNew, llLocationGroup;
    private boolean profileType;
    private String mUCID = "0";
    private String meetingObjectiveIdStr = "0";
    private boolean mandatory = true;
    private CreateMeetingProductAdapter productAdapter;
    private RecyclerView rvProductList;
    private ArrayList<CreateMeetingProductModel> productList;
    private ArrayAdapter<RowData> arrAdapSelectProduct, arrAdapMeetingDuration, arrAdapActivityType,
            arrAdapMeetingReason, arrAdapActionables, arrAdapCategory, arrAdapSubCategory, arrAdapBAClient, arrAdapOutStation, arrAdapMeetingsObjective, arrAdapMeetingLocation,
            arrAdapTravellingMode;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private boolean enableLocationGroup;
    private AdapterView.OnItemSelectedListener selectMeetingLocation = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (!((RowData) spMeetingLocation.getSelectedItem()).getKey().equalsIgnoreCase("-1") &&
                        !((RowData) spMeetingLocation.getSelectedItem()).getValue().equalsIgnoreCase("other")) {
                    GetAddressByLeadId(((RowData) spMeetingLocation.getSelectedItem()).getKey());
                }
                etLocation.setText("");
                tvMeetingLocationError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createMeetingView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_create_meeting);
        setUpView(createMeetingView);
        startLocationUpdates();
        try {
            leadId = this.getArguments().getString(LEAD_ID, "0");
            mUCID = this.getArguments().getString(UCID, "0").equalsIgnoreCase("") ? "0" : this.getArguments().getString(UCID, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!mUCID.equalsIgnoreCase("0")) {
            getClientDetail(mUCID);
            setProfileImage(mUCID);

        } else {
            getLeadByIdFromServer(leadId);
        }
        selectProduct();
        meetingObjective();
        meetingLocation();
        setAppTitle(getClass().toString(), "Create Meeting");

        return createMeetingView;
    }

    private AdapterView.OnItemSelectedListener selectCategoryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                categoryCodeStr = ((RowData) spCategory.getSelectedItem()).getKey();
                categoryNameStr = ((RowData) spCategory.getSelectedItem()).getValue();
                tvCategoryError.setVisibility(View.GONE);
                subCategory();
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
                if (!productCodeStr.equalsIgnoreCase("-1")) {
                    productAdapter.addDocument(new CreateMeetingProductModel(productCodeStr, productNameStr));
                }
                tvProductError.setVisibility(View.GONE);

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
                    arrMeetingReason = new ArrayList<>();
                    arrMeetingReason.add(new RowData(0, "-1", SELECT_MEETING_REASON));
                    meetingReason();
                    if (spActivityType.getSelectedItem().toString().equalsIgnoreCase("Email") || spActivityType.getSelectedItem().toString().equalsIgnoreCase("Phone")) {
                        enableLocationGroup = false;
                        disableField(llLocationGroup);
                        disableReset();
                    } else {
                        enableLocationGroup = true;
                        enableField(llLocationGroup);
                    }

                } else {
                    arrMeetingReason = new ArrayList<>();
                    arrMeetingReason.add(new RowData(0, "-1", SELECT_MEETING_REASON));
                    arrAdapMeetingReason = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrMeetingReason);
                    arrAdapMeetingReason.setDropDownViewResource(R.layout.mdropdown);
                    spMeetingReason.setAdapter(arrAdapMeetingReason);
                    spMeetingReason.setOnItemSelectedListener(selectMeetingReasonListener);
                    arrAdapMeetingReason.notifyDataSetChanged();
                }
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        mainFromDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        tvStartDate.setText(CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));
        ivDatePicker = (ImageView) createMeetingView.findViewById(R.id.ivDatePicker);
        rlDate.setOnClickListener(this);
        ivLead = (ImageView) createMeetingView.findViewById(R.id.ivLead);
        tvName = (TextView) createMeetingView.findViewById(R.id.tvName);
        tvClientCode = (TextView) createMeetingView.findViewById(R.id.tvClientCode);
        tvLeadId = (TextView) createMeetingView.findViewById(R.id.tvLeadId);
        tvBranchCode = (TextView) createMeetingView.findViewById(R.id.tvBranchCode);
        tvCurrentStage = (TextView) createMeetingView.findViewById(R.id.tvCurrentStage);
        tvCurrentStageSince = (TextView) createMeetingView.findViewById(R.id.tvCurrentStageSince);

        tvSplAttendeeCount = (TextView) createMeetingView.findViewById(R.id.tvSplAttendeeCount);
        tvSplAttendee = (TextView) createMeetingView.findViewById(R.id.tvSplAttendee);
        tvAttendeeCount = (TextView) createMeetingView.findViewById(R.id.tvAttendeeCount);
        tvAttendee = (TextView) createMeetingView.findViewById(R.id.tvAttendee);
        tvProductError = (TextView) createMeetingView.findViewById(R.id.tvProductError);
        tvMeetingDurationError = (TextView) createMeetingView.findViewById(R.id.tvMeetingDurationError);
        tvMeetingObjectiveError = (TextView) createMeetingView.findViewById(R.id.tvMeetingObjectiveError);
        tvMeetingReasonError = (TextView) createMeetingView.findViewById(R.id.tvMeetingReasonError);
        tvActivityTypeError = (TextView) createMeetingView.findViewById(R.id.tvActivityTypeError);
        tvActionablesError = (TextView) createMeetingView.findViewById(R.id.tvActionablesError);
        tvCategoryError = (TextView) createMeetingView.findViewById(R.id.tvCategoryError);
        tvSubCategoryError = (TextView) createMeetingView.findViewById(R.id.tvSubCategoryError);
        tvMeetingDescriptionError = (TextView) createMeetingView.findViewById(R.id.tvMeetingDescriptionError);
        tvSubCategoryCommentsError = (TextView) createMeetingView.findViewById(R.id.tvSubCategoryCommentsError);
        tvCategoryCommentsError = (TextView) createMeetingView.findViewById(R.id.tvCategoryCommentsError);
        tvLocationError = (TextView) createMeetingView.findViewById(R.id.tvLocationError);
        tvPinCodeError = (TextView) createMeetingView.findViewById(R.id.tvPinCodeError);
        tvMeetingLocationError = (TextView) createMeetingView.findViewById(R.id.tvMeetingLocationError);

        tvBAClientError = (TextView) createMeetingView.findViewById(R.id.tvBAClientError);
        tvOutStaitionError = (TextView) createMeetingView.findViewById(R.id.tvOutStaitionError);
        tvClientCodeError = (TextView) createMeetingView.findViewById(R.id.tvClientCodeError);
        tvClientNameError = (TextView) createMeetingView.findViewById(R.id.tvClientNameError);

        tvActionables = (TextView) createMeetingView.findViewById(R.id.tvActionables);
        llActionables = (LinearLayout) createMeetingView.findViewById(R.id.llActionables);
        linaerLayoutNew = (LinearLayout) createMeetingView.findViewById(R.id.linaerLayoutNew);
        llLocationGroup = (LinearLayout) createMeetingView.findViewById(R.id.llLocationGroup);
        setIcon(llActionables, tvActionables);
        tvActionables.setOnClickListener(this);
        btnSave = (Button) createMeetingView.findViewById(R.id.btnSave);
        btnReset = (Button) createMeetingView.findViewById(R.id.btnReset);
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
        etAmount = (EditText) createMeetingView.findViewById(R.id.etAmount);
        etLocation = (EditText) createMeetingView.findViewById(R.id.etLocation);
        etPinCode = (EditText) createMeetingView.findViewById(R.id.etPinCode);
        etLocation.setFilters(new InputFilter[]{filterOnlyAlphabetAndNumbersAnsAllowedChar});
        llSubcategory = createMeetingView.findViewById(R.id.llSubcategory);

        spCategory = (Spinner) createMeetingView.findViewById(R.id.spCategory);
        spSubCategory = (Spinner) createMeetingView.findViewById(R.id.spSubCategory);
        spProduct = (Spinner) createMeetingView.findViewById(R.id.spProduct);
        spMeetingObjective = (Spinner) createMeetingView.findViewById(R.id.spMeetingObjective);
        spMeetingDuration = (Spinner) createMeetingView.findViewById(R.id.spMeetingDuration);
        spActivityType = (Spinner) createMeetingView.findViewById(R.id.spActivityType);
        spMeetingReason = (Spinner) createMeetingView.findViewById(R.id.spMeetingReason);
        spActionables = (Spinner) createMeetingView.findViewById(R.id.spActionables);
        spBAClient = (Spinner) createMeetingView.findViewById(R.id.spBAClient);
        spOutStaition = (Spinner) createMeetingView.findViewById(R.id.spOutStaition);
        spMeetingLocation = (Spinner) createMeetingView.findViewById(R.id.spMeetingLocation);
        spTravellingMode = (Spinner) createMeetingView.findViewById(R.id.spTravellingMode);
        rvProductList = (RecyclerView) createMeetingView.findViewById(R.id.rvProductList);
        productList = new ArrayList<>();
        productAdapter = new CreateMeetingProductAdapter(productList, getContext());
        rvProductList.setLayoutManager(new GridLayoutManager(getMainActivity(), 1));
        rvProductList.setAdapter(productAdapter);
        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        ibAttendee.setOnClickListener(this);
        ibSplAttendee.setOnClickListener(this);
        tvAttendeeCount.setOnClickListener(this);
        tvSplAttendeeCount.setOnClickListener(this);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        fromDat = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        String dateArray[] = mainToDate.split("-");
        currentYear = ((Integer.parseInt(dateArray[0])));
        month = ((Integer.parseInt(dateArray[1])));
        day = ((Integer.parseInt(dateArray[2])));
        AddedAttendeeModel.setAttendeeUserEmplyeeCode(AccountDetails.getEmployeeCode());
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
                if (!isBlank(etClientCode.getText().toString().trim())) {
                    tvClientCodeError.setVisibility(View.GONE);
                } else {
                    tvClientCodeError.setVisibility(View.GONE);
                    tvClientCodeError.setText("Enter client code");
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
                if (!isBlank(etSubCategory.getText().toString().trim()) && etSubCategory.getText().length() >= 10) {
                    tvSubCategoryCommentsError.setVisibility(View.GONE);
                } else {
                    tvSubCategoryCommentsError.setVisibility(View.VISIBLE);
                    tvSubCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
                }
            }
        });

        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etLocation.getText().toString().trim()) && etLocation.getText().length() >= 20) {
                    tvLocationError.setVisibility(View.GONE);
                } else {
                    tvLocationError.setVisibility(View.VISIBLE);
                    tvLocationError.setText(ATLEAST_TWENTY_CHARACTERS);
                }
            }
        });

        etPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etPinCode.getText().toString().trim()) && etPinCode.getText().length() >= 6) {
                    tvPinCodeError.setVisibility(View.GONE);
                } else {
                    tvPinCodeError.setVisibility(View.VISIBLE);
                    tvPinCodeError.setText(INVALID_PINCODE_NUMBER);
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
                if (!isBlank(etMeetingDescription.getText().toString().trim()) && etMeetingDescription.getText().length() >= 50) {
                    tvMeetingDescriptionError.setVisibility(View.GONE);
                } else {
                    tvMeetingDescriptionError.setVisibility(View.VISIBLE);
                    tvMeetingDescriptionError.setText(ATLEAST_FIFTY_CHARACTERS);
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
                if (!isBlank(etCategory.getText().toString().trim()) && etCategory.getText().length() >= 10) {
                    tvCategoryCommentsError.setVisibility(View.GONE);
                } else {
                    tvCategoryCommentsError.setVisibility(View.VISIBLE);
                    tvCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
                }
            }
        });
    }
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private AdapterView.OnItemSelectedListener selectMeetingObjective = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
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
                selectDate();
            } else if (id == R.id.tvActionables) {
                toggleSectionExpansion(llActionables, tvActionables);
            } else if (id == R.id.btnReset) {
                resetData();
            } else if (id == R.id.btnSave) {
                if (validateForm()) {
                    MeetingApptModel meetingApptModel = new MeetingApptModel();
                    meetingApptModel.setMeetingType("M");
                    meetingApptModel.setDisplayType("A");
                    if (mCurrentLocation != null) {
                        Geocoder geocoder = new Geocoder(getMainActivity(), Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(
                                    mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                            meetingApptModel.setGeoArea(addressList.get(0).getAddressLine(0));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        meetingApptModel.setGeoLatitude(mCurrentLocation.getLatitude() + "");
                        meetingApptModel.setGeoLongitude(mCurrentLocation.getLongitude() + "");
                    }
                    meetingApptModel.setPincode(etPinCode.getText().toString());
                    meetingApptModel.setAmount(etAmount.getText().toString());
                    meetingApptModel.setMeetingAddress(etLocation.getText().toString());
                    meetingApptModel.setMeetingAddressTypeId(((RowData) spMeetingLocation.getSelectedItem()).getKey());
                    meetingApptModel.setTravellingModeIds(((RowData) spTravellingMode.getSelectedItem()).getKey());
                    meetingApptModel.setLeadId(leadId);
                    meetingApptModel.setUcid(mUCID);
                    meetingApptModel.setMeetingDate(CRMAppUtil.getDBDate(currentYear, month, day, hour, minute));
                    meetingApptModel.setProduct("0");
                    meetingApptModel.setProductList(productAdapter.getListProduct());
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
                    if (enableLocationGroup && ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        setPermisson(getMainActivity(), getMainActivity());
                    } else {
                        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        if (enableLocationGroup && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getMainActivity())) {

                            enableLoc(getMainActivity());

                        } else {
                            if (enableLocationGroup && mCurrentLocation == null) {
                                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                                mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                Geocoder geocoder = new Geocoder(getMainActivity(), Locale.getDefault());
                                try {
                                    List<Address> addressList = geocoder.getFromLocation(
                                            mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                                    meetingApptModel.setGeoArea(addressList.get(0).getAddressLine(0));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                meetingApptModel.setGeoLatitude(mCurrentLocation.getLatitude() + "");
                                meetingApptModel.setGeoLongitude(mCurrentLocation.getLongitude() + "");
                            }
                            createMeetingSave(meetingApptModel);
                        }
//                        createMeetingSave(meetingApptModel);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableReset() {
        spMeetingLocation.setSelection(0);
        spTravellingMode.setSelection(0);
        etLocation.setText("");
        etAmount.setText("");
        etPinCode.setText("");
        tvMeetingLocationError.setVisibility(View.GONE);
        tvPinCodeError.setVisibility(View.GONE);
        tvLocationError.setVisibility(View.GONE);

    }
    private void resetData() {
        addedAttendeeModelList.clear();
        addedSplAttendeeModelList.clear();
        tvAttendeeCount.setText("");
        tvSplAttendeeCount.setText("");
        tvStartDate.setText(CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));
        spProduct.setSelection(0);
        spMeetingDuration.setSelection(0);
        spActivityType.setSelection(0);
        spMeetingReason.setSelection(0);
        etMeetingDescription.setText("");
        etCustomerComment.setText("");
        spActionables.setSelection(0);
        spCategory.setSelection(0);
        spMeetingObjective.setSelection(0);
        etCategory.setText("");
        tvMeetingDescriptionError.setVisibility(View.GONE);
        spMeetingLocation.setSelection(0);
        spTravellingMode.setSelection(0);
        etLocation.setText("");
        etAmount.setText("");
        etPinCode.setText("");
        tvMeetingLocationError.setVisibility(View.GONE);
        tvPinCodeError.setVisibility(View.GONE);
        tvLocationError.setVisibility(View.GONE);
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
     */

    private boolean validateForm() {
        if (spProduct.getSelectedItem().toString().equalsIgnoreCase(SELECT_PRODUCT) && productAdapter.getListProduct().size() == 0) {
            tvProductError.setVisibility(View.VISIBLE);
            tvProductError.setText(SELECT_PRODUCT);
            tvProductError.requestFocus();
            return false;
        } else if (productAdapter.getListProduct().size() == 0) {
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
        } else if (etMeetingDescription.getText().length() < 50) {
            tvMeetingDescriptionError.setVisibility(View.VISIBLE);
            tvMeetingDescriptionError.setText(ATLEAST_FIFTY_CHARACTERS);
            tvMeetingDescriptionError.requestFocus();
            return false;
        } else if (enableLocationGroup && spMeetingLocation.getSelectedItem().toString().equalsIgnoreCase(SELECT_MEETING_LOCATION)) {
            tvMeetingLocationError.setVisibility(View.VISIBLE);
            tvMeetingLocationError.setText(SELECT_MEETING_LOCATION);
            tvMeetingLocationError.requestFocus();
            return false;
        } else if (enableLocationGroup && etLocation.getText().toString().trim().isEmpty()) {
            tvLocationError.setVisibility(View.VISIBLE);
            tvLocationError.setText(LOCATION_CANNOT_BE_LEFT_BLANK);
            tvLocationError.requestFocus();
            return false;
        } else if (enableLocationGroup && etLocation.getText().length() < 20) {
            tvLocationError.setVisibility(View.VISIBLE);
            tvLocationError.setText(ATLEAST_TWENTY_CHARACTERS);
            tvLocationError.requestFocus();
            return false;
        } else if (enableLocationGroup && !etPinCode.getText().toString().trim().isEmpty() && etPinCode.getText().length() < 6) {
            tvPinCodeError.setVisibility(View.VISIBLE);
            tvPinCodeError.setText(INVALID_PINCODE_NUMBER);
            tvPinCodeError.requestFocus();
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

    private void setProfileImage(String ucid) {


        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Client/GetClientImage";
            String tmpReq = String.format(ID_OBJECT, ucid);

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    String objDetail = response.getString(RESPONSE);

                                    byte[] byt = Base64.decode(objDetail, Base64.DEFAULT);
                                    if (byt != null) {
                                        CRMAppUtil.setImage(byt, ivLead, getMainActivity());

                                    } else {
                                        CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
                                    }


                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }

    }

    //region Dropdown Web calls
    private void getClientDetail(String ucid) {

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put(UCID, ucid);
            String serviceMethod = "Client/GetClientViewByCommand/";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {

                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject objDetail = response.getJSONObject(RESPONSE);

                                    new ClientDetail(objDetail);
                                    tvLeadId.setText(ClientDetail.getLeadId());
                                    tvCurrentStage.setText(ClientDetail.getCurrentStage());
                                    if (!ClientDetail.getCurrentStageSince().isEmpty()) {
                                        tvCurrentStageSince.setText(ClientDetail.getCurrentStageSince() + " Days");
                                    }
                                    tvName.setText(ClientDetail.getName());
                                    tvClientCode.setText(ClientDetail.getClientCode());
                                    tvBranchCode.setText(ClientDetail.getBranchCode());

                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void getLeadByIdFromServer(CharSequence strLeadId) {
        CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {

            String serviceMethod = "Lead/GetLeadView";
            String tmpReq = String.format(ID_OBJECT, strLeadId);
            showProgress();

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject Response_obj = response.getJSONObject(RESPONSE);
                                    String leadID = (Response_obj.isNull(LEADID) || Response_obj.getString(LEADID).equals("")) ? "" : Response_obj.getString(LEADID);
                                    String branchcode = (Response_obj.isNull(BRANCHCODE) || Response_obj.getString(BRANCHCODE).equals("")) ? "" : Response_obj.getString(BRANCHCODE);
                                    JSONObject contact_obj = Response_obj.getJSONObject(CONTACT);
                                    String Name = (contact_obj.isNull(NAME) || contact_obj.getString(NAME).equals("")) ? "" : contact_obj.getString(NAME);
                                    JSONArray leadProductMapping = Response_obj.getJSONArray(LEADPRODUCTMAPPING);
                                    for (int leadProductMapping_i = 0; leadProductMapping_i < leadProductMapping.length(); leadProductMapping_i++) {
                                        JSONObject leadProductMappingObj = leadProductMapping.getJSONObject(leadProductMapping_i);
                                        String clientCode = (leadProductMappingObj.isNull(CLIENTCODE) || leadProductMappingObj.getString(CLIENTCODE).equals("")) ? "" : leadProductMappingObj.getString(CLIENTCODE);
                                        String StageName = (leadProductMappingObj.isNull(STAGENAME) || leadProductMappingObj.getString(STAGENAME).equals("")) ? "" : leadProductMappingObj.getString(STAGENAME);
                                        String isDefault = (leadProductMappingObj.isNull(IS_DEFAULT) || leadProductMappingObj.getString(IS_DEFAULT).equals("")) ? "" : leadProductMappingObj.getString(IS_DEFAULT);
                                        String pendingDays = (leadProductMappingObj.isNull(PENDING_DAYS) || leadProductMappingObj.getString(PENDING_DAYS).equals("")) ? "" : leadProductMappingObj.getString(PENDING_DAYS);
                                        if (isDefault.equalsIgnoreCase("true")) {
                                            tvClientCode.setText(clientCode);
                                            tvCurrentStage.setText(StageName);
                                            tvCurrentStageSince.setText(pendingDays + " Days");
                                        }

                                    }

                                    tvName.setText(Name);
                                    tvLeadId.setText(leadID);
                                    tvBranchCode.setText(branchcode);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
                            }
                            hideProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void category() {
        showProgress();
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", CATEGORY);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrCategory = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
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
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
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

    private void meetingObjective() {
        showProgress();
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", MEETINGOBJECTIVE);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrMeetingObjective = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
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

    private void subCategory() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
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
                        arrSubCategory = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    arrSubCategory.add(new RowData(0, "-1", SELECT_SUB_CATEGORY));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);
                                    mandatory = (jsonArray.length() > 1);
                                    if (mandatory) {
                                        llSubcategory.setVisibility(View.VISIBLE);
                                    } else {
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
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);

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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void selectProduct() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            meetingDuration();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", INTERESTEDIN);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrSelectProduct = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                hideProgress();

                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
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
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
                            }
                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
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

    private void meetingDuration() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            activityType();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", MEETINGDURATION);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrMeetingDuration = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                hideProgress();
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
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
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void activityType() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", ACTIVITYTYPE);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrActivityType = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
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
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void meetingReason() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            baClient();
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
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
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
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }


    private void baClient() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            outStation();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", BACLIENTMEETING);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrBAClient = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void outStation() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            actionables();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", OUTSTATIONMEETING);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrOutStation = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void actionables() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            category();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", ACTIONABLES);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrActionables = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
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
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }


    private void meetingLocation() {
        showProgress();
        travellingMode();
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", MEETINGADDRESSID);
            // as per mail 0 add default
            inputMap.put("BusinessUnitId", 0);
            inputMap.put("IgnoreBusinessUnit", true);

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrMeetingLocation = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    arrMeetingLocation.add(new RowData(0, "-1", SELECT_MEETING_LOCATION));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        arrMeetingLocation.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapMeetingLocation = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrMeetingLocation);
                                    arrAdapMeetingLocation.setDropDownViewResource(R.layout.mdropdown);
                                    spMeetingLocation.setAdapter(arrAdapMeetingLocation);
                                    spMeetingLocation.setOnItemSelectedListener(selectMeetingLocation);
                                    arrAdapMeetingLocation.notifyDataSetChanged();
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
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

    private void GetAddressByLeadId(String meetingId) {
        showProgress();
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("LeadId", tvLeadId.getText().toString());
            inputMap.put("MeetingAddressTypeId", meetingId);

            String serviceMethod = "Lead/GetAddressByLeadId";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    JSONObject jsonObject = response.getJSONObject("Response");
                                    String meetingAddress = jsonObject.getString("MeetingAddress");
                                    etLocation.setText(meetingAddress);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
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

    private void travellingMode() {
        showProgress();
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", TRAVELLINGMODEID);
            // as per mail 0 add default
            inputMap.put("BusinessUnitId", 0);
            inputMap.put("IgnoreBusinessUnit", true);

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrTravellingMode = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    arrTravellingMode.add(new RowData(0, "-1", SELECT_TRAVELLING_MODE));
                                    JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objDetail = jsonArray.getJSONObject(i);
                                        String lookUpId = (objDetail.isNull(LOOKUPID) || objDetail.getString(LOOKUPID).equals("")) ? "" : objDetail.getString(LOOKUPID);
                                        String lookUpName = (objDetail.isNull(LOOKUPNAME) || objDetail.getString(LOOKUPNAME).equals("")) ? "" : objDetail.getString(LOOKUPNAME);
                                        arrTravellingMode.add(new RowData(i + 1, lookUpId, lookUpName));
                                    }
                                    arrAdapTravellingMode = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrTravellingMode);
                                    arrAdapTravellingMode.setDropDownViewResource(R.layout.mdropdown);
                                    spTravellingMode.setAdapter(arrAdapTravellingMode);
                                    arrAdapTravellingMode.notifyDataSetChanged();
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
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
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
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

    public void selectDate() {
        today = Calendar.getInstance();

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDate = (DatePicker) dialog.findViewById(R.id.datePickerFromDate);
        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        close = (ImageView) dialog.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        Calendar cal = Calendar.getInstance();
        cal.add(cal.DAY_OF_MONTH, -3);
        dpFromDate.setMinDate(cal.getTimeInMillis());
        dpFromDate.setMaxDate(System.currentTimeMillis() - 1000);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
                hour = arg1;
                minute = arg2;
            }
        });

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
                    if (mainFromDate != null) {
                        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
                        String finalSelectedDateStr = fromPostingDate + " " + hour + ":" + minute + ":" + "00"; //dd-MM-yyyy hh:mm:ss
                        Date finalSelectedDate = CRMAppUtil.getDate(finalSelectedDateStr, "dd-MM-yyyy hh:mm");
                        if (CRMAppUtil.isBeforeandEqualDate(finalSelectedDate)) { //
                            tvStartDate.setText(CRMAppUtil.getDBDate(finalSelectedDate, DATE_FORMAT_TO_DISPLAY));
                            dialog.dismiss();
                        } else {
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INVALID_TIME, "OK", true, null);
                        }
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
                    setCount(addedAttendeeCount);
                    dialogAttendeeList.dismiss();
                } else {
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
        etSearchAttendee.setImeOptions(EditorInfo.IME_ACTION_DONE);
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
                    String attendeeType = "S";
                    addedSplAttendeeModelList.add(new AddedSplAttendeeModel(attendeeType, true, clientModel.getAttendeeName(), clientModel.getAttendeeCode()));
                    addedSplAttendeeCount++;
                    setCountForSpecial(addedSplAttendeeCount);
                    dialogSplAttendeeList.dismiss();

                } else {
                    Toast.makeText(getMainActivity(), SPL_ATTENDEE_ALREADY_ADDED, Toast.LENGTH_SHORT).show();
                    etSearchAttendee.setText("");
                }

            }

        });

        dialogSplAttendeeList.show();
    }


    private void createMeetingSave(MeetingApptModel meetingApptModel) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            String requestJson = meetingApptModel.getJson();
            String serviceMethod = "Meeting/AddUpdateMeeting";
            showProgress();
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), requestJson, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                hideProgress();
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    MSFDialog.alertDialog(getActivity(), 0, getResources().getString(R.string.motilal_oswal), CREATE_MEETING, "OK", true, new MSFDialog.DialogListener() {
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
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        hideKeyboard(textView);
        return false;
    }


    public class AttendeeAdapter extends BaseAdapter implements Filterable {
        Context context;
        private LayoutInflater inflater;
        private List<EmployeeModel> resultList = new ArrayList<>();

        public AttendeeAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
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
            final HolderView holderView;
            final EmployeeModel clientModel = resultList.get(position);
            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
                holderView = new HolderView(convertView);
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView) convertView.getTag();
            }
            viewColor(position, convertView);

            if (clientModel.isNoData()) {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
                holderView.linaerLayout.setOnClickListener(null);
            } else {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode() + "-" + clientModel.getAttendeeName() + "-" + clientModel.getBranchcode());
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
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String employeeName = jsonObject.getString(EMPLOYEE_NAME) == null ? "" : jsonObject.getString(EMPLOYEE_NAME);
                                        String employeeCode = jsonObject.getString(EMPLOYEE_CODE) == null ? "" : jsonObject.getString(EMPLOYEE_CODE);
                                        String BranchCode = jsonObject.getString("BranchCode") == null ? "" : jsonObject.getString("BranchCode");//region Check self Empcode and don't add to list
                                        if (employeeCode.equalsIgnoreCase(AccountDetails.getEmployeeCode())) {
                                            continue;
                                        }
                                        modelList.add(new EmployeeModel(employeeName, employeeCode, BranchCode, false));
                                        filterResults.values = modelList;
                                        filterResults.count = modelList.size();

                                    }
                                } else {
                                    modelList.add(EmployeeModel.getNoData(CRMMOSLCommon.getErrorMsgFromResponse(response)));
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
            TextView tvAttendeeName;
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
            viewColor(position, convertView);
            if (clientModel.isNoData()) {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
                holderView.linaerLayout.setOnClickListener(null);

            } else {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode() + "-" + clientModel.getAttendeeName() + "-" + clientModel.getBranchCode());
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
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String employeeName = jsonObject.getString(EMPLOYEE_NAME) == null ? "" : jsonObject.getString(EMPLOYEE_NAME);
                                        String employeeCode = jsonObject.getString(EMPLOYEE_CODE) == null ? "" : jsonObject.getString(EMPLOYEE_CODE);
                                        String BranchCode = jsonObject.getString("BranchCode") == null ? "" : jsonObject.getString("BranchCode");
                                        modelList.add(new SplAttendeeModel(employeeName, employeeCode, BranchCode));
                                        filterResults.values = modelList;
                                        filterResults.count = modelList.size();

                                    }
                                } else {
                                    hideProgress();
                                    modelList.add(SplAttendeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
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
            Holder holder = new Holder();
            view = inflater.inflate(R.layout.item_list_added_attendee, parent, false);
            AddedAttendeeModel attendeeModel = attendeeModelList.get(position);
            holder.tvAttendeeName = (TextView) view.findViewById(R.id.tvAttendeeName);
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedAttendeeCount--;
                    setCount(addedAttendeeCount);
                    attendeeModelList.remove(position);
                    notifyDataSetChanged();
                }
            });

            return view;
        }


        class Holder {
            TextView tvAttendeeName;
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
            Holder holder = new Holder();
            view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false);

            AddedSplAttendeeModel attendeeModel = attendeeModelList.get(position);

            holder.tvAttendeeName = (TextView) view.findViewById(R.id.tvAttendeeName);
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedSplAttendeeCount--;
                    setCountForSpecial(addedSplAttendeeCount);
                    attendeeModelList.remove(position);
                    notifyDataSetChanged();
                }
            });

            return view;
        }


        class Holder {
            TextView tvAttendeeName;
            ImageView ivDelete;
        }


    }

    private void setCount(int cnt) {
        if (cnt <= 0) {
            tvAttendeeCount.setText("");
        } else {
            tvAttendeeCount.setText("(" + String.valueOf(cnt) + ")");
        }
    }

    private void setCountForSpecial(int cnt) {
        if (cnt <= 0) {
            tvSplAttendeeCount.setText("");
        } else {
            tvSplAttendeeCount.setText("(" + String.valueOf(cnt) + ")");
        }
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getMainActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(getMainActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        mCurrentLocation = locationResult.getLastLocation();

                    }
                },
                Looper.myLooper());
    }


}