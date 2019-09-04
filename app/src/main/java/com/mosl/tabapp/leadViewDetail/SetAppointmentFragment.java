package com.mosl.tabapp.leadViewDetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mosl.app.core.data.UiModel;
import com.mosl.app.core.data.UiModelHelper;
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
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.AddedAttendeeModel;
import com.mosl.tabapp.model.AddedSplAttendeeModel;
import com.mosl.tabapp.model.AddedSplSetAttendeeModel;
import com.mosl.tabapp.model.ClientDetail;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.MeetingApptModel;
import com.mosl.tabapp.model.SetAddAttendeeModel;
import com.mosl.tabapp.model.SetAttendeeModel;
import com.mosl.tabapp.model.SplAttendeeModel;
import com.mosl.tabapp.model.SplSetAttendeeModel;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;

/**
 * Created by Piyush on 04-03-2017.
 */
public class SetAppointmentFragment extends MOSLCommonFragment
    implements View.OnClickListener, TextView.OnEditorActionListener {

    View setAppointmentView, llRepeteOnDaysPannel;
    // private SwitchCompat SwitchRepeateOn;
    // spMeetingType,

    private TextView tvProductTypeSpError, tvRepeatTypeSpError, tvRepeatWeekdayError;
    private Spinner spSelectProduct, spRepeateWeek, spSelectWeekDay, spActivityType, spRepeateOn;
    private final ArrayList<RowData> arrSelectProduct = new ArrayList<>();
    private TextView tvActivityTypeError, tvNewAppointment;
    private View rlDate;
    private ImageButton ibAttendee, ibSplAttendee;
    private Button btnSave, btnReset;
    private ImageView ivUserImage, ivLead;
    private ListView lvAddedAttendee;
    private ArrayList<SplSetAttendeeModel> SplSetAttendeeModelList;
    private ArrayList<AddedSplSetAttendeeModel> addedSplSetAttendeeModelList = new ArrayList<>();
    private ArrayList<SetAttendeeModel> setattendeeModelList;
    private ArrayList<SetAddAttendeeModel> setaddAttendeeModelList = new ArrayList<>();
    int addedAttendeeCount;
    int addedSplAttendeeCount;
    //  int addedSplSetAttendeeCount;
    public static final String TAG = SetAppointmentFragment.class.getSimpleName();
    private String str_LeadID = "";
    //  private RadioButton rbDays;
    private String productCodeStr;//, productNameStr;
    private ArrayAdapter<RowData> arrAdapSelectProduct;
    private final ArrayList<RowData> arrSelectWeek = new ArrayList<>();
    private ArrayList<AddedAttendeeModel> addedAttendeeModelList = new ArrayList<>();
    private AttendeeAdapter attendeeAdapter;
    private ArrayList<AddedSplAttendeeModel> addedSplAttendeeModelList = new ArrayList<>();
    private SplAttendeeAdapter splAttendeeAdapter;
    private AddedAttendeeAdapter addedAttendeeAdapter;
    private AddedSplAttendeeAdapter addedSplAttendeeAdapter;
    private final ArrayList<RowData> arrSelectWeekDay = new ArrayList<>();
    private ArrayAdapter<RowData> arrAdapSelectWeek;
    private String weekCodeStr, weekNameStr, weekDayCodeStr, weekDayNameStr;
    private final ArrayList<RowData> arrActivityType = new ArrayList<>();
    private ArrayAdapter<RowData> arrAdapSelectWeekDay;
    private ArrayAdapter<RowData> arrAdapActivityType;
    private final ArrayList<RowData> arrRepeatMode = new ArrayList<>();
    //date picker variable
    private Calendar today;
    private Dialog dialogDateTime;
    private DatePicker dpFromDate;
    private DatePicker dpFromDateStartTime;
    private DatePicker dpFromDateEndTime;
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
    private String lookUpIdRepeatMode;
    private String activityTypeCodeStr, activityTypeNameStr;
    private Date fromDat;//, toDate;
    private String meetingId = "";
    private boolean createMode = true;
    private ArrayAdapter<RowData> arrAdapRepeteMode;
    private TextView tvCurrentStageSince, tvCurrentStage, tvEndDateTimeDisplay, tvEndDateTime,
        tvBranchCode, tvLeadId, tvClientCode, tvName, tvSplAttendeeCount, tvSplAttendee, tvAttendee,
        tvAttendeeCount, tvApppointmentDateTimeDisplay, tvApppointmentDateTime,
        tvStartDateTimeDisplay, tvStartDateTime;
    private String mUCID = "0";
    private ScrollView scrollView;
    private TextView tvStartDateError, tvEndDateError;
    private TextView tvAPPDateError;
    private AdapterView.OnItemSelectedListener selectProductListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    tvProductTypeSpError.setVisibility(View.GONE);

                    productCodeStr = ((RowData) spSelectProduct.getSelectedItem()).getKey();
                    //   productNameStr = ((RowData) spSelectProduct.getSelectedItem()).getValue();

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
                    weekDayNameStr = ((RowData) spSelectWeekDay.getSelectedItem()).getValue();
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
    private AdapterView.OnItemSelectedListener selectActivityTypeListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    activityTypeCodeStr = ((RowData) spActivityType.getSelectedItem()).getKey();
                    activityTypeNameStr = ((RowData) spActivityType.getSelectedItem()).getValue();
                    tvActivityTypeError.setVisibility(View.GONE);
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
    //endregion
    //region DropDown listner
    private AdapterView.OnItemSelectedListener selectWeekListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    tvRepeatTypeSpError.setVisibility(View.GONE);
                    weekCodeStr = ((RowData) spRepeateWeek.getSelectedItem()).getKey();
                    weekNameStr = ((RowData) spRepeateWeek.getSelectedItem()).getValue();
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
               /* Object tmp = parent.getSelectedItem();
                if (tmp instanceof RowData) {
                    RowData mData = (RowData) tmp;

                    if (mData.getValue().contentEquals("Days")) {
                        llRepeteOnDaysPannel.setVisibility(View.VISIBLE);
                    } else {
                        llRepeteOnDaysPannel.setVisibility(View.GONE);
                    }
                } else {
                    llRepeteOnDaysPannel.setVisibility(View.GONE);
                }*/

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

    //endregion
    //deos dedup on list
    public static boolean listHasAttende(List<AddedAttendeeModel> addedAttendeeModelList,
        List<AddedSplAttendeeModel> addedSplAttendeeModelList, String empID) {
        //    addedAttendeeModelList
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        setAppointmentView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_set_appiontment);
        setAppTitle(getClass().toString(), "Set Appointment");
        // getting look up from server

        try {
            mUCID = this.getArguments().getString(UCID, "0").equalsIgnoreCase("") ? "0"
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
        setUpView(setAppointmentView);
        if (!mUCID.equalsIgnoreCase("0")) {
            getClientDetail(mUCID);
            setProfileImage(mUCID);
        } else {
            getLeadByIdFromServer(str_LeadID);
        }

        return setAppointmentView;
    }

    private void setUpView(View setAppointmentView) {
        scrollView = (ScrollView) setAppointmentView.findViewById(R.id.scrollView);
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
        ivLead = (ImageView) setAppointmentView.findViewById(R.id.ivLead);
        tvName = (TextView) setAppointmentView.findViewById(R.id.tvName);
        tvClientCode = (TextView) setAppointmentView.findViewById(R.id.tvClientCode);
        tvLeadId = (TextView) setAppointmentView.findViewById(R.id.tvLeadId);
        tvBranchCode = (TextView) setAppointmentView.findViewById(R.id.tvBranchCode);
        tvCurrentStage = (TextView) setAppointmentView.findViewById(R.id.tvCurrentStage);
        tvCurrentStageSince = (TextView) setAppointmentView.findViewById(R.id.tvCurrentStageSince);

        tvSplAttendeeCount = (TextView) setAppointmentView.findViewById(R.id.tvSplAttendeeCount);
        tvSplAttendee = (TextView) setAppointmentView.findViewById(R.id.tvSplAttendee);
        tvAttendeeCount = (TextView) setAppointmentView.findViewById(R.id.tvAttendeeCount);
        tvAttendee = (TextView) setAppointmentView.findViewById(R.id.tvAttendee);
        tvApppointmentDateTime =
            (TextView) setAppointmentView.findViewById(R.id.tvApppointmentDateTime);
        tvApppointmentDateTimeDisplay =
            (TextView) setAppointmentView.findViewById(R.id.tvApppointmentDateTimeDisplay);

        // spMeetingType = (Spinner) setAppointmentView.findViewById(R.id.spMeetingType);
        spSelectProduct = (Spinner) setAppointmentView.findViewById(R.id.spSelectProduct);
        spRepeateWeek = (Spinner) setAppointmentView.findViewById(R.id.spRepeateWeek);
        spSelectWeekDay = (Spinner) setAppointmentView.findViewById(R.id.spSelectWeekDay);
        spActivityType = (Spinner) setAppointmentView.findViewById(R.id.spActivityType);
        spRepeateOn = (Spinner) setAppointmentView.findViewById(R.id.spRepeateOn);
        tvProductTypeSpError =
            (TextView) setAppointmentView.findViewById(R.id.tvselectproducterror);
        // tvRepeatTypeSpError
        tvRepeatTypeSpError = (TextView) setAppointmentView.findViewById(R.id.tvRepeatTypeSpError);
        tvRepeatWeekdayError =
            (TextView) setAppointmentView.findViewById(R.id.tvRepeatWeekdayError);
        tvActivityTypeError = (TextView) setAppointmentView.findViewById(R.id.tvActivityTypeError);
        tvNewAppointment = (TextView) setAppointmentView.findViewById(R.id.tvNewAppointment);

        btnSave = (Button) setAppointmentView.findViewById(R.id.btnSave);
        btnReset = (Button) setAppointmentView.findViewById(R.id.btnReset);
        ibAttendee = (ImageButton) setAppointmentView.findViewById(R.id.ibAttendee);
        ibSplAttendee = (ImageButton) setAppointmentView.findViewById(R.id.ibSplAttendee);
        // rbDays = (RadioButton) setAppointmentView.findViewById(R.id.rbDays);
        tvStartDateTime = (TextView) setAppointmentView.findViewById(R.id.tvStartDateTime);
        tvStartDateTimeDisplay =
            (TextView) setAppointmentView.findViewById(R.id.tvStartDateTimeDisplay);
        tvEndDateTime = (TextView) setAppointmentView.findViewById(R.id.tvEndDateTime);
        tvEndDateTimeDisplay =
            (TextView) setAppointmentView.findViewById(R.id.tvEndDateTimeDisplay);
        tvStartDateError = (TextView) setAppointmentView.findViewById(R.id.tvStartDateError);
        tvEndDateError = (TextView) setAppointmentView.findViewById(R.id.tvEndDateError);
        tvAPPDateError = (TextView) setAppointmentView.findViewById(R.id.tvAPPDateError);
        rlDate = setAppointmentView.findViewById(R.id.rlDate);

        llRepeteOnDaysPannel = setAppointmentView.findViewById(R.id.llRepeteView);
       /* SwitchRepeateOn = (SwitchCompat) setAppointmentView.findViewById(R.id.SwitchRepeateOn);
        SwitchRepeateOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.SwitchRepeateOn: {
                        if (isChecked) {
                            llRepeteOnDaysPannel.setVisibility(View.VISIBLE);
                        } else {
                            llRepeteOnDaysPannel.setVisibility(View.GONE);
                        }
                    }
                    break;
                }
            }
        });*/

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
        AddedAttendeeModel.setAttendeeUserEmplyeeCode(AccountDetails.getEmployeeCode());
        // if(meetingId!=null && meetingId.length()>1 ){
        if (!createMode) {
            //reschedule appointment
            tvNewAppointment.setText("Reschedule appointment");
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
        //tvApppointmentDateTime
        //opening apoint ment date picker
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
                //MEETINGTYPE
                meetingApptModel.setMeetingType("A");
                meetingApptModel.setDisplayType("A");
                meetingApptModel.setLeadId(str_LeadID);
                meetingApptModel.setUcid(mUCID);
                // meetingApptModel.setMeetingDate(CRMAppUtil.getDBDate(currentYear, month, day, hour, minute));
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
                //  if(meetingId!=null && meetingId.length()>1 ){
                if (!createMode) {
                    //Set meeting id in edit mode only
                    meetingApptModel.setMeetingID(meetingId);
                }
                createAppointmentSave(meetingApptModel);
            }
        } else if (id == R.id.btnReset) {
            Log.v(TAG, "Reset called");
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
            // Log.v("String",spSelectProduct.getSelectedItem().toString());
            //Added on 9/6/2017 sanket
            if (spActivityType.getSelectedItem()
                .toString()
                .equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
                tvActivityTypeError.setVisibility(View.VISIBLE);
                tvActivityTypeError.setText(SELECT_ACTIVITY_TYPE);
                tvActivityTypeError.requestFocus();

                return false;
            }
            //  CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute);

            else if (meetingDate.before(new Date())) {

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
        /*addedAttendeeModelList;
        addedSplAttendeeModelList;*/
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            RequestBody request =
                RequestBody.create(MediaType.parse("text/plain"), meetingApptModel.getJson());
            Disposable subscribe = saathiApi.setAppointmentMeeting(request)
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
                getResources().getString(R.string.motilal_oswal),
                (createMode) ? SET_APPOINTMENTS : UPDATE_APPOINTMENTS,
                "OK", true, new MSFDialog.DialogListener() {
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
        }
    }

    private void dialogAddedSplAttendee() {
        final Dialog dialogAddedSplAttendee = new Dialog(getContext());
        dialogAddedSplAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddedSplAttendee.getWindow()
            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddedSplAttendee.setContentView(R.layout.dialog_addedd_spl_attendee_list);
        dialogAddedSplAttendee.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAddedSplAttendee.setCanceledOnTouchOutside(true);
        ListView lvAttendee = (ListView) dialogAddedSplAttendee.findViewById(R.id.lvAttendee);
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
        TextView tvEmpty = (TextView) dialogAddedAttendee.findViewById(R.id.tvEmpty);
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
        ListView lvAttendee = (ListView) dialogAttendeeList.findViewById(R.id.lvAttendee);
        final DelayAutoCompleteTextView etSearchAttendee =
            (DelayAutoCompleteTextView) dialogAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee.setThreshold(3);
        etSearchAttendee
            .setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSearchAttendee.setOnEditorActionListener(this);
        attendeeAdapter = new AttendeeAdapter(getContext());
        etSearchAttendee.setAdapter(attendeeAdapter);
        etSearchAttendee.setLoadingIndicator(
            (ProgressBar) dialogAttendeeList.findViewById(R.id.pbLoadingIndicator));
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
                            clientModel.getAttendeeCode(), clientModel.getMeetingAttendeeCode()));
                    addedAttendeeCount++;
                    //  tvAttendeeCount.setText("(" + String.valueOf(addedAttendeeCount) + ")");
                    setCount(addedAttendeeCount);
                    dialogAttendeeList.dismiss();
                } else {
                    //clear It
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
        ListView lvAttendee = (ListView) dialogSplAttendeeList.findViewById(R.id.lvAttendee);

        final DelayAutoCompleteTextView etSearchAttendee =
            (DelayAutoCompleteTextView) dialogSplAttendeeList.findViewById(R.id.etSearchAttendee);
        etSearchAttendee.setThreshold(3);
        splAttendeeAdapter = new SplAttendeeAdapter(getContext());
        etSearchAttendee.setAdapter(splAttendeeAdapter);
        etSearchAttendee.setLoadingIndicator(
            (ProgressBar) dialogSplAttendeeList.findViewById(R.id.pbLoadingIndicator));
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
                    String attendeeType = "S"; //todo //R=Regular  & S=special
                    addedSplAttendeeModelList.add(
                        new AddedSplAttendeeModel(attendeeType, true, clientModel.getAttendeeName(),
                            clientModel.getAttendeeCode()));
                    // etSearchAttendee.setText(clientModel.getAttendeeName());
                    addedSplAttendeeCount++;
                    tvSplAttendeeCount.setText("(" + String.valueOf(addedSplAttendeeCount) + ")");
                    dialogSplAttendeeList.dismiss();
                } else {
                    //clear It
                    Toast.makeText(getMainActivity(), SPL_ATTENDEE_ALREADY_ADDED,
                        Toast.LENGTH_SHORT).show();
                    etSearchAttendee.setText("");
                }
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
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientImageRequest request =
                new ClientImageRequest(ucid);
            Disposable subscribe = saathiApi.getClientImage(request)
                .compose(UiModelHelper.applyUiModel())
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
                CRMAppUtil.setImage(byt, ivLead, getMainActivity());
            } else {
                CRMAppUtil.setImage(new byte[0], ivLead,
                    getMainActivity());
            }
        }
    }

    private void getClientDetail(String ucid) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientDetailRequest request =
                new ClientDetailRequest(Integer.valueOf(ucid));
            Disposable subscribe = saathiApi.getClientDetail(request)
                .compose(UiModelHelper.applyUiModel())
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
                    ClientDetail.getCurrentStageSince() + " Days");
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
        CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LeadViewRequest request =
                new LeadViewRequest(String.valueOf(strLeadId));
            Disposable subscribe = saathiApi.getLeadView(request)
                .compose(UiModelHelper.applyUiModel())
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
                    tvCurrentStageSince.setText(pendingDays + " Days");
                }
                tvName.setText(name);
                tvLeadId.setText(leadId);
                tvBranchCode.setText(branchCode);
            }
        }
    }

    private void selectProduct(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), INTERESTEDIN, null, null);
            Disposable disposable = saathiApi.getAllLookup(request)
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
                String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrSelectProduct.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapSelectProduct = new ArrayAdapter<>(getMainActivity(),
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
        hideKeyboard(textView);
        return false;
    }

    public void selectDateAppointment() {
        today = Calendar.getInstance();
        dialogDateTime = new Dialog(context);
        dialogDateTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDateTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogDateTime.setContentView(R.layout.date_picker);
        dialogDateTime.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDate = (DatePicker) dialogDateTime.findViewById(R.id.datePickerFromDate);

        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        close = (ImageView) dialogDateTime.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialogDateTime.findViewById(R.id.btnSubmit);
        dpFromDate.setMinDate(System.currentTimeMillis() - 1000);
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

        final TimePicker timePicker = (TimePicker) dialogDateTime.findViewById(R.id.timePicker);
        //    timePic

        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
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
                // TODO Auto-generated method stub
                //                hour = arg0.getHour();
                //                minute = arg0.getMinute();

                hour = arg1;
                minute = arg2;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDateTime.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainFromDate != null) {
                    fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                    fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                    //  tvApppointmentDateTime.setText(fromPostingDate + "T" + hour + ":" + minute + ":" + "00");
                    tvApppointmentDateTime.setText(fromPostingDate
                        + " "
                        + hour
                        + ":"
                        + minute
                        + ":"
                        + "00");//Replaced with  T>> -
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
            }
        });

        dialogDateTime.show();
    }

    public void selectDateAppointmentStartTime() {
        today = Calendar.getInstance();
        dialogDateTime = new Dialog(context);
        dialogDateTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDateTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDateTime.setContentView(R.layout.date_picker);
        dialogDateTime.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDateStartTime = (DatePicker) dialogDateTime.findViewById(R.id.datePickerFromDate);

        dpFromDateStartTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        //  dpToDate = (DatePicker) dialogDateTime.findViewById(R.id.datePickerToDate);
        close = (ImageView) dialogDateTime.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialogDateTime.findViewById(R.id.btnSubmit);

        dpFromDateStartTime.setMinDate(System.currentTimeMillis() - 1000);
        if (mainFromDateStartDate != null) {
            String[] dateSplit = mainFromDateStartDate.split("-");
            dpFromDateStartTime.init(Integer.parseInt(dateSplit[0]),
                Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDateStartDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearStartDate = year;
                        monthStartDate = monthOfYear + 1;
                        dayStartDate = dayOfMonth;
                    }
                });
        } else {
            dpFromDateStartTime.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDateStartDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearStartDate = year;
                        monthStartDate = monthOfYear + 1;
                        dayStartDate = dayOfMonth;
                    }
                });
        }

        final TimePicker timePicker = (TimePicker) dialogDateTime.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hourStartDate);
            timePicker.setMinute(minuteStartDate);
        } else {
            timePicker.setCurrentHour(hourStartDate);
            timePicker.setCurrentMinute(minuteStartDate);
        }
        //Thisgives api level warning
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
                hourStartDate = arg1;
                minuteStartDate = arg2;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDateTime.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainFromDateStartDate != null) {
                    fromDat = CRMAppUtil.getDate(mainFromDateStartDate, "yyyy-MM-dd");
                    fromPostingDateStartDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                    tvStartDateTime.setText(fromPostingDateStartDate
                        + "-"
                        + hourStartDate
                        + ":"
                        + minuteStartDate
                        + ":"
                        + "00");
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
            }
        });

        dialogDateTime.show();
    }

    public void selectDateAppointmentEndTime() {
        today = Calendar.getInstance();
        dialogDateTime = new Dialog(context);
        dialogDateTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDateTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDateTime.setContentView(R.layout.date_picker);
        dialogDateTime.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDateEndTime = (DatePicker) dialogDateTime.findViewById(R.id.datePickerFromDate);

        dpFromDateEndTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        //  dpToDate = (DatePicker) dialogDateTime.findViewById(R.id.datePickerToDate);
        close = (ImageView) dialogDateTime.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialogDateTime.findViewById(R.id.btnSubmit);

        dpFromDateEndTime.setMinDate(System.currentTimeMillis() - 1000);
        if (mainFromDateEndDate != null) {
            String[] dateSplit = mainFromDateEndDate.split("-");
            dpFromDateEndTime.init(Integer.parseInt(dateSplit[0]),
                Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDateEndDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearEndDate = year;
                        monthEndDate = monthOfYear + 1;
                        dayEndDate = dayOfMonth;
                    }
                });
        } else {
            dpFromDateEndTime.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDateEndDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        currentYearEndDate = year;
                        monthEndDate = monthOfYear + 1;
                        dayEndDate = dayOfMonth;
                    }
                });
        }

        final TimePicker timePicker = (TimePicker) dialogDateTime.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        //Check api and Set timeaccordingly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hourEndDate);
            timePicker.setMinute(minuteEndDate);
        } else {
            timePicker.setCurrentHour(hourEndDate);
            timePicker.setCurrentMinute(minuteEndDate);
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
                hourEndDate = arg1;
                minuteEndDate = arg2;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDateTime.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                //       toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                if (mainFromDateEndDate != null) {
                    fromDat = CRMAppUtil.getDate(mainFromDateEndDate, "yyyy-MM-dd");
                    fromPostingDateEndDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                    tvEndDateTime.setText(fromPostingDateEndDate
                        + " "
                        + hourEndDate
                        + ":"
                        + minuteEndDate
                        + ":"
                        + "00");
                }
                String splitstartDate = stringArray(tvStartDateTime.getText().toString());
                String[] newstartDate = splitstartDate.split("-");
                String splitEndDate = stringArray(tvEndDateTime.getText().toString());
                String[] newEndDate = splitEndDate.split("-");
                tvEndDateTimeDisplay.setText(CRMAppUtil.getDBDate(
                    CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]),
                        Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]),
                        hourEndDate, minuteEndDate), DATE_FORMAT_TO_DISPLAY));

                Date meetingDate = CRMAppUtil.getDBDateObj(currentYear, month, day, hour, minute);
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
            }
        });

        dialogDateTime.show();
    }

    //select week day
    // selectDateAppointmentEndTime
    // Date picker end
    private void selectWeekDay(final String id) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), DAYS, null, null);
            Disposable disposable = saathiApi.getAllLookup(request)
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
                String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrSelectWeekDay.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapSelectWeekDay = new ArrayAdapter<>(getMainActivity(),
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

    /*
    * lokup days 26
    {
      "Response": [
        {
          "LookUpid": 121,
          "LookUpName": "Days",
          "ParentId": 0,
          "LookupTypeId": 26,
          "LookupType": 26,
          "BusinessUnitId": 3,
          "Sequence": 1,
          "Status": 1,
          "Active": true,
          "SysName": null
        },
        {
          "LookUpid": 165,
          "LookUpName": "Days",
          "ParentId": 0,
          "LookupTypeId": 26,
          "LookupType": 26,
          "BusinessUnitId": 4,
          "Sequence": 1,
          "Status": 1,
          "Active": true,
          "SysName": null
        }
      ],
      "Message": "Process Completed Successfull",
      "Status": true,
      "Erros": []
    }
    * */

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

        String dateArray[] = mainFromDate.split("-");
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
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), ACTIVITYTYPE, null, null);
            Disposable disposable = saathiApi.getAllLookup(request)
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
                String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
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
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            MeetingByIdRequest request =
                new MeetingByIdRequest(meetingId);
            Disposable subscribe = saathiApi.getMeetingById(request)
                .compose(UiModelHelper.applyUiModel())
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
        // mModel.setLeadId(response.getInt(LEADID) == null ? "" : response.getString(LEADID));
        // mModel.setLeadId(String.valueOf(response.getInt(LEADID)));
        mModel.setLeadId(String.valueOf(response.leadId));
        //TODO LEAD ID IS DIFFRENT
        mModel.setMeetingDate(stringOrEmpty(response.meetingDate));
        mModel.setActivityTypeID(stringOrEmpty(response.activityTypeID));
        mModel.setProduct(stringOrEmpty(response.productTypeID));
        //REPEATATIONSTARTON
        mModel.setAppointmentStartTime(stringOrEmpty(response.repeatationStartOn));
        // REPEATATION_END_SON
        mModel.setAppointmentEndTime(stringOrEmpty(response.repeatationEndsOn));

        //String str_repeatEveryWeekCode = response.getString(TAG_REPEATEVERYWEEKCODE);
        mModel.setRepeatEveryWeekCode(
            stringOrEmpty(response.repeatEveryWeekCode)
        );
        //   String str_repeatEveryDayCode = response.getString(TAG_REPEATEVERYDAYCODE);
        mModel.setRepeatEveryDayCode(stringOrEmpty(response.repeatEveryDayCode));

        // String str_ParentID = response.getString(TAG_PARENTID);

        //        String str_repeatationStartOn = response.getString(TAG_REPEATATIONSTARTON);
        mModel.setRepeatOnCode(
            stringOrEmpty(response.repeatOnCode));
        String str_repeatationEndsOn = response.repeatEveryWeekCode;
        String str_AttendeeName = response.attendeeName;

        //REPEATONCODE
        //   String str_repeatOnCode = response.getString((response.isNull(REPEATONCODE)||response.getString(REPEATONCODE) == null) ? "" : response.getString(REPEATONCODE));

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
                    if (str_AttendeeType.trim().equalsIgnoreCase("R")) {
                        AddedAttendeeModel attendTmp =
                            new AddedAttendeeModel(str_AttendeeType, attended, str_AttendeeEmpName,
                                str_AttendeeEmpcode, str_meetingAttendeeCode);
                        addedAttendeeModelList.add(attendTmp);
                        addedAttendeeCount++;
                    } else {
                        //Its special attendee
                        //AddedSplAttendeeModel
                        AddedSplAttendeeModel tempSpAttendee =
                            new AddedSplAttendeeModel(str_AttendeeType, attended,
                                str_AttendeeEmpName, str_AttendeeEmpcode);
                        addedSplAttendeeModelList.add(tempSpAttendee);
                        addedSplAttendeeCount++;
                    }
                }
            }
        }
        tvSplAttendeeCount.setText("(" + String.valueOf(addedSplAttendeeCount) + ")");
        //  tvAttendeeCount.setText("(" + String.valueOf(addedAttendeeCount) + ")");
        setCount(addedAttendeeCount);
        mModel.setMeetingAttendee(addedAttendeeModelList);
        mModel.setSplMeetingAttendee(addedSplAttendeeModelList);
        //        addedAttendeeAdapter.notifyDataSetChanged();
        //        addedSplAttendeeAdapter.notifyDataSetChanged();

        return mModel;
    }

    private void setCallenderDateFrom(MeetingApptModel mModel) {
        //   mainFromDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");

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
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), REPEATMODE, null, null);
            Disposable disposable = saathiApi.getAllLookup(request)
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
                String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrRepeatMode.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapRepeteMode = new ArrayAdapter<>(getMainActivity(),
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
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), WEEKS, null, null);
            Disposable disposable = saathiApi.getAllLookup(request)
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
                String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrSelectWeek.add(
                    new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapSelectWeek = new ArrayAdapter<>(getMainActivity(),
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
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode()
                    + "-"
                    + clientModel.getAttendeeName()
                    + "-"
                    + clientModel.getBranchcode());
                //                holderView.tvAttendeeName.setOnClickListener(new View.OnClickListener() {
                //                    @Override
                //                    public void onClick(View view) {
                //
                //                    }
                //                });
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
                                    }
                                } catch (Exception e) {
                                    modelList.add(EmployeeModel.getNoData(getErrorOnFailure(e)));
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
                inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                holderView.tvAttendeeName.setText(clientModel.getAttendeeCode()
                    + "-"
                    + clientModel.getAttendeeName()
                    + "-"
                    + clientModel.getBranchCode());
              /*  holderView.tvAttendeeName.setOnClickListener(new View.OnClickListener() {
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
                            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                AutoCompleteAttendeeRequest request =
                                    new AutoCompleteAttendeeRequest(
                                        URLEncoder.encode(constraint.toString(), "utf-8"),
                                        URLEncoder.encode(constraint.toString(), "utf-8")
                                    );
                                try {
                                    List<AutoCompleteAttendeeItem> dataList =
                                        saathiApi.getAutoCompleteSplAttendee(request)
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
                                    modelList.add(SplAttendeeModel.getNoData(getErrorOnFailure(e)));
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

            public HolderView(View parent) {
                tvAttendeeName = (TextView) parent.findViewById(R.id.tvAttendeeName);
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
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(
                attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedAttendeeCount--;
                    //  tvAttendeeCount.setText("(" + String.valueOf(addedAttendeeCount) + ")");
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

        public AddedSplAttendeeAdapter(Context c,
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            AddedSplAttendeeAdapter.Holder holder = new AddedSplAttendeeAdapter.Holder();
            view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false);

            AddedSplAttendeeModel attendeeModel = attendeeModelList.get(position);

            holder.tvAttendeeName = (TextView) view.findViewById(R.id.tvAttendeeName);
            holder.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

            holder.tvAttendeeName.setText(
                attendeeModel.getAttendeeEmplyeeCode() + "-" + attendeeModel.getAttendeeName());
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addedSplAttendeeCount--;
                    tvSplAttendeeCount.setText(
                        "(" + String.valueOf(addedSplAttendeeCount) + ")");
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

    //Attende count
    private void setCount(int cnt) {
        if (cnt <= 0) {
            tvAttendeeCount.setText("");
        } else {
            tvAttendeeCount.setText("(" + String.valueOf(cnt) + ")");
        }
    }
}
