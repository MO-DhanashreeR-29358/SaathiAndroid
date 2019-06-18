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
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.app.core.data.UiModel;
import com.mosl.app.core.data.UiModelHelper;
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem;
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest;
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdRequest;
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdResponse;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest;
import com.mosl.saathi.api.model.createmeeting.ClientImageRequest;
import com.mosl.saathi.api.model.lead.Contact;
import com.mosl.saathi.api.model.lead.LeadProductMappingItem;
import com.mosl.saathi.api.model.lead.LeadViewItem;
import com.mosl.saathi.api.model.lead.LeadViewRequest;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
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
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.CRMAppUtil.isLocationFromMockProvider;
import static com.mosl.tabapp.common.CRMMOSLCommon.disableField;
import static com.mosl.tabapp.common.CRMMOSLCommon.enableLoc;
import static com.mosl.tabapp.common.CRMMOSLCommon.hasGPSDevice;
import static com.mosl.tabapp.common.CRMMOSLCommon.setPermisson;
import static com.mosl.tabapp.leadViewDetail.SetAppointmentFragment.listHasAttende;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 3/7/2017.
 */

public class CreateMeetingFragment extends MOSLCommonFragment
    implements View.OnClickListener, TextView.OnEditorActionListener {
    View createMeetingView;
    private ImageView ivLead;
    EditText etCustomerComment, etMeetingDescription, etCategory, etSubCategory, etClientCode,
        etClientName, etAmount, etLocation, etPinCode;
    Location mCurrentLocation; // location
    private TextView tvCurrentStageSince, tvCurrentStage, tvBranchCode, tvLeadId, tvClientCode,
        tvName, tvSplAttendeeCount, tvAttendeeCount, tvStartDate,
        tvLocationError, tvPinCodeError, tvMeetingLocationError, tvTravellingModeError;
    View llSubcategory;
    private Button btnSave, btnReset;
    private ImageButton ibSplAttendee, ibAttendee;
    private AttendeeAdapter attendeeAdapter;
    private SplAttendeeAdapter splAttendeeAdapter;
    private AddedAttendeeAdapter addedAttendeeAdapter;
    private AddedSplAttendeeAdapter addedSplAttendeeAdapter;
    ArrayList<AddedAttendeeModel> addedAttendeeModelList = new ArrayList<>();
    ArrayList<AddedSplAttendeeModel> addedSplAttendeeModelList = new ArrayList<>();
    int addedAttendeeCount;
    int addedSplAttendeeCount;
    private String mainFromDate, mainToDate, fromPostingDate, productCodeStr, productNameStr,
        meetingDurationCodeStr, activityTypeCodeStr,
        meetingReasonCodeStr, meetingReasonNameStr, actionablesCodeStr,
        categoryCodeStr, subCategoryCodestr, outStaitionCodeStr, baClientCodeStr;
    private Date fromDat;
    private Dialog dialog;
    private Calendar today;
    private DatePicker dpFromDate;
    private ImageView close;
    private Button btnSubmit;
    private Spinner spProduct, spMeetingDuration, spActivityType, spMeetingReason, spActionables,
        spSubCategory, spCategory, spBAClient, spOutStaition, spMeetingObjective, spMeetingLocation,
        spTravellingMode;
    private final ArrayList<RowData> arrCategory = new ArrayList<>();
    private final ArrayList<RowData> arrMeetingObjective = new ArrayList<>();
    private final ArrayList<RowData> arrSelectProduct = new ArrayList<>();
    private final ArrayList<RowData> arrMeetingDuration = new ArrayList<>();
    private final ArrayList<RowData> arrActivityType = new ArrayList<>();
    private final ArrayList<RowData> arrMeetingReason = new ArrayList<>();
    private final ArrayList<RowData> arrActionables = new ArrayList<>();
    private final ArrayList<RowData> arrSubCategory = new ArrayList<>();
    private final ArrayList<RowData> arrBAClient = new ArrayList<>();
    private final ArrayList<RowData> arrOutStation = new ArrayList<>();
    private final ArrayList<RowData> arrMeetingLocation = new ArrayList<>();
    private final ArrayList<RowData> arrTravellingMode = new ArrayList<>();
    private String leadId;
    private int currentYear, month, day, hour, minute;
    private Calendar calendar = Calendar.getInstance();
    private TextView tvProductError, tvMeetingDurationError, tvMeetingObjectiveError,
        tvMeetingReasonError, tvActivityTypeError, tvActionablesError, tvMeetingDescriptionError,
        tvCategoryError, tvCategoryCommentsError, tvSubCategoryError, tvSubCategoryCommentsError,
        tvActionables, tvBAClientError, tvOutStaitionError, tvClientCodeError, tvClientNameError;
    private RelativeLayout rlDate;
    private ScrollView scrollView;
    private LinearLayout llActionables, linaerLayoutNew, llLocationGroup;
    private boolean profileType;
    private String mUCID = "0";
    private String meetingObjectiveIdStr = "0";
    private boolean mandatory = true, isPotentialPMSProduct, isOnlyClient;
    private CreateMeetingProductAdapter productAdapter;
    private CreateMeetingTravellingModeAdapter travellingAdapter;
    private RecyclerView rvProductList, rvTravellingList;
    private ArrayList<CreateMeetingProductModel> productList;
    private ArrayList<CreateMeetingTravellingModel> travellingList;
    private ArrayAdapter<RowData> arrAdapSelectProduct, arrAdapMeetingDuration, arrAdapActivityType,
        arrAdapMeetingReason, arrAdapActionables, arrAdapCategory, arrAdapSubCategory,
        arrAdapBAClient, arrAdapOutStation, arrAdapMeetingsObjective, arrAdapMeetingLocation,
        arrAdapTravellingMode;

    private long UPDATE_INTERVAL = 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private boolean enableLocationGroup;
    private AdapterView.OnItemSelectedListener selectMeetingLocation =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!((RowData) spMeetingLocation.getSelectedItem()).getKey()
                        .equalsIgnoreCase("-1")
                        && !((RowData) spMeetingLocation.getSelectedItem()).getValue()
                        .equalsIgnoreCase("other")) {
                        GetAddressByLeadId(
                            ((RowData) spMeetingLocation.getSelectedItem()).getKey());
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
    private AdapterView.OnItemSelectedListener selectSubCategoryListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    subCategoryCodestr = ((RowData) spSubCategory.getSelectedItem()).getKey();
                    tvSubCategoryError.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener selectCategoryListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    categoryCodeStr = ((RowData) spCategory.getSelectedItem()).getKey();
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
    private AdapterView.OnItemSelectedListener selectProductListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    productCodeStr = ((RowData) spProduct.getSelectedItem()).getKey();
                    productNameStr = ((RowData) spProduct.getSelectedItem()).getValue();
                    if (!productCodeStr.equalsIgnoreCase("-1")) {
                        productAdapter.addProduct(
                            new CreateMeetingProductModel(productCodeStr, productNameStr));
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
    private AdapterView.OnItemSelectedListener selectTravellingMode =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    if (!((RowData) spTravellingMode.getSelectedItem()).getKey()
                        .equalsIgnoreCase("-1")) {
                        travellingAdapter.addTravellingMode(new CreateMeetingTravellingModel(
                            ((RowData) spTravellingMode.getSelectedItem()).getKey(),
                            ((RowData) spTravellingMode.getSelectedItem()).getValue()));
                    }

                    tvTravellingModeError.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener selectMeetingDurationListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    meetingDurationCodeStr =
                        ((RowData) spMeetingDuration.getSelectedItem()).getKey();
                    tvMeetingDurationError.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener selectMeetingReasonListener =
        new AdapterView.OnItemSelectedListener() {
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
    private AdapterView.OnItemSelectedListener selectActionablesListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    actionablesCodeStr = ((RowData) spActionables.getSelectedItem()).getKey();
                    tvActionablesError.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener selectBAClientListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    baClientCodeStr = ((RowData) spBAClient.getSelectedItem()).getKey();
                    tvBAClientError.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener selectOutStationListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    outStaitionCodeStr = ((RowData) spOutStaition.getSelectedItem()).getKey();
                    tvOutStaitionError.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener selectActivityTypeListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    activityTypeCodeStr = ((RowData) spActivityType.getSelectedItem()).getKey();
                    tvActivityTypeError.setVisibility(View.GONE);
                    if (!spActivityType.getSelectedItem()
                        .toString()
                        .equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
                        arrMeetingReason.clear();
                        arrMeetingReason.add(new RowData(0, "-1", SELECT_MEETING_REASON));
                        meetingReason();
                        if (spActivityType.getSelectedItem().toString().equalsIgnoreCase("Email")
                            || spActivityType.getSelectedItem()
                            .toString()
                            .equalsIgnoreCase("Phone")) {
                            enableLocationGroup = false;
                            disableField(llLocationGroup);
                            disableReset();
                        } else {
                            enableLocationGroup = true;
                            enableField(llLocationGroup);
                        }
                    } else {
                        arrMeetingReason.clear();
                        arrMeetingReason.add(new RowData(0, "-1", SELECT_MEETING_REASON));
                        arrAdapMeetingReason =
                            new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                                arrMeetingReason);
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
    private AdapterView.OnItemSelectedListener selectMeetingObjective =
        new AdapterView.OnItemSelectedListener() {
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        createMeetingView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_create_meeting);
        setUpView(createMeetingView);
        startLocationUpdates();
        try {
            leadId = this.getArguments().getString(LEAD_ID, "0");
            mUCID = this.getArguments().getString(UCID, "0").equalsIgnoreCase("") ? "0"
                : this.getArguments().getString(UCID, "0");
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

    private void setUpView(View createMeetingView) {

        scrollView = createMeetingView.findViewById(R.id.scrollView);
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
        rlDate = createMeetingView.findViewById(R.id.rlDate);
        tvStartDate = createMeetingView.findViewById(R.id.tvDate);
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        mainFromDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        tvStartDate.setText(CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));
        rlDate.setOnClickListener(this);
        ivLead = createMeetingView.findViewById(R.id.ivLead);
        tvName = createMeetingView.findViewById(R.id.tvName);
        tvClientCode = createMeetingView.findViewById(R.id.tvClientCode);
        tvLeadId = createMeetingView.findViewById(R.id.tvLeadId);
        tvBranchCode = createMeetingView.findViewById(R.id.tvBranchCode);
        tvCurrentStage = createMeetingView.findViewById(R.id.tvCurrentStage);
        tvCurrentStageSince = createMeetingView.findViewById(R.id.tvCurrentStageSince);

        tvSplAttendeeCount = createMeetingView.findViewById(R.id.tvSplAttendeeCount);
        tvAttendeeCount = createMeetingView.findViewById(R.id.tvAttendeeCount);
        tvProductError = createMeetingView.findViewById(R.id.tvProductError);
        tvMeetingDurationError =
            createMeetingView.findViewById(R.id.tvMeetingDurationError);
        tvMeetingObjectiveError =
            createMeetingView.findViewById(R.id.tvMeetingObjectiveError);
        tvMeetingReasonError = createMeetingView.findViewById(R.id.tvMeetingReasonError);
        tvActivityTypeError = createMeetingView.findViewById(R.id.tvActivityTypeError);
        tvActionablesError = createMeetingView.findViewById(R.id.tvActionablesError);
        tvCategoryError = createMeetingView.findViewById(R.id.tvCategoryError);
        tvSubCategoryError = createMeetingView.findViewById(R.id.tvSubCategoryError);
        tvMeetingDescriptionError =
            createMeetingView.findViewById(R.id.tvMeetingDescriptionError);
        tvSubCategoryCommentsError =
            createMeetingView.findViewById(R.id.tvSubCategoryCommentsError);
        tvCategoryCommentsError =
            createMeetingView.findViewById(R.id.tvCategoryCommentsError);
        tvLocationError = createMeetingView.findViewById(R.id.tvLocationError);
        tvPinCodeError = createMeetingView.findViewById(R.id.tvPinCodeError);
        tvMeetingLocationError =
            createMeetingView.findViewById(R.id.tvMeetingLocationError);
        tvTravellingModeError =
            createMeetingView.findViewById(R.id.tvTravellingModeError);

        tvBAClientError = createMeetingView.findViewById(R.id.tvBAClientError);
        tvOutStaitionError = createMeetingView.findViewById(R.id.tvOutStaitionError);
        tvClientCodeError = createMeetingView.findViewById(R.id.tvClientCodeError);
        tvClientNameError = createMeetingView.findViewById(R.id.tvClientNameError);

        tvActionables = createMeetingView.findViewById(R.id.tvActionables);
        llActionables = createMeetingView.findViewById(R.id.llActionables);
        linaerLayoutNew = createMeetingView.findViewById(R.id.linaerLayoutNew);
        llLocationGroup = createMeetingView.findViewById(R.id.llLocationGroup);
        setIcon(llActionables, tvActionables);
        tvActionables.setOnClickListener(this);
        btnSave = createMeetingView.findViewById(R.id.btnSave);
        btnReset = createMeetingView.findViewById(R.id.btnReset);
        ibSplAttendee = createMeetingView.findViewById(R.id.ibSplAttendee);
        ibAttendee = createMeetingView.findViewById(R.id.ibAttendee);

        etCustomerComment = createMeetingView.findViewById(R.id.etCustomerComment);
        etCustomerComment.setFilters(
            new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etMeetingDescription = createMeetingView.findViewById(R.id.etMeetingDescription);
        etMeetingDescription.setFilters(
            new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etCategory = createMeetingView.findViewById(R.id.etCategory);
        etCategory.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etSubCategory = createMeetingView.findViewById(R.id.etSubCategory);
        etSubCategory.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etClientCode = createMeetingView.findViewById(R.id.etClientCode);
        etClientName = createMeetingView.findViewById(R.id.etClientName);
        etAmount = createMeetingView.findViewById(R.id.etAmount);
        etLocation = createMeetingView.findViewById(R.id.etLocation);
        etPinCode = createMeetingView.findViewById(R.id.etPinCode);
        etLocation.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        llSubcategory = createMeetingView.findViewById(R.id.llSubcategory);

        spCategory = createMeetingView.findViewById(R.id.spCategory);
        spSubCategory = createMeetingView.findViewById(R.id.spSubCategory);
        spProduct = createMeetingView.findViewById(R.id.spProduct);
        spMeetingObjective = createMeetingView.findViewById(R.id.spMeetingObjective);
        spMeetingDuration = createMeetingView.findViewById(R.id.spMeetingDuration);
        spActivityType = createMeetingView.findViewById(R.id.spActivityType);
        spMeetingReason = createMeetingView.findViewById(R.id.spMeetingReason);
        spActionables = createMeetingView.findViewById(R.id.spActionables);
        spBAClient = createMeetingView.findViewById(R.id.spBAClient);
        spOutStaition = createMeetingView.findViewById(R.id.spOutStaition);
        spMeetingLocation = createMeetingView.findViewById(R.id.spMeetingLocation);
        spTravellingMode = createMeetingView.findViewById(R.id.spTravellingMode);
        rvProductList = (RecyclerView) createMeetingView.findViewById(R.id.rvProductList);
        rvTravellingList = (RecyclerView) createMeetingView.findViewById(R.id.rvTravellingList);
        productList = new ArrayList<>();
        productAdapter = new CreateMeetingProductAdapter(productList, getContext());
        rvProductList.setLayoutManager(new GridLayoutManager(getMainActivity(), 1));
        rvProductList.setAdapter(productAdapter);
        travellingList = new ArrayList<>();
        travellingAdapter = new CreateMeetingTravellingModeAdapter(travellingList, getContext());
        rvTravellingList.setLayoutManager(new GridLayoutManager(getMainActivity(), 1));
        rvTravellingList.setAdapter(travellingAdapter);
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
        if (AccountDetails.getProfileType().equalsIgnoreCase(FRANCHIES)
            || AccountDetails.getProfileType().equalsIgnoreCase(IFA)) {
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
                if (!isBlank(etSubCategory.getText().toString().trim())
                    && etSubCategory.getText().length() >= 10) {
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
                if (!isBlank(etLocation.getText().toString().trim())
                    && etLocation.getText().length() >= 20) {
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
                if (!isBlank(etPinCode.getText().toString().trim())
                    && etPinCode.getText().length() >= 6) {
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
                if (!isBlank(etMeetingDescription.getText().toString().trim())
                    && etMeetingDescription.getText().length() >= 50) {
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
                if (!isBlank(etCategory.getText().toString().trim())
                    && etCategory.getText().length() >= 10) {
                    tvCategoryCommentsError.setVisibility(View.GONE);
                } else {
                    tvCategoryCommentsError.setVisibility(View.VISIBLE);
                    tvCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
                }
            }
        });
    }

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
                    meetingApptModel.setPincode(etPinCode.getText().toString());
                    meetingApptModel.setAmount(etAmount.getText().toString());
                    meetingApptModel.setMeetingAddress(etLocation.getText().toString());
                    meetingApptModel.setMeetingAddressTypeId(
                        ((RowData) spMeetingLocation.getSelectedItem()).getKey());
                    meetingApptModel.setTravellingList(travellingAdapter.getTravellingList());
                    meetingApptModel.setLeadId(leadId);
                    meetingApptModel.setUcid(mUCID);
                    meetingApptModel.setMeetingDate(
                        CRMAppUtil.getDBDate(currentYear, month, day, hour, minute));
                    meetingApptModel.setProduct("0");
                    meetingApptModel.setProductList(productAdapter.getListProduct());
                    meetingApptModel.setMeetingDuration(meetingDurationCodeStr);
                    meetingApptModel.setMeetingObjectiveID(meetingObjectiveIdStr);
                    meetingApptModel.setActivityTypeID(activityTypeCodeStr);
                    meetingApptModel.setMeetingReason(meetingReasonCodeStr);
                    meetingApptModel.setMeetingDes(
                        getJsonSafeString(etMeetingDescription.getText().toString().trim()));
                    meetingApptModel.setMeetingAttendee(addedAttendeeModelList);
                    meetingApptModel.setSplMeetingAttendee(addedSplAttendeeModelList);
                    meetingApptModel.setCustomerComment(
                        getJsonSafeString(etCustomerComment.getText().toString().trim()));
                    meetingApptModel.setActionables(actionablesCodeStr);
                    meetingApptModel.setCategory(categoryCodeStr);
                    meetingApptModel.setEtCategory(etCategory.getText().toString().trim());
                    meetingApptModel.setSubCategory(subCategoryCodestr);
                    meetingApptModel.setEtSubCategory(etSubCategory.getText().toString().trim());
                    meetingApptModel.setBaClient(baClientCodeStr);
                    meetingApptModel.setOutStation(outStaitionCodeStr);
                    meetingApptModel.setClientCode(etClientCode.getText().toString().trim());
                    meetingApptModel.setClientName(etClientName.getText().toString().trim());
                    if (mCurrentLocation != null) {
                        Geocoder geocoder = new Geocoder(getMainActivity(), Locale.getDefault());
                        try {
                            List<Address> addressList =
                                geocoder.getFromLocation(mCurrentLocation.getLatitude(),
                                    mCurrentLocation.getLongitude(), 1);
                            meetingApptModel.setGeoArea(addressList.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        meetingApptModel.setGeoLatitude(mCurrentLocation.getLatitude() + "");
                        meetingApptModel.setGeoLongitude(mCurrentLocation.getLongitude() + "");
                    }
                    if (enableLocationGroup && mCurrentLocation == null) {
                        getLatLong(meetingApptModel);
                    } else {
                        isMockLocation(meetingApptModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isMockLocation(MeetingApptModel meetingApptModel) {
        if (enableLocationGroup && isLocationFromMockProvider(getMainActivity(),
            mCurrentLocation)) {
            MSFDialog.alertDialog(getActivity(), 0,
                getResources().getString(R.string.motilal_oswal),
                "Mocked location enabled. Please uninstall mock location apps to continue ", "OK",
                true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {
                            getActivity().finish();
                        }
                    }
                });
        } else {
            if (isPotentialPMSProduct && !isOnlyClient) {
                MSFDialog.alertDialog(getActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    POTENTIAL_INVEST_IN_PRODUCT, YES, NO, true,
                    new MSFDialog.DialogListener() {
                        @Override
                        public void alertDialogAction(MSFDialog.Action action,
                            Object... data) {
                            if (action == MSFDialog.Action.OK) {
                                meetingApptModel.setIsPotentialPMSProduct(YES);
                                createMeetingSave(meetingApptModel);
                            } else {
                                meetingApptModel.setIsPotentialPMSProduct(NO);
                                createMeetingSave(meetingApptModel);
                            }
                        }
                    });
            } else {
                meetingApptModel.setIsPotentialPMSProduct("");
                createMeetingSave(meetingApptModel);
            }
        }
    }

    private LocationRequest getmLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        return mLocationRequest;
    }

    private void getLatLong(MeetingApptModel meetingApptModel) {
        Handler mainHandler = new Handler(getMainActivity().getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getMainActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getMainActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                }
                getFusedLocationProviderClient(getMainActivity()).requestLocationUpdates(
                    getmLocationRequest(), new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            mCurrentLocation = locationResult.getLastLocation();
                        }
                    }, Looper.myLooper());
                Geocoder geocoder = new Geocoder(getMainActivity(), Locale.getDefault());
                try {
                    List<Address> addressList =
                        geocoder.getFromLocation(mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude(), 1);
                    meetingApptModel.setGeoArea(addressList.get(0).getAddressLine(0));
                    meetingApptModel.setGeoLatitude(mCurrentLocation.getLatitude() + "");
                    meetingApptModel.setGeoLongitude(mCurrentLocation.getLongitude() + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isMockLocation(meetingApptModel);
            } // This is your code
        };
        mainHandler.post(myRunnable);
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
        productList.clear();
        travellingList.clear();
        productAdapter.deleteAllProduct();
        travellingAdapter.deleteAllProduct();
    }

    /**
     * form validation
     */

    private boolean validateForm() {
        if (spProduct.getSelectedItem().toString().equalsIgnoreCase(SELECT_PRODUCT)
            && productAdapter.getListProduct().size() == 0) {
            tvProductError.setVisibility(View.VISIBLE);
            tvProductError.setText(SELECT_PRODUCT);
            tvProductError.requestFocus();
            return false;
        } else if (productAdapter.getListProduct().size() == 0) {
            tvProductError.setVisibility(View.VISIBLE);
            tvProductError.setText(SELECT_PRODUCT);
            tvProductError.requestFocus();
            return false;
        } else if (spMeetingDuration.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_MEETING_DURATION)) {
            tvMeetingDurationError.setVisibility(View.VISIBLE);
            tvMeetingDurationError.setText(SELECT_MEETING_DURATION);
            tvMeetingDurationError.requestFocus();
            return false;
        } else if (spMeetingObjective.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_MEETING_OBJECTIVE)) {
            tvMeetingObjectiveError.setVisibility(View.VISIBLE);
            tvMeetingObjectiveError.setText(SELECT_MEETING_OBJECTIVE);
            tvMeetingObjectiveError.requestFocus();
            return false;
        } else if (spActivityType.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
            tvActivityTypeError.setVisibility(View.VISIBLE);
            tvActivityTypeError.setText(SELECT_ACTIVITY_TYPE);
            tvActivityTypeError.requestFocus();
            return false;
        } else if (spMeetingReason.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_MEETING_REASON)) {
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
        } else if (enableLocationGroup && spMeetingLocation.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_MEETING_LOCATION)) {
            tvMeetingLocationError.setVisibility(View.VISIBLE);
            tvMeetingLocationError.setText(SELECT_MEETING_LOCATION);
            tvMeetingLocationError.requestFocus();
            return false;
        }
        if (enableLocationGroup
            && spTravellingMode.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_TRAVELLING_MODE)
            && travellingAdapter.getTravellingList().size() == 0) {
            tvTravellingModeError.setVisibility(View.VISIBLE);
            tvTravellingModeError.setText(SELECT_TRAVELLING_MODE);
            tvTravellingModeError.requestFocus();
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
        } else if (enableLocationGroup && etPinCode.getText().length() < 6) {
            tvPinCodeError.setVisibility(View.VISIBLE);
            tvPinCodeError.setText(INVALID_PINCODE_NUMBER);
            tvPinCodeError.requestFocus();
            return false;
        } else if (profileType && spBAClient.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_BA_CLIENT_MEETING)) {
            tvBAClientError.setVisibility(View.VISIBLE);
            tvBAClientError.setText(SELECT_BA_CLIENT_MEETING);
            tvBAClientError.requestFocus();
            return false;
        } else if (profileType && spOutStaition.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_OUT_STATION_MEETING)) {
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
        } else if ((llActionables.getVisibility() == View.VISIBLE)
            && spActionables.getSelectedItem().toString().equalsIgnoreCase(SELECT_ACTIONABLES)) {
            tvActionablesError.setVisibility(View.VISIBLE);
            tvActionablesError.setText(SELECT_ACTIONABLES);
            tvActionablesError.requestFocus();
            return false;
        } else if ((llActionables.getVisibility() == View.VISIBLE) && spCategory.getSelectedItem()
            .toString()
            .equalsIgnoreCase(SELECT_CATEGORY)) {
            tvCategoryError.setVisibility(View.VISIBLE);
            tvCategoryError.setText(SELECT_CATEGORY);
            tvCategoryError.requestFocus();
            return false;
        } else if ((llActionables.getVisibility() == View.VISIBLE)
            && etCategory.getText().length() <= 10) {
            tvCategoryCommentsError.setVisibility(View.VISIBLE);
            tvCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
            tvCategoryCommentsError.requestFocus();
            return false;
        } else if (mandatory
            && (llActionables.getVisibility() == View.VISIBLE)
            && spSubCategory.getSelectedItem().toString().equalsIgnoreCase(SELECT_SUB_CATEGORY)) {
            tvSubCategoryError.setVisibility(View.VISIBLE);
            tvSubCategoryError.setText(SELECT_SUB_CATEGORY);
            tvSubCategoryError.requestFocus();
            return false;
        } else if (mandatory
            && (llActionables.getVisibility() == View.VISIBLE)
            && etSubCategory.getText().length() <= 10) {
            tvSubCategoryCommentsError.setVisibility(View.VISIBLE);
            tvSubCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
            tvSubCategoryCommentsError.requestFocus();
            return false;
        } else if (enableLocationGroup
            && ActivityCompat.checkSelfPermission(getMainActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getMainActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            setPermisson(getMainActivity(), getMainActivity());
            return false;
        } else if (enableLocationGroup
            && !((LocationManager) getActivity().getSystemService(
            Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)
            && hasGPSDevice(getMainActivity())) {

            enableLoc(getMainActivity());

            return false;
        }
        return true;
    }

    private void setProfileImage(String ucid) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientImageRequest request =
                new ClientImageRequest(ucid);
            Disposable subscribe = saathiApi.getClientImage(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderClientImage);
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, OK, true, null);
        }
    }

    private void renderClientImage(UiModel<String> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        String data = uiModel.data;
        if (data != null) {
            byte[] byt = Base64.decode(data, Base64.DEFAULT);
            if (byt != null) {
                CRMAppUtil.setImage(byt, ivLead, getMainActivity());
            } else {
                CRMAppUtil.setImage(new byte[0], ivLead,
                    getMainActivity());
            }
        }
    }

    //region Dropdown Web calls
    private void getClientDetail(String ucid) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientDetailRequest request =
                new ClientDetailRequest(ucid);
            Disposable subscribe = saathiApi.postClientDetail(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderClientDetails);
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, OK, true, null);
        }
    }

    private void renderClientDetails(UiModel<ClientDetailItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        ClientDetailItem data = uiModel.data;
        if (data != null) {
            new ClientDetail(data);
            tvLeadId.setText(ClientDetail.getLeadId());
            tvCurrentStage.setText(ClientDetail.getCurrentStage());
            if (!ClientDetail.getCurrentStageSince().isEmpty()) {
                tvCurrentStageSince.setText(
                    ClientDetail.getCurrentStageSince() + " Days");
            }
            tvName.setText(ClientDetail.getName());
            tvClientCode.setText(ClientDetail.getClientCode());
            tvBranchCode.setText(ClientDetail.getBranchCode());
            String IsPotentialPMSProduct = stringOrEmpty(data.isPotentialPMSProduct);
            isPotentialPMSProduct = IsPotentialPMSProduct.isEmpty();
            isOnlyClient = data.isOnlyClient;
        }
    }

    private void getLeadByIdFromServer(CharSequence strLeadId) {
        CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LeadViewRequest request =
                new LeadViewRequest(String.valueOf(strLeadId));
            Disposable subscribe = saathiApi.postLeadView(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderLeadView);
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, OK, true, null);
        }
    }

    private void renderLeadView(UiModel<LeadViewItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        LeadViewItem data = uiModel.data;
        if (data != null) {
            String leadId = stringOrEmpty(data.leadID);
            String branchCode = stringOrEmpty(data.branchcode);
            Contact contact = data.contact;
            String name = stringOrEmpty(contact.name);

            String isPotentialProduct = stringOrEmpty(data.isPotentialPMSProduct);
            isPotentialPMSProduct = isPotentialProduct.isEmpty();
            List<LeadProductMappingItem> leadProductMapping = data.leadProductMapping;
            for (int i = 0; i < leadProductMapping.size(); i++) {
                LeadProductMappingItem item = leadProductMapping.get(i);
                String clientCode = stringOrEmpty(item.clientCode);
                String stageName = stringOrEmpty(item.stageName);
                String isDefault = stringOrEmpty(item.isDefault);
                String pendingDays = stringOrEmpty(item.pendingDays);
                if (isDefault.equalsIgnoreCase("true")) {
                    tvClientCode.setText(clientCode);
                    tvCurrentStage.setText(stageName);
                    tvCurrentStageSince.setText(pendingDays + " Days");
                }
            }
            tvName.setText(name);
            tvLeadId.setText(leadId);
            tvBranchCode.setText(branchCode);
        }
    }

    private void category() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), CATEGORY, null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderCategory);
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void renderCategory(
        UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrCategory.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrCategory.clear();
            arrCategory.add(new RowData(0, "-1", SELECT_CATEGORY));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrCategory.add(new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapCategory = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrCategory);
            arrAdapCategory.setDropDownViewResource(R.layout.mdropdown);
            spCategory.setAdapter(arrAdapCategory);
            spCategory.setOnItemSelectedListener(
                selectCategoryListener);
            arrAdapCategory.notifyDataSetChanged();
        }
    }

    private void meetingObjective() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), MEETINGOBJECTIVE, null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderMeetingObjective);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderMeetingObjective(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrMeetingObjective.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrMeetingObjective.clear();
            arrMeetingObjective.add(new RowData(0, "-1", SELECT_MEETING_OBJECTIVE));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);

                arrMeetingObjective.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapMeetingsObjective =
                new ArrayAdapter<>(getMainActivity(),
                    R.layout.simple_spinner_item, arrMeetingObjective);
            arrAdapMeetingsObjective.setDropDownViewResource(
                R.layout.mdropdown);
            spMeetingObjective.setAdapter(arrAdapMeetingsObjective);
            spMeetingObjective.setOnItemSelectedListener(
                selectMeetingObjective);
            arrAdapMeetingsObjective.notifyDataSetChanged();
        }
    }

    private void subCategory() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), SUBCATEGORY, categoryCodeStr,
                    null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::rendrerSubCategory);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void rendrerSubCategory(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrSubCategory.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrSubCategory.clear();
            arrSubCategory.add(new RowData(0, "-1", SELECT_SUB_CATEGORY));
            mandatory = data.size() > 1;
            if (mandatory) {
                llSubcategory.setVisibility(View.VISIBLE);
            } else {
                etSubCategory.setText("");
                llSubcategory.setVisibility(View.GONE);
            }
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);

                arrSubCategory.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapSubCategory = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrSubCategory);
            arrAdapSubCategory.setDropDownViewResource(
                R.layout.mdropdown);
            spSubCategory.setAdapter(arrAdapSubCategory);
            spSubCategory.setOnItemSelectedListener(
                selectSubCategoryListener);
            arrAdapSubCategory.notifyDataSetChanged();
        }
    }

    private void selectProduct() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            meetingDuration();
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), INTERESTEDIN, null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderProduct);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderProduct(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrSelectProduct.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrSelectProduct.clear();
            arrSelectProduct.add(new RowData(0, "-1", SELECT_PRODUCT));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrSelectProduct.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapSelectProduct = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrSelectProduct);
            arrAdapSelectProduct.setDropDownViewResource(
                R.layout.mdropdown);
            spProduct.setAdapter(arrAdapSelectProduct);
            spProduct.setOnItemSelectedListener(selectProductListener);
            arrAdapSelectProduct.notifyDataSetChanged();
        }
    }

    private void meetingDuration() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            activityType();
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), MEETINGDURATION, null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderMeetingDuration);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderMeetingDuration(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrMeetingDuration.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrMeetingDuration.clear();
            arrMeetingDuration.add(new RowData(0, "-1", SELECT_MEETING_DURATION));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrMeetingDuration.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapMeetingDuration =
                new ArrayAdapter<>(getMainActivity(),
                    R.layout.simple_spinner_item, arrMeetingDuration);
            arrAdapMeetingDuration.setDropDownViewResource(
                R.layout.mdropdown);
            spMeetingDuration.setAdapter(arrAdapMeetingDuration);
            spMeetingDuration.setOnItemSelectedListener(
                selectMeetingDurationListener);
            arrAdapMeetingDuration.notifyDataSetChanged();
        }
    }

    private void activityType() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), ACTIVITYTYPE, null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderActivityType);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderActivityType(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrActivityType.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrActivityType.clear();
            arrActivityType.add(
                new RowData(0, "-1", SELECT_ACTIVITY_TYPE));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrActivityType.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapActivityType = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrActivityType);
            arrAdapActivityType.setDropDownViewResource(
                R.layout.mdropdown);
            spActivityType.setAdapter(arrAdapActivityType);
            spActivityType.setOnItemSelectedListener(
                selectActivityTypeListener);
            arrAdapActivityType.notifyDataSetChanged();
        }
    }

    private void meetingReason() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            baClient();
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), MEETINGREASON,
                    activityTypeCodeStr, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderMeetingReason);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderMeetingReason(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrMeetingReason.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrMeetingReason.clear();
            arrMeetingReason.add(new RowData(0, "-1", SELECT_MEETING_REASON));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrMeetingReason.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapMeetingReason = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrMeetingReason);
            arrAdapMeetingReason.setDropDownViewResource(
                R.layout.mdropdown);
            spMeetingReason.setAdapter(arrAdapMeetingReason);
            spMeetingReason.setOnItemSelectedListener(
                selectMeetingReasonListener);
            arrAdapMeetingReason.notifyDataSetChanged();
        }
    }

    private void baClient() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            outStation();
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), BACLIENTMEETING,
                    null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderBaClient);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderBaClient(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrBAClient.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrBAClient.clear();
            arrBAClient.add(new RowData(0, "-1", SELECT_BA_CLIENT_MEETING));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrBAClient.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapBAClient = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrBAClient);
            arrAdapBAClient.setDropDownViewResource(R.layout.mdropdown);
            spBAClient.setAdapter(arrAdapBAClient);
            spBAClient.setOnItemSelectedListener(
                selectBAClientListener);
            arrAdapBAClient.notifyDataSetChanged();
        }
    }

    private void outStation() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            actionables();
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), OUTSTATIONMEETING,
                    null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderOutStation);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderOutStation(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrOutStation.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrOutStation.clear();
            arrOutStation.add(new RowData(0, "-1", SELECT_BA_CLIENT_MEETING));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrOutStation.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapOutStation = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrOutStation);
            arrAdapOutStation.setDropDownViewResource(
                R.layout.mdropdown);
            spOutStaition.setAdapter(arrAdapOutStation);
            spOutStaition.setOnItemSelectedListener(
                selectOutStationListener);
            arrAdapOutStation.notifyDataSetChanged();
        }
    }

    private void actionables() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            category();
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), ACTIONABLES, null, null);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderActionables);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderActionables(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrActionables.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrActionables.clear();
            arrActionables.add(new RowData(0, "-1", SELECT_ACTIONABLES));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrActionables.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapActionables = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrActionables);
            arrAdapActionables.setDropDownViewResource(
                R.layout.mdropdown);
            spActionables.setAdapter(arrAdapActionables);
            spActionables.setOnItemSelectedListener(
                selectActionablesListener);
            arrAdapActionables.notifyDataSetChanged();
        }
    }

    private void meetingLocation() {
        travellingMode();
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest("0", MEETINGADDRESSID, null, true);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderMeetingLocation);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderMeetingLocation(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrMeetingLocation.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrMeetingLocation.clear();
            arrMeetingLocation.add(new RowData(0, "-1", SELECT_MEETING_LOCATION));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrMeetingLocation.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapMeetingLocation =
                new ArrayAdapter<>(getMainActivity(),
                    R.layout.simple_spinner_item, arrMeetingLocation);
            arrAdapMeetingLocation.setDropDownViewResource(
                R.layout.mdropdown);
            spMeetingLocation.setAdapter(arrAdapMeetingLocation);
            spMeetingLocation.setOnItemSelectedListener(
                selectMeetingLocation);
            arrAdapMeetingLocation.notifyDataSetChanged();
        }
    }

    private void GetAddressByLeadId(String meetingId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            AddressByLeadIdRequest request =
                new AddressByLeadIdRequest(tvLeadId.getText().toString(), meetingId);
            Disposable subscribe = saathiApi.getAddressByLeadId(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderAddressByLeadId);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderAddressByLeadId(UiModel<AddressByLeadIdResponse> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        AddressByLeadIdResponse data = uiModel.data;
        if (data != null) {
            String meetingAddress = data.meetingAddress;
            etLocation.setText(meetingAddress);
        }
    }

    private void travellingMode() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest("0", TRAVELLINGMODEID, null, true);
            Disposable disposable = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderTravellingMode);
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderTravellingMode(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrTravellingMode.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrTravellingMode.clear();
            arrTravellingMode.add(
                new RowData(0, "-1", SELECT_TRAVELLING_MODE));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrTravellingMode.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapTravellingMode =
                new ArrayAdapter<>(getMainActivity(),
                    R.layout.simple_spinner_item, arrTravellingMode);
            arrAdapTravellingMode.setDropDownViewResource(
                R.layout.mdropdown);
            spTravellingMode.setAdapter(arrAdapTravellingMode);
            spTravellingMode.setOnItemSelectedListener(
                selectTravellingMode);
            arrAdapTravellingMode.notifyDataSetChanged();
        }
    }

    public void selectDate() {
        today = Calendar.getInstance();

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDate = dialog.findViewById(R.id.datePickerFromDate);
        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        close = dialog.findViewById(R.id.ibtnClose);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        Calendar cal = Calendar.getInstance();
        cal.add(cal.DAY_OF_MONTH, -3);
        dpFromDate.setMinDate(cal.getTimeInMillis());
        dpFromDate.setMaxDate(System.currentTimeMillis() - 1000);

        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYear = year;
                        month = monthOfYear + 1;
                        day = dayOfMonth;
                    }
                });
        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
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
                    if (mainFromDate != null) {
                        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
                        String finalSelectedDateStr = fromPostingDate
                            + " "
                            + hour
                            + ":"
                            + minute
                            + ":"
                            + "00"; //dd-MM-yyyy hh:mm:ss
                        Date finalSelectedDate =
                            CRMAppUtil.getDate(finalSelectedDateStr, "dd-MM-yyyy hh:mm");
                        if (CRMAppUtil.isBeforeandEqualDate(finalSelectedDate)) { //
                            tvStartDate.setText(
                                CRMAppUtil.getDBDate(finalSelectedDate, DATE_FORMAT_TO_DISPLAY));
                            dialog.dismiss();
                        } else {
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal), INVALID_TIME,
                                "OK", true, null);
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
        dialogAddedSplAttendee.getWindow()
            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddedSplAttendee.setContentView(R.layout.dialog_addedd_spl_attendee_list);
        TextView tvEmpty = dialogAddedSplAttendee.findViewById(R.id.tvEmpty);
        dialogAddedSplAttendee.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAddedSplAttendee.setCanceledOnTouchOutside(true);
        ListView lvAttendee = (ListView) dialogAddedSplAttendee.findViewById(R.id.lvAttendee);
        lvAttendee.setEmptyView(tvEmpty);
        addedSplAttendeeAdapter =
            new AddedSplAttendeeAdapter(getActivity(), addedSplAttendeeModelList);
        lvAttendee.setAdapter(addedSplAttendeeAdapter);
        addedSplAttendeeAdapter.notifyDataSetChanged();
        dialogAddedSplAttendee.show();
    }

    private void dialogAddedAttendee() {
        final Dialog dialogAddedAttendee = new Dialog(getContext());
        dialogAddedAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddedAttendee.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddedAttendee.setContentView(R.layout.dialog_addedd_attendee_list);
        TextView tvEmpty = dialogAddedAttendee.findViewById(R.id.tvEmpty);
        dialogAddedAttendee.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        dialogAttendeeList.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAttendeeList.setCanceledOnTouchOutside(true);
        final DelayAutoCompleteTextView etSearchAttendee =
            dialogAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSearchAttendee.setOnEditorActionListener(this);
        etSearchAttendee.setThreshold(3);
        attendeeAdapter = new AttendeeAdapter(getContext());
        etSearchAttendee.setAdapter(attendeeAdapter);
        etSearchAttendee.setLoadingIndicator(
            dialogAttendeeList.findViewById(R.id.pbLoadingIndicator));
        etSearchAttendee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EmployeeModel clientModel = attendeeAdapter.getItem(position);
                boolean has = listHasAttende(addedAttendeeModelList, addedSplAttendeeModelList,
                    clientModel.getAttendeeCode());
                if (!has) {
                    if (clientModel.isNoData()) {
                        return;
                    }
                    String attendeeType = "R"; //todo //R=Regular  & S=special
                    addedAttendeeModelList.add(
                        new AddedAttendeeModel(attendeeType, true, clientModel.getAttendeeName(),
                            clientModel.getAttendeeCode()));
                    addedAttendeeCount++;
                    setCount(addedAttendeeCount);
                    dialogAttendeeList.dismiss();
                } else {
                    Toast.makeText(getMainActivity(), ATTENDEE_ALREADY_ADDED, Toast.LENGTH_SHORT)
                        .show();
                    etSearchAttendee.setText("");
                }
            }
        });

        dialogAttendeeList.show();
    }

    private void dialogSplAttendee() {
        final Dialog dialogSplAttendeeList = new Dialog(getContext());
        dialogSplAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSplAttendeeList.getWindow()
            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSplAttendeeList.setContentView(R.layout.dialog_attendee_list);
        dialogSplAttendeeList.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSplAttendeeList.setCanceledOnTouchOutside(true);
        final DelayAutoCompleteTextView etSearchAttendee =
            dialogSplAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSearchAttendee.setOnEditorActionListener(this);
        etSearchAttendee.setThreshold(3);
        splAttendeeAdapter = new SplAttendeeAdapter(getContext());
        etSearchAttendee.setAdapter(splAttendeeAdapter);
        etSearchAttendee.setLoadingIndicator(
            dialogSplAttendeeList.findViewById(R.id.pbLoadingIndicator));
        etSearchAttendee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SplAttendeeModel clientModel = splAttendeeAdapter.getItem(position);
                boolean has = listHasAttende(addedAttendeeModelList, addedSplAttendeeModelList,
                    clientModel.getAttendeeCode());

                if (!has) {
                    if (clientModel.isNoData()) {
                        return;
                    }
                    String attendeeType = "S";
                    addedSplAttendeeModelList.add(
                        new AddedSplAttendeeModel(attendeeType, true, clientModel.getAttendeeName(),
                            clientModel.getAttendeeCode()));
                    addedSplAttendeeCount++;
                    setCountForSpecial(addedSplAttendeeCount);
                    dialogSplAttendeeList.dismiss();
                } else {
                    Toast.makeText(getMainActivity(), SPL_ATTENDEE_ALREADY_ADDED,
                        Toast.LENGTH_SHORT).show();
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
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                    requestJson, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (statusCode == 200) {
                                    hideProgress();
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        MSFDialog.alertDialog(getActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            CREATE_MEETING, "OK", true,
                                            new MSFDialog.DialogListener() {
                                                @Override
                                                public void alertDialogAction(
                                                    MSFDialog.Action action, Object... data) {
                                                    if (action == MSFDialog.Action.OK) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString(LEAD_ID,
                                                            getArguments().getString(LEAD_ID));
                                                        bundle.putString(UCID,
                                                            getArguments().getString(UCID));
                                                        navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle,
                                                            getFragmentManager(), false);
                                                    }
                                                }
                                            });
                                    } else {
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        hideKeyboard(textView);
        return false;
    }

    private void startLocationUpdates() {
        Handler mainHandler = new Handler(getMainActivity().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getMainActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getMainActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                } else {
                    getFusedLocationProviderClient(getMainActivity()).requestLocationUpdates(
                        getmLocationRequest(), new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                mCurrentLocation = locationResult.getLastLocation();
                            }
                        }, Looper.myLooper());
                }
            } // This is your code
        };
        mainHandler.post(myRunnable);
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
                inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode()
                    + "-"
                    + clientModel.getAttendeeName()
                    + "-"
                    + clientModel.getBranchcode());
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
                            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                AutoCompleteAttendeeRequest request =
                                    new AutoCompleteAttendeeRequest(
                                        URLEncoder.encode(constraint.toString(), "utf-8"),
                                        URLEncoder.encode(constraint.toString(), "utf-8")
                                    );
                                try {
                                    List<AutoCompleteAttendeeItem> dataList =
                                        saathiApi.getAutoCompleteAttendee(request)
                                            .blockingFirst();
                                    for (AutoCompleteAttendeeItem item : dataList) {
                                        String employeeName = stringOrEmpty(item.employeeName);
                                        String employeeCode = stringOrEmpty(item.employeeCode);
                                        String BranchCode = stringOrEmpty(item.branchCode);
                                        if (employeeCode.equalsIgnoreCase(
                                            AccountDetails.getEmployeeCode())) {
                                            continue;
                                        }
                                        modelList.add(new EmployeeModel(employeeName, employeeCode,
                                            BranchCode, false));
                                        filterResults.values = modelList;
                                        filterResults.count = modelList.size();
                                        if (modelList.isEmpty()) {
                                            modelList.add(EmployeeModel.getNoData(
                                                ATTENDEEE_NOT_FOUND_MESSAGE));
                                        }
                                    }
                                } catch (Exception e) {
                                    modelList.add(EmployeeModel.getNoData(getErrorOnFailure(e)));
                                    filterResults.values = modelList;
                                    filterResults.count = modelList.size();
                                    return filterResults;
                                }
                            } else {
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    INTERNET_CONNECTION, "OK", true, null);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
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
                tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
                linaerLayout = parent.findViewById(R.id.linaerLayout);
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
                inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode()
                    + "-"
                    + clientModel.getAttendeeName()
                    + "-"
                    + clientModel.getBranchCode());
            }

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                FilterResults filterResults = new FilterResults();

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    ArrayList<SplAttendeeModel> modelList = new ArrayList<>();
                    try {
                        if (constraint != null) {
                            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                AutoCompleteAttendeeRequest request =
                                    new AutoCompleteAttendeeRequest(
                                        URLEncoder.encode(constraint.toString(), "utf-8"),
                                        URLEncoder.encode(constraint.toString(), "utf-8")
                                    );
                                Disposable subscribe = saathiApi.getAutoCompleteSplAttendee(request)
                                    .compose(applyUiModel())
                                    .subscribe(uiModel -> {
                                        renderSpecialAttendees(modelList, uiModel);
                                    });
                            } else {
                                hideProgress();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    INTERNET_CONNECTION, "OK", true,
                                    null);
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

                private void renderSpecialAttendees(ArrayList<SplAttendeeModel> modelList,
                    UiModel<List<AutoCompleteAttendeeItem>> uiModel) {

                    Throwable error = uiModel.error;
                    if (error != null) {
                        modelList.add(SplAttendeeModel.getNoData(getErrorOnFailure(error)));
                        filterResults.values = modelList;
                        filterResults.count = modelList.size();
                    }

                    List<AutoCompleteAttendeeItem> dataList = uiModel.data;
                    if (dataList != null) {
                        for (AutoCompleteAttendeeItem item : dataList) {
                            String employeeName = stringOrEmpty(item.employeeName);
                            String employeeCode = stringOrEmpty(item.employeeCode);
                            String BranchCode = stringOrEmpty(item.branchCode);
                            modelList.add(
                                new SplAttendeeModel(employeeName, employeeCode,
                                    BranchCode));
                            filterResults.values = modelList;
                            filterResults.count = modelList.size();
                        }
                    }
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
                tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
                linaerLayout = parent.findViewById(R.id.linaerLayout);
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
            holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
            holder.ivDelete = view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(
                attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
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

            holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
            holder.ivDelete = view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(
                attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
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
}