package com.mosl.tabapp.calendar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mosl.app.core.data.UiModel;
import com.mosl.saathi.api.model.calender.CalenderItem;
import com.mosl.saathi.api.model.calender.CalenderRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.CalenderModel;
import com.mosl.tabapp.model.LeadActivitiesAppointModel;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.getDateFromString;

/**
 * Created by raju on 3/8/2017.
 */

public class CalendarAppointsListFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View calendarView;
    private TextView tvStartDate, tvNochart;
    private ImageView ivDatePicker;
    private String mainFromDate, mainToDate, fromPostingDate;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;
    private DatePicker dpFromDate;
    private ImageView close;
    private Button btnSubmit;
    private RecyclerView recyclerView;
    private ArrayList<LeadActivitiesAppointModel> calenderList;
    private ClientMeetingAdapter calendarAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_client_meeting);
        setAppTitle(getClass().toString(), "Calendar");
        setUpView(calendarView);
        return calendarView;
    }

    private void setUpView(View calendarView) {
        tvStartDate = (TextView) calendarView.findViewById(R.id.tvDate);
        tvNochart = (TextView) calendarView.findViewById(R.id.tvNochart);
        //  mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        mainToDate = this.getArguments().getString(DATE);
        fromPostingDate = mainToDate;
        mainToDate = getDateFromString(mainToDate, DATE_FORMAT_TO_SERVER_MEETING_SEND, DATE_FORMAT_OF_DD_MMM_YYYY);
        tvStartDate.setText(mainToDate);

        ivDatePicker = (ImageView) calendarView.findViewById(R.id.ivDatePicker);
        ivDatePicker.setOnClickListener(this);
        recyclerView = (RecyclerView) calendarView.findViewById(R.id.recyclerView);
        calenderList = new ArrayList<>();

        for (LeadActivitiesAppointModel list : CalenderModel.getCalenderList()) {
            calenderList.add(new LeadActivitiesAppointModel(list.getStatus(), list.getMeetingid(), list.getLeadid(), list.getName(), list.getEmail(), list.getMobNo(), list.getDate(), list.getTime(), list.getAttendeeEmp()
                    , list.getProduct(), list.getStatusName(), list.getActivityType(), list.getMeetingReason(), list.getMeetingDuration(), list.getCategory(), list.getActionable(), list.getComments(), list.getStage(), list.getCallback(), list.getQueryDesc()));
        }

        calendarAdapter = new ClientMeetingAdapter(calenderList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(calendarAdapter);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivDatePicker) {
            selectDate();
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
        LinearLayout linearLayoutOfToDate = (LinearLayout) dialog.findViewById(R.id.linearLayoutOfToDate);
        linearLayoutOfToDate.setVisibility(View.GONE);
        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        //  dpToDate = (DatePicker) dialog.findViewById(R.id.datePickerToDate);
        close = (ImageView) dialog.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);


        if (fromPostingDate != null) {
            String[] dateSplit = fromPostingDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fromPostingDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                }
            });
        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fromPostingDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                }
            });
        }

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
                fromDat = CRMAppUtil.getDate(fromPostingDate, "yyyy-MM-dd");
                toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                if (fromPostingDate != null) {
                    fromDat = CRMAppUtil.getDate(fromPostingDate, "yyyy-MM-dd");
                    fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                    // fromPostingDate = CRMAppUtil.getDate(fromDat, CAL_DATE_FORMAT);
                    // tvStartDate.setText(fromPostingDate);
                    tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, DATE_FORMAT_OF_DD_MMM_YYYY));
                    appointments(fromPostingDate);
                }
                dialog.dismiss();


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

    private void renderCalender(UiModel<List<CalenderItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
            tvNochart.setVisibility(View.VISIBLE);
            calenderList = new ArrayList<>();
            calendarAdapter = new ClientMeetingAdapter(calenderList);
            RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(calendarAdapter);
            CalenderModel.setCalenderList(calenderList);
        }

        List<CalenderItem> data = uiModel.data;

        if (data != null) {
            calenderList = new ArrayList<>();
            if (data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    CalenderItem item = data.get(i);
                    String status = stringOrEmpty(item.status);
                    String meetingId = stringOrEmpty(item.meetingID);
                    String leadId = stringOrEmpty(item.leadId);
                    String name = stringOrEmpty(item.name);
                    String email = stringOrEmpty(item.email);
                    String mobNo = stringOrEmpty(item.mobNo);
                    String meetingDate = stringOrEmpty(item.meetingDate);
                    String attendeeEmp = stringOrEmpty(item.attendeeEmp);
                    String product = stringOrEmpty(item.product);
                    String statusName = stringOrEmpty(item.statusName);
                    String activityType = stringOrEmpty(item.activityType);
                    String meetingReason = stringOrEmpty(item.meetingReason);
                    String meetingDuration = stringOrEmpty(item.meetingDuration);
                    String category = stringOrEmpty(item.category);
                    String actionable = stringOrEmpty(item.actionable);
                    String comments = stringOrEmpty(item.comments);
                    String stage = stringOrEmpty(item.stage);
                    String customerServiceRemark = stringOrEmpty(item.customerServiceRemark);
                    String queryDesc = stringOrEmpty(item.queryDesc);
                    String dateDisplay = stringOrEmpty(item.dateDisplay);

                    calenderList.add(
                        new LeadActivitiesAppointModel(status,
                            meetingId, leadId, name, email, mobNo,
                            dateDisplay, meetingDate, attendeeEmp
                            , product, statusName, activityType,
                            meetingReason, meetingDuration, category,
                            actionable, comments, stage, customerServiceRemark,
                            queryDesc));
                }

                calendarAdapter = new ClientMeetingAdapter(calenderList);
                RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(calendarAdapter);
                CalenderModel.setCalenderList(calenderList);
                tvNochart.setVisibility(View.GONE);
            } else {
                tvNochart.setVisibility(View.VISIBLE);
                calendarAdapter = new ClientMeetingAdapter(calenderList);
                RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(calendarAdapter);
                CalenderModel.setCalenderList(calenderList);
            }
        }
    }


    private void appointments(String selectionDate) {

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            CalenderRequest request =
                new CalenderRequest(selectionDate, "A", AccountDetails.getEmployeeCode());
            Disposable subscribe = saathiApi.postCalenderByDate(request)
                .compose(applyUiModel())
                .subscribe(this::renderCalender);
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }


        /*if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
//            customProgress.setVisibility(View.VISIBLE);
            *//*{"LeadID":6028,"PageSize":10,"Page":1,"CalculatedPage":0,"TotalCount":0}*//*
            Map inputMap = new HashMap<>();

            inputMap.put("AttendeeCode", AccountDetails.getEmployeeCode());
            inputMap.put("MeetingDate", selectionDate);
            inputMap.put("MeetingType", "A");

            //  String serviceMethod = "Meeting/GetAppointment";
            String serviceMethod = "Meeting/GetMeetingByDate";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
                                    calenderList = new ArrayList<>();

                                    //  JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                    JSONArray dataArray = response.getJSONArray(RESPONSE);
                                   *//* "Status": 1,
                                            "MeetingID": 2004,
                                            "LeadID": 6028,
                                            "Name": "MS Dhoni",
                                            "EMAIL": "msdhoni@gmail.com",
                                            "MOBNO": "8798798797",
                                            "DATE": "2012-12-22",
                                            "TIME": "09:55",
                                            "AttendeeEmp": null,
                                            "Product": "Offline",
                                            "StatusName": "Wrong registration - Survey",
                                            "ActivityType": "Personal Meeting",
                                            "MeetingReason": "Introduction and Customer Profiling",
                                            "MeetingDuration": "15 min",
                                            "Category": null,
                                            "Actionable": null,
                                            "Comments": "Tis si si",
                                            "Stage": "",
                                            "Callback": "",
                                            "QueryDesc": "",*//*

                                    if (dataArray.length() > 0) {
                                        for (int i = 0; i < dataArray.length(); i++) {
                                            JSONObject objDetail = dataArray.getJSONObject(i);
                                            String Status = (objDetail.isNull(STATUS) || objDetail.getString(STATUS).equals("")) ? "" : objDetail.getString(STATUS);
                                            String MeetingID = (objDetail.isNull("MeetingID") || objDetail.getString("MeetingID").equals("")) ? "" : objDetail.getString("MeetingID");
                                            String LeadID = (objDetail.isNull("LeadId") || objDetail.getString("LeadId").equals("")) ? "" : objDetail.getString("LeadId");
                                            String Name = "";
                                            if (objDetail.has(NAME)) {//checking for existeace
                                                Name = (objDetail.isNull(NAME) || objDetail.getString(NAME).equals("")) ? "" : objDetail.getString(NAME);
                                            } else {
                                                Name = "";//TODO REMOVE
                                            }
                                            //  String Name = (objDetail.isNull("Clientname") || objDetail.getString("Clientname").equals("")) ? "" : objDetail.getString("Clientname");
                                            String EMAIL = (objDetail.isNull("EMAIL") || objDetail.getString("EMAIL").equals("")) ? "" : objDetail.getString("EMAIL");
                                            String mobNo = (objDetail.isNull(MOBNO) || objDetail.getString(MOBNO).equals("")) ? "" : objDetail.getString(MOBNO);
                                            //   String date = (objDetail.isNull("DATE") || objDetail.getString("DATE").equals("")) ? "" : objDetail.getString("DATE");
                                            //   "MeetingDate": "2017-09-23T20:07:20",
                                            String time = (objDetail.isNull(MeetingDate) || objDetail.getString(MeetingDate).equals("")) ? "" : objDetail.getString(MeetingDate);
                                            String AttendeeEmp = (objDetail.isNull("AttendeeEmp") || objDetail.getString("AttendeeEmp").equals("")) ? "" : objDetail.getString("AttendeeEmp");
                                            String Product = (objDetail.isNull("Product") || objDetail.getString("Product").equals("")) ? "" : objDetail.getString("Product");
                                            String StatusName = (objDetail.isNull("StatusName") || objDetail.getString("StatusName").equals("")) ? "" : objDetail.getString("StatusName");
                                            String ActivityType = (objDetail.isNull("ActivityType") || objDetail.getString("ActivityType").equals("")) ? "" : objDetail.getString("ActivityType");
                                            String MeetingReason = (objDetail.isNull("MeetingReason") || objDetail.getString("MeetingReason").equals("")) ? "" : objDetail.getString("MeetingReason");
                                            String MeetingDuration = (objDetail.isNull("MeetingDuration") || objDetail.getString("MeetingDuration").equals("")) ? "" : objDetail.getString("MeetingDuration");
                                            String Category = (objDetail.isNull("Category") || objDetail.getString("Category").equals("")) ? "" : objDetail.getString("Category");
                                            String Actionable = (objDetail.isNull("Actionable") || objDetail.getString("Actionable").equals("")) ? "" : objDetail.getString("Actionable");
                                            String Comments = (objDetail.isNull("Comments") || objDetail.getString("Comments").equals("")) ? "" : objDetail.getString("Comments");
                                            String Stage = (objDetail.isNull("Stage") || objDetail.getString("Stage").equals("")) ? "" : objDetail.getString("Stage");
                                            String CSR = (objDetail.isNull("CustomerServiceRemark") || objDetail.getString("CustomerServiceRemark").equals("")) ? "" : objDetail.getString("CustomerServiceRemark");
                                            String QueryDesc = (objDetail.isNull("QueryDesc") || objDetail.getString("QueryDesc").equals("")) ? "" : objDetail.getString("QueryDesc");
                                            String dateDisplay = (objDetail.isNull("DATE_DISPLAY") || objDetail.getString("DATE_DISPLAY").equals("")) ? "" : objDetail.getString("DATE_DISPLAY");


                                            calenderList.add(new LeadActivitiesAppointModel(Status, MeetingID, LeadID, Name, EMAIL, mobNo, dateDisplay, time, AttendeeEmp
                                                    , Product, StatusName, ActivityType, MeetingReason, MeetingDuration, Category, Actionable, Comments, Stage, CSR, QueryDesc));
                                        }

                                        calendarAdapter = new ClientMeetingAdapter(calenderList);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setAdapter(calendarAdapter);
                                        CalenderModel.setCalenderList(calenderList);
                                        tvNochart.setVisibility(View.GONE);
                                    } else {
                                        hideProgress();
                                        tvNochart.setVisibility(View.VISIBLE);
                                        calendarAdapter = new ClientMeetingAdapter(calenderList);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setAdapter(calendarAdapter);

//                                customProgress.setVisibility(View.GONE);
                                    }
                                } else {
                                    hideProgress();
                                    tvNochart.setVisibility(View.VISIBLE);
                                    calenderList = new ArrayList<>();
                                    calendarAdapter = new ClientMeetingAdapter(calenderList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(calendarAdapter);

                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
                                tvNochart.setVisibility(View.VISIBLE);
                                calenderList = new ArrayList<>();
                                calendarAdapter = new ClientMeetingAdapter(calenderList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(calendarAdapter);
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            tvNochart.setVisibility(View.VISIBLE);
                            calenderList = new ArrayList<>();
                            calendarAdapter = new ClientMeetingAdapter(calenderList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(calendarAdapter);
                            e.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
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
                                tvNochart.setVisibility(View.VISIBLE);
                                calenderList = new ArrayList<>();
                                calendarAdapter = new ClientMeetingAdapter(calenderList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(calendarAdapter);
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
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }*/
    }


    public class ClientMeetingAdapter extends RecyclerView.Adapter<ClientMeetingAdapter.MyViewHolder> {

        private List<LeadActivitiesAppointModel> calenderList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView //tvDay,
                    // tvDate,
                    // tvHour,
                    tvClientCode, tvName, tvCompleteDate, tvTime;

            public MyViewHolder(View view) {
                super(view);
                // tvDay = (TextView) view.findViewById(R.id.tvDay);
                //  tvDate = (TextView) view.findViewById(R.id.tvDate);
                //   tvHour = (TextView) view.findViewById(R.id.tvHour);
                tvClientCode = (TextView) view.findViewById(R.id.tvClientCode);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvCompleteDate = (TextView) view.findViewById(R.id.tvCompleteDate);
                tvTime = (TextView) view.findViewById(R.id.tvTime);

            }
        }


        public ClientMeetingAdapter(List<LeadActivitiesAppointModel> calenderList) {
            this.calenderList = calenderList;
        }

        public void addCalenderList(LeadActivitiesAppointModel doc) {
            if (this.calenderList == null) {
                this.calenderList = new ArrayList<>();
            }

            this.calenderList.add(doc);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_client_meeting_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            LeadActivitiesAppointModel clientMeetingModel = calenderList.get(position);
            try {
                // holder.tvDay.setText((clientMeetingModel.getDate()));
                // holder.tvHour.setText("Your schedule's");

                holder.tvClientCode.setText(clientMeetingModel.getLeadid());
                holder.tvName.setText(clientMeetingModel.getName());
                String dateStr = getDateFromString(clientMeetingModel.getTime(), DATE_FORMAT_TO_SERVER_MEETING, "dd-MMM-yyyy");
                holder.tvCompleteDate.setText(dateStr);
                String timeStr = getDateFromString(clientMeetingModel.getTime(), DATE_FORMAT_TO_SERVER_MEETING, "hh:mm a");
                holder.tvTime.setText(timeStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return calenderList.size();
        }
    }
}
