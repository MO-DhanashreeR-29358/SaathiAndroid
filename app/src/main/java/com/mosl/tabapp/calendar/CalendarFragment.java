package com.mosl.tabapp.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.CalenderModel;
import com.mosl.tabapp.model.LeadActivitiesAppointModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 3/9/2017.
 */

public class CalendarFragment extends MOSLCommonFragment implements OnDateSelectedListener {
    private View calendarView;
    // private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private MaterialCalendarView materialCalendarView;

    private TextView tvDay, tvNochart;// tvDate,tvHour;
    private String selectionDate;
    private RecyclerView recyclerView;
    private ArrayList<LeadActivitiesAppointModel> calenderList;
    private CalendarAdapter calendarAdapter;
    Boolean firstLoadDone = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        calendarView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_calendar);
        setAppTitle(getClass().toString(), "Calendar");
        setUpView(calendarView);
        setUserVisibleHint(true);
        return calendarView;
    }

    private void setUpView(View calendarView) {
        tvDay = (TextView) calendarView.findViewById(R.id.tvDay);
        tvNochart = (TextView) calendarView.findViewById(R.id.tvNochart);
        recyclerView = (RecyclerView) calendarView.findViewById(R.id.recyclerView);

        materialCalendarView =
            (MaterialCalendarView) calendarView.findViewById(R.id.materialCalendarView);
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        materialCalendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR) - 1, Calendar.JANUARY, 1);
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        selectionDate = getSelectedDatesString();
        materialCalendarView.addDecorators(new MySelectorDecorator(getActivity()),
            new HighlightMonthsDecorator(),
            oneDayDecorator);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && calendarView != null) {
                if (firstLoadDone) {
                    appointments(selectionDate);
                    firstLoadDone = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
        @Nullable CalendarDay date, boolean selected) {
        // textView.setText(getSelectedDatesString());
        selectionDate = getSelectedDatesString();
        appointments(selectionDate);
        oneDayDecorator.setDate(date.getDate());
        materialCalendarView.invalidateDecorators();
    }

    private String getSelectedDatesString() {
        CalendarDay date = materialCalendarView.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

    private Date getSelectedDate() {
        CalendarDay date = materialCalendarView.getSelectedDate();
        if (date == null) {
            return null;
        }
        return date.getDate();
    }

    private void appointments(String selectionDate) {
        //E MMMM d  -->> Fri September 22
        Date tDate = getSelectedDate();

        tvDay.setText(CRMAppUtil.getDBDate(tDate, "E MMMM d"));
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            //            customProgress.setVisibility(View.VISIBLE);
            /*{"LeadID":6028,"PageSize":10,"Page":1,"CalculatedPage":0,"TotalCount":0}*/
            Map inputMap = new HashMap<>();
            // inputMap.put("LeadID", leadId);
            //            inputMap.put(LEAD_ID, "0");
            //            inputMap.put(UCID, "0");
            //            inputMap.put("PageSize", "10");
            //            inputMap.put("Page", "1");
            //            inputMap.put("TotalCount", "1");

            inputMap.put("AttendeeCode", AccountDetails.getEmployeeCode());
            inputMap.put("MeetingDate", selectionDate);
            inputMap.put("MeetingType", "A");
            /*{
"AttendeeCode":"9179",
"MeetingDate": "10/Jan/2017",
"MeetingType": "M"
}*/

            //String serviceMethod = "Meeting/GetAppointment";
            String serviceMethod = "Meeting/GetMeetingByDate";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    hideProgress();
                                    if (status.equalsIgnoreCase("true")) {
                                        calenderList = new ArrayList<>();

                                        // JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                        JSONArray dataArray = response.getJSONArray(RESPONSE);
                                   /* "MeetingID":931446,
                                            "LeadId":2293589,
                                            "UCID":0,
                                            "MeetingDate":"2017-09-23T20:07:20",
                                            "ActivityTypeID":77,
                                            "ProductTypeID":18,
                                            "MeetingReasonID":0,
                                            "MeetingDurationID":0,
                                            "MeetingDescription":null,
                                            "BAClientMeetingID":0,
                                            "OutstationMeetingID":0,
                                            "Clientcode":null,
                                            "Clientname":null,
                                            "MeetingObjectiveID":0,
                                            "ProductCategoryID":0,
                                            "CustomerRemark":null,
                                            "ActionableID":0,
                                            "CategoryID":0,
                                            "CategoryDescription":null,
                                            "SubCategoryID":0,
                                            "SubCategoryDescription":null,
                                            "MeetingWith":"L",
                                            "AttendeeCode":null,
                                            "AttendeeName":null,
                                            "repeatOnCode":"null",
                                            "repeatEveryWeekCode":"null",
                                            "repeatEveryDayCode":"null",
                                            "repeatationStartOn":"2016-11-30T20:07:20",
                                            "repeatationEndsOn":"2016-11-30T20:07:20",
                                            "MeetingType":"A",
                                            "ParentID":null,
                                            "meetingAttendee":[
                                    {
                                        "MeetingAttendeeID":1459904,
                                            "MeetingID":931446,
                                            "AttendeeEmpcode":"5254",
                                            "AttendeeEmpName":null,
                                            "AttendeeType":"R",
                                            "Attended":true,
                                            "DefaultAttendee":true,
                                            "IsAdminMode":true,
                                            "Status":1,
                                            "InApproval":false,
                                            "ApprovalFieldList":null
                                    }
],
                                    "FinalAction":"A",
                                            "IsAdminMode":true,
                                            "Status":1,
                                            "InApproval":false,
                                            "ApprovalFieldList":null*/

                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject objDetail = dataArray.getJSONObject(i);
                                                String Status = (objDetail.isNull(STATUS)
                                                    || objDetail.getString(STATUS).equals("")) ? ""
                                                    : objDetail.getString(STATUS);
                                                String MeetingID = (objDetail.isNull("MeetingID")
                                                    || objDetail.getString("MeetingID").equals(""))
                                                    ? "" : objDetail.getString("MeetingID");
                                                String LeadID = (objDetail.isNull("LeadId")
                                                    || objDetail.getString("LeadId").equals(""))
                                                    ? "" : objDetail.getString("LeadId");
                                                String Name = "";
                                                if (objDetail.has(NAME)) {//checking for existeace
                                                    Name = (objDetail.isNull(NAME)
                                                        || objDetail.getString(NAME).equals(""))
                                                        ? "" : objDetail.getString(NAME);
                                                } else {
                                                    Name = "";//TODO REMOVE
                                                }
                                                //  String Name = (objDetail.isNull("Clientname") || objDetail.getString("Clientname").equals("")) ? "" : objDetail.getString("Clientname");
                                                String EMAIL = (objDetail.isNull("EMAIL")
                                                    || objDetail.getString("EMAIL").equals("")) ? ""
                                                    : objDetail.getString("EMAIL");
                                                String mobNo =
                                                    (objDetail.isNull(MOBNO) || objDetail.getString(
                                                        MOBNO).equals("")) ? ""
                                                        : objDetail.getString(MOBNO);
                                                //   String date = (objDetail.isNull("DATE") || objDetail.getString("DATE").equals("")) ? "" : objDetail.getString("DATE");
                                                //   "MeetingDate": "2017-09-23T20:07:20",
                                                String time = (objDetail.isNull(MeetingDate)
                                                    || objDetail.getString(MeetingDate).equals(""))
                                                    ? "" : objDetail.getString(MeetingDate);
                                                String AttendeeEmp =
                                                    (objDetail.isNull("AttendeeEmp")
                                                        || objDetail.getString("AttendeeEmp")
                                                        .equals("")) ? ""
                                                        : objDetail.getString("AttendeeEmp");
                                                String Product = (objDetail.isNull("Product")
                                                    || objDetail.getString("Product").equals(""))
                                                    ? "" : objDetail.getString("Product");
                                                String StatusName = (objDetail.isNull("StatusName")
                                                    || objDetail.getString("StatusName").equals(""))
                                                    ? "" : objDetail.getString("StatusName");
                                                String ActivityType =
                                                    (objDetail.isNull("ActivityType")
                                                        || objDetail.getString("ActivityType")
                                                        .equals("")) ? ""
                                                        : objDetail.getString("ActivityType");
                                                String MeetingReason =
                                                    (objDetail.isNull("MeetingReason")
                                                        || objDetail.getString("MeetingReason")
                                                        .equals("")) ? ""
                                                        : objDetail.getString("MeetingReason");
                                                String MeetingDuration =
                                                    (objDetail.isNull("MeetingDuration")
                                                        || objDetail.getString("MeetingDuration")
                                                        .equals("")) ? ""
                                                        : objDetail.getString("MeetingDuration");
                                                String Category = (objDetail.isNull("Category")
                                                    || objDetail.getString("Category").equals(""))
                                                    ? "" : objDetail.getString("Category");
                                                String Actionable = (objDetail.isNull("Actionable")
                                                    || objDetail.getString("Actionable").equals(""))
                                                    ? "" : objDetail.getString("Actionable");
                                                String Comments = (objDetail.isNull("Comments")
                                                    || objDetail.getString("Comments").equals(""))
                                                    ? "" : objDetail.getString("Comments");
                                                String Stage = (objDetail.isNull("Stage")
                                                    || objDetail.getString("Stage").equals("")) ? ""
                                                    : objDetail.getString("Stage");
                                                String CSR =
                                                    (objDetail.isNull("CustomerServiceRemark")
                                                        || objDetail.getString(
                                                        "CustomerServiceRemark").equals("")) ? ""
                                                        : objDetail.getString(
                                                            "CustomerServiceRemark");
                                                String QueryDesc = (objDetail.isNull("QueryDesc")
                                                    || objDetail.getString("QueryDesc").equals(""))
                                                    ? "" : objDetail.getString("QueryDesc");
                                                String dateDisplay =
                                                    (objDetail.isNull("DATE_DISPLAY")
                                                        || objDetail.getString("DATE_DISPLAY")
                                                        .equals("")) ? ""
                                                        : objDetail.getString("DATE_DISPLAY");

                                                calenderList.add(
                                                    new LeadActivitiesAppointModel(Status,
                                                        MeetingID, LeadID, Name, EMAIL, mobNo,
                                                        dateDisplay, time, AttendeeEmp
                                                        , Product, StatusName, ActivityType,
                                                        MeetingReason, MeetingDuration, Category,
                                                        Actionable, Comments, Stage, CSR,
                                                        QueryDesc));
                                            }

                                            calendarAdapter = new CalendarAdapter(calenderList);
                                            RecyclerView.LayoutManager mLayoutManager =
                                                new LinearLayoutManager(getActivity());
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            recyclerView.setAdapter(calendarAdapter);
                                            CalenderModel.setCalenderList(calenderList);
                                            tvNochart.setVisibility(View.GONE);
                                        } else {
                                            tvNochart.setVisibility(View.VISIBLE);
                                            calendarAdapter = new CalendarAdapter(calenderList);
                                            RecyclerView.LayoutManager mLayoutManager =
                                                new LinearLayoutManager(getActivity());
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            recyclerView.setAdapter(calendarAdapter);
                                            CalenderModel.setCalenderList(calenderList);
                                        }
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                        tvNochart.setVisibility(View.VISIBLE);
                                        calenderList = new ArrayList<>();
                                        calendarAdapter = new CalendarAdapter(calenderList);
                                        RecyclerView.LayoutManager mLayoutManager =
                                            new LinearLayoutManager(getActivity());
                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setAdapter(calendarAdapter);
                                        CalenderModel.setCalenderList(calenderList);
                                    }
                                } else {
                                    hideProgress();
                                    tvNochart.setVisibility(View.VISIBLE);
                                    calenderList = new ArrayList<>();
                                    calendarAdapter = new CalendarAdapter(calenderList);
                                    RecyclerView.LayoutManager mLayoutManager =
                                        new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(calendarAdapter);
                                    CalenderModel.setCalenderList(calenderList);
                                    //                                customProgress.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                tvNochart.setVisibility(View.VISIBLE);
                                calenderList = new ArrayList<>();
                                calendarAdapter = new CalendarAdapter(calenderList);
                                RecyclerView.LayoutManager mLayoutManager =
                                    new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(calendarAdapter);
                                CalenderModel.setCalenderList(calenderList);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                            //                        customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                            //                        customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
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
                                    tvNochart.setVisibility(View.VISIBLE);
                                    calenderList = new ArrayList<>();
                                    calendarAdapter = new CalendarAdapter(calenderList);
                                    RecyclerView.LayoutManager mLayoutManager =
                                        new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(calendarAdapter);
                                    CalenderModel.setCalenderList(calenderList);
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
                            //                        customProgress.setVisibility(View.GONE);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            //                        customProgress.setVisibility(View.GONE);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

        private List<LeadActivitiesAppointModel> calenderList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvTime;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
                linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            }
        }

        public CalendarAdapter(List<LeadActivitiesAppointModel> calenderList) {
            this.calenderList = calenderList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_calendar, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final LeadActivitiesAppointModel calendarModel = calenderList.get(position);
            holder.tvName.setText(calendarModel.getName());
            // holder.tvTime.setText(calendarModel.getTime());
            String dateStr =
                CRMAppUtil.getDateFromString(calendarModel.getTime(), DATE_FORMAT_TO_SERVER_MEETING,
                    "hh:mm a");
            holder.tvTime.setText(dateStr);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(DATE, selectionDate);
                    args.putString(NAME, calendarModel.getName());
                    args.putString(TIME, calendarModel.getTime());
                    navigateTo(NAV_TO_CLIENT_MEETING, args, getFragmentManager(), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return calenderList.size();
        }
    }
}
