package com.mosl.tabapp.leadViewDetail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.mobile.pdf.OTPFragment;
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingItem;
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem;
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdRequest;
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdResponse;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest;
import com.mosl.saathi.api.model.lead.Contact;
import com.mosl.saathi.api.model.lead.LeadProductMappingItem;
import com.mosl.saathi.api.model.lead.LeadViewItem;
import com.mosl.saathi.api.model.lead.LeadViewRequest;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.saathi.api.model.lead.createmeeting.SendOTPRequest;
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.FusedLocation;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.AddedAttendeeModel;
import com.mosl.tabapp.model.AddedSplAttendeeModel;
import com.mosl.tabapp.model.ClientDetail;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.MeetingApptModel;
import com.mosl.tabapp.model.SplAttendeeModel;
import com.mosl.tabapp.permission.PermissionCheckHelperFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.util.SpinnerAdapterFactory;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.jakewharton.rxbinding3.widget.RxAdapterView.itemSelections;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.commons.rx.RxUtils.InvalidSelectionPredicate;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.CRMAppUtil.isLocationFromMockProvider;
import static com.mosl.tabapp.common.CRMMOSLCommon.hasGPSDevice;
import static com.mosl.tabapp.constants.MOSLConstants.ACTIONABLES;
import static com.mosl.tabapp.constants.MOSLConstants.ACTIVITYTYPE;
import static com.mosl.tabapp.constants.MOSLConstants.ATLEAST_FIFTY_CHARACTERS;
import static com.mosl.tabapp.constants.MOSLConstants.ATLEAST_TEN_CHARACTERS;
import static com.mosl.tabapp.constants.MOSLConstants.ATLEAST_TWENTY_CHARACTERS;
import static com.mosl.tabapp.constants.MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.ATTENDEE_ALREADY_ADDED;
import static com.mosl.tabapp.constants.MOSLConstants.BACLIENTMEETING;
import static com.mosl.tabapp.constants.MOSLConstants.CATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.CREATE_MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY;
import static com.mosl.tabapp.constants.MOSLConstants.INTERESTEDIN;
import static com.mosl.tabapp.constants.MOSLConstants.INVALID_PINCODE_NUMBER;
import static com.mosl.tabapp.constants.MOSLConstants.INVALID_TIME;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.LOCATION_CANNOT_BE_LEFT_BLANK;
import static com.mosl.tabapp.constants.MOSLConstants.MEETINGADDRESSID;
import static com.mosl.tabapp.constants.MOSLConstants.MEETINGDURATION;
import static com.mosl.tabapp.constants.MOSLConstants.MEETINGOBJECTIVE;
import static com.mosl.tabapp.constants.MOSLConstants.MEETINGREASON;
import static com.mosl.tabapp.constants.MOSLConstants.MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK;
import static com.mosl.tabapp.constants.MOSLConstants.MOCK_LOCATION_ENABLED_MSG;
import static com.mosl.tabapp.constants.MOSLConstants.NO;
import static com.mosl.tabapp.constants.MOSLConstants.OUTSTATIONMEETING;
import static com.mosl.tabapp.constants.MOSLConstants.POTENTIAL_INVEST_IN_PRODUCT;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIONABLES;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIVITY_TYPE;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_BA_CLIENT_MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_CATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_DURATION;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_LOCATION;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_OBJECTIVE;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_REASON;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_OUT_STATION_MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_PRODUCT;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_SUB_CATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_TRAVELLING_MODE;
import static com.mosl.tabapp.constants.MOSLConstants.SEND_OTP_LABLE;
import static com.mosl.tabapp.constants.MOSLConstants.SPL_ATTENDEE_ALREADY_ADDED;
import static com.mosl.tabapp.constants.MOSLConstants.SUBCATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.TRAVELLINGMODEID;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.constants.MOSLConstants.VERIFY_OTP_LABLE;
import static com.mosl.tabapp.constants.MOSLConstants.YES;
import static com.mosl.tabapp.leadViewDetail.SetAppointmentFragmentOld.listHasAttende;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER;
import static java.lang.String.format;

/**
 * Created by raju on 3/7/2017.
 */

public class CreateMeetingFragment extends CommonFragment
    implements View.OnClickListener, TextView.OnEditorActionListener,
    FusedLocation.LatLongDispatcher, OTPFragment.OTPDialogListener {
  final public static String TAG = CreateMeetingFragment.class.getSimpleName();
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
  @Inject
  PermissionCheckHelperFragment permissionCheckHelper;
  @Inject
  FusedLocation fusedLocation;
  private ImageView ivLead;
  private EditText etCustomerComment, etMeetingDescription, etCategory, etSubCategory,
      etClientCode,
      etClientName, etAmount, etLocation, etPinCode;
  private Location mCurrentLocation; // location
  private TextView tvCurrentStageSince, tvCurrentStage, tvBranchCode, tvLeadId, tvClientCode,
      tvName, tvSplAttendeeCount, tvAttendeeCount, tvStartDate,
      tvLocationError, tvPinCodeError, tvMeetingLocationError, tvTravellingModeError;
  private View llSubcategory;
  private AttendeeAdapter attendeeAdapter;
  private SplAttendeeAdapter splAttendeeAdapter;
  private ArrayList<AddedAttendeeModel> addedAttendeeModelList = new ArrayList<>();
  private ArrayList<AddedSplAttendeeModel> addedSplAttendeeModelList = new ArrayList<>();
  private int addedAttendeeCount;
  private int addedSplAttendeeCount;
  private String mainFromDate;
  private String fromPostingDate;
  private String meetingDurationCodeStr;
  private String activityTypeCodeStr;
  private String meetingReasonCodeStr;
  private String actionablesCodeStr;
  private String categoryCodeStr;
  private String subCategoryCodestr;
  private String outStaitionCodeStr;
  private String baClientCodeStr;
  private Date fromDat;
  private Spinner spProduct, spMeetingDuration, spActivityType, spMeetingReason, spActionables,
      spSubCategory, spCategory, spBAClient, spOutStaition, spMeetingObjective, spMeetingLocation,
      spTravellingMode;
  private String leadId;
  private int currentYear, month, day, hour, minute;
  private Calendar calendar = Calendar.getInstance();
  private TextView tvProductError, tvMeetingDurationError, tvMeetingObjectiveError,
      tvMeetingReasonError, tvActivityTypeError, tvActionablesError, tvMeetingDescriptionError,
      tvCategoryError, tvCategoryCommentsError, tvSubCategoryError, tvSubCategoryCommentsError,
      tvActionables, tvBAClientError, tvOutStaitionError, tvClientCodeError, tvClientNameError;
  private LinearLayout llActionables;
  private LinearLayout llLocationGroup;
  private boolean profileType;
  private String mUCID = "0";
  private String meetingObjectiveIdStr = "0";
  private boolean mandatory = true, isPotentialPMSProduct, isOnlyClient;
  private CreateMeetingProductAdapter productAdapter;
  private CreateMeetingTravellingModeAdapter travellingAdapter;
  private ArrayList<CreateMeetingProductModel> productList;
  private ArrayList<CreateMeetingTravellingModel> travellingList;
  private ArrayAdapter<RowData> arrAdapMeetingReason;
  private boolean enableLocationGroup;
  private Button btnSendOtp;
  private Boolean isSendOtpSuccessful = false;
  private PublishSubject<Boolean> otpEvent = PublishSubject.create();

  @Override
  protected int getLayout() {
    return R.layout.frag_create_meeting_old;
  }

  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View createMeetingView = super.onCreateView(inflater, container, savedInstanceState);
    container.post(() -> setAppTitle("Create Meeting"));
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
    return createMeetingView;
  }

  private void setVisibilityOTP(Boolean status) {
    if (userManager.getUser().isRM()) {
      if (status) {
        btnSendOtp.setVisibility(View.VISIBLE);
      } else {
        btnSendOtp.setVisibility(View.GONE);
      }
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  private void setUpView(View createMeetingView) {

    ScrollView scrollView = createMeetingView.findViewById(R.id.scrollView);
    scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    scrollView.setFocusable(true);
    scrollView.setFocusableInTouchMode(true);
    scrollView.setOnTouchListener((v, event) -> {
      v.requestFocusFromTouch();
      return false;
    });
    RelativeLayout rlDate = createMeetingView.findViewById(R.id.rlDate);
    tvStartDate = createMeetingView.findViewById(R.id.tvDate);
    String mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
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
    LinearLayout linaerLayoutNew = createMeetingView.findViewById(R.id.linaerLayoutNew);
    llLocationGroup = createMeetingView.findViewById(R.id.llLocationGroup);
    setIcon(llActionables, tvActionables);
    tvActionables.setOnClickListener(this);
    Button btnSave = createMeetingView.findViewById(R.id.btnSave);
    btnSendOtp = createMeetingView.findViewById(R.id.btnSendOtp);
    Button btnReset = createMeetingView.findViewById(R.id.btnReset);
    ImageButton ibSplAttendee = createMeetingView.findViewById(R.id.ibSplAttendee);
    ImageButton ibAttendee = createMeetingView.findViewById(R.id.ibAttendee);

    etCustomerComment = createMeetingView.findViewById(R.id.etCustomerComment);
    etMeetingDescription = createMeetingView.findViewById(R.id.etMeetingDescription);
    etCategory = createMeetingView.findViewById(R.id.etCategory);
    etSubCategory = createMeetingView.findViewById(R.id.etSubCategory);
    etClientCode = createMeetingView.findViewById(R.id.etClientCode);
    etClientName = createMeetingView.findViewById(R.id.etClientName);
    etAmount = createMeetingView.findViewById(R.id.etAmount);
    etLocation = createMeetingView.findViewById(R.id.etLocation);
    etPinCode = createMeetingView.findViewById(R.id.etPinCode);
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
    RecyclerView rvProductList =
        createMeetingView.findViewById(R.id.rvProductList);
    RecyclerView rvTravellingList =
        createMeetingView.findViewById(R.id.rvTravellingList);
    productAdapter = new CreateMeetingProductAdapter(requireContext());
    rvProductList.setLayoutManager(new GridLayoutManager(requireContext(), 1));
    rvProductList.setAdapter(productAdapter);
    travellingAdapter =
        new CreateMeetingTravellingModeAdapter(requireContext());
    rvTravellingList.setLayoutManager(new GridLayoutManager(requireContext(), 1));
    rvTravellingList.setAdapter(travellingAdapter);
    btnSave.setOnClickListener(this);
    btnReset.setOnClickListener(this);

    ibAttendee.setOnClickListener(this);
    ibSplAttendee.setOnClickListener(this);
    tvAttendeeCount.setOnClickListener(this);
    tvSplAttendeeCount.setOnClickListener(this);
    btnSendOtp.setOnClickListener(this);

    hour = calendar.get(Calendar.HOUR_OF_DAY);
    minute = calendar.get(Calendar.MINUTE);
    fromDat = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
    try {
      String[] dateArray = mainToDate.split("-");
      currentYear = ((Integer.parseInt(dateArray[0])));
      month = ((Integer.parseInt(dateArray[1])));
      day = ((Integer.parseInt(dateArray[2])));
      AddedAttendeeModel.setAttendeeUserEmplyeeCode(
          userManager.getUser().employeeCode);
    } catch (Exception e) {
      Timber.d(e, TAG);
    }
    if (userManager.getUser().isFranchise()
        || userManager.getUser().isIFA()) {
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
          tvClientNameError.setText(R.string.validation_msg_enter_clien_name);
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
          tvClientCodeError.setText(R.string.validation_msg_enter_client_code);
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

    arrAdapMeetingReason = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
    spMeetingReason.setAdapter(arrAdapMeetingReason);
  }

  private void setMeetingLocationListener() {
    disposable.add(
        itemSelections(spMeetingLocation)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              if (!((RowData) spMeetingLocation.getSelectedItem()).getKey()
                  .equalsIgnoreCase("-1")
                  && !((RowData) spMeetingLocation.getSelectedItem()).getValue()
                  .equalsIgnoreCase("other")) {
                GetAddressByLeadId(
                    ((RowData) spMeetingLocation.getSelectedItem()).getKey());
              }
              etLocation.setText("");
              tvMeetingLocationError.setVisibility(View.GONE);
            }));
  }

  private void setSubCategoryListener() {
    disposable.add(
        itemSelections(spSubCategory)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              subCategoryCodestr = ((RowData) spSubCategory.getSelectedItem()).getKey();
              tvSubCategoryError.setVisibility(View.GONE);
            }));
  }

  private void setCategoryListener() {
    disposable.add(
        itemSelections(spCategory)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              categoryCodeStr = ((RowData) spCategory.getSelectedItem()).getKey();
              tvCategoryError.setVisibility(View.GONE);
              subCategory();
            }));
  }

  private void setProductListener() {
    disposable.add(
        itemSelections(spProduct)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              String productCodeStr = ((RowData) spProduct.getSelectedItem()).getKey();
              String productNameStr = ((RowData) spProduct.getSelectedItem()).getValue();
              if (!productCodeStr.equalsIgnoreCase("-1")) {
                productAdapter.addProduct(
                    new CreateMeetingProductModel(productCodeStr, productNameStr));
              }
              tvProductError.setVisibility(View.GONE);
            }));
  }

  private void setTravellingModeListener() {
    disposable.add(
        itemSelections(spTravellingMode)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              if (!((RowData) spTravellingMode.getSelectedItem()).getKey()
                  .equalsIgnoreCase("-1")) {
                travellingAdapter.addTravellingMode(new CreateMeetingTravellingModel(
                    ((RowData) spTravellingMode.getSelectedItem()).getKey(),
                    ((RowData) spTravellingMode.getSelectedItem()).getValue()));
              }

              tvTravellingModeError.setVisibility(View.GONE);
            }));
  }

  private void setMeetingDurationListener() {
    disposable.add(
        itemSelections(spMeetingDuration)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              meetingDurationCodeStr =
                  ((RowData) spMeetingDuration.getSelectedItem()).getKey();
              tvMeetingDurationError.setVisibility(View.GONE);
            }));
  }

  private void setMeetingReasonListener() {
    disposable.add(
        itemSelections(spMeetingReason)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              meetingReasonCodeStr = ((RowData) spMeetingReason.getSelectedItem()).getKey();
              String meetingReasonNameStr =
                  ((RowData) spMeetingReason.getSelectedItem()).getValue();
              tvMeetingReasonError.setVisibility(View.GONE);
              if (meetingReasonNameStr.equalsIgnoreCase("Customer Service Related")) {
                llActionables.setVisibility(View.VISIBLE);
              } else {
                llActionables.setVisibility(View.GONE);
              }
            }));
  }

  private void setActionablesListener() {
    disposable.add(
        itemSelections(spActionables)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              actionablesCodeStr = ((RowData) spActionables.getSelectedItem()).getKey();
              tvActionablesError.setVisibility(View.GONE);
            }));
  }

  private void setBAClientListener() {
    disposable.add(
        itemSelections(spBAClient)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              baClientCodeStr = ((RowData) spBAClient.getSelectedItem()).getKey();
              tvBAClientError.setVisibility(View.GONE);
            }));
  }

  private void setOutStationListener() {
    disposable.add(
        itemSelections(spOutStaition)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              outStaitionCodeStr = ((RowData) spOutStaition.getSelectedItem()).getKey();
              tvOutStaitionError.setVisibility(View.GONE);
            }));
  }

  private void setActivityTypeListener() {
    disposable.add(
        itemSelections(spActivityType)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              activityTypeCodeStr = ((RowData) spActivityType.getSelectedItem()).getKey();
              tvActivityTypeError.setVisibility(View.GONE);
              if (!spActivityType.getSelectedItem()
                  .toString()
                  .equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
                meetingReason();
                if (spActivityType.getSelectedItem()
                    .toString()
                    .equalsIgnoreCase("Personal Meeting")) {
                  enableLocationGroup = true;
                  enableField(llLocationGroup);
                  setVisibilityOTP(true);
                } else {
                  enableLocationGroup = false;
                  disableField(llLocationGroup);
                  disableReset();
                  setVisibilityOTP(false);
                }
              } else {
                setVisibilityOTP(false);
                arrMeetingReason.clear();
                arrMeetingReason.add(new RowData(0, "-1", SELECT_MEETING_REASON));
                AdapterUtils.setSpinnerData(arrAdapMeetingReason, spMeetingReason,
                    arrMeetingReason);
                arrAdapMeetingReason.notifyDataSetChanged();
              }
            }));
  }

  private void setMeetingObjectiveListener() {
    disposable.add(
        itemSelections(spMeetingObjective)
            .filter(InvalidSelectionPredicate)
            .skip(1)
            .subscribe(xyz -> {
              meetingObjectiveIdStr =
                  ((RowData) spMeetingObjective.getSelectedItem()).getKey();
              tvMeetingObjectiveError.setVisibility(View.GONE);
            }));
  }

  private void sendOTPRequest() {

    if (CRMAppUtil.isNetworkEnabled(requireContext())) {

      String geoArea = "";

      Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
      try {
        List<Address> addressList = geocoder.getFromLocation(mCurrentLocation.getLatitude(),
            mCurrentLocation.getLongitude(), 1);
        geoArea = addressList.get(0).getAddressLine(0);
      } catch (IOException e) {
        e.printStackTrace();
      }

      SendOTPRequest request =
          new SendOTPRequest(leadId, mUCID, geoArea,
              mCurrentLocation.getLongitude() + "",
              mCurrentLocation.getLatitude() + "", "2", 0,
              userManager.getUser().employeeCode);
      Disposable disposable = getSaathiApi().getStartMeeting(request)
          .compose(applyUiModel())
          .subscribe(this::renderSendOTP);
    } else {
      noInternetConnectionDialog();
    }
  }

  private void renderSendOTP(UiModel<String> uiModel) {
    toggleProgress(uiModel.inProgress);

    Throwable error = uiModel.error;
    if (error != null) {
      arrTravellingMode.clear();
      showOnFailurePopup(error);
    }
    String data = uiModel.data;
    if (data != null) {
      Toast.makeText(requireContext(), "OTP sent successfully", Toast.LENGTH_SHORT)
          .show();
      btnSendOtp.setText(R.string.btn_label_verify_otp);
    }
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
      } else if (id == R.id.btnSendOtp) {
        if (isSendOtpSuccessful) {
          Toast.makeText(requireContext(), "OTP is already verified.", Toast.LENGTH_LONG).show();
        } else {
          if (btnSendOtp.getText().equals(SEND_OTP_LABLE)) {
            if (checkPermission()) {
              if (mCurrentLocation != null) {
                sendOTPRequest();
              } else {
                fusedLocation.LocationUpdates();
              }
            }
          } else if (btnSendOtp.getText().equals(VERIFY_OTP_LABLE)) {
            FragmentTransaction ft =
                requireActivity().getSupportFragmentManager().beginTransaction();

            OTPFragment dialogFragment = OTPFragment.newInstance(leadId, mUCID);
            dialogFragment.setTargetFragment(CreateMeetingFragment.this, 1);
            dialogFragment.setListener(this);
            dialogFragment.show(ft, "");
          }
        }
      } else if (id == R.id.btnReset) {
        resetData();
      } else if (id == R.id.btnSave) {
        saveMeeting();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveMeeting() {
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
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
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

  private boolean checkPermission() {
    if (!permissionCheckHelper.locationPermission()) {
      permissionCheckHelper.requestLocation();
      return false;
    } else if (!((LocationManager) requireContext().getSystemService(
        Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)
        && hasGPSDevice(requireContext())) {

      fusedLocation.initDispatcher(this);
      return false;
    }
    return true;
  }

  private void isMockLocation(MeetingApptModel meetingApptModel) {
    if (enableLocationGroup && isLocationFromMockProvider(requireContext(),
        mCurrentLocation)) {
      MSFDialog.alertDialog(requireContext(), 0,
          getResources().getString(R.string.motilal_oswal),
          MOCK_LOCATION_ENABLED_MSG, "OK",
          true, (action, data) -> {
            if (action == MSFDialog.Action.OK) {
              requireActivity().finish();
            }
          });
    } else {
      if (isPotentialPMSProduct && !isOnlyClient) {
        MSFDialog.alertDialog(requireContext(), 0,
            getResources().getString(R.string.motilal_oswal),
            POTENTIAL_INVEST_IN_PRODUCT, YES, NO, true,
            (action, data) -> {
              if (action == MSFDialog.Action.OK) {
                meetingApptModel.setIsPotentialPMSProduct(YES);
                createMeetingSave(meetingApptModel);
              } else {
                meetingApptModel.setIsPotentialPMSProduct(NO);
                createMeetingSave(meetingApptModel);
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
    /* 10 secs */
    long UPDATE_INTERVAL = 1000;
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    /* 2 sec */
    long FASTEST_INTERVAL = 2000;
    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    return mLocationRequest;
  }

  private void getLatLong(MeetingApptModel meetingApptModel) {
    Handler mainHandler = new Handler(requireContext().getMainLooper());
    Runnable myRunnable = () -> {
      getFusedLocationProviderClient(requireContext()).requestLocationUpdates(
          getmLocationRequest(), new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
              mCurrentLocation = locationResult.getLastLocation();
            }
          }, Looper.myLooper());
      Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
      try {
        if (mCurrentLocation != null) {
          List<Address> addressList =
              geocoder.getFromLocation(mCurrentLocation.getLatitude(),
                  mCurrentLocation.getLongitude(), 1);
          meetingApptModel.setGeoArea(addressList.get(0).getAddressLine(0));
          meetingApptModel.setGeoLatitude(mCurrentLocation.getLatitude() + "");
          meetingApptModel.setGeoLongitude(mCurrentLocation.getLongitude() + "");
          isMockLocation(meetingApptModel);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
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
    setVisibilityOTP(false);
    btnSendOtp.setText(R.string.btn_label_send_otp);
  }

  /**
   * form validation
   */

  private boolean validateForm() {
    if (userManager.getUser().isRM() && !isSendOtpSuccessful &&
        spActivityType.getSelectedItem().toString().equalsIgnoreCase("Personal Meeting")) {

      alert("Please verify OTP to create meeting");
      return false;
    } else if (spActivityType.getSelectedItem()
        .toString()
        .equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
      tvActivityTypeError.setVisibility(View.VISIBLE);
      tvActivityTypeError.setText(SELECT_ACTIVITY_TYPE);
      tvActivityTypeError.requestFocus();
      return false;
    } else if (spProduct.getSelectedItem().toString().equalsIgnoreCase(SELECT_PRODUCT)
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
      tvClientCodeError.setText(R.string.validation_msg_enter_client_code);
      tvClientCodeError.setVisibility(View.VISIBLE);
      tvClientCodeError.requestFocus();
      return false;
    } else if (profileType && isBlank(etClientName.getText().toString().trim())) {
      tvClientNameError.setText(R.string.validation_msg_enter_clien_name);
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
        && !permissionCheckHelper.locationPermission()) {
      permissionCheckHelper.requestLocation();

      return false;
    } else if (enableLocationGroup
        && !((LocationManager) requireContext().getSystemService(
        Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)
        && hasGPSDevice(requireContext())) {

      fusedLocation.initDispatcher(this);

      return false;
    }

    return true;
  }

  private void setProfileImage(String ucid) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      ClientImageRequest request =
          new ClientImageRequest(ucid);
      Disposable subscribe = getSaathiApi().getClientImage(request)
          .compose(applyUiModel())
          .subscribe(this::renderClientImage);
    } else {
      noInternetConnectionDialog();
    }
  }

  private void renderClientImage(UiModel<SaathiCommonResponse<String>> uiModel) {
    toggleProgress(uiModel.inProgress);

    Throwable error = uiModel.error;
    if (error != null) {
      showOnFailurePopup(error);
    }
    SaathiCommonResponse<String> data = uiModel.data;
    if (data != null) {
      String response = data.response;
      byte[] byt = Base64.decode(response, Base64.DEFAULT);
      if (byt != null) {
        CRMAppUtil.setImage(byt, ivLead, requireContext());
      } else {
        CRMAppUtil.setImage(new byte[0], ivLead,
            requireContext());
      }
    }
  }

  //region Dropdown Web calls
  private void getClientDetail(String ucid) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      ClientDetailRequest request =
          new ClientDetailRequest(Integer.valueOf(ucid));
      Disposable subscribe = getSaathiApi().getClientDetail(request)
          .compose(applyUiModel())
          .subscribe(this::renderClientDetails);
    } else {
      noInternetConnectionDialog();
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
            format("%s Days", ClientDetail.getCurrentStageSince()));
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
    CRMAppUtil.setImage(new byte[0], ivLead, requireContext());
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      LeadViewRequest request =
          new LeadViewRequest(String.valueOf(strLeadId));
      Disposable subscribe = getSaathiApi().getLeadView(request)
          .compose(applyUiModel())
          .subscribe(this::renderLeadView);
    } else {
      noInternetConnectionDialog();
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
      String leadId = stringOrEmpty(String.valueOf(data.leadID));
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
        String isDefault = stringOrEmpty(String.valueOf(item.isDefault));
        String pendingDays = stringOrEmpty(String.valueOf(item.pendingDays));
        if (isDefault.equalsIgnoreCase("true")) {
          tvClientCode.setText(clientCode);
          tvCurrentStage.setText(stageName);
          tvCurrentStageSince.setText(format("%s Days", pendingDays));
        }
      }
      tvName.setText(name);
      tvLeadId.setText(leadId);
      tvBranchCode.setText(branchCode);
    }
  }

  private void category() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId, CATEGORY,
              null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderCategory);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrCategory.add(new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapCategory =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spCategory.setAdapter(arrAdapCategory);
      AdapterUtils.setSpinnerData(arrAdapCategory, spCategory, arrCategory);
      arrAdapCategory.notifyDataSetChanged();

      setCategoryListener();
    }
  }

  private void meetingObjective() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId,
              MEETINGOBJECTIVE, null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderMeetingObjective);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);

        arrMeetingObjective.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapMeetingsObjective =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spMeetingObjective.setAdapter(arrAdapMeetingsObjective);
      AdapterUtils.setSpinnerData(arrAdapMeetingsObjective, spMeetingObjective,
          arrMeetingObjective);
      arrAdapMeetingsObjective.notifyDataSetChanged();

      setMeetingObjectiveListener();
    }
  }

  private void subCategory() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId, SUBCATEGORY,
              categoryCodeStr,
              null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::rendrerSubCategory);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);

        arrSubCategory.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapSubCategory =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spSubCategory.setAdapter(arrAdapSubCategory);
      AdapterUtils.setSpinnerData(arrAdapSubCategory, spSubCategory, arrSubCategory);
      arrAdapSubCategory.notifyDataSetChanged();
      setSubCategoryListener();
    }
  }

  private void selectProduct() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      meetingDuration();
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId, INTERESTEDIN,
              null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderProduct);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrSelectProduct.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapSelectProduct =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spProduct.setAdapter(arrAdapSelectProduct);
      AdapterUtils.setSpinnerData(arrAdapSelectProduct, spProduct, arrSelectProduct);
      arrAdapSelectProduct.notifyDataSetChanged();

      setProductListener();
    }
  }

  private void meetingDuration() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      activityType();
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId,
              MEETINGDURATION, null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderMeetingDuration);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrMeetingDuration.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapMeetingDuration =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spMeetingDuration.setAdapter(arrAdapMeetingDuration);
      AdapterUtils.setSpinnerData(arrAdapMeetingDuration, spMeetingDuration,
          arrMeetingDuration);
      arrAdapMeetingDuration.notifyDataSetChanged();

      setMeetingDurationListener();
    }
  }

  private void activityType() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId, ACTIVITYTYPE,
              null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderActivityType);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrActivityType.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }

      ArrayAdapter<RowData> arrAdapActivityType =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spActivityType.setAdapter(arrAdapActivityType);
      AdapterUtils.setSpinnerData(arrAdapActivityType, spActivityType, arrActivityType);
      arrAdapActivityType.notifyDataSetChanged();

      setActivityTypeListener();
    }
  }

  private void meetingReason() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      baClient();
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId,
              MEETINGREASON,
              activityTypeCodeStr, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderMeetingReason);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrMeetingReason.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      AdapterUtils.setSpinnerData(arrAdapMeetingReason, spMeetingReason, arrMeetingReason);
      arrAdapMeetingReason.notifyDataSetChanged();
      setMeetingReasonListener();
    }
  }

  private void baClient() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      outStation();
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId,
              BACLIENTMEETING,
              null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderBaClient);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrBAClient.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapBAClient =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spBAClient.setAdapter(arrAdapBAClient);
      AdapterUtils.setSpinnerData(arrAdapBAClient, spBAClient, arrBAClient);
      arrAdapBAClient.notifyDataSetChanged();
      setBAClientListener();
    }
  }

  private void outStation() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      actionables();
      LookupRequest request =
          new LookupRequest(
              userManager.getUser().currentBusinessProfile.businessUnitId, OUTSTATIONMEETING,
              null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderOutStation);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrOutStation.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapOutStation =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spOutStaition.setAdapter(arrAdapOutStation);
      AdapterUtils.setSpinnerData(arrAdapOutStation, spOutStaition, arrOutStation);
      arrAdapOutStation.notifyDataSetChanged();

      setOutStationListener();
    }
  }

  private void actionables() {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      category();
      LookupRequest request =
          new LookupRequest(userManager.getUser().currentBusinessProfile.businessUnitId,
              ACTIONABLES, null, null);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderActionables);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrActionables.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapActionables =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spActionables.setAdapter(arrAdapActionables);
      AdapterUtils.setSpinnerData(arrAdapActionables, spActionables, arrActionables);
      arrAdapActionables.notifyDataSetChanged();

      setActionablesListener();
    }
  }

  private void meetingLocation() {
    travellingMode();
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      LookupRequest request =
          new LookupRequest("0", MEETINGADDRESSID, null, true);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderMeetingLocation);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrMeetingLocation.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapMeetingLocation =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spMeetingLocation.setAdapter(arrAdapMeetingLocation);
      AdapterUtils.setSpinnerData(arrAdapMeetingLocation, spMeetingLocation,
          arrMeetingLocation);
      arrAdapMeetingLocation.notifyDataSetChanged();

      setMeetingLocationListener();
    }
  }

  private void GetAddressByLeadId(String meetingId) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      AddressByLeadIdRequest request =
          new AddressByLeadIdRequest(tvLeadId.getText().toString(), meetingId);
      Disposable subscribe = getSaathiApi().getAddressByLeadId(request)
          .compose(applyUiModel())
          .subscribe(this::renderAddressByLeadId);
    } else {
      noInternetConnectionDialog();
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
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      LookupRequest request =
          new LookupRequest("0", TRAVELLINGMODEID, null, true);
      Disposable disposable = getSaathiApi().getAllLookup(request)
          .compose(applyUiModel())
          .subscribe(this::renderTravellingMode);
    } else {
      noInternetConnectionDialog();
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
        String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
        String lookUpName = stringOrEmpty(item.lookUpName);
        arrTravellingMode.add(
            new RowData(i + 1, lookUpId, lookUpName));
      }
      ArrayAdapter<RowData> arrAdapTravellingMode =
          SpinnerAdapterFactory.getDefaultAdapter(requireContext());
      spTravellingMode.setAdapter(arrAdapTravellingMode);
      AdapterUtils.setSpinnerData(arrAdapTravellingMode, spTravellingMode, arrTravellingMode);
      arrAdapTravellingMode.notifyDataSetChanged();

      setTravellingModeListener();
    }
  }

  private void selectDate() {
    Calendar today = Calendar.getInstance();

    Dialog dialog = new Dialog(requireContext());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow()
        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.setContentView(R.layout.date_picker);
    dialog.getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    DatePicker dpFromDate = dialog.findViewById(R.id.datePickerFromDate);
    dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
    ImageView close = dialog.findViewById(R.id.ibtnClose);
    Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
    final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
    timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, -1);
    if (spActivityType.getSelectedItem().toString().equalsIgnoreCase("Personal Meeting")
        && userManager.getUser().isRM()) {
      dpFromDate.setMinDate(System.currentTimeMillis() - 1000);
    } else {
      dpFromDate.setMinDate(cal.getTimeInMillis());
    }
    dpFromDate.setMaxDate(System.currentTimeMillis() - 1000);
    if (mainFromDate != null) {
      try {
        String[] dateSplit = mainFromDate.split("-");
        dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
            Integer.parseInt(dateSplit[2]), (view, year, monthOfYear, dayOfMonth) -> {
              mainFromDate =
                  year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
              currentYear = year;
              month = monthOfYear + 1;
              day = dayOfMonth;
            });
      } catch (Exception e) {
        Timber.d(e, TAG);
      }
    } else {
      dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
          today.get(Calendar.DATE), (view, year, monthOfYear, dayOfMonth) -> {
            mainFromDate =
                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
            currentYear = year;
            month = monthOfYear + 1;
            day = dayOfMonth;
          });
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      timePicker.setHour(hour);
      timePicker.setMinute(minute);
    } else {
      timePicker.setCurrentHour(hour);
      timePicker.setCurrentMinute(minute);
    }

    timePicker.setOnTimeChangedListener((arg0, arg1, arg2) -> {
      hour = arg1;
      minute = arg2;
    });

    close.setOnClickListener(v -> dialog.dismiss());

    btnSubmit.setOnClickListener(v -> {
      try {
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
        if (mainFromDate != null) {
          fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
          fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
          @SuppressLint("DefaultLocale") String finalSelectedDateStr =
              String.format("%s %d:%d:00", fromPostingDate, hour,
                  minute); //dd-MM-yyyy hh:mm:ss
          Date finalSelectedDate =
              CRMAppUtil.getDate(finalSelectedDateStr, "dd-MM-yyyy hh:mm");
          if (CRMAppUtil.isBeforeandEqualDate(
              finalSelectedDate)) { //
            tvStartDate.setText(
                CRMAppUtil.getDBDate(finalSelectedDate, DATE_FORMAT_TO_DISPLAY));
            dialog.dismiss();
          } else {
            MSFDialog.alertDialog(requireContext(), 0,
                getResources().getString(R.string.motilal_oswal), INVALID_TIME,
                "OK", true, null);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
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
    final Dialog dialogAddedSplAttendee = new Dialog(requireContext());
    dialogAddedSplAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialogAddedSplAttendee.getWindow()
        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialogAddedSplAttendee.setContentView(R.layout.dialog_addedd_spl_attendee_list);
    TextView tvEmpty = dialogAddedSplAttendee.findViewById(R.id.tvEmpty);
    dialogAddedSplAttendee.getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    dialogAddedSplAttendee.setCanceledOnTouchOutside(true);
    ListView lvAttendee = dialogAddedSplAttendee.findViewById(R.id.lvAttendee);
    lvAttendee.setEmptyView(tvEmpty);
    AddedSplAttendeeAdapter addedSplAttendeeAdapter =
        new AddedSplAttendeeAdapter(requireContext(), addedSplAttendeeModelList);
    lvAttendee.setAdapter(addedSplAttendeeAdapter);
    addedSplAttendeeAdapter.notifyDataSetChanged();
    dialogAddedSplAttendee.show();
  }

  private void dialogAddedAttendee() {
    final Dialog dialogAddedAttendee = new Dialog(requireContext());
    dialogAddedAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialogAddedAttendee.getWindow()
        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialogAddedAttendee.setContentView(R.layout.dialog_addedd_attendee_list);
    TextView tvEmpty = dialogAddedAttendee.findViewById(R.id.tvEmpty);
    dialogAddedAttendee.getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    dialogAddedAttendee.setCanceledOnTouchOutside(true);
    ListView lvAttendee = dialogAddedAttendee.findViewById(R.id.lvAttendee);
    lvAttendee.setEmptyView(tvEmpty);
    AddedAttendeeAdapter addedAttendeeAdapter =
        new AddedAttendeeAdapter(requireContext(), addedAttendeeModelList);
    lvAttendee.setAdapter(addedAttendeeAdapter);
    addedAttendeeAdapter.notifyDataSetChanged();
    dialogAddedAttendee.show();
  }

  private void dialogAttendee() {
    final Dialog dialogAttendeeList = new Dialog(requireContext());
    dialogAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialogAttendeeList.getWindow()
        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialogAttendeeList.setContentView(R.layout.dialog_attendee_list);
    dialogAttendeeList.getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    dialogAttendeeList.setCanceledOnTouchOutside(true);
    final DelayAutoCompleteTextView etSearchAttendee =
        dialogAttendeeList.findViewById(R.id.etSearchAttendee);
    etSearchAttendee.setImeOptions(EditorInfo.IME_ACTION_DONE);
    etSearchAttendee.setOnEditorActionListener(this);
    etSearchAttendee.setThreshold(3);
    attendeeAdapter = new AttendeeAdapter(requireContext());
    etSearchAttendee.setAdapter(attendeeAdapter);
    etSearchAttendee.setLoadingIndicator(
        dialogAttendeeList.findViewById(R.id.pbLoadingIndicator));
    etSearchAttendee.setOnItemClickListener((parent, view, position, id) -> {

      EmployeeModel clientModel = attendeeAdapter.getCount() > position ?
          attendeeAdapter.getItem(position) : new EmployeeModel("", false);

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
        Toast.makeText(requireContext(), ATTENDEE_ALREADY_ADDED, Toast.LENGTH_SHORT)
            .show();
        etSearchAttendee.setText("");
      }
    });

    dialogAttendeeList.show();
  }

  private void dialogSplAttendee() {
    final Dialog dialogSplAttendeeList = new Dialog(requireContext());
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
    splAttendeeAdapter = new SplAttendeeAdapter(requireContext());
    etSearchAttendee.setAdapter(splAttendeeAdapter);
    etSearchAttendee.setLoadingIndicator(
        dialogSplAttendeeList.findViewById(R.id.pbLoadingIndicator));
    etSearchAttendee.setOnItemClickListener((parent, view, position, id) -> {

      SplAttendeeModel clientModel =
          splAttendeeAdapter.getCount() > position ? splAttendeeAdapter.getItem(position)
              : new SplAttendeeModel("");
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
        Toast.makeText(requireContext(), SPL_ATTENDEE_ALREADY_ADDED,
            Toast.LENGTH_SHORT).show();
        etSearchAttendee.setText("");
      }
    });

    dialogSplAttendeeList.show();
  }

  private void renderAddUpdate(UiModel<AddUpdateMeetingItem> uiModel) {
    toggleProgress(uiModel.inProgress);
    Throwable error = uiModel.error;
    if (error != null) {
      showOnFailurePopup(error);
    }

    AddUpdateMeetingItem data = uiModel.data;
    if (data != null) {
      MSFDialog.alertDialog(requireContext(), 0,
          getResources().getString(R.string.motilal_oswal),
          CREATE_MEETING, "OK", true, (action, data1) -> {
            if (action == MSFDialog.Action.OK) {
              Bundle bundle = new Bundle();
              bundle.putString(LEAD_ID,
                  getArguments().getString(LEAD_ID));
              bundle.putString(UCID,
                  getArguments().getString(UCID));
              navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true, false);
            }
          });
    }
  }

  private void createMeetingSave(MeetingApptModel meetingApptModel) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      RequestBody request =
          RequestBody.create(MediaType.parse("text/plain"), meetingApptModel.getJson());
      Disposable subscribe = getSaathiApi().postAddUpdateMeeting(request)
          .compose(applyUiModel())
          .subscribe(this::renderAddUpdate);
    } else {
      noInternetConnectionDialog();
    }
  }

  @Override
  public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
    hideKeyboard(textView);
    return false;
  }

  private void startLocationUpdates() {

    if (permissionCheckHelper.locationPermission()) {
      fusedLocation.initDispatcher(this);
    }
  }

  private void setCount(int cnt) {
    if (cnt <= 0) {
      tvAttendeeCount.setText("");
    } else {
      tvAttendeeCount.setText(format("(%s)", String.valueOf(cnt)));
    }
  }

  private void setCountForSpecial(int cnt) {
    if (cnt <= 0) {
      tvSplAttendeeCount.setText("");
    } else {
      tvSplAttendeeCount.setText(format("(%s)", String.valueOf(cnt)));
    }
  }

  @Override
  public void onLatLongChanged(Location location) {
    mCurrentLocation = location;
  }

  @Override
  public void onSuccessResult(boolean status) {
    isSendOtpSuccessful = status;
  }

  public class AttendeeAdapter extends BaseAdapter implements Filterable {
    Context context;
    private LayoutInflater inflater;
    private List<EmployeeModel> resultList = new ArrayList<>();

    public AttendeeAdapter(Context context) {
      this.inflater = LayoutInflater.from(context);
      this.context = context;
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
        holderView.tvAttendeeName.setText(
            format("%s-%s-%s", clientModel.getAttendeeCode(),
                clientModel.getAttendeeName(), clientModel.getBranchcode()));
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
              if (CRMAppUtil.isNetworkEnabled(requireContext())) {
                AutoCompleteAttendeeRequest request =
                    new AutoCompleteAttendeeRequest(
                        URLEncoder.encode(constraint.toString(), "utf-8"),
                        URLEncoder.encode(constraint.toString(), "utf-8")
                    );
                try {
                  List<AutoCompleteAttendeeItem> dataList =
                      getSaathiApi().getAutoCompleteAttendee(request)
                          .blockingFirst();
                  for (AutoCompleteAttendeeItem item : dataList) {
                    String employeeName = stringOrEmpty(item.employeeName);
                    String employeeCode = stringOrEmpty(item.employeeCode);
                    String BranchCode = stringOrEmpty(item.branchCode);
                    if (employeeCode.equalsIgnoreCase(
                        userManager.getUser().employeeCode)) {
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
                  modelList.add(EmployeeModel.getNoData(getErrorOnFailureNew(e)));
                  filterResults.values = modelList;
                  filterResults.count = modelList.size();
                  return filterResults;
                }
              } else {
                noInternetConnectionDialog();
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

      private HolderView(View parent) {
        tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
        linaerLayout = parent.findViewById(R.id.linaerLayout);
      }
    }
  }

  public class SplAttendeeAdapter extends BaseAdapter implements Filterable {
    Context mContext;
    private LayoutInflater inflater;
    private List<SplAttendeeModel> attendeeModelList = new ArrayList<>();

    private SplAttendeeAdapter(Context c) {
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
            (LayoutInflater) requireContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
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
        holderView.tvAttendeeName.setText(
            format("%s-%s-%s", clientModel.getAttendeeCode(),
                clientModel.getAttendeeName(), clientModel.getBranchCode()));
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
              if (CRMAppUtil.isNetworkEnabled(requireContext())) {
                AutoCompleteAttendeeRequest request =
                    new AutoCompleteAttendeeRequest(
                        URLEncoder.encode(constraint.toString(), "utf-8"),
                        URLEncoder.encode(constraint.toString(), "utf-8")
                    );
                try {
                  List<AutoCompleteAttendeeItem> dataList =
                      getSaathiApi().getAutoCompleteSplAttendee(request)
                          .blockingFirst();
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
                } catch (Exception e) {
                  modelList.add(
                      SplAttendeeModel.getNoData(getErrorOnFailureNew(e)));
                  filterResults.values = modelList;
                  filterResults.count = modelList.size();
                }
              } else {
                noInternetConnectionDialog();
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
      TextView tvAttendeeName;
      LinearLayout linaerLayout;

      private HolderView(View parent) {
        tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
        linaerLayout = parent.findViewById(R.id.linaerLayout);
      }
    }
  }

  public class AddedAttendeeAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context mContext;
    List<AddedAttendeeModel> attendeeModelList;

    private AddedAttendeeAdapter(Context c, List<AddedAttendeeModel> attendeeModelList) {
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
      Holder holder = new Holder();
      view = inflater.inflate(R.layout.item_list_added_attendee, parent, false);
      AddedAttendeeModel attendeeModel = attendeeModelList.get(position);
      holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
      holder.ivDelete = view.findViewById(R.id.ivDelete);

      holder.tvAttendeeName.setText(
          format("%s-%s", attendeeModel.getAttendeeEmplyeeCode(),
              attendeeModel.getAttendeeName()));
      holder.ivDelete.setOnClickListener(view1 -> {
        addedAttendeeCount--;
        setCount(addedAttendeeCount);
        attendeeModelList.remove(position);
        notifyDataSetChanged();
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

    private AddedSplAttendeeAdapter(Context c, List<AddedSplAttendeeModel> attendeeModelList) {
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
      Holder holder = new Holder();
      view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false);

      AddedSplAttendeeModel attendeeModel = attendeeModelList.get(position);

      holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
      holder.ivDelete = view.findViewById(R.id.ivDelete);

      holder.tvAttendeeName.setText(
          format("%s-%s", attendeeModel.getAttendeeEmplyeeCode(),
              attendeeModel.getAttendeeName()));
      holder.ivDelete.setOnClickListener(view1 -> {
        addedSplAttendeeCount--;
        setCountForSpecial(addedSplAttendeeCount);
        attendeeModelList.remove(position);
        notifyDataSetChanged();
      });

      return view;
    }

    class Holder {
      TextView tvAttendeeName;
      ImageView ivDelete;
    }
  }
}