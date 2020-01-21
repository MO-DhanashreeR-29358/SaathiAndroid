package com.mosl.tabapp.leadViewDetail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse;
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem;
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest;
import com.mosl.saathi.api.model.createmeeting.MeetingAttendeeItem;
import com.mosl.saathi.api.model.createmeeting.MeetingByIdItem;
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest;
import com.mosl.saathi.api.model.lead.Contact;
import com.mosl.saathi.api.model.lead.LeadProductMappingItem;
import com.mosl.saathi.api.model.lead.LeadViewItem;
import com.mosl.saathi.api.model.lead.LeadViewRequest;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.AddedAttendeeModel;
import com.mosl.tabapp.model.AddedSplAttendeeModel;
import com.mosl.tabapp.model.ClientDetail;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.MeetingApptModel;
import com.mosl.tabapp.model.SplAttendeeModel;
import com.mosl.tabapp.ui.MSFDialog;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.R.string.motilal_oswal;
import static com.mosl.tabapp.R.string.txt_label_reschedule_appointment;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;
import static com.mosl.tabapp.constants.MOSLConstants.ACTIVITYTYPE;
import static com.mosl.tabapp.constants.MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.ATTENDEE_ALREADY_ADDED;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY;
import static com.mosl.tabapp.constants.MOSLConstants.DAYS;
import static com.mosl.tabapp.constants.MOSLConstants.END_TIME_EQUAL_ERROR;
import static com.mosl.tabapp.constants.MOSLConstants.END_TIME_ERROR;
import static com.mosl.tabapp.constants.MOSLConstants.INTERESTEDIN;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MEETING_ID;
import static com.mosl.tabapp.constants.MOSLConstants.REPEATMODE;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIVITY_TYPE;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_APPT_DATE_TYPE;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_PRODUCT;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_WEEK;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_WEEK_DAYS;
import static com.mosl.tabapp.constants.MOSLConstants.SET_APPOINTMENTS;
import static com.mosl.tabapp.constants.MOSLConstants.SPL_ATTENDEE_ALREADY_ADDED;
import static com.mosl.tabapp.constants.MOSLConstants.START_TIME_ERROR;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.constants.MOSLConstants.UPDATE_APPOINTMENTS;
import static com.mosl.tabapp.constants.MOSLConstants.WEEKS;
import static com.mosl.tabapp.constants.MOSLConstants.mFormatWithDash;
import static com.mosl.tabapp.constants.MOSLConstants.mFormatWithSpace;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class SetAppointmentFragment extends CommonFragment
        implements View.OnClickListener, TextView.OnEditorActionListener {

    public static final String TAG = SetAppointmentFragment.class.getSimpleName();
    private final ArrayList<RowData> arrSelectProduct = new ArrayList<>();
    private final ArrayList<RowData> arrSelectWeek = new ArrayList<>();
    private final ArrayList<RowData> arrSelectWeekDay = new ArrayList<>();
    private final ArrayList<RowData> arrActivityType = new ArrayList<>();
    private final ArrayList<RowData> arrRepeatMode = new ArrayList<>();
    private View llRepeteOnDaysPannel;
    private TextView tvProductTypeSpError, tvRepeatTypeSpError, tvRepeatWeekdayError;
    private Spinner spSelectProduct, spRepeateWeek, spSelectWeekDay, spActivityType, spRepeateOn;
    private TextView tvActivityTypeError;
    private ImageView ivLead;
    private int addedAttendeeCount;
    private int addedSplAttendeeCount;
    private String str_LeadID = "";
    private String productCodeStr;
    private ArrayList<AddedAttendeeModel> addedAttendeeModelList = new ArrayList<>();
    private AttendeeAdapter attendeeAdapter;
    private ArrayList<AddedSplAttendeeModel> addedSplAttendeeModelList = new ArrayList<>();
    private SplAttendeeAdapter splAttendeeAdapter;
    private String weekCodeStr, weekDayCodeStr;
    private Calendar today;
    private Dialog dialogDateTime;
    private ImageView close;
    private Button btnSubmit;
    private int hour, minute;
    private String mainFromDate, fromPostingDate;
    private String mainFromDateStartDate, fromPostingDateStartDate;
    private int hourStartDate, minuteStartDate;
    private String mainFromDateEndDate, fromPostingDateEndDate;
    private int hourEndDate, minuteEndDate;
    private int currentYear, month, day;
    private int currentYearStartDate, monthStartDate, dayStartDate;
    private int currentYearEndDate, monthEndDate, dayEndDate;
    private String activityTypeCodeStr;
    private Date fromDat;//, toDate;
    private String meetingId = "";
    private boolean createMode = true;
    private TextView tvCurrentStageSince, tvCurrentStage, tvEndDateTimeDisplay, tvEndDateTime,
            tvBranchCode, tvLeadId, tvClientCode, tvName, tvSplAttendeeCount,
            tvAttendeeCount, tvApppointmentDateTimeDisplay, tvApppointmentDateTime,
            tvStartDateTimeDisplay, tvStartDateTime;
    private String mUCID = "0";
    private TextView tvStartDateError, tvEndDateError;
    private TextView tvAPPDateError;
    private AdapterView.OnItemSelectedListener selectProductListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        tvProductTypeSpError.setVisibility(View.GONE);

                        productCodeStr = ((RowData) spSelectProduct.getSelectedItem()).getKey();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
    //weekDayCodeStr,weekDayNameStr
    private AdapterView.OnItemSelectedListener selectWeekDayListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        tvRepeatWeekdayError.setVisibility(View.GONE);
                        weekDayCodeStr = ((RowData) spSelectWeekDay.getSelectedItem()).getKey();
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
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
    //endregion
    //region DropDown listner
    private AdapterView.OnItemSelectedListener selectWeekListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        tvRepeatTypeSpError.setVisibility(View.GONE);
                        weekCodeStr = ((RowData) spRepeateWeek.getSelectedItem()).getKey();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
    private AdapterView.OnItemSelectedListener selectRepeteModeListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {

                        if (position > 0) {
                            llRepeteOnDaysPannel.setVisibility(View.VISIBLE);
                        } else {
                            llRepeteOnDaysPannel.setVisibility(View.GONE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    //endregion
    //deos dedup on list
    static boolean listHasAttende(List<AddedAttendeeModel> addedAttendeeModelList,
                                  List<AddedSplAttendeeModel> addedSplAttendeeModelList, String empID) {
        boolean has = false;
        if (addedAttendeeModelList != null) {
            for (AddedAttendeeModel atten : addedAttendeeModelList) {
                if (atten.getAttendeeEmplyeeCode().trim().equalsIgnoreCase(empID.trim())) {
                    has = true;
                    break;
                }
            }
        }
        if (addedSplAttendeeModelList != null) {
            for (AddedSplAttendeeModel atten : addedSplAttendeeModelList) {
                if (atten.getAttendeeEmplyeeCode().trim().equalsIgnoreCase(empID.trim())) {
                    has = true;
                    break;
                }
            }
        }
        return has;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_set_appiontment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View setAppointmentView = super.onCreateView(inflater, container, savedInstanceState);
        container.post(() -> setAppTitle("Set Appointment"));
        try {
            mUCID = Objects.requireNonNull(this.getArguments())
                    .getString(UCID, "0")
                    .equalsIgnoreCase("") ? "0"
                    : this.getArguments().getString(UCID, "0");
            str_LeadID = this.getArguments().getString(LEAD_ID, "0");
            meetingId = this.getArguments().getString(MEETING_ID, "0");
            if (meetingId != null && !meetingId.equalsIgnoreCase("0")) {
                //Editt mode
                createMode = false;
                getAppointmentDetailsFromServer(meetingId);
            } else {
                //Create mode
                selectProduct("");
                activityType("");
                getRepeatMode("");
                selectWeek("");
                selectWeekDay("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUpView(Objects.requireNonNull(setAppointmentView));
        if (!mUCID.equalsIgnoreCase("0")) {
            getClientDetail(mUCID);
            setProfileImage(mUCID);
        } else {
            getLeadByIdFromServer(str_LeadID);
        }

        return setAppointmentView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpView(View setAppointmentView) {
        ScrollView scrollView = setAppointmentView.findViewById(R.id.scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener((v, event) -> {
            v.requestFocusFromTouch();
            return false;
        });
        ivLead = setAppointmentView.findViewById(R.id.ivLead);
        tvName = setAppointmentView.findViewById(R.id.tvName);
        tvClientCode = setAppointmentView.findViewById(R.id.tvClientCode);
        tvLeadId = setAppointmentView.findViewById(R.id.tvLeadId);
        tvBranchCode = setAppointmentView.findViewById(R.id.tvBranchCode);
        tvCurrentStage = setAppointmentView.findViewById(R.id.tvCurrentStage);
        tvCurrentStageSince = setAppointmentView.findViewById(R.id.tvCurrentStageSince);

        tvSplAttendeeCount = setAppointmentView.findViewById(R.id.tvSplAttendeeCount);
        tvAttendeeCount = setAppointmentView.findViewById(R.id.tvAttendeeCount);
        tvApppointmentDateTime =
                setAppointmentView.findViewById(R.id.tvApppointmentDateTime);
        tvApppointmentDateTimeDisplay =
                setAppointmentView.findViewById(R.id.tvApppointmentDateTimeDisplay);

        spSelectProduct = setAppointmentView.findViewById(R.id.spSelectProduct);
        spRepeateWeek = setAppointmentView.findViewById(R.id.spRepeateWeek);
        spSelectWeekDay = setAppointmentView.findViewById(R.id.spSelectWeekDay);
        spActivityType = setAppointmentView.findViewById(R.id.spActivityType);
        spRepeateOn = setAppointmentView.findViewById(R.id.spRepeateOn);
        tvProductTypeSpError =
                setAppointmentView.findViewById(R.id.tvselectproducterror);
        tvRepeatTypeSpError = setAppointmentView.findViewById(R.id.tvRepeatTypeSpError);
        tvRepeatWeekdayError =
                setAppointmentView.findViewById(R.id.tvRepeatWeekdayError);
        tvActivityTypeError = setAppointmentView.findViewById(R.id.tvActivityTypeError);
        TextView tvNewAppointment =
                setAppointmentView.findViewById(R.id.tvNewAppointment);

        Button btnSave = setAppointmentView.findViewById(R.id.btnSave);
        Button btnReset = setAppointmentView.findViewById(R.id.btnReset);
        ImageButton ibAttendee = setAppointmentView.findViewById(R.id.ibAttendee);
        ImageButton ibSplAttendee =
                setAppointmentView.findViewById(R.id.ibSplAttendee);
        tvStartDateTime = setAppointmentView.findViewById(R.id.tvStartDateTime);
        tvStartDateTimeDisplay =
                setAppointmentView.findViewById(R.id.tvStartDateTimeDisplay);
        tvEndDateTime = setAppointmentView.findViewById(R.id.tvEndDateTime);
        tvEndDateTimeDisplay =
                setAppointmentView.findViewById(R.id.tvEndDateTimeDisplay);
        tvStartDateError = setAppointmentView.findViewById(R.id.tvStartDateError);
        tvEndDateError = setAppointmentView.findViewById(R.id.tvEndDateError);
        tvAPPDateError = setAppointmentView.findViewById(R.id.tvAPPDateError);
        View rlDate = setAppointmentView.findViewById(R.id.rlDate);

        llRepeteOnDaysPannel = setAppointmentView.findViewById(R.id.llRepeteView);

        ibAttendee.setOnClickListener(this);
        ibSplAttendee.setOnClickListener(this);
        tvAttendeeCount.setOnClickListener(this);
        tvSplAttendeeCount.setOnClickListener(this);
        tvApppointmentDateTime.setOnClickListener(this);
        tvApppointmentDateTimeDisplay.setOnClickListener(this);
        rlDate.setOnClickListener(this);
        tvStartDateTime.setOnClickListener(this);
        tvStartDateTimeDisplay.setOnClickListener(this);
        tvEndDateTime.setOnClickListener(this);
        tvEndDateTimeDisplay.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        setCallenderDate();
        AddedAttendeeModel.setAttendeeUserEmplyeeCode(userManager.getUser().employeeCode);
        if (!createMode) {
            tvNewAppointment.setText(getString(txt_label_reschedule_appointment));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ibAttendee) {
            dialogAttendee();
        }
        if (id == R.id.ibSplAttendee) {
            dialogSplAttendee();
        }
        if (id == R.id.tvAttendeeCount) {
            dialogAddedAttendee();
        }
        if (id == R.id.tvSplAttendeeCount) {
            dialogAddedSplAttendee();
        }
        if (id == R.id.tvApppointmentDateTime) {
            selectDateAppointment();
        } else if (id == R.id.tvApppointmentDateTimeDisplay) {
            selectDateAppointment();
        }
        if (id == R.id.rlDate) {
            selectDateAppointment();
        }
        //tvStartDateTime
        if (id == R.id.tvStartDateTime) {
            selectDateAppointmentStartTime();
        } else if (id == R.id.tvStartDateTimeDisplay) {
            selectDateAppointmentStartTime();
        }
        //tvEndDateTime
        if (id == R.id.tvEndDateTime || id == R.id.tvEndDateTimeDisplay) {
            selectDateAppointmentEndTime();
        } else if (id == R.id.btnSave) {

            if (validateForm()) {
                MeetingApptModel meetingApptModel = new MeetingApptModel();
                meetingApptModel.setMeetingType("A");
                meetingApptModel.setDisplayType("A");
                meetingApptModel.setLeadId(str_LeadID);
                meetingApptModel.setUcid(mUCID);
                meetingApptModel.setMeetingDate(
                        CRMAppUtil.getDBDate(currentYear, month, day, hour, minute));
                meetingApptModel.setAppointmentStartTime(
                        CRMAppUtil.getDBDate(currentYearStartDate, monthStartDate, dayStartDate,
                                hourStartDate, minuteStartDate));
                meetingApptModel.setAppointmentEndTime(
                        CRMAppUtil.getDBDate(currentYearEndDate, monthEndDate, dayEndDate, hourEndDate,
                                minuteEndDate));
                meetingApptModel.setMeetingAttendee(addedAttendeeModelList);
                meetingApptModel.setSplMeetingAttendee(addedSplAttendeeModelList);
                meetingApptModel.setProduct(productCodeStr);
                //TODO SwitchRepeateOn check
                if (spRepeateOn.getSelectedItemPosition() > 0) {
                    Object tmp = spRepeateOn.getSelectedItem();
                    if (tmp instanceof RowData) {
                        RowData a = (RowData) tmp;
                        meetingApptModel.setRepeatOnCode(a.getKey());
                    }
                }

                meetingApptModel.setRepeatEveryWeekCode(weekCodeStr);
                meetingApptModel.setRepeatEveryDayCode(weekDayCodeStr);
                meetingApptModel.setActivityTypeID(activityTypeCodeStr);
                if (!createMode) {
                    meetingApptModel.setMeetingID(meetingId);
                }
                createAppointmentSave(meetingApptModel);
            }
        } else if (id == R.id.btnReset) {
            resetForm();
        }//btnReset
    }

    /**
     * form validation
     */
    private boolean validateForm() {
        try {
            String splitstartDate = stringArray(tvStartDateTime.getText().toString());
            String[] newstartDate = splitstartDate.split("-");
            String splitEndDate = stringArray(tvEndDateTime.getText().toString());
            String[] newEndDate = splitEndDate.split("-");

            Date meetingDate = CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute + 5);
            Date NewMeetingDate = CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute);
            Date endDate = CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]),
                    Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), hourEndDate,
                    minuteEndDate);
            Date startDate = CRMAppUtil.getDBDateObj(Integer.parseInt(newstartDate[0]),
                    Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]), hourStartDate,
                    minuteStartDate);

            //For e
            Date startDateEQ = CRMAppUtil.getDBDateObj(Integer.parseInt(newstartDate[0]),
                    Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]), 0, 0);
            Date endDateEQ = CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]),
                    Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), 0, 0);
            if (spActivityType.getSelectedItem()
                    .toString()
                    .equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
                tvActivityTypeError.setVisibility(View.VISIBLE);
                tvActivityTypeError.setText(SELECT_ACTIVITY_TYPE);
                tvActivityTypeError.requestFocus();

                return false;
            } else if (meetingDate.before(new Date())) {

                tvAPPDateError.setVisibility(View.VISIBLE);
                tvAPPDateError.setText(SELECT_APPT_DATE_TYPE);
                tvAPPDateError.requestFocus();
                return false;
            } else if (spSelectProduct.getSelectedItem()
                    .toString()
                    .equalsIgnoreCase(SELECT_PRODUCT)) {
                tvProductTypeSpError.setVisibility(View.VISIBLE);
                tvProductTypeSpError.setText(SELECT_PRODUCT);
                tvProductTypeSpError.requestFocus();
                return false;
            }

            //Do this only if you select repete <code></code>
            if (spRepeateOn.getSelectedItemPosition() > 0) {
                if (spRepeateWeek.getSelectedItem().toString().equalsIgnoreCase(SELECT_WEEK)) {
                    tvRepeatTypeSpError.setVisibility(View.VISIBLE);
                    tvRepeatTypeSpError.setText(SELECT_WEEK);
                    tvRepeatTypeSpError.requestFocus();
                    return false;
                } else if (spSelectWeekDay.getSelectedItem()
                        .toString()
                        .equalsIgnoreCase(SELECT_WEEK_DAYS)) {
                    tvRepeatWeekdayError.setVisibility(View.VISIBLE);
                    tvRepeatWeekdayError.setText(SELECT_WEEK_DAYS);
                    tvRepeatWeekdayError.requestFocus();
                    return false;
                } else if (NewMeetingDate.getTime() > startDate.getTime()) {
                    tvStartDateError.setVisibility(View.VISIBLE);
                    tvStartDateError.setText(START_TIME_ERROR);
                    tvStartDateError.requestFocus();
                    return false;
                } else if (startDate.after(endDate)) {
                    tvEndDateError.setVisibility(View.VISIBLE);
                    tvEndDateError.setText(END_TIME_ERROR);
                    tvEndDateError.requestFocus();
                    return false;
                } else if (startDateEQ.getTime() == endDateEQ.getTime()) {
                    tvEndDateError.setVisibility(View.VISIBLE);
                    tvEndDateError.setText(END_TIME_EQUAL_ERROR);
                    tvEndDateError.requestFocus();
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createAppointmentSave(MeetingApptModel meetingApptModel) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            RequestBody request =
                    RequestBody.create(MediaType.parse("text/plain"), meetingApptModel.getJson());
            Disposable subscribe = getSaathiApi().setAppointmentMeeting(request)
                    .compose(applyUiModel())
                    .subscribe(this::renderAddUpdate);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderAddUpdate(UiModel<SetAppointmentResponse> uiModel) {
        toggleProgress(uiModel.inProgress);
        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        SetAppointmentResponse data = uiModel.data;
        if (data != null) {
            MSFDialog.alertDialog(getActivity(), 0,
                    getResources().getString(motilal_oswal),
                    (createMode) ? SET_APPOINTMENTS : UPDATE_APPOINTMENTS,
                    "OK", true, new MSFDialog.DialogListener() {
                        @Override
                        public void alertDialogAction(
                                MSFDialog.Action action, Object... data) {
                            if (action == MSFDialog.Action.OK) {
                                Bundle bundle = new Bundle();
                                bundle.putString(LEAD_ID,
                                        Objects.requireNonNull(getArguments()).getString(LEAD_ID));
                                bundle.putString(UCID,
                                        getArguments().getString(UCID));
                                navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true, false);
                            }
                        }
                    });
        }
    }

    private void dialogAddedSplAttendee() {
        final Dialog dialogAddedSplAttendee = new Dialog(requireContext());
        dialogAddedSplAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(Objects.requireNonNull(dialogAddedSplAttendee.getWindow()))
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddedSplAttendee.setContentView(R.layout.dialog_addedd_spl_attendee_list);
        dialogAddedSplAttendee.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAddedSplAttendee.setCanceledOnTouchOutside(true);
        ListView lvAttendee = dialogAddedSplAttendee.findViewById(R.id.lvAttendee);
        AddedSplAttendeeAdapter addedSplAttendeeAdapter =
                new AddedSplAttendeeAdapter(getActivity(), addedSplAttendeeModelList);
        lvAttendee.setAdapter(addedSplAttendeeAdapter);
        addedSplAttendeeAdapter.notifyDataSetChanged();
        dialogAddedSplAttendee.show();
    }

    private void dialogAddedAttendee() {
        final Dialog dialogAddedAttendee = new Dialog(requireContext());
        dialogAddedAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialogAddedAttendee.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddedAttendee.setContentView(R.layout.dialog_addedd_attendee_list);
        TextView tvEmpty = dialogAddedAttendee.findViewById(R.id.tvEmpty);
        dialogAddedAttendee.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAddedAttendee.setCanceledOnTouchOutside(true);
        ListView lvAttendee = dialogAddedAttendee.findViewById(R.id.lvAttendee);
        lvAttendee.setEmptyView(tvEmpty);
        AddedAttendeeAdapter addedAttendeeAdapter =
                new AddedAttendeeAdapter(getActivity(), addedAttendeeModelList);
        lvAttendee.setAdapter(addedAttendeeAdapter);
        addedAttendeeAdapter.notifyDataSetChanged();
        dialogAddedAttendee.show();
    }

    private void dialogAttendee() {
        final Dialog dialogAttendeeList = new Dialog(requireContext());
        dialogAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialogAttendeeList.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAttendeeList.setContentView(R.layout.dialog_attendee_list);
        dialogAttendeeList.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAttendeeList.setCanceledOnTouchOutside(true);
        final DelayAutoCompleteTextView etSearchAttendee =
                dialogAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee.setThreshold(3);
        etSearchAttendee
                .setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSearchAttendee.setOnEditorActionListener(this);
        attendeeAdapter = new AttendeeAdapter(getContext());
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
                                clientModel.getAttendeeCode(), clientModel.getMeetingAttendeeCode()));
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
        Objects.requireNonNull(dialogSplAttendeeList.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSplAttendeeList.setContentView(R.layout.dialog_attendee_list);
        dialogSplAttendeeList.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSplAttendeeList.setCanceledOnTouchOutside(true);
        final DelayAutoCompleteTextView etSearchAttendee =
                dialogSplAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee.setThreshold(3);
        splAttendeeAdapter = new SplAttendeeAdapter(getContext());
        etSearchAttendee.setAdapter(splAttendeeAdapter);
        etSearchAttendee.setLoadingIndicator(
                dialogSplAttendeeList.findViewById(R.id.pbLoadingIndicator));
        etSearchAttendee.setOnItemClickListener((parent, view, position, id) -> {
            SplAttendeeModel clientModel = splAttendeeAdapter.getCount() > position ?
                    splAttendeeAdapter.getItem(position) : new SplAttendeeModel("");
            boolean has = listHasAttende(addedAttendeeModelList, addedSplAttendeeModelList,
                    clientModel.getAttendeeCode());

            if (!has) {
                if (clientModel.isNoData()) {
                    return;
                }
                String attendeeType = "S"; //todo //R=Regular  & S=special
                addedSplAttendeeModelList.add(
                        new AddedSplAttendeeModel(attendeeType, true, clientModel.getAttendeeName(),
                                clientModel.getAttendeeCode()));
                addedSplAttendeeCount++;
                tvSplAttendeeCount.setText(
                        format("(%s)", valueOf(addedSplAttendeeCount)));
                dialogSplAttendeeList.dismiss();
            } else {
                Toast.makeText(requireContext(), SPL_ATTENDEE_ALREADY_ADDED,
                        Toast.LENGTH_SHORT).show();
                etSearchAttendee.setText("");
            }
        });

        dialogSplAttendeeList.show();
    }

    //Date picker start

    public String prefix(int input) {
        if (Integer.toString(input).length() == 1) {
            return "0" + input;
        }
        return input + "";
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
        }
    }

    private String stringArray(String date) {
        String[] spaceString = date.split(" ");

        return spaceString[0];
    }

    //@add by sanket on 30/03/2017
    // calls web service takes lead id as parameter
    //@param strLeadId Lead id
    private void getLeadByIdFromServer(CharSequence strLeadId) {
        CRMAppUtil.setImage(new byte[0], ivLead, requireContext());
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LeadViewRequest request =
                    new LeadViewRequest(valueOf(strLeadId));
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
            String leadId = stringOrEmpty(valueOf(data.leadID));
            String branchCode = stringOrEmpty(data.branchcode);
            Contact contact = data.contact;
            String name = stringOrEmpty(Objects.requireNonNull(contact).name);

            List<LeadProductMappingItem> leadProductMapping = data.leadProductMapping;
            for (int i = 0; i < Objects.requireNonNull(leadProductMapping).size(); i++) {
                LeadProductMappingItem item = leadProductMapping.get(i);
                String clientCode = stringOrEmpty(item.clientCode);
                String stageName = stringOrEmpty(item.stageName);
                String isDefault = stringOrEmpty(valueOf(item.isDefault));
                String pendingDays = stringOrEmpty(valueOf(item.pendingDays));
                if (isDefault.equalsIgnoreCase("true")) {
                    tvClientCode.setText(clientCode);
                    tvCurrentStage.setText(stageName);
                    tvCurrentStageSince.setText(format("%s Days", pendingDays));
                }
                tvName.setText(name);
                tvLeadId.setText(leadId);
                tvBranchCode.setText(branchCode);
            }
        }
    }

    private void selectProduct(final String id) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LookupRequest request =
                    new LookupRequest(Objects.requireNonNull(
                            Objects.requireNonNull(
                                    userManager.getUser()).currentBusinessProfile.businessUnitId), INTERESTEDIN,
                            null, null);
            Disposable disposable = getSaathiApi().getAllLookup(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderProduct(uiModel, id));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderProduct(UiModel<List<LookupItem>> uiModel, String id) {
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
                String lookUpId = stringOrEmpty(valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrSelectProduct.add(
                        new RowData(i + 1, lookUpId, lookUpName));
            }
            ArrayAdapter<RowData> arrAdapSelectProduct = new ArrayAdapter<>(requireContext(),
                    R.layout.simple_spinner_item, arrSelectProduct);
            arrAdapSelectProduct.setDropDownViewResource(
                    R.layout.mdropdown);
            spSelectProduct.setAdapter(arrAdapSelectProduct);
            spSelectProduct.setOnItemSelectedListener(
                    selectProductListener);
            arrAdapSelectProduct.notifyDataSetChanged();
            arrAdapSelectProduct.setNotifyOnChange(true);

            RowData productData =
                    getSpinnerRowDataById(id, arrAdapSelectProduct);
            if (productData != null) {
                spSelectProduct.setSelection(productData.getPosition());
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    @SuppressLint("DefaultLocale")
    private void selectDateAppointment() {
        today = Calendar.getInstance();
        dialogDateTime = new Dialog(requireContext());
        dialogDateTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialogDateTime.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogDateTime.setContentView(R.layout.date_picker);
        dialogDateTime.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        DatePicker dpFromDate = dialogDateTime.findViewById(R.id.datePickerFromDate);

        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        close = dialogDateTime.findViewById(R.id.ibtnClose);
        btnSubmit = dialogDateTime.findViewById(R.id.btnSubmit);
        dpFromDate.setMinDate(System.currentTimeMillis() - 1000);
        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                    Integer.parseInt(dateSplit[2]), (view, year, monthOfYear, dayOfMonth) -> {
                        mainFromDate =
                                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYear = year;
                        month = monthOfYear + 1;
                        day = dayOfMonth;
                    });
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

        final TimePicker timePicker = dialogDateTime.findViewById(R.id.timePicker);
        //    timePic

        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
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

        close.setOnClickListener(v -> dialogDateTime.dismiss());

        btnSubmit.setOnClickListener(v -> {

            if (mainFromDate != null) {
                fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                tvApppointmentDateTime.setText(
                        String.format("%s %d:%d:00", fromPostingDate, hour,
                                minute));//Replaced with  T>> -
                tvAPPDateError.setVisibility(View.GONE);
            }
            String meetingDate = stringArray(tvApppointmentDateTime.getText().toString());
            String[] newstartDate = meetingDate.split("-");
            tvApppointmentDateTimeDisplay.setText(CRMAppUtil.getDBDate(
                    CRMAppUtil.getDBDateObj(Integer.parseInt(newstartDate[0]),
                            Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]), hour,
                            minute), DATE_FORMAT_TO_DISPLAY));//Replaced with  T>> -

            dialogDateTime.dismiss();
            tvStartDateError.setVisibility(View.GONE);
        });

        dialogDateTime.show();
    }

    @SuppressLint("DefaultLocale")
    private void selectDateAppointmentStartTime() {
        today = Calendar.getInstance();
        dialogDateTime = new Dialog(requireContext());
        dialogDateTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialogDateTime.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDateTime.setContentView(R.layout.date_picker);
        dialogDateTime.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        DatePicker dpFromDateStartTime =
                dialogDateTime.findViewById(R.id.datePickerFromDate);

        dpFromDateStartTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        close = dialogDateTime.findViewById(R.id.ibtnClose);
        btnSubmit = dialogDateTime.findViewById(R.id.btnSubmit);

        dpFromDateStartTime.setMinDate(System.currentTimeMillis() - 1000);
        if (mainFromDateStartDate != null) {
            String[] dateSplit = mainFromDateStartDate.split("-");
            dpFromDateStartTime.init(Integer.parseInt(dateSplit[0]),
                    Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mainFromDateStartDate =
                                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearStartDate = year;
                        monthStartDate = monthOfYear + 1;
                        dayStartDate = dayOfMonth;
                    });
        } else {
            dpFromDateStartTime.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                    today.get(Calendar.DATE), (view, year, monthOfYear, dayOfMonth) -> {
                        mainFromDateStartDate =
                                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearStartDate = year;
                        monthStartDate = monthOfYear + 1;
                        dayStartDate = dayOfMonth;
                    });
        }

        final TimePicker timePicker = dialogDateTime.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hourStartDate);
            timePicker.setMinute(minuteStartDate);
        } else {
            timePicker.setCurrentHour(hourStartDate);
            timePicker.setCurrentMinute(minuteStartDate);
        }
        //Thisgives api level warning
        timePicker.setOnTimeChangedListener((arg0, arg1, arg2) -> {
            hourStartDate = arg1;
            minuteStartDate = arg2;
        });

        close.setOnClickListener(v -> dialogDateTime.dismiss());

        btnSubmit.setOnClickListener(v -> {

            if (mainFromDateStartDate != null) {
                fromDat = CRMAppUtil.getDate(mainFromDateStartDate, "yyyy-MM-dd");
                fromPostingDateStartDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                tvStartDateTime.setText(
                        String.format("%s-%d:%d:00", fromPostingDateStartDate, hourStartDate,
                                minuteStartDate));
            }
            String splitstartDate = stringArray(tvStartDateTime.getText().toString());
            String[] newstartDate = splitstartDate.split("-");
            String splitEndDate = stringArray(tvEndDateTime.getText().toString());
            String[] newEndDate = splitEndDate.split("-");
            tvStartDateTimeDisplay.setText(CRMAppUtil.getDBDate(
                    CRMAppUtil.getDBDateObj(Integer.parseInt(newstartDate[0]),
                            Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]),
                            hourStartDate, minuteStartDate), DATE_FORMAT_TO_DISPLAY));

            Date meetingDate = CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute);
            Date endDate = CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]),
                    Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), hourEndDate,
                    minuteEndDate);
            Date startDate = CRMAppUtil.getDBDateObj(Integer.parseInt(newstartDate[0]),
                    Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]),
                    hourStartDate, minuteStartDate);
            if (startDate.before(endDate)) {
                tvEndDateError.setVisibility(View.GONE);
            }

            if (meetingDate.getTime() > startDate.getTime()) {
                Toast.makeText(getActivity(), START_TIME_ERROR, Toast.LENGTH_SHORT).show();
            } else {
                dialogDateTime.dismiss();
                tvStartDateError.setVisibility(View.GONE);
                dialogDateTime.dismiss();
            }
        });

        dialogDateTime.show();
    }

    @SuppressLint("DefaultLocale")
    private void selectDateAppointmentEndTime() {
        today = Calendar.getInstance();
        dialogDateTime = new Dialog(requireContext());
        dialogDateTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialogDateTime.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDateTime.setContentView(R.layout.date_picker);
        dialogDateTime.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        DatePicker dpFromDateEndTime =
                dialogDateTime.findViewById(R.id.datePickerFromDate);

        dpFromDateEndTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        close = dialogDateTime.findViewById(R.id.ibtnClose);
        btnSubmit = dialogDateTime.findViewById(R.id.btnSubmit);

        dpFromDateEndTime.setMinDate(System.currentTimeMillis() - 1000);
        if (mainFromDateEndDate != null) {
            String[] dateSplit = mainFromDateEndDate.split("-");
            dpFromDateEndTime.init(Integer.parseInt(dateSplit[0]),
                    Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mainFromDateEndDate =
                                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearEndDate = year;
                        monthEndDate = monthOfYear + 1;
                        dayEndDate = dayOfMonth;
                    });
        } else {
            dpFromDateEndTime.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                    today.get(Calendar.DATE), (view, year, monthOfYear, dayOfMonth) -> {
                        mainFromDateEndDate =
                                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearEndDate = year;
                        monthEndDate = monthOfYear + 1;
                        dayEndDate = dayOfMonth;
                    });
        }

        final TimePicker timePicker = dialogDateTime.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        //Check api and Set timeaccordingly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hourEndDate);
            timePicker.setMinute(minuteEndDate);
        } else {
            timePicker.setCurrentHour(hourEndDate);
            timePicker.setCurrentMinute(minuteEndDate);
        }
        timePicker.setOnTimeChangedListener((arg0, arg1, arg2) -> {
            hourEndDate = arg1;
            minuteEndDate = arg2;
        });

        close.setOnClickListener(v -> dialogDateTime.dismiss());

        btnSubmit.setOnClickListener(v -> {
            if (mainFromDateEndDate != null) {
                fromDat = CRMAppUtil.getDate(mainFromDateEndDate, "yyyy-MM-dd");
                fromPostingDateEndDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                tvEndDateTime.setText(
                        String.format("%s %d:%d:00", fromPostingDateEndDate, hourEndDate,
                                minuteEndDate));
            }
            String splitstartDate = stringArray(tvStartDateTime.getText().toString());
            String[] newstartDate = splitstartDate.split("-");
            String splitEndDate = stringArray(tvEndDateTime.getText().toString());
            String[] newEndDate = splitEndDate.split("-");
            tvEndDateTimeDisplay.setText(CRMAppUtil.getDBDate(
                    CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]),
                            Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]),
                            hourEndDate, minuteEndDate), DATE_FORMAT_TO_DISPLAY));

            Date endDate = CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]),
                    Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), hourEndDate,
                    minuteEndDate);
            Date startDate = CRMAppUtil.getDBDateObj(Integer.parseInt(newstartDate[0]),
                    Integer.parseInt(newstartDate[1]), Integer.parseInt(newstartDate[2]),
                    hourStartDate, minuteStartDate);

            if (startDate.after(endDate)) {
                Toast.makeText(getActivity(), END_TIME_ERROR, Toast.LENGTH_SHORT).show();
            } else {
                dialogDateTime.dismiss();
                tvEndDateError.setVisibility(View.GONE);
            }
            if (endDate.getTime() > startDate.getTime()) {
                tvEndDateError.setVisibility(View.GONE);
            }
        });

        dialogDateTime.show();
    }

    //select week day
    // selectDateAppointmentEndTime
    // Date picker end
    private void selectWeekDay(final String id) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LookupRequest request =
                    new LookupRequest(Objects.requireNonNull(
                            Objects.requireNonNull(
                                    userManager.getUser()).currentBusinessProfile.businessUnitId), DAYS, null,
                            null);
            Disposable disposable = getSaathiApi().getAllLookup(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderWeekDay(uiModel, id));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderWeekDay(UiModel<List<LookupItem>> uiModel, String id) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrSelectWeekDay.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrSelectWeekDay.clear();
            arrSelectWeekDay.add(
                    new RowData(0, "-1", SELECT_WEEK_DAYS));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrSelectWeekDay.add(
                        new RowData(i + 1, lookUpId, lookUpName));
            }
            ArrayAdapter<RowData> arrAdapSelectWeekDay = new ArrayAdapter<>(requireContext(),
                    R.layout.simple_spinner_item, arrSelectWeekDay);
            arrAdapSelectWeekDay.setDropDownViewResource(
                    R.layout.mdropdown);
            spSelectWeekDay.setAdapter(arrAdapSelectWeekDay);
            spSelectWeekDay.setOnItemSelectedListener(
                    selectWeekDayListener);
            arrAdapSelectWeekDay.notifyDataSetChanged();
            RowData productData =
                    getSpinnerRowDataById(id, arrAdapSelectWeekDay);
            if (productData != null) {
                spSelectWeekDay.setSelection(productData.getPosition());
            }
        }
    }

    private void resetForm() {
        spSelectProduct.setSelection(0, true);
        spActivityType.setSelection(0, true);
        spRepeateOn.setSelection(0, true);
        spSelectWeekDay.setSelection(0, true);
        spRepeateWeek.setSelection(0, true);
        addedAttendeeModelList.clear();
        tvAttendeeCount.setText("");
        setCallenderDate();
    }

    private void setCallenderDate() {
        mainFromDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");

        String[] dateArray = Objects.requireNonNull(mainFromDate).split("-");
        currentYear = ((Integer.parseInt(dateArray[0])));

        currentYearStartDate = currentYear;
        currentYearEndDate = currentYear;
        month = ((Integer.parseInt(dateArray[1])));
        day = ((Integer.parseInt(dateArray[2])));

        mainFromDateEndDate = mainFromDate;
        mainFromDateStartDate = mainFromDate;
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        hourEndDate = hour;
        minuteEndDate = minute;
        hourStartDate = hour;
        minuteStartDate = minute;
        tvEndDateTime.setText(CRMAppUtil.getCurrentDisplayDate());
        tvEndDateTimeDisplay.setText(CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));
        tvStartDateTime.setText(CRMAppUtil.getCurrentDisplayDate());
        tvStartDateTimeDisplay.setText(CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));
        tvApppointmentDateTime.setText(CRMAppUtil.getCurrentDisplayDate());

        tvApppointmentDateTimeDisplay.setText(
                CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));//Replaced with  T>> -
    }

    private void activityType(final String id) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LookupRequest request =
                    new LookupRequest(Objects.requireNonNull(
                            Objects.requireNonNull(
                                    userManager.getUser()).currentBusinessProfile.businessUnitId), ACTIVITYTYPE,
                            null, null);
            Disposable disposable = getSaathiApi().getAllLookup(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderActivityType(uiModel, id));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderActivityType(UiModel<List<LookupItem>> uiModel, String id) {
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
                String lookUpId = stringOrEmpty(valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrActivityType.add(
                        new RowData(i + 1, lookUpId, lookUpName));
            }
            ArrayAdapter<RowData> arrAdapActivityType = new ArrayAdapter<>(requireContext(),
                    R.layout.simple_spinner_item, arrActivityType);
            arrAdapActivityType.setDropDownViewResource(
                    R.layout.mdropdown);
            spActivityType.setAdapter(arrAdapActivityType);
            spActivityType.setOnItemSelectedListener(
                    selectActivityTypeListener);
            arrAdapActivityType.notifyDataSetChanged();
            RowData productData =
                    getSpinnerRowDataById(id, arrAdapActivityType);
            if (productData != null) {
                spActivityType.setSelection(productData.getPosition());
            }
        }
    }

    //region Update mode
    //@add by sanket on 30/03/2017
    // calls web service takes meeting id as parameter
    //@param  strMeetingId
    private void getAppointmentDetailsFromServer(CharSequence strMeetingId) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            MeetingByIdRequest request =
                    new MeetingByIdRequest(meetingId);
            Disposable subscribe = getSaathiApi().getMeetingById(request)
                    .compose(applyUiModel())
                    .subscribe(this::renderMeetingById);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderMeetingById(UiModel<MeetingByIdItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        MeetingByIdItem data = uiModel.data;
        if (data != null) {
            MeetingApptModel mModel = getAppointmentDet(data);
            setCallenderDateFrom(mModel);
            selectProduct(mModel.getProduct());
            activityType(mModel.getActivityTypeID());
            getRepeatMode(mModel.getRepeatOnCode());
            selectWeek(mModel.getRepeatEveryWeekCode());
            selectWeekDay(mModel.getRepeatEveryDayCode());
        }
    }

    private MeetingApptModel getAppointmentDet(MeetingByIdItem response) {
        MeetingApptModel mModel = new MeetingApptModel();
        //        response.get(MEETING_ID);

        mModel.setMeetingType(stringOrEmpty(response.meetingType));
        mModel.setDisplayType("A");
        mModel.setMeetingID(stringOrEmpty(response.meetingID));
        mModel.setLeadId(valueOf(response.leadId));
        //TODO LEAD ID IS DIFFRENT
        mModel.setMeetingDate(stringOrEmpty(response.meetingDate));
        mModel.setActivityTypeID(stringOrEmpty(response.activityTypeID));
        mModel.setProduct(stringOrEmpty(response.productTypeID));
        mModel.setAppointmentStartTime(stringOrEmpty(response.repeatationStartOn));
        mModel.setAppointmentEndTime(stringOrEmpty(response.repeatationEndsOn));

        mModel.setRepeatEveryWeekCode(
                stringOrEmpty(response.repeatEveryWeekCode)
        );
        mModel.setRepeatEveryDayCode(stringOrEmpty(response.repeatEveryDayCode));
        mModel.setRepeatOnCode(
                stringOrEmpty(response.repeatOnCode));

        List<MeetingAttendeeItem> meetingAttendee = response.meetingAttendee;
        for (int meetingAttendee_i = 0; meetingAttendee_i < meetingAttendee.size();
             meetingAttendee_i++) {
            MeetingAttendeeItem meetingAttendee_obj = meetingAttendee.get(meetingAttendee_i);
            String str_AttendeeType = meetingAttendee_obj.attendeeType;

            String str_AttendeeEmpcode = meetingAttendee_obj.attendeeEmpcode;

            String str_AttendeeEmpName = meetingAttendee_obj.attendeeEmpName;
            String str_meetingAttendeeCode = meetingAttendee_obj.meetingAttendeeID;

            boolean attended = meetingAttendee_obj.attended;

            boolean defaultAttendee = meetingAttendee_obj.defaultAttendee;
            int status = meetingAttendee_obj.status;
            if (status == 1) {
                if (!defaultAttendee) {
                    //TODO ignore self object
                    if (Objects.requireNonNull(str_AttendeeType).trim().equalsIgnoreCase("R")) {
                        AddedAttendeeModel attendTmp =
                                new AddedAttendeeModel(str_AttendeeType, attended, str_AttendeeEmpName,
                                        str_AttendeeEmpcode, str_meetingAttendeeCode);
                        addedAttendeeModelList.add(attendTmp);
                        addedAttendeeCount++;
                    } else {
                        AddedSplAttendeeModel tempSpAttendee =
                                new AddedSplAttendeeModel(str_AttendeeType, attended,
                                        str_AttendeeEmpName, str_AttendeeEmpcode);
                        addedSplAttendeeModelList.add(tempSpAttendee);
                        addedSplAttendeeCount++;
                    }
                }
            }
        }
        tvSplAttendeeCount.setText(String.format("(%s)", valueOf(addedSplAttendeeCount)));
        setCount(addedAttendeeCount);
        mModel.setMeetingAttendee(addedAttendeeModelList);
        mModel.setSplMeetingAttendee(addedSplAttendeeModelList);

        return mModel;
    }

    private void setCallenderDateFrom(MeetingApptModel mModel) {

        Date mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.getMeetingDate());
        tvApppointmentDateTime.setText(CRMAppUtil.getDBDate(mMeetingDate, mFormatWithSpace));
        tvApppointmentDateTimeDisplay.setText(
                CRMAppUtil.getDBDate(mMeetingDate, DATE_FORMAT_TO_DISPLAY));
        Calendar cal = Calendar.getInstance();
        cal.setTime(mMeetingDate);
        currentYear = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;//Adding one onth
        day = cal.get(Calendar.DAY_OF_MONTH);
        minute = cal.get(Calendar.MINUTE);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        mainFromDate = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithDash);

        //Start date
        mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.getAppointmentStartTime());
        tvStartDateTime.setText(CRMAppUtil.getDBDate(mMeetingDate, mFormatWithSpace));
        tvStartDateTimeDisplay.setText(CRMAppUtil.getDBDate(mMeetingDate, DATE_FORMAT_TO_DISPLAY));

        cal.setTime(mMeetingDate);
        currentYearStartDate = cal.get(Calendar.YEAR);
        monthStartDate = cal.get(Calendar.MONTH) + 1;//Adding one onth
        dayStartDate = cal.get(Calendar.DAY_OF_MONTH);
        minuteStartDate = cal.get(Calendar.MINUTE);
        hourStartDate = cal.get(Calendar.HOUR_OF_DAY);
        mainFromDateStartDate = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithDash);
        //end Date
        mMeetingDate = CRMAppUtil.getDBDateFrom(mModel.getAppointmentEndTime());
        tvEndDateTime.setText(CRMAppUtil.getDBDate(mMeetingDate, mFormatWithSpace));
        tvEndDateTimeDisplay.setText(CRMAppUtil.getDBDate(mMeetingDate, DATE_FORMAT_TO_DISPLAY));

        cal.setTime(mMeetingDate);
        currentYearEndDate = cal.get(Calendar.YEAR);
        monthEndDate = cal.get(Calendar.MONTH) + 1;//Adding one onth
        dayEndDate = cal.get(Calendar.DAY_OF_MONTH);
        minuteEndDate = cal.get(Calendar.MINUTE);
        hourEndDate = cal.get(Calendar.HOUR_OF_DAY);
        mainFromDateEndDate = CRMAppUtil.getDBDate(mMeetingDate, mFormatWithDash);
    }

    //endregion
    //region Lookups call
    private void getRepeatMode(final String id) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LookupRequest request =
                    new LookupRequest(Objects.requireNonNull(
                            Objects.requireNonNull(
                                    userManager.getUser()).currentBusinessProfile.businessUnitId), REPEATMODE,
                            null, null);
            Disposable disposable = getSaathiApi().getAllLookup(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderRepeatMode(uiModel, id));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderRepeatMode(UiModel<List<LookupItem>> uiModel, String id) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrRepeatMode.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrRepeatMode.clear();
            arrRepeatMode.add(new RowData(0, "-1", "Repeat On"));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrRepeatMode.add(
                        new RowData(i + 1, lookUpId, lookUpName));
            }
            ArrayAdapter<RowData> arrAdapRepeteMode = new ArrayAdapter<>(requireContext(),
                    R.layout.simple_spinner_item, arrRepeatMode);
            arrAdapRepeteMode.setDropDownViewResource(
                    R.layout.mdropdown);
            spRepeateOn.setAdapter(arrAdapRepeteMode);
            spRepeateOn.setOnItemSelectedListener(
                    selectRepeteModeListener);
            arrAdapRepeteMode.notifyDataSetChanged();
            RowData productData =
                    getSpinnerRowDataById(id, arrAdapRepeteMode);
            if (productData != null) {
                spRepeateOn.setSelection(productData.getPosition());
            }
        }
    }

    // selectDateAppointmentEndTime
    // Date picker end
    private void selectWeek(final String id) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LookupRequest request =
                    new LookupRequest(Objects.requireNonNull(
                            Objects.requireNonNull(
                                    userManager.getUser()).currentBusinessProfile.businessUnitId), WEEKS, null,
                            null);
            Disposable disposable = getSaathiApi().getAllLookup(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderWeek(uiModel, id));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderWeek(UiModel<List<LookupItem>> uiModel, String id) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrSelectWeek.clear();
            showOnFailurePopup(error);
        }
        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrSelectWeek.clear();
            arrSelectWeek.add(new RowData(0, "-1", SELECT_WEEK));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrSelectWeek.add(
                        new RowData(i + 1, lookUpId, lookUpName));
            }
            ArrayAdapter<RowData> arrAdapSelectWeek = new ArrayAdapter<>(requireContext(),
                    R.layout.simple_spinner_item, arrSelectWeek);
            arrAdapSelectWeek.setDropDownViewResource(
                    R.layout.mdropdown);
            spRepeateWeek.setAdapter(arrAdapSelectWeek);
            spRepeateWeek.setOnItemSelectedListener(selectWeekListener);
            arrAdapSelectWeek.notifyDataSetChanged();
            RowData productData =
                    getSpinnerRowDataById(id, arrAdapSelectWeek);
            if (productData != null) {
                spRepeateWeek.setSelection(productData.getPosition());
            }
        }
    }

    //Attende count
    private void setCount(int cnt) {
        if (cnt <= 0) {
            tvAttendeeCount.setText("");
        } else {
            tvAttendeeCount.setText(String.format("(%s)", valueOf(cnt)));
        }
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
            final AttendeeAdapter.HolderView holderView;
            final EmployeeModel clientModel = resultList.get(position);
            if (convertView == null) {
                inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                holderView.tvAttendeeName.setText(
                        String.format("%s-%s-%s", clientModel.getAttendeeCode(),
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
            // viewColor(position, convertView);

            viewColor(position, convertView);
            if (clientModel.isNoData()) {
                holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
            } else {
                holderView.tvAttendeeName.setText(
                        String.format("%s-%s-%s", clientModel.getAttendeeCode(),
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
                                    return filterResults;
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

            private HolderView(View parent) {
                tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
            }
        }
    }

    public class AddedAttendeeAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        Context mContext;
        List<AddedAttendeeModel> attendeeModelList;
        private ArrayList<EmployeeModel> arraylist;

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            AddedAttendeeAdapter.Holder holder = new AddedAttendeeAdapter.Holder();
            view = inflater.inflate(R.layout.item_list_added_attendee, parent, false);

            AddedAttendeeModel attendeeModel = attendeeModelList.get(position);

            holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
            holder.ivDelete = view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(
                    String.format("%s-%s", attendeeModel.getAttendeeEmplyeeCode(),
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

        private AddedSplAttendeeAdapter(Context c,
                                        List<AddedSplAttendeeModel> attendeeModelList) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            AddedSplAttendeeAdapter.Holder holder = new AddedSplAttendeeAdapter.Holder();
            view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false);

            AddedSplAttendeeModel attendeeModel = attendeeModelList.get(position);

            holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
            holder.ivDelete = view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(
                    String.format("%s-%s", attendeeModel.getAttendeeEmplyeeCode(),
                            attendeeModel.getAttendeeName()));
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedSplAttendeeCount--;
                    tvSplAttendeeCount.setText(
                            String.format("(%s)", valueOf(addedSplAttendeeCount)));
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
